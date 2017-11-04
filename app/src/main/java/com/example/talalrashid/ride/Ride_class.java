package com.example.talalrashid.ride;

import java.io.Serializable;

/**
 * Created by Hamza Imtiaz on 10/19/2017.
 */

public class Ride_class implements Serializable {
String source_name;
    String destination_name;

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public void setDestination_name(String destination_name) {
        this.destination_name = destination_name;
    }

    public Ride_class(String source_name, String destination_name) {

        this.source_name = source_name;
        this.destination_name = destination_name;
    }
}