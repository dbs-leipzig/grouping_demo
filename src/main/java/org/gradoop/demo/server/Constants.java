package org.gradoop.demo.server;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.unmodifiableSet;

public final class Constants {
    private Constants() {}

    public static final Set<String> BUNDLED_DATABASE_NAMES =
        unmodifiableSet(newHashSet("Pokec_Sample", "Graphalytics_SF1_Sample", "Example"));
}
