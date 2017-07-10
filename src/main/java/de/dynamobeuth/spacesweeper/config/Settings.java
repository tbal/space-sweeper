package de.dynamobeuth.spacesweeper.config;

public class Settings {
    public static final String SKIN = "default";
    public static final int LANES = 3;
    public static final int COL_WIDTH = 100;
    public static final int COL_HEIGHT = 600;
    public static final int PLAYER_MOVE_SPEED = 300; // in millis
    public static final int SPRITE_SPEED = 2500; // in millis
    public static final int LEVEL_UP_TIMER = 30000; // in millis
    public static final double GAME_SPEED_MULTIPLICATOR = 1.1; // in percent per timer
    public static final String DATABASE_URL = "https://space-sweeper.firebaseio.com";
    public static final Boolean SOUND = true;
    public static final int INITIAL_LIVES = 3;
}
