package view;

import dao.KhachHangDAO;
import model.KhachHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class KhachHangFrame extends JFrame {
    private KhachHangDAO khachHangDAO;
    private DefaultTableModel tableModel;
    private JTable tblKhachHang;
    private JTextField txtTimKiem;
    
    // Form fields
    private JTextField txtMaKH, txtTenKH, txtCMND, txtSDT, txtEmail, txtDiaChi, txtQuocTich;
    private JComboBox<String> cboGioiTinh;
    private JFormattedTextField txtNgaySinh;
    
    public KhachHangFrame() {
        khachHangDAO = new KhachHangDAO();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Quản Lý Khách Hàng");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // === NORTH: Tìm kiếm ===
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northPanel.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(20);
        northPanel.add(txtTimKiem);
        
        JButton btnTimKiem = new JButton("Tìm");
        northPanel.add(btnTimKiem);
        
        JButton btnTaiLai = new JButton("Tải lại");
        northPanel.add(btnTaiLai);
        
        mainPanel.add(northPanel, BorderLayout.NORTH);
        
        // === CENTER: Bảng dữ liệu ===
        String[] columns = {"Mã KH", "Tên KH", "CMND", "SĐT", "Email", "Địa chỉ", "Ngày sinh", "Giới tính", "Quốc tịch"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblKhachHang = new JTable(tableModel);
        tblKhachHang.setRowHeight(25);
        tblKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // === EAST: Form nhập liệu ===
        JPanel eastPanel = new JPanel(new GridBagLayout());
        eastPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Mã KH (tự động)
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Mã KH:"), gbc);
        gbc.gridx = 1;
        txtMaKH = new JTextField(15);
        txtMaKH.setEditable(false);
        eastPanel.add(txtMaKH, gbc);
        gbc.gridx = 2;
        JButton btnTaoMa = new JButton("Tạo mã");
        eastPanel.add(btnTaoMa, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Tên KH:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtTenKH = new JTextField(20);
        eastPanel.add(txtTenKH, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("CMND:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtCMND = new JTextField(20);
        eastPanel.add(txtCMND, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtSDT = new JTextField(20);
        eastPanel.add(txtSDT, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtEmail = new JTextField(20);
        eastPanel.add(txtEmail, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtDiaChi = new JTextField(20);
        eastPanel.add(txtDiaChi, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Ngày sinh:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtNgaySinh = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        txtNgaySinh.setColumns(15);
        eastPanel.add(txtNgaySinh, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        eastPanel.add(cboGioiTinh, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Quốc tịch:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtQuocTich = new JTextField(20);
        eastPanel.add(txtQuocTich, gbc);
        
        mainPanel.add(eastPanel, BorderLayout.EAST);
        
        // === SOUTH: Các nút chức năng ===
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnThem = new JButton("Thêm mới");
        JButton btnSua = new JButton("Cập nhật");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");
        
        southPanel.add(btnThem);
        southPanel.add(btnSua);
        southPanel.add(btnXoa);
        southPanel.add(btnLamMoi);
        
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // === SỰ KIỆN ===
        btnTaiLai.addActionListener(e -> loadData());
        
        btnTimKiem.addActionListener(e -> timKiem());
        
        btnTaoMa.addActionListener(e -> {
            txtMaKH.setText(khachHangDAO.getAutoId());
        });
        
        btnThem.addActionListener(e -> themKhachHang());
        
        btnSua.addActionListener(e -> suaKhachHang());
        
        btnXoa.addActionListener(e -> xoaKhachHang());
        
        btnLamMoi.addActionListener(e -> lamMoiForm());
        
        tblKhachHang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiChiTiet();
            }
        });
        
        txtMaKH.setText(khachHangDAO.getAutoId());
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<KhachHang> list = khachHangDAO.getAll();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (KhachHang kh : list) {
            Object[] row = {
                kh.getMaKH(),
                kh.getTenKH(),
                kh.getCmnd(),
                kh.getSoDienThoai(),
                kh.getEmail(),
                kh.getDiaChi(),
                kh.getNgaySinh() != null ? sdf.format(kh.getNgaySinh()) : "",
                kh.getGioiTinh(),
                kh.getQuocTich()
            };
            tableModel.addRow(row);
        }
    }
    
    private void timKiem() {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        
        tableModel.setRowCount(0);
        List<KhachHang> list = khachHangDAO.search(keyword);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (KhachHang kh : list) {
            Object[] row = {
                kh.getMaKH(),
                kh.getTenKH(),
                kh.getCmnd(),
                kh.getSoDienThoai(),
                kh.getEmail(),
                kh.getDiaChi(),
                kh.getNgaySinh() != null ? sdf.format(kh.getNgaySinh()) : "",
                kh.getGioiTinh(),
                kh.getQuocTich()
            };
            tableModel.addRow(row);
        }
    }
    
    private void themKhachHang() {
        try {
            if (txtTenKH.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!");
                return;
            }
            
            KhachHang kh = new KhachHang();
            kh.setMaKH(txtMaKH.getText().trim());
            kh.setTenKH(txtTenKH.getText().trim());
            kh.setCmnd(txtCMND.getText().trim());
            kh.setSoDienThoai(txtSDT.getText().trim());
            kh.setEmail(txtEmail.getText().trim());
            kh.setDiaChi(txtDiaChi.getText().trim());
            kh.setGioiTinh(cboGioiTinh.getSelectedItem().toString());
            kh.setQuocTich(txtQuocTich.getText().trim());
            
            if (!txtNgaySinh.getText().trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    kh.setNgaySinh(sdf.parse(txtNgaySinh.getText().trim()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng (dd/MM/yyyy)!");
                    return;
                }
            }
            
            khachHangDAO.insert(kh);
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            loadData();
            lamMoiForm();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void suaKhachHang() {
        int selectedRow = tblKhachHang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
            return;
        }
        
        try {
            KhachHang kh = new KhachHang();
            kh.setMaKH(txtMaKH.getText().trim());
            kh.setTenKH(txtTenKH.getText().trim());
            kh.setCmnd(txtCMND.getText().trim());
            kh.setSoDienThoai(txtSDT.getText().trim());
            kh.setEmail(txtEmail.getText().trim());
            kh.setDiaChi(txtDiaChi.getText().trim());
            kh.setGioiTinh(cboGioiTinh.getSelectedItem().toString());
            kh.setQuocTich(txtQuocTich.getText().trim());
            
            if (!txtNgaySinh.getText().trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    kh.setNgaySinh(sdf.parse(txtNgaySinh.getText().trim()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng (dd/MM/yyyy)!");
                    return;
                }
            }
            
            khachHangDAO.update(kh);
            JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!");
            loadData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xoaKhachHang() {
        int selectedRow = tblKhachHang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
            return;
        }
        
        String maKH = tableModel.getValueAt(selectedRow, 0).toString();
        String tenKH = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa khách hàng: " + tenKH + "?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                khachHangDAO.delete(maKH);
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                loadData();
                lamMoiForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Không thể xóa! Khách hàng có thể đang có đơn đặt phòng.", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void hienThiChiTiet() {
        int selectedRow = tblKhachHang.getSelectedRow();
        if (selectedRow == -1) return;
        
        txtMaKH.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtTenKH.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtCMND.setText(tableModel.getValueAt(selectedRow, 2).toString());
        txtSDT.setText(tableModel.getValueAt(selectedRow, 3).toString());
        txtEmail.setText(tableModel.getValueAt(selectedRow, 4).toString());
        txtDiaChi.setText(tableModel.getValueAt(selectedRow, 5).toString());
        txtNgaySinh.setText(tableModel.getValueAt(selectedRow, 6).toString());
        
        String gioiTinh = tableModel.getValueAt(selectedRow, 7).toString();
        cboGioiTinh.setSelectedItem(gioiTinh);
        
        txtQuocTich.setText(tableModel.getValueAt(selectedRow, 8).toString());
    }
    
    private void lamMoiForm() {
        txtMaKH.setText(khachHangDAO.getAutoId());
        txtTenKH.setText("");
        txtCMND.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtNgaySinh.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtQuocTich.setText("Việt Nam");
        txtTenKH.requestFocus();
        tblKhachHang.clearSelection();
    }
}
