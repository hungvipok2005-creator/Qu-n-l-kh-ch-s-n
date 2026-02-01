package model;

import java.util.Date;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String cmnd;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private Date ngaySinh;
    private String gioiTinh;
    private String chucVu;
    private double luong;
    private Date ngayVaoLam;
    private String tenDangNhap;
    private String matKhau;
    private String quyen; // Admin, Manager, Staff

    // Constructor mặc định
    public NhanVien() {}

    // Constructor đầy đủ
    public NhanVien(String maNV, String tenNV, String cmnd, String soDienThoai,
                   String email, String diaChi, Date ngaySinh, String gioiTinh,
                   String chucVu, double luong, Date ngayVaoLam,
                   String tenDangNhap, String matKhau, String quyen) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.cmnd = cmnd;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.chucVu = chucVu;
        this.luong = luong;
        this.ngayVaoLam = ngayVaoLam;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.quyen = quyen;
    }

    // Constructor đăng nhập (chỉ cần thông tin cơ bản)
    public NhanVien(String maNV, String tenNV, String chucVu, String quyen, String tenDangNhap) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.chucVu = chucVu;
        this.quyen = quyen;
        this.tenDangNhap = tenDangNhap;
    }

    // Getters & Setters
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    public String getCmnd() { return cmnd; }
    public void setCmnd(String cmnd) { this.cmnd = cmnd; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }

    public Date getNgayVaoLam() { return ngayVaoLam; }
    public void setNgayVaoLam(Date ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getQuyen() { return quyen; }
    public void setQuyen(String quyen) { this.quyen = quyen; }

    // Phương thức tiện ích
    public boolean isAdmin() {
        return "Admin".equals(quyen);
    }

    public boolean isManager() {
        return "Manager".equals(quyen) || isAdmin();
    }

    public String getLuongFormatted() {
        return String.format("%,.0f VNĐ", luong);
    }

    @Override
    public String toString() {
        return String.format("%s (%s - %s)", tenNV, chucVu, maNV);
    }

    // equals & hashCode cho so sánh
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NhanVien nhanVien = (NhanVien) obj;
        return maNV != null ? maNV.equals(nhanVien.maNV) : nhanVien.maNV == null;
    }

    @Override
    public int hashCode() {
        return maNV != null ? maNV.hashCode() : 0;
    }
}
