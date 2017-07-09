package de.dynamobeuth.spacesweeper.util;

import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class Sound {

    public enum Sounds {
        THEME (skinSoundPath + "theme.mp3"),
        IN_GAME (skinSoundPath + "in_game.mp4"),
        HIGHSCORE (skinSoundPath + "highscore.mp4"),
        BUTTON_ACTION (skinSoundPath + "button_action.mp3"), // game start
        HIT (skinSoundPath + "hit.mp3"), // bad obstacles
        EXPLODE (skinSoundPath + "explode.mp3"), // life affecting hit
        CONSUME (skinSoundPath + "consume.mp3"), // good obstacles
        GAME_START (skinSoundPath + "game_start.mp3"), // game start
        GAME_OVER (skinSoundPath + "game_over.mp3"), // game over
        ;

        private final String resource;

        Sounds(String s) {
            resource = s;
        }

        public String toString() {
            return this.resource;
        }

        public Media getMedia() {
            URL resource = Sound.class.getResource(this.resource);

            if (resource == null) {
                return null;
            }

            return new Media(resource.toExternalForm());
        }
    }

    private static MediaPlayer backgroundPlayer;

    private static final String skinSoundPath = "/de/dynamobeuth/spacesweeper/skin/" + Settings.SKIN + "/snd/";

    public static SimpleBooleanProperty soundEnabledProperty = new SimpleBooleanProperty(Settings.SOUND);

    public static SimpleBooleanProperty soundAvailableProperty = new SimpleBooleanProperty(true);

    public static void play(Sounds sound) {
        if (!soundAvailableProperty.get()) {
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