package com.example.ui;

import com.example.service.SettingsService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TopHeader {
    public Node render(SettingsService settings, String pageTitleKey, String subtitleKey, Runnable onSettingsClick) {
        Label title = new Label();
        title.getStyleClass().add("topbar-title");

        Label sub = new Label();
        sub.getStyleClass().add("topbar-subtitle");

        VBox titles = new VBox(2, title, sub);

        TextField search = new TextField();
        search.promptTextProperty().bind(settings.bindText("top.search"));
        search.getStyleClass().add("topbar-search");
        search.setMaxWidth(360);

        title.textProperty().bind(settings.bindText(pageTitleKey));
        sub.textProperty().bind(settings.bindText(subtitleKey));

        Button settingsBtn = new Button();
        settingsBtn.textProperty().bind(settings.bindText("top.settings"));
        settingsBtn.getStyleClass().add("topbar-settings");
        settingsBtn.setOnAction(e -> onSettingsClick.run());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox bar = new HBox(14, titles, spacer, search, settingsBtn);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(14, 22, 14, 22));
        bar.getStyleClass().add("topbar");
        return bar;
    }
}

