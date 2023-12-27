package model;

import consts.GameConstant;
import service.UserService;
import service.impl.UserServiceImpl;

import java.net.Socket;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Room {
    private String id;
    private List<Integer> userId;
    private Map<Integer, Integer> userScore;

    private List<Player> players;
    private LocalDateTime localDateTime;

    private Game game = GameConstant.game1;

    Map<Integer, List<String>> currentScore;

    public void setCurrentScore(Map<Integer, List<String>> currentScore) {
        this.currentScore = currentScore;
    }

    public Map<Integer, List<String>> getCurrentScore() {
        return currentScore;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public Room(Integer userId1, Integer userId2) {
        UserService userService = new UserServiceImpl();
        id = String.valueOf(userId1) + String.valueOf(userId2);
        userId = new ArrayList<>();
        userId.add(userId1);
        userId.add(userId2);
        userScore = new HashMap<>();
        players = new ArrayList<>();
        long epochSeconds = Instant.now().getEpochSecond();

        // Chuyển đổi thành Instant
        Instant instant = Instant.ofEpochSecond(epochSeconds);

        // Chuyển đổi thành LocalDateTime
        localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        List<String> currentIn4 = new ArrayList<>();
        currentIn4.add("0");
        currentIn4.add("0");
        currentScore = new HashMap<>();
        currentScore.put(userId1,currentIn4 );
        currentScore.put(userId2,currentIn4);
    }

//    public Room(List<Integer> userId, Map<Integer, Integer> userScore) {
//        this.userId = userId;
//        this.userScore = userScore;
//    }

    public void setUserId(List<Integer> userId) {
        this.userId = userId;
    }

    public void setUserScore(Map<Integer, Integer> userScore) {
        this.userScore = userScore;
    }

    public List<Integer> getUserId() {
        return userId;
    }

    public Map<Integer, Integer> getUserScore() {
        return userScore;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
