package com.example.ui;

import com.example.model.Bill;
import com.example.service.BillService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainUI {

    private BillService service = new BillService();

    public void start(Stage stage){

        Label title = new Label("ระบบบิลหอพัก");

        TextField room = new TextField();
        room.setPromptText("ห้อง");

        TextField tenant = new TextField();
        tenant.setPromptText("ชื่อผู้เช่า");

        TextField rent = new TextField();
        rent.setPromptText("ค่าเช่า");

        TextField water = new TextField();
        water.setPromptText("ค่าน้ำ");

        TextField electric = new TextField();
        electric.setPromptText("ค่าไฟ (หน่วย)");

        Button save = new Button("บันทึกบิล");

        ListView<String> list = new ListView<>();

        save.setOnAction(e -> {

            Bill bill = new Bill(
                    room.getText(),
                    tenant.getText(),
                    Double.parseDouble(rent.getText()),
                    Double.parseDouble(water.getText()),
                    Integer.parseInt(electric.getText())
            );

            service.addBill(bill);

            list.getItems().add(
                    "ห้อง " + bill.getRoom() +
                    " | " + bill.getTenant() +
                    " | รวม " + bill.getTotal() + " บาท"
            );

        });

        VBox root = new VBox(10,
                title,
                room,
                tenant,
                rent,
                water,
                electric,
                save,
                list
        );

        Scene scene = new Scene(root,400,500);

        stage.setTitle("Dorm Bill System");
        stage.setScene(scene);
        stage.show();
    }
}