package com.example.ui;

import com.example.service.SettingsService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SettingsDialog {
    private final SettingsService settings;

    public SettingsDialog(SettingsService settings) {
        this.settings = settings;
    }

    public void show(Stage owner) {
        Stage dialog = new Stage(StageStyle.UTILITY);
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setResizable(false);

        Label title = new Label();
        title.textProperty().bind(settings.bindText("settings.title"));
        title.getStyleClass().add("dialog-title");

        Label langLabel = new Label();
        langLabel.textProperty().bind(settings.bindText("settings.language"));

        ComboBox<SettingsService.Language> langBox = new ComboBox<>(
            FXCollections.observableArrayList(SettingsService.Language.EN, SettingsService.Language.TH)
        );
        langBox.valueProperty().bindBidirectional(settings.languageProperty());
        langBox.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(SettingsService.Language item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : (item == SettingsService.Language.TH ? "ไทย" : "English"));
            }
        });
        langBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(SettingsService.Language item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : (item == SettingsService.Language.TH ? "ไทย" : "English"));
            }
        });

        Label themeLabel = new Label();
        themeLabel.textProperty().bind(settings.bindText("settings.theme"));

        ComboBox<SettingsService.Theme> themeBox = new ComboBox<>(
            FXCollections.observableArrayList(SettingsService.Theme.LIGHT, SettingsService.Theme.DARK)
        );
        themeBox.valueProperty().bindBidirectional(settings.themeProperty());
        themeBox.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(SettingsService.Theme item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item == SettingsService.Theme.DARK ? settings.t("settings.theme.dark") : settings.t("settings.theme.light"));
                }
            }
        });
        themeBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(SettingsService.Theme item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item == SettingsService.Theme.DARK ? settings.t("settings.theme.dark") : settings.t("settings.theme.light"));
                }
            }
        });

        HBox langRow = new HBox(12, langLabel, langBox);
        langRow.setAlignment(Pos.CENTER_LEFT);

        HBox themeRow = new HBox(12, themeLabel, themeBox);
        themeRow.setAlignment(Pos.CENTER_LEFT);

        Button close = new Button();
        close.textProperty().bind(settings.bindText("settings.close"));
        close.setOnAction(e -> dialog.close());

        HBox actions = new HBox(close);
        actions.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(14, title, langRow, themeRow, actions);
        card.getStyleClass().add("settings-card");
        card.setPadding(new Insets(18));

        Scene scene = new Scene(new VBox(card));
        scene.getStylesheets().setAll(owner.getScene().getStylesheets());

        dialog.setScene(scene);
        dialog.sizeToScene();
        dialog.show();
    }
}

