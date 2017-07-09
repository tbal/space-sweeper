package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.spacesweeper.config.Settings;
import de.dynamobeuth.spacesweeper.util.Misc;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public abstract class Obstacle extends Sprite implements ObstacleDefaults {

    protected int lane;

    protected ParallelTransition transition;

    private int size;

    private Runnable onStop;

    private Boolean collisioned = false;

    public Obstacle(int lane) {
        super();

        if (lane > Settings.LANES - 1) {
            throw new IndexOutOfBoundsException("Specified lane exceeds total lanes.");
        }

        this.lane = lane;

        getStyleClass().add("obstacle");

        size = Misc.randomInRange(Settings.COL_WIDTH / 2, Settings.COL_WIDTH - 10);

        setPrefWidth(size);
        setPrefHeight(size);

        setTranslateX((lane * Settings.COL_WIDTH) + ((Settings.COL_WIDTH - size) / 2));
        setTranslateY(0 - size);

        transition = new ParallelTransition(this);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(Settings.SPRITE_SPEED / getSpeed()), this);
        translateTransition.setInterpolator(Interpolator.LINEAR);
        translateTransition.setFromY(0 - size);
        translateTransition.setToY(Settings.COL_HEIGHT + size);
        translateTransition.setOnFinished(event -> stop());

        transition.getChildren().addAll(translateTransition);
    }

    public void addAnimation(Animation animation) {
        transition.getChildren().add(animation);
    }

    @Override
    public void start() {
        transition.play();
    }

    @Override
    public void pause() {
        transition.pause();
    }

    @Override
    public void resume() {
        transition.play();
    }

    @Override
    public void stop() {
        transition.stop();

        onStop.run();
    }

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
//            /* DEBUG START */
//            targetBoundingBox.setStyle("-fx-fill: blue; -fx-opacity: 0.7");
//            thisBoundingBox.setStyle("-fx-fill: red; -fx-opacity: 0.7");
//            Space space = ((Space)this.getParent());
//            space.getChildren().addAll(thisBoundingBox, targetBoundingBox);
//            Misc.setTimeout(() -> space.getChildren().removeAll(thisBoundingBox, targetBoundingBox), 1000);
//            /* DEBUG END */

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
