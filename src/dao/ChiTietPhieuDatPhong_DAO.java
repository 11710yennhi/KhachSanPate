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

    public List<ChiTietPhieuDatPhong> getChiTietTheoMaPhieu(String maPhieuDatPhong) {
        List<ChiTietPhieuDatPhong> dsCT = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getInstance().getConnection();

            String sql = """
                SELECT ctpdp.maPhieuDatPhong, ctpdp.maPhong,
                       ctpdp.ngayNhanThuc, ctpdp.ngayTraThuc, ctpdp.ngayTra,
                       ctpdp.trangThai AS trangThaiCT,
                       p.trangThai AS trangThaiPhong,
                       lp.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, lp.moTa
                FROM ChiTietPhieuDatPhong ctpdp
                JOIN Phong p ON ctpdp.maPhong = p.maPhong
                JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
                WHERE ctpdp.maPhieuDatPhong = ?
            """;

            ps = con.prepareStatement(sql);
            ps.setString(1, maPhieuDatPhong);
            rs = ps.executeQuery();

            while (rs.next()) {
                PhieuDatPhong phieu = new PhieuDatPhong(maPhieuDatPhong);

                String maLoaiPhong = rs.getString("maLoaiPhong");
                String tenLoaiPhong = rs.getString("tenLoaiPhong");
                int sucChua = rs.getInt("sucChua");
                double gia = rs.getDouble("gia");
                String moTa = rs.getString("moTa");

                LoaiPhong loai = new LoaiPhong(maLoaiPhong, tenLoaiPhong, sucChua, gia, moTa);

                String maPhong = rs.getString("maPhong");
                String trangThaiPhong = rs.getString("trangThaiPhong");
                Phong phong = new Phong(maPhong, loai, trangThaiPhong);

                Date ngayNhanSQL = rs.getDate("ngayNhanThuc");
                Date ngayTraSQL = rs.getDate("ngayTraThuc");
                Date ngayTraThucTeSQL = rs.getDate("ngayTra");

                LocalDate ngayNhan = ngayNhanSQL != null ? ngayNhanSQL.toLocalDate() : null;
                LocalDate ngayTra = ngayTraSQL != null ? ngayTraSQL.toLocalDate() : null;
                LocalDate ngayTraThucTe = ngayTraThucTeSQL != null ? ngayTraThucTeSQL.toLocalDate() : null;

                String trangThaiChiTiet = rs.getString("trangThaiCT");

                ChiTietPhieuDatPhong ct =
                        new ChiTietPhieuDatPhong(phieu, phong, ngayNhan, ngayTra, trangThaiChiTiet, ngayTraThucTe);

                dsCT.add(ct);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return dsCT;
    }


    public boolean themChiTietPhieuDatPhong(ChiTietPhieuDatPhong ct) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean result = false;

        try {
            con = ConnectDB.getInstance().getConnection();

            String sql = """
                INSERT INTO ChiTietPhieuDatPhong
                    (maPhieuDatPhong, maPhong, ngayNhanThuc, ngayTraThuc, ngayTra, trangThai)
                VALUES (?, ?, ?, ?, ?, ?)
            """;

            ps = con.prepareStatement(sql);
            ps.setString(1, ct.getPhieuDatPhong().getMaPhieuDatPhong());
            ps.setString(2, ct.getPhong().getMaPhong());

            if (ct.getNgayNhanThuc() != null)
                ps.setDate(3, Date.valueOf(ct.getNgayNhanThuc()));
            else
                ps.setNull(3, java.sql.Types.DATE);

            if (ct.getNgayTraThuc() != null)
                ps.setDate(4, Date.valueOf(ct.getNgayTraThuc()));
            else
                ps.setNull(4, java.sql.Types.DATE);

            if (ct.getNgayTra() != null)
                ps.setDate(5, Date.valueOf(ct.getNgayTra()));
            else
                ps.setNull(5, java.sql.Types.DATE);

            ps.setString(6, ct.getTrangThai());

            int rows = ps.executeUpdate();
            result = rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    public List<ChiTietPhieuDatPhong> getAllChiTietPhieuDatPhong() {
        List<ChiTietPhieuDatPhong> dsCT = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getInstance().getConnection();

            String sql = """
                SELECT ctpdp.maPhieuDatPhong, ctpdp.maPhong,
                       ctpdp.ngayNhanThuc, ctpdp.ngayTraThuc, ctpdp.ngayTra,
                       ctpdp.trangThai AS trangThaiCT,
                       p.trangThai AS trangThaiPhong,
                       lp.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, lp.moTa
                FROM ChiTietPhieuDatPhong ctpdp
                JOIN Phong p ON ctpdp.maPhong = p.maPhong
                JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
            """;

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {

                String maPhieuDatPhong = rs.getString("maPhieuDatPhong");
                PhieuDatPhong phieu = new PhieuDatPhong(maPhieuDatPhong);

                String maLoaiPhong = rs.getString("maLoaiPhong");
                String tenLoaiPhong = rs.getString("tenLoaiPhong");
                int sucChua = rs.getInt("sucChua");
                double gia = rs.getDouble("gia");
                String moTa = rs.getString("moTa");

                LoaiPhong loai = new LoaiPhong(maLoaiPhong, tenLoaiPhong, sucChua, gia, moTa);

                String maPhong = rs.getString("maPhong");
                String trangThaiPhong = rs.getString("trangThaiPhong");
                Phong phong = new Phong(maPhong, loai, trangThaiPhong);

                Date ngayNhanSQL = rs.getDate("ngayNhanThuc");
                Date ngayTraSQL = rs.getDate("ngayTraThuc");
                Date ngayTraThucTeSQL = rs.getDate("ngayTra");

                LocalDate ngayNhan = ngayNhanSQL != null ? ngayNhanSQL.toLocalDate() : null;
                LocalDate ngayTra = ngayTraSQL != null ? ngayTraSQL.toLocalDate() : null;
                LocalDate ngayTraThucTe = ngayTraThucTeSQL != null ? ngayTraThucTeSQL.toLocalDate() : null;

                String trangThaiChiTiet = rs.getString("trangThaiCT");

                ChiTietPhieuDatPhong ct =
                        new ChiTietPhieuDatPhong(phieu, phong, ngayNhan, ngayTra, trangThaiChiTiet, ngayTraThucTe);

                dsCT.add(ct);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return dsCT;
    }

    public boolean xoaCTTheoMaPhieu(String maPhieu) {
        String sql = "DELETE FROM ChiTietPhieuDatPhong WHERE maPhieuDatPhong = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhieu);
            return ps.executeUpdate() >= 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<ChiTietPhieuDatPhong> getChiTietPhongTuHomNay() {
        List<ChiTietPhieuDatPhong> dsCT = new ArrayList<>();

        String sql = """
            SELECT ctpdp.maPhieuDatPhong, ctpdp.maPhong,
                   ctpdp.ngayNhanThuc, ctpdp.ngayTraThuc, ctpdp.ngayTra,
                   ctpdp.trangThai AS trangThaiCT,
                   p.trangThai AS trangThaiPhong,
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
                    rs.getString("trangThaiPhong")
                );

                LocalDate ngayNhan = rs.getDate("ngayNhanThuc").toLocalDate();
                LocalDate ngayTra = rs.getDate("ngayTraThuc") != null ? rs.getDate("ngayTraThuc").toLocalDate() : null;
                LocalDate ngayTraThucTe = rs.getDate("ngayTra") != null ? rs.getDate("ngayTra").toLocalDate() : null;

                ChiTietPhieuDatPhong ct = new ChiTietPhieuDatPhong(
                        phieu, phong, ngayNhan, ngayTra, rs.getString("trangThaiCT"), ngayTraThucTe);

                dsCT.add(ct);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsCT;
    }


}
