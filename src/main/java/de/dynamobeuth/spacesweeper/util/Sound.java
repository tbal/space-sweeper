package de.dynamobeuth.spacesweeper.util;

import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
            return new Media(Sound.class.getResource(this.resource).toExternalForm());
        }
    }

    private static MediaPlayer backgroundPlayer;

    private static final String skinSoundPath = "/de/dynamobeuth/spacesweeper/skin/" + Settings.SKIN + "/snd/";

    public static SimpleBooleanProperty soundEnabledProperty = new SimpleBooleanProperty(Settings.SOUND);

    public static void play(Sounds sound) {
        (new MediaPlayer(sound.getMedia())).play();
    }

    public static void playBackground(Sounds sound) {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
        }

        backgroundPlayer = new MediaPlayer(sound.getMedia());
        backgroundPlayer.muteProperty().bind(soundEnabledProperty.not());
        backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundPlayer.play();
    }

    public static void resumeBackgroundPlayer() {
        backgroundPlayer.play();
    }

    public static void pauseBackgroundPlayer() {
        backgroundPlayer.pause();
    }

    public static void setBackgroundPlayerRate(double rate) {
        backgroundPlayer.setRate(rate);
    }

    public static double getBackgroundPlayerRate() {
        return backgroundPlayer.getRate();
    }
}