package de.dynamobeuth.spacesweeper.model;

import java.util.ArrayList;
import java.util.List;

public class ObstacleManager {

    private List<Obstacle> obstacles = new ArrayList<>();

    public Obstacle createObstacle(int column) {
        Obstacle obstacle = new Astronaut(column);
        obstacles.add(obstacle);
        return obstacle;
    }

    public List<Obstacle> getAll() {
        return obstacles;
    }

//    public static TreeMap<Double, Obstacle> getYMap() {
//        TreeMap<Double, Obstacle> treeMap = new TreeMap<>();
//        obstacles.forEach(obstacle -> treeMap.put(obstacle.getTranslateY(), obstacle));
//        return treeMap;
//    }

    public void removeObstacle(Obstacle obstacle) {
        ((Space) obstacle.getParent()).getChildren().remove(obstacle);
        obstacles.remove(obstacle);
    }

    public void pauseAll() {
        obstacles.forEach(obstacle -> obstacle.pause());
    }

    public void resumeAll() {
        obstacles.forEach(obstacle -> obstacle.resume());
    }
}
