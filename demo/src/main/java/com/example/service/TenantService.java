package com.example.service;

import com.example.database.JsonDatabase;
import com.example.model.Bill;
import com.example.model.Room;
import com.example.model.Tenant;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TenantService {
    private final JsonDatabase.State state;

    public TenantService(JsonDatabase.State state) {
        this.state = state;
    }

    public List<Tenant> getTenants() {
        return state.getTenants();
    }

    public Tenant addTenant(String name, String phone, Room roomOrNull) {
        Tenant t = new Tenant(UUID.randomUUID().toString(), name, phone, roomOrNull == null ? null : roomOrNull.getId(), System.currentTimeMillis());
        state.getTenants().add(t);
        state.getTenants().sort(Comparator.comparing(a -> a.getName() == null ? "" : a.getName()));
        JsonDatabase.saveState(state);
        return t;
    }

    public void updateTenant(Tenant tenant, String name, String phone, Room roomOrNull) {
        tenant.setName(name);
        tenant.setPhone(phone);
        tenant.setRoomId(roomOrNull == null ? null : roomOrNull.getId());
        JsonDatabase.saveState(state);
    }

    public void deleteTenant(Tenant tenant, List<Bill> bills) {
        // Keep bills but remove links
        for (Bill b : bills) {
            if (tenant.getId() != null && tenant.getId().equals(b.getTenantId())) {
                b.setTenantId(null);
                if (b.getTenantName() == null || b.getTenantName().isBlank()) {
                    b.setTenantName(tenant.getName());
                }
            }
        }
        state.getTenants().remove(tenant);
        JsonDatabase.saveState(state);
    }

    public Optional<Tenant> findById(String tenantId) {
        if (tenantId == null) return Optional.empty();
        return state.getTenants().stream().filter(t -> tenantId.equals(t.getId())).findFirst();
    }
}

