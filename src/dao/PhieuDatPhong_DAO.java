package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import connectDB.ConnectDB;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatPhong;

public class PhieuDatPhong_DAO {

    // ==================== THÊM PHIẾU ĐẶT PHÒNG ====================
    public boolean themPhieuDatPhong(PhieuDatPhong pdt) {
        String query = "INSERT INTO PhieuDatPhong (maPhieuDatPhong, maKhachHang, maNhanVien, ngayTao, trangThai, soTreEm, soNguoiLon) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, pdt.getMaPhieuDatPhong());
            ps.setString(2, pdt.getKhachHang().getMaKhachHang());
            ps.setString(3, pdt.getNhanVien().getMaNhanVien());
            ps.setDate(4, Date.valueOf(pdt.getNgayTao()));
            ps.setString(5, pdt.getTrangThai());
            ps.setInt(6, pdt.getSoTreEm());
            ps.setInt(7, pdt.getSoNguoiLon());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==================== TÌM PHIẾU ĐẶT PHÒNG THEO MÃ ====================
    public PhieuDatPhong timPhieuDatPhongTheoMa(String maPhieuDatPhong) {
        String query = "SELECT * FROM PhieuDatPhong WHERE maPhieuDatPhong = ?";
        PhieuDatPhong pdt = null;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, maPhieuDatPhong);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pdt = new PhieuDatPhong(
                    rs.getString("maPhieuDatPhong"),
                    new KhachHang(rs.getString("maKhachHang")),
                    new NhanVien(rs.getString("maNhanVien")),
                    rs.getDate("ngayTao").toLocalDate(),
                    rs.getString("trangThai"),
                    rs.getInt("soTreEm"),
                    rs.getInt("soNguoiLon")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pdt;
    }

    // ==================== LẤY DANH SÁCH TẤT CẢ PHIẾU ====================
    public List<PhieuDatPhong> getAllPhieuDatPhong() {
        List<PhieuDatPhong> dsPhieu = new ArrayList<>();
        String query = "SELECT * FROM PhieuDatPhong";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhieuDatPhong pdt = new PhieuDatPhong(
                    rs.getString("maPhieuDatPhong"),
                    new KhachHang(rs.getString("maKhachHang")),
                    new NhanVien(rs.getString("maNhanVien")),
                    rs.getDate("ngayTao").toLocalDate(),
                    rs.getString("trangThai"),
                    rs.getInt("soTreEm"),
                    rs.getInt("soNguoiLon")
                );
                dsPhieu.add(pdt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsPhieu;
    }

    // ==================== CẬP NHẬT PHIẾU ĐẶT PHÒNG ====================
    public boolean capNhatPhieuDatPhong(PhieuDatPhong pdt) {
        String sql = "UPDATE PhieuDatPhong "
                   + "SET maKhachHang = ?, maNhanVien = ?, ngayTao = ?, trangThai = ?, soTreEm = ?, soNguoiLon = ? "
                   + "WHERE maPhieuDatPhong = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pdt.getKhachHang().getMaKhachHang());
            ps.setString(2, pdt.getNhanVien().getMaNhanVien());
            ps.setDate(3, Date.valueOf(pdt.getNgayTao()));
            ps.setString(4, pdt.getTrangThai());
            ps.setInt(5, pdt.getSoTreEm());
            ps.setInt(6, pdt.getSoNguoiLon());
            ps.setString(7, pdt.getMaPhieuDatPhong());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==================== (TÙY CHỌN) XÓA PHIẾU ====================
    public boolean xoaPhieuDatPhong(String maPhieuDatPhong) {
        String query = "DELETE FROM PhieuDatPhong WHERE maPhieuDatPhong = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, maPhieuDatPhong);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean daCoPhong(String maPhieuDatPhong, String maPhong) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getInstance().getConnection();

            String sql = """
                SELECT 1
                FROM ChiTietPhieuDatPhong
                WHERE maPhieuDatPhong = ?
                  AND maPhong = ?
            """;

            ps = con.prepareStatement(sql);
            ps.setString(1, maPhieuDatPhong);
            ps.setString(2, maPhong);

            rs = ps.executeQuery();

            return rs.next(); 

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
        }

        return false;
    }
    public List<PhieuDatPhong> getPhieuDatPhong2ThangGanNhat() {
        List<PhieuDatPhong> dsPhieu = new ArrayList<>();

        String sql = """
            SELECT *
            FROM PhieuDatPhong
            WHERE ngayTao >= DATEADD(MONTH, -2, GETDATE())
              AND trangThai NOT IN (N'Đã hủy', N'Hoàn thành')
            ORDER BY ngayTao DESC
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhieuDatPhong p = new PhieuDatPhong(
                    rs.getString("maPhieuDatPhong"),
                    new KhachHang(rs.getString("maKhachHang")),
                    new NhanVien(rs.getString("maNhanVien")),
                    rs.getDate("ngayTao").toLocalDate(),
                    rs.getString("trangThai"),
                    rs.getInt("soTreEm"),
                    rs.getInt("soNguoiLon")
                );
                dsPhieu.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsPhieu;
    }


}
