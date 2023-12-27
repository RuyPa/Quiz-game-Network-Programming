package model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private int userId;
    private String userName;
    private String passWord;
    private String status;
    private int win;
    private int lose;

    public User(String userName, String passWord, String status, int win, int lose) {
        this.userName = userName;
        this.passWord = passWord;
        this.status = status;
        this.win = win;
        this.lose = lose;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public User() {
    }

    public User(String username, String password) {
        this.userName = username;
        this.passWord = password;
    }

    public User(int id, String userName){
        this.userId = id;
        this.userName = userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getStatus() {
        return status;
    }

    public int getWin() {
        return win;
    }

    public int getLose() {
        return lose;
    }
}
