package de.dynamobeuth.spacesweeper.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

public abstract class Sprite extends Region {

    private SimpleBooleanProperty paused = new SimpleBooleanProperty(false);

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

    public boolean isPaused() {
        return paused.get();
    }

    public SimpleBooleanProperty pausedProperty() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused.set(paused);
    }
}
