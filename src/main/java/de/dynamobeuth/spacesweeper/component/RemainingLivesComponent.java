package de.dynamobeuth.spacesweeper.component;

import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;

import java.io.IOException;

public class RemainingLivesComponent extends HBox {

    private SimpleIntegerProperty remainingLives = new SimpleIntegerProperty(Settings.INITIAL_LIVES);

    public ReadOnlyIntegerProperty remainingLivesProperty() {
        return remainingLives;
    }

    public int getRemainingLives() {
        return remainingLives.get();
    }

    public void increaseRemaingLifes() {
        remainingLives.set(remainingLives.get() + 1);
    }

    public void decreaseRemaingLifes() {
        remainingLives.set(remainingLives.get() - 1);
    }

    @FXML
    private void initialize() {
        for (int i = 0; i < remainingLives.get(); i++) {
            getChildren().add(createRegion());
        }

        remainingLives.addListener((observable, oldValue, newValue) -> {
            if (oldValue.intValue() < newValue.intValue()) {
                getChildren().add(createRegion());
            } else {
                try {
                    removeHeart();
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("GAME OVER!!!");
                }
            }
        });
    }

    private Region createRegion() {
        Region r =  new Region();

        r.getStyleClass().add("live");
        return r;
    }

    public RemainingLivesComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RemainingLivesComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void removeHeart() {
        FadeTransition ft = new FadeTransition(Duration.millis(100), getChildren().get(getChildren().size() - 1));
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setCycleCount(7);
        ft.setAutoReverse(true);
        ft.play();
        ft.setOnFinished(event -> getChildren().remove(getChildren().size() - 1));
    }
}
