package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.ScreenController;
import de.dynamobeuth.multiscreen.animation.FadeScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

import static de.dynamobeuth.multiscreen.animation.SlideScreenTransition.SlideDirection.SLIDE_RIGHT;

public class HighscoreController extends ScreenController {

    private StringProperty playerName = new SimpleStringProperty("");

    public Boolean showAddHighscoreEntryDialog = false;

    public String getPlayerName() {
        return playerName.get();
    }

    public StringProperty playerNameProperty() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName.set(playerName);
    }

    @FXML
    private Node root;

    @FXML
    private Button btnStartGame;

    @FXML
    private Button btnStartScreen;

    @FXML
    private void showGameScreenAction(ActionEvent event) {
        getScreenManager().showScreen("game", (new SlideScreenTransition()).setSlideDirection(SLIDE_RIGHT));
    }

    @FXML
    private void showStartScreenAction(ActionEvent event) {
        getScreenManager().showScreen(
                "start",
                getScreenManager().previousScreenNameMatches("start")
                        ? (new SlideScreenTransition()).setSlideDirection(SLIDE_RIGHT)
                        : new FadeScreenTransition());
    }

    public void show() {
        if (showAddHighscoreEntryDialog) {
            showAddHighscoreEntryDialogAction();
            showAddHighscoreEntryDialog = false;
        }
    }

    public void showAddHighscoreEntryDialogAction() {
        TextInputDialog enterHighscoreDialog = new TextInputDialog(getPlayerName());
        enterHighscoreDialog.setTitle("In Highscore eintragen");
        enterHighscoreDialog.setHeaderText("Bitte geben deinen Namen an,\num dein Ergebnis einzutragen.");
        enterHighscoreDialog.setContentText("Dein Name:");

        // showAndWait() probably fails due to a java bug, see: https://stackoverflow.com/a/22478966
        // therefore we use show() in combination with a changeListener on the resultProperty
        enterHighscoreDialog.resultProperty().addListener((observable, oldValue, newValue) -> {
            setPlayerName(newValue);
            System.out.println("Dein Name ist (direkt): " + newValue);
            System.out.println("Dein Name ist (property): " + getPlayerName());
        });
        enterHighscoreDialog.show();
    }

}
