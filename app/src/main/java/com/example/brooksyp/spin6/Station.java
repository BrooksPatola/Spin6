package com.example.brooksyp.spin6;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brooksyp on 2017-03-11.
 */

public class Station implements Serializable {

    private double latitude;
    private double longitude;
    private String name;
    private int bikes;
    private int docks;
    static List<Station> stations = new ArrayList<Station>();

    public Station(String name, int bikes, int docks, double latitude, double longitude){

        this.name = name;
        this.bikes = bikes;
        this.docks = docks;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public void setBikes(int bikes) {this.bikes = bikes;}

    public void setDocks(int docks) {this.docks = docks;}

    public void setLatitude(double latitude){this.latitude = latitude;}

    public void setLongtitude(double longitude) {this.longitude = longitude;}

    public int getBikes() {return bikes;}

    public double getLatitude(){return latitude;}

    public double getLongtitude() {return longitude;}

    public int getDocks() {return docks;}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public static void populateStations(List<Station> stationList) {
        stations = stationList;
    }

}