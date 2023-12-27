package repository;

import config.DatabaseConfiguration;
import model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository{

    static DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
//  for Mysql
//    public static User getUserByAccount(String username) throws SQLException {
////        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
//        String query = "SELECT userId, username, password FROM users WHERE username = ?";
//        PreparedStatement statement =  databaseConfiguration.connection.prepareStatement(query);
//        statement.setString(1, username);
//
//        ResultSet resultSet = statement.executeQuery();
//
//        User result= new User();
//        if(resultSet.next()){
//            result = new User(resultSet.getInt("userId"),
//                    resultSet.getString("username"),
//                    resultSet.getString("password"));
//        }
//        return result;
//    }

    public static User getUserByAccount(String username) throws SQLException {
        User user = new User();
        user.setUserName("000");
        user.setPassWord("111");
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            int dem = 0;
            while ((line = reader.readLine()) != null) {
                if(dem == 0){
                    dem++;
                    continue;
                }
                // Phân tích dữ liệu từ file và tạo đối tượng User
                String[] userData = line.split(",");
                if(username.equals(userData[1])){
                    user.setUserId(Integer.parseInt(userData[0]));
                    user.setUserName(userData[1]);
                    user.setPassWord(userData[2]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getUserById(int id) throws SQLException {
        User user = new User();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            int dem = 0;
            while ((line = reader.readLine()) != null) {
                if(dem == 0){
                    dem++;
                    continue;
                }
                // Phân tích dữ liệu từ file và tạo đối tượng User
                String[] userData = line.split(",");
                if(id == Integer.parseInt(userData[0])){
                    user.setUserId(Integer.parseInt(userData[0]));
                    user.setUserName(userData[1]);
                    user.setPassWord(userData[2]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

//sql
//    public static List<User> getActiveUsers(int myId) throws SQLException{
//        String query = "SELECT userId, username FROM users where status = 1 and userId != ?";
//        PreparedStatement statement = databaseConfiguration.connection.prepareStatement(query);
//        statement.setInt(1, myId);
//        ResultSet resultSet = statement.executeQuery();
//
//        List<User> users = new ArrayList<>();
//        while (resultSet.next()){
//            users.add(new User(resultSet.getInt("userId"),
//                                resultSet.getString("username")));
//        }
//        System.out.println(users.size());
//        return users;
//    }

    public static List<User> getActiveUsers(int myId) throws SQLException{
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            int dem = 0;
            while ((line = reader.readLine()) != null) {
                if(dem == 0){
                    dem++;
                    continue;
                }
                // Phân tích dữ liệu từ file và tạo đối tượng User
                String[] userData = line.split(",");
                if(Integer.parseInt(userData[7]) == 1 && Integer.parseInt(userData[0])!= myId){
                    User user = new User();
                    user.setUserId(Integer.parseInt(userData[0]));
                    user.setUserName(userData[1]);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}
