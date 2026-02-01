package dao;

import model.KhachHang;
import connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO implements BaseDAO<KhachHang> {
    
    public static void testConnection() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("DB Connection: " + (conn != null ? "OK" : "FAIL"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void insert(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (MaKH, TenKH, CMND, SoDienThoai, Email, DiaChi, NgaySinh, GioiTinh, QuocTich) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            System.out.println("Inserting: " + kh.getMaKH() + " - " + kh.getTenKH());
            
            ps.setString(1, kh.getMaKH());
            ps.setNString(2, kh.getTenKH());
            ps.setString(3, kh.getCmnd());
            ps.setString(4, kh.getSoDienThoai());
            ps.setString(5, kh.getEmail());
            ps.setNString(6, kh.getDiaChi() != null ? kh.getDiaChi() : "");
            
            if (kh.getNgaySinh() != null) {
                ps.setDate(7, new java.sql.Date(kh.getNgaySinh().getTime()));
            } else {
                ps.setNull(7, Types.DATE);
            }
            
            ps.setNString(8, kh.getGioiTinh());
            ps.setNString(9, kh.getQuocTich() != null ? kh.getQuocTich() : "Việt Nam");
            
            int result = ps.executeUpdate();
            System.out.println("INSERT RESULT: " + result + " rows affected");
            
        } catch (SQLException e) {
            System.err.println("INSERT ERROR: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi thêm khách hàng: " + e.getMessage());
        }
    }

    @Override
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang ORDER BY MaKH DESC"; 
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToKhachHang(rs));
            }
            System.out.println("getAll() SUCCESS: " + list.size() + " records loaded");
            
        } catch (SQLException e) {
            System.err.println("getAll() ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET TenKH=?, CMND=?, SoDienThoai=?, Email=?, DiaChi=?, NgaySinh=?, GioiTinh=?, QuocTich=? WHERE MaKH=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setNString(1, kh.getTenKH());
            ps.setString(2, kh.getCmnd());
            ps.setString(3, kh.getSoDienThoai());
            ps.setString(4, kh.getEmail());
            ps.setNString(5, kh.getDiaChi());
            
            if (kh.getNgaySinh() != null) {
                ps.setDate(6, new java.sql.Date(kh.getNgaySinh().getTime()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setNString(7, kh.getGioiTinh());
            ps.setNString(8, kh.getQuocTich());
            ps.setString(9, kh.getMaKH());
            
            ps.executeUpdate();
            System.out.println("UPDATE SUCCESS: " + kh.getMaKH());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi cập nhật: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM KhachHang WHERE MaKH = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            System.out.println("DELETE SUCCESS: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi xóa: " + e.getMessage());
        }
    }
    
    @Override
    public List<KhachHang> search(String keyword) {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE TenKH LIKE ? OR SoDienThoai LIKE ? OR CMND LIKE ? OR MaKH LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchKey = "%" + keyword + "%";
            ps.setNString(1, searchKey);
            ps.setString(2, searchKey);
            ps.setString(3, searchKey);
            ps.setString(4, searchKey);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToKhachHang(rs));
                }
            }
            System.out.println("SEARCH RESULT: " + list.size() + " records found for '" + keyword + "'");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public KhachHang getById(String id) {
        return null; 
    }

    public String getAutoId() {
        String sql = "SELECT TOP 1 MaKH FROM KhachHang ORDER BY MaKH DESC"; 
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String maxId = rs.getString("MaKH");
                if (maxId != null && maxId.length() > 2) {
                    String numberPart = maxId.substring(2); 
                    try {
                        int num = Integer.parseInt(numberPart);
                        return String.format("KH%03d", num + 1);
                    } catch (NumberFormatException e) {
                         System.err.println("Error parsing ID: " + maxId);
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "KH001";
    }

    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setMaKH(rs.getString("MaKH"));
        kh.setTenKH(rs.getNString("TenKH"));
        kh.setCmnd(rs.getString("CMND"));
        kh.setSoDienThoai(rs.getString("SoDienThoai"));
        kh.setEmail(rs.getString("Email"));
        kh.setDiaChi(rs.getNString("DiaChi"));
        kh.setNgaySinh(rs.getDate("NgaySinh"));
        kh.setGioiTinh(rs.getNString("GioiTinh"));
        kh.setQuocTich(rs.getNString("QuocTich"));
        return kh;
    }
}