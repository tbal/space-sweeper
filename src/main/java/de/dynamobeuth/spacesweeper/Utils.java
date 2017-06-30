package de.dynamobeuth.spacesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Random;

public class Utils {

    private static Random rand = new Random();

    public static void setTimeout(Runnable runnable, int delay){
        new Timeline(new KeyFrame(
            Duration.millis(delay),
            actionEvent -> runnable.run()
        )).play();
    }

    public static Color randomColor() {
        return new Color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), 1);
    }

    public static int randomInRange(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }
}
