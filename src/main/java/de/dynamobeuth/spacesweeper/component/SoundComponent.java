package de.dynamobeuth.spacesweeper.component;

import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class SoundComponent extends Pane {
    @FXML
    private CheckBox soundCheckBox;

    @FXML
    private void initialize() {
        soundCheckBox.selectedProperty().bindBidirectional(Sound.soundEnabledProperty);
        soundCheckBox.getStylesheets().add(getClass().getResource("checkbox.css").toExternalForm());
    }

    public SoundComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SoundComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
