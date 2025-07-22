package com.simplemova.util;

import java.util.Random;

public class Util {

    private static final Random RANDOM = new Random();

    public static boolean performSimpleSearch(String val, String searchValue) {
        return val != null && val.toLowerCase().trim().contains(searchValue);
    }

    public static String generateId() {
        return "#NEJ" + (RANDOM.nextInt(1000000000));
    }
}
