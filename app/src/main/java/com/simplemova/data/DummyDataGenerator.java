package com.simplemova.data;

import com.simplemova.R;
import com.simplemova.model.Parcel;
import com.simplemova.model.Vehicle;

import java.util.Arrays;
import java.util.List;

public class DummyDataGenerator {

    public static List<Vehicle> getAvailableVehicles() {
        return Arrays.asList(new Vehicle("Ocean freight", "International", R.drawable.freight_ocean),
                new Vehicle("Cargo freight", "Reliable", R.drawable.cargo_freight),
                new Vehicle("Air freight", "International", R.drawable.air_freight),
                new Vehicle("Train freight", "Multi Service", R.drawable.train_freight),
                new Vehicle("Drone freight", "Live", R.drawable.drone_freight),
                new Vehicle("Instant freight", "Local", R.drawable.instant_frieght),
                new Vehicle("Road freight", "Local", R.drawable.road_freight));
    }

    public static List<Parcel> getAllParcels() {
        return Arrays.asList(new Parcel("Macbook pro M2", "Paris", "Morocco"),
                new Parcel("Summer linen jacket", "Barcelona", "Paris"),
                new Parcel("Tapered-fit jeans AW", "Columbia", "Paris"),
                new Parcel("Slim fit jeans AW", "Bogota", "Dhaka"),
                new Parcel("Office setup desk", "France", "German"));
    }
}
