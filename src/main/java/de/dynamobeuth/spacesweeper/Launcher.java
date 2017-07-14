package de.dynamobeuth.spacesweeper;

import com.firebase.client.Firebase;
import de.dynamobeuth.multiscreen.MultiScreenApplication;
import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.application.Platform;
import de.dynamobeuth.spacesweeper.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import static de.dynamobeuth.multiscreen.animation.RotateScreenTransition.RotationMode.ROTATE_IN;

/**
 * Application starting point
 */
public class Launcher extends MultiScreenApplication {

    /**
     * Constructor
     */
    public Launcher() {
        setTitle("Space Sweeper - Der letzte räumt den Weltraum auf");
        setSkin(Settings.SKIN);
    }

    /**
     * Main method
     *
     * @param args Main method arguments
     */
    public static void main(String[] args) {
        // fixes ungly text effects on lcd screens, see: https://stackoverflow.com/questions/24254000/
        System.setProperty("prism.lcdtext", "false");

        launch(args);
    }

    /**
     * Set custom stage configuration
     */
    @Override
    public void configureStage() {
        initWindowMovingUsingMouse();

        stage.setResizable(false);

        // transparent screen background
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getScene().setFill(null);
        getScreenManager().setStyle("-fx-background-color: transparent");
    }

    /**
     * Sets event handlers to move the application by dragging+dropping the window using the mouse
     */
    private void initWindowMovingUsingMouse() {
        final double[] screenOffsetX = new double[1];
        final double[] screenOffsetY = new double[1];

        stage.getScene().setOnMousePressed(event -> {
            screenOffsetX[0] = stage.getX() - event.getScreenX();
            screenOffsetY[0] = stage.getY() - event.getScreenY();
        });

        stage.getScene().setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + screenOffsetX[0]);
            stage.setY(event.getScreenY() + screenOffsetY[0]);
        });
    }

    /**
     * Own handler for application close request.
     * Show confirm dialog before exiting application.
     *
     * @return True if application should be closed, false if not.
     */
    @Override
    public boolean close() {
        Alert exitConfirmDialog = new Alert(Alert.AlertType.CONFIRMATION, getScreenManager());

        exitConfirmDialog.setTitle("Spiel beenden");
        exitConfirmDialog.setHeaderText("Spiel beenden?");
        exitConfirmDialog.setContentText("Bist du sicher, dass du das Spiel beenden möchtest?");

        if (exitConfirmDialog.showAndWait().get() == ButtonType.OK) {
            // close firebase connection
            Platform.runLater(() -> Firebase.goOffline());

            return true;
        } else {
            return false;
        }
    }

    /**
     * Own implementation for showing first screen to allow starting animation-in of start-screen
     */
    @Override
    protected void initStartScreen() {
        getScreenManager().showScreen("start", (new RotateScreenTransition()).setRotationMode(ROTATE_IN).setDuration(Duration.seconds(2)));
    }
}
