package com.example;

import com.example.database.JsonDatabase;
import com.example.model.Bill;
import com.example.service.BillService;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    BillService service = new BillService();

    ObservableList<Bill> data;

    Label totalRoomsLabel = new Label();
    Label occupiedLabel = new Label();
    Label revenueLabel = new Label();

    @Override
    public void start(Stage stage){

        Label title = new Label("ระบบคำนวณบิลหอพัก");
        title.getStyleClass().add("header");

        data = FXCollections.observableArrayList(service.getBills());

        TableView<Bill> table = new TableView<>(data);

        /* ---------- Columns ---------- */

        TableColumn<Bill,String> roomCol = new TableColumn<>("ห้อง");
        roomCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getRoom()));

        TableColumn<Bill,String> tenantCol = new TableColumn<>("ผู้เช่า");
        tenantCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getTenant()));

        TableColumn<Bill,Number> rentCol = new TableColumn<>("ค่าเช่า");
        rentCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleDoubleProperty(d.getValue().getRent()));

 TableColumn<Bill,Number> waterCol = new TableColumn<>("ค่าน้ำ");
waterCol.setCellValueFactory(d ->
        new javafx.beans.property.SimpleDoubleProperty(d.getValue().getWaterCost()));

TableColumn<Bill,Number> elecCol = new TableColumn<>("ค่าไฟ");
elecCol.setCellValueFactory(d ->
        new javafx.beans.property.SimpleDoubleProperty(d.getValue().getElectricCost()));

        TableColumn<Bill,Number> totalCol = new TableColumn<>("รวม");
        totalCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleDoubleProperty(d.getValue().getTotal()));

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(roomCol,tenantCol,rentCol,waterCol,elecCol,totalCol);
        table.setPrefHeight(350);

        /* ---------- Dashboard ---------- */

        VBox totalCard = createStatCard("จำนวนห้องทั้งหมด", totalRoomsLabel);
        VBox occupiedCard = createStatCard("ห้องที่มีผู้พัก", occupiedLabel);
        VBox revenueCard = createStatCard("รายได้", revenueLabel);

        HBox dashboard = new HBox(20,totalCard,occupiedCard,revenueCard);

        /* ---------- Form ---------- */

        TextField roomField = new TextField();
        roomField.setPromptText("เลขห้อง");

        TextField tenantField = new TextField();
        tenantField.setPromptText("ชื่อผู้เช่า");

        TextField rentField = new TextField();
        rentField.setPromptText("ค่าเช่า");

        TextField waterField = new TextField();
        waterField.setPromptText("ค่าน้ำ");

        TextField elecField = new TextField();
        elecField.setPromptText("ค่าไฟ");
        /* ---------- Table Select ---------- */

        table.getSelectionModel().selectedItemProperty().addListener((obs,oldV,newV)->{

            if(newV != null){

                roomField.setText(newV.getRoom());
                tenantField.setText(newV.getTenant());
                rentField.setText(String.valueOf(newV.getRent()));
                waterField.setText(String.valueOf(newV.getWaterUnit()));
                elecField.setText(String.valueOf(newV.getElectricUnit()));

            }

        });

        /* ---------- Add ---------- */

        Button addBtn = new Button("เพิ่มบิล");

        addBtn.setOnAction(e -> {

            try{

                String room = roomField.getText();
                String tenant = tenantField.getText();

                double rent = Double.parseDouble(rentField.getText());
                double water = Double.parseDouble(waterField.getText());
                double elec = Double.parseDouble(elecField.getText());

                Bill bill = new Bill(room,tenant,rent,water,elec);

                service.addBill(bill);
                data.add(bill);

                updateDashboard();

                roomField.clear();
                tenantField.clear();
                rentField.clear();
                waterField.clear();
                elecField.clear();

            }catch(Exception ex){

                showError("ข้อมูลไม่ถูกต้อง","กรุณากรอกตัวเลขให้ถูกต้อง");

            }

        });

        /* ---------- Edit ---------- */

        Button editBtn = new Button("แก้ไขบิล");

        editBtn.setOnAction(e -> {

            Bill selected = table.getSelectionModel().getSelectedItem();

            if(selected != null){

                try{

                    selected.setRoom(roomField.getText());
                    selected.setTenant(tenantField.getText());

                    selected.setRent(Double.parseDouble(rentField.getText()));
                    selected.setWaterUnit(Double.parseDouble(waterField.getText()));
                    selected.setElectricUnit(Double.parseDouble(elecField.getText()));

                    table.refresh();
                    updateDashboard();

                    JsonDatabase.saveBills(service.getBills());

                }catch(Exception ex){

                    showError("Invalid Input","Please enter correct numbers");

                }

            }

        });

        /* ---------- Delete ---------- */

        Button deleteBtn = new Button("ลบบิล");
        deleteBtn.getStyleClass().add("delete-btn");

        deleteBtn.setOnAction(e -> {

            Bill selected = table.getSelectionModel().getSelectedItem();

            if(selected != null){

                data.remove(selected);
                service.deleteBill(selected);

                updateDashboard();

            }

        });

        /* ---------- Search ---------- */

        TextField searchField = new TextField();
        searchField.setPromptText("ค้นหาห้อง");

        searchField.textProperty().addListener((obs,oldV,newV)->{

            if(newV.isEmpty()){

                table.setItems(data);

            }else{

                ObservableList<Bill> filtered = FXCollections.observableArrayList();

                for(Bill b : data){

                    if(b.getRoom().contains(newV)){
                        filtered.add(b);
                    }

                }

                table.setItems(filtered);

            }

        });

        /* ---------- Layout ---------- */

        HBox form = new HBox(10,
                roomField,
                tenantField,
                rentField,
                waterField,
                elecField,
                addBtn,
                editBtn,
                deleteBtn,
                searchField
        );

        VBox card = new VBox(15,form,table);
        card.getStyleClass().add("card");

        HBox topBar = new HBox(20,title);

        VBox root = new VBox(25,topBar,dashboard,card);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(root,1100,650);

        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );

        updateDashboard();

        stage.setTitle("Dorm Billing System");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createStatCard(String title, Label value){

        Label t = new Label(title);
        t.getStyleClass().add("stat-title");

        value.getStyleClass().add("stat-value");

        VBox box = new VBox(8,t,value);
        box.getStyleClass().add("stat-card");

        return box;
    }

    private void updateDashboard(){

        int totalRooms = data.size();

        int occupied = 0;
        double revenue = 0;

        for(Bill b : data){

            if(b.getTenant()!=null && !b.getTenant().isEmpty()){
                occupied++;
            }

            revenue += b.getTotal();

        }

        totalRoomsLabel.setText(String.valueOf(totalRooms));
        occupiedLabel.setText(String.valueOf(occupied));
        revenueLabel.setText(String.format("%.2f ฿",revenue));
    }

    private void showError(String title,String msg){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }

    public static void main(String[] args){
        launch();
    }

}