package ranking;



import ranking.model.User;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Rank extends JFrame {
    public Rank() {

        // Tạo JFrame
        this.setTitle("Ranking");
        // Tạo JLabel phía trên
        JLabel label = new JLabel("Bảng xếp hạng người chơi", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        // Tạo DefaultTableModel với các cột
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("STT");
        model.addColumn("Tên người chơi");
        model.addColumn("Số câu làm đúng đúng");
        model.addColumn("Điểm");
        model.addColumn("Thời gian sử dụng");
        model.addColumn("Thời gian còn lại");

        List<User> listUser = new ArrayList();
        listUser.add(new User("Duy Ba", 1, 1 , 100,100));
        listUser.add(new User("Phuc Chien", 2, 2 , 200,200));
        listUser.add(new User("Hung Lam", 3, 3 , 300,100));
        listUser.add(new User("Uyen Chi", 4, 4 , 400,300));
        listUser.add(new User("Huyen Nhi", 5, 5 , 500,400));
        listUser.add(new User("Duc Lam", 5, 5, 600,100));
        listUser.add(new User("Nguyen Van Thanh", 7, 700 , 300,100));
        listUser.add(new User("Duc Lam", 8, 800 , 300,500));
        listUser.add(new User("Giang Nam", 9, 900 , 300,100));


        Collections.sort(listUser);

        for(int i = 0 ;i< listUser.size();i++) {
            model.addRow(new Object[]{
                    i + 1,
                    listUser.get(i).getUserName(),
                    listUser.get(i).getTotalQuestion(),
                    listUser.get(i).getScore(),
                    listUser.get(i).getTimeUsed(),
                    listUser.get(i).getTimeRemaining()
            });
        }

        // Thêm dữ liệu mẫu


        // Tạo JTable với DefaultTableModel
        JTable table = new JTable(model);

        // Tạo JScrollPane để hỗ trợ cuộn khi bảng quá lớn
        JScrollPane scrollPane = new JScrollPane(table);

        // Chỉnh lề của JScrollPane
        scrollPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 0, 50));

        // Tạo JPanel để chứa JLabel và JScrollPane
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Thêm JPanel vào JFrame
        this.getContentPane().add(panel);

        // Thêm ListSelectionListener để lắng nghe sự kiện khi dòng được chọn
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Đảm bảo rằng sự kiện không phải là sự kiện thay đổi nội dung bảng
                if (!e.getValueIsAdjusting()) {
                    // Lấy chỉ mục hàng được chọn
                    int selectedRow = table.getSelectedRow();

                    // Kiểm tra xem một dòng đã được chọn
                    if (selectedRow != -1) {
                        // Lấy giá trị từ cột "STT" của dòng được chọn
                        Object selectedSTT = table.getValueAt(selectedRow, 0);

                        // Chuyển sang trang mới và truyền STT vào trang đó
                        openNewPage(selectedSTT.toString());
                    }
                }
            }
        });

//        // Tạo Timer để giảm giá trị "Thời gian còn lại" mỗi giây
//        timer = new Timer(1000, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Giảm giá trị của cột "Thời gian còn lại" mỗi giây
//                for (int row = 0; row < model.getRowCount(); row++) {
//                    String timeString = (String) model.getValueAt(row, 3);
//                    int timeRemaining = Integer.parseInt(timeString.split(" ")[0]);
//                    if (timeRemaining > 0) {
//                        timeRemaining--;
//                        model.setValueAt(timeRemaining + " phút", row, 3);
//                    } else {
//                        // Xử lý khi thời gian còn lại hết
//                        model.setValueAt("Hết giờ", row, 3);
//                    }
//                }
//            }
//        });
//

        // Bắt đầu Time
//        timer.start();

        // Cài đặt kích thước JFrame
        this.setSize(640, 480);

        // Thiết lập JFrame hiển thị ở giữa màn hình
        this.setLocationRelativeTo(null);

        // Thiết lập chức năng đóng khi bấm nút đóng cửa sổ
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Hiển thị JFrame
        this.setVisible(true);
    }

    // Phương thức mẫu để mô phỏng việc mở trang mới
    private void openNewPage(String selectedSTT) {
        // In thông báo ra console
        System.out.println("Selected STT: " + selectedSTT);

        // Ở đây bạn có thể thực hiện các hành động khác, chẳng hạn như mở một JFrame/trang mới
    }

    public static void main(String[] args) {
        new Rank();
    }

}
