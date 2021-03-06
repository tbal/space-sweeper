package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.multiscreen.ScreenManager;
import de.dynamobeuth.spacesweeper.config.Settings;
import de.dynamobeuth.spacesweeper.util.Helper;
import de.dynamobeuth.spacesweeper.util.Sound;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

/**
 * Game Logic
 */
public class Game {

    // Game States
    public enum State {
        READY,
        RUNNING,
        COLLISION_PAUSED,
        PAUSED,
        STOPPED,
        FINISHED
    }

    public ReadOnlyObjectProperty<State> stateProperty() {
        return state;
    }

    public boolean isState(State state) {
        return this.state.get().equals(state);
    }

    private SimpleObjectProperty<State> state = new SimpleObjectProperty<>();

    private final ScreenManager screenManager;

    private final Space space;

    private Timeline increaseLevelTimer;

    private AnimationTimer collisionDetectionLoop;

    private Timeline[] obstacleSpawningLoops = new Timeline[Settings.LANES];

    private Spaceship spaceship;

    private ObstacleManager obstacleManager = new ObstacleManager();

    private List<Obstacle> currentObstaclesInLanes = Arrays.asList(new Obstacle[Settings.LANES]);

    private List<Obstacle> currentLivesDecreasingObstaclesInLanes = Arrays.asList(new Obstacle[Settings.LANES]);

    private int gameSpeed;

    private int activeCollisions;

    private SimpleBooleanProperty gameOver = new SimpleBooleanProperty();

    public ReadOnlyBooleanProperty gameOverProperty() { return gameOver; }

    public IntegerProperty remainingLivesProperty() { return remainingLives; }

    private SimpleIntegerProperty remainingLives = new SimpleIntegerProperty();

    public ReadOnlyIntegerProperty levelProperty() { return level; }

    private SimpleIntegerProperty level = new SimpleIntegerProperty();

    public ReadOnlyIntegerProperty scoreProperty() { return score; }

    private SimpleIntegerProperty score = new SimpleIntegerProperty();

