package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.ChiTietPhieuDatPhong;
import entity.PhieuDatPhong;
import entity.Phong;
import entity.LoaiPhong;

public class ChiTietPhieuDatPhong_DAO {

    // ===================== LẤY CHI TIẾT THEO MÃ PHIẾU =====================
    public List<ChiTietPhieuDatPhong> getChiTietTheoMaPhieu(String maPhieuDatPhong) {
        List<ChiTietPhieuDatPhong> dsCT = new ArrayList<>();

        String sql = """
            SELECT ctpdp.maPhieuDatPhong, ctpdp.maPhong,
                   ctpdp.ngayNhanThuc, ctpdp.ngayTraThuc, ctpdp.ngayTra,
                   p.trangThai,
                   lp.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, lp.moTa
            FROM ChiTietPhieuDatPhong ctpdp
            JOIN Phong p ON ctpdp.maPhong = p.maPhong
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
            WHERE ctpdp.maPhieuDatPhong = ?
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhieuDatPhong);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dsCT.add(mapResultSetToCT(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsCT;
    }

    // ===================== THÊM CHI TIẾT PHIẾU =====================
    public boolean themChiTietPhieuDatPhong(ChiTietPhieuDatPhong ct) {

        String sql = """
            INSERT INTO ChiTietPhieuDatPhong
                (maPhieuDatPhong, maPhong, ngayNhanThuc, ngayTraThuc, ngayTra)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ct.getPhieuDatPhong().getMaPhieuDatPhong());
            ps.setString(2, ct.getPhong().getMaPhong());

            ps.setDate(3, ct.getNgayNhanThuc() == null ? null : Date.valueOf(ct.getNgayNhanThuc()));
            ps.setDate(4, ct.getNgayTraThuc() == null ? null : Date.valueOf(ct.getNgayTraThuc()));
            ps.setDate(5, ct.getNgayTra() == null ? null : Date.valueOf(ct.getNgayTra()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===================== LẤY TẤT CẢ CHI TIẾT =====================
    public List<ChiTietPhieuDatPhong> getAllChiTietPhieuDatPhong() {
        List<ChiTietPhieuDatPhong> dsCT = new ArrayList<>();

        String sql = """
            SELECT ctpdp.maPhieuDatPhong, ctpdp.maPhong,
                   ctpdp.ngayNhanThuc, ctpdp.ngayTraThuc, ctpdp.ngayTra,
                   p.trangThai,
                   lp.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, lp.moTa
            FROM ChiTietPhieuDatPhong ctpdp
            JOIN Phong p ON ctpdp.maPhong = p.maPhong
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsCT.add(mapResultSetToCT(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsCT;
    }

    // ===================== XÓA THEO MÃ PHIẾU =====================
    public boolean xoaCTTheoMaPhieu(String maPhieu) {
        String sql = "DELETE FROM ChiTietPhieuDatPhong WHERE maPhieuDatPhong = ?";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhieu);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===================== PHÒNG NHẬN TỪ HÔM NAY =====================
    public List<ChiTietPhieuDatPhong> getChiTietPhongTuHomNay() {
        List<ChiTietPhieuDatPhong> dsCT = new ArrayList<>();

        String sql = """
            SELECT ctpdp.maPhieuDatPhong, ctpdp.maPhong,
                   ctpdp.ngayNhanThuc, ctpdp.ngayTraThuc, ctpdp.ngayTra,
                   p.trangThai,
                   lp.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, lp.moTa
            FROM ChiTietPhieuDatPhong ctpdp
            JOIN Phong p ON ctpdp.maPhong = p.maPhong
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
            WHERE ctpdp.ngayNhanThuc >= CAST(GETDATE() AS DATE)
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsCT.add(mapResultSetToCT(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsCT;
    }

    // ===================== MAP RESULTSET =====================
    private ChiTietPhieuDatPhong mapResultSetToCT(ResultSet rs) throws SQLException {

        PhieuDatPhong phieu = new PhieuDatPhong(rs.getString("maPhieuDatPhong"));

        LoaiPhong loai = new LoaiPhong(
                rs.getString("maLoaiPhong"),
                rs.getString("tenLoaiPhong"),
                rs.getInt("sucChua"),
                rs.getDouble("gia"),
                rs.getString("moTa")
        );

        Phong phong = new Phong(
                rs.getString("maPhong"),
                loai,
                rs.getString("trangThai")
        );

        LocalDate ngayNhan = rs.getDate("ngayNhanThuc") == null ? null
                : rs.getDate("ngayNhanThuc").toLocalDate();

        LocalDate ngayTraThuc = rs.getDate("ngayTraThuc") == null ? null
                : rs.getDate("ngayTraThuc").toLocalDate();

        LocalDate ngayTra = rs.getDate("ngayTra") == null ? null
                : rs.getDate("ngayTra").toLocalDate();

        return new ChiTietPhieuDatPhong(phieu, phong, ngayNhan, ngayTraThuc, ngayTra);
    }
    public boolean isPhongDangO(String maPhong) {
	    String sql = """
	        SELECT COUNT(*) AS cnt
	        FROM ChiTietPhieuDatPhong ct
	        JOIN PhieuDatPhong pdp
	            ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
	        WHERE pdp.trangThai = N'Đang ở'
	          AND ct.maPhong = ?
	          AND ct.ngayNhanThuc <= CAST(GETDATE() AS DATE)
	          AND ct.ngayTraThuc  > CAST(GETDATE() AS DATE)
	    """;

	    try (Connection con = ConnectDB.getInstance().getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, maPhong);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("cnt") > 0;
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}
}
