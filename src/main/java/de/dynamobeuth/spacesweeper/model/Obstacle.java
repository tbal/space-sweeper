package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.spacesweeper.config.Settings;
import de.dynamobeuth.spacesweeper.util.Misc;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Obstacle extends Parent {
    private int column;
    private TranslateTransition transition;
    private Node node;
    private int radius;

    public Obstacle() {
        column = Misc.randomInRange(0, Settings.COL_COUNT - 1);
        create();
    }

    public Obstacle(int column) throws IndexOutOfBoundsException {
        if (column > Settings.COL_COUNT - 1) {
            throw new IndexOutOfBoundsException("Specified column exceeds column count.");
        }

        this.column = column;

        create();
    }

    private void create() {
        radius = Misc.randomInRange(Settings.RADIUS / 2, Settings.RADIUS);
        node = new Circle(radius, Misc.randomColor());
        node.setTranslateX((column + 1) * Settings.COL_WIDTH - (Settings.COL_WIDTH / 2));
        node.setOpacity(0.0);

        transition = new TranslateTransition(Duration.millis(Settings.SPRITE_SPEED), node);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setFromY(0);
        transition.setToY(Settings.COL_HEIGHT + radius);

        getChildren().add(node);
        node.toBack();
    }

    public double getRadius() {
        return radius;
    }

    public Animation getAnimation() {
        return transition;
    }

    public void start() {
        node.setOpacity(1.0);
        transition.play();
    }

    public void stop() {
        transition.stop();
        transition.getOnFinished().handle(new ActionEvent(this, null));
    }

    public void pause() {
        transition.pause();
    }

    public void resume() {
        start();
    }
}
