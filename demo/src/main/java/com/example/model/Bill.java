package com.example.model;

public class Bill {
    private String id;
    private String billingMonth; // yyyy-MM

    private String roomId;   // nullable
    private String tenantId; // nullable

    // denormalized fallbacks for display/migration
    private String roomNumber;
    private String tenantName;

    private double rent;
    private double waterUnit;
    private double electricUnit;

    private boolean paid;
    private long createdAtEpochMs;

    private static final double WATER_RATE = 5.0;
    private static final double ELECTRIC_RATE = 8.0;

    public Bill() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public double getWaterUnit() {
        return waterUnit;
    }

    public void setWaterUnit(double waterUnit) {
        this.waterUnit = waterUnit;
    }

    public double getElectricUnit() {
        return electricUnit;
    }

    public void setElectricUnit(double electricUnit) {
        this.electricUnit = electricUnit;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public long getCreatedAtEpochMs() {
        return createdAtEpochMs;
    }

    public void setCreatedAtEpochMs(long createdAtEpochMs) {
        this.createdAtEpochMs = createdAtEpochMs;
    }

    public static double getWaterRate() {
        return WATER_RATE;
    }

    public static double getElectricRate() {
        return ELECTRIC_RATE;
    }

    public double getWaterCost() {
        return waterUnit * WATER_RATE;
    }

    public double getElectricCost() {
        return electricUnit * ELECTRIC_RATE;
    }

    public double getTotal() {
        return rent + getWaterCost() + getElectricCost();
    }
}