package model;

import java.util.Date;

public class ChiTietDichVu {
    private int maChiTiet;
    private String maDatPhong;
    private String maDV;
    private int soLuong;
    private double donGia;
    private Date ngaySuDung;
    private double thanhTien;

    public ChiTietDichVu() {}

    public ChiTietDichVu(int maChiTiet, String maDatPhong, String maDV, int soLuong,
                        double donGia, Date ngaySuDung, double thanhTien) {
        this.maChiTiet = maChiTiet;
        this.maDatPhong = maDatPhong;
        this.maDV = maDV;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.ngaySuDung = ngaySuDung;
        this.thanhTien = thanhTien;
    }

    // Getters & Setters
    public int getMaChiTiet() { return maChiTiet; }
    public void setMaChiTiet(int maChiTiet) { this.maChiTiet = maChiTiet; }
    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }
    public String getMaDV() { return maDV; }
    public void setMaDV(String maDV) { this.maDV = maDV; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
    public Date getNgaySuDung() { return ngaySuDung; }
    public void setNgaySuDung(Date ngaySuDung) { this.ngaySuDung = ngaySuDung; }
    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }

    @Override
    public String toString() {
        return maDV + " x" + soLuong + " = " + String.format("%.0f", thanhTien);
    }
}
