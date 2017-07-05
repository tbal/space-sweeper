package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.ScreenController;
import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import de.dynamobeuth.spacesweeper.SpaceSweeperApp;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import javax.naming.InvalidNameException;

public class GameController extends ScreenController {
    @FXML
    private Label lblGameStatus;

    @FXML
    private Label lblGame;

    @FXML
    private Node root;

    @FXML
    private Button btnHighscore;

    @FXML
    private Button btnStartScreen;

    private boolean bindingsSet = false;

    @FXML
    private void initialize() {
        System.out.println("game controller init");
    }

    protected void prepare() {
        // pause if the app wants to be closed and resume if the exit intention was canceled
        ((SpaceSweeperApp) getApplication()).exitConfirmDialogShowing.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblGameStatus.setText("PAUSE");
            } else {
                lblGameStatus.setText("WEITER");
            }
        });

        try {
            HighscoreController highscoreController = (HighscoreController) getScreenManager().getControllerByName("highscore");

            lblGame.textProperty().bind(highscoreController.playerNameProperty());

            bindingsSet = true;
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
    }

    protected void beforeShow() {
        System.out.println("beforeshow game view");
    }

    protected void show() {
        System.out.println("show game view");

        ScaleTransition st = new ScaleTransition(Duration.millis(100), lblGame);
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(2);
        st.setToY(2);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    @FXML
    void showHighscoreScreenAction(ActionEvent event) {
        try {
            HighscoreController highscoreController = (HighscoreController) getScreenManager().getControllerByName("highscore");
            highscoreController.showAddHighscoreEntryDialog = true;

        } catch (InvalidNameException e) {
            e.printStackTrace();
        }

        getScreenManager().showScreen("highscore", new SlideScreenTransition());
    }

    @FXML
    void showStartScreenAction(ActionEvent event) {
        getScreenManager().showScreen("start", new RotateScreenTransition());
    }
}
