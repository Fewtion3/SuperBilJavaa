package com.example.service;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Map;

public class SettingsService {
    public enum Language { EN, TH }
    public enum Theme { LIGHT, DARK }

    private final ObjectProperty<Language> language = new SimpleObjectProperty<>(Language.EN);
    private final ObjectProperty<Theme> theme = new SimpleObjectProperty<>(Theme.LIGHT);

    private final Map<String, String> en = Map.ofEntries(
        Map.entry("app.title", "Dormitory system"),
        Map.entry("nav.dashboard", "Dashboard"),
        Map.entry("nav.rooms", "Rooms"),
        Map.entry("nav.tenants", "Tenants"),
        Map.entry("nav.billing", "Billing"),
        Map.entry("nav.settings", "Settings"),

        Map.entry("top.search", "Search..."),
        Map.entry("top.settings", "Settings"),

        Map.entry("page.dashboard.title", "Dashboard"),
        Map.entry("page.dashboard.sub", "Overview of rooms, tenants, and billing"),
        Map.entry("page.rooms.title", "Rooms"),
        Map.entry("page.rooms.sub", "Manage rooms and occupancy status"),
        Map.entry("page.tenants.title", "Tenants"),
        Map.entry("page.tenants.sub", "Manage tenants and room assignments"),
        Map.entry("page.billing.title", "Billing"),
        Map.entry("page.billing.sub", "Create monthly bills and track payments"),

        Map.entry("dash.totalRooms", "Total rooms"),
        Map.entry("dash.occupied", "Occupied"),
        Map.entry("dash.vacant", "Vacant"),
        Map.entry("dash.revenue", "Total revenue (paid)"),
        Map.entry("dash.recentBills", "Recent bills"),

        Map.entry("settings.title", "Settings"),
        Map.entry("settings.language", "Language"),
        Map.entry("settings.theme", "Theme"),
        Map.entry("settings.theme.light", "Light mode"),
        Map.entry("settings.theme.dark", "Dark mode"),
        Map.entry("settings.close", "Close"),

        Map.entry("bill.month", "Month"),
        Map.entry("bill.room", "Room"),
        Map.entry("bill.tenant", "Tenant"),
        Map.entry("bill.rent", "Rent"),
        Map.entry("bill.water", "Water"),
        Map.entry("bill.electric", "Electric"),
        Map.entry("bill.total", "Total"),
        Map.entry("bill.status", "Status"),
        Map.entry("bill.paid", "Paid"),
        Map.entry("bill.unpaid", "Unpaid")
    );

    private final Map<String, String> th = Map.ofEntries(
        Map.entry("app.title", "ระบบจัดการหอพัก"),
        Map.entry("nav.dashboard", "แดชบอร์ด"),
        Map.entry("nav.rooms", "ห้องพัก"),
        Map.entry("nav.tenants", "ผู้เช่า"),
        Map.entry("nav.billing", "บิล"),
        Map.entry("nav.settings", "ตั้งค่า"),

        Map.entry("top.search", "ค้นหา..."),
        Map.entry("top.settings", "ตั้งค่า"),

        Map.entry("page.dashboard.title", "แดชบอร์ด"),
        Map.entry("page.dashboard.sub", "ภาพรวมของห้อง ผู้เช่า และบิล"),
        Map.entry("page.rooms.title", "ห้องพัก"),
        Map.entry("page.rooms.sub", "จัดการห้องและสถานะการเข้าพัก"),
        Map.entry("page.tenants.title", "ผู้เช่า"),
        Map.entry("page.tenants.sub", "จัดการผู้เช่าและการกำหนดห้อง"),
        Map.entry("page.billing.title", "บิล"),
        Map.entry("page.billing.sub", "สร้างบิลรายเดือนและติดตามการชำระเงิน"),

        Map.entry("dash.totalRooms", "จำนวนห้องทั้งหมด"),
        Map.entry("dash.occupied", "ห้องที่มีผู้พัก"),
        Map.entry("dash.vacant", "ห้องว่าง"),
        Map.entry("dash.revenue", "รายได้รวม (ชำระแล้ว)"),
        Map.entry("dash.recentBills", "บิลล่าสุด"),

        Map.entry("settings.title", "ตั้งค่า"),
        Map.entry("settings.language", "ภาษา"),
        Map.entry("settings.theme", "ธีม"),
        Map.entry("settings.theme.light", "โหมดสว่าง"),
        Map.entry("settings.theme.dark", "โหมดมืด"),
        Map.entry("settings.close", "ปิด"),

        Map.entry("bill.month", "เดือน"),
        Map.entry("bill.room", "ห้อง"),
        Map.entry("bill.tenant", "ผู้เช่า"),
        Map.entry("bill.rent", "ค่าเช่า"),
        Map.entry("bill.water", "ค่าน้ำ"),
        Map.entry("bill.electric", "ค่าไฟ"),
        Map.entry("bill.total", "รวม"),
        Map.entry("bill.status", "สถานะ"),
        Map.entry("bill.paid", "ชำระแล้ว"),
        Map.entry("bill.unpaid", "ค้างชำระ")
    );

    public ObjectProperty<Language> languageProperty() {
        return language;
    }

    public Language getLanguage() {
        return language.get();
    }

    public void setLanguage(Language lang) {
        language.set(lang);
    }

    public ObjectProperty<Theme> themeProperty() {
        return theme;
    }

    public Theme getTheme() {
        return theme.get();
    }

    public void setTheme(Theme t) {
        theme.set(t);
    }

    public String t(String key) {
        Map<String, String> dict = getLanguage() == Language.TH ? th : en;
        return dict.getOrDefault(key, key);
    }

    public StringBinding bindText(String key) {
        return new StringBinding() {
            {
                bind(language);
            }

            @Override
            protected String computeValue() {
                return t(key);
            }
        };
    }
}

