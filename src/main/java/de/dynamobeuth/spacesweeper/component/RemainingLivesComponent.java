package de.dynamobeuth.spacesweeper.component;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;

import java.io.IOException;

public class RemainingLivesComponent extends HBox {

    private SimpleIntegerProperty remainingLives = new SimpleIntegerProperty();

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

            if (delta == 1) {
                addLive();
            } else if (delta == -1) {
                if (remainingLives.get() > 0) {
                    removeLive();
                }
            } else {
                resetLives();
            }
        });
    }

    private void resetLives() {
        getChildren().removeAll();

        for (int i = 0; i < remainingLives.get(); i++) {
            addLive();
        }
    }

    private void addLive() {
        Region region =  new Region();
        region.getStyleClass().add("live");

        getChildren().add(region);
    }

    private void removeLive() {
        FadeTransition ft = new FadeTransition(Duration.millis(100), getChildren().get(getChildren().size() - 1));
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setCycleCount(7);
        ft.setAutoReverse(true);
        ft.setOnFinished(event -> getChildren().remove(getChildren().size() - 1));
        ft.play();
    }

    public int getRemainingLives() {
        return remainingLives.get();
    }

    public SimpleIntegerProperty remainingLivesProperty() {
        return remainingLives;
    }

    public void setRemainingLives(int remainingLives) {
        this.remainingLives.set(remainingLives);
    }

    public void increaseRemaingLifes() {
        remainingLives.set(remainingLives.get() + 1);
    }

    public void decreaseRemaingLifes() {
        remainingLives.set(remainingLives.get() - 1);
    }
}
