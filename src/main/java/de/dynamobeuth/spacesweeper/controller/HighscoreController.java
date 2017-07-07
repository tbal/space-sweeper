package de.dynamobeuth.spacesweeper.controller;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import de.dynamobeuth.multiscreen.ScreenController;
import de.dynamobeuth.multiscreen.animation.FadeScreenTransition;
import de.dynamobeuth.multiscreen.animation.SlideScreenTransition;
import de.dynamobeuth.spacesweeper.model.HighscoreEntry;
import de.dynamobeuth.spacesweeper.util.Misc;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import static de.dynamobeuth.multiscreen.animation.SlideScreenTransition.SlideDirection.SLIDE_RIGHT;
import static de.dynamobeuth.spacesweeper.config.Settings.DATABASE_URL;

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

    @FXML
    private Pane root;

    @FXML
    public TableView<HighscoreEntry> tableView;

    @FXML
    public TableColumn rankColumn;

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
    protected void prepare() {
    }

    @Override
    protected void onBeforeFirstShow() {
        System.out.println("onBeforeFirstShow highscore view");
        database = new Firebase(DATABASE_URL);
        tableView.itemsProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                getScreenManager().hideLoadingIndicatorOverlay();
            }
        });
    }

    @Override
    protected void onBeforeShow() {
        System.out.println("onBeforeShow highscore view");

        if (showAddHighscoreEntryDialog) {
            showAddHighscoreEntryDialogAction();
            showAddHighscoreEntryDialog = false;
        }
    }

    @Override
    protected void onFirstShow() {
        System.out.println("onFirstShow highscore view");

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
                //highscoreData = tableView.getItems();

                highscoreData.add(highscoreEntry);
                tableView.setItems(sortedHighscoreData);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                HighscoreEntry highscoreEntry = dataSnapshot.getValue(HighscoreEntry.class);
//                HighscoreEntry newhe = new HighscoreEntry(highscoreEntry.getName(), highscoreEntry.getScore());
//                String old_key = dataSnapshot.getKey();
//                highscoreData.forEach((entry) -> {
//                    if(entry.getKey().equals(old_key)) {
//                        highscoreData.set(highscoreData.indexOf(entry), newhe);
//                        newhe.setKey(old_key);
//                    }
//                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                String old_key = dataSnapshot.getKey();
//                highscoreData.forEach((entry) -> {
//                    if(entry.getKey().equals(old_key)) {
//                        highscoreData.remove(entry);
//                    }
//                });
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("startListeners: unable to attach listener to highscore");
                System.out.println("startListeners: " + firebaseError.getMessage());
            }
        });
    }

    @Override
    protected void onShow() {
        System.out.println("onShow highscore view");
        getScreenManager().showLoadingIndicatorOverlay();
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
            database.child("highscore").push().setValue(new HighscoreEntry(newValue, Misc.randomInRange(1, 20)));

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
