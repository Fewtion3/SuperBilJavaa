package com.example.model;

public class Room {
    private String id;
    private String number;
    private double defaultRent;
    private String notes;

    public Room() {}

    public Room(String id, String number, double defaultRent, String notes) {
        this.id = id;
        this.number = number;
        this.defaultRent = defaultRent;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getDefaultRent() {
        return defaultRent;
    }

    public void setDefaultRent(double defaultRent) {
        this.defaultRent = defaultRent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

