package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.LoaiPhong;
import entity.Phong;

public class Phong_DAO {

    private static final String SQL_SELECT_ALL = """
        SELECT p.maPhong, p.trangThai,
               p.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, COALESCE(lp.moTa,'') AS moTa
        FROM Phong p
        LEFT JOIN LoaiPhong lp ON LTRIM(RTRIM(p.maLoaiPhong)) = LTRIM(RTRIM(lp.maLoaiPhong))
        ORDER BY p.maPhong
    """;

    private static final String SQL_SELECT_BY_TRANGTHAI = """
        SELECT p.maPhong, p.trangThai,
               p.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, COALESCE(lp.moTa,'') AS moTa
        FROM Phong p
        LEFT JOIN LoaiPhong lp ON LTRIM(RTRIM(p.maLoaiPhong)) = LTRIM(RTRIM(lp.maLoaiPhong))
        WHERE LTRIM(RTRIM(p.trangThai)) = ?
        ORDER BY p.maPhong
    """;

    private static final String SQL_SELECT_BY_MALOAIPHONG = """
        SELECT p.maPhong, p.trangThai,
               p.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, COALESCE(lp.moTa,'') AS moTa
        FROM Phong p
        LEFT JOIN LoaiPhong lp ON LTRIM(RTRIM(p.maLoaiPhong)) = LTRIM(RTRIM(lp.maLoaiPhong))
        WHERE LTRIM(RTRIM(p.maLoaiPhong)) = ?
        ORDER BY p.maPhong
    """;

    private static final String SQL_FIND_BY_MA = """
        SELECT RTRIM(LTRIM(p.maPhong)) AS maPhong, 
               RTRIM(LTRIM(p.trangThai)) AS trangThai,
               RTRIM(LTRIM(lp.maLoaiPhong)) AS maLoaiPhong, 
               RTRIM(LTRIM(lp.tenLoaiPhong)) AS tenLoaiPhong, 
               lp.sucChua, lp.gia, COALESCE(lp.moTa, '') AS moTa
        FROM Phong p 
        LEFT JOIN LoaiPhong lp ON LTRIM(RTRIM(p.maLoaiPhong)) = LTRIM(RTRIM(lp.maLoaiPhong))
        WHERE LTRIM(RTRIM(p.maPhong)) = ?
    """;

    // ======= LẤY TOÀN BỘ PHÒNG =======
    public List<Phong> getAllPhong() {
        List<Phong> ds = new ArrayList<>();
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(mapPhong(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ======= LẤY PHÒNG TRỐNG =======
    public List<Phong> getPhongTrong() {
        List<Phong> ds = new ArrayList<>();
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_TRANGTHAI)) {

            ps.setString(1, "Trống");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapPhong(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ======= LẤY PHÒNG THEO MÃ LOẠI PHÒNG =======
    public List<Phong> getPhongTheoMaLoaiPhong(String maLoaiPhong) {
        List<Phong> ds = new ArrayList<>();
        if (maLoaiPhong == null) return ds;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_MALOAIPHONG)) {

            ps.setString(1, maLoaiPhong.trim());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapPhong(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ======= THÊM PHÒNG =======
    public boolean themPhong(Phong phong) {
        String sql = "INSERT INTO Phong (maPhong, maLoaiPhong, trangThai) VALUES (?, ?, ?)";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, phong.getMaPhong().trim());
            ps.setString(2, phong.getLoaiPhong() != null ? phong.getLoaiPhong().getMaLoaiPhong().trim() : null);
            ps.setString(3, phong.getTrangThai().trim());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // trùng khóa, lỗi FK...
            e.printStackTrace();
        }
        return false;
    }

    // ======= CẬP NHẬT PHÒNG =======
    public boolean capNhatPhong(Phong phong) {
        String sql = "UPDATE Phong SET maLoaiPhong=?, trangThai=? WHERE maPhong=?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, phong.getLoaiPhong() != null ? phong.getLoaiPhong().getMaLoaiPhong().trim() : null);
            ps.setString(2, phong.getTrangThai().trim());
            ps.setString(3, phong.getMaPhong().trim());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======= XÓA PHÒNG =======
    public boolean xoaPhong(String maPhong) {
        String sql = "DELETE FROM Phong WHERE maPhong=?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhong.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // nếu phòng đang được tham chiếu (FK) thì sẽ lỗi
            e.printStackTrace();
        }
        return false;
    }

    // ======= TÌM PHÒNG THEO MÃ =======
    public Phong timPhongTheoMa(String maPhongInput) {
        if (maPhongInput == null || maPhongInput.trim().isEmpty()) return null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_MA)) {

            ps.setString(1, maPhongInput.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapPhong(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ======= LẤY MÃ PHÒNG CAO NHẤT THEO PREFIX (P1/P2/P3...) =======
    public String getLastRoomCodeByLoai(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) return null;

        String sql = """
            SELECT TOP 1 maPhong
            FROM Phong
            WHERE maPhong LIKE ?
            ORDER BY TRY_CAST(SUBSTRING(maPhong, LEN(?) + 1, LEN(maPhong) - LEN(?)) AS INT) DESC
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String pfx = prefix.trim();
            ps.setString(1, pfx + "%");
            ps.setString(2, pfx);
            ps.setString(3, pfx);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("maPhong").trim();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ======= DS PHÒNG TRỐNG THEO NGÀY (giữ lại như bạn đang dùng) =======
    public List<Phong> getDSPhongTrongTheoNgay(LocalDate ngayNhanMoi, LocalDate ngayTraMoi) {
        List<Phong> dsPhong = new ArrayList<>();

        String sql = """
            SELECT p.maPhong, p.trangThai,
                   lp.maLoaiPhong, lp.tenLoaiPhong,
                   lp.sucChua, lp.gia, lp.moTa
            FROM Phong p
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
            WHERE p.trangThai = N'Trống'
            AND NOT EXISTS (
                SELECT 1
                FROM ChiTietPhieuDatPhong ct
                JOIN PhieuDatPhong pdp ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
                WHERE ct.maPhong = p.maPhong
                  AND pdp.trangThai NOT IN (N'Đã hủy', N'Hoàn thành')
                  AND ? < ct.ngayTra
                  AND ? > ct.ngayNhanThuc
            )
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(ngayNhanMoi));
            ps.setDate(2, java.sql.Date.valueOf(ngayTraMoi));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dsPhong.add(mapPhong(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsPhong;
    }

    // ======= Mapper =======
    private Phong mapPhong(ResultSet rs) throws SQLException {
        String maPhong = rs.getString("maPhong") != null ? rs.getString("maPhong").trim() : "";
        String trangThai = rs.getString("trangThai") != null ? rs.getString("trangThai").trim() : "";

        String maLoai = rs.getString("maLoaiPhong") != null ? rs.getString("maLoaiPhong").trim() : "";
        String tenLoai = rs.getString("tenLoaiPhong") != null ? rs.getString("tenLoaiPhong").trim() : "";
        int sucChua = rs.getInt("sucChua");
        double gia = rs.getDouble("gia");
        String moTa = rs.getString("moTa") != null ? rs.getString("moTa").trim() : "";

        LoaiPhong lp = new LoaiPhong(maLoai, tenLoai, sucChua, gia, moTa);
        return new Phong(maPhong, lp, trangThai);
    }
    
    //==========DASHBOARD=====================
  //Trạng thái bảo trì
    public int countPhongBaoTri() {
        int result = 0;

        String sql = """
            SELECT COUNT(*) AS cnt
            FROM Phong
            WHERE trangThai = N'Bảo trì'
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                result = rs.getInt("cnt");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
