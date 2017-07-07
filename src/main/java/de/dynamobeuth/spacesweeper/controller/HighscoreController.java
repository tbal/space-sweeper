package de.dynamobeuth.spacesweeper.controller;

import de.dynamobeuth.multiscreen.ScreenController;
import de.dynamobeuth.multiscreen.animation.FadeScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import static de.dynamobeuth.multiscreen.animation.SlideScreenTransition.SlideDirection.SLIDE_RIGHT;

public class HighscoreController extends ScreenController {

    private SimpleStringProperty playerName = new SimpleStringProperty("");

    public Boolean showAddHighscoreEntryDialog = false;

    private StackPane overlay;

    public String getPlayerName() {
        return playerName.get();
    }

    public SimpleStringProperty playerNameProperty() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName.set(playerName);
    }

    @FXML
    private Pane root;

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


    @Override
    protected void onBeforeFirstShow() {
        System.out.println("onBeforeFirstShow highscore view");
    }

    @Override
    protected void onBeforeShow() {
        System.out.println("onBeforeShow highscore view");
    }

    @Override
    protected void onFirstShow() {
        System.out.println("onFirstShow highscore view");
    }

    @Override
    protected void onShow() {
        System.out.println("onShow highscore view");

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

        enterHighscoreDialog.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                shadeScreen();
            } else {
                unshadeScreen();
            }
        });

        // showAndWait() probably fails due to a java bug, see: https://stackoverflow.com/a/22478966
        // therefore we use show() in combination with a changeListener on the resultProperty
        enterHighscoreDialog.resultProperty().addListener((observable, oldValue, newValue) -> {
            setPlayerName(newValue);

            System.out.println("Dein Name ist (direkt): " + newValue);
            System.out.println("Dein Name ist (property): " + getPlayerName());
        });

        enterHighscoreDialog.show();
    }

    private void shadeScreen() {
        overlay = new StackPane();
        overlay.setOpacity(0.3);
        overlay.setStyle("-fx-background-color: black");

        root.setEffect(new GaussianBlur());
        root.getChildren().add(overlay);
    }

    private void unshadeScreen() {
        root.setEffect(null);
        root.getChildren().remove(overlay);
    }

}
