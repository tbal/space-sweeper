package de.dynamobeuth.spacesweeper.component;

import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.io.IOException;

public class RemainingLifeComponent extends HBox {

    public int getRemainingLives() {
        return remainingLives.get();
    }

    public ReadOnlyIntegerProperty remainingLivesProperty() {
        return remainingLives;
    }

    public void increaseRemaingLifes() {
        remainingLives.set(remainingLives.get() + 1);
    }

    public void decreaseRemaingLifes() {
        remainingLives.set(remainingLives.get() - 1);
    }

    private SimpleIntegerProperty remainingLives = new SimpleIntegerProperty(Settings.INITIAL_LIVES);


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
                    getChildren().remove(getChildren().size() - 1);
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

    public RemainingLifeComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RemainingLifeComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
