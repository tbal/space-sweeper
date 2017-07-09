package de.dynamobeuth.spacesweeper.model.obstacle;

import de.dynamobeuth.spacesweeper.model.Obstacle;
import de.dynamobeuth.spacesweeper.model.Sprite;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class Planet extends Obstacle {

    public Planet(int lane) {
        super(lane);

        this.lane = lane;

        getStyleClass().add("planet");
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
    public int getLivesImpact() {
        return -1;
    }

    @Override
    public double getSpeed() {
        return 0.7;
    }
}
