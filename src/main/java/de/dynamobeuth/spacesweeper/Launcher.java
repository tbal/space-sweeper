package de.dynamobeuth.spacesweeper;

import de.dynamobeuth.multiscreen.MultiScreenApplication;
import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;

import static de.dynamobeuth.multiscreen.animation.RotateScreenTransition.RotationMode.ROTATE_IN;

public class Launcher extends MultiScreenApplication {

    public Launcher() {
        setTitle("Space Sweeper - Der letzte räumt den Weltraum auf");
        setSkin(Settings.SKIN);
        setStartScreen("splash");
    }

    public static void main(String[] args) {
        // fixes ungly text effects on lcd screens, see: https://stackoverflow.com/questions/24254000/
        System.setProperty("prism.lcdtext", "false");

        launch(args);
    }

    @Override
    public void configureStage() {
        stage.setResizable(false);
    }

    @Override
    public boolean close() {
        Alert exitConfirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        exitConfirmDialog.setTitle("Spiel beenden");
        exitConfirmDialog.setHeaderText("Bist du sicher, dass du das Spiel beenden möchtest?");

        return exitConfirmDialog.showAndWait().get() == ButtonType.OK;
    }

    @Override
    protected void initStartScreen() {
        getScreenManager().showScreen("splash", (new RotateScreenTransition()).setRotationMode(ROTATE_IN).setDuration(Duration.seconds(2)));

//        getScreenManager().showScreen("splash", (sm, currentScreen, nextScreen, onFinished) -> {
//            sm.getChildren().add(nextScreen);
//
//            PauseTransition pauseTransition = new PauseTransition(Duration.millis(1000));
//            pauseTransition.setOnFinished(e -> {
//                if (onFinished != null) {
//                    onFinished.handle(e);
//                }
//
//                sm.showScreen("start");
//            });
//            pauseTransition.play();
//        });
    }
}
