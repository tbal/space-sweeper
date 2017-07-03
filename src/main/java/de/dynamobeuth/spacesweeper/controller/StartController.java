package de.dynamobeuth.spacesweeper.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartController {

    @FXML
    private Button btnStartGame;

    @FXML
    void startGameAction(ActionEvent event) {
        System.out.println("hallo!");
    }

}
