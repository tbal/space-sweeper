package de.dynamobeuth.multiscreen;

public interface CloseHandler {

    default boolean close() {
        return true;
    }
}
