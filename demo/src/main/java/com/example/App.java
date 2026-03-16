package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("ระบบบิลหอพัก");

        TextField roomField = new TextField();
        roomField.setPromptText("ห้อง");

        TextField tenantField = new TextField();
        tenantField.setPromptText("ชื่อผู้เช่า");

        TextField rentField = new TextField();
        rentField.setPromptText("ค่าเช่าห้อง");

        TextField waterField = new TextField();
        waterField.setPromptText("ค่าน้ำ");

        TextField electricField = new TextField();
        electricField.setPromptText("ค่าไฟ (หน่วย)");

        Button saveButton = new Button("บันทึกบิล");

        ListView<String> billList = new ListView<>();

        saveButton.setOnAction(e -> {

            String room = roomField.getText();
            String tenant = tenantField.getText();

            double rent = Double.parseDouble(rentField.getText());
            double water = Double.parseDouble(waterField.getText());
            int electric = Integer.parseInt(electricField.getText());

            double electricCost = electric * 8;

            double total = rent + water + electricCost;

            String billText =
                    "ห้อง " + room +
                    " | " + tenant +
                    " | รวม " + total + " บาท";

            billList.getItems().add(billText);

        });

        VBox layout = new VBox(10,
                title,
                roomField,
                tenantField,
                rentField,
                waterField,
                electricField,
                saveButton,
                billList
        );

        Scene scene = new Scene(layout, 400, 500);

        stage.setTitle("Dorm Bill System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}