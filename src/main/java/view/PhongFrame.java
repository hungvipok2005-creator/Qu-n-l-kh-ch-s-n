package view;

import dao.LoaiPhongDAO;
import dao.PhongDAO;
import model.LoaiPhong;
import model.Phong;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PhongFrame extends JFrame {
    private PhongDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;
    private DefaultTableModel tableModel;
    private JTable tblPhong;
    
    // Form fields (sẽ dùng cho dialog thêm/sửa)
    private JTextField txtMaPhong, txtTenPhong, txtTang, txtGhiChu;
    private JComboBox<String> cboMaLoaiPhong, cboTrangThai;
    
    public PhongFrame() {
        phongDAO = new PhongDAO();
        loaiPhongDAO = new LoaiPhongDAO();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("QUẢN LÝ PHÒNG");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // === NORTH: Tìm kiếm ===
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northPanel.add(new JLabel("Tìm kiếm:"));
        JTextField txtTimKiem = new JTextField(20);
        northPanel.add(txtTimKiem);
        
        JButton btnTim = new JButton("Tìm");
        northPanel.add(btnTim);
        
        JButton btnTaiLai = new JButton("Tải lại");
        northPanel.add(btnTaiLai);
        
        JButton btnPhongTrong = new JButton("Phòng trống");
        northPanel.add(btnPhongTrong);
        
        JButton btnPhongDangThue = new JButton("Phòng đang thuê");
        northPanel.add(btnPhongDangThue);
        
        JButton btnThongKe = new JButton("Thống kê");
        northPanel.add(btnThongKe);
        
        mainPanel.add(northPanel, BorderLayout.NORTH);
        
        // === CENTER: Bảng ===
        String[] columns = {"Mã phòng", "Mã loại", "Tên phòng", "Tầng", "Trạng thái", "Ghi chú"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblPhong = new JTable(tableModel);
        tblPhong.setRowHeight(25);
        tblPhong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Tô màu các hàng theo trạng thái
        tblPhong.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    String trangThai = table.getValueAt(row, 4).toString();
                    switch (trangThai) {
                        case "Trống":
                            c.setBackground(new Color(220, 255, 220)); // Xanh nhạt
                            break;
                        case "Đang thuê":
                            c.setBackground(new Color(255, 220, 220)); // Đỏ nhạt
                            break;
                        case "Đang dọn dẹp":
                            c.setBackground(new Color(255, 255, 200)); // Vàng nhạt
                            break;
                        case "Bảo trì":
                            c.setBackground(new Color(220, 220, 220)); // Xám
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblPhong);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // === SOUTH: Các nút chức năng ===
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnThem = new JButton("Thêm phòng");
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
        // Tìm kiếm
        btnTim.addActionListener(e -> {
            String keyword = txtTimKiem.getText().trim();
            if (!keyword.isEmpty()) {
                timKiem(keyword);
            } else {
                loadData();
            }
        });
        
        // Tải lại
        btnTaiLai.addActionListener(e -> loadData());
        
        // Hiển thị phòng trống
        btnPhongTrong.addActionListener(e -> hienThiPhongTrong());
        
        // Hiển thị phòng đang thuê
        btnPhongDangThue.addActionListener(e -> hienThiPhongDangThue());
        
        // Thống kê
        btnThongKe.addActionListener(e -> hienThiThongKe());
        
        // Thêm mới
        btnThem.addActionListener(e -> themPhong());
        
        // Sửa
        btnSua.addActionListener(e -> suaPhong());
        
        // Xóa
        btnXoa.addActionListener(e -> xoaPhong());
        
        // Làm mới
        btnLamMoi.addActionListener(e -> lamMoiDuLieu());
        
        // Xuất Excel
        btnXuatExcel.addActionListener(e -> xuatExcel());
        
        // Đóng
        btnDong.addActionListener(e -> dispose());
        
        // Double click để sửa
        tblPhong.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    suaPhong();
                }
            }
        });
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Phong> list = phongDAO.getAll();
        
        for (Phong p : list) {
            Object[] row = {
                p.getMaPhong(),
                p.getMaLoaiPhong(),
                p.getTenPhong(),
                p.getTang(),
                p.getTrangThai(),
                p.getGhiChu()
            };
            tableModel.addRow(row);
        }
        
        int tongPhong = list.size();
        int phongTrong = phongDAO.getPhongTrong().size();
        int phongDangThue = demPhongTheoTrangThai("Đang thuê");
        int phongBaoTri = demPhongTheoTrangThai("Bảo trì");
        
        setTitle("QUẢN LÝ PHÒNG - Tổng: " + tongPhong + 
                " | Trống: " + phongTrong + 
                " | Đang thuê: " + phongDangThue + 
                " | Bảo trì: " + phongBaoTri);
    }
    
    private int demPhongTheoTrangThai(String trangThai) {
        List<Phong> list = phongDAO.getAll();
        int count = 0;
        for (Phong p : list) {
            if (trangThai.equals(p.getTrangThai())) {
                count++;
            }
        }
        return count;
    }
    
    private void timKiem(String keyword) {
        tableModel.setRowCount(0);
        List<Phong> list = phongDAO.search(keyword);
        
        for (Phong p : list) {
            Object[] row = {
                p.getMaPhong(),
                p.getMaLoaiPhong(),
                p.getTenPhong(),
                p.getTang(),
                p.getTrangThai(),
                p.getGhiChu()
            };
            tableModel.addRow(row);
        }
        
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy phòng nào!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void hienThiPhongTrong() {
        tableModel.setRowCount(0);
        List<Phong> list = phongDAO.getPhongTrong();
        
        for (Phong p : list) {
            Object[] row = {
                p.getMaPhong(),
                p.getMaLoaiPhong(),
                p.getTenPhong(),
                p.getTang(),
                p.getTrangThai(),
                p.getGhiChu()
            };
            tableModel.addRow(row);
        }
        
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hiện không có phòng trống!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void hienThiPhongDangThue() {
        tableModel.setRowCount(0);
        List<Phong> list = phongDAO.getAll();
        
        for (Phong p : list) {
            if ("Đang thuê".equals(p.getTrangThai())) {
                Object[] row = {
                    p.getMaPhong(),
                    p.getMaLoaiPhong(),
                    p.getTenPhong(),
                    p.getTang(),
                    p.getTrangThai(),
                    p.getGhiChu()
                };
                tableModel.addRow(row);
            }
        }
        
        int count = demPhongTheoTrangThai("Đang thuê");
        if (count == 0) {
            JOptionPane.showMessageDialog(this, "Hiện không có phòng đang thuê!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void hienThiThongKe() {
        int tongPhong = tableModel.getRowCount();
        int phongTrong = phongDAO.getPhongTrong().size();
        int phongDangThue = demPhongTheoTrangThai("Đang thuê");
        int phongBaoTri = demPhongTheoTrangThai("Bảo trì");
        int phongDonDep = demPhongTheoTrangThai("Đang dọn dẹp");
        
        String thongKe = String.format(
            "THỐNG KÊ PHÒNG\n" +
            "================\n" +
            "Tổng số phòng: %d\n" +
            "Phòng trống: %d (%.1f%%)\n" +
            "Phòng đang thuê: %d (%.1f%%)\n" +
            "Phòng bảo trì: %d (%.1f%%)\n" +
            "Phòng đang dọn dẹp: %d (%.1f%%)\n",
            tongPhong,
            phongTrong, (tongPhong > 0 ? (phongTrong * 100.0 / tongPhong) : 0),
            phongDangThue, (tongPhong > 0 ? (phongDangThue * 100.0 / tongPhong) : 0),
            phongBaoTri, (tongPhong > 0 ? (phongBaoTri * 100.0 / tongPhong) : 0),
            phongDonDep, (tongPhong > 0 ? (phongDonDep * 100.0 / tongPhong) : 0)
        );
        
        JOptionPane.showMessageDialog(this, thongKe, "Thống kê phòng", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void themPhong() {
        JDialog dialog = new JDialog(this, "Thêm phòng mới", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Mã phòng
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Mã phòng (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtMaPhong = new JTextField(15);
        String autoId = taoMaPhongTuDong();
        txtMaPhong.setText(autoId);
        panel.add(txtMaPhong, gbc);
        
        row++;
        // Mã loại phòng
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Loại phòng (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        cboMaLoaiPhong = new JComboBox<>();
        loadLoaiPhongVaoComboBox();
        panel.add(cboMaLoaiPhong, gbc);
        
        row++;
        // Tên phòng
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Tên phòng (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtTenPhong = new JTextField(15);
        panel.add(txtTenPhong, gbc);
        
        row++;
        // Tầng
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Tầng (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtTang = new JTextField(15);
        panel.add(txtTang, gbc);
        
        row++;
        // Trạng thái
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Trạng thái (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        cboTrangThai = new JComboBox<>(new String[]{"Trống", "Đang thuê", "Đang dọn dẹp", "Bảo trì"});
        panel.add(cboTrangThai, gbc);
        
        row++;
        // Ghi chú
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtGhiChu = new JTextField(15);
        panel.add(txtGhiChu, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnLuu = new JButton("Lưu");
        JButton btnHuy = new JButton("Hủy");
        
        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        btnLuu.addActionListener(e -> {
            if (validateForm()) {
                luuPhongMoi();
                dialog.dispose();
                loadData();
            }
        });
        
        btnHuy.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void suaPhong() {
        int selectedRow = tblPhong.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maPhong = tableModel.getValueAt(selectedRow, 0).toString();
        Phong phong = phongDAO.getById(maPhong);
        
        if (phong == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "Cập nhật thông tin phòng", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Mã phòng (readonly)
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Mã phòng:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtMaPhong = new JTextField(15);
        txtMaPhong.setText(phong.getMaPhong());
        txtMaPhong.setEditable(false);
        panel.add(txtMaPhong, gbc);
        
        row++;
        // Mã loại phòng
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Loại phòng (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        cboMaLoaiPhong = new JComboBox<>();
        loadLoaiPhongVaoComboBox();
        for (int i = 0; i < cboMaLoaiPhong.getItemCount(); i++) {
            if (cboMaLoaiPhong.getItemAt(i).toString().startsWith(phong.getMaLoaiPhong())) {
                cboMaLoaiPhong.setSelectedIndex(i);
                break;
            }
        }
        panel.add(cboMaLoaiPhong, gbc);
        
        row++;
        // Tên phòng
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Tên phòng (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtTenPhong = new JTextField(15);
        txtTenPhong.setText(phong.getTenPhong());
        panel.add(txtTenPhong, gbc);
        
        row++;
        // Tầng
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Tầng (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtTang = new JTextField(15);
        txtTang.setText(String.valueOf(phong.getTang()));
        panel.add(txtTang, gbc);
        
        row++;
        // Trạng thái
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Trạng thái (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        cboTrangThai = new JComboBox<>(new String[]{"Trống", "Đang thuê", "Đang dọn dẹp", "Bảo trì"});
        cboTrangThai.setSelectedItem(phong.getTrangThai());
        panel.add(cboTrangThai, gbc);
        
        row++;
        // Ghi chú
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtGhiChu = new JTextField(15);
        txtGhiChu.setText(phong.getGhiChu() != null ? phong.getGhiChu() : "");
        panel.add(txtGhiChu, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnLuu = new JButton("Cập nhật");
        JButton btnHuy = new JButton("Hủy");
        
        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        btnLuu.addActionListener(e -> {
            if (validateForm()) {
                capNhatPhong();
                dialog.dispose();
                loadData();
            }
        });
        
        btnHuy.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void xoaPhong() {
        int selectedRow = tblPhong.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maPhong = tableModel.getValueAt(selectedRow, 0).toString();
        String tenPhong = tableModel.getValueAt(selectedRow, 2).toString();
        String trangThai = tableModel.getValueAt(selectedRow, 4).toString();
        
        if ("Đang thuê".equals(trangThai)) {
            JOptionPane.showMessageDialog(this, 
                "Không thể xóa phòng đang có khách thuê!\nVui lòng chờ khách trả phòng hoặc hủy đặt phòng.",
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa phòng: " + tenPhong + "?\n" +
            "Mã: " + maPhong + "\n" +
            "(Lưu ý: Không thể hoàn tác sau khi xóa)", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                phongDAO.delete(maPhong);
                JOptionPane.showMessageDialog(this, "Xóa phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Không thể xóa phòng!\nCó thể phòng đang được tham chiếu trong bảng khác.", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void lamMoiDuLieu() {
        loadData();
        JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void xuatExcel() {
        JOptionPane.showMessageDialog(this, 
            "Chức năng xuất Excel đang phát triển...\n" +
            "Số lượng phòng hiện tại: " + tableModel.getRowCount(), 
            "Thông báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String taoMaPhongTuDong() {
        List<Phong> list = phongDAO.getAll();
        int maxNumber = 0;
        
        for (Phong p : list) {
            String maPhong = p.getMaPhong();
            if (maPhong != null && maPhong.startsWith("P")) {
                try {
                    String numberStr = maPhong.substring(1);
                    int num = Integer.parseInt(numberStr);
                    if (num > maxNumber) {
                        maxNumber = num;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua nếu không parse được
                }
            }
        }
        
        return "P" + String.format("%03d", maxNumber + 1);
    }
    
    private void loadLoaiPhongVaoComboBox() {
        if (cboMaLoaiPhong != null) {
            cboMaLoaiPhong.removeAllItems();
            List<LoaiPhong> list = loaiPhongDAO.getAll();
            for (LoaiPhong lp : list) {
                cboMaLoaiPhong.addItem(lp.getMaLoaiPhong() + " - " + lp.getTenLoaiPhong());
            }
        }
    }
    
    private boolean validateForm() {
        if (txtMaPhong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã phòng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMaPhong.requestFocus();
            return false;
        }
        
        if (cboMaLoaiPhong.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (txtTenPhong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên phòng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenPhong.requestFocus();
            return false;
        }
        
        if (txtTang.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tầng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTang.requestFocus();
            return false;
        }
        
        try {
            int tang = Integer.parseInt(txtTang.getText().trim());
            if (tang < 1 || tang > 50) {
                JOptionPane.showMessageDialog(this, "Tầng phải từ 1 đến 50!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtTang.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tầng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTang.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void luuPhongMoi() {
        try {
            Phong p = new Phong();
            p.setMaPhong(txtMaPhong.getText().trim());
            
            String selectedLoaiPhong = cboMaLoaiPhong.getSelectedItem().toString();
            String maLoaiPhong = selectedLoaiPhong.split(" - ")[0];
            p.setMaLoaiPhong(maLoaiPhong);
            
            p.setTenPhong(txtTenPhong.getText().trim());
            p.setTang(Integer.parseInt(txtTang.getText().trim()));
            p.setTrangThai(cboTrangThai.getSelectedItem().toString());
            p.setGhiChu(txtGhiChu.getText().trim());
            
            phongDAO.insert(p);
            JOptionPane.showMessageDialog(this, "Thêm phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void capNhatPhong() {
        try {
            Phong p = new Phong();
            p.setMaPhong(txtMaPhong.getText().trim());
            
            String selectedLoaiPhong = cboMaLoaiPhong.getSelectedItem().toString();
            String maLoaiPhong = selectedLoaiPhong.split(" - ")[0];
            p.setMaLoaiPhong(maLoaiPhong);
            
            p.setTenPhong(txtTenPhong.getText().trim());
            p.setTang(Integer.parseInt(txtTang.getText().trim()));
            p.setTrangThai(cboTrangThai.getSelectedItem().toString());
            p.setGhiChu(txtGhiChu.getText().trim());
            
            phongDAO.update(p);
            JOptionPane.showMessageDialog(this, "Cập nhật phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
