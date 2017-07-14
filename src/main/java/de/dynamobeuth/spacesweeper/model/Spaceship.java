package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

/**
 * Spaceship
 */
public class Spaceship extends Sprite {

    private boolean movementLocked = true;

    private TranslateTransition transition;

    public Spaceship() {
        super();

        getStyleClass().add("spaceship");

        setPrefWidth(Settings.COL_WIDTH);
        setPrefHeight(Settings.COL_WIDTH);

        placeToStartingPosition();

        transition = new TranslateTransition(Duration.millis(Settings.PLAYER_MOVE_SPEED));
        transition.setNode(this);
    }

    @Override
    public void start() {
        movementLocked = false;
    }

    @Override
    public void pause() {
        movementLocked = true;
        transition.pause();
    }

    @Override
    public void resume() {
        movementLocked = false;
        transition.play();
    }

    @Override
    public void stop() {
        transition.stop();
    }

    @Override
    protected double getCollisionRadiusScale() {
        return 0.6;
    }

    /**
     * Resets spaceship to its initial state
     */
    public void reset() {
        transition.stop();

        placeToStartingPosition();

        movementLocked = false;
    }

    /**
     * Moves spaceship to the left
     */
    public void moveLeft() {
        move(-1);
    }

    /**
     * Moves spaceship to the right
     */
    public void moveRight() {
        move(+1);
    }

    /**
     * Moves the spaceship horizontally to left if direction < 0 or to right if direction > 0
     *
     * @param direction If < 0 moves to left, if > 0 moves to right
     */
    protected void move(int direction) {
        if (movementLocked) {
            return;
        }

        double fromX = getTranslateX();

        double toX = fromX + (Settings.COL_WIDTH * direction);
        double minX = 0;
        double maxX = (Settings.LANES - 1) * Settings.COL_WIDTH;

        if (toX < minX) {
            toX = minX;
        } else if (toX > maxX) {
            toX = maxX;
        }

        if (toX == fromX) {
            return;
        }

        transition.setOnFinished(value -> {
            setRotate(0);
            movementLocked = false;
        });

        movementLocked = true;
        transition.setToX(toX);

        if (direction < 0) {
            setRotate(-45);
        } else {
            setRotate(+45);
        }

        transition.play();
    }

    /**
     * Reset starting position of spaceship
     */
    private void placeToStartingPosition() {
        setTranslateX(Math.floor(Settings.LANES / 2) * Settings.COL_WIDTH);
        setTranslateY(Settings.COL_HEIGHT - getPrefHeight());
        setRotate(0);
    }
}
