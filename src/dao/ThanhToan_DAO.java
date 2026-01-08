package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import connectDB.ConnectDB;

public class ThanhToan_DAO {

    public boolean thanhToan(
            String maHD,
            String maPDP,
            String maKM,
            String phuongThucTT,
            long tongTienPhong,
            long tongCPPS,
            long tongThanhToan,
            LocalDate ngayTao
    ) {

        Connection con = null;

        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false); // 🔥 BẮT ĐẦU TRANSACTION

            // 1️⃣ Cập nhật Phiếu Đặt Phòng
            String sqlPDP = """
                UPDATE PhieuDatPhong
                SET trangThai = N'Hoàn thành'
                WHERE maPhieuDatPhong = ?
            """;
            try (PreparedStatement ps = con.prepareStatement(sqlPDP)) {
                ps.setString(1, maPDP);
                ps.executeUpdate();
            }

            // 3️⃣ Insert Hóa Đơn
            String sqlHD = """
                INSERT INTO HoaDon
                (maHoaDon, maPhieuDatPhong, maKhuyenMai, phuongThucThanhToan,
                 tongTienPhong, tongTienCPPS, tongThanhToan, ngayTao)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

            try (PreparedStatement ps = con.prepareStatement(sqlHD)) {
                ps.setString(1, maHD);
                ps.setString(2, maPDP);
                ps.setString(3, maKM);
                ps.setString(4, phuongThucTT);
                ps.setLong(5, tongTienPhong);
                ps.setLong(6, tongCPPS);
                ps.setLong(7, tongThanhToan);
                ps.setDate(8, Date.valueOf(ngayTao));
                ps.executeUpdate();
            }

            con.commit(); // ✅ THÀNH CÔNG
            return true;

        } catch (Exception e) {
            try {
                if (con != null) con.rollback(); // ❌ LỖI → ROLLBACK
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

