package de.dynamobeuth.spacesweeper.component;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ScoreComponent extends HBox {

    private SimpleIntegerProperty score = new SimpleIntegerProperty(0);

    public SimpleIntegerProperty scoreProperty() {
        return score;
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    @FXML
    private Label lblScore;

    @FXML
    private void initialize() {
        lblScore.textProperty().bind(scoreProperty().asString());
    }

    public ScoreComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ScoreComponent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
