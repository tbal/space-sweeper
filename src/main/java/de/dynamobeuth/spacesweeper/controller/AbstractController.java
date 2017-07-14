package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.ScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Base Controller to be extended from
 */
public abstract class AbstractController extends ScreenController {

    @FXML
    protected Pane root;

    @FXML
    protected void exitGameAction(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();

        stage.getOnCloseRequest().handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
