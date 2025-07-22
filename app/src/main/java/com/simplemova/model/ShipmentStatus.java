package com.simplemova.model;

public class ShipmentStatus {

    public final int iconRes;
    public final String title;
    private int shipmentCount;

    public ShipmentStatus(String title, int iconRes) {
        this.title = title;
        this.iconRes = iconRes;
        this.shipmentCount = 0;
    }

    public void clearShipmentCount() { shipmentCount = 0; }

    public void addShipmentCount() { shipmentCount++; }

    public String getShipmentCountStr() { return String.valueOf(shipmentCount); }
}
