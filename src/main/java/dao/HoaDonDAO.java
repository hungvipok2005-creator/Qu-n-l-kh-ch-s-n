package dao;

import model.HoaDon;
import connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO implements BaseDAO<HoaDon> {

    @Override
    public void insert(HoaDon hd) {
        // Liệt kê rõ cột để an toàn hơn
        String sql = "INSERT INTO HoaDon (MaHD, MaDatPhong, NgayLap, TienPhong, TienDichVu, TienCoc, GiamGia, ThanhTien, PhuongThucTT, TrangThai, MaNV) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getMaDatPhong());
            
            // Xử lý ngày lập (nếu null thì lấy giờ hiện tại)
            if (hd.getNgayLap() != null) {
                ps.setTimestamp(3, new Timestamp(hd.getNgayLap().getTime()));
            } else {
                ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            }
            
            ps.setDouble(4, hd.getTienPhong());
            ps.setDouble(5, hd.getTienDichVu());
            ps.setDouble(6, hd.getTienCoc());
            ps.setDouble(7, hd.getGiamGia());
            ps.setDouble(8, hd.getThanhTien());
            ps.setNString(9, hd.getPhuongThucTT());
            ps.setNString(10, hd.getTrangThai());
            ps.setString(11, hd.getMaNV());
            
            ps.executeUpdate();
            System.out.println("Insert HoaDon thanh cong: " + hd.getMaHD());
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Loi them hoa don: " + e.getMessage());
        }
    }

    @Override
    public void update(HoaDon hd) {
        String sql = "UPDATE HoaDon SET MaDatPhong=?, NgayLap=?, TienPhong=?, TienDichVu=?, TienCoc=?, GiamGia=?, ThanhTien=?, PhuongThucTT=?, TrangThai=?, MaNV=? WHERE MaHD=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, hd.getMaDatPhong());
            ps.setTimestamp(2, new Timestamp(hd.getNgayLap().getTime()));
            ps.setDouble(3, hd.getTienPhong());
            ps.setDouble(4, hd.getTienDichVu());
            ps.setDouble(5, hd.getTienCoc());
            ps.setDouble(6, hd.getGiamGia());
            ps.setDouble(7, hd.getThanhTien());
            ps.setNString(8, hd.getPhuongThucTT());
            ps.setNString(9, hd.getTrangThai());
            ps.setString(10, hd.getMaNV());
            ps.setString(11, hd.getMaHD());
            
            ps.executeUpdate();
            System.out.println("Update HoaDon thanh cong: " + hd.getMaHD());
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM HoaDon WHERE MaHD = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            System.out.println("Delete HoaDon ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HoaDon> getAll() {
        List<HoaDon> list = new ArrayList<>();
        // Sắp xếp ngày mới nhất lên đầu để dễ theo dõi
        String sql = "SELECT * FROM HoaDon ORDER BY NgayLap DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(mapResultSetToHoaDon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public HoaDon getById(String id) {
        String sql = "SELECT * FROM HoaDon WHERE MaHD = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToHoaDon(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<HoaDon> search(String keyword) {
        List<HoaDon> list = new ArrayList<>();
        // Tìm theo Mã Hóa Đơn hoặc Mã Đặt Phòng
        String sql = "SELECT * FROM HoaDon WHERE MaHD LIKE ? OR MaDatPhong LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToHoaDon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // --- HÀM TỰ ĐỘNG SINH MÃ (HD001, HD002...) ---
    public String getAutoId() {
        String sql = "SELECT TOP 1 MaHD FROM HoaDon ORDER BY MaHD DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String maxId = rs.getString("MaHD"); // VD: HD005
                if (maxId != null && maxId.length() > 2) {
                    String numberPart = maxId.substring(2); 
                    try {
                        int num = Integer.parseInt(numberPart);
                        return String.format("HD%03d", num + 1);
                    } catch (NumberFormatException e) {
                        System.err.println("Loi parse ma: " + maxId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "HD001"; // Mặc định nếu chưa có hóa đơn nào
    }

    // Hàm phụ trợ mapping
    private HoaDon mapResultSetToHoaDon(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHD(rs.getString("MaHD"));
        hd.setMaDatPhong(rs.getString("MaDatPhong"));
        hd.setNgayLap(rs.getTimestamp("NgayLap"));
        hd.setTienPhong(rs.getDouble("TienPhong"));
        hd.setTienDichVu(rs.getDouble("TienDichVu"));
        hd.setTienCoc(rs.getDouble("TienCoc"));
        hd.setGiamGia(rs.getDouble("GiamGia"));
        hd.setThanhTien(rs.getDouble("ThanhTien"));
        hd.setPhuongThucTT(rs.getNString("PhuongThucTT"));
        hd.setTrangThai(rs.getNString("TrangThai"));
        hd.setMaNV(rs.getString("MaNV"));
        return hd;
    }
    public void deleteByMaDatPhong(String maDatPhong) {
    String sql = "DELETE FROM HoaDon WHERE MaDatPhong = ?";
 try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, maDatPhong);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi xóa hóa đơn theo mã đặt phòng: " + e.getMessage());
    }
}
    
}