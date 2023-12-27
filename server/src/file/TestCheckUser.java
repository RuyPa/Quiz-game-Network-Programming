package file;

import model.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestCheckUser {

    public static void main(String[] args) {
        // Thay đổi thông tin người dùng cần kiểm tra
        User userToCheck = new User();
        userToCheck.setUserName("duy");
        userToCheck.setPassWord("duy");

        if (isUserInList(userToCheck)){
            System.out.println("User found in the list.");
        } else {
            System.out.println("User not found in the list.");
        }
    }

    public static boolean isUserInList(User user) {
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
                if(user.getUserName().equals(userData[1]) & user.getPassWord().equals(userData[2])){
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
