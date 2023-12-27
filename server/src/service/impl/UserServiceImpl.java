package service.impl;

import model.User;
import repository.UserRepository;
import service.UserService;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserByUsername(String username) throws SQLException {
        return UserRepository.getUserByAccount(username);
    }

    @Override
    public List<User> getActiveUser(int myId) throws SQLException {
        return UserRepository.getActiveUsers(myId);
    }
}
