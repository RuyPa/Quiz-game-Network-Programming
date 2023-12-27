package test;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Scanner;

public class Test extends JFrame {
    private JTextPane textPane;
    private int count;

    public Test() {
        super("Hiển thị số sau mỗi 2 giây");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textPane = new JTextPane();
        textPane.setEditable(false);
        add(new JScrollPane(textPane));

        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count++;
                appendText(String.valueOf(count) + "\n");
            }
        });
        timer.start();

        setSize(200, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void appendText(String text) {
        Document doc = textPane.getDocument();
        try {
            doc.insertString(doc.getLength(), text, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Test();
        });
    }
}
