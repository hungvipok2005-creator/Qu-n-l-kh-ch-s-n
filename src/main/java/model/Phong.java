package model;

public class Phong {
    private String maPhong;
    private String maLoaiPhong;
    private String tenPhong;
    private int tang;
    private String trangThai;
    private String ghiChu;

    public Phong() {}

    public Phong(String maPhong, String maLoaiPhong, String tenPhong, int tang, 
                String trangThai, String ghiChu) {
        this.maPhong = maPhong;
        this.maLoaiPhong = maLoaiPhong;
        this.tenPhong = tenPhong;
        this.tang = tang;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    // Getters & Setters
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public String getMaLoaiPhong() { return maLoaiPhong; }
    public void setMaLoaiPhong(String maLoaiPhong) { this.maLoaiPhong = maLoaiPhong; }
    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
    public int getTang() { return tang; }
    public void setTang(int tang) { this.tang = tang; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    @Override
    public String toString() {
        return tenPhong + " - " + trangThai;
    }
}
