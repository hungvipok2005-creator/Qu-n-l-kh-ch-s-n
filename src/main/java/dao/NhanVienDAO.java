package dao;

import model.NhanVien;
import connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO implements BaseDAO<NhanVien> {
    // Đăng nhập
    public NhanVien login(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM NhanVien WHERE TenDangNhap = ? AND MatKhau = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau); // Nên hash password trong thực tế
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getNString("TenNV"));
                nv.setCmnd(rs.getString("CMND"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setEmail(rs.getString("Email"));
                nv.setDiaChi(rs.getNString("DiaChi"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setGioiTinh(rs.getNString("GioiTinh"));
                nv.setChucVu(rs.getNString("ChucVu"));
                nv.setLuong(rs.getDouble("Luong"));
                nv.setNgayVaoLam(rs.getDate("NgayVaoLam"));
                nv.setTenDangNhap(rs.getString("TenDangNhap"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setQuyen(rs.getNString("Quyen"));
                return nv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNV());
            ps.setNString(2, nv.getTenNV());
            ps.setString(3, nv.getCmnd());
            ps.setString(4, nv.getSoDienThoai());
            ps.setString(5, nv.getEmail());
            ps.setNString(6, nv.getDiaChi());
            ps.setDate(7, nv.getNgaySinh() != null ? new Date(nv.getNgaySinh().getTime()) : null);
            ps.setNString(8, nv.getGioiTinh());
            ps.setNString(9, nv.getChucVu());
            ps.setDouble(10, nv.getLuong());
            ps.setDate(11, nv.getNgayVaoLam() != null ? new Date(nv.getNgayVaoLam().getTime()) : null);
            ps.setString(12, nv.getTenDangNhap());
            ps.setString(13, nv.getMatKhau());
            ps.setNString(14, nv.getQuyen());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET TenNV=?, CMND=?, SoDienThoai=?, Email=?, DiaChi=?, NgaySinh=?, GioiTinh=?, ChucVu=?, Luong=?, NgayVaoLam=?, TenDangNhap=?, MatKhau=?, Quyen=? WHERE MaNV=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, nv.getTenNV());
            ps.setString(2, nv.getCmnd());
            ps.setString(3, nv.getSoDienThoai());
            ps.setString(4, nv.getEmail());
            ps.setNString(5, nv.getDiaChi());
            ps.setDate(6, nv.getNgaySinh() != null ? new Date(nv.getNgaySinh().getTime()) : null);
            ps.setNString(7, nv.getGioiTinh());
            ps.setNString(8, nv.getChucVu());
            ps.setDouble(9, nv.getLuong());
            ps.setDate(10, nv.getNgayVaoLam() != null ? new Date(nv.getNgayVaoLam().getTime()) : null);
            ps.setString(11, nv.getTenDangNhap());
            ps.setString(12, nv.getMatKhau());
            ps.setNString(13, nv.getQuyen());
            ps.setString(14, nv.getMaNV());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM NhanVien WHERE MaNV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getNString("TenNV"));
                nv.setCmnd(rs.getString("CMND"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setEmail(rs.getString("Email"));
                nv.setDiaChi(rs.getNString("DiaChi"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setGioiTinh(rs.getNString("GioiTinh"));
                nv.setChucVu(rs.getNString("ChucVu"));
                nv.setLuong(rs.getDouble("Luong"));
                nv.setNgayVaoLam(rs.getDate("NgayVaoLam"));
                nv.setTenDangNhap(rs.getString("TenDangNhap"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setQuyen(rs.getNString("Quyen"));
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public NhanVien getById(String id) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getNString("TenNV"));
                nv.setCmnd(rs.getString("CMND"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setEmail(rs.getString("Email"));
                nv.setDiaChi(rs.getNString("DiaChi"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setGioiTinh(rs.getNString("GioiTinh"));
                nv.setChucVu(rs.getNString("ChucVu"));
                nv.setLuong(rs.getDouble("Luong"));
                nv.setNgayVaoLam(rs.getDate("NgayVaoLam"));
                nv.setTenDangNhap(rs.getString("TenDangNhap"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setQuyen(rs.getNString("Quyen"));
                return nv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<NhanVien> search(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE TenNV LIKE N'%' + ? + '%' OR ChucVu LIKE N'%' + ? + '%'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("MaNV"));
                nv.setTenNV(rs.getNString("TenNV"));
                nv.setCmnd(rs.getString("CMND"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setEmail(rs.getString("Email"));
                nv.setDiaChi(rs.getNString("DiaChi"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setGioiTinh(rs.getNString("GioiTinh"));
                nv.setChucVu(rs.getNString("ChucVu"));
                nv.setLuong(rs.getDouble("Luong"));
                nv.setNgayVaoLam(rs.getDate("NgayVaoLam"));
                nv.setTenDangNhap(rs.getString("TenDangNhap"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setQuyen(rs.getNString("Quyen"));
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
