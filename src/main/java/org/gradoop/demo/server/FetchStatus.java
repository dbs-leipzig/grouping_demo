package org.gradoop.demo.server;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

final class FetchStatus {
    enum Status {
        FETCH_ERROR,
        FETCHED_FROM_HDFS,
        PRESENT_LOCALLY
    }
    private final String name;
    private final Status status;

    FetchStatus(String name, Status status) {
        requireNonNull(name);
        requireNonNull(status);
        this.name = name;
        this.status = status;
    }
    String getName() {
        return name;
    }
    Status getStatus() {
        return status;
    }
    @Override
    public String toString() {
        return "[name: " + name + ", status: " + status + "]";
    }
}