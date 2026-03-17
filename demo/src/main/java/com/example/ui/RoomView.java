package com.example.ui;

import com.example.model.Room;
import com.example.model.Tenant;
import com.example.service.RoomService;
import com.example.service.SettingsService;
import com.example.service.TenantService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RoomView {
    private final SettingsService settings;
    private final RoomService roomService;
    private final TenantService tenantService;

    public RoomView(SettingsService settings, RoomService roomService, TenantService tenantService) {
        this.settings = settings;
        this.roomService = roomService;
        this.tenantService = tenantService;
    }

    public Node render() {
        Label title = new Label();
        title.textProperty().bind(settings.bindText("page.rooms.title"));
        title.getStyleClass().add("header");

        ObservableList<Room> rooms = FXCollections.observableArrayList(roomService.getRooms());
        TableView<Room> table = new TableView<>(rooms);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Room, String> numberCol = new TableColumn<>("เลขห้อง");
        numberCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNumber()));

        TableColumn<Room, String> statusCol = new TableColumn<>("สถานะ");
        statusCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            isOccupied(d.getValue()) ? "มีผู้พัก" : "ห้องว่าง"
        ));

        TableColumn<Room, Number> rentCol = new TableColumn<>("ค่าเช่า");
        rentCol.setCellValueFactory(d -> new javafx.beans.property.SimpleDoubleProperty(d.getValue().getDefaultRent()));

        TableColumn<Room, String> noteCol = new TableColumn<>("หมายเหตุ");
        noteCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNotes() == null ? "" : d.getValue().getNotes()));

        table.getColumns().addAll(numberCol, statusCol, rentCol, noteCol);
        table.setPrefHeight(420);

        TextField numberField = new TextField();
        numberField.setPromptText("เลขห้อง");

        TextField rentField = new TextField();
        rentField.setPromptText("ค่าเช่า (เริ่มต้น)");

        TextField notesField = new TextField();
        notesField.setPromptText("หมายเหตุ");

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n == null) return;
            numberField.setText(n.getNumber());
            rentField.setText(String.valueOf(n.getDefaultRent()));
            notesField.setText(n.getNotes() == null ? "" : n.getNotes());
        });

        Button addBtn = new Button("เพิ่มห้อง");
        addBtn.getStyleClass().add("save-btn");
        addBtn.setOnAction(e -> {
            try {
                Room r = roomService.addRoom(numberField.getText(), Double.parseDouble(rentField.getText()), notesField.getText());
                rooms.setAll(roomService.getRooms());
                table.getSelectionModel().select(r);
                clear(numberField, rentField, notesField);
            } catch (Exception ex) {
                showError("ข้อมูลไม่ถูกต้อง", "กรุณากรอกค่าเช่าเป็นตัวเลข");
            }
        });

        Button editBtn = new Button("แก้ไข");
        editBtn.setOnAction(e -> {
            Room selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            try {
                roomService.updateRoom(selected, numberField.getText(), Double.parseDouble(rentField.getText()), notesField.getText());
                rooms.setAll(roomService.getRooms());
                table.refresh();
            } catch (Exception ex) {
                showError("ข้อมูลไม่ถูกต้อง", "กรุณากรอกค่าเช่าเป็นตัวเลข");
            }
        });

        Button deleteBtn = new Button("ลบ");
        deleteBtn.getStyleClass().add("delete-btn");
        deleteBtn.setOnAction(e -> {
            Room selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            roomService.deleteRoom(selected, tenantService.getTenants());
            rooms.setAll(roomService.getRooms());
            table.refresh();
            clear(numberField, rentField, notesField);
        });

        HBox form = new HBox(10, numberField, rentField, notesField, addBtn, editBtn, deleteBtn);
        VBox card = new VBox(15, form, table);
        card.getStyleClass().add("card");

        VBox root = new VBox(22, title, card);
        root.setPadding(new Insets(0));
        return root;
    }

    private boolean isOccupied(Room room) {
        for (Tenant t : tenantService.getTenants()) {
            if (room.getId() != null && room.getId().equals(t.getRoomId())) return true;
        }
        return false;
    }

    private static void clear(TextField... fields) {
        for (TextField f : fields) f.clear();
    }

    private static void showError(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setContentText(msg);
        a.show();
    }
}

