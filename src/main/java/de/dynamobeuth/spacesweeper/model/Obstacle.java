package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.spacesweeper.config.Settings;
import de.dynamobeuth.spacesweeper.util.Misc;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public abstract class Obstacle extends Sprite implements ObstacleDefaults {

    protected int lane;

    private Runnable onStop;

    protected Transition transition;

    private Boolean collisioned = false;

    public Obstacle(int lane) {
        super();

        if (lane > Settings.LANES - 1) {
            throw new IndexOutOfBoundsException("Specified lane exceeds total lanes.");
        }

        this.lane = lane;

        getStyleClass().add("obstacle");

        int size = Misc.randomInRange(Settings.COL_WIDTH / 2, Settings.COL_WIDTH - 10);

        setPrefWidth(size);
        setPrefHeight(size);

        setTranslateX((lane * Settings.COL_WIDTH) + ((Settings.COL_WIDTH - size) / 2));
        setOpacity(0.0);

        TranslateTransition transition = new TranslateTransition(Duration.millis(Settings.SPRITE_SPEED), this);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setFromY(0);
        transition.setToY(Settings.COL_HEIGHT + size);
        transition.setOnFinished(event -> stop());

        this.transition = transition;
    }

    @Override
    public void start() {
        setOpacity(1.0);
        transition.play();
    }

    @Override
    public void pause() {
        transition.pause();
    }

    @Override
    public void resume() {
        start();
    }

    @Override
    public void stop() {
        transition.stop();

        ObstacleManager.removeObstacle(this);
        // TODO: move to OM.removeObstacle
        ((Pane) getParent()).getChildren().remove(this);

        onStop.run();
    }

    abstract public void handleCollision(Sprite target, Runnable onFinished);

    public Boolean intersects(Sprite target) {
        Circle targetBoundingBox = target.getCollisionBounds();
        Circle thisBoundingBox = getCollisionBounds();

        Point2D targetCenter = targetBoundingBox.localToScene(targetBoundingBox.getCenterX(), targetBoundingBox.getCenterY());
        Point2D thisCenter = thisBoundingBox.localToScene(thisBoundingBox.getCenterX(), thisBoundingBox.getCenterY());

        double dx = targetCenter.getX() - thisCenter.getX();
        double dy = targetCenter.getY() - thisCenter.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double minDist = targetBoundingBox.getRadius() + thisBoundingBox.getRadius();

        if (distance < minDist) {

//            targetBoundingBox.setStyle("-fx-fill: blue; -fx-opacity: 0.5");
//            thisBoundingBox.setStyle("-fx-fill: pink; -fx-opacity: 0.5");
//            ((Pane)this.getParent()).getChildren().addAll(thisBoundingBox, targetBoundingBox);
            return true;
        } else {
            return false;
        }
    }

    public Runnable getOnStop() {
        return onStop;
    }

    public void setOnStop(Runnable onStop) {
        this.onStop = onStop;
    }

    public Boolean getCollisioned() {
        return collisioned;
    }

    public void setCollisioned(Boolean collisioned) {
        this.collisioned = collisioned;
    }
}
