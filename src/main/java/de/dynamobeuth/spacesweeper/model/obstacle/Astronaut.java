package de.dynamobeuth.spacesweeper.model.obstacle;

import de.dynamobeuth.spacesweeper.model.Obstacle;
import de.dynamobeuth.spacesweeper.util.Helper;
import javafx.animation.RotateTransition;

public class Astronaut extends Obstacle {

    public Astronaut(int lane, int speed) {
        super(lane, speed);

        getStyleClass().add("astronaut");

        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setNode(this);
        rotateTransition.setByAngle(Helper.randomInRange(-180, 180));
        rotateTransition.setDuration(transition.getTotalDuration());

        addAnimation(rotateTransition);
    }

    @Override
    protected double getCollisionRadiusScale() {
        return 0.7;
    }

//    @Override
//    public void handleCollision(Sprite target, Runnable beforeHook, Runnable afterHook) {
//    }

    @Override
    public int getScoreImpact() {
        return +25;
    }

    @Override
    public double getSpeedMultiplicator() {
        return 0.6;
    }
}
