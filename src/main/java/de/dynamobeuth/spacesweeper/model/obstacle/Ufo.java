package de.dynamobeuth.spacesweeper.model.obstacle;

import de.dynamobeuth.spacesweeper.model.Obstacle;
import de.dynamobeuth.spacesweeper.model.Sprite;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class Ufo extends Obstacle {

    public Ufo(int lane) {
        super(lane);

        this.lane = lane;

        getStyleClass().add("ufo");
    }

    @Override
    public void handleCollision(Sprite target, Runnable beforeHook, Runnable afterHook) {
        beforeHook.run();

        FadeTransition ft = new FadeTransition();
        ft.setNode(this);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setCycleCount(7);
        ft.setAutoReverse(true);
        ft.setDuration(Duration.millis(100));
        ft.setOnFinished(event -> {
            stop();

            afterHook.run();
        });
        ft.play();
    }

    @Override
    public int getScoreImpact() {
        return -20;
    }

    @Override
    public double getSpeed() {
        return 1;
    }
}
