package de.dynamobeuth.spacesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class SpaceSweeperApp extends Application {

    Stage stage;

    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;

        stage.setOnCloseRequest(e -> {
            e.consume();
            exit();
        });

        Parent root = FXMLLoader.load(getClass().getResource("/Templates/StartView.fxml"));

        stage.setTitle("Space Sweeper - Der letzte räumt den Weltraum auf");
        stage.setScene(new Scene(root));
//        stage.setScene(new Scene(root, Settings.COL_COUNT * Settings.COL_WIDTH, Settings.COL_HEIGHT));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void exit() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Spiel beenden");
        confirmDialog.setHeaderText("Bist du sicher, dass du das Spiel beenden möchtest?");
        //confirmDialog.setContentText("");

        if (confirmDialog.showAndWait().get() == ButtonType.OK) {
            stage.close();
        }
    }
}
