package de.dynamobeuth.spacesweeper.component;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ScoreComponent extends HBox {

    @FXML
    private Label lblScore;

    private SimpleIntegerProperty score = new SimpleIntegerProperty(0);

    public ScoreComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ScoreComponent.fxml"));
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
        lblScore.textProperty().bind(scoreProperty().asString());
    }

    public SimpleIntegerProperty scoreProperty() {
        return score;
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }
}
