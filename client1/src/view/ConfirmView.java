package view;

import model.Request;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConfirmView extends JFrame implements ActionListener {

    private String question;
    private JButton yesButton;
    private JButton noButton;
    private Socket socket;
    private User frIvinting;
    private ObjectOutputStream objectOutputStream;

    public ConfirmView(ObjectOutputStream objectOutputStream, String question, User frInviting) {
        super(question);
        this.question = question;
        this.objectOutputStream = objectOutputStream;
        this.frIvinting = frInviting;

        // Tạo các nút Yes và No
        yesButton = new JButton("Yes");
        noButton = new JButton("No");

        // Đặt action command để phân biệt các sự kiện
        yesButton.setActionCommand("YES");
        noButton.setActionCommand("NO");

        // Đăng ký listener cho các nút
        yesButton.addActionListener(this);
        noButton.addActionListener(this);

        // Tạo JPanel để chứa các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        // Thêm panel vào frame
        this.add(buttonPanel);

        // Cấu hình frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 100);
        this.setLocationRelativeTo(null); // Đặt cửa sổ ở giữa màn hình
    }

//    public static void main(String[] args) {
//        ConfirmView confirmView = new ConfirmView("Are you sure you want to perform this action?");
//        confirmView.setVisible(true);
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Request request = new Request();
            List<Object> objectList = new ArrayList<>();
            request.setObjects(objectList);
            if ("YES".equals(e.getActionCommand())) {
                request.getObjects().add(frIvinting);
                request.getObjects().add(new String("YES"));
                request.getObjects().add(new String("CONFIRM"));
                objectOutputStream.writeObject(request);
                dispose(); // Đóng cửa sổ khi đã hoàn tất
            } else if ("NO".equals(e.getActionCommand())) {
                request.getObjects().add(frIvinting);
                request.getObjects().add(new String("NO"));
                request.getObjects().add(new String("CONFIRM"));

                objectOutputStream.writeObject(request);
                dispose(); // Đóng cửa sổ khi đã hoàn tất
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
