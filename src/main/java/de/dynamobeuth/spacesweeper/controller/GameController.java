package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.ScreenController;
import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import de.dynamobeuth.spacesweeper.model.Game;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.naming.InvalidNameException;

public class GameController extends ScreenController {
    @FXML
    private Pane gameContainer;

    @FXML
    private Label lblLives;

    @FXML
    private Label lblScore;

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

    @FXML
    private void initialize() {
        System.out.println("game controller init");
    }

    @Override
    protected void prepare() {
        // pause if the app wants to be closed and resume if the close intention was canceled
        getScreenManager().closeRequestActiveProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblGameStatus.setText("PAUSE");
            } else {
                lblGameStatus.setText("WEITER");
            }
        });

//        try {
//            HighscoreController highscoreController = (HighscoreController) getScreenManager().getControllerByName("highscore");
//
//            lblGame.textProperty().bind(highscoreController.playerNameProperty());
//
//        } catch (InvalidNameException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onFirstShow() {
        new Game(gameContainer, getScreenManager());
    }

    @Override
    protected void onShow() {
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
