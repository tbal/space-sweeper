package de.dynamobeuth.spacesweeper.component;

import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SoundComponent extends Pane {
    @FXML
    private ToggleButton soundToggleButton;

    @FXML
    private void initialize() {
        soundToggleButton.selectedProperty().bindBidirectional(Sound.soundEnabledProperty);
        soundToggleButton.visibleProperty().bind(Sound.soundAvailableProperty);
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
