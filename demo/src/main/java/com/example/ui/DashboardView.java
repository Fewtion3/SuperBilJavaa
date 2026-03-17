package com.example.ui;

import com.example.model.Bill;
import com.example.service.BillService;
import com.example.service.RoomService;
import com.example.service.SettingsService;
import com.example.service.TenantService;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.List;

public class DashboardView {
    private final SettingsService settings;
    private final RoomService roomService;
    private final TenantService tenantService;
    private final BillService billService;

    public DashboardView(SettingsService settings, RoomService roomService, TenantService tenantService, BillService billService) {
        this.settings = settings;
        this.roomService = roomService;
        this.tenantService = tenantService;
        this.billService = billService;
    }

    public Node render() {
        Label title = new Label();
        title.textProperty().bind(settings.bindText("page.dashboard.title"));
        title.getStyleClass().add("header");

        int totalRooms = roomService.getRooms().size();
        long occupied = tenantService.getTenants().stream().filter(t -> t.getRoomId() != null && !t.getRoomId().isBlank()).count();
        long vacant = Math.max(0, totalRooms - occupied);
        double totalRevenue = billService.getBills().stream().filter(Bill::isPaid).mapToDouble(Bill::getTotal).sum();

        VBox totalRoomsCard = statCard(settings.bindText("dash.totalRooms"), new Label(String.valueOf(totalRooms)));
        VBox occupiedCard = statCard(settings.bindText("dash.occupied"), new Label(String.valueOf(occupied)));
        VBox vacantCard = statCard(settings.bindText("dash.vacant"), new Label(String.valueOf(vacant)));
        VBox revenueCard = statCard(settings.bindText("dash.revenue"), new Label(String.format("%.2f ฿", totalRevenue)));

        HBox cards = new HBox(20, totalRoomsCard, occupiedCard, vacantCard, revenueCard);

        TableView<Bill> recent = new TableView<>();
        recent.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        TableColumn<Bill, Number> totalCol = new TableColumn<>();
        totalCol.textProperty().bind(settings.bindText("bill.total"));
        totalCol.setCellValueFactory(d -> new javafx.beans.property.SimpleDoubleProperty(d.getValue().getTotal()));

        TableColumn<Bill, String> paidCol = new TableColumn<>();
        paidCol.textProperty().bind(settings.bindText("bill.status"));
        paidCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().isPaid() ? settings.t("bill.paid") : settings.t("bill.unpaid")
        ));

        recent.getColumns().addAll(monthCol, roomCol, tenantCol, totalCol, paidCol);
        recent.setPrefHeight(360);

        List<Bill> sorted = billService.getBills().stream()
            .sorted(Comparator.comparingLong(Bill::getCreatedAtEpochMs).reversed())
            .limit(15)
            .toList();
        recent.setItems(FXCollections.observableArrayList(sorted));

        Label recentTitle = new Label();
        recentTitle.textProperty().bind(settings.bindText("dash.recentBills"));
        VBox tableCard = new VBox(12, recentTitle, recent);
        tableCard.getStyleClass().add("card");

        VBox root = new VBox(22, title, cards, tableCard);
        root.setPadding(new Insets(0));
        return root;
    }

    private VBox statCard(StringBinding title, Label value) {
        Label t = new Label();
        t.textProperty().bind(title);
        t.getStyleClass().add("stat-title");
        value.getStyleClass().add("stat-value");
        VBox box = new VBox(8, t, value);
        box.getStyleClass().add("stat-card");
        return box;
    }
}

