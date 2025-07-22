package com.simplemova.model;

public class Shipment {

    private String status;
    private int iconRes;

    public Shipment(String status, int iconRes) {
        this.status = status;
        this.iconRes = iconRes;
    }

    public String getStatus() {
        return status;
    }

    public int getIconRes() {
        return iconRes;
    }

    public boolean isSameStatus(String status) {
        return status.equals(getStatus());
    }
}