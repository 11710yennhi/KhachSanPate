package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import connectDB.ConnectDB;
import entity.ChiPhiPhatSinh;
import entity.ChiTietChiPhiPhatSinh;
import entity.PhieuDatPhong;

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
    
    public List<ChiTietChiPhiPhatSinh> getChiTietTheoMaPhieu(String maPhieuDatPhong) {
        List<ChiTietChiPhiPhatSinh> ds = new ArrayList<>();
        
        String sql = "SELECT maPhieuDatPhong, maChiPhiPhatSinh, soLuong "
                   + "FROM ChiTietChiPhiPhatSinh "
                   + "WHERE maPhieuDatPhong = ?";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhieuDatPhong);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maPDP = rs.getString("maPhieuDatPhong");
                String maCP = rs.getString("maChiPhiPhatSinh");
                int soLuong = rs.getInt("soLuong");

                // Tạo đối tượng
                ChiTietChiPhiPhatSinh ct = new ChiTietChiPhiPhatSinh();
                ct.setSoLuong(soLuong);

                // Gán phiếu đặt phòng
                PhieuDatPhong pdp = new PhieuDatPhong();
                pdp.setMaPhieuDatPhong(maPDP);
                ct.setPhieuDatPhong(pdp);

                // Gán chi phí phát sinh
                ChiPhiPhatSinh cp = new ChiPhiPhatSinh();
                cp.setMaChiPhiPhatSinh(maCP);
                ct.setChiPhiPhatSinh(cp);

                ds.add(ct);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Lỗi khi lấy chi tiết chi phí phát sinh: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        return ds;
    }
    public boolean xoaChiPhiTheoMaPhieu(String maPDP) {
        String sql = "DELETE FROM ChiTietChiPhiPhatSinh WHERE maPhieuDatPhong = ?";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPDP);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                null,
                "Lỗi xóa chi phí theo mã phiếu: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
            );
        }

        return false;
    }

}
