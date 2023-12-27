package handle;

import consts.GameConstant;
import controller.UserController;
import model.*;
import repository.UserRepository;
import server.Server;
import service.UserService;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Integer id;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;


    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setObjectInputStream(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    public void setClientSocket(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public Integer getId() {
        return id;
    }

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            User user = new User();
            System.out.println("New client connected: " + clientSocket.getInetAddress());
            this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            while (true) {

                try{
                    List<ClientHandler> connectionList = Server.connectionList;
                    List<Room> roomList = Server.roomList;
                    System.out.println("Read at client : " + String.valueOf(user.getUserName()));

                    Request request = (Request) objectInputStream.readObject();
                    String action = (String) request.getObjects().get(request.getObjects().size()-1);
                    System.out.println("Action: " + action);

                    Request responeForClient = new Request();
                    List<Object> objectList = new ArrayList<>();
                    responeForClient.setObjects(objectList);

                    switch (action){
                        case "LOGIN":
                            user = (User) request.getObjects().get(0);
                            UserController.login(objectOutputStream, user);
                            user = UserController.userService.getUserByUsername(user.getUserName());
                            System.out.println("userid: " + user.getUserId());
                            connectionList.get(connectionList.size() - 1).setId(user.getUserId());
                            break;
                        case "INVITE":
//                            TODO: chua xu li phan client so 2
                            Request responeForMyself = new Request();
                            List<Object> obj = new ArrayList<>();
                            obj.add(new String("INVITED"));
                            responeForMyself.setObjects(obj);
                            objectOutputStream.writeObject(responeForMyself);


                            Integer invitedId = (Integer) request.getObjects().get(0);
                            for(ClientHandler clientHandler : connectionList){
                                if(clientHandler.getId().equals(invitedId)){
                                    Request requestForFr = new Request();
                                    List<Object> objects = new ArrayList<>();
                                    objects.add(user);
                                    requestForFr.setObjects(objects);
                                    objects.add(new String("INVITE"));
                                    clientHandler.getObjectOutputStream().writeObject(requestForFr);
                                    break;
                                }
                            }
                            break;
                        case "CONFIRM":

                            List<Player> playersz = new ArrayList<>();
                            for(Room room : roomList) {
//
                                if (room.getUserId().contains((Integer) user.getUserId())) {


                                    for (Integer id : room.getUserId()) {
                                        Player player1 = new Player();
                                        player1.setId(id);
                                        player1.setUsername(UserRepository.getUserById(id).getUserName());
                                        player1.setCorrectAnswers(Integer.parseInt(room.getCurrentScore().get(id).get(0)));
                                        player1.setUsedTime(Integer.parseInt(room.getCurrentScore().get(id).get(1)));
                                        playersz.add(player1);
                                    }
                                }
                            }
                            String confirm = (String)request.getObjects().get(1);
                            if (confirm.equals("YES")){
                                Game game = GameConstant.game1;
                                responeForClient.getObjects().add(game);
                                responeForClient.getObjects().add(playersz);
                                responeForClient.getObjects().add(new String("GAME"));
                                User frInviting = (User) request.getObjects().get(0);
                                for(ClientHandler clientHandler : connectionList){
                                    if(clientHandler.getId().equals(frInviting.getUserId())){
                                        Room room = new Room(frInviting.getUserId(), user.getUserId());
                                        roomList.add(room);
                                        this.objectOutputStream.writeObject(responeForClient);
                                        clientHandler.getObjectOutputStream().writeObject(responeForClient);
                                        break;
                                    }
                                }
                            }
                            break;
                        case "ANSWER":

                            Request abc = new Request();
                            List<Object> objj = new ArrayList<>();
                            abc.setObjects(objj);
//                            objectOutputStream.writeObject(abc);

                            int totaltimeused = (Integer) request.getObjects().get(1);

                            int score = 0;
                            List<Question> questionList = (List<Question>) request.getObjects().get(0);
                            for(Question question : questionList){
                                for(Question constQuestion : GameConstant.game1.getQuestionList()){
                                    if(question.getName().equals(constQuestion.getName())){
                                        if(question.getCorrectAnswer() == null || question.getCorrectAnswer().isEmpty()){
                                            continue;
                                        }
                                        if(question.getCorrectAnswer().equals(constQuestion.getCorrectAnswer())){
                                            score++;
                                        }
                                    }
                                }
                            }


                            Player player = new Player(user.getUserId(), user.getUserName(), score, totaltimeused);

                            for(Room room : roomList){
//                                đúng phòng thì check điểm và add
                                if(room.getUserId().contains((Integer) user.getUserId())){
                                    room.getPlayers().add(player);


//                                    nếu full điểm thì gửi cho all players
                                    if(room.getPlayers().size()== room.getUserId().size()){


                                        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("result.txt", true)))) {
                                            // Append information to the existing file

                                            LocalDateTime localDateTime = LocalDateTime.now();
                                            writer.println(localDateTime.toString());
                                            writer.println( "Room Id: " + room.getId());
                                            for (Player p : room.getPlayers()) {
                                                writer.println("Id: " + p.getId());
                                                writer.println("Player: " + p.getUsername());
                                                writer.println("Correct Answers: " + p.getCorrectAnswers());
                                                writer.println("Used Time: " + p.getUsedTime() + " seconds");
                                                writer.println("-----");
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace(); // Handle the exception according to your needs
                                        }

                                        abc.getObjects().add(room.getPlayers());
                                        abc.getObjects().add(new String("RANK"));

                                        for(ClientHandler clientHandler : connectionList){
                                            for(Integer id : room.getUserId()) {
                                                if (clientHandler.getId().equals(id)){
                                                    System.out.println("gui ket qua");
                                                    clientHandler.getObjectOutputStream().writeObject(abc);
                                                }
                                            }
                                        }
                                        roomList.remove(room);
                                    }
                                }
                            }


//                            System.out.println(score);
//                            for(Room room : roomList){
//                                if(room.getUserId().contains((Integer) user.getUserId())){
//                                    room.getUserScore().put(user.getUserId(), score);
//
//                                    for(Integer anotherId : room.getUserId()){
//                                        if(anotherId != user.getUserId()){
//                                            if(room.getUserScore().containsKey(anotherId)){
//                                                if(score < room.getUserScore().get(anotherId)){
//                                                    System.out.println("thua");
//                                                } else if(score > room.getUserScore().get(anotherId)){
//                                                    System.out.println("thang");
//                                                } else {
//                                                    System.out.println("hoa");
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
                            break;
                        case "EACH":
                            String nameQt = (String)request.getObjects().get(0);
                            String answer = (String)request.getObjects().get(1);
                            int time = (Integer) request.getObjects().get(2);

                            Request abcd = new Request();
                            List<Object> objjj = new ArrayList<>();
                            abcd.setObjects(objjj);

                            for(Room room : roomList) {
//                                đúng phòng thì check điểm và add
                                if (room.getUserId().contains((Integer) user.getUserId())) {
                                    for(Question question : room.getGame().getQuestionList()){
                                        if (question.getName().equals(nameQt)){
                                            if(answer.equals(question.getCorrectAnswer())){
                                                int currentCorrect = Integer.parseInt(room.getCurrentScore().get(user.getUserId()).get(0));
                                                int currentTime = Integer.parseInt(room.getCurrentScore().get(user.getUserId()).get(1));
                                                List<String> strings = new ArrayList<>();
                                                strings.add(String.valueOf(currentCorrect + 1));
                                                strings.add(String.valueOf(currentTime + time));
                                                room.getCurrentScore().put(user.getUserId(), strings);
                                            }
                                            break;
                                        }
                                    }

                                    List<Player> players = new ArrayList<>();
                                    for (Integer id : room.getUserId()){
                                        Player player1 = new Player();
                                        player1.setId(id);
                                        player1.setUsername(UserRepository.getUserById(id).getUserName());
                                        player1.setCorrectAnswers(Integer.parseInt(room.getCurrentScore().get(id).get(0)));
                                        player1.setUsedTime(Integer.parseInt(room.getCurrentScore().get(id).get(1)));
                                        players.add(player1);
                                    }



                                    abcd.getObjects().add(players);
                                    abcd.getObjects().add(new String("LEADERBOARD"));

                                    for(ClientHandler clientHandler : connectionList){
                                        for(int userid : room.getUserId()){

                                            if(userid == clientHandler.getId() && userid != this.id){
                                                System.out.println("dyyuuyuyuyuy");
                                                clientHandler.getObjectOutputStream().writeObject(abcd);
                                            }
                                        }
                                    }
                                    break;
                                }
                            }



                            objectOutputStream.writeObject(abcd);
                            break;
                    }
                    objectOutputStream.flush();
                } catch (EOFException | SQLException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
