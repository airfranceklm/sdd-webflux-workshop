package com.afkl.tecc.lopi.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PagedResponse<T> {

    private final List<T> results;
    private final int size;
    private final int cursor;

    @JsonCreator
    public PagedResponse(@JsonProperty("results") List<T> results,
                         @JsonProperty("size") int size,
                         @JsonProperty("cursor") int cursor) {
        this.results = results;
        this.size = size;
        this.cursor = cursor;
    }

    public List<T> getResults() {
        return results;
    }

    public int getSize() {
        return size;
    }

    public int getCursor() {
        return cursor;
    }
}
