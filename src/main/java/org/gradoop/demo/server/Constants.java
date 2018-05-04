package org.gradoop.demo.server;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.unmodifiableSet;

public final class Constants {
    static final Set<String> BUNDLED_DATABASE_NAMES =
        unmodifiableSet(newHashSet("Pokec_Sample", "Graphalytics_SF1_Sample", "Example"));

    static final Set<String> GRADOOP_FILE_NAMES =
        unmodifiableSet(newHashSet("metadata.csv", "vertices.csv", "edges.csv"));

    private Constants() {
    }
}
