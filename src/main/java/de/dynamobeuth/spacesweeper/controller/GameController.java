package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.ScreenController;
import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.spacesweeper.component.LevelComponent;
import de.dynamobeuth.spacesweeper.component.RemainingLivesComponent;
import de.dynamobeuth.spacesweeper.component.ScoreComponent;
import de.dynamobeuth.spacesweeper.model.Game;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

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
            if (newValue) {
                game.pause();
                lblGameStatus.setText("PAUSE");
            } else {
                game.resume();
                lblGameStatus.setText("WEITER");
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

        scoreProperty().bind(game.scoreProperty());

        remainingLivesComponent.remainingLivesProperty().bind(game.remainingLivesProperty());
        levelComponent.levelProperty().bind(game.levelProperty());
        scoreComponent.scoreProperty().bind(scoreProperty());
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

//    // TODO: REMOVE, just for testing
//    @FXML
//    void showHighscoreScreenAction(ActionEvent event) {
//        try {
//            HighscoreController highscoreController = (HighscoreController) getScreenManager().getControllerByName("highscore");
//            highscoreController.showAddHighscoreEntryDialog = true;
//
//        } catch (InvalidNameException e) {
//            e.printStackTrace();
//        }
//
//        getScreenManager().showScreen("highscore", new SlideScreenTransition());
//    }

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
                "Dein aktueller Spielstand wird nicht gespeichert!");

        if (backToHomeConfirmDialog.showAndWait().get() == ButtonType.OK) {
            getScreenManager().showScreen("start", new RotateScreenTransition());
            game.stop();
            return true;
        } else {
            game.resume();
            return false;
        }
    }

    public SimpleIntegerProperty scoreProperty() { return score; }
}
