package com.example;

import com.example.model.Bill;
import com.example.service.BillService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    BillService service = new BillService();

    @Override
    public void start(Stage stage) {

        TableView<Bill> table = new TableView<>();

        TableColumn<Bill,String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getRoom()));

        TableColumn<Bill,String> tenantCol = new TableColumn<>("Tenant");
        tenantCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTenant()));

        TableColumn<Bill,Number> rentCol = new TableColumn<>("Rent");
        rentCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getRent()));

        table.getColumns().addAll(roomCol, tenantCol, rentCol);

        table.getItems().addAll(service.getBills());

        // INPUT FIELD
        TextField roomField = new TextField();
        roomField.setPromptText("Room");

        TextField tenantField = new TextField();
        tenantField.setPromptText("Tenant");

        TextField rentField = new TextField();
        rentField.setPromptText("Rent");

        Button addButton = new Button("Add Bill");

        addButton.setOnAction(e -> {

            String room = roomField.getText();
            String tenant = tenantField.getText();
            double rent = Double.parseDouble(rentField.getText());

            Bill bill = new Bill(room, tenant, rent,0,0);

            service.addBill(bill);

            table.getItems().add(bill);

            roomField.clear();
            tenantField.clear();
            rentField.clear();

        });

        HBox form = new HBox(10, roomField, tenantField, rentField, addButton);

        VBox root = new VBox(10, form, table);

        Scene scene = new Scene(root,600,400);

        stage.setTitle("Dorm Billing System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}