package de.dynamobeuth.spacesweeper;

import de.dynamobeuth.multiscreen.ScreenManager;
import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class SpaceSweeperApp extends Application {

    private Stage stage;

    public SimpleBooleanProperty exitConfirmDialogShowing = new SimpleBooleanProperty(false);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        // fixes ungly texts
        // https://stackoverflow.com/questions/24254000/how-to-force-anti-aliasing-in-javafx-fonts
        System.setProperty("prism.lcdtext", "false");

        stage.setOnCloseRequest(e -> {
            e.consume();
            exit();
        });

        ScreenManager screenManager = new ScreenManager(this);
        screenManager.initScreens();
        screenManager.initStylesheets(Settings.SKIN);

        screenManager.showScreen("splash");
//        screenManager.showScreen("splash", (sm, currentScreen, nextScreen, onFinished) -> {
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

        stage.setTitle("Space Sweeper - Der letzte räumt den Weltraum auf");
        stage.setScene(new Scene(screenManager /*, Settings.COL_COUNT * Settings.COL_WIDTH, Settings.COL_HEIGHT */));
        stage.show();
    }

    private void exit() {
        Alert exitConfirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        exitConfirmDialog.setTitle("Spiel beenden");
        exitConfirmDialog.setHeaderText("Bist du sicher, dass du das Spiel beenden möchtest?");

        // bind info if dialog is currently open to public property, to allow reacting to it e.g. from within controllers
        exitConfirmDialogShowing.bind(exitConfirmDialog.showingProperty());

        if (exitConfirmDialog.showAndWait().get() == ButtonType.OK) {
            stage.close();
        }
    }
}
