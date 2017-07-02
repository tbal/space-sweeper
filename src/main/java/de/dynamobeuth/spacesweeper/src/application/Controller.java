package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;

public class Controller {

	@FXML
	// mainscreen
	// always visible
	private AnchorPane screen;
	// mainscreen images
	private AnchorPane PaneImages;
	// mainscreen panetitle
	private Pane PaneTitle;
	// mainscreen buttonbar
	private ButtonBar buttonbar;
	// mainscreen checkbox sound
	// always visible
	private CheckBox checkboxSound;

	// gamescreen panelegend
	private AnchorPane PaneLegend;
	// gamescreen paneLifes
	private AnchorPane PaneLifes;
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

	// highscorescreen Panehighscore - Highscore einbinden
	private AnchorPane PaneHighscore;
	// highscorescreen paneimagetrophy
	private AnchorPane PaneImageTrophy;

	@FXML
	void disableSound(ActionEvent event) {

	}

	@FXML
	void playGame(ActionEvent event) {
		fadeOutMainscreen();

		fadeInGamescreen();

		fadeOutHighscore();
	}

	@FXML
	void showHighscore(ActionEvent event) {

		fadeOutMainscreen();

		fadeOutGamescreen();

		fadeInHighscore();

	}

	void fadeInMainscreen() {
		PaneImages.setVisible(true);
		PaneTitle.setVisible(true);
		buttonbar.setVisible(true);
	}

	void fadeOutMainscreen() {
		PaneImages.setVisible(false);
		PaneTitle.setVisible(false);
		buttonbar.setVisible(false);
	}

	void fadeInGamescreen() {
		PaneLegend.setVisible(true);
		PaneLifes.setVisible(true);
		PaneGame.setVisible(true);
		PanePoints.setVisible(true);
		PaneControls.setVisible(true);
	}

	void fadeOutGamescreen() {
		PaneLegend.setVisible(false);
		PaneLifes.setVisible(false);
		PaneGame.setVisible(false);
		PanePoints.setVisible(false);
		PaneControls.setVisible(false);
	}

	void fadeOutHighscore() {
		PaneHighscore.setVisible(false);
		PaneImageTrophy.setVisible(false);
	}

	void fadeInHighscore() {
		PaneHighscore.setVisible(true);
		PaneImageTrophy.setVisible(true);
	}

}
