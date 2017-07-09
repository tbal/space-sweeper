package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.spacesweeper.util.Misc;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.util.Duration;

public class Astronaut extends Obstacle {

    public Astronaut(int lane) {
        super(lane);

        this.lane = lane;

        getStyleClass().add("astronaut");

        RotateTransition rt = new RotateTransition();
        rt.setNode(this);
        rt.setByAngle(Misc.randomInRange(-360, 360));
        rt.setDuration(transition.getTotalDuration());

        ParallelTransition pt = new ParallelTransition(this);
        pt.getChildren().addAll(transition, rt);

        transition = pt;
    }

    @Override
    protected double getCollisionRadiusScale() {
        return 0.7;
    }

    @Override
    public void handleCollision(Sprite target, Runnable onFinished) {
        FadeTransition ft = new FadeTransition();
        ft.setNode(this);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setCycleCount(7);
        ft.setAutoReverse(true);
        ft.setDuration(Duration.millis(100));
        ft.setOnFinished(event -> {
            stop();

            onFinished.run();
        });
        ft.play();
    }

    @Override
    public int getScoreImpact() {
        return +25;
    }

    @Override
    public double getSpeed() {
        return 1.5;
    }
}
