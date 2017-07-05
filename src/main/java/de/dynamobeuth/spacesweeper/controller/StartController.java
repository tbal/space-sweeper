package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.ScreenController;
import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

import static de.dynamobeuth.multiscreen.animation.RotateScreenTransition.RotationMode.ROTATE_IN;

public class StartController extends ScreenController {

    @FXML
    private Node root;

    @FXML
    private Button btnStartGame;

    @FXML
    private Button btnHighscore;

    @FXML
    void showGameScreenAction(ActionEvent event) {
//        getScreenManager().showScreen("game", new SlideScreenTransition());
        getScreenManager().showScreen("game", (new RotateScreenTransition()).setRotationMode(ROTATE_IN));
    }

    @FXML
    void showHighscoreScreenAction(ActionEvent event) {
        getScreenManager().showScreen("highscore", new SlideScreenTransition());
    }

}