    /**
     * Constructor
     *
     * @param root Game container node where to place all the game objects
     * @param screenManager ScreenManager instance
     */
    public Game(Pane root, ScreenManager screenManager) {
        this.screenManager = screenManager;

        space = new Space(root);

        remainingLivesProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.intValue() == 0) {
                state.set(State.FINISHED);
                gameOver.set(true);
            }
        });

        resetStateValues();

        initSpaceship();
        setKeyBindings();

        state.set(State.READY);
    }

    /**
     * Start the game
     */
    public void start() {
        if (!isState(State.READY)) {
            return;
        }

        state.set(State.RUNNING);

        startCollisionDetectionLoop();
        startObstacleSpawningLoops();

        spaceship.start();
        startIncreaseLevelTimer();

        Sound.play(Sound.Sounds.GAME_START);
    }

    /**
     * Pause the game when requested
     */
    public void pause() {
        if (!isState(State.RUNNING) && !isState(State.COLLISION_PAUSED)) {
            return;
        }

        doPause();

        state.set(State.PAUSED);
    }

    /**
     * Short break if collision occurs
     */
    private void collisionPause() {
        if (!isState(State.RUNNING)) {
            return;
        }

        activeCollisions++;

        doPause();

        state.set(State.COLLISION_PAUSED);
    }

    /**
     * Pause the game
     * Should be called by pause() and collisionPause() only!
     */
    private void doPause() {
        pauseObstacleSpawningLoops();
        obstacleManager.pauseAll();
        stopCollisionDetectionLoop();

        spaceship.pause();
        pauseIncreaseLevelTimer();
    }

    /**
     * Resume after game was paused (e.g. by exit game dialog)
     */
    public void resume() {
        if (!isState(State.PAUSED)) {
            return;
        }

        doResume();
    }

    /**
     * Resume after collision
     */
    private void collisionResume() {
        if (isState(State.FINISHED) || !isState(State.COLLISION_PAUSED) || --activeCollisions > 0) {
            return;
        }

        doResume();
    }

    /**
     * Resume the game
     * Should be called by resume() and collisionResume() only!
     */
    private void doResume() {
        startCollisionDetectionLoop();

        spaceship.resume();

        obstacleManager.resumeAll();
        resumeObstacleSpawningLoops();
        resumeIncreaseLevelTimer();

        state.set(State.RUNNING);
    }

    /**
     * Stop the game
     */
    public void stop() {
        stopObstacleSpawningLoops();
        stopCollisionDetectionLoop();
        obstacleManager.removeAll();

        for (int i = 0; i < currentObstaclesInLanes.size(); i++) {
            currentObstaclesInLanes.set(i, null);
        }

        for (int i = 0; i < currentLivesDecreasingObstaclesInLanes.size(); i++) {
            currentLivesDecreasingObstaclesInLanes.set(i, null);
        }

        spaceship.reset();
        stopIncreaseLevelTimer();

        state.set(State.STOPPED);
    }

    /**
     * Reset game and all values associated with the game to make it ready for start
     */
    public void reset() {
        stop();

        resetStateValues();

        state.set(State.READY);
    }

    /**
     * Initialise our spaceship
     */
    private void initSpaceship() {
        spaceship = new Spaceship();
        space.getChildren().add(spaceship);
    }

    /**
     * Set keyboard bindings to control the spaceship
     */
    private void setKeyBindings() {
        screenManager.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    spaceship.moveLeft();
                    break;

                case RIGHT:
                    spaceship.moveRight();
                    break;
            }
        });
    }

    /**
     * Loop which checks for collisions on every scene graph pulse (around 60fps)
     */
    private void startCollisionDetectionLoop() {
        if (collisionDetectionLoop == null) {
            collisionDetectionLoop = new AnimationTimer() {
                @Override
                public void handle(long currentNanoTime) {

                    obstacleManager.getAll().forEach(obstacle -> {
                        if (!obstacle.isCollisioned() && obstacle.intersects(spaceship)) {
                            obstacle.setCollisioned(true);

                            obstacle.handleCollision(spaceship, () -> {
                                collisionPause();

                                remainingLives.set(remainingLives.get() + obstacle.getLivesImpact());

                                score.set(score.get() + obstacle.getScoreImpact());
                            }, () -> collisionResume());
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

    /**
     * Start spawning random obstacles with random delays
     */
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

    /**
     * Start timer which increases the level every defined seconds
     */
    private void startIncreaseLevelTimer() {
        increaseLevelTimer = Helper.setTimeout(() -> {
            increaseGameSpeed();
            increaseBackgroundSoundSpeed();

            level.set(level.get() + 1);

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

    /**
     * Reset score, level, points, state, etc.
     */
    private void resetStateValues() {
        remainingLives.set(Settings.INITIAL_LIVES);
        level.set(1);
        score.set(0);
        gameSpeed = Settings.SPRITE_SPEED;
        gameOver.set(false);
        activeCollisions = 0;
    }

    /**
     * Spawn an random obstacle in the given lane.
     * With some logic to avoid multiple obstacles in one lane
     * and live reducing obstacles in all lanes at the same time.
     *
     * @param lane Lane in which to spawn the obstacle
     */
    private void spawnObstacle(int lane) {
        if (!isState(State.RUNNING) && !isState(State.COLLISION_PAUSED)) {
            return;
        }

        if (obstacleSpawningLoops[lane] != null) {
            obstacleSpawningLoops[lane].stop();
        }

        obstacleSpawningLoops[lane] = Helper.setTimeout(() -> {
            Obstacle obstacle;

            if (!allowObstacleSpawningInLane(lane)) {
                Helper.setTimeout(() -> spawnObstacle(lane), gameSpeed / 4);
                return;
            } else if (!allowLivesDecreasingObstacleSpawningInLane(lane)) {
                obstacle = obstacleManager.createRandomObstacle(lane, gameSpeed, ObstacleManager.ObstacleTypes.PLANET);
            } else {
                obstacle = obstacleManager.createRandomObstacle(lane, gameSpeed);
            }

            obstacle.setOnStop(() -> {
                obstacleManager.removeObstacle(obstacle);

                currentLivesDecreasingObstaclesInLanes.set(lane, null);
                currentObstaclesInLanes.set(lane, null);

                spawnObstacle(lane);
            });

            space.getChildren().add(obstacle);
            obstacle.toBack();

            if (obstacle.getLivesImpact() < 0) {
                currentLivesDecreasingObstaclesInLanes.set(lane, obstacle);
            }
            currentObstaclesInLanes.set(lane, obstacle);

            if (!isState(State.PAUSED) && !isState(State.COLLISION_PAUSED)) {
                obstacle.start();
            }
        }, Helper.randomInRange(1, gameSpeed));
    }

    /**
     * Check if it is allowed to spawn an obstacle in the given lane
     *
     * @param lane Lane in which to check for allowed obstacle
     * @return True if allowed, false if not
     */
    private boolean allowObstacleSpawningInLane(int lane) {
        return currentObstaclesInLanes.get(lane) == null;
    }

    private boolean allowLivesDecreasingObstacleSpawningInLane(int lane) {
        int lanesWithLivesDecreasingObstacles = 0;

        for (int i = 0; i < Settings.LANES; i++) {
            if (i != lane && currentLivesDecreasingObstaclesInLanes.get(i) != null) {
                lanesWithLivesDecreasingObstacles++;
            }
        }

        return lanesWithLivesDecreasingObstacles < (Settings.LANES - 1);
    }

    /**
     * Increase game speed
     */
    private void increaseGameSpeed() {
        gameSpeed = (int) (gameSpeed / Settings.GAME_SPEED_MULTIPLICATOR);
    }

    /**
     * Increase background sound speed
     */
    private void increaseBackgroundSoundSpeed() {
        Sound.setBackgroundPlayerRate(Sound.getBackgroundPlayerRate() * Settings.GAME_SPEED_MULTIPLICATOR);
    }
}
