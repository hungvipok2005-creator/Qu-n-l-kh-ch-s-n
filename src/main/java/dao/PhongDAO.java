package dao;

import model.Phong;
import connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongDAO implements BaseDAO<Phong> {
    
    // Test connection
    public static void testConnection() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("DB Connection: " + (conn != null ? "OK" : "FAIL"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Phong p) {
        // Nên liệt kê rõ tên cột để tránh lỗi nếu cấu trúc bảng thay đổi
        String sql = "INSERT INTO Phong (MaPhong, MaLoaiPhong, TenPhong, Tang, TrangThai, GhiChu) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getMaLoaiPhong());
            ps.setNString(3, p.getTenPhong());
            ps.setInt(4, p.getTang());
            ps.setNString(5, p.getTrangThai());
            ps.setNString(6, p.getGhiChu());
            ps.executeUpdate();
            System.out.println("Insert success: " + p.getMaPhong());
        } catch (SQLException e) {
            System.err.println("Insert error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi thêm phòng: " + e.getMessage());
        }
    }

    @Override
    public void update(Phong p) {
        String sql = "UPDATE Phong SET MaLoaiPhong=?, TenPhong=?, Tang=?, TrangThai=?, GhiChu=? WHERE MaPhong=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getMaLoaiPhong());
            ps.setNString(2, p.getTenPhong());
            ps.setInt(3, p.getTang());
            ps.setNString(4, p.getTrangThai());
            ps.setNString(5, p.getGhiChu());
            ps.setString(6, p.getMaPhong());
            ps.executeUpdate();
            System.out.println("Update success: " + p.getMaPhong());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi cập nhật phòng: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Phong WHERE MaPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            System.out.println("Delete success: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi xóa phòng: " + e.getMessage());
        }
    }

    @Override
    public List<Phong> getAll() {
        List<Phong> list = new ArrayList<>();
        String sql = "SELECT * FROM Phong ORDER BY MaPhong ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToPhong(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Phong getById(String id) {
        String sql = "SELECT * FROM Phong WHERE MaPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPhong(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Phong> search(String keyword) {
        List<Phong> list = new ArrayList<>();
        String sql = "SELECT * FROM Phong WHERE TenPhong LIKE ? OR TrangThai LIKE ? OR MaPhong LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String key = "%" + keyword + "%";
            ps.setNString(1, key);
            ps.setNString(2, key);
            ps.setString(3, key);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPhong(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Phong> getPhongTrong() {
        List<Phong> list = new ArrayList<>();
        String sql = "SELECT * FROM Phong WHERE TrangThai = N'Trống'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToPhong(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int getSoNguoiToiDaByMaPhong(String maPhong) {
    String sql = "SELECT lp.SoNguoiToiDa " +
                 "FROM Phong p " +
                 "JOIN LoaiPhong lp ON p.MaLoaiPhong = lp.MaLoaiPhong " +
                 "WHERE p.MaPhong = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, maPhong);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("SoNguoiToiDa");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1; // giá trị báo lỗi
}


    // Helper method để code gọn hơn
    private Phong mapResultSetToPhong(ResultSet rs) throws SQLException {
        Phong p = new Phong();
        p.setMaPhong(rs.getString("MaPhong"));
        p.setMaLoaiPhong(rs.getString("MaLoaiPhong"));
        p.setTenPhong(rs.getNString("TenPhong"));
        p.setTang(rs.getInt("Tang"));
        p.setTrangThai(rs.getNString("TrangThai"));
        p.setGhiChu(rs.getNString("GhiChu"));
        return p;
    }
}