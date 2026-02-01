package model;

public class DichVu {
    private String maDV;
    private String tenDV;
    private double donGia;
    private String donVi;
    private String moTa;

    public DichVu() {}

    public DichVu(String maDV, String tenDV, double donGia, String donVi, String moTa) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.donGia = donGia;
        this.donVi = donVi;
        this.moTa = moTa;
    }

    // Getters & Setters
    public String getMaDV() { return maDV; }
    public void setMaDV(String maDV) { this.maDV = maDV; }
    public String getTenDV() { return tenDV; }
    public void setTenDV(String tenDV) { this.tenDV = tenDV; }
    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
    public String getDonVi() { return donVi; }
    public void setDonVi(String donVi) { this.donVi = donVi; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    @Override
    public String toString() {
        return tenDV + " (" + String.format("%.0f", donGia) + " VNĐ/" + donVi + ")";
    }
}
