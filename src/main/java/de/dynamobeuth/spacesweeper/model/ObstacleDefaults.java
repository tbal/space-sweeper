package de.dynamobeuth.spacesweeper.model;

public interface ObstacleDefaults {

    default int getScoreImpact() {
        return 0;
    }

    default int getLivesImpact() {
        return 0;
    }

    default double getSpeed() {
        return 1.0;
    }
}
