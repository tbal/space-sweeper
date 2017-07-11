package de.dynamobeuth.spacesweeper.control;

import de.dynamobeuth.multiscreen.ScreenManager;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TextInputDialog extends javafx.scene.control.TextInputDialog {

    public TextInputDialog(String defaultValue, ScreenManager screenManager) {
        super(defaultValue);

        getDialogPane().getStylesheets().add("/de/dynamobeuth/spacesweeper/skin/default/css/control/dialog.css");

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
