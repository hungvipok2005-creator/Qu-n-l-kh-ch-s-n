package view;

import dao.NhanVienDAO;
import model.NhanVien;

import javax.swing.*;
import java.awt.*;

public class DangNhapFrame extends JFrame {

    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JButton btnThoat;

    public DangNhapFrame() {
        initUI();
        initEvents();
    }

    private void initUI() {
        setTitle("Đăng Nhập - Quản Lý Khách Sạn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        // Giá trị mặc định
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
    }

    private JComponent createHeader() {
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 100, 200));
        return lblTitle;
    }

    private JComponent createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tên đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Tên đăng nhập:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtTenDangNhap = new JTextField(15);
        panel.add(txtTenDangNhap, gbc);

        // Mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Mật khẩu:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtMatKhau = new JPasswordField(15);
        panel.add(txtMatKhau, gbc);

        return panel;
    }

    private JComponent createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnDangNhap = new JButton("Đăng Nhập");
        btnDangNhap.setForeground(Color.BLACK);
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 14));

        btnThoat = new JButton("Thoát");
        btnThoat.setForeground(Color.BLACK);

        panel.add(btnDangNhap);
        panel.add(btnThoat);

        return panel;
    }

    private void initEvents() {
        btnDangNhap.addActionListener(e -> dangNhap());
        btnThoat.addActionListener(e -> System.exit(0));
        txtMatKhau.addActionListener(e -> dangNhap()); // Enter để đăng nhập
    }

    private void dangNhap() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());

        if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ thông tin!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        NhanVienDAO dao = new NhanVienDAO();
        NhanVien nv = dao.login(tenDangNhap, matKhau);

        if (nv == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Tên đăng nhập hoặc mật khẩu không đúng!",
                    "Đăng nhập thất bại",
                    JOptionPane.ERROR_MESSAGE
            );
            txtMatKhau.setText("");
            txtTenDangNhap.requestFocus();
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Đăng nhập thành công!\nXin chào: " + nv.getTenNV(),
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();

    
            MainFrame mainFrame = new MainFrame(nv);
            mainFrame.setVisible(true);
        
    }
}
