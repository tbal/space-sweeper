package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("SpaceSweeper.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Space Sweeper - Der Letzte räumt den Weltraum auf!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
