package view;

import dao.LoaiPhongDAO;
import model.LoaiPhong;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LoaiPhongFrame extends JFrame {
    private LoaiPhongDAO loaiPhongDAO;
    private DefaultTableModel tableModel;
    private JTable tblLoaiPhong;
    
    // Form fields
    private JTextField txtMaLoai, txtTenLoai, txtGiaGio, txtGiaNgay, txtSoNguoi;
    private JTextArea txtMoTa;
    
    public LoaiPhongFrame() {
        loaiPhongDAO = new LoaiPhongDAO();
        initComponents();
        loadData();
        lamMoiForm(); // Tạo mã tự động ban đầu
    }
    
    private void initComponents() {
        setTitle("QUẢN LÝ LOẠI PHÒNG");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel với BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // === NORTH: Tìm kiếm ===
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northPanel.add(new JLabel("Tìm kiếm theo tên:"));
        JTextField txtTimKiem = new JTextField(20);
        northPanel.add(txtTimKiem);
        
        JButton btnTimKiem = new JButton("Tìm");
        northPanel.add(btnTimKiem);
        
        JButton btnTaiLai = new JButton("Tải lại");
        northPanel.add(btnTaiLai);
        
        JButton btnThongKe = new JButton("Thống kê");
        northPanel.add(btnThongKe);
        
        mainPanel.add(northPanel, BorderLayout.NORTH);
        
        // === CENTER: Bảng dữ liệu ===
        String[] columns = {"Mã loại", "Tên loại phòng", "Giá theo giờ", "Giá theo ngày", "Số người tối đa", "Mô tả"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblLoaiPhong = new JTable(tableModel);
        tblLoaiPhong.setRowHeight(25);
        tblLoaiPhong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Custom renderer cho cột giá
        tblLoaiPhong.getColumnModel().getColumn(2).setCellRenderer(new CurrencyRenderer());
        tblLoaiPhong.getColumnModel().getColumn(3).setCellRenderer(new CurrencyRenderer());
        
        // Tô màu các hàng
        tblLoaiPhong.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(240, 248, 255)); // Màu xanh nhạt
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblLoaiPhong);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // === EAST: Form nhập liệu ===
        JPanel eastPanel = new JPanel(new GridBagLayout());
        eastPanel.setBorder(BorderFactory.createTitledBorder("Thông tin loại phòng"));
        eastPanel.setPreferredSize(new Dimension(450, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Mã loại
        gbc.gridx = 0; gbc.gridy = row;
        eastPanel.add(new JLabel("Mã loại (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtMaLoai = new JTextField(15);
        txtMaLoai.setEditable(false);
        eastPanel.add(txtMaLoai, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        JButton btnTaoMa = new JButton("Tạo mã");
        btnTaoMa.setToolTipText("Tạo mã tự động");
        eastPanel.add(btnTaoMa, gbc);
        
        row++;
        // Tên loại
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("Tên loại (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtTenLoai = new JTextField(20);
        eastPanel.add(txtTenLoai, gbc);
        
        row++;
        // Giá theo giờ
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("Giá theo giờ (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtGiaGio = new JTextField(20);
        txtGiaGio.setText("50000");
        eastPanel.add(txtGiaGio, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("VNĐ/giờ"), gbc);
        
        row++;
        // Giá theo ngày
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("Giá theo ngày (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtGiaNgay = new JTextField(20);
        txtGiaNgay.setText("500000");
        eastPanel.add(txtGiaNgay, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("VNĐ/ngày"), gbc);
        
        row++;
        // Số người tối đa
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("Số người (*):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtSoNguoi = new JTextField(20);
        txtSoNguoi.setText("2");
        eastPanel.add(txtSoNguoi, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("người"), gbc);
        
        row++;
        // Mô tả
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        txtMoTa = new JTextArea(4, 20);
        txtMoTa.setLineWrap(true);
        txtMoTa.setText("Phòng tiêu chuẩn với đầy đủ tiện nghi");
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        eastPanel.add(scrollMoTa, gbc);
        
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
            } else {
                loadData();
            }
        });
        
        btnTaoMa.addActionListener(e -> taoMaTuDong());
        
        btnThem.addActionListener(e -> themLoaiPhong());
        
        btnSua.addActionListener(e -> suaLoaiPhong());
        
        btnXoa.addActionListener(e -> xoaLoaiPhong());
        
        btnLamMoi.addActionListener(e -> lamMoiForm());
        
        btnThongKe.addActionListener(e -> hienThiThongKe());
        
        btnXuatExcel.addActionListener(e -> xuatExcel());
        
        btnDong.addActionListener(e -> dispose());
        
        tblLoaiPhong.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiChiTiet();
            }
        });
        
        tblLoaiPhong.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    suaLoaiPhong();
                }
            }
        });
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<LoaiPhong> list = loaiPhongDAO.getAll();
        
        for (LoaiPhong lp : list) {
            Object[] row = {
                lp.getMaLoaiPhong(),
                lp.getTenLoaiPhong(),
                lp.getGiaTheoGio(),
                lp.getGiaTheoNgay(),
                lp.getSoNguoiToiDa(),
                lp.getMoTa()
            };
            tableModel.addRow(row);
        }
        
        setTitle("QUẢN LÝ LOẠI PHÒNG - Tổng: " + list.size() + " loại");
    }
    
    private void timKiem(String keyword) {
        tableModel.setRowCount(0);
        List<LoaiPhong> list = loaiPhongDAO.search(keyword);
        
        for (LoaiPhong lp : list) {
            Object[] row = {
                lp.getMaLoaiPhong(),
                lp.getTenLoaiPhong(),
                lp.getGiaTheoGio(),
                lp.getGiaTheoNgay(),
                lp.getSoNguoiToiDa(),
                lp.getMoTa()
            };
            tableModel.addRow(row);
        }
        
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy loại phòng nào!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void hienThiThongKe() {
        List<LoaiPhong> list = loaiPhongDAO.getAll();
        double giaGioTB = 0;
        double giaNgayTB = 0;
        int soNguoiTB = 0;
        
        if (!list.isEmpty()) {
            for (LoaiPhong lp : list) {
                giaGioTB += lp.getGiaTheoGio();
                giaNgayTB += lp.getGiaTheoNgay();
                soNguoiTB += lp.getSoNguoiToiDa();
            }
            
            giaGioTB /= list.size();
            giaNgayTB /= list.size();
            soNguoiTB /= list.size();
        }
        
        String thongKe = String.format(
            "THỐNG KÊ LOẠI PHÒNG\n" +
            "==================\n" +
            "Tổng số loại phòng: %d\n" +
            "Giá giờ trung bình: %,.0f VNĐ/giờ\n" +
            "Giá ngày trung bình: %,.0f VNĐ/ngày\n" +
            "Số người TB: %d người/loại\n\n" +
            "CÁC LOẠI PHÒNG:\n",
            list.size(), giaGioTB, giaNgayTB, soNguoiTB
        );
        
        for (LoaiPhong lp : list) {
            thongKe += String.format("- %s: %,.0f VNĐ/ngày\n", 
                lp.getTenLoaiPhong(), lp.getGiaTheoNgay());
        }
        
        JOptionPane.showMessageDialog(this, thongKe, "Thống kê loại phòng", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void taoMaTuDong() {
        List<LoaiPhong> list = loaiPhongDAO.getAll();
        int maxNumber = 0;
        
        for (LoaiPhong lp : list) {
            String maLoai = lp.getMaLoaiPhong();
            if (maLoai != null && maLoai.startsWith("LP")) {
                try {
                    String numberStr = maLoai.substring(2);
                    int num = Integer.parseInt(numberStr);
                    if (num > maxNumber) {
                        maxNumber = num;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua
                }
            }
        }
        
        txtMaLoai.setText("LP" + String.format("%03d", maxNumber + 1));
    }
    
    private void themLoaiPhong() {
        try {
            if (!validateForm()) {
                return;
            }
            
            LoaiPhong lp = new LoaiPhong();
            lp.setMaLoaiPhong(txtMaLoai.getText().trim());
            lp.setTenLoaiPhong(txtTenLoai.getText().trim());
            lp.setGiaTheoGio(Double.parseDouble(txtGiaGio.getText().trim()));
            lp.setGiaTheoNgay(Double.parseDouble(txtGiaNgay.getText().trim()));
            lp.setSoNguoiToiDa(Integer.parseInt(txtSoNguoi.getText().trim()));
            lp.setMoTa(txtMoTa.getText().trim());
            
            loaiPhongDAO.insert(lp);
            JOptionPane.showMessageDialog(this, "Thêm loại phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            lamMoiForm();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void suaLoaiPhong() {
        int selectedRow = tblLoaiPhong.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maLoaiTrongBang = tableModel.getValueAt(selectedRow, 0).toString();
        if (!maLoaiTrongBang.equals(txtMaLoai.getText().trim())) {
            hienThiChiTiet();
        }
        
        try {
            if (!validateForm()) {
                return;
            }
            
            LoaiPhong lp = new LoaiPhong();
            lp.setMaLoaiPhong(txtMaLoai.getText().trim());
            lp.setTenLoaiPhong(txtTenLoai.getText().trim());
            lp.setGiaTheoGio(Double.parseDouble(txtGiaGio.getText().trim()));
            lp.setGiaTheoNgay(Double.parseDouble(txtGiaNgay.getText().trim()));
            lp.setSoNguoiToiDa(Integer.parseInt(txtSoNguoi.getText().trim()));
            lp.setMoTa(txtMoTa.getText().trim());
            
            loaiPhongDAO.update(lp);
            JOptionPane.showMessageDialog(this, "Cập nhật loại phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xoaLoaiPhong() {
        int selectedRow = tblLoaiPhong.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maLoai = tableModel.getValueAt(selectedRow, 0).toString();
        String tenLoai = tableModel.getValueAt(selectedRow, 1).toString();
        double giaNgay = 0;
        try {
            Object giaObj = tableModel.getValueAt(selectedRow, 3);
            if (giaObj instanceof Number) {
                giaNgay = ((Number)giaObj).doubleValue();
            } else if (giaObj instanceof String) {
                String giaStr = ((String)giaObj).replaceAll("[^0-9]", "");
                if (!giaStr.isEmpty()) {
                    giaNgay = Double.parseDouble(giaStr);
                }
            }
        } catch (Exception e) {
            giaNgay = 0;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa loại phòng:\n" +
            "Tên: " + tenLoai + "\n" +
            "Mã: " + maLoai + "\n" +
            "Giá ngày: " + String.format("%,.0f VNĐ", giaNgay) + "\n\n" +
            "(Lưu ý: Không thể xóa nếu có phòng đang sử dụng loại này)", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                loaiPhongDAO.delete(maLoai);
                JOptionPane.showMessageDialog(this, "Xóa loại phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                lamMoiForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Không thể xóa loại phòng này!\n" +
                    "Có thể có phòng đang sử dụng loại phòng này.", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void hienThiChiTiet() {
        int selectedRow = tblLoaiPhong.getSelectedRow();
        if (selectedRow == -1) return;
        
        try {
            txtMaLoai.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenLoai.setText(tableModel.getValueAt(selectedRow, 1).toString());
            
            Object giaGioObj = tableModel.getValueAt(selectedRow, 2);
            Object giaNgayObj = tableModel.getValueAt(selectedRow, 3);
            
            if (giaGioObj instanceof Number) {
                txtGiaGio.setText(String.valueOf(((Number)giaGioObj).doubleValue()));
            } else if (giaGioObj instanceof String) {
                String giaStr = ((String)giaGioObj).replaceAll("[^0-9]", "");
                txtGiaGio.setText(giaStr);
            }
            
            if (giaNgayObj instanceof Number) {
                txtGiaNgay.setText(String.valueOf(((Number)giaNgayObj).doubleValue()));
            } else if (giaNgayObj instanceof String) {
                String giaStr = ((String)giaNgayObj).replaceAll("[^0-9]", "");
                txtGiaNgay.setText(giaStr);
            }
            
            txtSoNguoi.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtMoTa.setText(tableModel.getValueAt(selectedRow, 5).toString());
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void lamMoiForm() {
        taoMaTuDong();
        txtTenLoai.setText("");
        txtGiaGio.setText("50000");
        txtGiaNgay.setText("500000");
        txtSoNguoi.setText("2");
        txtMoTa.setText("Phòng tiêu chuẩn với đầy đủ tiện nghi");
        txtTenLoai.requestFocus();
        tblLoaiPhong.clearSelection();
    }
    
    private void xuatExcel() {
        int soLuong = tableModel.getRowCount();
        if (soLuong == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, 
            "Chức năng xuất Excel đang phát triển...\n" +
            "Số lượng loại phòng: " + soLuong + "\n" +
            "Dữ liệu sẽ được xuất ra file: LoaiPhong_" + System.currentTimeMillis() + ".xlsx", 
            "Thông báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private boolean validateForm() {
        if (txtTenLoai.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenLoai.requestFocus();
            return false;
        }
        
        if (txtGiaGio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá theo giờ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaGio.requestFocus();
            return false;
        }
        
        try {
            double giaGio = Double.parseDouble(txtGiaGio.getText().trim());
            if (giaGio <= 0) {
                JOptionPane.showMessageDialog(this, "Giá theo giờ phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtGiaGio.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá theo giờ phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaGio.requestFocus();
            return false;
        }
        
        if (txtGiaNgay.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá theo ngày!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaNgay.requestFocus();
            return false;
        }
        
        try {
            double giaNgay = Double.parseDouble(txtGiaNgay.getText().trim());
            if (giaNgay <= 0) {
                JOptionPane.showMessageDialog(this, "Giá theo ngày phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtGiaNgay.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá theo ngày phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaNgay.requestFocus();
            return false;
        }
        
        if (txtSoNguoi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số người tối đa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoNguoi.requestFocus();
            return false;
        }
        
        try {
            int soNguoi = Integer.parseInt(txtSoNguoi.getText().trim());
            if (soNguoi <= 0) {
                JOptionPane.showMessageDialog(this, "Số người tối đa phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSoNguoi.requestFocus();
                return false;
            }
            if (soNguoi > 10) {
                JOptionPane.showMessageDialog(this, "Số người tối đa không được vượt quá 10!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSoNguoi.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số người tối đa phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoNguoi.requestFocus();
            return false;
        }
        
        try {
            double giaGio = Double.parseDouble(txtGiaGio.getText().trim());
            double giaNgay = Double.parseDouble(txtGiaNgay.getText().trim());
            if (giaNgay < giaGio * 24) {
                int option = JOptionPane.showConfirmDialog(this, 
                    "Giá theo ngày (" + String.format("%,.0f", giaNgay) + ") thấp hơn giá theo giờ x 24 (" + 
                    String.format("%,.0f", giaGio * 24) + ").\nBạn có muốn tiếp tục?", 
                    "Cảnh báo", 
                    JOptionPane.YES_NO_OPTION);
                return option == JOptionPane.YES_OPTION;
            }
        } catch (Exception e) {
            // Bỏ qua
        }
        
        return true;
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
