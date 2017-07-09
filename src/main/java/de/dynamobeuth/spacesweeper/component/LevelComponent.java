package de.dynamobeuth.spacesweeper.component;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class LevelComponent extends HBox {

    @FXML
    private Label lblLevel;

    private SimpleIntegerProperty level = new SimpleIntegerProperty();

    public LevelComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LevelComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        lblLevel.textProperty().bind(levelProperty().asString());
    }

    public SimpleIntegerProperty levelProperty() {
        return level;
    }

    public int getLevel() {
        return level.get();
    }

    public void setLevel(int level) {
        this.level.set(level);
    }
}
