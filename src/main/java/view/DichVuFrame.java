package view;

import dao.DichVuDAO;
import model.DichVu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DichVuFrame extends JFrame {
    private DichVuDAO dichVuDAO;
    private DefaultTableModel tableModel;
    private JTable tblDichVu;

    // Form fields
    private JTextField txtMaDV, txtTenDV, txtDonGia, txtDonVi;
    private JTextArea txtMoTa;

    public DichVuFrame() {
        dichVuDAO = new DichVuDAO();
        initComponents();
        loadData();
        lamMoiForm(); // Tạo mã tự động ban đầu
    }

    private void initComponents() {
        setTitle("QUẢN LÝ DỊCH VỤ");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Không can thiệp UIManager, giữ nguyên Look&Feel mặc định của hệ thống

        // Main panel với BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === NORTH: Tìm kiếm ===
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northPanel.add(new JLabel("Tìm kiếm (tên hoặc mã):"));
        JTextField txtTimKiem = new JTextField(20);
        northPanel.add(txtTimKiem);

        JButton btnTimKiem = createButton("Tìm");
        northPanel.add(btnTimKiem);

        JButton btnTaiLai = createButton("Tải lại");
        northPanel.add(btnTaiLai);

        JButton btnThongKe = createButton("Thống kê");
        northPanel.add(btnThongKe);

        mainPanel.add(northPanel, BorderLayout.NORTH);

        // === CENTER: Bảng dữ liệu ===
        String[] columns = {"Mã DV", "Tên DV", "Đơn giá", "Đơn vị", "Mô tả"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblDichVu = new JTable(tableModel);
        tblDichVu.setRowHeight(25);
        tblDichVu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDichVu.setFillsViewportHeight(true);

        // Không đổi màu header, chỉ tắt reorder
        tblDichVu.getTableHeader().setReorderingAllowed(false);

        // Renderer cho cột giá
        tblDichVu.getColumnModel().getColumn(2).setCellRenderer(new CurrencyRenderer());

        // Tô màu các hàng xen kẽ
        tblDichVu.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(240, 248, 255)); // xanh rất nhạt
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblDichVu);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // === EAST: Form nhập liệu ===
        JPanel eastPanel = new JPanel(new GridBagLayout());
        eastPanel.setBorder(BorderFactory.createTitledBorder("Thông tin dịch vụ"));
        eastPanel.setPreferredSize(new Dimension(450, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Mã DV (tự động sinh)
        gbc.gridx = 0;
        gbc.gridy = row;
        eastPanel.add(new JLabel("Mã DV (*):"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtMaDV = new JTextField(15);
        txtMaDV.setEditable(false);
        eastPanel.add(txtMaDV, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        JButton btnTaoMa = createButton("Tạo mã");
        btnTaoMa.setToolTipText("Tạo mã tự động");
        eastPanel.add(btnTaoMa, gbc);

        row++;
        // Tên DV
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("Tên DV (*):"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtTenDV = new JTextField(20);
        eastPanel.add(txtTenDV, gbc);

        row++;
        // Đơn giá
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("Đơn giá (*):"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtDonGia = new JTextField(20);
        txtDonGia.setText("50000");
        eastPanel.add(txtDonGia, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("VNĐ"), gbc);

        row++;
        // Đơn vị
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        eastPanel.add(new JLabel("Đơn vị (*):"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtDonVi = new JTextField(20);
        txtDonVi.setText("lần");
        eastPanel.add(txtDonVi, gbc);

        row++;
        // Mô tả
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        eastPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        txtMoTa = new JTextArea(4, 20);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setText("Dịch vụ tiêu chuẩn");
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        eastPanel.add(scrollMoTa, gbc);

        mainPanel.add(eastPanel, BorderLayout.EAST);

        // === SOUTH: Các nút chức năng ===
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnThem      = createButton("Thêm mới");
        JButton btnSua       = createButton("Cập nhật");
        JButton btnXoa       = createButton("Xóa");
        JButton btnLamMoi    = createButton("Làm mới");
        JButton btnXuatExcel = createButton("Xuất Excel");
        JButton btnDong      = createButton("Đóng");

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

        btnThem.addActionListener(e -> themDichVu());

        btnSua.addActionListener(e -> suaDichVu());

        btnXoa.addActionListener(e -> xoaDichVu());

        btnLamMoi.addActionListener(e -> lamMoiForm());

        btnThongKe.addActionListener(e -> hienThiThongKe());

        btnXuatExcel.addActionListener(e -> xuatExcel());

        btnDong.addActionListener(e -> dispose());

        tblDichVu.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiChiTiet();
            }
        });

        tblDichVu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    suaDichVu();
                }
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<DichVu> list = dichVuDAO.getAll();

        for (DichVu dv : list) {
            Object[] row = {
                dv.getMaDV(),
                dv.getTenDV(),
                dv.getDonGia(),
                dv.getDonVi(),
                dv.getMoTa()
            };
            tableModel.addRow(row);
        }

        setTitle("QUẢN LÝ DỊCH VỤ - Tổng: " + list.size() + " dịch vụ");
    }

    private void timKiem(String keyword) {
        tableModel.setRowCount(0);
        List<DichVu> list = dichVuDAO.search(keyword);

        for (DichVu dv : list) {
            Object[] row = {
                dv.getMaDV(),
                dv.getTenDV(),
                dv.getDonGia(),
                dv.getDonVi(),
                dv.getMoTa()
            };
            tableModel.addRow(row);
        }

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy dịch vụ nào!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void hienThiThongKe() {
        List<DichVu> list = dichVuDAO.getAll();
        double tongGiaTri = 0;
        double giaTB = 0;

        if (!list.isEmpty()) {
            for (DichVu dv : list) {
                tongGiaTri += dv.getDonGia();
            }
            giaTB = tongGiaTri / list.size();
        }

        String thongKe = String.format(
            "THỐNG KÊ DỊCH VỤ\n" +
            "================\n" +
            "Tổng số dịch vụ: %d\n" +
            "Tổng giá trị: %,.0f VNĐ\n" +
            "Giá trung bình: %,.0f VNĐ\n\n" +
            "CÁC DỊCH VỤ PHỔ BIẾN:\n",
            list.size(), tongGiaTri, giaTB
        );

        int count = Math.min(list.size(), 5);
        for (int i = 0; i < count; i++) {
            DichVu dv = list.get(i);
            thongKe += String.format("- %s: %,.0f VNĐ/%s\n",
                dv.getTenDV(), dv.getDonGia(), dv.getDonVi());
        }

        JOptionPane.showMessageDialog(this, thongKe, "Thống kê dịch vụ", JOptionPane.INFORMATION_MESSAGE);
    }

    private void taoMaTuDong() {
        txtMaDV.setText(dichVuDAO.getAutoId());
    }

    private void themDichVu() {
        try {
            if (!validateForm()) {
                return;
            }

            DichVu dv = new DichVu();
            dv.setMaDV(txtMaDV.getText().trim());
            dv.setTenDV(txtTenDV.getText().trim());
            dv.setDonGia(Double.parseDouble(txtDonGia.getText().trim()));
            dv.setDonVi(txtDonVi.getText().trim());
            dv.setMoTa(txtMoTa.getText().trim());

            dichVuDAO.insert(dv);
            JOptionPane.showMessageDialog(this, "Thêm dịch vụ thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            lamMoiForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void suaDichVu() {
        int selectedRow = tblDichVu.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maDVTrongBang = tableModel.getValueAt(selectedRow, 0).toString();
        if (!maDVTrongBang.equals(txtMaDV.getText().trim())) {
            hienThiChiTiet();
        }

        try {
            if (!validateForm()) {
                return;
            }

            DichVu dv = new DichVu();
            dv.setMaDV(txtMaDV.getText().trim());
            dv.setTenDV(txtTenDV.getText().trim());
            dv.setDonGia(Double.parseDouble(txtDonGia.getText().trim()));
            dv.setDonVi(txtDonVi.getText().trim());
            dv.setMoTa(txtMoTa.getText().trim());

            dichVuDAO.update(dv);
            JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadData();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaDichVu() {
        int selectedRow = tblDichVu.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maDV = tableModel.getValueAt(selectedRow, 0).toString();
        String tenDV = tableModel.getValueAt(selectedRow, 1).toString();
        double donGia = 0;
        try {
            Object giaObj = tableModel.getValueAt(selectedRow, 2);
            if (giaObj instanceof Number) {
                donGia = ((Number) giaObj).doubleValue();
            } else if (giaObj instanceof String) {
                String giaStr = ((String) giaObj).replaceAll("[^0-9]", "");
                if (!giaStr.isEmpty()) {
                    donGia = Double.parseDouble(giaStr);
                }
            }
        } catch (Exception e) {
            donGia = 0;
        }
        String donVi = tableModel.getValueAt(selectedRow, 3).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa dịch vụ:\n" +
            "Tên: " + tenDV + "\n" +
            "Mã: " + maDV + "\n" +
            "Đơn giá: " + String.format("%,.0f VNĐ", donGia) + "/" + donVi + "\n\n" +
            "(Lưu ý: Không thể xóa nếu dịch vụ đang được sử dụng)",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dichVuDAO.delete(maDV);
                JOptionPane.showMessageDialog(this, "Xóa dịch vụ thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                lamMoiForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Không thể xóa dịch vụ này!\n" +
                    "Có thể dịch vụ đang được sử dụng trong hóa đơn.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void hienThiChiTiet() {
        int selectedRow = tblDichVu.getSelectedRow();
        if (selectedRow == -1) return;

        try {
            txtMaDV.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenDV.setText(tableModel.getValueAt(selectedRow, 1).toString());

            Object donGiaObj = tableModel.getValueAt(selectedRow, 2);
            if (donGiaObj instanceof Number) {
                txtDonGia.setText(String.valueOf(((Number) donGiaObj).doubleValue()));
            } else if (donGiaObj instanceof String) {
                String giaStr = ((String) donGiaObj).replaceAll("[^0-9]", "");
                txtDonGia.setText(giaStr);
            }

            txtDonVi.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtMoTa.setText(tableModel.getValueAt(selectedRow, 4).toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void lamMoiForm() {
        taoMaTuDong();
        txtTenDV.setText("");
        txtDonGia.setText("50000");
        txtDonVi.setText("lần");
        txtMoTa.setText("Dịch vụ tiêu chuẩn");
        txtTenDV.requestFocus();
        tblDichVu.clearSelection();
    }

    private void xuatExcel() {
        int soLuong = tableModel.getRowCount();
        if (soLuong == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
            "Chức năng xuất Excel đang phát triển...\n" +
            "Số lượng dịch vụ: " + soLuong + "\n" +
            "Dữ liệu sẽ được xuất ra file: DichVu_" + System.currentTimeMillis() + ".xlsx",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean validateForm() {
        if (txtTenDV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên dịch vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenDV.requestFocus();
            return false;
        }

        if (txtDonGia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đơn giá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDonGia.requestFocus();
            return false;
        }

        try {
            double donGia = Double.parseDouble(txtDonGia.getText().trim());
            if (donGia <= 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtDonGia.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDonGia.requestFocus();
            return false;
        }

        if (txtDonVi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đơn vị tính!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDonVi.requestFocus();
            return false;
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

    // Tạo nút: dùng đúng màu/mặc định của Look&Feel
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        // Không set background, foreground, font -> giữ hoàn toàn mặc định L&F
        btn.setFocusPainted(false);
        btn.setMargin(new Insets(6, 14, 6, 14));
        return btn;
    }
}
