package com.example;

import javafx.application.Application;
import javafx.stage.Stage;
import com.example.ui.MainUI;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        MainUI ui = new MainUI();
        ui.start(stage);

    }

    public static void main(String[] args) {
        launch();
    }
}