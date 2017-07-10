package de.dynamobeuth.spacesweeper.model.obstacle;

import de.dynamobeuth.spacesweeper.model.Obstacle;
import de.dynamobeuth.spacesweeper.model.Sprite;
import de.dynamobeuth.spacesweeper.util.Helper;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

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
        return 1.0;
    }

    @Override
    public void handleCollision(Sprite target, Runnable beforeHook, Runnable afterHook) {
        if (beforeHook != null) {
            beforeHook.run();
        }

        Sound.play(Sound.Sounds.CONSUME);

        ScaleTransition st = new ScaleTransition();
        st.setNode(this);
        st.setToX(0.0);
        st.setToY(0.0);
        st.setDuration(Duration.millis(200));
        st.setOnFinished(event -> stop());
        st.play();

        if (afterHook != null) {
            afterHook.run();
        }
    }

    @Override
    public int getScoreImpact() {
        return +5;
    }

    @Override
    public double getSpeedMultiplicator() {
        return 1.0;
    }
}
