package de.dynamobeuth.spacesweeper.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Random;

public class Misc {

    private final static Random RANDOM = new Random();

    public static Timeline setTimeout(Runnable runnable, int delay){
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.millis(delay),
            actionEvent -> runnable.run()
        ));

        timeline.play();

        return timeline;
    }

    public static Color randomColor() {
        return new Color(RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble(), 1);
    }

    public static int randomInRange(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }
}
