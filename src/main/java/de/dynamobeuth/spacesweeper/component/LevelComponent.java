package de.dynamobeuth.spacesweeper.component;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class LevelComponent extends HBox {

    private SimpleIntegerProperty level = new SimpleIntegerProperty();

    public SimpleIntegerProperty levelProperty() {
        return level;
    }

    public int getLevel() {
        return level.get();
    }

    public void setLevel(int level) {
        this.level.set(level);
    }

    @FXML
    private Label lblLevel;

    @FXML
    private void initialize() {
        lblLevel.textProperty().bind(levelProperty().asString());
    }

    public LevelComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LevelComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
