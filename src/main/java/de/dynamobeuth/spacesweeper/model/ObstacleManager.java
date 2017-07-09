package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.spacesweeper.model.obstacle.*;
import de.dynamobeuth.spacesweeper.util.Misc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ObstacleManager {

    private enum ObstacleTypes {
        // good obstacles
        ASTRONAUT (Astronaut.class, 7),
        SATELLITE (Satellite.class, 5),

        // bad obstacles
        METEOR (Meteor.class, 10),
        UFO (Ufo.class, 5),
        PLANET (Planet.class, 3);

        private Class obstacleClass;
        private final double probability;

        ObstacleTypes(Class obstacleClass, int probability) {
            this.obstacleClass = obstacleClass;
            this.probability = probability;
        }

        public Class getObstacleClass() {
            return obstacleClass;
        }

        public double getProbability() {
            return probability;
        }
    }

    private List<Obstacle> obstacles = new ArrayList<>();

    private ObstacleTypes[] obstacleTypes = ObstacleTypes.values();

    private List<Class<Obstacle>> obstacleTypesList = new ArrayList<>();

    public ObstacleManager() {
        for (ObstacleTypes obstacleType : obstacleTypes) {
            for (int i = 0; i < obstacleType.getProbability(); i++) {
                obstacleTypesList.add(obstacleType.getObstacleClass());
            }
        }

        System.out.println(obstacleTypesList);
    }

    public Obstacle createRandomObstacle(int column) {
        Class<Obstacle> obstacleClass = obstacleTypesList.get(Misc.randomInRange(0, obstacleTypesList.size() - 1));

        Obstacle obstacle = null;
        try {
            obstacle = obstacleClass.getConstructor(int.class).newInstance(column);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

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
