package de.dynamobeuth.spacesweeper.util;

import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class Sound {

    public enum Sounds {
        /* long running background sounds */
        BACKGROUND_START(SOUND_PATH + "bg_start.mp3"),
        BACKGROUND_GAME(SOUND_PATH + "bg_game.mp3"),
        BACKGROUND_HIGHSCORE (SOUND_PATH + "bg_highscore.mp3"),

        /* short one-time play sounds */
        // UI
        BUTTON_HOVER (SOUND_PATH + "button_hover.mp3"),

        // Collisions
        HIT (SOUND_PATH + "hit.mp3"),
        EXPLODE (SOUND_PATH + "explode.mp3"),
        CONSUME (SOUND_PATH + "consume.mp3"),

        // Game
        GAME_START (SOUND_PATH + "game_start.mp3"),
        GAME_OVER (SOUND_PATH + "game_over.mp3");

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

    public static void resumeBackgroundPlayer() {
        if (backgroundPlayer != null) {
            backgroundPlayer.play();
        }
    }

    public static void pauseBackgroundPlayer() {
        if (backgroundPlayer != null) {
            backgroundPlayer.pause();
        }
    }

    public static void setBackgroundPlayerRate(double rate) {
        if (backgroundPlayer != null) {
            backgroundPlayer.setRate(rate);
        }
    }

    public static double getBackgroundPlayerRate() {
        if (backgroundPlayer == null) {
            return 0.0;
        }

        return backgroundPlayer.getRate();
    }
}