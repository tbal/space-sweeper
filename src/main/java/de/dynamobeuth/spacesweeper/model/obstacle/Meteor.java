package de.dynamobeuth.spacesweeper.model.obstacle;

import de.dynamobeuth.spacesweeper.model.Obstacle;
import de.dynamobeuth.spacesweeper.model.Sprite;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class Meteor extends Obstacle {

    public Meteor(int lane, int speed) {
        super(lane, speed);

        getStyleClass().add("meteor");
    }

    @Override
    public void handleCollision(Sprite target, Runnable beforeHook, Runnable afterHook) {
        if (beforeHook != null) {
            beforeHook.run();
        }

        Sound.play(Sound.Sounds.HIT);

        int amplitudeX = 5;
        int amplitudeY = ((Math.random() < 0.5) ? -1 : 1) * amplitudeX;

        double backupX = target.getTranslateX();
        double backupY = target.getTranslateY();

        target.setTranslateX(target.getTranslateX() + amplitudeX);
        target.setTranslateY(target.getTranslateY() + amplitudeY);

        TranslateTransition tt = new TranslateTransition();
        tt.setNode(target);
        tt.setFromX(target.getTranslateX() - (amplitudeX / 2));
        tt.setFromY(target.getTranslateY() - (amplitudeY / 2));
        tt.setByX(-amplitudeX);
        tt.setByY(-amplitudeY);
        tt.setCycleCount(7);
        tt.setAutoReverse(true);
        tt.setDuration(Duration.millis(40));
        tt.setOnFinished(event -> {
            target.setTranslateX(backupX);
            target.setTranslateY(backupY);

            if (afterHook != null) {
                afterHook.run();
            }
        });
        tt.play();
    }

    @Override
    public int getScoreImpact() {
        return -5;
    }

    @Override
    public double getSpeedMultiplicator() {
        return 1.2;
    }
}
