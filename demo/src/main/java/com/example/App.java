package com.example;

import com.example.model.Bill;
import com.example.service.BillService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    BillService service = new BillService();

    @Override
    public void start(Stage stage) {

        TableView<Bill> table = new TableView<>();

        TableColumn<Bill,String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRoom()));

        TableColumn<Bill,String> tenantCol = new TableColumn<>("Tenant");
        tenantCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTenant()));

        TableColumn<Bill,Number> rentCol = new TableColumn<>("Rent");
        rentCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getRent()));

        table.getColumns().addAll(roomCol, tenantCol, rentCol);

        table.getItems().addAll(service.getBills());

        VBox root = new VBox(table);

        Scene scene = new Scene(root,600,400);

        stage.setTitle("Dorm Billing System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}