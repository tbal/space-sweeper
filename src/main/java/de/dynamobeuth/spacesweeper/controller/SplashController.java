package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.ScreenController;
import de.dynamobeuth.spacesweeper.util.Helper;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class SplashController extends ScreenController {

    @FXML
    private Pane root;

    private Timeline timeout;

    @Override
    protected void onShow() {
        timeout = Helper.setTimeout(() -> getScreenManager().showScreen("start"), 1000);

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
    private void showStartScreenAction() {
        if (timeout != null) {
            timeout.stop();
        }

        getScreenManager().showScreen("start");
    }
}
