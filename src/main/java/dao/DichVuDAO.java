package dao;

import model.DichVu;
import connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DichVuDAO implements BaseDAO<DichVu> {

    // Test kết nối
    public static void testConnection() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("DB Connection: " + (conn != null ? "OK" : "FAIL"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(DichVu dv) {
        String sql = "INSERT INTO DichVu (MaDV, TenDV, DonGia, DonVi, MoTa) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, dv.getMaDV());
            ps.setNString(2, dv.getTenDV());
            ps.setDouble(3, dv.getDonGia());
            ps.setNString(4, dv.getDonVi());
            ps.setNString(5, dv.getMoTa());
            
            ps.executeUpdate();
            System.out.println("Insert Service Success: " + dv.getMaDV());
        } catch (SQLException e) {
            System.err.println("Insert Service Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi thêm dịch vụ: " + e.getMessage());
        }
    }

    @Override
    public void update(DichVu dv) {
        String sql = "UPDATE DichVu SET TenDV=?, DonGia=?, DonVi=?, MoTa=? WHERE MaDV=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setNString(1, dv.getTenDV());
            ps.setDouble(2, dv.getDonGia());
            ps.setNString(3, dv.getDonVi());
            ps.setNString(4, dv.getMoTa());
            ps.setString(5, dv.getMaDV());
            
            ps.executeUpdate();
            System.out.println("Update Service Success: " + dv.getMaDV());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi cập nhật dịch vụ: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM DichVu WHERE MaDV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            System.out.println("Delete Service Success: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi xóa dịch vụ (Có thể đang được sử dụng trong hóa đơn): " + e.getMessage());
        }
    }

    @Override
    public List<DichVu> getAll() {
        List<DichVu> list = new ArrayList<>();
        String sql = "SELECT * FROM DichVu ORDER BY MaDV ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToDichVu(rs));
            }
            System.out.println("Loaded " + list.size() + " services.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public DichVu getById(String id) {
        String sql = "SELECT * FROM DichVu WHERE MaDV = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDichVu(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DichVu> search(String keyword) {
        List<DichVu> list = new ArrayList<>();
        // Tìm theo tên hoặc mã
        String sql = "SELECT * FROM DichVu WHERE TenDV LIKE ? OR MaDV LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String key = "%" + keyword + "%";
            ps.setNString(1, key);
            ps.setString(2, key);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDichVu(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm tự động sinh mã DV001, DV002...
    public String getAutoId() {
        String sql = "SELECT TOP 1 MaDV FROM DichVu ORDER BY MaDV DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String maxId = rs.getString("MaDV");
                if (maxId != null && maxId.length() > 2) {
                    String numberPart = maxId.substring(2);
                    try {
                        int num = Integer.parseInt(numberPart);
                        return String.format("DV%03d", num + 1);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing ID: " + maxId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "DV001";
    }

    private DichVu mapResultSetToDichVu(ResultSet rs) throws SQLException {
        DichVu dv = new DichVu();
        dv.setMaDV(rs.getString("MaDV"));
        dv.setTenDV(rs.getNString("TenDV"));
        dv.setDonGia(rs.getDouble("DonGia"));
        dv.setDonVi(rs.getNString("DonVi"));
        dv.setMoTa(rs.getNString("MoTa"));
        return dv;
    }
}