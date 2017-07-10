package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.ScreenController;
import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import de.dynamobeuth.spacesweeper.component.LevelComponent;
import de.dynamobeuth.spacesweeper.component.RemainingLivesComponent;
import de.dynamobeuth.spacesweeper.component.ScoreComponent;
import de.dynamobeuth.spacesweeper.model.Game;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import javax.naming.InvalidNameException;
import java.util.Optional;

import static de.dynamobeuth.spacesweeper.util.Sound.Sounds.BACKGROUND_GAME;

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
    }

    @Override
    protected void onFirstShow() {
        game = new Game(gameContainer, getScreenManager());

        lblGameStatus.textProperty().bind(game.stateProperty().asString()); // TODO: remove, just debugging

        game.gameOverProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                handleGameOver();
            }
        });

        scoreProperty().bind(game.scoreProperty());

        remainingLivesComponent.remainingLivesProperty().bind(game.remainingLivesProperty());
        levelComponent.levelProperty().bind(game.levelProperty());
        scoreComponent.scoreProperty().bind(scoreProperty());

        // TODO: esc on game shows pause dialog
//        root.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//            if (event.getCode() == KeyCode.ESCAPE) {
//                event.consume();
//
//                game.pause();
//
//                Alert pauseGameDialog = new Alert(Alert.AlertType.CONFIRMATION);
//                pauseGameDialog.getDialogPane().getStylesheets().add("/de/dynamobeuth/spacesweeper/skin/default/css/modal-dialog.css");
//                pauseGameDialog.initStyle(StageStyle.UNDECORATED);
//                pauseGameDialog.setHeaderText("Spiel pausiert");
//                pauseGameDialog.setContentText(
//                        "TODO!");
//
//                if (pauseGameDialog.showAndWait().get() == ButtonType.OK) {
//                    game.resume();
//                } else {
//                    getScreenManager().showScreen("start", new RotateScreenTransition());
//                }
//            }
//        });
    }

    @Override
    protected void onShow() {
        game.start();
//        // TODO: REMOVE, just a eval test
//        ScaleTransition st = new ScaleTransition(Duration.millis(100), lblGame);
//        st.setFromX(1);
//        st.setFromY(1);
//        st.setToX(2);
//        st.setToY(2);
//        st.setAutoReverse(true);
//        st.setCycleCount(2);
//        st.play();
    }

    @FXML
    private void exitGame(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    private boolean backHome(ActionEvent event) {
        game.pause();

        Alert backToHomeConfirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        backToHomeConfirmDialog.getDialogPane().getStylesheets().add("/de/dynamobeuth/spacesweeper/skin/default/css/modal-dialog.css");
        backToHomeConfirmDialog.initStyle(StageStyle.UNDECORATED);
        backToHomeConfirmDialog.setHeaderText("Spiel abbrechen?");
        backToHomeConfirmDialog.setContentText(
                "Bist du sicher, dass du zurück zum Start möchtest?\n" +
                "Dein aktueller Spielstand geht dabei verloren!");

        if (backToHomeConfirmDialog.showAndWait().get() == ButtonType.OK) {
            getScreenManager().showScreen("start", new RotateScreenTransition());
            game.stop();
            return true;
        } else {
            game.resume();
            return false;
        }
    }

    private void handleGameOver() {
        Alert gameOverDialog = new Alert(null);
        gameOverDialog.getDialogPane().getStylesheets().add("/de/dynamobeuth/spacesweeper/skin/default/css/modal-dialog.css");
        Stage dialogStage = (Stage) gameOverDialog.getDialogPane().getScene().getWindow();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.getScene().setFill(null);

        gameOverDialog.setHeaderText("Game Over!");
        gameOverDialog.setContentText("Du hast " + scoreProperty().get() + " Punkte erreicht.\nTrage dich in die Highscore ein oder spiele gleich noch einmal!");

        // TODO: try own Button implementation with included sound
        ButtonType btnBackHome = new ButtonType("zum Menü");
        ButtonType btnNewGame = new ButtonType("Spiel neustarten");
        ButtonType btnHighscore = new ButtonType("in Highscore eintragen");

        gameOverDialog.getButtonTypes().setAll(btnBackHome, btnNewGame, btnHighscore);

        gameOverDialog.resultProperty().addListener((observable, oldValue, newValue) -> {
            Optional<ButtonType> result = Optional.ofNullable(gameOverDialog.getResult());

            if (result.get() == btnBackHome) {
                getScreenManager().showScreen("start", new RotateScreenTransition());

            } else if (result.get() == btnNewGame) {
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
