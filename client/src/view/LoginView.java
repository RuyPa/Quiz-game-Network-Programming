package view;

import model.Request;
import model.User;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author ruy_pa_
 */
public class LoginView extends JFrame implements ActionListener{
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public Socket clientSocket;
    private LoginView loginView;

    public LoginView(Socket clientSocket)
    {
        super("Login");

        this.clientSocket = clientSocket;
        loginView = this;

        txtUsername= new JTextField(15);
        txtPassword= new JPasswordField(15);
        txtPassword.setEchoChar('*');
        btnLogin= new JButton("Login");

        JPanel pnMain= new JPanel();
        pnMain.setSize(this.getSize().width- 5, this.getSize().height- 20);
        pnMain.setLayout(new BoxLayout(pnMain, BoxLayout.PAGE_AXIS));
        pnMain.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel lblHome= new JLabel("Login");
        lblHome.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblHome.setFont(lblHome.getFont().deriveFont(20.0f));
        pnMain.add(lblHome);
        pnMain.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel pnUsername= new JPanel();
        pnUsername.setLayout(new FlowLayout());
        pnUsername.add(new JLabel("Username:"));
        pnUsername.add(txtUsername);
        pnMain.add(pnUsername);

        JPanel pnPassword= new JPanel();
        pnPassword.setLayout(new FlowLayout());
        pnPassword.add(new JLabel("Password:"));
        pnPassword.add(txtPassword);
        pnMain.add(pnPassword);

        pnMain.add(btnLogin);
        pnMain.add(Box.createRigidArea(new Dimension(0, 10)));
        btnLogin.addActionListener(this);

        this.setSize(400, 200);
        this.setLocation(600,300);
        this.setContentPane(pnMain);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if((e.getSource() instanceof JButton) && (((JButton)e.getSource()).equals(btnLogin)))
        {
            System.out.println(clientSocket.getInetAddress());
            Request request= new Request();
            User user= new User();
            user.setUserName(txtUsername.getText());
            user.setPassWord(txtPassword.getText());

            List<Object> objects = new ArrayList<>();
            objects.add(user);

            request.setObjects(objects);
            request.getObjects().add(new String("LOGIN"));

            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutputStream.writeObject(request);

                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                Object result = objectInputStream.readObject();

                if(result instanceof Boolean ){
                    System.out.println("sai roif :>>");
                }
                if(result instanceof List<?>) {
                    System.out.println("dung roi anh oi");
                    UserListView userListView = new UserListView(objectInputStream, objectOutputStream, clientSocket, (List<User>) result, user);
                    loginView.dispose();
                    userListView.setVisible(true);
                }


            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

}