package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Space extends Pane {

    public Space(Pane root) {
        super();

        getStyleClass().add("space");

        setPrefWidth(Settings.LANES * Settings.COL_WIDTH);
        setPrefHeight(Settings.COL_HEIGHT);

        setClip(new Rectangle(getPrefWidth(), getPrefHeight()));

        root.getChildren().add(this);

        addLaneSeparationLines();
    }

    private void addLaneSeparationLines() {
        for (int i = 1; i < Settings.LANES; i++) {

            int x = Settings.COL_WIDTH * i;

            Line spacer = new Line(x, 0, x, Settings.COL_HEIGHT);
            spacer.getStyleClass().add("line");
            getChildren().add(spacer);
        }
    }

}
