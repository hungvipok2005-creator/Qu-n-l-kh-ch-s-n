package view;

import dao.*;
import model.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.List;

public class DatPhongFrame extends JFrame {
    private DatPhongDAO datPhongDAO;
    private KhachHangDAO khachHangDAO;
    private PhongDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;
    private DichVuDAO dichVuDAO;
    private ChiTietDichVuDAO chiTietDichVuDAO;
    private HoaDonDAO hoaDonDAO;
    
    private DefaultTableModel tableModel;
    private JTable tblDatPhong;
    private JTextField txtMaDatPhong, txtSoNguoi, txtTienCoc, txtTimKiem;
    private JComboBox<String> cboKhachHang, cboPhong, cboTrangThai;
    private JTextArea txtGhiChu;
    private JSpinner spnNgayNhan, spnNgayTra;
    
    private JLabel lblTienPhong, lblThoiGianThue;
    private JButton btnCheckIn, btnCheckOut, btnHuy, btnThemDV, btnSuaDV, btnXoaDV;
    private JComboBox<String> cboDichVu;
    private JSpinner spnSoLuongDV;
    private JTextField txtMaDP_DV;
    private DefaultTableModel dvTableModel;
    private JLabel lblTongDV;
    private String maCTDVSelected;
    
    public DatPhongFrame() {
        datPhongDAO = new DatPhongDAO();
        khachHangDAO = new KhachHangDAO();
        phongDAO = new PhongDAO();
        loaiPhongDAO = new LoaiPhongDAO();
        dichVuDAO = new DichVuDAO();
        chiTietDichVuDAO = new ChiTietDichVuDAO();
        hoaDonDAO = new HoaDonDAO();
        
        initComponents();
        loadData();
        loadComboboxData();
        resetForm();
    }
    
