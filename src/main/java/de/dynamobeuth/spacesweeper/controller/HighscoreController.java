package de.dynamobeuth.spacesweeper.controller;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import de.dynamobeuth.multiscreen.animation.RotateScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import de.dynamobeuth.spacesweeper.control.TextInputDialog;
import de.dynamobeuth.spacesweeper.model.HighscoreEntry;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import javax.naming.InvalidNameException;

import static de.dynamobeuth.multiscreen.animation.RotateScreenTransition.RotationMode.ROTATE_IN;
import static de.dynamobeuth.multiscreen.animation.SlideScreenTransition.SlideDirection.SLIDE_RIGHT;
import static de.dynamobeuth.spacesweeper.config.Settings.DATABASE_URL;
import static de.dynamobeuth.spacesweeper.util.Sound.Sounds.BACKGROUND_HIGHSCORE;
import static de.dynamobeuth.spacesweeper.util.Sound.Sounds.HIGHSCORE_ENTRY_ADDED;

/**
 * Highscore Screen Controller
 */
public class HighscoreController extends AbstractController {

    @FXML
    private TableView<HighscoreEntry> tableView;

    @FXML
    private TableColumn rankColumn;

    public Boolean showAddHighscoreEntryDialog = false;

    private String playerName = "";

    private static Firebase database;

    private ObservableList<HighscoreEntry> highscoreData = FXCollections.observableArrayList();

    private SortedList<HighscoreEntry> sortedHighscoreData = highscoreData.sorted((o1, o2) -> {
        if (o1.getScore() > o2.getScore()) {
            return -1;
        } else if (o1.getScore() < o2.getScore()) {
            return 1;
        } else {
            return 0;
        }
    });

    private boolean highscoreEntriesLoaded = false;

    @Override
    protected void onBeforeFirstShow() {
        database = new Firebase(DATABASE_URL);

        tableView.itemsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.size() > 0) {
                highscoreEntriesLoaded = true;
                Platform.runLater(() -> getScreenManager().hideLoadingIndicatorOverlay());
            };
        });
    }

    @Override
    protected void onBeforeShow() {
        Sound.playBackground(BACKGROUND_HIGHSCORE);

        if (showAddHighscoreEntryDialog) {
            showAddHighscoreEntryDialogAction();
            showAddHighscoreEntryDialog = false;
        }
    }

    @Override
    protected void onFirstShow() {
        rankColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<HighscoreEntry, String>, ObservableValue<String>>() {
            @Override public ObservableValue<String> call(TableColumn.CellDataFeatures<HighscoreEntry, String> p) {
                return new ReadOnlyObjectWrapper(tableView.getItems().indexOf(p.getValue()) + 1 + "");
            }
        });

        database.child("highscore").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildName) {
                HighscoreEntry highscoreEntry = dataSnapshot.getValue(HighscoreEntry.class);
                highscoreEntry.setKey(dataSnapshot.getKey());

                highscoreData.add(highscoreEntry);
                tableView.setItems(sortedHighscoreData);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    @Override
    protected void onShow() {
        if (!highscoreEntriesLoaded) {
            getScreenManager().showLoadingIndicatorOverlay();
        }
    }

    /**
     * Opens dialog to enter player name and adds highscore entry on confirm
     */
    private void showAddHighscoreEntryDialogAction() {
        int tmpScore = 0;

        try {
            GameController gameController = (GameController) getScreenManager().getControllerByName("game");

            tmpScore = gameController.scoreProperty().get();
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }

        int score = tmpScore;

        TextInputDialog enterHighscoreDialog = new TextInputDialog(playerName, getScreenManager());

        enterHighscoreDialog.setHeaderText(score + " Punkte in Highscore eintragen");
        enterHighscoreDialog.setContentText("Name:");

        // showAndWait() probably fails due to a java bug, see: https://stackoverflow.com/a/22478966
        // therefore we use show() in combination with a changeListener on the resultProperty
        enterHighscoreDialog.resultProperty().addListener((observable, oldValue, newValue) -> {
            playerName = newValue;

            database.child("highscore").push().setValue(new HighscoreEntry(playerName, score));

            Sound.play(HIGHSCORE_ENTRY_ADDED);
        });

        enterHighscoreDialog.show();
    }

    /**
     * Action handler for back home button
     *
     * @param event ActionEvent
     */
    @FXML
    private void showStartScreenAction(ActionEvent event) {
        getScreenManager().showScreen("start", (new SlideScreenTransition()).setSlideDirection(SLIDE_RIGHT));
    }

    /**
     * Action handler for start game button
     *
     * @param event ActionEvent
     */
    @FXML
    private void showGameScreenAction(ActionEvent event) {
        getScreenManager().showScreen(
                "game",
                getScreenManager().previousScreenNameMatches("game")
                        ? (new SlideScreenTransition()).setSlideDirection(SLIDE_RIGHT)
                        : (new RotateScreenTransition()).setRotationMode(ROTATE_IN));
    }
}
