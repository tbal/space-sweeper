package de.dynamobeuth.spacesweeper.model;

import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

/**
 * Base class for all sprites
 */
public abstract class Sprite extends Region {

    Sprite() {
        getStyleClass().add("sprite");
    }

    abstract public void start();

    abstract public void pause();

    abstract public void resume();

    abstract public void stop();

    protected double getCollisionRadiusScale() {
        return 1.0;
    }

    public Circle getCollisionBounds() {
        return new Circle(
                getTranslateX() + (getPrefWidth() / 2),
                getTranslateY() + (getPrefHeight() / 2),
                Math.min(getPrefWidth(), getPrefHeight()) / 2 * getCollisionRadiusScale()
        );
    }
}
