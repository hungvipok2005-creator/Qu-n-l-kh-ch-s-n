/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import dao.HoaDonDAO;
import dao.DatPhongDAO;
import dao.ChiTietDichVuDAO;
import dao.NhanVienDAO;
import model.HoaDon;
import model.DatPhong;
import model.ChiTietDichVu;
import model.NhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class HoaDonFrame extends JFrame {
    private HoaDonDAO hoaDonDAO;
    private DatPhongDAO datPhongDAO;
    private ChiTietDichVuDAO chiTietDichVuDAO;
    private NhanVienDAO nhanVienDAO;
    private DefaultTableModel tableModel;
    private JTable tblHoaDon;
    private JTextField txtTimKiem, txtMaHD, txtMaDatPhong, txtMaNV;
    private JFormattedTextField txtNgayLap;
    private JTextField txtTienPhong, txtTienDichVu, txtTienCoc, txtGiamGia, txtThanhTien;
    private JComboBox<String> cboPhuongThucTT, cboTrangThai;
    private JLabel lblTongThu, lblTongHD, lblTongChuaThanhToan;

    public HoaDonFrame() {
        hoaDonDAO = new HoaDonDAO();
        datPhongDAO = new DatPhongDAO();
        chiTietDichVuDAO = new ChiTietDichVuDAO();
        nhanVienDAO = new NhanVienDAO();
        initComponents();
        loadData();
        loadComboboxData();
        lamMoiForm();
    }

    private void initComponents() {
        setTitle("QUẢN LÝ HÓA ĐƠN");
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel với BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === HEADER ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ HÓA ĐƠN", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        // Panel thống kê nhanh
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        statsPanel.setBorder(BorderFactory.createEtchedBorder());

        lblTongHD = new JLabel("Tổng hóa đơn: 0");
        lblTongHD.setFont(new Font("Arial", Font.BOLD, 12));
        lblTongHD.setForeground(new Color(0, 123, 255));

        lblTongThu = new JLabel("Tổng thu: 0 VNĐ");
        lblTongThu.setFont(new Font("Arial", Font.BOLD, 12));
        lblTongThu.setForeground(new Color(40, 167, 69));

        lblTongChuaThanhToan = new JLabel("Chưa thanh toán: 0");
        lblTongChuaThanhToan.setFont(new Font("Arial", Font.BOLD, 12));
        lblTongChuaThanhToan.setForeground(new Color(220, 53, 69));

        statsPanel.add(lblTongHD);
        statsPanel.add(lblTongThu);
        statsPanel.add(lblTongChuaThanhToan);

        headerPanel.add(statsPanel, BorderLayout.SOUTH);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // === CENTER: Bảng dữ liệu ===
        String[] columns = {"Mã HD", "Mã Đặt Phòng", "Ngày lập", "Tiền phòng", "Tiền DV", "Tiền cọc",
                "Giảm giá", "Thành tiền", "PTTT", "Trạng thái", "Mã NV"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 3 && columnIndex <= 7) return Double.class;
                return String.class;
            }
        };

        tblHoaDon = new JTable(tableModel);
        tblHoaDon.setRowHeight(30);
        tblHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHoaDon.setFont(new Font("Arial", Font.PLAIN, 11));

        // Set column widths
        int[] columnWidths = {80, 100, 120, 90, 90, 90, 90, 100, 80, 100, 80};
        for (int i = 0; i < columnWidths.length; i++) {
            tblHoaDon.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Renderer cho cột tiền
        for (int i = 3; i <= 7; i++) {
            tblHoaDon.getColumnModel().getColumn(i).setCellRenderer(new CurrencyRenderer());
        }

        // Renderer cho cột trạng thái
        tblHoaDon.getColumnModel().getColumn(9).setCellRenderer(new StatusRenderer());

        JScrollPane scrollPane = new JScrollPane(tblHoaDon);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách hóa đơn"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // === LEFT: Form nhập liệu ===
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        leftPanel.setPreferredSize(new Dimension(400, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Mã hóa đơn
        gbc.gridx = 0;
        gbc.gridy = row;
        leftPanel.add(new JLabel("Mã HD:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtMaHD = new JTextField(15);
        txtMaHD.setEditable(false);
        txtMaHD.setBackground(new Color(245, 245, 245));
        leftPanel.add(txtMaHD, gbc);

        row++;
        // Mã đặt phòng
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        leftPanel.add(new JLabel("Mã đặt phòng:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtMaDatPhong = new JTextField(15);
        leftPanel.add(txtMaDatPhong, gbc);
        gbc.gridx = 3;
        JButton btnChonDatPhong = new JButton("...");
        btnChonDatPhong.setToolTipText("Chọn đặt phòng");
        leftPanel.add(btnChonDatPhong, gbc);

        row++;
        // Ngày lập
        gbc.gridx = 0;
        gbc.gridy = row;
        leftPanel.add(new JLabel("Ngày lập:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtNgayLap = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy HH:mm"));
        txtNgayLap.setValue(new Date());
        leftPanel.add(txtNgayLap, gbc);

        row++;
        // Tiền phòng
        gbc.gridx = 0;
        gbc.gridy = row;
        leftPanel.add(new JLabel("Tiền phòng:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtTienPhong = new JTextField(10);
        txtTienPhong.setText("0");
        leftPanel.add(txtTienPhong, gbc);
        gbc.gridx = 3;
        leftPanel.add(new JLabel("VNĐ"), gbc);

        row++;
        // Tiền dịch vụ
        gbc.gridx = 0;
        gbc.gridy = row;
        leftPanel.add(new JLabel("Tiền dịch vụ:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtTienDichVu = new JTextField(10);
        txtTienDichVu.setText("0");
        txtTienDichVu.setEditable(false);
        txtTienDichVu.setBackground(new Color(245, 245, 245));
        leftPanel.add(txtTienDichVu, gbc);
        gbc.gridx = 3;
        leftPanel.add(new JLabel("VNĐ"), gbc);

        row++;
        // Tiền cọc
        gbc.gridx = 0;
        gbc.gridy = row;
        leftPanel.add(new JLabel("Tiền cọc:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtTienCoc = new JTextField(10);
        txtTienCoc.setText("0");
        leftPanel.add(txtTienCoc, gbc);
        gbc.gridx = 3;
        leftPanel.add(new JLabel("VNĐ"), gbc);

        row++;
        // Giảm giá
        gbc.gridx = 0;
        gbc.gridy = row;
        leftPanel.add(new JLabel("Giảm giá:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtGiamGia = new JTextField(10);
        txtGiamGia.setText("0");
        leftPanel.add(txtGiamGia, gbc);
        gbc.gridx = 3;
        leftPanel.add(new JLabel("VNĐ"), gbc);

        row++;
        // Thành tiền
        gbc.gridx = 0;
        gbc.gridy = row;
        leftPanel.add(new JLabel("Thành tiền:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtThanhTien = new JTextField(10);
        txtThanhTien.setText("0");
        txtThanhTien.setEditable(false);
        txtThanhTien.setBackground(new Color(245, 245, 245));
        leftPanel.add(txtThanhTien, gbc);
        gbc.gridx = 3;
        leftPanel.add(new JLabel("VNĐ"), gbc);

        row++;
        // Phương thức thanh toán
        gbc.gridx = 0;
        gbc.gridy = row;
        leftPanel.add(new JLabel("Phương thức TT:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        cboPhuongThucTT = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản", "Thẻ", "VNPay", "MoMo"});
        leftPanel.add(cboPhuongThucTT, gbc);

        row++;
        // Trạng thái
        gbc.gridx = 0;
        gbc.gridy = row;
        leftPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        cboTrangThai = new JComboBox<>(new String[]{"Chưa thanh toán", "Đã thanh toán", "Hủy"});
        leftPanel.add(cboTrangThai, gbc);

        row++;
        // Mã nhân viên
        gbc.gridx = 0;
        gbc.gridy = row;
        leftPanel.add(new JLabel("Mã NV:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtMaNV = new JTextField(10);
        txtMaNV.setText("NV001");
        leftPanel.add(txtMaNV, gbc);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // === RIGHT: Tìm kiếm và chức năng ===
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 0));

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new GridLayout(7, 1, 5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        searchPanel.add(lblSearch);

        txtTimKiem = new JTextField();
        txtTimKiem.setToolTipText("Nhập mã HD, mã đặt phòng, hoặc trạng thái...");
        searchPanel.add(txtTimKiem);

        JButton btnTimKiem = new JButton("Tìm kiếm");
        searchPanel.add(btnTimKiem);

        JButton btnTaiLai = new JButton("Tải lại");
        searchPanel.add(btnTaiLai);

        // Thêm khoảng cách
        searchPanel.add(new JLabel());

        // Button tính thành tiền
        JButton btnTinhTien = new JButton("Tính thành tiền");
        searchPanel.add(btnTinhTien);

        rightPanel.add(searchPanel, BorderLayout.NORTH);

        // Panel chức năng
        JPanel functionPanel = new JPanel(new GridLayout(8, 1, 5, 5));
        functionPanel.setBorder(BorderFactory.createTitledBorder("Chức năng"));

        JButton btnThem = new JButton("Tạo mới");
        JButton btnSua = new JButton("Cập nhật");
        JButton btnXoa = new JButton("Xóa");
        JButton btnXemCT = new JButton("Xem chi tiết");
        JButton btnInHD = new JButton("In hóa đơn");
        JButton btnXuatExcel = new JButton("Xuất Excel");
        JButton btnLamMoi = new JButton("Làm mới");
        JButton btnDong = new JButton("Đóng");

        functionPanel.add(btnThem);
        functionPanel.add(btnSua);
        functionPanel.add(btnXoa);
        functionPanel.add(btnXemCT);
        functionPanel.add(btnInHD);
        functionPanel.add(btnXuatExcel);
        functionPanel.add(btnLamMoi);
        functionPanel.add(btnDong);

        rightPanel.add(functionPanel, BorderLayout.CENTER);

        mainPanel.add(rightPanel, BorderLayout.EAST);

        add(mainPanel);

        // === SỰ KIỆN ===
        // Tải lại dữ liệu
        btnTaiLai.addActionListener(e -> {
            loadData();
            JOptionPane.showMessageDialog(this, "Đã tải lại dữ liệu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });

        // Tìm kiếm
        btnTimKiem.addActionListener(e -> {
            String keyword = txtTimKiem.getText().trim();
            if (!keyword.isEmpty()) {
                timKiem(keyword);
            } else {
                loadData();
            }
        });

        // Tìm kiếm real-time
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String keyword = txtTimKiem.getText().trim();
                if (keyword.isEmpty()) {
                    loadData();
                } else {
                    timKiem(keyword);
                }
            }
        });

        // Tạo mới
        btnThem.addActionListener(e -> themHoaDon());

        // Cập nhật
        btnSua.addActionListener(e -> suaHoaDon());

        // Xóa
        btnXoa.addActionListener(e -> xoaHoaDon());

        // Xem chi tiết
        btnXemCT.addActionListener(e -> xemChiTiet());

        // In hóa đơn
        btnInHD.addActionListener(e -> inHoaDon());

        // Xuất Excel
        btnXuatExcel.addActionListener(e -> xuatExcel());

        // Tính thành tiền
        btnTinhTien.addActionListener(e -> tinhThanhTien());

        // Chọn đặt phòng
        btnChonDatPhong.addActionListener(e -> chonDatPhong());

        // Làm mới form
        btnLamMoi.addActionListener(e -> lamMoiForm());

        // Đóng
        btnDong.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn đóng?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        // Chọn hàng trong bảng
        tblHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiHoaDon();
            }
        });

        // Tính thành tiền khi thay đổi các trường tiền
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                tinhThanhTien();
            }
        };
        txtTienPhong.addKeyListener(keyAdapter);
        txtTienDichVu.addKeyListener(keyAdapter);
        txtTienCoc.addKeyListener(keyAdapter);
        txtGiamGia.addKeyListener(keyAdapter);

        // Double click xem chi tiết
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    xemChiTiet();
                }
            }
        });

        // Phím tắt
        setupKeyboardShortcuts();
    }

    private void setupKeyboardShortcuts() {
        // F5: Tải lại
        getRootPane().registerKeyboardAction(e -> loadData(),
                KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Ctrl+N: Thêm mới
        getRootPane().registerKeyboardAction(e -> themHoaDon(),
                KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Ctrl+S: Lưu/Cập nhật
        getRootPane().registerKeyboardAction(e -> suaHoaDon(),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Delete: Xóa
        getRootPane().registerKeyboardAction(e -> xoaHoaDon(),
                KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Ctrl+F: Tìm kiếm
        getRootPane().registerKeyboardAction(e -> txtTimKiem.requestFocus(),
                KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Ctrl+P: In hóa đơn
        getRootPane().registerKeyboardAction(e -> inHoaDon(),
                KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Esc: Đóng
        getRootPane().registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<HoaDon> list = hoaDonDAO.getAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        DecimalFormat df = new DecimalFormat("#,###");

        double tongThu = 0;
        int tongChuaThanhToan = 0;

        for (HoaDon hd : list) {
            Object[] row = {
                    hd.getMaHD(),
                    hd.getMaDatPhong(),
                    hd.getNgayLap() != null ? sdf.format(hd.getNgayLap()) : "",
                    hd.getTienPhong(),
                    hd.getTienDichVu(),
                    hd.getTienCoc(),
                    hd.getGiamGia(),
                    hd.getThanhTien(),
                    hd.getPhuongThucTT(),
                    hd.getTrangThai(),
                    hd.getMaNV()
            };
            tableModel.addRow(row);

            // Tính tổng thu chỉ tính các hóa đơn đã thanh toán
            if ("Đã thanh toán".equals(hd.getTrangThai())) {
                tongThu += hd.getThanhTien();
            }

            // Đếm số hóa đơn chưa thanh toán
            if ("Chưa thanh toán".equals(hd.getTrangThai())) {
                tongChuaThanhToan++;
            }
        }

        lblTongHD.setText("Tổng hóa đơn: " + list.size());
        lblTongThu.setText(String.format("Tổng thu: %, .0f VNĐ", tongThu));
        lblTongChuaThanhToan.setText("Chưa thanh toán: " + tongChuaThanhToan);
    }

    private void loadComboboxData() {
        // Các combobox đã khởi tạo sẵn trong initComponents
    }

    private void timKiem(String keyword) {
        tableModel.setRowCount(0);
        List<HoaDon> list = hoaDonDAO.search(keyword);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (HoaDon hd : list) {
            Object[] row = {
                    hd.getMaHD(),
                    hd.getMaDatPhong(),
                    hd.getNgayLap() != null ? sdf.format(hd.getNgayLap()) : "",
                    hd.getTienPhong(),
                    hd.getTienDichVu(),
                    hd.getTienCoc(),
                    hd.getGiamGia(),
                    hd.getThanhTien(),
                    hd.getPhuongThucTT(),
                    hd.getTrangThai(),
                    hd.getMaNV()
            };
            tableModel.addRow(row);
        }
    }

    private void tinhThanhTien() {
        try {
            double tienPhong = txtTienPhong.getText().isEmpty()
                    ? 0 : Double.parseDouble(txtTienPhong.getText().replaceAll(",", ""));
            double tienDichVu = txtTienDichVu.getText().isEmpty()
                    ? 0 : Double.parseDouble(txtTienDichVu.getText().replaceAll(",", ""));
            double tienCoc = txtTienCoc.getText().isEmpty()
                    ? 0 : Double.parseDouble(txtTienCoc.getText().replaceAll(",", ""));
            double giamGia = txtGiamGia.getText().isEmpty()
                    ? 0 : Double.parseDouble(txtGiamGia.getText().replaceAll(",", ""));

            double thanhTien = tienPhong + tienDichVu - tienCoc - giamGia;
            if (thanhTien < 0) thanhTien = 0;

            txtThanhTien.setText(String.format("%,.0f", thanhTien));
        } catch (NumberFormatException ex) {
            txtThanhTien.setText("0");
        }
    }

    private void chonDatPhong() {
        List<DatPhong> dsDatPhong = datPhongDAO.getAll();
        String[] options = new String[dsDatPhong.size()];

        for (int i = 0; i < dsDatPhong.size(); i++) {
            DatPhong dp = dsDatPhong.get(i);
            options[i] = dp.getMaDatPhong() + " - " + dp.getTrangThai();
        }

        String selected = (String) JOptionPane.showInputDialog(
                this,
                "Chọn đặt phòng",
                "Chọn đặt phòng",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options.length > 0 ? options[0] : null
        );

        if (selected != null) {
            String maDatPhong = selected.split(" - ")[0];
            txtMaDatPhong.setText(maDatPhong);

            // Tự động lấy tiền dịch vụ từ chi tiết dịch vụ
            double tongTienDichVu = tinhTienDichVuTheoMaDP(maDatPhong);
            txtTienDichVu.setText(String.format("%,.0f", tongTienDichVu));

            // Lấy tiền cọc từ đặt phòng
            DatPhong dp = datPhongDAO.getById(maDatPhong);
            if (dp != null) {
                txtTienCoc.setText(String.format("%,.0f", dp.getTienCoc()));
            }

            // Tính lại thành tiền
            tinhThanhTien();
        }
    }

    private double tinhTienDichVuTheoMaDP(String maDatPhong) {
        double tongTien = 0;
        List<ChiTietDichVu> list = chiTietDichVuDAO.getByMaDatPhong(maDatPhong);
        for (ChiTietDichVu ctdv : list) {
            tongTien += ctdv.getThanhTien();
        }
        return tongTien;
    }

    private void themHoaDon() {
        try {
            // Validate dữ liệu
            if (txtMaDatPhong.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập mã đặt phòng!",
                        "Lỗi", JOptionPane.WARNING_MESSAGE);
                txtMaDatPhong.requestFocus();
                return;
            }

            // Kiểm tra đặt phòng có tồn tại không
            DatPhong dp = datPhongDAO.getById(txtMaDatPhong.getText().trim());
            if (dp == null) {
                JOptionPane.showMessageDialog(this,
                        "Mã đặt phòng không tồn tại!",
                        "Lỗi", JOptionPane.WARNING_MESSAGE);
                txtMaDatPhong.requestFocus();
                return;
            }

            // Kiểm tra có hóa đơn cho đặt phòng này chưa
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String maDP = tableModel.getValueAt(i, 1).toString();
                if (maDP.equals(txtMaDatPhong.getText().trim())) {
                    JOptionPane.showMessageDialog(this,
                            "Đã có hóa đơn cho đặt phòng này! Vui lòng chọn đặt phòng khác.",
                            "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Lấy thông tin
            HoaDon hd = new HoaDon();
            hd.setMaHD(hoaDonDAO.getAutoId());
            hd.setMaDatPhong(txtMaDatPhong.getText().trim());
            hd.setNgayLap((java.util.Date) txtNgayLap.getValue());
            hd.setTienPhong(Double.parseDouble(txtTienPhong.getText().replaceAll(",", "")));
            hd.setTienDichVu(Double.parseDouble(txtTienDichVu.getText().replaceAll(",", "")));
            hd.setTienCoc(Double.parseDouble(txtTienCoc.getText().replaceAll(",", "")));
            hd.setGiamGia(Double.parseDouble(txtGiamGia.getText().replaceAll(",", "")));
            hd.setThanhTien(Double.parseDouble(txtThanhTien.getText().replaceAll(",", "")));
            hd.setPhuongThucTT(cboPhuongThucTT.getSelectedItem().toString());
            hd.setTrangThai(cboTrangThai.getSelectedItem().toString());
            hd.setMaNV(txtMaNV.getText().trim());

            // Thêm vào database
            hoaDonDAO.insert(hd);

            JOptionPane.showMessageDialog(this,
                    "Thêm hóa đơn thành công! Mã hóa đơn: " + hd.getMaHD(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

            loadData();
            lamMoiForm();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Các trường tiền phải là số hợp lệ!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void suaHoaDon() {
        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần sửa!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Validate dữ liệu
            if (txtMaHD.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy mã hóa đơn!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy thông tin
            HoaDon hd = new HoaDon();
            hd.setMaHD(txtMaHD.getText().trim());
            hd.setMaDatPhong(txtMaDatPhong.getText().trim());
            hd.setNgayLap((java.util.Date) txtNgayLap.getValue());
            hd.setTienPhong(Double.parseDouble(txtTienPhong.getText().replaceAll(",", "")));
            hd.setTienDichVu(Double.parseDouble(txtTienDichVu.getText().replaceAll(",", "")));
            hd.setTienCoc(Double.parseDouble(txtTienCoc.getText().replaceAll(",", "")));
            hd.setGiamGia(Double.parseDouble(txtGiamGia.getText().replaceAll(",", "")));
            hd.setThanhTien(Double.parseDouble(txtThanhTien.getText().replaceAll(",", "")));
            hd.setPhuongThucTT(cboPhuongThucTT.getSelectedItem().toString());
            hd.setTrangThai(cboTrangThai.getSelectedItem().toString());
            hd.setMaNV(txtMaNV.getText().trim());

            // Cập nhật
            hoaDonDAO.update(hd);

            JOptionPane.showMessageDialog(this,
                    "Cập nhật hóa đơn thành công!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

            loadData();
            lamMoiForm();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Các trường tiền phải là số hợp lệ!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void xoaHoaDon() {
        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maHD = tableModel.getValueAt(selectedRow, 0).toString();
        String trangThai = tableModel.getValueAt(selectedRow, 9).toString();

        // Không cho xóa hóa đơn đã thanh toán
        if ("Đã thanh toán".equals(trangThai)) {
            JOptionPane.showMessageDialog(this,
                    "Không thể xóa hóa đơn đã thanh toán!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Bạn có chắc chắn muốn xóa hóa đơn %s?", maHD),
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                hoaDonDAO.delete(maHD);

                JOptionPane.showMessageDialog(this,
                        "Xóa hóa đơn thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);

                loadData();
                lamMoiForm();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Không thể xóa hóa đơn!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xemChiTiet() {
        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để xem chi tiết!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maHD = tableModel.getValueAt(selectedRow, 0).toString();
        String maDatPhong = tableModel.getValueAt(selectedRow, 1).toString();

        // Lấy chi tiết hóa đơn và dịch vụ
        HoaDon hd = hoaDonDAO.getById(maHD);
        DatPhong dp = datPhongDAO.getById(maDatPhong);
        List<ChiTietDichVu> dsDichVu = chiTietDichVuDAO.getByMaDatPhong(maDatPhong);

        // Tạo nội dung chi tiết
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("CHI TIẾT HÓA ĐƠN\n");
        sb.append("================\n\n");
        sb.append("Mã hóa đơn: ").append(hd.getMaHD()).append("\n");
        sb.append("Mã đặt phòng: ").append(hd.getMaDatPhong()).append("\n");
        sb.append("Ngày lập: ").append(sdf.format(hd.getNgayLap())).append("\n");
        sb.append("Nhân viên: ").append(hd.getMaNV()).append("\n\n");

        if (dp != null) {
            sb.append("THÔNG TIN ĐẶT PHÒNG\n");
            sb.append("-------------------\n");
            sb.append("Mã phòng: ").append(dp.getMaPhong()).append("\n");
            sb.append("Ngày nhận: ").append(sdf.format(dp.getNgayNhanPhong())).append("\n");
            sb.append("Ngày trả: ").append(sdf.format(dp.getNgayTraPhong())).append("\n");
            sb.append("Loại thuê: ").append(dp.getLoaiThue()).append("\n");
            sb.append(String.format("Tiền cọc: %,.0f VNĐ\n", dp.getTienCoc()));
            sb.append("\n");
        }

        sb.append("DỊCH VỤ SỬ DỤNG\n");
        sb.append("-------------------\n");
        if (dsDichVu.isEmpty()) {
            sb.append("Không có dịch vụ\n");
        } else {
            double tongTienDV = 0;
            for (ChiTietDichVu ctdv : dsDichVu) {
                sb.append(String.format("- %s: x%d, đơn giá %,.0f, thành tiền %,.0f VNĐ\n",
                        ctdv.getMaDV(),
                        ctdv.getSoLuong(),
                        ctdv.getDonGia(),
                        ctdv.getThanhTien()));
                tongTienDV += ctdv.getThanhTien();
            }
            sb.append(String.format("Tổng tiền dịch vụ: %,.0f VNĐ\n", tongTienDV));
        }
        sb.append("\n");

        sb.append("THANH TOÁN\n");
        sb.append("----------\n");
        sb.append(String.format("Tiền phòng: %,.0f VNĐ\n", hd.getTienPhong()));
        sb.append(String.format("Tiền dịch vụ: %,.0f VNĐ\n", hd.getTienDichVu()));
        sb.append(String.format("Tiền cọc: %,.0f VNĐ\n", hd.getTienCoc()));
        sb.append(String.format("Giảm giá: %,.0f VNĐ\n", hd.getGiamGia()));
        sb.append(String.format("THÀNH TIỀN: %,.0f VNĐ\n", hd.getThanhTien()));
        sb.append("Phương thức TT: ").append(hd.getPhuongThucTT()).append("\n");
        sb.append("Trạng thái: ").append(hd.getTrangThai()).append("\n");

        // Hiển thị trong dialog
        JTextArea textArea = new JTextArea(sb.toString(), 30, 50);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Chi tiết hóa đơn " + maHD, JOptionPane.INFORMATION_MESSAGE);
    }

    private void inHoaDon() {
        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để in!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maHD = tableModel.getValueAt(selectedRow, 0).toString();
        HoaDon hd = hoaDonDAO.getById(maHD);
        if (hd == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Tạo file text (có thể nâng cấp thành PDF sau)
            File file = new File("HoaDon_" + maHD + ".txt");
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("KHÁCH SẠN XYZ\n");
                writer.write("----------------------------\n");
                writer.write("HÓA ĐƠN THANH TOÁN\n");
                writer.write("----------------------------\n\n");

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                writer.write("Mã hóa đơn: " + hd.getMaHD() + "\n");
                writer.write("Ngày lập: " + sdf.format(hd.getNgayLap()) + "\n");
                writer.write("Mã đặt phòng: " + hd.getMaDatPhong() + "\n");
                writer.write("Nhân viên: " + hd.getMaNV() + "\n");
                writer.write("----------------------------\n");
                writer.write("THANH TOÁN\n");
                writer.write("----------------------------\n");
                writer.write(String.format("Tiền phòng: %,.0f VNĐ\n", hd.getTienPhong()));
                writer.write(String.format("Tiền dịch vụ: %,.0f VNĐ\n", hd.getTienDichVu()));
                writer.write(String.format("Tiền cọc: %,.0f VNĐ\n", hd.getTienCoc()));
                writer.write(String.format("Giảm giá: %,.0f VNĐ\n", hd.getGiamGia()));
                writer.write("----------------------------\n");
                writer.write(String.format("THÀNH TIỀN: %,.0f VNĐ\n", hd.getThanhTien()));
                writer.write("----------------------------\n");
                writer.write("Phương thức TT: " + hd.getPhuongThucTT() + "\n");
                writer.write("Trạng thái: " + hd.getTrangThai() + "\n\n");
                writer.write("Cảm ơn quý khách!\n");
                writer.write("Hẹn gặp lại!\n");
            }

            JOptionPane.showMessageDialog(this,
                    "In hóa đơn thành công! " + file.getAbsolutePath(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

            // Mở file
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi in hóa đơn: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuatExcel() {
        try {
            File file = new File("DanhSachHoaDon.csv");
            try (FileWriter writer = new FileWriter(file)) {
                // Ghi header
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.write(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) writer.write(",");
                }
                writer.write("\n");

                // Ghi dữ liệu
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object value = tableModel.getValueAt(i, j);
                        writer.write(value != null ? value.toString().replace(",", "") : "");
                        if (j < tableModel.getColumnCount() - 1) writer.write(",");
                    }
                    writer.write("\n");
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Xuất Excel thành công! " + file.getAbsolutePath(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi xuất Excel: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hienThiHoaDon() {
        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow == -1) return;

        try {
            txtMaHD.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtMaDatPhong.setText(tableModel.getValueAt(selectedRow, 1).toString());

            // Ngày lập
            String ngayStr = tableModel.getValueAt(selectedRow, 2).toString();
            if (!ngayStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                txtNgayLap.setValue(sdf.parse(ngayStr));
            }

            // Các trường tiền
            DecimalFormat df = new DecimalFormat("#,###");
            double tienPhong = (double) tableModel.getValueAt(selectedRow, 3);
            double tienDichVu = (double) tableModel.getValueAt(selectedRow, 4);
            double tienCoc = (double) tableModel.getValueAt(selectedRow, 5);
            double giamGia = (double) tableModel.getValueAt(selectedRow, 6);
            double thanhTien = (double) tableModel.getValueAt(selectedRow, 7);

            txtTienPhong.setText(df.format(tienPhong));
            txtTienDichVu.setText(df.format(tienDichVu));
            txtTienCoc.setText(df.format(tienCoc));
            txtGiamGia.setText(df.format(giamGia));
            txtThanhTien.setText(df.format(thanhTien));

            // Phương thức TT
            String pttt = tableModel.getValueAt(selectedRow, 8).toString();
            cboPhuongThucTT.setSelectedItem(pttt);

            // Trạng thái
            String trangThai = tableModel.getValueAt(selectedRow, 9).toString();
            cboTrangThai.setSelectedItem(trangThai);

            // Mã NV
            txtMaNV.setText(tableModel.getValueAt(selectedRow, 10).toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void lamMoiForm() {
        txtMaHD.setText("");
        txtMaDatPhong.setText("");
        txtNgayLap.setValue(new Date());
        txtTienPhong.setText("0");
        txtTienDichVu.setText("0");
        txtTienCoc.setText("0");
        txtGiamGia.setText("0");
        txtThanhTien.setText("0");
        cboPhuongThucTT.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        txtMaNV.setText("NV001");
        txtTimKiem.setText("");
        tblHoaDon.clearSelection();
    }

    // Renderer cho cột tiền tệ
    private class CurrencyRenderer extends javax.swing.table.DefaultTableCellRenderer {
        private DecimalFormat df = new DecimalFormat("#,###");

        public CurrencyRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof Number) {
                Number number = (Number) value;
                setText(String.format("%,.0f", number.doubleValue()));
            } else {
                super.setValue(value);
            }
        }
    }

    // Renderer cho cột trạng thái
    private class StatusRenderer extends javax.swing.table.DefaultTableCellRenderer {
        public StatusRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null) {
                String status = value.toString();
                if ("Đã thanh toán".equals(status)) {
                    c.setBackground(new Color(200, 230, 201)); // Xanh nhạt
                    c.setForeground(new Color(56, 142, 60));
                } else if ("Chưa thanh toán".equals(status)) {
                    c.setBackground(new Color(255, 245, 157)); // Vàng nhạt
                    c.setForeground(new Color(245, 124, 0));
                } else if ("Hủy".equals(status)) {
                    c.setBackground(new Color(255, 205, 210)); // Đỏ nhạt
                    c.setForeground(new Color(198, 40, 40));
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }
            }

            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }

            return c;
        }
    }
}
