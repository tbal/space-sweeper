package de.dynamobeuth.multiscreen.animation;

import de.dynamobeuth.multiscreen.ScreenManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

public interface ScreenTransition {

    public void animate(ScreenManager screenManager, Node currentScreen, Node nextScreen, EventHandler<ActionEvent> onFinished);

}
