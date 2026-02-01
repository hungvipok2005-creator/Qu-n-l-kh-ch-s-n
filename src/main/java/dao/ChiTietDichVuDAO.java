package dao;

import model.ChiTietDichVu;
import connection.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietDichVuDAO implements BaseDAO<ChiTietDichVu> {

    @Override
    public void insert(ChiTietDichVu ctdv) {
        // KHÔNG insert MaChiTiet (Identity) và ThanhTien (Computed)
        String sql = "INSERT INTO ChiTietDichVu (MaDatPhong, MaDV, SoLuong, DonGia, NgaySuDung) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ctdv.getMaDatPhong());
            ps.setString(2, ctdv.getMaDV());
            ps.setInt(3, ctdv.getSoLuong());
            ps.setDouble(4, ctdv.getDonGia());
            
            // Xử lý an toàn: Nếu ngày null thì lấy giờ hiện tại
            if (ctdv.getNgaySuDung() != null) {
                ps.setTimestamp(5, new Timestamp(ctdv.getNgaySuDung().getTime()));
            } else {
                ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            }
            
            ps.executeUpdate();
            System.out.println("✅ Insert ChiTietDichVu thành công: " + ctdv.getMaDatPhong());
            
        } catch (SQLException e) {
            System.err.println("💥 Lỗi insert ChiTietDichVu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(ChiTietDichVu ctdv) {
        // Update các thông tin, trừ Thành tiền (tự tính)
        String sql = "UPDATE ChiTietDichVu SET MaDatPhong=?, MaDV=?, SoLuong=?, DonGia=?, NgaySuDung=? WHERE MaChiTiet=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ctdv.getMaDatPhong());
            ps.setString(2, ctdv.getMaDV());
            ps.setInt(3, ctdv.getSoLuong());
            ps.setDouble(4, ctdv.getDonGia());
            
            if (ctdv.getNgaySuDung() != null) {
                ps.setTimestamp(5, new Timestamp(ctdv.getNgaySuDung().getTime()));
            } else {
                ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            }
            
            ps.setInt(6, ctdv.getMaChiTiet());
            
            ps.executeUpdate();
            System.out.println("✅ Update ChiTietDichVu thành công ID: " + ctdv.getMaChiTiet());
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM ChiTietDichVu WHERE MaChiTiet = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Chuyển đổi String id sang int
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
            System.out.println("🗑️ Delete ChiTietDichVu ID: " + id);
            
        } catch (NumberFormatException e) {
            System.err.println("Lỗi định dạng ID khi xóa: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ChiTietDichVu> getAll() {
        List<ChiTietDichVu> list = new ArrayList<>();
        // Sắp xếp ID giảm dần để thấy cái mới nhất
        String sql = "SELECT * FROM ChiTietDichVu ORDER BY MaChiTiet DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ChiTietDichVu ctdv = new ChiTietDichVu();
                ctdv.setMaChiTiet(rs.getInt("MaChiTiet"));
                ctdv.setMaDatPhong(rs.getString("MaDatPhong"));
                ctdv.setMaDV(rs.getString("MaDV"));
                ctdv.setSoLuong(rs.getInt("SoLuong"));
                ctdv.setDonGia(rs.getDouble("DonGia"));
                ctdv.setNgaySuDung(rs.getTimestamp("NgaySuDung"));
                ctdv.setThanhTien(rs.getDouble("ThanhTien")); // Lấy giá trị SQL tự tính
                list.add(ctdv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ChiTietDichVu getById(String id) {
        String sql = "SELECT * FROM ChiTietDichVu WHERE MaChiTiet = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChiTietDichVu ctdv = new ChiTietDichVu();
                    ctdv.setMaChiTiet(rs.getInt("MaChiTiet"));
                    ctdv.setMaDatPhong(rs.getString("MaDatPhong"));
                    ctdv.setMaDV(rs.getString("MaDV"));
                    ctdv.setSoLuong(rs.getInt("SoLuong"));
                    ctdv.setDonGia(rs.getDouble("DonGia"));
                    ctdv.setNgaySuDung(rs.getTimestamp("NgaySuDung"));
                    ctdv.setThanhTien(rs.getDouble("ThanhTien"));
                    return ctdv;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ChiTietDichVu> search(String keyword) {
        List<ChiTietDichVu> list = new ArrayList<>();
        // Tìm kiếm theo Mã Đặt Phòng (rất hữu ích khi xem hóa đơn)
        String sql = "SELECT * FROM ChiTietDichVu WHERE MaDatPhong LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietDichVu ctdv = new ChiTietDichVu();
                    ctdv.setMaChiTiet(rs.getInt("MaChiTiet"));
                    ctdv.setMaDatPhong(rs.getString("MaDatPhong"));
                    ctdv.setMaDV(rs.getString("MaDV"));
                    ctdv.setSoLuong(rs.getInt("SoLuong"));
                    ctdv.setDonGia(rs.getDouble("DonGia"));
                    ctdv.setNgaySuDung(rs.getTimestamp("NgaySuDung"));
                    ctdv.setThanhTien(rs.getDouble("ThanhTien"));
                    list.add(ctdv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Hàm phụ: Lấy danh sách dịch vụ cụ thể của 1 phòng (Dùng chính xác, không like)
    public List<ChiTietDichVu> getByMaDatPhong(String maDP) {
        List<ChiTietDichVu> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietDichVu WHERE MaDatPhong = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietDichVu ctdv = new ChiTietDichVu();
                ctdv.setMaChiTiet(rs.getInt("MaChiTiet"));
                ctdv.setMaDatPhong(rs.getString("MaDatPhong"));
                ctdv.setMaDV(rs.getString("MaDV"));
                ctdv.setSoLuong(rs.getInt("SoLuong"));
                ctdv.setDonGia(rs.getDouble("DonGia"));
                ctdv.setNgaySuDung(rs.getTimestamp("NgaySuDung"));
                ctdv.setThanhTien(rs.getDouble("ThanhTien"));
                list.add(ctdv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public void deleteByMaDatPhong(String maDatPhong) {
    String sql = "DELETE FROM ChiTietDichVu WHERE MaDatPhong = ?";
    try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, maDatPhong);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Lỗi khi xóa chi tiết dịch vụ theo mã đặt phòng: " + e.getMessage());
    }
}
}