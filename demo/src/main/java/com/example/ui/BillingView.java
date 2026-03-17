package com.example.ui;

import com.example.model.Bill;
import com.example.model.Room;
import com.example.model.Tenant;
import com.example.service.BillService;
import com.example.service.RoomService;
import com.example.service.SettingsService;
import com.example.service.TenantService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.YearMonth;
import java.util.Comparator;

public class BillingView {
    private final SettingsService settings;
    private final RoomService roomService;
    private final TenantService tenantService;
    private final BillService billService;

    public BillingView(SettingsService settings, RoomService roomService, TenantService tenantService, BillService billService) {
        this.settings = settings;
        this.roomService = roomService;
        this.tenantService = tenantService;
        this.billService = billService;
    }

    public Node render() {
        Label title = new Label();
        title.textProperty().bind(settings.bindText("page.billing.title"));
        title.getStyleClass().add("header");

        ObservableList<Bill> bills = FXCollections.observableArrayList(billService.getBills());

        TextField monthField = new TextField(YearMonth.now().toString());
        monthField.setPromptText("เดือน (yyyy-MM)");

        ComboBox<Room> roomBox = roomBox();
        roomBox.setItems(FXCollections.observableArrayList(roomService.getRooms()));

        ComboBox<Tenant> tenantBox = tenantBox();
        tenantBox.setItems(FXCollections.observableArrayList(tenantService.getTenants()));

        TextField rentField = new TextField();
        rentField.setPromptText("ค่าเช่า");

        TextField waterField = new TextField();
        waterField.setPromptText("ค่าน้ำ (หน่วย)");

        TextField elecField = new TextField();
        elecField.setPromptText("ค่าไฟ (หน่วย)");

        Label totalPreview = new Label("รวม: -");

        Runnable updateTotal = () -> {
            try {
                double rent = Double.parseDouble(blankToZero(rentField.getText()));
                double water = Double.parseDouble(blankToZero(waterField.getText()));
                double elec = Double.parseDouble(blankToZero(elecField.getText()));
                Bill tmp = new Bill();
                tmp.setRent(rent);
                tmp.setWaterUnit(water);
                tmp.setElectricUnit(elec);
                totalPreview.setText(String.format("รวม: %.2f ฿", tmp.getTotal()));
            } catch (Exception ignored) {
                totalPreview.setText("รวม: -");
            }
        };
        rentField.textProperty().addListener((o, a, b) -> updateTotal.run());
        waterField.textProperty().addListener((o, a, b) -> updateTotal.run());
        elecField.textProperty().addListener((o, a, b) -> updateTotal.run());

        roomBox.valueProperty().addListener((obs, o, n) -> {
            if (n == null) return;
            // Auto rent from room
            rentField.setText(String.valueOf(n.getDefaultRent()));

            // Auto-choose tenant assigned to that room (if any)
            Tenant t = tenantService.getTenants().stream()
                .filter(x -> n.getId() != null && n.getId().equals(x.getRoomId()))
                .findFirst()
                .orElse(null);
            tenantBox.getSelectionModel().select(t);
        });

        TableView<Bill> table = new TableView<>(bills);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Bill, String> monthCol = new TableColumn<>();
        monthCol.textProperty().bind(settings.bindText("bill.month"));
        monthCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getBillingMonth()));

        TableColumn<Bill, String> roomCol = new TableColumn<>();
        roomCol.textProperty().bind(settings.bindText("bill.room"));
        roomCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().getRoomNumber() != null ? d.getValue().getRoomNumber() : "-"
        ));

        TableColumn<Bill, String> tenantCol = new TableColumn<>();
        tenantCol.textProperty().bind(settings.bindText("bill.tenant"));
        tenantCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().getTenantName() != null ? d.getValue().getTenantName() : "-"
        ));

        TableColumn<Bill, Number> rentCol = new TableColumn<>();
        rentCol.textProperty().bind(settings.bindText("bill.rent"));
        rentCol.setCellValueFactory(d -> new javafx.beans.property.SimpleDoubleProperty(d.getValue().getRent()));

        TableColumn<Bill, Number> waterCostCol = new TableColumn<>();
        waterCostCol.textProperty().bind(settings.bindText("bill.water"));
        waterCostCol.setCellValueFactory(d -> new javafx.beans.property.SimpleDoubleProperty(d.getValue().getWaterCost()));

        TableColumn<Bill, Number> elecCostCol = new TableColumn<>();
        elecCostCol.textProperty().bind(settings.bindText("bill.electric"));
        elecCostCol.setCellValueFactory(d -> new javafx.beans.property.SimpleDoubleProperty(d.getValue().getElectricCost()));

        TableColumn<Bill, Number> totalCol = new TableColumn<>();
        totalCol.textProperty().bind(settings.bindText("bill.total"));
        totalCol.setCellValueFactory(d -> new javafx.beans.property.SimpleDoubleProperty(d.getValue().getTotal()));

        TableColumn<Bill, String> paidCol = new TableColumn<>();
        paidCol.textProperty().bind(settings.bindText("bill.status"));
        paidCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().isPaid() ? settings.t("bill.paid") : settings.t("bill.unpaid")
        ));

        table.getColumns().addAll(monthCol, roomCol, tenantCol, rentCol, waterCostCol, elecCostCol, totalCol, paidCol);
        table.setPrefHeight(420);

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n == null) return;
            monthField.setText(n.getBillingMonth());
            rentField.setText(String.valueOf(n.getRent()));
            waterField.setText(String.valueOf(n.getWaterUnit()));
            elecField.setText(String.valueOf(n.getElectricUnit()));
            updateTotal.run();
        });

        Button addBtn = new Button("สร้างบิลรายเดือน");
        addBtn.getStyleClass().add("save-btn");
        addBtn.setOnAction(e -> {
            try {
                Room room = roomBox.getValue();
                Tenant tenant = tenantBox.getValue();
                double water = Double.parseDouble(waterField.getText());
                double elec = Double.parseDouble(elecField.getText());

                billService.createMonthlyBill(monthField.getText(), room, tenant, water, elec);
                bills.setAll(billService.getBills().stream()
                    .sorted(Comparator.comparingLong(Bill::getCreatedAtEpochMs).reversed())
                    .toList());

                waterField.clear();
                elecField.clear();
                totalPreview.setText("รวม: -");
            } catch (Exception ex) {
                showError("ข้อมูลไม่ถูกต้อง", "กรุณากรอกหน่วยน้ำ/ไฟเป็นตัวเลข");
            }
        });

        Button togglePaidBtn = new Button("สลับสถานะจ่ายแล้ว");
        togglePaidBtn.setOnAction(e -> {
            Bill selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            billService.togglePaid(selected);
            table.refresh();
        });

        Button deleteBtn = new Button("ลบบิล");
        deleteBtn.getStyleClass().add("delete-btn");
        deleteBtn.setOnAction(e -> {
            Bill selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            billService.deleteBill(selected);
            bills.setAll(billService.getBills());
            table.refresh();
        });

        HBox form = new HBox(10, monthField, roomBox, tenantBox, rentField, waterField, elecField, addBtn, togglePaidBtn, deleteBtn, totalPreview);
        VBox card = new VBox(15, form, table);
        card.getStyleClass().add("card");

        VBox root = new VBox(22, title, card);
        root.setPadding(new Insets(0));
        return root;
    }

    private ComboBox<Room> roomBox() {
        ComboBox<Room> box = new ComboBox<>();
        box.setPromptText("ห้อง");
        box.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Room item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNumber());
            }
        });
        box.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Room item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNumber());
            }
        });
        return box;
    }

    private ComboBox<Tenant> tenantBox() {
        ComboBox<Tenant> box = new ComboBox<>();
        box.setPromptText("ผู้เช่า");
        box.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Tenant item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        box.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Tenant item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        return box;
    }

    private static String blankToZero(String s) {
        return (s == null || s.isBlank()) ? "0" : s;
    }

    private static void showError(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setContentText(msg);
        a.show();
    }
}

