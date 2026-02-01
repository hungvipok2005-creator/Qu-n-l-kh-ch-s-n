package model;

import java.util.Date;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String cmnd;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private Date ngaySinh;
    private String gioiTinh;
    private String quocTich;
    private Date ngayTao;

    public KhachHang() {}

    public KhachHang(String maKH, String tenKH, String cmnd, String soDienThoai, 
                    String email, String diaChi, Date ngaySinh, String gioiTinh, 
                    String quocTich, Date ngayTao) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.cmnd = cmnd;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.quocTich = quocTich;
        this.ngayTao = ngayTao;
    }

    // Getters & Setters
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }
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
    public String getQuocTich() { return quocTich; }
    public void setQuocTich(String quocTich) { this.quocTich = quocTich; }
    public Date getNgayTao() { return ngayTao; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }

    @Override
    public String toString() {
        return tenKH + " (" + cmnd + ")";
    }
}
