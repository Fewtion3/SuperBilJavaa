package com.example.ui;

import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.Map;

public class Sidebar extends VBox {
    private final Map<String, Button> buttons = new LinkedHashMap<>();
    private final Label titleLabel = new Label();

    public Sidebar(String title) {
        getStyleClass().add("sidebar");
        setPadding(new Insets(22));
        setSpacing(10);

        titleLabel.setText(title);
        titleLabel.getStyleClass().add("nav-title");
        getChildren().add(titleLabel);
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public Button addItem(String key, StringBinding labelBinding, Runnable onClick) {
        Button b = new Button();
        b.textProperty().bind(labelBinding);
        b.getStyleClass().add("nav-btn");
        b.setOnAction(e -> onClick.run());
        buttons.put(key, b);
        getChildren().add(b);
        return b;
    }

    public void setActive(String key) {
        for (Map.Entry<String, Button> e : buttons.entrySet()) {
            e.getValue().getStyleClass().remove("nav-btn-active");
        }
        Button b = buttons.get(key);
        if (b != null) b.getStyleClass().add("nav-btn-active");
    }
}

