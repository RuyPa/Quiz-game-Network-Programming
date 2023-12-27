package controller;

import model.User;
import repository.UserRepository;
import service.UserService;
import service.impl.UserServiceImpl;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class UserController {
    public static UserService userService = new UserServiceImpl();

    public static void login(ObjectOutputStream objectOutputStream, User user) throws SQLException, IOException {
        User result = userService.getUserByUsername(user.getUserName());

        if(result.getPassWord().equals(user.getPassWord())){
            List<User> users = userService.getActiveUser(result.getUserId());
            objectOutputStream.writeObject(users);
        } else{
            Boolean outputResult = false;
            objectOutputStream.writeObject(outputResult);
        }
    }
}
