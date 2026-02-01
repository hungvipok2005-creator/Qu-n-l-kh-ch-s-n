package dao;

import model.DatPhong;
import connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatPhongDAO implements BaseDAO<DatPhong> {
    @Override
    public void insert(DatPhong dp) {
        String sql = "INSERT INTO DatPhong VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dp.getMaDatPhong());
            ps.setString(2, dp.getMaKH());
            ps.setString(3, dp.getMaPhong());
            ps.setString(4, dp.getMaNV());
            ps.setTimestamp(5, dp.getNgayDat() != null ? new Timestamp(dp.getNgayDat().getTime()) : new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(6, new Timestamp(dp.getNgayNhanPhong().getTime()));
            ps.setTimestamp(7, new Timestamp(dp.getNgayTraPhong().getTime()));
            ps.setInt(8, dp.getSoNguoi());
            ps.setNString(9, dp.getLoaiThue());
            ps.setNString(10, dp.getTrangThai());
            ps.setDouble(11, dp.getTienCoc());
            ps.setNString(12, dp.getGhiChu());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(DatPhong dp) {
        String sql = "UPDATE DatPhong SET MaKH=?, MaPhong=?, MaNV=?, NgayNhanPhong=?, NgayTraPhong=?, SoNguoi=?, LoaiThue=?, TrangThai=?, TienCoc=?, GhiChu=? WHERE MaDatPhong=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dp.getMaKH());
            ps.setString(2, dp.getMaPhong());
            ps.setString(3, dp.getMaNV());
            ps.setTimestamp(4, new Timestamp(dp.getNgayNhanPhong().getTime()));
            ps.setTimestamp(5, new Timestamp(dp.getNgayTraPhong().getTime()));
            ps.setInt(6, dp.getSoNguoi());
            ps.setNString(7, dp.getLoaiThue());
            ps.setNString(8, dp.getTrangThai());
            ps.setDouble(9, dp.getTienCoc());
            ps.setNString(10, dp.getGhiChu());
            ps.setString(11, dp.getMaDatPhong());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM DatPhong WHERE MaDatPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DatPhong> getAll() {
        List<DatPhong> list = new ArrayList<>();
        String sql = "SELECT * FROM DatPhong";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DatPhong dp = new DatPhong();
                dp.setMaDatPhong(rs.getString("MaDatPhong"));
                dp.setMaKH(rs.getString("MaKH"));
                dp.setMaPhong(rs.getString("MaPhong"));
                dp.setMaNV(rs.getString("MaNV"));
                dp.setNgayDat(rs.getTimestamp("NgayDat"));
                dp.setNgayNhanPhong(rs.getTimestamp("NgayNhanPhong"));
                dp.setNgayTraPhong(rs.getTimestamp("NgayTraPhong"));
                dp.setSoNguoi(rs.getInt("SoNguoi"));
                dp.setLoaiThue(rs.getNString("LoaiThue"));
                dp.setTrangThai(rs.getNString("TrangThai"));
                dp.setTienCoc(rs.getDouble("TienCoc"));
                dp.setGhiChu(rs.getNString("GhiChu"));
                list.add(dp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public DatPhong getById(String id) {
        String sql = "SELECT * FROM DatPhong WHERE MaDatPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DatPhong dp = new DatPhong();
                dp.setMaDatPhong(rs.getString("MaDatPhong"));
                dp.setMaKH(rs.getString("MaKH"));
                dp.setMaPhong(rs.getString("MaPhong"));
                dp.setMaNV(rs.getString("MaNV"));
                dp.setNgayDat(rs.getTimestamp("NgayDat"));
                dp.setNgayNhanPhong(rs.getTimestamp("NgayNhanPhong"));
                dp.setNgayTraPhong(rs.getTimestamp("NgayTraPhong"));
                dp.setSoNguoi(rs.getInt("SoNguoi"));
                dp.setLoaiThue(rs.getNString("LoaiThue"));
                dp.setTrangThai(rs.getNString("TrangThai"));
                dp.setTienCoc(rs.getDouble("TienCoc"));
                dp.setGhiChu(rs.getNString("GhiChu"));
                return dp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
public List<DatPhong> search(String keyword) {
    List<DatPhong> list = new ArrayList<>();
    String sql = "SELECT * FROM DatPhong WHERE MaDatPhong LIKE ? OR TrangThai LIKE N?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        String key = "%" + keyword + "%";
        ps.setString(1, key);
        ps.setString(2, key);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DatPhong dp = new DatPhong();
                dp.setMaDatPhong(rs.getString("MaDatPhong"));
                dp.setMaKH(rs.getString("MaKH"));
                dp.setMaPhong(rs.getString("MaPhong"));
                dp.setMaNV(rs.getString("MaNV"));
                dp.setNgayDat(rs.getTimestamp("NgayDat"));
                dp.setNgayNhanPhong(rs.getTimestamp("NgayNhanPhong"));
                dp.setNgayTraPhong(rs.getTimestamp("NgayTraPhong"));
                dp.setSoNguoi(rs.getInt("SoNguoi"));
                dp.setLoaiThue(rs.getNString("LoaiThue"));
                dp.setTrangThai(rs.getNString("TrangThai"));
                dp.setTienCoc(rs.getDouble("TienCoc"));
                dp.setGhiChu(rs.getNString("GhiChu"));
                list.add(dp);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

}
