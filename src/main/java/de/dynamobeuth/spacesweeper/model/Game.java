package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.multiscreen.ScreenManager;
import de.dynamobeuth.spacesweeper.component.RemainingLifeComponent;
import de.dynamobeuth.spacesweeper.config.Settings;
import de.dynamobeuth.spacesweeper.util.Misc;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public class Game {
    private final ScreenManager screenManager;
    private final Pane root;
    private final RemainingLifeComponent r;

    private boolean gamePaused = false;

    private AnimationTimer collisionDetectionLoop;

    private Spaceship spaceship;

    private List<Obstacle> currentObstacleInLane = Arrays.asList(new Obstacle[3]);

    public Game(Pane root, ScreenManager screenManager, RemainingLifeComponent r) {
        this.root = root;
        this.screenManager = screenManager;
        this.r = r;

        addLaneSeparationLines();
        addSpaceship();
        setKeyBindings();
        startCollisionDetectionLoop();
        startObstacleSpawning();
    }

    private void setKeyBindings() {
        screenManager.getScene().setOnKeyPressed((event -> {
            switch (event.getCode()) {
                case LEFT:
                    spaceship.move(-1);
                    break;

                case RIGHT:
                    spaceship.move(+1);
                    break;

                case ESCAPE:
                    if (gamePaused) {
                        resumeGame();
                    } else {
                        pauseGame();
                    }
                    break;
            }
        }));
    }

    private void addLaneSeparationLines() {
        for (int i = 1; i < Settings.COL_COUNT; i++) {
            int x = Settings.COL_WIDTH * i;
            Line spacer = new Line(x, 0, x, Settings.COL_HEIGHT);
            spacer.getStyleClass().add("line");
            root.getChildren().add(spacer);
        }
    }

    private void addSpaceship() {
        spaceship = new Spaceship();
        root.getChildren().add(spaceship);
    }

    public void startObstacleSpawning() {
        for (int i = 0; i < Settings.COL_COUNT; i++) {
            spawnObstacle(i);
        }
    }

    public void pauseGame() {
        ObstacleManager.pauseAll();
        collisionDetectionLoop.stop();
        spaceship.pause();
        gamePaused = true;
    }

    public void resumeGame() {
        collisionDetectionLoop.start();
        ObstacleManager.resumeAll();
        spaceship.resume();
        gamePaused = false;
    }

    public void startCollisionDetectionLoop() {
        collisionDetectionLoop = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {

                ObstacleManager.getAll().forEach(obstacle -> {
//                    if (checkIntersection(obstacle)) {
                    if (!obstacle.collisioned && obstacle.intersects(spaceship.getBoundsInParent())) {
                        obstacle.collisioned = true;

                        pauseGame();
                        r.decreaseRemaingLifes();

                        FadeTransition collisionTransition = new FadeTransition(Duration.millis(100), obstacle);
                        collisionTransition.setFromValue(1.0);
                        collisionTransition.setToValue(0.0);
                        collisionTransition.setCycleCount(7);
                        collisionTransition.setAutoReverse(true);
                        collisionTransition.setOnFinished(value -> {
                            obstacle.stop();
                            ObstacleManager.removeObstacle(obstacle);
                            root.getChildren().remove(obstacle);
                            resumeGame();
                        });

                        collisionTransition.play();
                    }
                });
            }
        };

        collisionDetectionLoop.start();
    }

//    public boolean checkIntersection(Obstacle obstacle) {
//        double dx = obstacle.getTranslateX() - spaceship.getTranslateX();
//        double dy = obstacle.getTranslateY() - spaceship.getTranslateY();
//
//        System.out.println(obstacle.getCenterX());
//        System.out.println(spaceship.getCenterX());
//
//        double d = Math.sqrt(((dy * dy) + (dx * dx)));
//        //System.out.println(d);
//
//        if (d > (Settings.RADIUS + obstacle.getRadius())) {
//            return false;
//        } else if (d < Math.abs(Settings.RADIUS + obstacle.getRadius())) {
//            return false;
//        }
//
//        return true;
//    }

    public void spawnObstacle(int col) {
        Misc.setTimeout(() -> {
            if (!allowObstacleSpawning(col)) {
                Misc.setTimeout(() -> spawnObstacle(col), Settings.SPRITE_SPEED / 4);
                return;
            }

            Obstacle obstacle = ObstacleManager.createObstacle(col);

            obstacle.getAnimation().setOnFinished(value -> {
                    currentObstacleInLane.set(col, null);

                    ObstacleManager.removeObstacle(obstacle);
                    root.getChildren().remove(obstacle);

                    spawnObstacle(col);
            });

            root.getChildren().add(obstacle);
            currentObstacleInLane.set(col, obstacle);
            obstacle.toBack();

            if (!gamePaused) {
                obstacle.start();
            }
        }, Misc.randomInRange(1, Settings.SPRITE_SPEED));
    }

    private boolean allowObstacleSpawning(int col) {
        int lanesWithObstacles = 0;

        for (int i = 0; i < Settings.COL_COUNT; i++) {
            if (i != col && currentObstacleInLane.get(i) != null) {
                lanesWithObstacles++;
            }
        }

        return lanesWithObstacles < (Settings.COL_COUNT - 1);
    }
}
