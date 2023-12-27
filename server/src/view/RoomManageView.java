package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomManageView extends JFrame {
    RoomManageView view;
    public RoomManageView() {
        view= this;
        JFrame frame = new JFrame("Room Management App");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton manageRoomButton = new JButton("Manage Room");

        frame.setLayout(null);

        Dimension frameSize = frame.getSize();

        Dimension buttonSize = manageRoomButton.getPreferredSize();

        int x = (frameSize.width - buttonSize.width) / 2;
        int y = (frameSize.height - buttonSize.height) / 2;

        manageRoomButton.setBounds(x, y, buttonSize.width, buttonSize.height);


        manageRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();

                new ActiveRoom();
            }
        });

        frame.add(manageRoomButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        RoomManageView roomManageView = new RoomManageView();
    }
}
