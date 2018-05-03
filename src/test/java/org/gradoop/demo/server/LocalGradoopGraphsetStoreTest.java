package org.gradoop.demo.server;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import static org.gradoop.demo.server.Constants.BUNDLED_DATABASE_NAMES;
import static org.junit.Assert.*;

public class LocalGradoopGraphsetStoreTest {

    @Test
    public void getDataSourceNames() throws URISyntaxException {
        String path = RequestHandler.class.getResource("/data/").getPath();
        LocalGradoopGraphsetStore store = new LocalGradoopGraphsetStore(new URI("hdfs://locahost:1234"), path);
        Set<String> names = store.getDataSourceNames();
        assertTrue("there are at least bundled samples", BUNDLED_DATABASE_NAMES.size() >= names.size());
        for (String name : names) {
            assertTrue(BUNDLED_DATABASE_NAMES.contains(name));
        }
    }
}