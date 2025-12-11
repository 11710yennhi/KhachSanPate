package dao;

import connectDB.ConnectDB;
import entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO {

    // Lấy tất cả hóa đơn
	public List<HoaDon> getAllHoaDon() {
	    List<HoaDon> ds = new ArrayList<>();

	    String sql =
	            "SELECT hd.*, p.maPhong, pdp.ngayTao " +
	            "FROM HoaDon hd " +
	            "JOIN PhieuDatPhong pdp ON hd.maPhieuDatPhong = pdp.maPhieuDatPhong " +
	            "JOIN ChiTietPhieuDatPhong ctpdp ON ctpdp.maPhieuDatPhong = pdp.maPhieuDatPhong " +
	            "JOIN Phong p ON p.maPhong = ctpdp.maPhong";

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            String maHD = rs.getString("maHoaDon");
	            String maPDP = rs.getString("maPhieuDatPhong");
	            String maKM = rs.getString("maKhuyenMai");
	            String phuongThucTT = rs.getString("phuongThucThanhToan");

	            // tạo đối tượng theo constructor của bạn
	            HoaDon hd = new HoaDon(maHD, maPDP, maKM, phuongThucTT);

	            ds.add(hd);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return ds;
	}


//
//
//    // Lấy hóa đơn theo mã
//    public HoaDon getHoaDon(String maHDSearch) {
//
//        String sql =
//                "SELECT hd.*, p.maPhong, pdp.ngayTao " +
//                "FROM HoaDon hd " +
//                "JOIN PhieuDatPhong pdp ON hd.maPhieuDatPhong = pdp.maPhieuDatPhong " +
//                "JOIN ChiTietPhieuDatPhong ctpdp ON ctpdp.maPhieuDatPhong = pdp.maPhieuDatPhong " +
//                "JOIN Phong p ON p.maPhong = ctpdp.maPhong " +
//                "WHERE hd.maHoaDon = ?";
//
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, maHDSearch);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//
//                    String maHD = rs.getString("maHoaDon");
//                    String maPDP = rs.getString("maPhieuDatPhong");
//                    String maKM = rs.getString("maKhuyenMai");
//
//                    double tongTien = rs.getDouble("tongTien");
//                    double tongThanhToan = rs.getDouble("tongThanhToan");
//                    String phuongThucTT = rs.getString("phuongThucThanhToan");
//
//                    LocalDate ngayTao = rs.getDate("ngayTao").toLocalDate();
//                    String maPhong = rs.getString("maPhong");
//
//                    Phong phong = new Phong(maPhong);
//                    KhuyenMai km = new KhuyenMai(maKM);
//                    PhieuDatPhong pdp = new PhieuDatPhong(maPDP);
//
//                    return new HoaDon(maHD, pdp, km, tongTien, tongThanhToan, phuongThucTT, ngayTao, phong);
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//
//    // Xóa hóa đơn
//    public boolean xoaHoaDon(String maHD) {
//        String sql = "DELETE FROM HoaDon WHERE maHoaDon = ?";
//
//        try (Connection con = ConnectDB.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, maHD);
//            return ps.executeUpdate() > 0;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

}
