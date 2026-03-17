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

public class TenantView {
    private final SettingsService settings;
    private final RoomService roomService;
    private final TenantService tenantService;
    private final BillService billService;

    public TenantView(SettingsService settings, RoomService roomService, TenantService tenantService, BillService billService) {
        this.settings = settings;
        this.roomService = roomService;
        this.tenantService = tenantService;
        this.billService = billService;
    }

    public Node render() {
        Label title = new Label();
        title.textProperty().bind(settings.bindText("page.tenants.title"));
        title.getStyleClass().add("header");

        ObservableList<Tenant> tenants = FXCollections.observableArrayList(tenantService.getTenants());
        TableView<Tenant> table = new TableView<>(tenants);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Tenant, String> nameCol = new TableColumn<>("ชื่อ");
        nameCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));

        TableColumn<Tenant, String> phoneCol = new TableColumn<>("เบอร์");
        phoneCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getPhone() == null ? "" : d.getValue().getPhone()));

        TableColumn<Tenant, String> roomCol = new TableColumn<>("ห้อง");
        roomCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            roomService.findById(d.getValue().getRoomId()).map(Room::getNumber).orElse("-")
        ));

        table.getColumns().addAll(nameCol, phoneCol, roomCol);
        table.setPrefHeight(420);

        TextField nameField = new TextField();
        nameField.setPromptText("ชื่อผู้เช่า");

        TextField phoneField = new TextField();
        phoneField.setPromptText("เบอร์โทร");

        ComboBox<Room> roomBox = new ComboBox<>();
        roomBox.setPromptText("ห้อง");
        roomBox.setItems(FXCollections.observableArrayList(roomService.getRooms()));
        roomBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Room item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNumber());
            }
        });
        roomBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Room item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNumber());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n == null) return;
            nameField.setText(n.getName());
            phoneField.setText(n.getPhone() == null ? "" : n.getPhone());
            roomBox.getSelectionModel().clearSelection();
            roomService.findById(n.getRoomId()).ifPresent(r -> roomBox.getSelectionModel().select(r));
        });

        Button addBtn = new Button("เพิ่มผู้เช่า");
        addBtn.getStyleClass().add("save-btn");
        addBtn.setOnAction(e -> {
            Tenant t = tenantService.addTenant(nameField.getText(), phoneField.getText(), roomBox.getValue());
            tenants.setAll(tenantService.getTenants());
            table.getSelectionModel().select(t);
            clear(nameField, phoneField);
            roomBox.getSelectionModel().clearSelection();
        });

        Button editBtn = new Button("แก้ไข");
        editBtn.setOnAction(e -> {
            Tenant selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            tenantService.updateTenant(selected, nameField.getText(), phoneField.getText(), roomBox.getValue());
            tenants.setAll(tenantService.getTenants());
            table.refresh();
        });

        Button deleteBtn = new Button("ลบ");
        deleteBtn.getStyleClass().add("delete-btn");
        deleteBtn.setOnAction(e -> {
            Tenant selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            tenantService.deleteTenant(selected, billService.getBills());
            tenants.setAll(tenantService.getTenants());
            table.refresh();
            clear(nameField, phoneField);
            roomBox.getSelectionModel().clearSelection();
        });

        HBox form = new HBox(10, nameField, phoneField, roomBox, addBtn, editBtn, deleteBtn);
        VBox card = new VBox(15, form, table);
        card.getStyleClass().add("card");

        VBox root = new VBox(22, title, card);
        root.setPadding(new Insets(0));
        return root;
    }

    private static void clear(TextField... fields) {
        for (TextField f : fields) f.clear();
    }
}

