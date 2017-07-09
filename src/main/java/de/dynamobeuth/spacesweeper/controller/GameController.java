package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.ScreenController;
import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import de.dynamobeuth.spacesweeper.component.LevelComponent;
import de.dynamobeuth.spacesweeper.component.RemainingLivesComponent;
import de.dynamobeuth.spacesweeper.component.ScoreComponent;
import de.dynamobeuth.spacesweeper.model.Game;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import javax.naming.InvalidNameException;

public class GameController extends ScreenController {

    @FXML
    public Button closeButton;

    @FXML
    private RemainingLivesComponent remainingLivesComponent;

    @FXML
    private LevelComponent levelComponent;

    @FXML
    private ScoreComponent scoreComponent;

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
    private Pane root;

    @FXML
    private Button btnHighscore;

    @FXML
    private Button btnStartScreen;

    @FXML
    private void initialize() {
        System.out.println("game controller init");
    }

    public SimpleIntegerProperty scoreProperty() { return score; }

    private SimpleIntegerProperty score = new SimpleIntegerProperty();

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
        Game game = new Game(gameContainer, getScreenManager(), remainingLivesComponent);
        levelComponent.levelProperty().bind(game.levelProperty());
        scoreProperty().bind(game.scoreProperty());
        scoreComponent.scoreProperty().bind(scoreProperty());
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

    @FXML
    private void exitGame(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    void backHome(ActionEvent event) {

    }
}
