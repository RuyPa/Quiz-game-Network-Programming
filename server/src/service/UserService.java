package service;

import model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    User getUserByUsername(String username) throws SQLException;
    List<User> getActiveUser(int myId) throws SQLException;
}
