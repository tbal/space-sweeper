package de.dynamobeuth.spacesweeper.model;

/**
 * Highscore Entry
 */
public class HighscoreEntry {

    private String key;

    private String name;

    private int score;

    public HighscoreEntry() {}

    public HighscoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public String toString() {
        return "Key: " + key + ", Name: " + name + ", Score: " + score;
    }
}
