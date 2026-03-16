package com.example.model;

public class Bill {

    private String room;
    private String tenant;
    private double rent;
    private double water;
    private int electric;

    public Bill() {}

    public Bill(String room, String tenant, double rent, double water, int electric) {
        this.room = room;
        this.tenant = tenant;
        this.rent = rent;
        this.water = water;
        this.electric = electric;
    }

    public String getRoom() { return room; }
    public String getTenant() { return tenant; }
    public double getRent() { return rent; }
    public double getWater() { return water; }
    public int getElectric() { return electric; }

    public double getElectricCost(){
        return electric * 8;
    }

    public double getTotal(){
        return rent + water + getElectricCost();
    }
}