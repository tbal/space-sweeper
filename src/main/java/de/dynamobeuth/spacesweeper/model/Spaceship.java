package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class Spaceship extends Sprite {

    private boolean movementLocked;

    private TranslateTransition transition;

    public Spaceship() {
        super();

        getStyleClass().add("spaceship");
        System.out.println(getStyleClass());

        // TODO: test if setting this via css allows retrieving width/height in java code
        setPrefWidth(Settings.COL_WIDTH);
        setPrefHeight(Settings.COL_WIDTH);

        setTranslateX(Math.floor(Settings.COL_COUNT / 2) * Settings.COL_WIDTH);
        setTranslateY(Settings.COL_HEIGHT - getPrefHeight());

        transition = new TranslateTransition(Duration.millis(Settings.PLAYER_MOVE_SPEED));
        transition.setNode(this);
    }

    public void moveLeft() {
        move(-1);
    }

    public void moveRight() {
        move(+1);
    }

    protected void move(int direction) {
        if (movementLocked) {
            return;
        }

        double fromX = getTranslateX();

        double toX = fromX + (Settings.COL_WIDTH * direction);
        double minX = 0;
        double maxX = (Settings.COL_COUNT - 1) * Settings.COL_WIDTH;

        if (toX < minX) {
            toX = minX;
        } else if (toX > maxX) {
            toX = maxX;
        }

        if (toX == fromX) {
            return;
        }

//        animation.setInterpolator(Interpolator.LINEAR);
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
}
