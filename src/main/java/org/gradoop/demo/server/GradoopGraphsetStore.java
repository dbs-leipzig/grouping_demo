package org.gradoop.demo.server;

import org.gradoop.flink.io.api.DataSource;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>Represents a Store for Graphsets.</p>
 */
public interface GradoopGraphsetStore {
    /**
     * Returns a {@linkplain List} of names of {@linkplain DataSource}s that are available for exploration. A DataSource
     * could be a set of files, for instance, if the DataSource is a {@linkplain org.gradoop.flink.io.impl.csv.CSVDataSource},
     * then it is a folder with three files: <code>vertices.csv, edges.csv, and metadata.csv</code>.
     */
    Set<String> getDataSourceNames() throws IOException;
}
