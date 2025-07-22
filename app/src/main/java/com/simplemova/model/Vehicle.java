package com.simplemova.model;

public class Vehicle {

    private final String name;
    private final String type;
    private final int imageRes;

    public Vehicle(String name, String type, int imageRes) {
        this.name = name;
        this.type = type;
        this.imageRes = imageRes;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getImageRes() {
        return imageRes;
    }
}