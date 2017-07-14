package de.dynamobeuth.spacesweeper.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Random;

/**
 * Utility class with several static helper methods
 */
final public class Helper {

    private final static Random RANDOM = new Random();

    /**
     * Disallow instances of this class as it is a utility class with only static methods
     */
    private Helper() {}

    /**
     * Allows running code after a specified timeout.
     *
     * @param runnable Callback to execute after delay is over
     * @param delay Delay/Timeout in milliseconds to wait before calling the callback
     * @return Instance of the timeline used for the timeout.
     */
    public static Timeline setTimeout(Runnable runnable, int delay){
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.millis(delay),
            actionEvent -> runnable.run()
        ));

        timeline.play();

        return timeline;
    }

    /**
     * Returns a random int between the given min and max values.
     * min/max can also be negative values.
     *
     * @param min Value from
     * @param max Value to
     * @return Random int between min and max
     */
    public static int randomInRange(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }
}
