package org.gradoop.demo.server;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static org.gradoop.demo.server.Constants.GRADOOP_FILE_NAMES;

/**
 * <p>Represents a store of graphsets that are available to explore using Gradoop and that are stored in HDFS.</p>
 */
public class HdfsGradoopGraphsetStore extends Configured {

    public static final String DEFAULT_BASE_PATH_IN_HDFS = "/app/ugraph/gradoop-graphsets/";
    private static final Logger log = LoggerFactory.getLogger(HdfsGradoopGraphsetStore.class);

    private final String basePath;
    private final URI clusterUri;
    private final Configuration config;

    public HdfsGradoopGraphsetStore(URI clusterUri) {
        this(clusterUri, DEFAULT_BASE_PATH_IN_HDFS);
    }

    public HdfsGradoopGraphsetStore(URI clusterUri, String basePath) {
        requireNonNull(clusterUri);
        requireNonNull(basePath);
        this.clusterUri = clusterUri;
        this.basePath = basePath;
        this.config = new HdfsConfiguration(true);
        config.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        config.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        config.set("fs.default.name", clusterUri.toString());
    }

    public Set<String> getDataSourceNames() throws IOException {
        Path basePath = new Path(this.basePath);
        FileSystem fs = basePath.getFileSystem(config);
//        if (!fs.getFileStatus(basePath).isDirectory()) {
//            throw new RuntimeException("path is not a directory: " + basePath);
//        }
        Set<String> dataSources = new HashSet<>(16); // expected to be fewer than this
        try {
            RemoteIterator<LocatedFileStatus> iter = fs.listFiles(basePath, true);
            while (iter.hasNext()) {
                LocatedFileStatus fstat = iter.next();
                String name = getGradoopGraphsetName(fstat.getPath().toUri().getPath());
                if (name != null && ! dataSources.contains(name)) {
                    boolean added = dataSources.add(name);
                    assert added;
                    log.debug("adding graphset: " + name);
                }
            }
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (fs != null) {
                fs.close();
            }
        }
        return dataSources;
    }

    private String getGradoopGraphsetName(String path) {
        //TODO improve this logic
        String prefix = basePath;
        String rem = path.substring(prefix.length());
        int fsi = rem.indexOf('/'); // index of first slash in rem e.g. "one/foo"
        if (fsi < 0) {
            log.debug("Returning null, this is not a gradoop graphset: " + path);
            return null;
        }
        String folder = rem.substring(0, fsi);
        if (rem.length() - folder.length() > 1) {
            return folder;
        }
        log.debug("Returning null, this is not a gradoop graphset: " + path);
        return null;
    }

    /* package-private */void copyGradoopFiles(String graphsetName, File localBase) throws Exception {
        // copying all the files is critical, so make it an all-or-nothing operations, cleaning up
        // however, transactional semantics are not intended here
        Path basePath = new Path(this.basePath);
        FileSystem fs = basePath.getFileSystem(config);
        File localGraphsetFolder = new File(localBase, graphsetName);
        boolean a = localGraphsetFolder.mkdirs();
        if (! a) {
            throw new RuntimeException("Unlikely error in making the local folder: " + localGraphsetFolder);
        }
        Path localPath = new Path(localGraphsetFolder.getAbsolutePath());
        try {
            for (String f : GRADOOP_FILE_NAMES) {
                Path remotePath = new Path(this.basePath + "/" + graphsetName + "/" + f);
                fs.copyToLocalFile(false, remotePath, localPath);
                log.info("Copied {} to {}", remotePath.toString(), localPath);
                File gf = new File(localPath.toString(), f);
                log.debug("File {} -- is it copied? : {}", gf.getAbsolutePath(), gf.exists());
            }
        } catch (Exception e) {
            log.warn("Error copying graphset: {}, deleting the whole folder: " + e.getMessage());
            FileUtil.fullyDelete(localGraphsetFolder);
            throw e; //rethrow
        }
    }
}
