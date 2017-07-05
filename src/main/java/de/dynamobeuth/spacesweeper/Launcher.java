package de.dynamobeuth.spacesweeper;

import de.dynamobeuth.multiscreen.MultiScreenApplication;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Launcher extends MultiScreenApplication {

    public Launcher() {
        setTitle("Space Sweeper - Der letzte räumt den Weltraum auf");
        setSkin("bunt");
//        setStartScreen("splash");
    }

    public static void main(String[] args) {
        // fixes ungly texts, see: https://stackoverflow.com/questions/24254000/
        System.setProperty("prism.lcdtext", "false");

        launch(args);
    }

    @Override
    public boolean close() {
        Alert exitConfirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        exitConfirmDialog.setTitle("Spiel beenden");
        exitConfirmDialog.setHeaderText("Bist du sicher, dass du das Spiel beenden möchtest?");

        // bind info if dialog is currently open to public property, to allow reacting to it e.g. from within controllers
//        getScreenManager().closeRequestActiveProperty().bind(exitConfirmDialog.showingProperty());

        return exitConfirmDialog.showAndWait().get() == ButtonType.OK;
    }

//    @Override
//    protected void initStartScreen() {
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
//    }
}
