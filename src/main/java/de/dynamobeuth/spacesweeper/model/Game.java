package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.multiscreen.ScreenManager;
import de.dynamobeuth.spacesweeper.component.RemainingLivesComponent;
import de.dynamobeuth.spacesweeper.config.Settings;
import de.dynamobeuth.spacesweeper.util.Misc;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.animation.AnimationTimer;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

import static de.dynamobeuth.spacesweeper.util.Sound.Sounds.IN_GAME;

public class Game {
    private final ScreenManager screenManager;
    private final Space space;
    private final RemainingLivesComponent r;

    private boolean gamePaused = false;

    public ReadOnlyIntegerProperty levelProperty() { return level; }

    private SimpleIntegerProperty level = new SimpleIntegerProperty(1);

    public ReadOnlyIntegerProperty scoreProperty() { return score; }

    private SimpleIntegerProperty score = new SimpleIntegerProperty(0);

    private AnimationTimer collisionDetectionLoop;

    private Spaceship spaceship;

    private ObstacleManager obstacleManager = new ObstacleManager();

    private List<Obstacle> currentObstacleInLane = Arrays.asList(new Obstacle[Settings.LANES]);

    public Game(Pane root, ScreenManager screenManager, RemainingLivesComponent r) {
        this.screenManager = screenManager;
        this.r = r; // FIXME: solution with properties

        space = new Space(root);

        Sound.playBackground(IN_GAME);

        initSpaceship();
        setKeyBindings();
        startCollisionDetectionLoop();
        startObstacleSpawning();
        increaseLevel();
    }

    private void increaseLevel() {
        Misc.setTimeout(() -> {
            increaseGameSpeed();
            increaseBackgroundSoundSpeed();
            level.set(level.get() + 1);
            score.set(score.get() + 100);
            increaseLevel();
        }, Settings.GAME_SPEED_TIMER);
    }

    private void increaseGameSpeed() {
        Settings.SPRITE_SPEED = (int) (Settings.SPRITE_SPEED / Settings.GAME_SPEED_MULTIPLICATOR);
    }

    private void increaseBackgroundSoundSpeed() {
        Sound.setBackgroundPlayerRate(Sound.getBackgroundPlayerRate() * Settings.GAME_SPEED_MULTIPLICATOR);
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

    private void initSpaceship() {
        spaceship = new Spaceship();
        space.getChildren().add(spaceship);
        spaceship.start();
    }

    public void startObstacleSpawning() {
        for (int i = 0; i < Settings.LANES; i++) {
            spawnObstacle(i);
        }
    }

    public void pauseGame() {
        obstacleManager.pauseAll();
        collisionDetectionLoop.stop();
        spaceship.pause();
        gamePaused = true;
    }

    public void resumeGame() {
        collisionDetectionLoop.start();
        obstacleManager.resumeAll();
        spaceship.resume();
        gamePaused = false;
    }

    public void startCollisionDetectionLoop() {
        collisionDetectionLoop = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {

                obstacleManager.getAll().forEach(obstacle -> {
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

            Obstacle obstacle = obstacleManager.createObstacle(col);

            obstacle.setOnStop(() -> {
                obstacleManager.removeObstacle(obstacle);

                currentObstacleInLane.set(col, null);

                spawnObstacle(col);
            });

            space.getChildren().add(obstacle);
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
