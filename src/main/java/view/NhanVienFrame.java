package view;

import dao.NhanVienDAO;
import model.NhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NhanVienFrame extends JFrame {
    private NhanVienDAO nhanVienDAO;
    private DefaultTableModel tableModel;
    private JTable tblNhanVien;
    
    // Form fields
    private JTextField txtMaNV, txtTenNV, txtCMND, txtSDT, txtEmail, txtDiaChi, txtLuong, txtTenDangNhap, txtMatKhau;
    private JComboBox<String> cboGioiTinh, cboChucVu, cboQuyen;
    private JFormattedTextField txtNgaySinh, txtNgayVaoLam;
    
    public NhanVienFrame() {
        nhanVienDAO = new NhanVienDAO();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("QUẢN LÝ NHÂN VIÊN");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel với BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // === NORTH: Tìm kiếm ===
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northPanel.add(new JLabel("Tìm kiếm (tên hoặc chức vụ):"));
        JTextField txtTimKiem = new JTextField(20);
        northPanel.add(txtTimKiem);
        
        JButton btnTimKiem = new JButton("Tìm");
        northPanel.add(btnTimKiem);
        
        JButton btnTaiLai = new JButton("Tải lại");
        northPanel.add(btnTaiLai);
        
        // Nút đặc biệt: Đổi mật khẩu
        JButton btnDoiMatKhau = new JButton("Đổi mật khẩu");
        northPanel.add(btnDoiMatKhau);
        
        mainPanel.add(northPanel, BorderLayout.NORTH);
        
        // === CENTER: Bảng dữ liệu ===
        String[] columns = {"Mã NV", "Tên NV", "CMND", "SĐT", "Email", "Chức vụ", "Lương", "Ngày vào làm", "Quyền"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblNhanVien = new JTable(tableModel);
        tblNhanVien.setRowHeight(30);
        tblNhanVien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Custom renderer cho cột lương
        tblNhanVien.getColumnModel().getColumn(6).setCellRenderer(new CurrencyRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // === EAST: Form nhập liệu (2 cột) ===
        JPanel eastPanel = new JPanel(new GridBagLayout());
        eastPanel.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));
        eastPanel.setPreferredSize(new Dimension(500, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Mã NV (tự động sinh)
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Mã NV (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtMaNV = new JTextField(15);
        txtMaNV.setEditable(false);
        eastPanel.add(txtMaNV, gbc);
        gbc.gridx = 3; gbc.gridwidth = 1;
        JButton btnTaoMa = new JButton("Tạo mã");
        btnTaoMa.setToolTipText("Tạo mã tự động");
        eastPanel.add(btnTaoMa, gbc);
        
        row++;
        // Tên NV
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("Tên NV (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtTenNV = new JTextField(25);
        eastPanel.add(txtTenNV, gbc);
        
        row++;
        // CMND
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("CMND (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtCMND = new JTextField(20);
        eastPanel.add(txtCMND, gbc);
        
        row++;
        // Số điện thoại
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("SĐT (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtSDT = new JTextField(20);
        eastPanel.add(txtSDT, gbc);
        
        row++;
        // Email
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtEmail = new JTextField(20);
        eastPanel.add(txtEmail, gbc);
        
        row++;
        // Địa chỉ
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtDiaChi = new JTextField(20);
        eastPanel.add(txtDiaChi, gbc);
        
        row++;
        // Ngày sinh
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Ngày sinh:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtNgaySinh = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        txtNgaySinh.setColumns(15);
        eastPanel.add(txtNgaySinh, gbc);
        
        row++;
        // Giới tính
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        eastPanel.add(cboGioiTinh, gbc);
        
        row++;
        // Chức vụ
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Chức vụ (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        cboChucVu = new JComboBox<>(new String[]{"Quản lý", "Lễ tân", "Phục vụ", "Bảo vệ", "Kế toán", "Tổng giám đốc"});
        eastPanel.add(cboChucVu, gbc);
        
        row++;
        // Lương
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Lương (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        txtLuong = new JTextField(15);
        txtLuong.setText("5000000");
        eastPanel.add(txtLuong, gbc);
        gbc.gridx = 2; gbc.gridwidth = 2;
        eastPanel.add(new JLabel("VNĐ/tháng"), gbc);
        
        row++;
        // Ngày vào làm
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Ngày vào làm:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtNgayVaoLam = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        txtNgayVaoLam.setValue(new Date());
        txtNgayVaoLam.setColumns(15);
        eastPanel.add(txtNgayVaoLam, gbc);
        
        row++;
        // Tên đăng nhập
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Tên đăng nhập (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtTenDangNhap = new JTextField(15);
        eastPanel.add(txtTenDangNhap, gbc);
        
        row++;
        // Mật khẩu
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Mật khẩu (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtMatKhau = new JTextField(15);
        txtMatKhau.setText("123456"); // Mật khẩu mặc định
        eastPanel.add(txtMatKhau, gbc);
        
        row++;
        // Quyền
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Quyền (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        cboQuyen = new JComboBox<>(new String[]{"Admin", "Manager", "Staff"});
        eastPanel.add(cboQuyen, gbc);
        
        mainPanel.add(eastPanel, BorderLayout.EAST);
        
        // === SOUTH: Các nút chức năng ===
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnThem = new JButton("Thêm mới");
        JButton btnSua = new JButton("Cập nhật");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");
        JButton btnXuatExcel = new JButton("Xuất Excel");
        JButton btnDong = new JButton("Đóng");
        
        southPanel.add(btnThem);
        southPanel.add(btnSua);
        southPanel.add(btnXoa);
        southPanel.add(btnLamMoi);
        southPanel.add(btnXuatExcel);
        southPanel.add(btnDong);
        
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // === SỰ KIỆN ===
        btnTaiLai.addActionListener(e -> loadData());
        
        btnTimKiem.addActionListener(e -> {
            String keyword = txtTimKiem.getText().trim();
            if (!keyword.isEmpty()) {
                timKiem(keyword);
            }
        });
        
        btnTaoMa.addActionListener(e -> taoMaTuDong());
        
        btnThem.addActionListener(e -> themNhanVien());
        
        btnSua.addActionListener(e -> suaNhanVien());
        
        btnXoa.addActionListener(e -> xoaNhanVien());
        
        btnLamMoi.addActionListener(e -> lamMoiForm());
        
        btnXuatExcel.addActionListener(e -> xuatExcel());
        
        btnDoiMatKhau.addActionListener(e -> doiMatKhau());
        
        btnDong.addActionListener(e -> dispose());
        
        tblNhanVien.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiChiTiet();
            }
        });
        
        taoMaTuDong();
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<NhanVien> list = nhanVienDAO.getAll();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (NhanVien nv : list) {
            Object[] row = {
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getCmnd(),
                nv.getSoDienThoai(),
                nv.getEmail(),
                nv.getChucVu(),
                nv.getLuong(),
                nv.getNgayVaoLam() != null ? sdf.format(nv.getNgayVaoLam()) : "",
                nv.getQuyen()
            };
            tableModel.addRow(row);
        }
        
        JOptionPane.showMessageDialog(this, "Đã tải " + list.size() + " nhân viên.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void timKiem(String keyword) {
        tableModel.setRowCount(0);
        List<NhanVien> list = nhanVienDAO.search(keyword);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (NhanVien nv : list) {
            Object[] row = {
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getCmnd(),
                nv.getSoDienThoai(),
                nv.getEmail(),
                nv.getChucVu(),
                nv.getLuong(),
                nv.getNgayVaoLam() != null ? sdf.format(nv.getNgayVaoLam()) : "",
                nv.getQuyen()
            };
            tableModel.addRow(row);
        }
        
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void taoMaTuDong() {
        List<NhanVien> list = nhanVienDAO.getAll();
        int maxId = 0;
        
        for (NhanVien nv : list) {
            String maNV = nv.getMaNV();
            if (maNV != null && maNV.startsWith("NV")) {
                try {
                    int num = Integer.parseInt(maNV.substring(2));
                    if (num > maxId) {
                        maxId = num;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua
                }
            }
        }
        
        txtMaNV.setText(String.format("NV%03d", maxId + 1));
    }
    
    private void themNhanVien() {
        try {
            if (!validateData()) {
                return;
            }
            
            NhanVien nv = new NhanVien();
            nv.setMaNV(txtMaNV.getText().trim());
            nv.setTenNV(txtTenNV.getText().trim());
            nv.setCmnd(txtCMND.getText().trim());
            nv.setSoDienThoai(txtSDT.getText().trim());
            nv.setEmail(txtEmail.getText().trim());
            nv.setDiaChi(txtDiaChi.getText().trim());
            nv.setGioiTinh(cboGioiTinh.getSelectedItem().toString());
            nv.setChucVu(cboChucVu.getSelectedItem().toString());
            nv.setLuong(Double.parseDouble(txtLuong.getText().trim()));
            nv.setTenDangNhap(txtTenDangNhap.getText().trim());
            nv.setMatKhau(txtMatKhau.getText().trim());
            nv.setQuyen(cboQuyen.getSelectedItem().toString());
            
            if (!txtNgaySinh.getText().trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    nv.setNgaySinh(sdf.parse(txtNgaySinh.getText().trim()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng (dd/MM/yyyy)!");
                    return;
                }
            }
            
            if (!txtNgayVaoLam.getText().trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    nv.setNgayVaoLam(sdf.parse(txtNgayVaoLam.getText().trim()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Ngày vào làm không đúng định dạng (dd/MM/yyyy)!");
                    return;
                }
            } else {
                nv.setNgayVaoLam(new Date());
            }
            
            if (kiemTraTrungTenDangNhap(txtTenDangNhap.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại! Vui lòng chọn tên khác.");
                return;
            }
            
            nhanVienDAO.insert(nv);
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!\nTên đăng nhập: " + nv.getTenDangNhap() + 
                                         "\nMật khẩu: " + nv.getMatKhau(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            lamMoiForm();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lương phải là số!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private boolean kiemTraTrungTenDangNhap(String tenDangNhap) {
        List<NhanVien> list = nhanVienDAO.getAll();
        for (NhanVien nv : list) {
            if (nv.getTenDangNhap() != null && nv.getTenDangNhap().equalsIgnoreCase(tenDangNhap)) {
                return true;
            }
        }
        return false;
    }
    
    private void suaNhanVien() {
        int selectedRow = tblNhanVien.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
            return;
        }
        
        try {
            if (!validateData()) {
                return;
            }
            
            NhanVien nv = new NhanVien();
            nv.setMaNV(txtMaNV.getText().trim());
            nv.setTenNV(txtTenNV.getText().trim());
            nv.setCmnd(txtCMND.getText().trim());
            nv.setSoDienThoai(txtSDT.getText().trim());
            nv.setEmail(txtEmail.getText().trim());
            nv.setDiaChi(txtDiaChi.getText().trim());
            nv.setGioiTinh(cboGioiTinh.getSelectedItem().toString());
            nv.setChucVu(cboChucVu.getSelectedItem().toString());
            nv.setLuong(Double.parseDouble(txtLuong.getText().trim()));
            nv.setTenDangNhap(txtTenDangNhap.getText().trim());
            nv.setMatKhau(txtMatKhau.getText().trim());
            nv.setQuyen(cboQuyen.getSelectedItem().toString());
            
            if (!txtNgaySinh.getText().trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    nv.setNgaySinh(sdf.parse(txtNgaySinh.getText().trim()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng (dd/MM/yyyy)!");
                    return;
                }
            }
            
            if (!txtNgayVaoLam.getText().trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    nv.setNgayVaoLam(sdf.parse(txtNgayVaoLam.getText().trim()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Ngày vào làm không đúng định dạng (dd/MM/yyyy)!");
                    return;
                }
            }
            
            nhanVienDAO.update(nv);
            JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
            loadData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xoaNhanVien() {
        int selectedRow = tblNhanVien.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
            return;
        }
        
        String maNV = tableModel.getValueAt(selectedRow, 0).toString();
        String tenNV = tableModel.getValueAt(selectedRow, 1).toString();
        String quyen = tableModel.getValueAt(selectedRow, 8).toString();
        
        if ("Admin".equalsIgnoreCase(quyen)) {
            JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản Admin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa nhân viên: " + tenNV + "?\n(Lưu ý: Không thể xóa nếu nhân viên đã có giao dịch)", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                nhanVienDAO.delete(maNV);
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
                loadData();
                lamMoiForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Không thể xóa! Có thể nhân viên đã có đơn đặt phòng hoặc hóa đơn.", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void hienThiChiTiet() {
        int selectedRow = tblNhanVien.getSelectedRow();
        if (selectedRow == -1) return;
        
        try {
            txtMaNV.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenNV.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtCMND.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtSDT.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtEmail.setText(tableModel.getValueAt(selectedRow, 4).toString());
            
            String chucVu = tableModel.getValueAt(selectedRow, 5).toString();
            cboChucVu.setSelectedItem(chucVu);
            
            String luongStr = tableModel.getValueAt(selectedRow, 6).toString();
            if (luongStr.contains("VNĐ")) {
                luongStr = luongStr.replaceAll("[^0-9]", "");
            }
            txtLuong.setText(luongStr);
            
            String maNV = txtMaNV.getText();
            NhanVien nv = nhanVienDAO.getById(maNV);
            
            if (nv != null) {
                txtDiaChi.setText(nv.getDiaChi() != null ? nv.getDiaChi() : "");
                
                if (nv.getNgaySinh() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    txtNgaySinh.setText(sdf.format(nv.getNgaySinh()));
                } else {
                    txtNgaySinh.setText("");
                }
                
                if (nv.getGioiTinh() != null) {
                    cboGioiTinh.setSelectedItem(nv.getGioiTinh());
                }
                
                if (nv.getNgayVaoLam() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    txtNgayVaoLam.setText(sdf.format(nv.getNgayVaoLam()));
                } else {
                    txtNgayVaoLam.setText("");
                }
                
                txtTenDangNhap.setText(nv.getTenDangNhap() != null ? nv.getTenDangNhap() : "");
                txtMatKhau.setText(nv.getMatKhau() != null ? nv.getMatKhau() : "");
                
                if (nv.getQuyen() != null) {
                    cboQuyen.setSelectedItem(nv.getQuyen());
                }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void lamMoiForm() {
        taoMaTuDong();
        txtTenNV.setText("");
        txtCMND.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtNgaySinh.setText("");
        cboGioiTinh.setSelectedIndex(0);
        cboChucVu.setSelectedIndex(0);
        txtLuong.setText("5000000");
        txtNgayVaoLam.setValue(new Date());
        txtTenDangNhap.setText("");
        txtMatKhau.setText("123456");
        cboQuyen.setSelectedIndex(2); // Staff
        txtTenNV.requestFocus();
        tblNhanVien.clearSelection();
    }
    
    private boolean validateData() {
        if (txtTenNV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhân viên!");
            txtTenNV.requestFocus();
            return false;
        }
        
        if (txtCMND.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số CMND!");
            txtCMND.requestFocus();
            return false;
        }
        
        if (txtSDT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!");
            txtSDT.requestFocus();
            return false;
        }
        
        if (!txtSDT.getText().trim().matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (10-11 số)!");
            txtSDT.requestFocus();
            return false;
        }
        
        if (txtLuong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lương!");
            txtLuong.requestFocus();
            return false;
        }
        
        try {
            double luong = Double.parseDouble(txtLuong.getText().trim());
            if (luong < 0) {
                JOptionPane.showMessageDialog(this, "Lương không được âm!");
                txtLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lương phải là số!");
            txtLuong.requestFocus();
            return false;
        }
        
        if (txtTenDangNhap.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!");
            txtTenDangNhap.requestFocus();
            return false;
        }
        
        if (txtMatKhau.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!");
            txtMatKhau.requestFocus();
            return false;
        }
        
        if (txtMatKhau.getText().trim().length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự!");
            txtMatKhau.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void doiMatKhau() {
        int selectedRow = tblNhanVien.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần đổi mật khẩu!");
            return;
        }
        
        String maNV = tableModel.getValueAt(selectedRow, 0).toString();
        String tenNV = tableModel.getValueAt(selectedRow, 1).toString();
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JPasswordField txtMatKhauCu = new JPasswordField(20);
        JPasswordField txtMatKhauMoi = new JPasswordField(20);
        JPasswordField txtXacNhan = new JPasswordField(20);
        
        panel.add(new JLabel("Mật khẩu cũ:"));
        panel.add(txtMatKhauCu);
        panel.add(new JLabel("Mật khẩu mới:"));
        panel.add(txtMatKhauMoi);
        panel.add(new JLabel("Xác nhận mật khẩu:"));
        panel.add(txtXacNhan);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Đổi mật khẩu cho " + tenNV, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String matKhauCu = new String(txtMatKhauCu.getPassword());
            String matKhauMoi = new String(txtMatKhauMoi.getPassword());
            String xacNhan = new String(txtXacNhan.getPassword());
            
            if (!matKhauMoi.equals(xacNhan)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu mới và xác nhận không khớp!");
                return;
            }
            
            if (matKhauMoi.length() < 6) {
                JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có ít nhất 6 ký tự!");
                return;
            }
            
            NhanVien nv = nhanVienDAO.getById(maNV);
            if (nv != null) {
                if (!matKhauCu.equals(nv.getMatKhau())) {
                    JOptionPane.showMessageDialog(this, "Mật khẩu cũ không đúng!");
                    return;
                }
                
                nv.setMatKhau(matKhauMoi);
                nhanVienDAO.update(nv);
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            }
        }
    }
    
    private void xuatExcel() {
        JOptionPane.showMessageDialog(this, "Chức năng xuất Excel đang phát triển...");
    }
    
    // Renderer cho cột tiền tệ
    private class CurrencyRenderer extends javax.swing.table.DefaultTableCellRenderer {
        public CurrencyRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
        
        @Override
        public void setValue(Object value) {
            if (value instanceof Number) {
                Number number = (Number) value;
                setText(String.format("%,.0f VNĐ", number.doubleValue()));
            } else {
                super.setValue(value);
            }
        }
    }
}
