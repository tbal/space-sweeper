package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import de.dynamobeuth.spacesweeper.component.SoundComponent;
import de.dynamobeuth.spacesweeper.control.Button;
import de.dynamobeuth.spacesweeper.util.Helper;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.shape.*;
import javafx.util.Duration;

import static de.dynamobeuth.multiscreen.animation.RotateScreenTransition.RotationMode.ROTATE_IN;
import static de.dynamobeuth.spacesweeper.util.Sound.Sounds.BACKGROUND_START;

/**
 * Start Screen Controller
 */
public class StartController extends AbstractController {

    @FXML
    private Button closeButton;

    @FXML
    private SoundComponent soundComponent;

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
    protected void onBeforeFirstShow() {
        btnStartGame.setOpacity(0);
        btnHighscore.setOpacity(0);
        soundComponent.setOpacity(0);
        closeButton.setOpacity(0);
    }

    @Override
    protected void onFirstShow() {
        showControlButtons();
        showLogo();
    }

    @Override
    protected void onShow() {
        // multiply background rockets caused by multiple show action of start screen are by intention
        addBackgroundRocket();
    }

    /**
     * Animate-in close and toggle sound button
     */
    private void showControlButtons() {
        TranslateTransition translatecloseButton = new TranslateTransition(Duration.millis(1750), closeButton);
        TranslateTransition translateSoundComponent = new TranslateTransition(Duration.millis(1750), soundComponent);

        translatecloseButton.setToX(0);
        translateSoundComponent.setToX(0);

        soundComponent.setOpacity(1);
        closeButton.setOpacity(1);

        translatecloseButton.play();
        translateSoundComponent.play();
    }

    /**
     * Fancy logo animation
     */
    private void showLogo() {
        Image image = new Image(getClass().getResource("/de/dynamobeuth/spacesweeper/skin/" + getScreenManager().getSkin() + "/img/logo.png").toExternalForm());
        ImageView logo = new ImageView(image);

        Bloom effect = new Bloom();
        effect.setThreshold(0.01);
        logo.setEffect(effect);

        ScaleTransition scaleDownLogo = new ScaleTransition(Duration.millis(3000), logo);
        scaleDownLogo.setToX(0.05);
        scaleDownLogo.setToY(0.05);
        scaleDownLogo.setAutoReverse(true);
        scaleDownLogo.setCycleCount(2);

        TranslateTransition translateLogo = new TranslateTransition(Duration.millis(3000), logo);
        translateLogo.setToY(-150);
        translateLogo.setToY(-150);

        ParallelTransition transition = new ParallelTransition();
        transition.setOnFinished(event -> sloganAnimation());
        transition.getChildren().addAll(scaleDownLogo, translateLogo);

        root.getChildren().add(logo);

        transition.play();
    }

    /**
     * Fancy slogan animation
     */
    private void sloganAnimation() {
        Label slogan = new Label("Der Letzte räumt den Weltraum auf!");
        slogan.getStyleClass().add("slogan");
        slogan.setEffect(new Bloom());
        slogan.setTranslateY(-300);

        TranslateTransition translateSlogan = new TranslateTransition(Duration.millis(1000), slogan);
        translateSlogan.setByY(250);
        translateSlogan.setByZ(0);
        translateSlogan.setOnFinished(event -> showMenuButtons());

        root.getChildren().add(slogan);

        translateSlogan.play();
    }

    /**
     * Fancy menu button animation
     */
    private void showMenuButtons() {
        TranslateTransition translateHighscoreButton = new TranslateTransition(Duration.millis(500), btnHighscore);
        TranslateTransition translateStartGameButton = new TranslateTransition(Duration.millis(500), btnStartGame);

        translateHighscoreButton.setToY(0);
        translateStartGameButton.setToY(0);

        btnHighscore.setOpacity(1);
        btnStartGame.setOpacity(1);

        translateStartGameButton.setOnFinished(event -> translateHighscoreButton.play());
        translateHighscoreButton.setOnFinished(event -> addBackgroundRocket());

        translateStartGameButton.play();
    }

    /**
     * Fancy lost rocket animation
     */
    private void addBackgroundRocket() {
        Region rocket = new Region();
        rocket.setMinHeight(60);
        rocket.setMaxHeight(60);
        rocket.setMinWidth(60);
        rocket.setMaxWidth(60);
        rocket.getStyleClass().add("spaceship");
        rocket.setOpacity(0);

        PathElement[] path = {
            new MoveTo(Helper.randomInRange(-450, 450), Helper.randomInRange(-350, 350)),
            new ArcTo(100, 100, 0, 100, 400, false, false),
            new LineTo(Helper.randomInRange(-450, 450), Helper.randomInRange(-350, 350)),
            new ArcTo(100, 100, 0, 400, 300, false, false),
            new LineTo(400, 100),
            new ArcTo(100, 100, 0, 0, 100, false, false),
            new LineTo(Helper.randomInRange(-450, 450), Helper.randomInRange(-350, 350)),
            new ArcTo(100, 100, 0, 300, 0, false, false),
            new LineTo(100, 0),
            new ArcTo(100, 100, 0, 0, 100, false, false),
            new LineTo(Helper.randomInRange(-450, 450), Helper.randomInRange(-350, 350)),
            new ArcTo(100, 100, 0, 0, 100, false, false),
            new LineTo(0, 300),
            new ArcTo(100, 100, 0, 0, 100, false, false),
            new LineTo(Helper.randomInRange(-450, 450), Helper.randomInRange(-350, 350)),
            new ClosePath()
        };

        Path road = new Path();
        road.getElements().addAll(path);

        PathTransition anim = new PathTransition();
        anim.setNode(rocket);
        anim.setPath(road);
        anim.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        anim.setInterpolator(Interpolator.LINEAR);
        anim.setDuration(new Duration(70000));
        anim.setCycleCount(Timeline.INDEFINITE);

        FadeTransition fadeInRocket = new FadeTransition(Duration.millis(1000), rocket);
        fadeInRocket.setToValue(1);

        root.getChildren().add(rocket);

        rocket.toBack();

        anim.play();
        fadeInRocket.play();
    }

    /**
     * Action handler for start game button
     *
     * @param event ActionEvent
     */
    @FXML
    private void showGameScreenAction(ActionEvent event) {
        getScreenManager().showScreen("game", (new RotateScreenTransition()).setRotationMode(ROTATE_IN));
    }

    /**
     * Action handler for show highscore button
     *
     * @param event ActionEvent
     */
    @FXML
    private void showHighscoreScreenAction(ActionEvent event) {
        getScreenManager().showScreen("highscore", new SlideScreenTransition());
    }
}
