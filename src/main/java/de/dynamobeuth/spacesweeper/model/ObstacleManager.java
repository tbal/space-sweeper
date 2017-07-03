package de.dynamobeuth.spacesweeper.model;

import java.util.ArrayList;
import java.util.List;

public class ObstacleManager {

    private static List<Obstacle> obstacles = new ArrayList<>();

    public static Obstacle createObstacle() {
        Obstacle obstacle = new Obstacle();
        obstacles.add(obstacle);
        return obstacle;
    }

    public static Obstacle createObstacle(int column) {
        Obstacle obstacle = new Obstacle(column);
        obstacles.add(obstacle);
        return obstacle;
    }

    public static List<Obstacle> getAll() {
        return obstacles;
    }

//    public static TreeMap<Double, Obstacle> getYMap() {
//        TreeMap<Double, Obstacle> treeMap = new TreeMap<>();
//        obstacles.forEach(obstacle -> treeMap.put(obstacle.getTranslateY(), obstacle));
//        return treeMap;
//    }

    public static void removeObstacle(Obstacle obstacle) {
        obstacles.remove(obstacle);
    }

    public static void pauseAll() {
        obstacles.forEach(obstacle -> obstacle.pause());
    }

    public static void resumeAll() {
        obstacles.forEach(obstacle -> obstacle.resume());
    }
}
