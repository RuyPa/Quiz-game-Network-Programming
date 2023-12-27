import handle.ClientHandler;
import server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}

