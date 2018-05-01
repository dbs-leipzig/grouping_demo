package org.gradoop.demo.server;

import org.junit.Ignore;

@Ignore
public class HdfsGradoopGraphsetStoreBasicTest {

//    private MiniDFSCluster cluster;
//    @Before
//    public void setUp() throws Exception {
//        org.apache.log4j.Logger.getLogger("org.apache").setLevel(Level.ERROR);
//        org.apache.log4j.Logger.getLogger("BlockStateChange").setLevel(Level.ERROR);
//        Configuration conf = new HdfsConfiguration();
//        File baseDir = new File("./target/hdfs/").getAbsoluteFile();
//        FileUtil.fullyDelete(baseDir);
//        conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, baseDir.getAbsolutePath());
//        MiniDFSCluster.Builder builder = new MiniDFSCluster.Builder(conf);
//        cluster = builder.build();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        if (cluster != null) {
//            cluster.shutdown();
//        }
//    }
//    @Test
//    public void getDataSourceNames() throws IOException {
////        System.out.println("Cluster URI: " + cluster.getFileSystem().getUri());
//        DistributedFileSystem fs = cluster.getFileSystem();
//        fs.mkdirs(new Path(DEFAULT_BASE_PATH + "/some-file-not-a-folder"));
//        DFSTestUtil.createFile(fs, new Path(DEFAULT_BASE_PATH + "/not.a.set"), 100L, (short) 3, 123L);
//        createGradoopSet(fs, "foo", "bar", "baz", "longone");
//        DFSTestUtil.createFile(fs, new Path(DEFAULT_BASE_PATH + "/another-file.txt"), 100L, (short) 3, 123L);
//        HdfsGradoopGraphsetStore store = new HdfsGradoopGraphsetStore(cluster.getURI());
//        Set<String> names = store.getDataSourceNames();
//        assertEquals("only three elements", 4L, names.size());
//        assertTrue("set contains foo", names.contains("foo"));
//        assertTrue("set contains bar", names.contains("bar"));
//        assertTrue("set contains baz", names.contains("baz"));
//        assertTrue("set contains longone", names.contains("longone"));
//    }
//
//    private void createGradoopSet(DistributedFileSystem fs, String... folders) throws IOException {
//        for (String folder : folders) {
//            DFSTestUtil.createFile(fs, new Path(DEFAULT_BASE_PATH + "/" + folder + "/vertices.csv"), 100L, (short) 3, 123L);
//            DFSTestUtil.createFile(fs, new Path(DEFAULT_BASE_PATH + "/" + folder + "/edges.csv"), 100L, (short) 3, 123L);
//            DFSTestUtil.createFile(fs, new Path(DEFAULT_BASE_PATH + "/" + folder + "/metadata.csv"), 100L, (short) 3, 123L);
//        }
//    }
}