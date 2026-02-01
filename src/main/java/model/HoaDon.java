package model;

import java.util.Date;

public class HoaDon {
    private String maHD;
    private String maDatPhong;
    private Date ngayLap;
    private double tienPhong;
    private double tienDichVu;
    private double tienCoc;
    private double giamGia;
    private double thanhTien;
    private String phuongThucTT;
    private String trangThai;
    private String maNV;

    public HoaDon() {}

    // Constructor đầy đủ
    public HoaDon(String maHD, String maDatPhong, Date ngayLap, double tienPhong,
                 double tienDichVu, double tienCoc, double giamGia, double thanhTien,
                 String phuongThucTT, String trangThai, String maNV) {
        this.maHD = maHD;
        this.maDatPhong = maDatPhong;
        this.ngayLap = ngayLap;
        this.tienPhong = tienPhong;
        this.tienDichVu = tienDichVu;
        this.tienCoc = tienCoc;
        this.giamGia = giamGia;
        this.thanhTien = thanhTien;
        this.phuongThucTT = phuongThucTT;
        this.trangThai = trangThai;
        this.maNV = maNV;
    }

    // Getters & Setters (tất cả)
    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }
    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }
    public Date getNgayLap() { return ngayLap; }
    public void setNgayLap(Date ngayLap) { this.ngayLap = ngayLap; }
    public double getTienPhong() { return tienPhong; }
    public void setTienPhong(double tienPhong) { this.tienPhong = tienPhong; }
    public double getTienDichVu() { return tienDichVu; }
    public void setTienDichVu(double tienDichVu) { this.tienDichVu = tienDichVu; }
    public double getTienCoc() { return tienCoc; }
    public void setTienCoc(double tienCoc) { this.tienCoc = tienCoc; }
    public double getGiamGia() { return giamGia; }
    public void setGiamGia(double giamGia) { this.giamGia = giamGia; }
    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }
    public String getPhuongThucTT() { return phuongThucTT; }
    public void setPhuongThucTT(String phuongThucTT) { this.phuongThucTT = phuongThucTT; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    @Override
    public String toString() {
        return maHD + " - " + String.format("%.0f VNĐ", thanhTien);
    }
}
