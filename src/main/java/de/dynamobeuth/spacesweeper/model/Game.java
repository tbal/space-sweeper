package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.multiscreen.ScreenManager;
import de.dynamobeuth.spacesweeper.component.RemainingLifeComponent;
import de.dynamobeuth.spacesweeper.config.Settings;
import de.dynamobeuth.spacesweeper.util.Misc;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.Arrays;
import java.util.List;

import static de.dynamobeuth.spacesweeper.util.Sound.Sounds.IN_GAME;

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

        Sound.playBackground(IN_GAME);

        addLaneSeparationLines();
        initSpaceship();
        setKeyBindings();
        startCollisionDetectionLoop();
        startObstacleSpawning();
        increaseLevel();
    }

    private void increaseLevel() {
        Misc.setTimeout(() -> {
            Settings.SPRITE_SPEED = (int) (Settings.SPRITE_SPEED / Settings.GAME_SPEED_MULTIPLICATOR);
            Sound.setBackgroundPlayerRate(Sound.getBackgroundPlayerRate() * Settings.GAME_SPEED_MULTIPLICATOR);
            increaseLevel();
        }, Settings.GAME_SPEED_TIMER);
    }

    private void setKeyBindings() {
        screenManager.getScene().setOnKeyPressed((event -> {
            switch (event.getCode()) {
                case LEFT:
                    spaceship.moveLeft();
                    break;

                case RIGHT:
                    spaceship.moveRight();
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
        for (int i = 1; i < Settings.LANES; i++) {
            int x = Settings.COL_WIDTH * i;
            Line spacer = new Line(x, 0, x, Settings.COL_HEIGHT);
            spacer.getStyleClass().add("line");
            root.getChildren().add(spacer);
        }
    }

    private void initSpaceship() {
        spaceship = new Spaceship();
        root.getChildren().add(spaceship);
        spaceship.start();
    }

    public void startObstacleSpawning() {
        for (int i = 0; i < Settings.LANES; i++) {
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
                    if (!obstacle.getCollisioned() && obstacle.intersects(spaceship)) {
                        obstacle.setCollisioned(true);

                        pauseGame();
                        r.decreaseRemaingLifes();

                        obstacle.handleCollision(spaceship, () -> resumeGame());
                    }
                });
            }
        };

        collisionDetectionLoop.start();
    }

    public void spawnObstacle(int col) {
        Misc.setTimeout(() -> {
            if (!allowObstacleSpawning(col)) {
                Misc.setTimeout(() -> spawnObstacle(col), Settings.SPRITE_SPEED / 4);
                return;
            }

            Obstacle obstacle = ObstacleManager.createObstacle(col);

            obstacle.setOnStop(() -> {
                currentObstacleInLane.set(col, null);

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

        for (int i = 0; i < Settings.LANES; i++) {
            if (i != col && currentObstacleInLane.get(i) != null) {
                lanesWithObstacles++;
            }
        }

        return lanesWithObstacles < (Settings.LANES - 1);
    }
}
