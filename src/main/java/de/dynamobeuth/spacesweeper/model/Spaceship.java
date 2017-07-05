package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Spaceship extends Parent {

    private Node spaceshipNode;
    private boolean movementLocked;
    private TranslateTransition transition = new TranslateTransition(Duration.millis(Settings.PLAYER_MOVE_SPEED));

    public Spaceship() {
        create();
    }

    public void create() {
        spaceshipNode = new Circle(Settings.RADIUS, Color.BLUE);
        spaceshipNode.setTranslateX(Math.floor(Settings.COL_COUNT / 2) * Settings.COL_WIDTH + Settings.RADIUS);
        spaceshipNode.setTranslateY(Settings.COL_HEIGHT - Settings.RADIUS);

        getChildren().add(spaceshipNode);

        transition.setNode(spaceshipNode);
    }

    public void move(int direction) {
        if (movementLocked) {
            return;
        }

        double fromX = spaceshipNode.getTranslateX();

        double toX = fromX + (Settings.COL_WIDTH * direction);
        double minX = Settings.RADIUS;
        double maxX = Settings.COL_COUNT * Settings.COL_WIDTH - Settings.RADIUS;

        if (toX < minX) {
            toX = minX;
        } else if (toX > maxX) {
            toX = maxX;
        }

        if (toX == fromX) {
            return;
        }

//        animation.setInterpolator(Interpolator.LINEAR);
        transition.setOnFinished(value -> movementLocked = false);

        movementLocked = true;
        transition.setToX(toX);
        transition.play();
    }

    public void pause() {
        movementLocked = true;
        transition.pause();
    }

    public void resume() {
        movementLocked = false;
        transition.play();
    }

    public void hit() {

    }

    public void collect() {

    }
}
