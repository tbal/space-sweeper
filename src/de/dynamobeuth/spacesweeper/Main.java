package de.dynamobeuth.spacesweeper;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Box;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private boolean gamePaused = false;
    private Group root;
    private AnimationTimer collisionDetectionLoop;
    private Spaceship spaceship;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("SpaceSweeper.fxml"));

        root = new Group();

        for (int i = 1; i < Settings.COL_COUNT; i++) {
            int x = Settings.COL_WIDTH * i;
            Line spacer = new Line(x, 0, x, Settings.COL_HEIGHT);
            root.getChildren().add(spacer);
        }

        spaceship = new Spaceship();
        root.getChildren().add(spaceship);

        final Box keyboardNode = new Box();
        keyboardNode.setFocusTraversable(true);
        keyboardNode.requestFocus();
        keyboardNode.setOnKeyPressed((event -> {
            if (event.getCode() == KeyCode.LEFT) {
                spaceship.move(-1);
            }
            if (event.getCode() == KeyCode.RIGHT) {
                spaceship.move(+1);
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                if (gamePaused) {
                    resumeGame();
                } else {
                    pauseGame();
                }
            }
        }));
        root.getChildren().add(keyboardNode);

        primaryStage.setTitle("Space Sweeper - Der letzte räumt den Weltraum auf");
        primaryStage.setScene(new Scene(root, Settings.COL_COUNT * Settings.COL_WIDTH, Settings.COL_HEIGHT));
        primaryStage.show();

        startGame();
    }

    public void startGame() {
        for (int i = 0; i < Settings.COL_COUNT; i++) {
            spawnObstacle(i);
        }

        startCollisionDetectionLoop();
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
                    if (obstacle.intersects(spaceship.getBoundsInLocal())) {
                        pauseGame();

                        FadeTransition collisionTransition = new FadeTransition(Duration.millis(100), obstacle);
                        collisionTransition.setFromValue(1.0);
                        collisionTransition.setToValue(0.0);
                        collisionTransition.setCycleCount(7);
                        collisionTransition.setAutoReverse(true);
                        collisionTransition.setOnFinished(value -> {
                            obstacle.stop();
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
        Utils.setTimeout(() -> {
            Obstacle obstacle = ObstacleManager.createObstacle(col);

            obstacle.getAnimation().setOnFinished(value -> {
                ObstacleManager.removeObstacle(obstacle);
                root.getChildren().remove(obstacle);
                spawnObstacle(col);
            });

            root.getChildren().add(obstacle);
            obstacle.toBack();

            if (!gamePaused) {
                obstacle.start();
            }
        }, Utils.randomInRange(0, Settings.SPRITE_SPEED));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
