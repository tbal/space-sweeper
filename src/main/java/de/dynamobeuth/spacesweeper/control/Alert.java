package de.dynamobeuth.spacesweeper.control;

import de.dynamobeuth.multiscreen.ScreenManager;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Extended Alert with custom styling shade/unshade effect on show/hide
 */
public class Alert extends javafx.scene.control.Alert {

    public Alert(AlertType alertType, ScreenManager screenManager) {
        super(alertType);

        getDialogPane().getStylesheets().add("/de/dynamobeuth/spacesweeper/skin/" + screenManager.getSkin() + "/css/control/dialog.css");

        Stage dialogStage = (Stage) getDialogPane().getScene().getWindow();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.getScene().setFill(null);

        showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                screenManager.shadeScreen();
            } else {
                screenManager.unshadeScreen();
            }
        });
    }
}
