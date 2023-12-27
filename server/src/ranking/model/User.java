package ranking.model;

public class User implements Comparable<User>{
    private static final long serialVersionUID = 1L;
    private int userId;
    private String userName;
    private String passWord;
    private String status;
    private int win;
    private int totalQuestion;
    private int lose;
    private int timeRemaining;
    private int timeUsed;
    private int score;

    public User(String userName, int totalQuestion, int score, int timeUsed, int timeRemaining) {
        this.userName = userName;
        this.totalQuestion = totalQuestion;
        this.timeRemaining = timeRemaining;
        this.timeUsed = timeUsed;
        this.score = score;
    }


    public User(String userName, String passWord, String status, int win, int lose) {
        this.userName = userName;
        this.passWord = passWord;
        this.status = status;
        this.win = win;
        this.lose = lose;
    }

    public User() {
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public User(int userId, String username, String password) {
        this.userId = userId;
        this.userName = username;
        this.passWord = password;
    }

    public User(Integer userId, String username) {
        this.userName = username;
        this.userId = userId;
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

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public int getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(int timeUsed) {
        this.timeUsed = timeUsed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(User b) {
        if(this.score < b.score) return b.score - this.score;
        return b.timeUsed - this.timeUsed;
    }
}