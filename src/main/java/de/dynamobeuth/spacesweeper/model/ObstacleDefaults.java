package de.dynamobeuth.spacesweeper.model;

public interface ObstacleDefaults {

    default int getScoreImpact() {
        return 0;
    }

    default int getLivesImpact() {
        return 0;
    }

    default double getSpeedMultiplicator() {
        return 1.0;
    }

    default void handleCollision(Sprite target, Runnable beforeHook, Runnable afterHook) {
        if (beforeHook != null) {
            beforeHook.run();
        }

        if (afterHook != null) {
            afterHook.run();
        }
    }
}
