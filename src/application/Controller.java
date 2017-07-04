package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;

public class Controller {

	@FXML
	// mainscreen
	// always visible
	private AnchorPane screen;
	// mainscreen checkbox sound
	// always visible
	private CheckBox checkboxSound;

	// mainscreen
	private AnchorPane mainscreen;

	// screen highscore
	private AnchorPane highscorescreen;

	// gamescreen
	private AnchorPane gamescreen;
	// gamescreen paneLifes heart 1
	private ImageView ImageHeart1;
	// gamescreen paneLifes heart 2
	private ImageView ImageHeart2;
	// gamescreen paneLifes heart 3
	private ImageView ImageHeart3;
	// gamescreen panegame - Einbindung Game
	private AnchorPane PaneGame;
	// gamescreen panecontrols
	private AnchorPane PaneControls;
	// gamescreen panepoints - show points of actual game
	private TextFlow PanePoints;

	// PaneGameOver
	private AnchorPane Gameoverscreen;
	// PaneGameOver Textfield name for Firebase Highscore
	private TextField TextFieldHighscore;
	// Pane GameOver Button for sending name to Firebase Highscore
	private Button ButtonEintragen;

	@FXML
	void disableSound(ActionEvent event) {

	}

	void sendName(ActionEvent event) {

	}

	@FXML
	void playGame(ActionEvent event) {

		fadeInGamescreen();
	}

	@FXML
	void showHighscore(ActionEvent event) {

		fadeInHighscore();

	}

	void fadeInMainscreen() {
		mainscreen.setVisible(true);
		gamescreen.setVisible(false);
		highscorescreen.setVisible(false);
		Gameoverscreen.setVisible(false);
	}

	void fadeInGamescreen() {
		mainscreen.setVisible(false);
		gamescreen.setVisible(true);
		highscorescreen.setVisible(false);
		Gameoverscreen.setVisible(false);

	}

	void fadeInHighscore() {
		mainscreen.setVisible(false);
		gamescreen.setVisible(false);
		highscorescreen.setVisible(true);
		Gameoverscreen.setVisible(false);

	}

	void fadeInGameover() {
		mainscreen.setVisible(false);
		gamescreen.setVisible(false);
		highscorescreen.setVisible(false);
		Gameoverscreen.setVisible(true);

	}

}
