package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.spacesweeper.control.Button;
import de.dynamobeuth.multiscreen.ScreenController;
import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
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
        Platform.runLater(() -> btnStartGame.requestFocus());
    }

    @Override
    protected void onFirstShow() {
        logoAnimation();
    }

    private void logoAnimation() {
        Image image = new Image(getClass().getResource("../skin/default/img/logo.png").toExternalForm());
        ImageView logo = new ImageView(image);

        ScaleTransition scaleDownLogo = new ScaleTransition(Duration.millis(2000), logo);
        TranslateTransition translateLogo = new TranslateTransition(Duration.millis(3000), logo);

        scaleDownLogo.setToX(0);
        scaleDownLogo.setToY(0);
        scaleDownLogo.setAutoReverse(true);
        scaleDownLogo.setCycleCount(2);

        translateLogo.setToY(-220);
        translateLogo.setToY(-220);

        ParallelTransition transition = new ParallelTransition();

        transition.getChildren().addAll(scaleDownLogo, translateLogo);

        root.getChildren().add(logo);

        transition.setOnFinished(event -> sloganAnimation());

        transition.play();
    }

    private void sloganAnimation() {
        Label slogan = new Label("Der Letzte räumt den Weltraum auf!");
        slogan.getStyleClass().add("slogan");
        slogan.setEffect(new Bloom());
        TranslateTransition translateSlogan = new TranslateTransition(Duration.millis(2000), slogan);

        root.getChildren().add(slogan);
        slogan.setTranslateY(-300);
        translateSlogan.setByY(300);
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
