package com.example.model;

public class Tenant {
    private String id;
    private String name;
    private String phone;
    private String roomId; // nullable
    private long moveInEpochMs;

    public Tenant() {}

    public Tenant(String id, String name, String phone, String roomId, long moveInEpochMs) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.roomId = roomId;
        this.moveInEpochMs = moveInEpochMs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public long getMoveInEpochMs() {
        return moveInEpochMs;
    }

    public void setMoveInEpochMs(long moveInEpochMs) {
        this.moveInEpochMs = moveInEpochMs;
    }
}

