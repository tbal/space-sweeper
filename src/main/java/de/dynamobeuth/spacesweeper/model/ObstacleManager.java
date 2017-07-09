package de.dynamobeuth.spacesweeper.model;

import de.dynamobeuth.spacesweeper.model.obstacle.*;
import de.dynamobeuth.spacesweeper.util.Misc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ObstacleManager {

    private enum ObstacleTypes {
        // good obstacles
        ASTRONAUT (Astronaut.class, 2),
        SATELLITE (Satellite.class, 4),

        // bad obstacles
        METEOR (Meteor.class, 8),
        UFO (Ufo.class, 5),
        PLANET (Planet.class, 5);

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

    private List<Class<Obstacle>> obstacleTypesList = new ArrayList<>(); // TODO: better var naming

    private List<Obstacle> obstacles = new ArrayList<>();

    public ObstacleManager() {
        for (ObstacleTypes obstacleType : ObstacleTypes.values()) {
            for (int i = 0; i < obstacleType.getProbability(); i++) {
                obstacleTypesList.add(obstacleType.getObstacleClass());
            }
        }
    }

    public Obstacle createRandomObstacle(int column, int speed) {
        Class<Obstacle> obstacleClass = obstacleTypesList.get(Misc.randomInRange(0, obstacleTypesList.size() - 1));

        Obstacle obstacle = null;
        try {
            obstacle = obstacleClass.getConstructor(int.class, int.class).newInstance(column, speed);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        obstacles.add(obstacle);

        return obstacle;
    }

    public List<Obstacle> getAll() {
        return obstacles;
    }

    public void removeObstacle(Obstacle obstacle) {
        ((Space) obstacle.getParent()).getChildren().remove(obstacle);

        obstacles.remove(obstacle);
    }

    public void removeAll() {
        getAll().forEach(obstacle -> {
            ((Space) obstacle.getParent()).getChildren().remove(obstacle);
        });

        obstacles.clear();
    }

    public void pauseAll() {
        obstacles.forEach(Obstacle::pause);
    }

    public void resumeAll() {
        obstacles.forEach(Obstacle::resume);
    }
}
