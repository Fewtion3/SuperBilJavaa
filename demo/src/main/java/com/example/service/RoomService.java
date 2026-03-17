package com.example.service;

import com.example.database.JsonDatabase;
import com.example.model.Room;
import com.example.model.Tenant;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoomService {
    private final JsonDatabase.State state;

    public RoomService(JsonDatabase.State state) {
        this.state = state;
    }

    public List<Room> getRooms() {
        return state.getRooms();
    }

    public Room addRoom(String number, double defaultRent, String notes) {
        Room r = new Room(UUID.randomUUID().toString(), number, defaultRent, notes);
        state.getRooms().add(r);
        state.getRooms().sort(Comparator.comparing(a -> a.getNumber() == null ? "" : a.getNumber()));
        JsonDatabase.saveState(state);
        return r;
    }

    public void updateRoom(Room room, String number, double defaultRent, String notes) {
        room.setNumber(number);
        room.setDefaultRent(defaultRent);
        room.setNotes(notes);
        JsonDatabase.saveState(state);
    }

    public void deleteRoom(Room room, List<Tenant> tenants) {
        // Unassign tenants referencing this room
        for (Tenant t : tenants) {
            if (room.getId() != null && room.getId().equals(t.getRoomId())) {
                t.setRoomId(null);
            }
        }
        state.getRooms().remove(room);
        JsonDatabase.saveState(state);
    }

    public Optional<Room> findById(String roomId) {
        if (roomId == null) return Optional.empty();
        return state.getRooms().stream().filter(r -> roomId.equals(r.getId())).findFirst();
    }
}

