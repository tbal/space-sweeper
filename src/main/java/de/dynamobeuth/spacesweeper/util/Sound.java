package de.dynamobeuth.spacesweeper.util;

import de.dynamobeuth.spacesweeper.config.Settings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class Sound {

    private static List<MediaPlayer> indefiniteSounds = new ArrayList<>();

    private static final String skinPath = "../skin/" + Settings.SKIN;
    private static final String skinSoundPath = skinPath + "/snd/";

    public enum Sounds {
        THEME (skinSoundPath + "theme.mp3"),
        BUTTON_ACTION (skinSoundPath + "button_action.mp3"), // game start
        HIT (skinSoundPath + "hit.mp3"), // bad obstacles
        EXPLODE (skinSoundPath + "explode.mp3"), // life affecting hit
        CONSUME (skinSoundPath + "consume.mp3"), // good obstacles
        GAME_START (skinSoundPath + "game_start.mp3"), // game start
        GAME_OVER (skinSoundPath + "game_iover.mp3"), // game over
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

    public static SimpleBooleanProperty soundEnabledProperty = new SimpleBooleanProperty(Settings.SOUND);

    public static void play(Sounds sound) {
        (new MediaPlayer(sound.getMedia())).play();
    }

    public static void playIndefinite(Sounds sound) {
        MediaPlayer mediaPlayer = new MediaPlayer(sound.getMedia());
        mediaPlayer.muteProperty().bind(soundEnabledProperty.not());
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        indefiniteSounds.add(mediaPlayer);
    }
}