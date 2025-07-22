package com.simplemova.model;

import com.simplemova.util.Util;

public class Parcel {

    private String id;
    private String name;
    private String from;
    private String to;
    private final String searchKeyWords;

    public Parcel(String name, String from, String to) {
        this.id = Util.generateId();
        this.name = name;
        this.from = from;
        this.to = to;
        this.searchKeyWords = id + " " + name + " " + " " + from + " " + to;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return id + " " + from + " -> " + to;
    }

    public String getSearchKeyWords() {
        return searchKeyWords;
    }
}
