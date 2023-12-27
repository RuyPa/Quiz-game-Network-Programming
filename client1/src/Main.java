import view.LoginView;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        LoginView loginView = new LoginView(new Socket("127.0.0.1", 333));
        loginView.setVisible(true);
    }
}
