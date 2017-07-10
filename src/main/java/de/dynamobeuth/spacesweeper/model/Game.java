package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.multiscreen.ScreenManager;
import de.dynamobeuth.spacesweeper.config.Settings;
import de.dynamobeuth.spacesweeper.util.Helper;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

public class Game {

    private final ScreenManager screenManager;

    private final Space space;

    private boolean gamePaused = false;

    private Timeline increaseLevelTimer;

    private AnimationTimer collisionDetectionLoop;

    private Timeline[] obstacleSpawningLoops = new Timeline[Settings.LANES];

    private Spaceship spaceship;

    private ObstacleManager obstacleManager = new ObstacleManager();

    private List<Obstacle> currentObstacleInLane = Arrays.asList(new Obstacle[Settings.LANES]);

    private int gameSpeed;

    public ReadOnlyIntegerProperty remainingLivesProperty() { return remainingLives; }

    private SimpleIntegerProperty remainingLives = new SimpleIntegerProperty();

    public ReadOnlyIntegerProperty levelProperty() { return level; }

    private SimpleIntegerProperty level = new SimpleIntegerProperty();

    public ReadOnlyIntegerProperty scoreProperty() { return score; }

    private SimpleIntegerProperty score = new SimpleIntegerProperty();

    public Game(Pane root, ScreenManager screenManager) {
        this.screenManager = screenManager;

        space = new Space(root);

        resetStateValues();

        initSpaceship();
        setKeyBindings();
    }

    public void start() {
        reset();

        startCollisionDetectionLoop();
        startObstacleSpawningLoops();

        spaceship.start();
        startIncreaseLevelTimer();
    }

    public void pause() {
        gamePaused = true;

        pauseObstacleSpawningLoops();
        obstacleManager.pauseAll();
        stopCollisionDetectionLoop();

        spaceship.pause();
        pauseIncreaseLevelTimer();
    }

    public void stop() {
        stopObstacleSpawningLoops();
        stopCollisionDetectionLoop();
        obstacleManager.removeAll();

        for (int i = 0; i < currentObstacleInLane.size(); i++) {
            currentObstacleInLane.set(i, null);
        }

        spaceship.reset();
        stopIncreaseLevelTimer();

        gamePaused = false;
    }

    public void resume() {
        startCollisionDetectionLoop();
        obstacleManager.resumeAll();
        resumeObstacleSpawningLoops();

        spaceship.resume();
        resumeIncreaseLevelTimer();

        gamePaused = false;
    }

    public void reset() {
        stop();

        resetStateValues();
    }

    private void initSpaceship() {
        spaceship = new Spaceship();
        space.getChildren().add(spaceship);
    }

    private void setKeyBindings() {
        screenManager.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    spaceship.moveLeft();
                    break;

                case RIGHT:
                    spaceship.moveRight();
                    break;

                case ESCAPE:
                    // TODO
                    if (gamePaused) {
                        resume();
                    } else {
                        pause();
                    }
                    break;
            }
        });
    }

    private void startCollisionDetectionLoop() {
        if (collisionDetectionLoop == null) {
            collisionDetectionLoop = new AnimationTimer() {
                @Override
                public void handle(long currentNanoTime) {

                    obstacleManager.getAll().forEach(obstacle -> {
                        if (!obstacle.getCollisioned() && obstacle.intersects(spaceship)) {
                            obstacle.setCollisioned(true);

                            obstacle.handleCollision(spaceship, () -> {
                                pause();

                                remainingLives.set(remainingLives.get() + obstacle.getLivesImpact());

                                score.set(score.get() + obstacle.getScoreImpact());
                            }, () -> resume());
                        }
                    });
                }
            };
        }

        collisionDetectionLoop.start();
    }

    private void stopCollisionDetectionLoop() {
        if (collisionDetectionLoop != null) {
            collisionDetectionLoop.stop();
        }
    }

    private void startObstacleSpawningLoops() {
        for (int i = 0; i < Settings.LANES; i++) {
            spawnObstacle(i);
        }
    }

    private void stopObstacleSpawningLoops() {
        for (Timeline obstacleSpawningLoop : obstacleSpawningLoops) {
            if (obstacleSpawningLoop != null) {
                obstacleSpawningLoop.stop();
            }
        }
    }

    private void pauseObstacleSpawningLoops() {
        for (Timeline obstacleSpawningLoop : obstacleSpawningLoops) {
            if (obstacleSpawningLoop != null) {
                obstacleSpawningLoop.pause();
            }
        }
    }

    private void resumeObstacleSpawningLoops() {
        for (Timeline obstacleSpawningLoop : obstacleSpawningLoops) {
            if (obstacleSpawningLoop != null) {
                obstacleSpawningLoop.play();
            }
        }
    }

    private void startIncreaseLevelTimer() {
        increaseLevelTimer = Helper.setTimeout(() -> {
            increaseGameSpeed();
            increaseBackgroundSoundSpeed();

            level.set(level.get() + 1);
            score.set(score.get() + 100); // TODO: discuss

            startIncreaseLevelTimer();

        }, Settings.LEVEL_UP_TIMER);
    }

    private void stopIncreaseLevelTimer() {
        if (increaseLevelTimer != null) {
            increaseLevelTimer.stop();
        }
    }

    private void pauseIncreaseLevelTimer() {
        if (increaseLevelTimer != null) {
            increaseLevelTimer.pause();
        }
    }

    private void resumeIncreaseLevelTimer() {
        if (increaseLevelTimer != null) {
            increaseLevelTimer.play();
        }
    }

    private void resetStateValues() {
        remainingLives.set(Settings.INITIAL_LIVES);
        level.set(1);
        score.set(0);
        gameSpeed = Settings.SPRITE_SPEED;
    }

    private void spawnObstacle(int lane) {
        if (gamePaused) {
            return;
        }

        if (obstacleSpawningLoops[lane] != null) {
            obstacleSpawningLoops[lane].stop();
        }

        obstacleSpawningLoops[lane] = Helper.setTimeout(() -> {
            System.out.println("spawn! for lane: " + (lane + 1)); // TODO: remove
            if (!allowObstacleSpawning(lane)) {
                System.out.println("not allowed for lane: " + (lane + 1)); // TODO: remove
                Helper.setTimeout(() -> spawnObstacle(lane), gameSpeed / 4);
                return;
            }

            Obstacle obstacle = obstacleManager.createRandomObstacle(lane, gameSpeed);

            obstacle.setOnStop(() -> {
                obstacleManager.removeObstacle(obstacle);

                currentObstacleInLane.set(lane, null);

                spawnObstacle(lane);
            });

            space.getChildren().add(obstacle);
            currentObstacleInLane.set(lane, obstacle);
            obstacle.toBack();

            if (!gamePaused) {
                obstacle.start();
            }
        }, Helper.randomInRange(1, gameSpeed));
    }

    private boolean allowObstacleSpawning(int lane) {
        if (currentObstacleInLane.get(lane) != null) {
            return false;
        }

        int lanesWithObstacles = 0;

        for (int i = 0; i < Settings.LANES; i++) {
            if (i != lane && currentObstacleInLane.get(i) != null) {
                lanesWithObstacles++;
            }
        }

        return lanesWithObstacles < (Settings.LANES - 1);
    }

    private void increaseGameSpeed() {
        gameSpeed = (int) (gameSpeed / Settings.GAME_SPEED_MULTIPLICATOR);
    }

    private void increaseBackgroundSoundSpeed() {
        Sound.setBackgroundPlayerRate(Sound.getBackgroundPlayerRate() * Settings.GAME_SPEED_MULTIPLICATOR);
    }
}
