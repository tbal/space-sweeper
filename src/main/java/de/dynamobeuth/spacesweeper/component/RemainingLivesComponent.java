package de.dynamobeuth.spacesweeper.component;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Component to display the remaining lives
 */
public class RemainingLivesComponent extends HBox {

    private SimpleIntegerProperty remainingLives = new SimpleIntegerProperty();

    /**
     * Initialize component
     */
    public RemainingLivesComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RemainingLivesComponent.fxml"));
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
        remainingLives.addListener((observable, oldValue, newValue) -> {
            int delta = newValue.intValue() - oldValue.intValue();

            if (delta < 0) {
                if (remainingLives.get() >= 0) {
                    removeLive();
                }
            } else {
                resetLives();
            }
        });
    }

    /**
     * Resets lives
     */
    private void resetLives() {
        int currentLiveNodes = getChildren().size();

        for (int i = 0; i < remainingLives.get(); i++) {
            if (currentLiveNodes < (i + 1)) {
                addRegion();
            }

            getChildren().get(i).setOpacity(1);
        }
    }

    /**
     * Add new region to display one live
     */
    private void addRegion() {
        Region region =  new Region();
        region.getStyleClass().add("live");
        getChildren().add(region);
    }

    /**
     * Removes a live
     */
    private void removeLive() {
        FadeTransition ft = new FadeTransition(Duration.millis(100), getChildren().get(remainingLives.get()));
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setCycleCount(7);
        ft.setAutoReverse(true);
        ft.play();
    }

    public SimpleIntegerProperty remainingLivesProperty() {
        return remainingLives;
    }
}
