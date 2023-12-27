package view;

import model.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderBoard extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public LeaderBoard() {
        setTitle("Leaderboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Create the table model
        String[] columnNames = {"TOP", "ID", "Username", "Correct Answers", "Used Time", "Left Time"};
        model = new DefaultTableModel(null, columnNames);

        // Create the JTable
        table = new JTable(model);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");

        // Xử lý sự kiện khi nút "OK" được bấm
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Thực hiện các hành động khi nút "OK" được bấm
                dispose();  // Đóng JFrame
            }
        });
    }

    public void updateLeaderboard(List<Player> players) {
        // Clear current data
        model.setRowCount(0);

        // Sort players based on correct answers and time
        Collections.sort(players, Comparator.comparing((Player p) -> p.getCorrectAnswers())
                .reversed() // Sort in descending order for correct answers
                .thenComparing(p -> p.getUsedTime()));

        int rank = 0;
        // Add players to the table model
        for (Player player : players) {
            Object[] row = {++rank, player.getId(), player.getUsername(), player.getCorrectAnswers(), player.getUsedTime(), 60 - player.getUsedTime()};
            model.addRow(row);
        }

        // Repaint the table to reflect changes
        table.repaint();
    }
}
