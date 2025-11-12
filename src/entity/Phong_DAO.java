package entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.LoaiPhong;
import entity.Phong;

public class Phong_DAO {

    // ======= LẤY TOÀN BỘ PHÒNG =======
    public List<Phong> getAllPhong() {
        List<Phong> dsPhong = new ArrayList<>();
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT p.maPhong, p.trangThai, p.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, lp.moTa " +
                 "FROM Phong p JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maPhong = rs.getString("maPhong");
                String trangThai = rs.getString("trangThai");
                String maLoai = rs.getString("maLoaiPhong");
                String tenLoai = rs.getString("tenLoaiPhong");
                int sucChua = rs.getInt("sucChua");
                double gia = rs.getDouble("gia");
                String moTa = rs.getString("moTa");

                LoaiPhong loaiPhong = new LoaiPhong(maLoai, tenLoai, sucChua, gia, moTa);
                dsPhong.add(new Phong(maPhong, loaiPhong, trangThai));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhong;
    }

    // ======= LẤY PHÒNG TRỐNG =======
    public List<Phong> getPhongTrong() {
        List<Phong> dsPhongTrong = new ArrayList<>();
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT p.maPhong, p.trangThai, p.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, lp.moTa " +
                 "FROM Phong p JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong " +
                 "WHERE p.trangThai = N'Trống'");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maPhong = rs.getString("maPhong");
                String trangThai = rs.getString("trangThai");
                String maLoai = rs.getString("maLoaiPhong");
                String tenLoai = rs.getString("tenLoaiPhong");
                int sucChua = rs.getInt("sucChua");
                double gia = rs.getDouble("gia");
                String moTa = rs.getString("moTa");

                LoaiPhong loaiPhong = new LoaiPhong(maLoai, tenLoai, sucChua, gia, moTa);
                dsPhongTrong.add(new Phong(maPhong, loaiPhong, trangThai));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhongTrong;
    }

    // ======= LẤY PHÒNG THEO MÃ LOẠI PHÒNG =======
    public List<Phong> getPhongTheoMaLoaiPhong(String maLoaiPhong) {
        List<Phong> dsPhong = new ArrayList<>();
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT p.maPhong, p.trangThai, p.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, lp.moTa " +
                 "FROM Phong p JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong " +
                 "WHERE p.maLoaiPhong = ?")) {

            stmt.setString(1, maLoaiPhong);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maPhong = rs.getString("maPhong");
                String trangThai = rs.getString("trangThai");
                String maLoai = rs.getString("maLoaiPhong");
                String tenLoai = rs.getString("tenLoaiPhong");
                int sucChua = rs.getInt("sucChua");
                double gia = rs.getDouble("gia");
                String moTa = rs.getString("moTa");

                LoaiPhong loaiPhong = new LoaiPhong(maLoai, tenLoai, sucChua, gia, moTa);
                dsPhong.add(new Phong(maPhong, loaiPhong, trangThai));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhong;
    }

    // ======= THÊM PHÒNG =======
    public boolean themPhong(Phong phong) {
        String sql = "INSERT INTO Phong (maPhong, maLoaiPhong, trangThai) VALUES (?, ?, ?)";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phong.getMaPhong());
            ps.setString(2, phong.getLoaiPhong() != null ? phong.getLoaiPhong().getMaLoaiPhong() : null);
            ps.setString(3, phong.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======= CẬP NHẬT PHÒNG =======
    public boolean capNhatPhong(Phong phong) {
        String sql = "UPDATE Phong SET maLoaiPhong=?, trangThai=? WHERE maPhong=?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phong.getLoaiPhong() != null ? phong.getLoaiPhong().getMaLoaiPhong() : null);
            ps.setString(2, phong.getTrangThai());
            ps.setString(3, phong.getMaPhong());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======= XÓA PHÒNG =======
    public boolean xoaPhong(String maPhong) {
        String sql = "DELETE FROM Phong WHERE maPhong=?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPhong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======= TÌM PHÒNG THEO MÃ =======
    public Phong timPhongTheoMa(String maPhongInput) {
        Phong phong = null;
        String sql = """
            SELECT RTRIM(LTRIM(p.maPhong)) AS maPhong, 
                   RTRIM(LTRIM(p.trangThai)) AS trangThai,
                   RTRIM(LTRIM(lp.maLoaiPhong)) AS maLoaiPhong, 
                   RTRIM(LTRIM(lp.tenLoaiPhong)) AS tenLoaiPhong, 
                   lp.sucChua, lp.gia, COALESCE(lp.moTa, '') AS moTa
            FROM Phong p 
            LEFT JOIN LoaiPhong lp ON LTRIM(RTRIM(p.maLoaiPhong)) = LTRIM(RTRIM(lp.maLoaiPhong))
            WHERE LTRIM(RTRIM(p.maPhong)) = ?
        """;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhongInput.trim());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String maPhong = rs.getString("maPhong");
                String trangThai = rs.getString("trangThai");
                String maLoai = rs.getString("maLoaiPhong");
                String tenLoai = rs.getString("tenLoaiPhong");
                int sucChua = rs.getInt("sucChua");
                double gia = rs.getDouble("gia");
                String moTa = rs.getString("moTa");

                LoaiPhong loaiPhong = new LoaiPhong(maLoai, tenLoai, sucChua, gia, moTa);
                phong = new Phong(maPhong, loaiPhong, trangThai);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return phong;
    }
    
 // ======= LẤY MÃ PHÒNG CAO NHẤT THEO LOẠI (VD: P1, P2, P3) =======
    public String getLastRoomCodeByLoai(String prefix) {
        String lastCode = null;
        String sql = """
            SELECT TOP 1 maPhong
            FROM Phong
            WHERE maPhong LIKE ?
            ORDER BY 
                TRY_CAST(SUBSTRING(maPhong, LEN(?) + 1, LEN(maPhong) - LEN(?)) AS INT) DESC
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, prefix + "%"); // ví dụ prefix = "P1" → tìm P101, P102...
            ps.setString(2, prefix);
            ps.setString(3, prefix);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lastCode = rs.getString("maPhong").trim();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastCode; // null nếu chưa có phòng nào với prefix đó
    }

}
