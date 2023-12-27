package view;

import model.Room;
import server.Server;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
public class ActiveRoom extends JFrame{
    private Timer timer;
    List<Room> roomList = Server.roomList;

    public ActiveRoom() {
        // Tạo JFrame
        this.setTitle("Room");
        // Tạo JLabel phía trên
        JLabel label = new JLabel("Danh Sách Phòng Chơi", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));





//        List<Room> roomList = Server.roomList;

//        List<Room> roomList = new ArrayList<>();
//        roomList.add(new Room(1,2));

        // Tạo DefaultTableModel với các cột
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("STT");
        model.addColumn("Phòng");
        model.addColumn("Số người chơi");
        model.addColumn("Thời gian còn lại");

        System.out.println(roomList.size());

        int stt = 0;
        for(Room room : roomList){
            if(checkTime(room.getLocalDateTime())<= 60){
                System.out.println(checkTime(room.getLocalDateTime()));
                model.addRow(new Object[]{String.valueOf(stt++), room.getId(), String.valueOf(room.getUserId().size()), String.valueOf(60 + checkTime(room.getLocalDateTime()))});
            }

        }

        // Thêm dữ liệu mẫu
//        model.addRow(new Object[]{"1", "A101", "3", "300 giây"});
//        model.addRow(new Object[]{"2", "B204", "4", "450 giây"});
//        model.addRow(new Object[]{"3", "C302", "2", "600 giây"});
//        model.addRow(new Object[]{"4", "D105", "5", "400 giây"});
//        model.addRow(new Object[]{"5", "E201", "3", "550 giây"});
//        model.addRow(new Object[]{"6", "F301", "4", "500 giây"});
//        model.addRow(new Object[]{"7", "G102", "2", "350 giây"});
//        model.addRow(new Object[]{"8", "H205", "6", "750 giây"});
//        model.addRow(new Object[]{"9", "I303", "3", "250 giây"});
//        model.addRow(new Object[]{"10", "J401", "5", "500 giây"});


        JTable table = new JTable(model);


        JScrollPane scrollPane = new JScrollPane(table);


        scrollPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 0, 50));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);


        this.getContentPane().add(panel);


        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting()) {

                    int selectedRow = table.getSelectedRow();

                    if (selectedRow != -1) {
                        // Lấy giá trị từ cột "STT" của dòng được chọn
                        Object selectedSTT = table.getValueAt(selectedRow, 1);


                        try {
                            openNewPage(selectedSTT.toString());
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });

        // Tạo Timer để giảm giá trị "Thời gian còn lại" mỗi giây
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Giảm giá trị của cột "Thời gian còn lại" mỗi giây
                for (int row = 0; row < model.getRowCount(); row++) {
                    String timeString = (String) model.getValueAt(row, 3);
                    int timeRemaining = Integer.parseInt(timeString.split(" ")[0]);
                    if (timeRemaining > 0) {
                        timeRemaining--;
                        model.setValueAt(timeRemaining + " giây", row, 3);
                    } else {
                        // Xử lý khi thời gian còn lại hết
                        model.setValueAt("Hết giờ", row, 3);
                    }
                }
            }
        });

        JButton okButton = new JButton("OK");

        // Xử lý sự kiện khi nút "OK" được bấm
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Thực hiện các hành động khi nút "OK" được bấm
                dispose();  // Đóng JFrame
            }
        });

        // Tạo JPanel mới để chứa nút "OK"
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        // Thêm nút "OK" vào cuối cùng của BorderLayout.CENTER
        panel.add(buttonPanel, BorderLayout.SOUTH);


        // Bắt đầu Time
        timer.start();

        // Cài đặt kích thước JFrame
        this.setSize(640, 480);

        // Thiết lập JFrame hiển thị ở giữa màn hình
        this.setLocationRelativeTo(null);

        // Thiết lập chức năng đóng khi bấm nút đóng cửa sổ
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Hiển thị JFrame
        this.setVisible(true);
    }

    private long checkTime(LocalDateTime localDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        System.out.println(currentDateTime.toString());
        System.out.println(localDateTime.toString());

        // Tính độ chênh lệch giữa thời gian hiện tại và targetDateTime bằng giây
        long secondsDifference = Duration.between(currentDateTime, localDateTime).getSeconds();

        // Kiểm tra điều kiện (ví dụ: độ chênh lệch dưới 3600 giây - 1 giờ)
        return secondsDifference;
    }

    // Phương thức mẫu để mô phỏng việc mở trang mới
    private void openNewPage(String selectedSTT) throws SQLException {
        // In thông báo ra console
        for(Room room : roomList){
            if(room.getId().equals(selectedSTT)){
                RankView rankView = new RankView(room);
                rankView.setVisible(true);
                dispose();
                break;
            }
        }

        // Ở đây bạn có thể thực hiện các hành động khác, chẳng hạn như mở một JFrame/trang mới
    }

//    public static void main(String[] args) {
//        new RoomManage();
//    }
}