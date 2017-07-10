package de.dynamobeuth.spacesweeper.model.obstacle;

import de.dynamobeuth.spacesweeper.model.Obstacle;
import de.dynamobeuth.spacesweeper.model.Sprite;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class Planet extends Obstacle {

    public Planet(int lane, int speed) {
        super(lane, speed);

        getStyleClass().add("planet");
    }

    @Override
    public void handleCollision(Sprite target, Runnable beforeHook, Runnable afterHook) {
        if (beforeHook != null) {
            beforeHook.run();
        }

        Sound.play(Sound.Sounds.EXPLODE);

        target.getStyleClass().add("hit");

        FadeTransition ft = new FadeTransition();
        ft.setNode(target);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setCycleCount(6);
        ft.setAutoReverse(true);
        ft.setDuration(Duration.millis(100));
        ft.setOnFinished(event -> {
            target.getStyleClass().remove("hit");

            if (afterHook != null) {
                afterHook.run();
            }
        });
        ft.play();
    }

    @Override
    public int getLivesImpact() {
        return -1;
    }

    @Override
    public double getSpeedMultiplicator() {
        return 0.8;
    }
}
