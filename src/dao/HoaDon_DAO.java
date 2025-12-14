package dao;

import connectDB.ConnectDB;
import entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO {
	
	public HoaDon_DAO() {
    }
	
	public boolean themHoaDon(
	        String maHD,
	        String maPDP,
	        String maKM,
	        String phuongThucThanhToan,
	        double tongTienPhong,
	        double tongTienCPPS,
	        double tongThanhToan,
	        LocalDate ngayTao
	) {

	    String sql = "INSERT INTO HoaDon(" +
	            "maHoaDon, maPhieuDatPhong, maKhuyenMai, phuongThucThanhToan, " +
	            "tongTienPhong, tongTienCPPS, tongThanhToan, ngayTao) " +
	            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	    try (Connection con = ConnectDB.getInstance().getConnection();
	         PreparedStatement stmt = con.prepareStatement(sql)) {

	        stmt.setString(1, maHD);
	        stmt.setString(2, maPDP);

	        // khuyến mãi
	        if (maKM == null || maKM.trim().isEmpty())
	            stmt.setNull(3, Types.VARCHAR);
	        else
	            stmt.setString(3, maKM);

	        stmt.setString(4, phuongThucThanhToan);

	        stmt.setDouble(5, tongTienPhong);
	        stmt.setDouble(6, tongTienCPPS);
	        stmt.setDouble(7, tongThanhToan);

	        stmt.setDate(8, Date.valueOf(ngayTao));

	        return stmt.executeUpdate() > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}



	public String getMaHoaDonCuoiTrongNgay(String ngay) {
	    String maCuoi = null;

	    try {
	        Connection con = ConnectDB.getInstance().getConnection();

	        String sql = "SELECT TOP 1 maHoaDon FROM HoaDon "
	                   + "WHERE maHoaDon LIKE ? "
	                   + "ORDER BY maHoaDon DESC";

	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, "HD" + ngay + "%");

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            maCuoi = rs.getString("maHoaDon");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return maCuoi;
	}

	
	public Date[] getNgayNhanTraByMaPDP(String maPDP) {
	    Date[] d = new Date[2];

	    String sql = """
	        SELECT ctpdp.ngayNhanThuc, ctpdp.ngayTraThuc
	        FROM ChiTietPhieuDatPhong ctpdp
	        JOIN PhieuDatPhong pdp ON ctpdp.maPhieuDatPhong = pdp.maPhieuDatPhong
	        WHERE pdp.maPhieuDatPhong = ?
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, maPDP);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            d[0] = rs.getDate("ngayNhanThuc");
	            d[1] = rs.getDate("ngayTraThuc");
	        } else {
	            System.out.println(">>> Không tìm thấy PDP trong ChiTietPhieuDatPhong");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return d;
	}

}
