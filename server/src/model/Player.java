package model;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String username;
    private int correctAnswers;
    private int usedTime;

    public Player() {
    }

    public Player(int id, String username, int correctAnswers, int usedTime) {
        this.id = id;
        this.username = username;
        this.correctAnswers = correctAnswers;
        this.usedTime = usedTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getUsedTime() {
        return usedTime;
    }
}
