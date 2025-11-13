package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import connectDB.ConnectDB;
import entity.ChiTietChiPhiPhatSinh;

public class ChiTietChiPhiPhatSinh_DAO {

    // Thêm chi tiết chi phí phát sinh vào phiếu đặt phòng
    public boolean themChiTietChiPhi(ChiTietChiPhiPhatSinh ct) {
        String sql = "INSERT INTO ChiTietChiPhiPhatSinh (maPhieuDatPhong, maChiPhiPhatSinh, soLuong) VALUES (?, ?, ?)";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ct.getPhieuDatPhong().getMaPhieuDatPhong());
            ps.setString(2, ct.getChiPhiPhatSinh().getMaChiPhiPhatSinh());
            ps.setInt(3, ct.getSoLuong());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

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