    private void initComponents() {
        setTitle("QUẢN LÝ ĐẶT PHÒNG");
        setSize(1400, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel mainPanel = createMainPanel();
        JPanel servicePanel = createServicePanel();
        
        tabbedPane.addTab("Đặt Phòng", mainPanel);
        tabbedPane.addTab("Dịch Vụ ", servicePanel);
        
        add(tabbedPane);
        txtMaDatPhong.setText(generateAutoMaDP());
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitle = new JLabel("QUẢN LÝ ĐẶT PHÒNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        String[] columns = {"Mã ĐP", "Mã KH", "Tên KH", "Mã Phòng", "Ngày nhận", "Ngày trả", 
                           "Số người", "Trạng thái", "Tiền cọc"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tblDatPhong = new JTable(tableModel);
        tblDatPhong.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tblDatPhong);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel formPanel = new JPanel(new GridLayout(13, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đặt phòng"));
        
        formPanel.add(new JLabel("Mã đặt phòng:"));
        txtMaDatPhong = new JTextField();
        txtMaDatPhong.setEditable(false);
        formPanel.add(txtMaDatPhong);
        
        formPanel.add(new JLabel("Khách hàng:"));     
        cboKhachHang = new JComboBox<>();
        formPanel.add(cboKhachHang);
        
        formPanel.add(new JLabel("Phòng:"));
        cboPhong = new JComboBox<>();
        formPanel.add(cboPhong);
        
        formPanel.add(new JLabel("Ngày nhận:"));
        spnNgayNhan = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY));
        JSpinner.DateEditor dateEditorNhan = new JSpinner.DateEditor(spnNgayNhan, "dd/MM/yyyy HH:mm");
        spnNgayNhan.setEditor(dateEditorNhan);
        formPanel.add(spnNgayNhan);
        
        formPanel.add(new JLabel("Ngày trả:"));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 24);
        spnNgayTra = new JSpinner(new SpinnerDateModel(cal.getTime(), null, null, Calendar.HOUR_OF_DAY));
        JSpinner.DateEditor dateEditorTra = new JSpinner.DateEditor(spnNgayTra, "dd/MM/yyyy HH:mm");
        spnNgayTra.setEditor(dateEditorTra);
        formPanel.add(spnNgayTra);
        
        formPanel.add(new JLabel("Số người:"));
        txtSoNguoi = new JTextField("1");
        formPanel.add(txtSoNguoi);
        
        formPanel.add(new JLabel("Trạng thái:"));
        cboTrangThai = new JComboBox<>(new String[]{"Đã đặt", "Đang sử dụng", "Đã trả phòng", "Đã hủy"});
        formPanel.add(cboTrangThai);
        
        formPanel.add(new JLabel("Tiền cọc:"));
        txtTienCoc = new JTextField("0");
        formPanel.add(txtTienCoc);
        
        formPanel.add(new JLabel("Thời gian thuê:"));
        lblThoiGianThue = new JLabel("1 ngày 0 giờ");
        lblThoiGianThue.setForeground(Color.BLACK);
        formPanel.add(lblThoiGianThue);
        
        formPanel.add(new JLabel("Tiền phòng ước tính:"));
        lblTienPhong = new JLabel("0 VNĐ");
        lblTienPhong.setForeground(Color.BLACK);
        formPanel.add(lblTienPhong);
        
        formPanel.add(new JLabel("Ghi chú:"));
        txtGhiChu = new JTextArea(2, 20);
        formPanel.add(new JScrollPane(txtGhiChu));
        
       
   
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        btnCheckIn = new JButton("Check-in");
        btnCheckOut = new JButton("Check-out");
        btnHuy = new JButton("Hủy đặt");
        JButton btnReset = new JButton("Làm mới");
        JButton btnTim = new JButton("Tìm");
        
        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnCheckIn);
        buttonPanel.add(btnCheckOut);
        buttonPanel.add(btnHuy);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnTim);
        
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.add(new JLabel("Tìm kiếm (Mã ĐP, Tên KH, SĐT):"), BorderLayout.WEST);
        txtTimKiem = new JTextField();
        searchPanel.add(txtTimKiem, BorderLayout.CENTER);
        searchPanel.add(btnTim, BorderLayout.EAST);
        
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel southPanel = new JPanel(new BorderLayout(5, 5));
        southPanel.add(searchPanel, BorderLayout.CENTER);
        
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        btnThem.addActionListener(e -> themDatPhong());
        btnSua.addActionListener(e -> suaDatPhong());
        btnXoa.addActionListener(e -> xoaDatPhong());
        btnReset.addActionListener(e -> resetForm());
        btnTim.addActionListener(e -> timKiem());
        btnCheckIn.addActionListener(e -> checkIn());
        btnCheckOut.addActionListener(e -> checkOut());
        btnHuy.addActionListener(e -> huyDatPhong());
        txtTimKiem.addActionListener(e -> timKiem());
        
        spnNgayNhan.addChangeListener(e -> tinhToanThoiGianVaTien());
        spnNgayTra.addChangeListener(e -> tinhToanThoiGianVaTien());
        cboPhong.addActionListener(e -> tinhToanThoiGianVaTien());
        
        tblDatPhong.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) hienThiChiTiet();
        });
        
        return mainPanel;
    }
    
    private JPanel createServicePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitle = new JLabel("QUẢN LÝ DỊCH VỤ PHÒNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitle, BorderLayout.NORTH);
        
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Thông tin dịch vụ"));
        leftPanel.setPreferredSize(new Dimension(450, 350));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        leftPanel.add(new JLabel("Mã đặt phòng:"), gbc);
        gbc.gridx = 1;
        txtMaDP_DV = new JTextField();
        txtMaDP_DV.setEditable(false);
        leftPanel.add(txtMaDP_DV, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        leftPanel.add(new JLabel("Dịch vụ:"), gbc);
        gbc.gridx = 1;
        cboDichVu = new JComboBox<>();
        leftPanel.add(cboDichVu, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        leftPanel.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1;
        spnSoLuongDV = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        leftPanel.add(spnSoLuongDV, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel btnDichVuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnThemDV = new JButton("Thêm dịch vụ");
        btnSuaDV = new JButton("Sửa");
        btnXoaDV = new JButton("Xóa");
        JButton btnResetDV = new JButton("Làm mới");
        
        btnThemDV.setPreferredSize(new Dimension(120, 30));
        btnSuaDV.setPreferredSize(new Dimension(80, 30));
        btnXoaDV.setPreferredSize(new Dimension(80, 30));
        btnResetDV.setPreferredSize(new Dimension(100, 30));
        
        btnSuaDV.setEnabled(false);
        btnXoaDV.setEnabled(false);
        
        btnDichVuPanel.add(btnThemDV);
        btnDichVuPanel.add(btnSuaDV);
        btnDichVuPanel.add(btnXoaDV);
        btnDichVuPanel.add(btnResetDV);
        leftPanel.add(btnDichVuPanel, gbc);
        
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Dịch vụ đã sử dụng"));
        
        String[] dvColumns = {"STT", "Dịch vụ", "Số lượng", "Đơn giá", "Thành tiền", "Ngày sử dụng", "Mã CTDV"};
        dvTableModel = new DefaultTableModel(dvColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable tblDichVu = new JTable(dvTableModel);
        tblDichVu.setRowHeight(25);
        
        // Ẩn cột mã CTDV
        tblDichVu.removeColumn(tblDichVu.getColumnModel().getColumn(6));
        
        JScrollPane scrollPane = new JScrollPane(tblDichVu);
        
        lblTongDV = new JLabel("Tổng tiền dịch vụ: 0 VNĐ");
        lblTongDV.setFont(new Font("Arial", Font.BOLD, 12));
        lblTongDV.setForeground(Color.BLUE);
        
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(lblTongDV, BorderLayout.SOUTH);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        loadDichVuData();
        
        tblDatPhong.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblDatPhong.getSelectedRow();
                if (selectedRow >= 0) {
                    String maDP = tableModel.getValueAt(selectedRow, 0).toString();
                    txtMaDP_DV.setText(maDP);
                    loadDichVuDaSuDung(maDP);
                    resetFormDichVu();
                }
            }
        });
        
        // Sự kiện khi chọn dòng trong bảng dịch vụ
        tblDichVu.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblDichVu.getSelectedRow();
                if (selectedRow >= 0) {
                    hienThiChiTietDichVu(selectedRow);
                }
            }
        });
        
        btnThemDV.addActionListener(e -> {
            String maDP = txtMaDP_DV.getText();
            if (!maDP.isEmpty() && cboDichVu.getSelectedIndex() > 0) {
                themDichVuChoPhong(maDP);
                loadDichVuDaSuDung(maDP);
                JOptionPane.showMessageDialog(this, "Đã thêm dịch vụ thành công!");
                resetFormDichVu();
            } else {
                if (maDP.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn đặt phòng từ bảng danh sách!");
                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ!");
                }
            }
        });
        
        btnSuaDV.addActionListener(e -> {
            if (maCTDVSelected != null && !maCTDVSelected.isEmpty()) {
                suaDichVu();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần sửa!");
            }
        });
        
        btnXoaDV.addActionListener(e -> {
            if (maCTDVSelected != null && !maCTDVSelected.isEmpty()) {
                xoaDichVu();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần xóa!");
            }
        });
        
        btnResetDV.addActionListener(e -> {
            resetFormDichVu();
        });
        
        return panel;
    }
    
    private String generateAutoMaDP() {
        List<DatPhong> list = datPhongDAO.getAll();
        if (list.isEmpty()) {
            return "DP001";
        }
        
        int maxNumber = 0;
        for (DatPhong dp : list) {
            String maDP = dp.getMaDatPhong();
            if (maDP != null && maDP.startsWith("DP")) {
                try {
                    int number = Integer.parseInt(maDP.substring(2));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua nếu không phải định dạng số
                }
            }
        }
        
        String newNumber = String.format("%03d", maxNumber + 1);
        return "DP" + newNumber;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<DatPhong> list = datPhongDAO.getAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (DatPhong dp : list) {
            String tenKH = "";
            KhachHang kh = khachHangDAO.getById(dp.getMaKH());
            if (kh != null) tenKH = kh.getTenKH();
            
            tableModel.addRow(new Object[]{
                dp.getMaDatPhong(),
                dp.getMaKH(),
                tenKH,
                dp.getMaPhong(),
                
                dp.getSoNguoi(),dp.getNgayNhanPhong() != null ? sdf.format(dp.getNgayNhanPhong()) : "",
                dp.getNgayTraPhong() != null ? sdf.format(dp.getNgayTraPhong()) : "",
                dp.getTrangThai(),
                String.format("%,.0f", dp.getTienCoc())
            });
        }
    }
    
    private void loadComboboxData() {
        cboKhachHang.removeAllItems();
        cboKhachHang.addItem("-- Chọn khách hàng --");
        List<KhachHang> dsKH = khachHangDAO.getAll();
        for (KhachHang kh : dsKH) {
            cboKhachHang.addItem(kh.getMaKH() + " - " + kh.getTenKH() + " (" + kh.getSoDienThoai() + ")");
        }
        
        cboPhong.removeAllItems();
        cboPhong.addItem("-- Chọn phòng --");
        List<Phong> dsPhong = phongDAO.getAll();
        for (Phong p : dsPhong) {
            String status = p.getTrangThai();
            if ("Trống".equals(status)) {
                LoaiPhong lp = loaiPhongDAO.getById(p.getMaLoaiPhong());
                if (lp != null) {
                    cboPhong.addItem(p.getMaPhong() + " - " + p.getTenPhong() + 
                                   " (" + lp.getTenLoaiPhong() + " - " + 
                                   String.format("%,.0f", lp.getGiaTheoNgay()) + "/ngày)");
                } else {
                    cboPhong.addItem(p.getMaPhong() + " - " + p.getTenPhong() + " (Trống)");
                }
            }
        }
    }
    
    private void loadDichVuData() {
        cboDichVu.removeAllItems();
        cboDichVu.addItem("-- Chọn dịch vụ --");
        List<DichVu> dsDV = dichVuDAO.getAll();
        for (DichVu dv : dsDV) {
            cboDichVu.addItem(dv.getMaDV() + " - " + dv.getTenDV() + " (" + String.format("%,.0f", dv.getDonGia()) + ")");
        }
    }
    
    private void tinhToanThoiGianVaTien() {
        try {
            if (cboPhong.getSelectedIndex() <= 0) return;
            
            String maPhong = cboPhong.getSelectedItem().toString().split(" - ")[0];
            Phong p = phongDAO.getById(maPhong);
            if (p == null) return;
            
            LoaiPhong lp = loaiPhongDAO.getById(p.getMaLoaiPhong());
            if (lp == null) return;
            
            Date ngayNhan = (Date) spnNgayNhan.getValue();
            Date ngayTra = (Date) spnNgayTra.getValue();
            
            if (ngayNhan != null && ngayTra != null && ngayTra.after(ngayNhan)) {
                long diff = ngayTra.getTime() - ngayNhan.getTime();
                long hours = diff / (1000 * 60 * 60);
                long days = hours / 24;
                hours = hours % 24;
                
                double tienPhong = days * lp.getGiaTheoNgay();
                if (hours > 0) {
                    tienPhong += lp.getGiaTheoNgay();
                }
                if (tienPhong == 0 && hours > 0) {
                    tienPhong = lp.getGiaTheoNgay();
                }
                
                lblThoiGianThue.setText(String.format("%d ngày %d giờ", days, hours));
                lblTienPhong.setText(String.format("%,.0f VNĐ", tienPhong));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void themDatPhong() {
        try {
            if (cboKhachHang.getSelectedIndex() == 0 || cboPhong.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng và phòng!");
                return;
            }

            String maKH = cboKhachHang.getSelectedItem().toString().split(" - ")[0];
            String maPhong = cboPhong.getSelectedItem().toString().split(" - ")[0];
            String maNV = "NV001";

            Date ngayNhan = (Date) spnNgayNhan.getValue();
            Date ngayTra = (Date) spnNgayTra.getValue();

            if (ngayTra.before(ngayNhan)) {
                JOptionPane.showMessageDialog(this, "Ngày trả phải sau ngày nhận!");
                return;
            }

            int soNguoiNhap;
            try {
                soNguoiNhap = Integer.parseInt(txtSoNguoi.getText().trim());
                if (soNguoiNhap <= 0) {
                    JOptionPane.showMessageDialog(this, "Số người phải lớn hơn 0!");
                    txtSoNguoi.requestFocus();
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số người phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSoNguoi.requestFocus();
                return;
            }

            int soNguoiToiDa = phongDAO.getSoNguoiToiDaByMaPhong(maPhong);
            if (soNguoiToiDa <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Không lấy được số người tối đa của phòng!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (soNguoiNhap > soNguoiToiDa) {
                JOptionPane.showMessageDialog(this,
                        "Số người ở (" + soNguoiNhap + ") vượt quá số người tối đa của phòng (" + soNguoiToiDa + ")!",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                txtSoNguoi.requestFocus();
                return;
            }

            DatPhong dp = new DatPhong();
            dp.setMaDatPhong(txtMaDatPhong.getText().trim());
            dp.setMaKH(maKH);
            dp.setMaPhong(maPhong);
            dp.setMaNV(maNV);
            dp.setNgayDat(new Date());
            dp.setNgayNhanPhong(ngayNhan);
            dp.setNgayTraPhong(ngayTra);
            dp.setSoNguoi(soNguoiNhap);
            dp.setTrangThai(cboTrangThai.getSelectedItem().toString());
            dp.setTienCoc(Double.parseDouble(txtTienCoc.getText().trim().replace(",", "")));
            dp.setGhiChu(txtGhiChu.getText().trim());

            datPhongDAO.insert(dp);

            Phong p = phongDAO.getById(maPhong);
            if (p != null) {
                p.setTrangThai("Đặt");
                phongDAO.update(p);
            }

            JOptionPane.showMessageDialog(this, "Đặt phòng thành công!");
            loadData();
            loadComboboxData();
            resetForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void suaDatPhong() {
        int row = tblDatPhong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đặt phòng cần sửa!");
            return;
        }
        
        try {
            DatPhong dp = new DatPhong();
            dp.setMaDatPhong(txtMaDatPhong.getText().trim());
            dp.setMaKH(cboKhachHang.getSelectedItem().toString().split(" - ")[0]);
            dp.setMaPhong(cboPhong.getSelectedItem().toString().split(" - ")[0]);
            dp.setMaNV("NV001");
            dp.setNgayNhanPhong((Date) spnNgayNhan.getValue());
            dp.setNgayTraPhong((Date) spnNgayTra.getValue());
            dp.setSoNguoi(Integer.parseInt(txtSoNguoi.getText().trim()));
            dp.setTrangThai(cboTrangThai.getSelectedItem().toString());
            dp.setTienCoc(Double.parseDouble(txtTienCoc.getText().trim().replace(",", "")));
            dp.setGhiChu(txtGhiChu.getText().trim());
            
            datPhongDAO.update(dp);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadData();
            loadComboboxData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    private void xoaDatPhong() {
        int row = tblDatPhong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đặt phòng cần xóa!");
            return;
        }
        
        String maDP = tableModel.getValueAt(row, 0).toString();
        String maPhong = tableModel.getValueAt(row, 3).toString();
        String trangThai = tableModel.getValueAt(row, 7).toString();
        
        if ("Đang sử dụng".equals(trangThai)) {
            JOptionPane.showMessageDialog(this, "Không thể xóa đặt phòng đang sử dụng!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Xóa đặt phòng " + maDP + "?\nTất cả chi tiết dịch vụ liên quan cũng sẽ bị xóa!", 
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                chiTietDichVuDAO.deleteByMaDatPhong(maDP);
                hoaDonDAO.deleteByMaDatPhong(maDP);
                datPhongDAO.delete(maDP);
                
                Phong p = phongDAO.getById(maPhong);
                if (p != null && "Đã đặt".equals(p.getTrangThai())) {
                    p.setTrangThai("Trống");
                    phongDAO.update(p);
                }
                
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                loadComboboxData();
                resetForm();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Không thể xóa: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    private void timKiem() {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        
        tableModel.setRowCount(0);
        List<KhachHang> dsKH = khachHangDAO.search(keyword);
        Set<String> maKHs = new HashSet<>();
        for (KhachHang kh : dsKH) {
            maKHs.add(kh.getMaKH());
        }
        
        List<DatPhong> allBookings = datPhongDAO.getAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (DatPhong dp : allBookings) {
            boolean match = dp.getMaDatPhong().toLowerCase().contains(keyword.toLowerCase()) ||
                           dp.getMaPhong().toLowerCase().contains(keyword.toLowerCase()) ||
                           maKHs.contains(dp.getMaKH());
            
            if (match) {
                String tenKH = "";
                KhachHang kh = khachHangDAO.getById(dp.getMaKH());
                if (kh != null) tenKH = kh.getTenKH();
                
                tableModel.addRow(new Object[]{
                    dp.getMaDatPhong(),
                    dp.getMaKH(),
                    tenKH,
                    dp.getMaPhong(),
                    dp.getNgayNhanPhong() != null ? sdf.format(dp.getNgayNhanPhong()) : "",
                    dp.getNgayTraPhong() != null ? sdf.format(dp.getNgayTraPhong()) : "",
                    dp.getSoNguoi(),
                    dp.getTrangThai(),
                    String.format("%,.0f", dp.getTienCoc())
                });
            }
        }
    }
    
    private void hienThiChiTiet() {
        int row = tblDatPhong.getSelectedRow();
        if (row == -1) return;
        
        try {
            txtMaDatPhong.setText(tableModel.getValueAt(row, 0).toString());
            
            String maKH = tableModel.getValueAt(row, 1).toString();
            for (int i = 0; i < cboKhachHang.getItemCount(); i++) {
                String item = cboKhachHang.getItemAt(i).toString();
                if (item.startsWith(maKH + " - ")) {
                    cboKhachHang.setSelectedIndex(i);
                    break;
                }
            }
            
            String maPhong = tableModel.getValueAt(row, 3).toString();
            Phong p = phongDAO.getById(maPhong);
            if (p != null) {
                cboPhong.removeAllItems();
                cboPhong.addItem("-- Chọn phòng --");
                List<Phong> dsPhong = phongDAO.getAll();
                for (Phong ph : dsPhong) {
                    String status = ph.getTrangThai();
                    if ("Trống".equals(status) || ph.getMaPhong().equals(maPhong)) {
                        LoaiPhong lp = loaiPhongDAO.getById(ph.getMaLoaiPhong());
                        if (lp != null) {
                            cboPhong.addItem(ph.getMaPhong() + " - " + ph.getTenPhong() + 
                                           " (" + lp.getTenLoaiPhong() + " - " + 
                                           String.format("%,.0f", lp.getGiaTheoNgay()) + "/ngày)");
                        } else {
                            cboPhong.addItem(ph.getMaPhong() + " - " + ph.getTenPhong() + 
                                           " (" + status + ")");
                        }
                    }
                }
                
                for (int i = 0; i < cboPhong.getItemCount(); i++) {
                    String item = cboPhong.getItemAt(i).toString();
                    if (item.startsWith(maPhong + " - ")) {
                        cboPhong.setSelectedIndex(i);
                        break;
                    }
                }
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            spnNgayNhan.setValue(sdf.parse(tableModel.getValueAt(row, 4).toString()));
            spnNgayTra.setValue(sdf.parse(tableModel.getValueAt(row, 5).toString()));
            txtSoNguoi.setText(tableModel.getValueAt(row, 6).toString());
            cboTrangThai.setSelectedItem(tableModel.getValueAt(row, 7).toString());
            txtTienCoc.setText(tableModel.getValueAt(row, 8).toString().replace(",", ""));
            
            DatPhong dp = datPhongDAO.getById(txtMaDatPhong.getText());
            if (dp != null) {
                txtGhiChu.setText(dp.getGhiChu() != null ? dp.getGhiChu() : "");
            }
            
            tinhToanThoiGianVaTien();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void resetForm() {
        txtMaDatPhong.setText(generateAutoMaDP());
        cboKhachHang.setSelectedIndex(0);
        cboPhong.setSelectedIndex(0);
        spnNgayNhan.setValue(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 24);
        spnNgayTra.setValue(cal.getTime());
        txtSoNguoi.setText("1");
        cboTrangThai.setSelectedIndex(0);
        txtTienCoc.setText("0");
        txtGhiChu.setText("");
        txtTimKiem.setText("");
        tblDatPhong.clearSelection();
        loadComboboxData();
        tinhToanThoiGianVaTien();
    }
    
    private void checkIn() {
        int row = tblDatPhong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đặt phòng cần check-in!");
            return;
        }
        
        String maDP = tableModel.getValueAt(row, 0).toString();
        String trangThai = tableModel.getValueAt(row, 7).toString();
        
        if (!"Đã đặt".equals(trangThai)) {
            JOptionPane.showMessageDialog(this, "Chỉ có thể check-in cho đặt phòng ở trạng thái 'Đã đặt'!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Check-in đặt phòng " + maDP + "?\nKhách sẽ nhận phòng ngay bây giờ.", 
            "Xác nhận Check-in", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                DatPhong dp = datPhongDAO.getById(maDP);
                if (dp != null) {
                    dp.setTrangThai("Đang sử dụng");
                    dp.setNgayNhanPhong(new Date());
                    datPhongDAO.update(dp);
                    
                    Phong p = phongDAO.getById(dp.getMaPhong());
                    if (p != null) {
                        p.setTrangThai("Đang sử dụng");
                        phongDAO.update(p);
                    }
                    
                    JOptionPane.showMessageDialog(this, "Check-in thành công!");
                    loadData();
                    loadComboboxData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }
    
    private void checkOut() {
        int row = tblDatPhong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đặt phòng cần check-out!");
            return;
        }
        
        String maDP = tableModel.getValueAt(row, 0).toString();
        String trangThai = tableModel.getValueAt(row, 7).toString();
        
        if (!"Đang sử dụng".equals(trangThai)) {
            JOptionPane.showMessageDialog(this, "Chỉ có thể check-out cho đặt phòng ở trạng thái 'Đang sử dụng'!");
            return;
        }
        
        DatPhong dp = datPhongDAO.getById(maDP);
        if (dp == null) return;
        
        double tienPhong = tinhTienPhongThucTe(dp);
        double tienDichVu = tinhTienDichVu(maDP);
        double tongTien = tienPhong + tienDichVu;
        double canTraThem = tongTien - dp.getTienCoc();
        
        String message = String.format(
            "Check-out đặt phòng %s?\n\n" +
            "Tiền phòng: %,.0f VNĐ\n" +
            "Tiền dịch vụ: %,.0f VNĐ\n" +
            "Tổng cộng: %,.0f VNĐ\n" +
            "Tiền cọc: %,.0f VNĐ\n" +
            "Cần trả thêm: %,.0f VNĐ\n\n" +
            "Sau khi check-out, phòng sẽ được chuyển sang trạng thái 'Trống'.",
            maDP, tienPhong, tienDichVu, tongTien, dp.getTienCoc(), canTraThem
        );
        
        int confirm = JOptionPane.showConfirmDialog(this, message, 
            "Xác nhận Check-out", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dp.setTrangThai("Đã trả phòng");
                dp.setNgayTraPhong(new Date());
                datPhongDAO.update(dp);
                
                Phong p = phongDAO.getById(dp.getMaPhong());
                if (p != null) {
                    p.setTrangThai("Trống");
                    phongDAO.update(p);
                }
                
                // Tự động tạo hóa đơn mà không hiển thị hộp thoại
                taoHoaDon(maDP, tienPhong, tienDichVu, dp.getTienCoc(), canTraThem);
                
                JOptionPane.showMessageDialog(this, "Check-out thành công và đã tạo hóa đơn!");
                loadData();
                loadComboboxData();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }
    
    private void huyDatPhong() {
        int row = tblDatPhong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đặt phòng cần hủy!");
            return;
        }
        
        String maDP = tableModel.getValueAt(row, 0).toString();
        String trangThai = tableModel.getValueAt(row, 7).toString();
        
        if (!"Đã đặt".equals(trangThai)) {
            JOptionPane.showMessageDialog(this, "Chỉ có thể hủy đặt phòng ở trạng thái 'Đã đặt'!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Hủy đặt phòng " + maDP + "?\nTiền cọc sẽ được hoàn trả cho khách.", 
            "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                DatPhong dp = datPhongDAO.getById(maDP);
                if (dp != null) {
                    dp.setTrangThai("Đã hủy");
                    datPhongDAO.update(dp);
                    
                    Phong p = phongDAO.getById(dp.getMaPhong());
                    if (p != null) {
                        p.setTrangThai("Trống");
                        phongDAO.update(p);
                    }
                    
                    JOptionPane.showMessageDialog(this, "Hủy đặt phòng thành công!");
                    loadData();
                    loadComboboxData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }
    
   
    
    private void loadDichVuDaSuDung(String maDP) {
        dvTableModel.setRowCount(0);
        List<ChiTietDichVu> dsDV = chiTietDichVuDAO.getByMaDatPhong(maDP);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        double tongTienDV = 0;
        int stt = 1;
        for (ChiTietDichVu ctdv : dsDV) {
            DichVu dv = dichVuDAO.getById(ctdv.getMaDV());
            String tenDV = dv != null ? dv.getTenDV() : ctdv.getMaDV();
            
            dvTableModel.addRow(new Object[]{
                stt++,
                tenDV,
                ctdv.getSoLuong(),
                String.format("%,.0f", ctdv.getDonGia()),
                String.format("%,.0f", ctdv.getThanhTien()),
                ctdv.getNgaySuDung() != null ? sdf.format(ctdv.getNgaySuDung()) : "",
                ctdv.getMaChiTiet()// Lưu mã chi tiết để sửa/xóa
            });
            
            tongTienDV += ctdv.getThanhTien();
        }
        
        lblTongDV.setText(String.format("Tổng tiền dịch vụ: %,.0f VNĐ", tongTienDV));
    }
    
    private void hienThiChiTietDichVu(int row) {
        try {
            String maDVText = dvTableModel.getValueAt(row, 1).toString(); // Tên dịch vụ
            int soLuong = Integer.parseInt(dvTableModel.getValueAt(row, 2).toString().replace(",", ""));
            maCTDVSelected = dvTableModel.getValueAt(row, 6).toString(); // Mã chi tiết dịch vụ
            
            // Tìm mã dịch vụ từ tên dịch vụ
            String maDV = "";
            for (int i = 0; i < cboDichVu.getItemCount(); i++) {
                String item = cboDichVu.getItemAt(i).toString();
                if (item.contains(maDVText)) {
                    maDV = item.split(" - ")[0];
                    break;
                }
            }
            
            // Đặt giá trị cho combobox dịch vụ
            for (int i = 0; i < cboDichVu.getItemCount(); i++) {
                if (cboDichVu.getItemAt(i).toString().startsWith(maDV)) {
                    cboDichVu.setSelectedIndex(i);
                    break;
                }
            }
            
            spnSoLuongDV.setValue(soLuong);
            
            // Kích hoạt nút sửa và xóa
            btnSuaDV.setEnabled(true);
            btnXoaDV.setEnabled(true);
            btnThemDV.setText("Hủy chọn");
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void themDichVuChoPhong(String maDP) {
        try {
            if (cboDichVu.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ!");
                return;
            }
            
            String maDV = cboDichVu.getSelectedItem().toString().split(" - ")[0];
            DichVu dv = dichVuDAO.getById(maDV);
            if (dv == null) return;
            
            int soLuong = (Integer) spnSoLuongDV.getValue();
            
            ChiTietDichVu ctdv = new ChiTietDichVu();
            ctdv.setMaDatPhong(maDP);
            ctdv.setMaDV(maDV);
            ctdv.setSoLuong(soLuong);
            ctdv.setDonGia(dv.getDonGia());
            ctdv.setNgaySuDung(new Date());
            
            chiTietDichVuDAO.insert(ctdv);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    private void suaDichVu() {
        try {
            if (maCTDVSelected == null || maCTDVSelected.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần sửa!");
                return;
            }
            
            if (cboDichVu.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ!");
                return;
            }
            
            String maDV = cboDichVu.getSelectedItem().toString().split(" - ")[0];
            DichVu dv = dichVuDAO.getById(maDV);
            if (dv == null) return;
            
            int soLuong = (Integer) spnSoLuongDV.getValue();
            
            // Lấy chi tiết dịch vụ hiện tại
            ChiTietDichVu ctdv = chiTietDichVuDAO.getById(maCTDVSelected);
            if (ctdv == null) return;
            
            // Cập nhật thông tin
            ctdv.setMaDV(maDV);
            ctdv.setSoLuong(soLuong);
            ctdv.setDonGia(dv.getDonGia());
            ctdv.setNgaySuDung(new Date());
            
            chiTietDichVuDAO.update(ctdv);
            
            JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thành công!");
            loadDichVuDaSuDung(txtMaDP_DV.getText());
            resetFormDichVu();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    private void xoaDichVu() {
        try {
            if (maCTDVSelected == null || maCTDVSelected.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần xóa!");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Xóa dịch vụ này?", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                chiTietDichVuDAO.delete(maCTDVSelected);
                JOptionPane.showMessageDialog(this, "Xóa dịch vụ thành công!");
                loadDichVuDaSuDung(txtMaDP_DV.getText());
                resetFormDichVu();
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    private void resetFormDichVu() {
        cboDichVu.setSelectedIndex(0);
        spnSoLuongDV.setValue(1);
        maCTDVSelected = null;
        btnSuaDV.setEnabled(true);
        btnXoaDV.setEnabled(true);
        btnThemDV.setText("Thêm dịch vụ");
    }
    
    private double tinhTienPhongThucTe(DatPhong dp) {
        try {
            Phong p = phongDAO.getById(dp.getMaPhong());
            if (p == null) return 0;
            
            LoaiPhong lp = loaiPhongDAO.getById(p.getMaLoaiPhong());
            if (lp == null) return 0;
            
            Date ngayNhan = dp.getNgayNhanPhong();
            Date ngayTra = dp.getNgayTraPhong();
            
            if (ngayNhan != null && ngayTra != null && ngayTra.after(ngayNhan)) {
                long diff = ngayTra.getTime() - ngayNhan.getTime();
                long hours = diff / (1000 * 60 * 60);
                long days = hours / 24;
                hours = hours % 24;
                
                double tienPhong = days * lp.getGiaTheoNgay();
                if (hours > 0) {
                    tienPhong += lp.getGiaTheoNgay();
                }
                if (tienPhong == 0 && hours > 0) {
                    tienPhong = lp.getGiaTheoNgay();
                }
                
                return tienPhong;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private double tinhTienDichVu(String maDP) {
        double tong = 0;
        List<ChiTietDichVu> dsDV = chiTietDichVuDAO.getByMaDatPhong(maDP);
        for (ChiTietDichVu ctdv : dsDV) {
            tong += ctdv.getThanhTien();
        }
        return tong;
    }
    
    private void taoHoaDon(String maDP, double tienPhong, double tienDichVu, double tienCoc, double canTraThem) {
        try {
            HoaDon hd = new HoaDon();
            hd.setMaHD(hoaDonDAO.getAutoId());
            hd.setMaDatPhong(maDP);
            hd.setNgayLap(new Date());
            hd.setTienPhong(tienPhong);
            hd.setTienDichVu(tienDichVu);
            hd.setTienCoc(tienCoc);
            hd.setGiamGia(0);
            hd.setThanhTien(canTraThem > 0 ? canTraThem : 0);
            hd.setPhuongThucTT("Tiền mặt");
            hd.setTrangThai("Đã thanh toán");
            hd.setMaNV("NV001");
            
            hoaDonDAO.insert(hd);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo hóa đơn: " + ex.getMessage());
        }
    }
}