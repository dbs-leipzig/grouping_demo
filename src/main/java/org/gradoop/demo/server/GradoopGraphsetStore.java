package org.gradoop.demo.server;

import org.gradoop.flink.io.api.DataSource;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>Represents a Store for Graphsets.</p>
 */
interface GradoopGraphsetStore {
    /**
     * Returns a {@linkplain List} of names of {@linkplain DataSource}s that are available for exploration. A DataSource
     * could be a set of files, for instance, if the DataSource is a {@linkplain org.gradoop.flink.io.impl.csv.CSVDataSource},
     * then it is a folder with three files: <code>vertices.csv, edges.csv, and metadata.csv</code>.
     */
    Set<String> getDataSourceNames();

    /**
     * Returns the absolute path of the file that represent this data source. Remember that every data source
     * is a
     * @param dataSourceName
     * @return
     */
    String getPath(String dataSourceName);

    /**
     * <p> Updates the Gradoop Graphsets from remote (HDFS, e.g.) location. It returns a list of strings
     * indicating the status of the refresh operation. A graphset that is available locally is fetched again
     * when this operation is done.
     * </p>
     * <p>
     *     Details:
     *     <ul>
     *        <li>Get the set of locally available graphsets (A)</li>
     *        <li>Get the set of graphsets available in HDFS (B)</li>
     *        <li>For each graphset b in B, remove it from A, (re) fetch it from HDFS, set its status as
     *        {@linkplain FetchStatus.Status#FETCHED_FROM_HDFS}, add the status to the return set (C)</li>
     *        <li>For each graphset a in A, set a status {@linkplain FetchStatus.Status#PRESENT_LOCALLY}</li>
     *        <li>Return C</li>
     *     </ul>
     * </p>
     */
    Set<FetchStatus> refresh() throws IOException;

    /** Decides whether this store is "local-only" */
    boolean isLocal();
}
