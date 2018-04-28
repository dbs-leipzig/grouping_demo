package org.gradoop.demo.server;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * <p>Represents a store of graphsets that are available to explore using Gradoop and that are stored in HDFS.</p>
 */
public class HdfsGradoopGraphsetStore extends Configured implements GradoopGraphsetStore {

    public static final String DEFAULT_BASE_PATH = "/app/ugraph/gradoop-graphsets/";
    private static final Logger log = LoggerFactory.getLogger(HdfsGradoopGraphsetStore.class);

    private final String basePath; // must start with HDFS_PREF
    private final URI clusterUri;

    public HdfsGradoopGraphsetStore(URI clusterUri) {
        this(clusterUri, DEFAULT_BASE_PATH);
    }

    public HdfsGradoopGraphsetStore(URI clusterUri, String basePath) {
        requireNonNull(clusterUri);
        requireNonNull(basePath);
        this.clusterUri = clusterUri;
        this.basePath = basePath;
    }

    @Override
    public Set<String> getDataSourceNames() throws IOException {
        Path basePath = new Path(this.clusterUri + this.basePath);
        FileSystem fs = basePath.getFileSystem(new Configuration(true));
//        if (!fs.getFileStatus(basePath).isDirectory()) {
//            throw new RuntimeException("path is not a directory: " + basePath);
//        }
        Set<String> dataSources = new HashSet<>(16); // expected to be fewer than this
        try {
            RemoteIterator<LocatedFileStatus> iter = fs.listFiles(basePath, true);
            while (iter.hasNext()) {
                LocatedFileStatus fstat = iter.next();
                String name = getGradoopGraphsetName(fstat.getPath().toString());
                if (name != null) {
                    dataSources.add(name); // return value ignored
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
        String prefix = clusterUri + basePath;
        String rem = path.substring(prefix.length());
        int fsi = rem.indexOf('/'); // index of first slash in rem e.g. "one/foo"
        if (fsi < 0) {
            return null;
        }
        String folder = rem.substring(0, fsi);
        if (rem.length() - folder.length() > 1) {
            return folder;
        }
        return null;
    }
}
