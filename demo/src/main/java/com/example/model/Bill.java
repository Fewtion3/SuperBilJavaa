package com.example.model;

public class Bill {

    private String room;
    private String tenant;
    private double rent;

    // unit
    private double waterUnit;
    private double electricUnit;

    // rate
    private static final double WATER_RATE = 5;
    private static final double ELECTRIC_RATE = 8;

    public Bill(){}

    public Bill(String room,String tenant,double rent,double waterUnit,double electricUnit){
        this.room = room;
        this.tenant = tenant;
        this.rent = rent;
        this.waterUnit = waterUnit;
        this.electricUnit = electricUnit;
    }

    public String getRoom(){
        return room;
    }

    public String getTenant(){
        return tenant;
    }

    public double getRent(){
        return rent;
    }

    public double getWaterUnit(){
        return waterUnit;
    }

    public double getElectricUnit(){
        return electricUnit;
    }

    // ค่าน้ำ
    public double getWaterCost(){
        return waterUnit * WATER_RATE;
    }

    // ค่าไฟ
    public double getElectricCost(){
        return electricUnit * ELECTRIC_RATE;
    }

    // รวมทั้งหมด
    public double getTotal(){
        return rent + getWaterCost() + getElectricCost();
    }

    public void setRoom(String room){
        this.room = room;
    }

    public void setTenant(String tenant){
        this.tenant = tenant;
    }

    public void setRent(double rent){
        this.rent = rent;
    }

    public void setWaterUnit(double waterUnit){
        this.waterUnit = waterUnit;
    }

    public void setElectricUnit(double electricUnit){
        this.electricUnit = electricUnit;
    }
}