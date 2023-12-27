package file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CreateFile {
    public static void main(String[] args) throws IOException {
        // Tạo một số thông tin fake user
        String[] usernames = {"john_doe", "alice_smith", "bob_jones"};
        String[] passwords = {"password123", "pass456", "secret789"};

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (int i = 0; i < usernames.length; i++) {
                // Ghi thông tin user vào file
                String userLine = (i + 1) + "," + usernames[i] + "," + passwords[i] + "\n";
                writer.write(userLine);
            }
            System.out.println("File created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
