package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.ScreenController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class SplashController extends ScreenController {

    @FXML
    private Node root;

    @FXML
    public void beforeShow() {
//        root.setOpacity(0);
//        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), root);
//        fadeTransition.setFromValue(0.0);
//        fadeTransition.setToValue(1.0);
//        fadeTransition.setCycleCount(3);
//        fadeTransition.setAutoReverse(true);
//        fadeTransition.setOnFinished(e -> getScreenManager().showScreen("start"));
//        fadeTransition.play();
    }

    @FXML
    private void showStartScreenAction(MouseEvent mouseEvent) {
        getScreenManager().showScreen("start");
    }
}
