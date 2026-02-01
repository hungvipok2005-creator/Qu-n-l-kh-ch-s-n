package model;

import java.util.Date;

public class DatPhong {
    private String maDatPhong;
    private String maKH;
    private String maPhong;
    private String maNV;
    private Date ngayDat;
    private Date ngayNhanPhong;
    private Date ngayTraPhong;
    private int soNguoi;
    private String loaiThue;
    private String trangThai;
    private double tienCoc;
    private String ghiChu;

    public DatPhong() {}

    // Constructor đầy đủ
    public DatPhong(String maDatPhong, String maKH, String maPhong, String maNV,
                   Date ngayDat, Date ngayNhanPhong, Date ngayTraPhong,
                   int soNguoi, String loaiThue, String trangThai,
                   double tienCoc, String ghiChu) {
        this.maDatPhong = maDatPhong;
        this.maKH = maKH;
        this.maPhong = maPhong;
        this.maNV = maNV;
        this.ngayDat = ngayDat;
        this.ngayNhanPhong = ngayNhanPhong;
        this.ngayTraPhong = ngayTraPhong;
        this.soNguoi = soNguoi;
        this.loaiThue = loaiThue;
        this.trangThai = trangThai;
        this.tienCoc = tienCoc;
        this.ghiChu = ghiChu;
    }

    // Getters & Setters (tất cả)
    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public Date getNgayDat() { return ngayDat; }
    public void setNgayDat(Date ngayDat) { this.ngayDat = ngayDat; }
    public Date getNgayNhanPhong() { return ngayNhanPhong; }
    public void setNgayNhanPhong(Date ngayNhanPhong) { this.ngayNhanPhong = ngayNhanPhong; }
    public Date getNgayTraPhong() { return ngayTraPhong; }
    public void setNgayTraPhong(Date ngayTraPhong) { this.ngayTraPhong = ngayTraPhong; }
    public int getSoNguoi() { return soNguoi; }
    public void setSoNguoi(int soNguoi) { this.soNguoi = soNguoi; }
    public String getLoaiThue() { return loaiThue; }
    public void setLoaiThue(String loaiThue) { this.loaiThue = loaiThue; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public double getTienCoc() { return tienCoc; }
    public void setTienCoc(double tienCoc) { this.tienCoc = tienCoc; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    @Override
    public String toString() {
        return maDatPhong + " - " + trangThai;
    }
}
