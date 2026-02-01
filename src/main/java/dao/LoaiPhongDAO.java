package dao;

import model.LoaiPhong;
import connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiPhongDAO implements BaseDAO<LoaiPhong> {
    @Override
    public void insert(LoaiPhong lp) {
        String sql = "INSERT INTO LoaiPhong VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lp.getMaLoaiPhong());
            ps.setNString(2, lp.getTenLoaiPhong());
            ps.setDouble(3, lp.getGiaTheoGio());
            ps.setDouble(4, lp.getGiaTheoNgay());
            ps.setInt(5, lp.getSoNguoiToiDa());
            ps.setNString(6, lp.getMoTa());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(LoaiPhong lp) {
        String sql = "UPDATE LoaiPhong SET TenLoaiPhong=?, GiaTheoGio=?, GiaTheoNgay=?, SoNguoiToiDa=?, MoTa=? WHERE MaLoaiPhong=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, lp.getTenLoaiPhong());
            ps.setDouble(2, lp.getGiaTheoGio());
            ps.setDouble(3, lp.getGiaTheoNgay());
            ps.setInt(4, lp.getSoNguoiToiDa());
            ps.setNString(5, lp.getMoTa());
            ps.setString(6, lp.getMaLoaiPhong());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM LoaiPhong WHERE MaLoaiPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LoaiPhong> getAll() {
        List<LoaiPhong> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiPhong";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LoaiPhong lp = new LoaiPhong();
                lp.setMaLoaiPhong(rs.getString("MaLoaiPhong"));
                lp.setTenLoaiPhong(rs.getNString("TenLoaiPhong"));
                lp.setGiaTheoGio(rs.getDouble("GiaTheoGio"));
                lp.setGiaTheoNgay(rs.getDouble("GiaTheoNgay"));
                lp.setSoNguoiToiDa(rs.getInt("SoNguoiToiDa"));
                lp.setMoTa(rs.getNString("MoTa"));
                list.add(lp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public LoaiPhong getById(String id) {
        String sql = "SELECT * FROM LoaiPhong WHERE MaLoaiPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LoaiPhong lp = new LoaiPhong();
                lp.setMaLoaiPhong(rs.getString("MaLoaiPhong"));
                lp.setTenLoaiPhong(rs.getNString("TenLoaiPhong"));
                lp.setGiaTheoGio(rs.getDouble("GiaTheoGio"));
                lp.setGiaTheoNgay(rs.getDouble("GiaTheoNgay"));
                lp.setSoNguoiToiDa(rs.getInt("SoNguoiToiDa"));
                lp.setMoTa(rs.getNString("MoTa"));
                return lp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<LoaiPhong> search(String keyword) {
        List<LoaiPhong> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiPhong WHERE TenLoaiPhong LIKE N'%' + ? + '%'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, keyword);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LoaiPhong lp = new LoaiPhong();
                lp.setMaLoaiPhong(rs.getString("MaLoaiPhong"));
                lp.setTenLoaiPhong(rs.getNString("TenLoaiPhong"));
                lp.setGiaTheoGio(rs.getDouble("GiaTheoGio"));
                lp.setGiaTheoNgay(rs.getDouble("GiaTheoNgay"));
                lp.setSoNguoiToiDa(rs.getInt("SoNguoiToiDa"));
                lp.setMoTa(rs.getNString("MoTa"));
                list.add(lp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
