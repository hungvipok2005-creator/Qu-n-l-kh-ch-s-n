package model;

public class LoaiPhong {
    private String maLoaiPhong;
    private String tenLoaiPhong;
    private double giaTheoGio;
    private double giaTheoNgay;
    private int soNguoiToiDa;
    private String moTa;

    // Constructors
    public LoaiPhong() {}

    public LoaiPhong(String maLoaiPhong, String tenLoaiPhong, double giaTheoGio, 
                    double giaTheoNgay, int soNguoiToiDa, String moTa) {
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoaiPhong = tenLoaiPhong;
        this.giaTheoGio = giaTheoGio;
        this.giaTheoNgay = giaTheoNgay;
        this.soNguoiToiDa = soNguoiToiDa;
        this.moTa = moTa;
    }

    // Getters & Setters
    public String getMaLoaiPhong() { return maLoaiPhong; }
    public void setMaLoaiPhong(String maLoaiPhong) { this.maLoaiPhong = maLoaiPhong; }
    public String getTenLoaiPhong() { return tenLoaiPhong; }
    public void setTenLoaiPhong(String tenLoaiPhong) { this.tenLoaiPhong = tenLoaiPhong; }
    public double getGiaTheoGio() { return giaTheoGio; }
    public void setGiaTheoGio(double giaTheoGio) { this.giaTheoGio = giaTheoGio; }
    public double getGiaTheoNgay() { return giaTheoNgay; }
    public void setGiaTheoNgay(double giaTheoNgay) { this.giaTheoNgay = giaTheoNgay; }
    public int getSoNguoiToiDa() { return soNguoiToiDa; }
    public void setSoNguoiToiDa(int soNguoiToiDa) { this.soNguoiToiDa = soNguoiToiDa; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    @Override
    public String toString() {
        return tenLoaiPhong + " (Max: " + soNguoiToiDa + " người)";
    }
}
