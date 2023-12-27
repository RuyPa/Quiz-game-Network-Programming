package server;

import handle.ClientHandler;
import model.Room;
import view.RoomManageView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    public static List<ClientHandler> connectionList;
    public static List<Room> roomList;

    @Override
    public void run() {
        try {
            this.roomList = new ArrayList<>();
            this.connectionList = new ArrayList<>();
//            server start
            ServerSocket serverSocket = new ServerSocket(333);
            System.out.println("Server is running...");

            RoomManageView roomManageView = new RoomManageView();

//            loop when a client connect successfully
            while (true) {
//                xu li thang client, cho no vao mot luong de thuc hien bat dong bo
                Socket clientSocket = serverSocket.accept();
                System.out.println("server accepts");
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                connectionList.add(clientHandler);
                System.out.println("Number of current Client: " + String.valueOf(connectionList.size()));
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

