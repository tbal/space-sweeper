package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import de.dynamobeuth.spacesweeper.component.LevelComponent;
import de.dynamobeuth.spacesweeper.component.RemainingLivesComponent;
import de.dynamobeuth.spacesweeper.component.ScoreComponent;
import de.dynamobeuth.spacesweeper.control.Alert;
import de.dynamobeuth.spacesweeper.model.Game;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import javax.naming.InvalidNameException;
import java.util.Optional;

import static de.dynamobeuth.spacesweeper.util.Sound.Sounds.BACKGROUND_GAME;

/**
 * Game Screen Controller
 */
public class GameController extends AbstractController {

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
    private Button btnHighscore;

    @FXML
    private Button btnStartScreen;

    private Game game;

    private SimpleIntegerProperty score = new SimpleIntegerProperty();

    @Override
    protected void prepare() {
        // pause if the app wants to be closed and resume if the close intention was canceled
        getScreenManager().closeRequestActiveProperty().addListener((observable, oldValue, newValue) -> {
            if (!getScreenManager().currentScreenNameMatches("game")) {
                return;
            }

            if (newValue) {
                game.pause();
            } else {
                game.resume();
            }
        });
    }

    @Override
    protected void onBeforeShow() {
        Sound.playBackground(BACKGROUND_GAME);

        if (game != null) {
            game.reset();
        }
    }

    @Override
    protected void onFirstShow() {
        game = new Game(gameContainer, getScreenManager());


        game.gameOverProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                handleGameOver();
            }
        });

        scoreProperty().bind(game.scoreProperty());

        remainingLivesComponent.remainingLivesProperty().bind(game.remainingLivesProperty());
        levelComponent.levelProperty().bind(game.levelProperty());
        scoreComponent.scoreProperty().bind(scoreProperty());
    }

    @Override
    protected void onShow() {
        game.start();
    }

    @FXML
    protected void backHome(ActionEvent event) {
        game.pause();

        Alert backHomeDialog = new Alert(Alert.AlertType.CONFIRMATION, getScreenManager());

        backHomeDialog.setHeaderText("Spiel abbrechen?");
        backHomeDialog.setContentText(
                "Bist du sicher, dass du zurück zum Start möchtest?\n" +
                "Dein aktueller Spielstand geht dabei verloren!");

        if (backHomeDialog.showAndWait().get() == ButtonType.OK) {
            getScreenManager().showScreen("start", new RotateScreenTransition());

            game.stop();
        } else {
            game.resume();
        }
    }

    /**
     * Handle if game is over
     */
    private void handleGameOver() {
        Alert gameOverDialog = new Alert(null, getScreenManager());

        gameOverDialog.setHeaderText("Game Over!");
        gameOverDialog.setContentText("Du hast " + scoreProperty().get() + " Punkte erreicht.\nTrage dich in die Highscore ein oder spiele gleich noch einmal!");

        ButtonType btnBackHome = new ButtonType("zum Menü");
        ButtonType btnNewGame = new ButtonType("Spiel neustarten");
        ButtonType btnHighscore = new ButtonType("in Highscore eintragen");

        gameOverDialog.getButtonTypes().setAll(btnBackHome, btnNewGame, btnHighscore);

        gameOverDialog.resultProperty().addListener((observable, oldValue, newValue) -> {
            Optional<ButtonType> result = Optional.ofNullable(gameOverDialog.getResult());

            if (result.get() == btnBackHome) {
                getScreenManager().showScreen("start", new RotateScreenTransition());
                game.stop();

            } else if (result.get() == btnNewGame) {
                game.reset();
                game.start();

            } else if (result.get() == btnHighscore) {
                try {
                    HighscoreController highscoreController = (HighscoreController) getScreenManager().getControllerByName("highscore");
                    highscoreController.showAddHighscoreEntryDialog = true;

                } catch (InvalidNameException e) {
                    e.printStackTrace();
                }

                getScreenManager().showScreen("highscore", new SlideScreenTransition());
            }
        });

        gameOverDialog.show();
    }

    public SimpleIntegerProperty scoreProperty() { return score; }
}
