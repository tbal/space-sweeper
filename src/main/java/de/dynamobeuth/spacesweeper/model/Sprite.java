package de.dynamobeuth.spacesweeper.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public abstract class Sprite extends Region {

    private SimpleBooleanProperty paused = new SimpleBooleanProperty(false);

    Sprite() {
        getStyleClass().add("sprite");
    }

    public void start() {
        if (paused.get()) {
            // do sth
        } else {
            // do sth
        }
    }

    public void pause() {
        paused.set(true);
    }

    public void resume() {
        if (paused.get()) {
            // do sth
        } else {
            // do sth
        }
    }

    public void stop() {

    }

    public Shape getCollisionBounds() {
        return new Circle(Math.min(getWidth(), getHeight()));
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
