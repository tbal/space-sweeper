package de.dynamobeuth.spacesweeper.util;

import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Utility class to play sounds
 */
final public class Sound {

    public enum Sounds {
        /* long running background sounds */
        BACKGROUND_START(SOUND_PATH + "bg_start.mp3"),
        BACKGROUND_GAME(SOUND_PATH + "bg_game.mp3"),
        BACKGROUND_HIGHSCORE (SOUND_PATH + "bg_highscore.mp3"),

        /* short one-time play sounds */
        // UI
        BUTTON_HOVER (SOUND_PATH + "button_hover.mp3"),
        HIGHSCORE_ENTRY_ADDED (SOUND_PATH + "highscore_entry_added.mp3"),

        // Collisions
        HIT (SOUND_PATH + "hit.mp3"),
        EXPLODE (SOUND_PATH + "explode.mp3"),
        CONSUME (SOUND_PATH + "consume.mp3"),

        // Game
        GAME_START (SOUND_PATH + "game_start.mp3");

        private final String soundFilePath;

        Sounds(String soundFilePath) {
            this.soundFilePath = soundFilePath;
        }

        public String toString() {
            return soundFilePath;
        }

        public Media getMedia() {
            URL resource = Sound.class.getResource(soundFilePath);

            if (resource == null) {
                return null;
            }

            return new Media(resource.toExternalForm());
        }
    }

    private static MediaPlayer backgroundPlayer;

    private static final String SOUND_PATH = "/de/dynamobeuth/spacesweeper/skin/" + Settings.SKIN + "/sound/";

    public static SimpleBooleanProperty soundEnabledProperty = new SimpleBooleanProperty(Settings.SOUND);

    public static SimpleBooleanProperty soundAvailableProperty = new SimpleBooleanProperty(true);

    /**
     * Disallow instances of this class as it is a utility class with only static methods
     */
    private Sound() {}

    /**
     * Play one-time sound
     *
     * @param sound The sound to play
     */
    public static void play(Sounds sound) {
        if (!soundAvailableProperty.get() || !soundEnabledProperty.get()) {
            return;
        }

        Media media = sound.getMedia();

        if (media == null) {
            return;
        }

        try {
            (new MediaPlayer(media)).play();

        } catch (MediaException e) {
            soundAvailableProperty.set(false);

            e.printStackTrace();
        }
    }

    /**
     * Play sound in background in loop in until it is stopped
     * or overwritten by using this method again with another sound.
     *
     * @param sound The sound to play
     */
    public static void playBackground(Sounds sound) {
        if (!soundAvailableProperty.get()) {
            return;
        }

        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
        }

        Media media = sound.getMedia();

        if (media == null) {
            return;
        }

        try {
            backgroundPlayer = new MediaPlayer(media);
            backgroundPlayer.muteProperty().bind(soundEnabledProperty.not());
            backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundPlayer.play();

        } catch (MediaException e) {
            soundAvailableProperty.set(false);

            e.printStackTrace();
        }
    }

    /**
     * Sets the playback rate of the background sound
     *
     * @param rate The playback rate
     */
    public static void setBackgroundPlayerRate(double rate) {
        if (backgroundPlayer != null) {
            backgroundPlayer.setRate(rate);
        }
    }

    /**
     * Gets the current playback rate of the background sound
     *
     * @return The playback rate
     */
    public static double getBackgroundPlayerRate() {
        if (backgroundPlayer == null) {
            return 0.0;
        }

        return backgroundPlayer.getRate();
    }
}