package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.example.database.JsonDatabase;
import com.example.service.BillService;
import com.example.service.RoomService;
import com.example.service.SettingsService;
import com.example.service.TenantService;
import com.example.ui.BillingView;
import com.example.ui.DashboardView;
import com.example.ui.RoomView;
import com.example.ui.SettingsDialog;
import com.example.ui.Sidebar;
import com.example.ui.TopHeader;
import com.example.ui.TenantView;

public class App extends Application {

    @Override
    public void start(Stage stage){
        JsonDatabase.State state = JsonDatabase.loadState();

        RoomService roomService = new RoomService(state);
        TenantService tenantService = new TenantService(state);
        BillService billService = new BillService(state);
        SettingsService settingsService = new SettingsService();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(0));

        Sidebar sidebar = new Sidebar("");
        sidebar.getTitleLabel().textProperty().bind(settingsService.bindText("app.title"));
        root.setLeft(sidebar);

        TopHeader header = new TopHeader();

        SettingsDialog settingsDialog = new SettingsDialog(settingsService);
        Runnable openSettings = () -> settingsDialog.show(stage);

        root.setTop(header.render(settingsService, "page.dashboard.title", "page.dashboard.sub", openSettings));

        Scene scene = new Scene(root, 1200, 700);
        applyTheme(scene, settingsService.getTheme());
        settingsService.themeProperty().addListener((obs, o, n) -> applyTheme(scene, n));

        Runnable showDashboard = () -> {
            root.setTop(header.render(settingsService, "page.dashboard.title", "page.dashboard.sub", openSettings));
            setCenter(root, new DashboardView(settingsService, roomService, tenantService, billService).render());
        };
        Runnable showRooms = () -> {
            root.setTop(header.render(settingsService, "page.rooms.title", "page.rooms.sub", openSettings));
            setCenter(root, new RoomView(settingsService, roomService, tenantService).render());
        };
        Runnable showTenants = () -> {
            root.setTop(header.render(settingsService, "page.tenants.title", "page.tenants.sub", openSettings));
            setCenter(root, new TenantView(settingsService, roomService, tenantService, billService).render());
        };
        Runnable showBilling = () -> {
            root.setTop(header.render(settingsService, "page.billing.title", "page.billing.sub", openSettings));
            setCenter(root, new BillingView(settingsService, roomService, tenantService, billService).render());
        };

        sidebar.addItem("dashboard", settingsService.bindText("nav.dashboard"), () -> { sidebar.setActive("dashboard"); showDashboard.run(); });
        sidebar.addItem("rooms", settingsService.bindText("nav.rooms"), () -> { sidebar.setActive("rooms"); showRooms.run(); });
        sidebar.addItem("tenants", settingsService.bindText("nav.tenants"), () -> { sidebar.setActive("tenants"); showTenants.run(); });
        sidebar.addItem("billing", settingsService.bindText("nav.billing"), () -> { sidebar.setActive("billing"); showBilling.run(); });

        sidebar.setActive("dashboard");
        showDashboard.run();

        stage.setTitle("Dormitory Management System");
        stage.setScene(scene);
        stage.show();
    }

    private static void setCenter(BorderPane root, Node node) {
        root.setCenter(node);
        BorderPane.setMargin(node, new Insets(28));
    }

    private void applyTheme(Scene scene, SettingsService.Theme theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        if (theme == SettingsService.Theme.DARK) {
            scene.getStylesheets().add(getClass().getResource("/style-dark.css").toExternalForm());
        }
    }

    public static void main(String[] args){
        launch();
    }

}