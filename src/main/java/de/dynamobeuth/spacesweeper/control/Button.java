package de.dynamobeuth.spacesweeper.control;

import de.dynamobeuth.spacesweeper.util.Sound;

import static de.dynamobeuth.spacesweeper.util.Sound.Sounds.BUTTON_HOVER;

/**
 * Extended Button which plays sound on mouse hover
 */
public class Button extends javafx.scene.control.Button {

    public Button() {
        super();

        setOnMouseEntered(event -> Sound.play(BUTTON_HOVER));
    }
}
