/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.NhanVien;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private NhanVien nhanVienDangNhap;
    
    // Menu items cho menu Hệ thống
    private JMenuItem menuDangXuat, menuThoat;
    
    // Menu items cho các chức năng khác
    private JMenuItem menuDatPhong;
    private JMenuItem menuKhachHang, menuNhanVien, menuPhong, menuLoaiPhong, menuDichVu;
    private JMenuItem menuHoaDon;
    private JMenuItem menuGioiThieu;
    
    public MainFrame(NhanVien nv) {
        this.nhanVienDangNhap = nv;
        initComponents();
        setupEvents();
        setupPermissions(); // Phân quyền theo chức vụ
    }
    
    private void initComponents() {
        setTitle("QUẢN LÝ KHÁCH SẠN - " + nhanVienDangNhap.getTenNV() + " (" + nhanVienDangNhap.getChucVu() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        // Tạo menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // ========== MENU HỆ THỐNG ==========
        JMenu menuHeThong = new JMenu("Hệ Thống");
        menuDangXuat = new JMenuItem("Đăng Xuất");
        menuThoat = new JMenuItem("Thoát");
        
        menuHeThong.add(menuDangXuat);
        menuHeThong.addSeparator();
        menuHeThong.add(menuThoat);
        
        // ========== MENU ĐẶT PHÒNG ==========
        JMenu menuDatPhongMenu = new JMenu("Đặt Phòng");
        menuDatPhong = new JMenuItem("Quản lý đặt phòng");
        menuDatPhongMenu.add(menuDatPhong);
        
        // ========== MENU QUẢN LÝ ==========
        JMenu menuQuanLy = new JMenu("Quản Lý");
        
        menuKhachHang = new JMenuItem("Quản lý khách hàng");
        menuNhanVien = new JMenuItem("Quản lý nhân viên");
        menuPhong = new JMenuItem("Quản lý phòng");
        menuLoaiPhong = new JMenuItem("Quản lý loại phòng");
        menuDichVu = new JMenuItem("Quản lý dịch vụ");
        
        // Sắp xếp theo thứ tự yêu cầu
        menuQuanLy.add(menuKhachHang);
        menuQuanLy.add(menuNhanVien);
        menuQuanLy.add(menuPhong);
        menuQuanLy.add(menuLoaiPhong);
        menuQuanLy.add(menuDichVu);
        
        // ========== MENU HÓA ĐƠN ==========
        JMenu menuHoaDonMenu = new JMenu("Hóa Đơn");
        menuHoaDon = new JMenuItem("Quản lý hóa đơn");
        menuHoaDonMenu.add(menuHoaDon);
        
        // ========== MENU TRỢ GIÚP ==========
        JMenu menuTroGiup = new JMenu("Trợ Giúp");
        JMenuItem menuHuongDan = new JMenuItem("Hướng dẫn sử dụng");
        JMenuItem menuTroGiupItem = new JMenuItem("Trợ giúp trực tuyến");
        menuTroGiup.add(menuHuongDan);
        menuTroGiup.addSeparator();
        menuTroGiup.add(menuTroGiupItem);
        
        // ========== MENU GIỚI THIỆU ==========
        JMenu menuGioiThieuMenu = new JMenu("Giới Thiệu");
        menuGioiThieu = new JMenuItem("Thông tin ứng dụng");
        menuGioiThieuMenu.add(menuGioiThieu);
        
        // Thêm các menu vào menu bar theo thứ tự yêu cầu
        menuBar.add(menuHeThong);
        menuBar.add(menuDatPhongMenu);
        menuBar.add(menuQuanLy);
        menuBar.add(menuHoaDonMenu);
        menuBar.add(menuTroGiup);
        menuBar.add(menuGioiThieuMenu);
        
        setJMenuBar(menuBar);
        
        // Panel chính với thông tin đăng nhập
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        
        JLabel lblWelcome = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG QUẢN LÝ KHÁCH SẠN", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 28));
        lblWelcome.setForeground(new Color(0, 100, 200));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(100, 0, 50, 0));
        
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        infoPanel.setBackground(new Color(240, 248, 255));
        
        JLabel lblUser = new JLabel("Người dùng: " + nhanVienDangNhap.getTenNV(), SwingConstants.CENTER);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 18));
        lblUser.setForeground(new Color(60, 60, 60));
        
        JLabel lblChucVu = new JLabel("Chức vụ: " + nhanVienDangNhap.getChucVu(), SwingConstants.CENTER);
        lblChucVu.setFont(new Font("Arial", Font.PLAIN, 16));
        lblChucVu.setForeground(new Color(80, 80, 80));
        
        JLabel lblQuyen = new JLabel("Quyền truy cập: " + nhanVienDangNhap.getQuyen(), SwingConstants.CENTER);
        lblQuyen.setFont(new Font("Arial", Font.PLAIN, 16));
        lblQuyen.setForeground(new Color(80, 80, 80));
        
        infoPanel.add(lblUser);
        infoPanel.add(lblChucVu);
        infoPanel.add(lblQuyen);
        
        mainPanel.add(lblWelcome, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        // Thêm hình ảnh hoặc logo nếu có
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(new Color(240, 248, 255));
        // Giả sử bạn có một ảnh logo, nếu không bỏ phần này
        // ImageIcon logo = new ImageIcon("path/to/logo.png");
        // JLabel lblLogo = new JLabel(logo);
        // logoPanel.add(lblLogo);
        
        mainPanel.add(logoPanel, BorderLayout.NORTH);
        
        add(mainPanel);
    }
    
    private void setupEvents() {
        // Hệ thống
        menuDangXuat.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn đăng xuất?", 
                "Xác nhận đăng xuất", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new DangNhapFrame().setVisible(true);
            }
        });
        
        menuThoat.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn thoát chương trình?", 
                "Xác nhận thoát", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        // Đặt phòng
        menuDatPhong.addActionListener(e -> new DatPhongFrame().setVisible(true));
        
        // Quản lý
        menuKhachHang.addActionListener(e -> new KhachHangFrame().setVisible(true));
        menuNhanVien.addActionListener(e -> new NhanVienFrame().setVisible(true));
        menuPhong.addActionListener(e -> new PhongFrame().setVisible(true));
        menuLoaiPhong.addActionListener(e -> new LoaiPhongFrame().setVisible(true));
        menuDichVu.addActionListener(e -> new DichVuFrame().setVisible(true));
        
        // Hóa đơn
        menuHoaDon.addActionListener(e -> new HoaDonFrame().setVisible(true));
        
        // Giới thiệu
        menuGioiThieu.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "HỆ THỐNG QUẢN LÝ KHÁCH SẠN\n" +
                "Phiên bản: Trung béo   " +
                "Phát triển bởi: Team 7 \n" +
                "© 2025 Bản quyền thuộc về Công ty xyz",
                "Giới thiệu ứng dụng",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private void setupPermissions() {
        // Nếu không phải admin, ẩn menu quản lý nhân viên
        if (!"Admin".equalsIgnoreCase(nhanVienDangNhap.getQuyen())) {
            menuNhanVien.setEnabled(false);
        }
    }
}