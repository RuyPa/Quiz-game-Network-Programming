/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;


import model.Game;
import model.Player;
import model.Request;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author ruy_pa_
 */
public class UserListView extends JFrame implements ActionListener{
    private Socket socket;
    private JTable tblResult;
    private JButton btnOK;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Thread listenerThread;

    private UserListView mainFrm;

    //    public UsingFrm()
    public UserListView(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, Socket clientSocket, List<User> userList, User user) throws IOException, ClassNotFoundException {

        this.socket = clientSocket;
//        objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
//        objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
        mainFrm= this;
        initilizeComponent(userList, user);
        startClientListener();
    }

    private void initilizeComponent(List<User> userList, User user){
        btnOK= new JButton("OK");

        JPanel pnMain= new JPanel();
        pnMain.setSize(this.getSize().width-5, this.getSize().height-20);
        pnMain.setLayout(new BoxLayout(pnMain,BoxLayout.PAGE_AXIS));
        pnMain.add(Box.createRigidArea(new Dimension(0,10)));

        btnOK.addActionListener(this);

        JPanel pntbl= new JPanel();
        pntbl.setLayout(new BoxLayout(pntbl, BoxLayout.LINE_AXIS));
        tblResult= new JTable();
        JScrollPane sp= new JScrollPane(tblResult);

        btnOK= new JButton("OK");
        btnOK.setAlignmentX(CENTER_ALIGNMENT);
        btnOK.addActionListener(this);
        pnMain.add(btnOK);

        pnMain.add(sp);
        pnMain.add(pntbl);
        pnMain.add(btnOK);

        String[] columns= {"userId", "username"};
        String[][] value= new String[userList.size()][2];
        for(int i= 0; i< userList.size(); i++ ){
            if(userList.get(i).getUserId() == user.getUserId()){
                continue;
            }
            value[i][0]= String.valueOf(userList.get(i).getUserId());
            value[i][1]= String.valueOf(userList.get(i).getUserName());
        }
        DefaultTableModel tableModel= new DefaultTableModel(value, columns) {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        tblResult.setModel(tableModel);

        this.add(pnMain);
        this.setSize(800,600);
        this.setLocation(500,150);
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        tblResult.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int column= tblResult.getColumnModel().getColumnIndexAtX(e.getX());
                int row= e.getY()/ tblResult.getRowHeight();

                if(row< tblResult.getRowCount()&& row>= 0 && column< tblResult.getColumnCount()&& column>=0) {
                    Integer invitedId = userList.get(row).getUserId();
                    sendInvite(invitedId);
                }
            }
        });
    }

    private void sendInvite(Integer invitedId) {
        System.out.println("bam vao dong nay");
        System.out.println(invitedId);

        Request request = new Request();
        List<Object> objectList = new ArrayList<>();
        objectList.add(invitedId);
        objectList.add(new String("INVITE"));
        request.setObjects(objectList);

        try {
            objectOutputStream.writeObject(request);

//            Game game = (Game) objectInputStream.readObject();
//            GameView gameView = new GameView(socket, game);
//            mainFrm.dispose();
//            gameView.setVisible(true);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void startClientListener() {
        listenerThread = new Thread(new ClientListener());
        listenerThread.start();
    }

    private class ClientListener implements Runnable{
        @Override
        public void run() {
            LeaderBoard leaderBoard = new LeaderBoard();
            while (true){
                try {

                    Object tesst = objectInputStream.readObject();
                    System.out.println("respone" + tesst);
                    Request responeFrServer = (Request) tesst;
                    String action = (String) responeFrServer.getObjects().get(responeFrServer.getObjects().size()-1);
                    System.out.println("Action: " + action);
                    switch (action){
                        case "INVITED":
                            break;
                        case "GAME":
                            Game game = (Game)responeFrServer.getObjects().get(0);
                            leaderBoard.setVisible(true);
                            GameView gameView = new GameView(objectInputStream, objectOutputStream, socket, game);
                            gameView.setVisible(true);
//                            mainFrm.dispose();
                            break;
                        case "INVITE":
                            User frInviting = (User) responeFrServer.getObjects().get(0);
                            String question = "Do you want to play with " + frInviting.getUserName() + " ?";
                            ConfirmView confirmView = new ConfirmView(objectOutputStream,question, frInviting);
                            confirmView.setVisible(true);
                            break;
                        case "RANK":
                            List<Player> players = (List<Player>) responeFrServer.getObjects().get(0);
                            RankView rankView = new RankView(players);
                            rankView.setVisible(true);
                            break;
                        case "OK":
                            break;
                        case "LEADERBOARD":
                            List<Player> playerss = (List<Player>) responeFrServer.getObjects().get(0);
                            leaderBoard.updateLeaderboard(playerss);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if((e.getSource() instanceof JButton) && (((JButton)e.getSource()).equals(btnOK))) {
            mainFrm.dispose();
        }
    }
}