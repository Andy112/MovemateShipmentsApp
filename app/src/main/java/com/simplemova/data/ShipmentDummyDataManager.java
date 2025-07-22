package com.simplemova.data;

import static com.simplemova.activity.extra.SimpleMovaApp.getAppContext;
import static com.simplemova.util.WidgetUtil.getStatus;

import com.simplemova.R;
import com.simplemova.model.Shipment;
import com.simplemova.model.ShipmentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShipmentDummyDataManager {

    private final List<Shipment> filteredShipments;
    private final List<ShipmentStatus> shipmentStatuses;
    private final List<Shipment> allShipments;
    private final Random randomNumberGenerator;
    private final int noOfShipments;

    public ShipmentDummyDataManager() {
        randomNumberGenerator = new Random();
        noOfShipments = generateRandomTotalNoOfShipments();
        filteredShipments = new ArrayList<>();
        shipmentStatuses = generateShipmentStatuses();
        allShipments = generateDummyShipments();
        generateShipmentCountsForStatuses();
    }

    private int generateRandomTotalNoOfShipments() {
        int i = randomNumberGenerator.nextInt(50);
        if (i < 15) return generateRandomTotalNoOfShipments();
        return i;
    }

    public List<ShipmentStatus> getShipmentStatuses() {
        return shipmentStatuses;
    }

    public List<ShipmentStatus> generateShipmentStatuses() {
        List<ShipmentStatus> shipmentStatuses = new ArrayList<>();
        shipmentStatuses.add(new ShipmentStatus(getStatus(R.string.all), 0));
        shipmentStatuses.add(new ShipmentStatus(getStatus(R.string.completed), R.drawable.check_18px));
        shipmentStatuses.add(new ShipmentStatus(getStatus(R.string.in_progress), R.drawable.status_in_progress_18px));
        shipmentStatuses.add(new ShipmentStatus(getStatus(R.string.pending), R.drawable.status_pending_18px));
        shipmentStatuses.add(new ShipmentStatus(getStatus(R.string.loading), R.drawable.status_loading_18px));
        shipmentStatuses.add(new ShipmentStatus(getStatus(R.string.cancelled), R.drawable.status_pending_18px));
        return shipmentStatuses;
    }

    public List<Shipment> generateDummyShipments() {
        List<Shipment> shipments = new ArrayList<>();
        for (int i = 0; i < noOfShipments; i++) {
            ShipmentStatus status = getRandomShipmentStatus();
            shipments.add(new Shipment(status.title, status.iconRes));
        }
        return shipments;
    }

    private ShipmentStatus getRandomShipmentStatus() {
        int index = 1 + randomNumberGenerator.nextInt(5);
        return getShipmentStatuses().get(index);
    }

    private boolean isStatusAll(String status) {
        return getStatus(R.string.all).equals(status);
    }

    public void generateShipmentCountsForStatuses() {
        for (ShipmentStatus status : shipmentStatuses) status.clearShipmentCount();
        for (Shipment shipment : allShipments) {
            for (ShipmentStatus status : shipmentStatuses) {
                if (isStatusAll(status.title) || shipment.isSameStatus(status.title)) {
                    status.addShipmentCount();
                }
            }
        }
    }

    public List<Shipment> sortAndGetShipmentsByStatus(String status) {
        filteredShipments.clear();
        if (status.equals(getAppContext().getString(R.string.all))) {
            filteredShipments.addAll(allShipments);
        } else {
            for (Shipment shipment : allShipments) {
                if (shipment.isSameStatus(status)) {
                    filteredShipments.add(shipment);
                }
            }
        }
        return filteredShipments;
    }
}