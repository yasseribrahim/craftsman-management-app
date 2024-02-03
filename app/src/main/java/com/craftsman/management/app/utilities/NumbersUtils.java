package com.craftsman.management.app.utilities;

public class NumbersUtils {
    public static double round(float value, int places) {
        return  Math.round(value * Math.pow(10, places)) / Math.pow(10, places);
    }
}