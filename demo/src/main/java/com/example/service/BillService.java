package com.example.service;

import com.example.database.JsonDatabase;
import com.example.model.Bill;
import com.example.model.Room;
import com.example.model.Tenant;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BillService {

    private final JsonDatabase.State state;

    public BillService(JsonDatabase.State state) {
        this.state = state;
    }

    public List<Bill> getBills(){
        return state.getBills();
    }

    public Bill createMonthlyBill(String billingMonth, Room room, Tenant tenantOrNull, double waterUnit, double electricUnit) {
        Bill b = new Bill();
        b.setId(UUID.randomUUID().toString());
        b.setBillingMonth(billingMonth == null || billingMonth.isBlank() ? YearMonth.now().toString() : billingMonth);

        if (room != null) {
            b.setRoomId(room.getId());
            b.setRoomNumber(room.getNumber());
            b.setRent(room.getDefaultRent());
        } else {
            b.setRent(0);
        }

        if (tenantOrNull != null) {
            b.setTenantId(tenantOrNull.getId());
            b.setTenantName(tenantOrNull.getName());
        }

        b.setWaterUnit(waterUnit);
        b.setElectricUnit(electricUnit);
        b.setPaid(false);
        b.setCreatedAtEpochMs(System.currentTimeMillis());

        state.getBills().add(b);
        state.getBills().sort(Comparator.comparingLong(Bill::getCreatedAtEpochMs).reversed());
        JsonDatabase.saveState(state);
        return b;
    }

    public void updateBill(Bill bill, String billingMonth, Room roomOrNull, Tenant tenantOrNull,
                           double rent, double waterUnit, double electricUnit, boolean paid) {
        bill.setBillingMonth(billingMonth);
        bill.setRoomId(roomOrNull == null ? null : roomOrNull.getId());
        bill.setRoomNumber(roomOrNull == null ? bill.getRoomNumber() : roomOrNull.getNumber());
        bill.setTenantId(tenantOrNull == null ? null : tenantOrNull.getId());
        bill.setTenantName(tenantOrNull == null ? bill.getTenantName() : tenantOrNull.getName());
        bill.setRent(rent);
        bill.setWaterUnit(waterUnit);
        bill.setElectricUnit(electricUnit);
        bill.setPaid(paid);
        JsonDatabase.saveState(state);
    }

    public void deleteBill(Bill bill) {
        state.getBills().remove(bill);
        JsonDatabase.saveState(state);
    }

    public void togglePaid(Bill bill) {
        bill.setPaid(!bill.isPaid());
        JsonDatabase.saveState(state);
    }

    public Optional<Bill> findById(String billId) {
        if (billId == null) return Optional.empty();
        return state.getBills().stream().filter(b -> billId.equals(b.getId())).findFirst();
    }

}