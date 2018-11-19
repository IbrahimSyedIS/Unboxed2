package com.papayaman.unboxed;

import java.io.Serializable;

/**
 * Represents a garage sale
 * Contains information like date, address, name of seller, etc.
 */

public class Sale implements Serializable {

    private static final long serialVersionUID = 10001L;

    private final double lat;
    private final double lng;

    private int month, dayOfMonth, year;

    public Sale(double lat, double lng, int month, int dayOfMonth, int year) {
        this.lat = lat;
        this.lng = lng;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getYear() {
        return year;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String toString() {
        return (month + 1) + "/" + dayOfMonth + "/" + year + " at (" + lat + ", " + lng + ")";
    }

}
