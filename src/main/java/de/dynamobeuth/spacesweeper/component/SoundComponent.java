package de.dynamobeuth.spacesweeper.component;

import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SoundComponent extends Pane {

    @FXML
    private ToggleButton btnToggleSound;

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

    @FXML
    private void initialize() {
        btnToggleSound.selectedProperty().bindBidirectional(Sound.soundEnabledProperty);
        btnToggleSound.visibleProperty().bind(Sound.soundAvailableProperty);
    }
}
