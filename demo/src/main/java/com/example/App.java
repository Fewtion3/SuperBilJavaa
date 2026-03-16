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
    public void start(Stage stage){

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

        TableColumn<Bill,Number> waterCol = new TableColumn<>("Water");
        waterCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleDoubleProperty(data.getValue().getWater()));

        TableColumn<Bill,Number> elecCol = new TableColumn<>("Electric");
        elecCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleDoubleProperty(data.getValue().getElectric()));

        TableColumn<Bill,Number> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotal()));

        table.getColumns().addAll(roomCol,tenantCol,rentCol,waterCol,elecCol,totalCol);

        table.getItems().addAll(service.getBills());

        // INPUT
        TextField roomField = new TextField();
        roomField.setPromptText("Room");

        TextField tenantField = new TextField();
        tenantField.setPromptText("Tenant");

        TextField rentField = new TextField();
        rentField.setPromptText("Rent");

        TextField waterField = new TextField();
        waterField.setPromptText("Water");

        TextField elecField = new TextField();
        elecField.setPromptText("Electric");

        Button addBtn = new Button("Add");

        addBtn.setOnAction(e -> {

            String room = roomField.getText();
            String tenant = tenantField.getText();

            double rent = Double.parseDouble(rentField.getText());
            double water = Double.parseDouble(waterField.getText());
            double elec = Double.parseDouble(elecField.getText());

            Bill bill = new Bill(room,tenant,rent,water,elec);

            service.addBill(bill);

            table.getItems().add(bill);

            roomField.clear();
            tenantField.clear();
            rentField.clear();
            waterField.clear();
            elecField.clear();

        });

        Button deleteBtn = new Button("Delete");

        deleteBtn.setOnAction(e -> {

            Bill selected = table.getSelectionModel().getSelectedItem();

            if(selected != null){
                table.getItems().remove(selected);
            }

        });

        HBox form = new HBox(10,
                roomField,
                tenantField,
                rentField,
                waterField,
                elecField,
                addBtn,
                deleteBtn
        );

        VBox root = new VBox(10,form,table);

        Scene scene = new Scene(root,900,500);

        stage.setTitle("Dorm Billing System");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args){
        launch();
    }

}