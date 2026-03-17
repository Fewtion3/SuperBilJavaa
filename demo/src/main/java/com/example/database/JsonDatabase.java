package com.example.database;

import com.example.model.Bill;
import com.example.model.Room;
import com.example.model.Tenant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JsonDatabase {

    private static final String FILE = "database.json";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static State loadState() {
        try (FileReader reader = new FileReader(FILE)) {
            JsonElement root = JsonParser.parseReader(reader);

            // Backward compatibility: old format was a JSON array of legacy bills
            if (root != null && root.isJsonArray()) {
                Type listType = new TypeToken<List<LegacyBill>>() {}.getType();
                List<LegacyBill> legacyBills = gson.fromJson(root, listType);
                State state = State.empty();
                if (legacyBills != null) {
                    for (LegacyBill b : legacyBills) {
                        state.getBills().add(toModernBill(b));
                    }
                }
                saveState(state);
                return state;
            }

            State state = gson.fromJson(root, State.class);
            return state != null ? state : State.empty();
        } catch (Exception e) {
            return State.empty();
        }
    }

    public static void saveState(State state) {
        try (FileWriter writer = new FileWriter(FILE)) {
            gson.toJson(state, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Persisted schema root object for the dormitory management system.
     */
    public static class State {
        private List<Room> rooms;
        private List<Tenant> tenants;
        private List<Bill> bills;

        public static State empty() {
            State s = new State();
            s.rooms = new ArrayList<>();
            s.tenants = new ArrayList<>();
            s.bills = new ArrayList<>();
            return s;
        }

        public List<Room> getRooms() {
            if (rooms == null) rooms = new ArrayList<>();
            return rooms;
        }

        public void setRooms(List<Room> rooms) {
            this.rooms = rooms;
        }

        public List<Tenant> getTenants() {
            if (tenants == null) tenants = new ArrayList<>();
            return tenants;
        }

        public void setTenants(List<Tenant> tenants) {
            this.tenants = tenants;
        }

        public List<Bill> getBills() {
            if (bills == null) bills = new ArrayList<>();
            return bills;
        }

        public void setBills(List<Bill> bills) {
            this.bills = bills;
        }
    }

    // Old schema stored in database.json (array of these objects)
    private static class LegacyBill {
        String room;
        String tenant;
        double rent;
        double waterUnit;
        double electricUnit;
    }

    private static Bill toModernBill(LegacyBill legacy) {
        Bill b = new Bill();
        b.setId(UUID.randomUUID().toString());
        b.setBillingMonth(java.time.YearMonth.now().toString());
        b.setRoomNumber(legacy.room);
        b.setTenantName(legacy.tenant);
        b.setRent(legacy.rent);
        b.setWaterUnit(legacy.waterUnit);
        b.setElectricUnit(legacy.electricUnit);
        b.setPaid(false);
        b.setCreatedAtEpochMs(System.currentTimeMillis());
        return b;
    }
}