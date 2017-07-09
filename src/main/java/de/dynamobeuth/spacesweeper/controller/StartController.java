package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.ScreenController;
import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import static de.dynamobeuth.multiscreen.animation.RotateScreenTransition.RotationMode.ROTATE_IN;
import static de.dynamobeuth.spacesweeper.util.Sound.Sounds.BACKGROUND_START;

public class StartController extends ScreenController {

    @FXML
    public Button closeButton;

    @FXML
    private Pane root;

    @FXML
    private Button btnStartGame;

    @FXML
    private Button btnHighscore;

    @Override
    public void onBeforeShow() {
        Sound.playBackground(BACKGROUND_START);
    }

    @Override
    protected void onShow() {
        logoAnimation();
    }

    private void logoAnimation() {
        Label logo = new Label("SpaceSweeper");
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(5000), logo);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(5000), logo);
        TranslateTransition translateLogo = new TranslateTransition(Duration.millis(5000), logo);

        rotateTransition.setByAngle(3 * 353);
        scaleTransition.setByX(3f);
        scaleTransition.setByY(2.5f);
        translateLogo.setByX(-200f);
        translateLogo.setByY(-150f);
        translateLogo.setByZ(0);

        ParallelTransition transition = new ParallelTransition();

        transition.getChildren().addAll(rotateTransition, scaleTransition, translateLogo);

        root.getChildren().add(logo);

        transition.setOnFinished(event -> sloganAnimation());

        transition.play();
    }

    private void sloganAnimation() {
        Label slogan = new Label("Der Letzte räumt den Weltraum auf!");
        TranslateTransition translateSlogan = new TranslateTransition(Duration.millis(2000), slogan);

        root.getChildren().add(slogan);
        translateSlogan.setByX(-300);
        translateSlogan.setByZ(0);
        translateSlogan.play();
    }

    @FXML
    void showGameScreenAction(ActionEvent event) {
//        getScreenManager().showScreen("game", new SlideScreenTransition());
        getScreenManager().showScreen("game", (new RotateScreenTransition()).setRotationMode(ROTATE_IN));
    }

    @FXML
    void showHighscoreScreenAction(ActionEvent event) {
        getScreenManager().showScreen("highscore", new SlideScreenTransition());
    }

    @FXML
    private void exitGame(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
