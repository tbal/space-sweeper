package de.dynamobeuth.spacesweeper.model.obstacle;

import de.dynamobeuth.spacesweeper.model.Obstacle;
import de.dynamobeuth.spacesweeper.util.Misc;
import javafx.animation.RotateTransition;

public class Satellite extends Obstacle {

    public Satellite(int lane) {
        super(lane);

        this.lane = lane;

        getStyleClass().add("satellite");

        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setNode(this);
        rotateTransition.setByAngle(Misc.randomInRange(-180, 180));
        rotateTransition.setDuration(transition.getTotalDuration());

        addAnimation(rotateTransition);
    }

    @Override
    protected double getCollisionRadiusScale() {
        return 0.7;
    }

//    @Override
//    public void handleCollision(Sprite target, Runnable onFinished) {
//        FadeTransition ft = new FadeTransition();
//        ft.setNode(this);
//        ft.setFromValue(1);
//        ft.setToValue(0);
//        ft.setCycleCount(7);
//        ft.setAutoReverse(true);
//        ft.setDuration(Duration.millis(100));
//        ft.setOnFinished(event -> {
//            stop();
//
//            onFinished.run();
//        });
//        ft.play();
//    }

    @Override
    public int getScoreImpact() {
        return +5;
    }

    @Override
    public double getSpeed() {
        return 0.7;
    }
}
