package de.dynamobeuth.spacesweeper.model.obstacle;

import de.dynamobeuth.spacesweeper.model.Obstacle;
import de.dynamobeuth.spacesweeper.util.Helper;
import javafx.animation.RotateTransition;

public class Satellite extends Obstacle {

    public Satellite(int lane, int speed) {
        super(lane, speed);

        getStyleClass().add("satellite");

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
        return +5;
    }

    @Override
    public double getSpeedMultiplicator() {
        return 0.7;
    }
}
