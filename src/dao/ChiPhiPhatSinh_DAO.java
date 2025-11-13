package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import connectDB.ConnectDB;
import entity.ChiPhiPhatSinh;

public class ChiPhiPhatSinh_DAO {

    // Đọc tất cả dữ liệu từ bảng ChiPhiPhatSinh
    public List<ChiPhiPhatSinh> getAllChiPhiPhatSinh() {
        List<ChiPhiPhatSinh> ds = new ArrayList<>();
        String sql = "SELECT * FROM ChiPhiPhatSinh";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String ma = rs.getString("maChiPhiPhatSinh");
                String ten = rs.getString("tenChiPhiPhatSinh");
                String loai = rs.getString("loaiChiPhiPhatSinh");
                double gia = rs.getDouble("gia");
                ChiPhiPhatSinh cp = new ChiPhiPhatSinh(ma, ten, loai, gia);
                ds.add(cp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Thêm mới
    public boolean insertChiPhi(ChiPhiPhatSinh cp) {
        String sql = "INSERT INTO ChiPhiPhatSinh (maChiPhiPhatSinh, tenChiPhiPhatSinh, loaiChiPhiPhatSinh, gia) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cp.getMaChiPhiPhatSinh());
            ps.setString(2, cp.getTenChiPhiPhatSinh());
            ps.setString(3, cp.getLoaiChiPhiPhatSinh());
            ps.setDouble(4, cp.getGia());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLServerException e) {
            if (e.getMessage().contains("Violation of UNIQUE KEY constraint")) {
                JOptionPane.showMessageDialog(null, 
                    "Lỗi: Mã chi phí đã tồn tại trong hệ thống!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Lỗi khi thao tác với cơ sở dữ liệu: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi thao tác với cơ sở dữ liệu: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // Cập nhật
    public boolean updateChiPhi(ChiPhiPhatSinh cp) {
        String sql = "UPDATE ChiPhiPhatSinh SET tenChiPhiPhatSinh = ?, loaiChiPhiPhatSinh = ?, gia = ? WHERE maChiPhiPhatSinh = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cp.getTenChiPhiPhatSinh());
            ps.setString(2, cp.getLoaiChiPhiPhatSinh());
            ps.setDouble(3, cp.getGia());
            ps.setString(4, cp.getMaChiPhiPhatSinh());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi cập nhật cơ sở dữ liệu: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // Tìm kiếm theo mã
    public ChiPhiPhatSinh getChiPhiTheoMa(String ma) {
        String sql = "SELECT * FROM ChiPhiPhatSinh WHERE maChiPhiPhatSinh = ?";
        ChiPhiPhatSinh cp = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ma);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String ten = rs.getString("tenChiPhiPhatSinh");
                String loai = rs.getString("loaiChiPhiPhatSinh");
                double gia = rs.getDouble("gia");
                cp = new ChiPhiPhatSinh(ma, ten, loai, gia);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi tìm kiếm cơ sở dữ liệu: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return cp;
    }

    // Tìm kiếm theo tên (trả về 1 chi phí duy nhất)
    public ChiPhiPhatSinh getChiPhiTheoTen(String ten) {
        String sql = "SELECT TOP 1 * FROM ChiPhiPhatSinh WHERE TRIM(tenChiPhiPhatSinh) = ?";
        ChiPhiPhatSinh cp = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ten.trim());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String ma = rs.getString("maChiPhiPhatSinh");
                String tenCP = rs.getString("tenChiPhiPhatSinh").trim();
                String loai = rs.getString("loaiChiPhiPhatSinh");
                double gia = rs.getDouble("gia");

                cp = new ChiPhiPhatSinh(ma, tenCP, loai, gia);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Lỗi khi tìm kiếm chi phí theo tên: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        return cp;
    }

    // Thêm chi tiết chi phí phát sinh vào phiếu đặt phòng
    public boolean themChiTietChiPhi(String maPhieuDatPhong, String maChiPhiPhatSinh, int soLuong) {
        String sql = "INSERT INTO ChiTietChiPhiPhatSinh (maPhieuDatPhong, maChiPhiPhatSinh, soLuong) VALUES (?, ?, ?)";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhieuDatPhong);
            ps.setString(2, maChiPhiPhatSinh);
            ps.setInt(3, soLuong);

            int kq = ps.executeUpdate();
            return kq > 0;

        } catch (SQLServerException e) {
            if (e.getMessage().contains("Violation of UNIQUE KEY constraint")) {
                JOptionPane.showMessageDialog(null, 
                    "Chi phí này đã tồn tại trong phiếu đặt phòng!", 
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Lỗi SQL: " + e.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Lỗi khi thêm chi tiết chi phí phát sinh: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}
