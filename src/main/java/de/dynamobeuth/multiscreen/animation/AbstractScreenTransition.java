package de.dynamobeuth.multiscreen.animation;

import javafx.util.Duration;

public abstract class AbstractScreenTransition<T> implements ScreenTransition {

    protected static final Duration DEFAULT_DURATION = Duration.millis(500);

    private Duration duration;

    public AbstractScreenTransition() {
        setDuration(DEFAULT_DURATION);
    }

    public Duration getDuration() {
        return duration;
    }

    public T setDuration(Duration duration) {
        this.duration = duration;

        return (T) this;
    }
}
