package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoan_DAO {
	public TaiKhoan_DAO() {
	}
	public ArrayList<TaiKhoan> docTaiKhoanVaMatKhau() {
	    ArrayList<TaiKhoan> dsTaiKhoan = new ArrayList<>();
	    try {
	        Connection con = ConnectDB.getInstance().getConnection();
	        String sql = "SELECT maNhanVien, matKhau FROM TaiKhoan";
	        Statement statement = con.createStatement();
	        ResultSet rs = statement.executeQuery(sql);

	        while (rs.next()) {
	            String maNV = rs.getString("maNhanVien");
	            String matKhau = rs.getString("matKhau");

	            TaiKhoan tk = new TaiKhoan(maNV, matKhau);
	            dsTaiKhoan.add(tk);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return dsTaiKhoan;
	}
	public boolean themTaiKhoanMoi(String maNhanVien, String matKhau) {
	    try {
	        Connection con = ConnectDB.getInstance().getConnection();
	        String sql = "INSERT INTO TaiKhoan (maNhanVien, matKhau) VALUES (?, ?)";
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, maNhanVien);
	        stmt.setString(2, matKhau);

	        return stmt.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean doiMatKhau(String maNV, String matKhauMoi) {
	    try {
	        Connection con = ConnectDB.getInstance().getConnection();
	        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE maNhanVien = ?";
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, matKhauMoi);
	        stmt.setString(2, maNV);

	        return stmt.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean kiemTraDangNhap(String maNhanVien, String matKhau) {
	    try {
	        Connection con = ConnectDB.getInstance().getConnection();
	        String sql = "SELECT * FROM TaiKhoan WHERE maNhanVien = ? AND matKhau = ?";
	        PreparedStatement stmt = con.prepareStatement(sql);
	        stmt.setString(1, maNhanVien);
	        stmt.setString(2, matKhau);
	        
	        ResultSet rs = stmt.executeQuery();
	        return rs.next();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean taoTaiKhoanMacDinh(String maNhanVien) {
        try {
            String sql = """
                INSERT INTO TaiKhoan(maNhanVien, matKhau)
                VALUES (?, ?)
            """;

            PreparedStatement ps = ConnectDB.getInstance().getConnection().prepareStatement(sql);

            ps.setString(1, maNhanVien);
            ps.setString(2, "123456"); // mật khẩu mặc định

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
	public boolean kiemTraTonTai(String maNV) {
	    try {
	        String sql = "SELECT maNhanVien FROM TaiKhoan WHERE maNhanVien = ?";
	        PreparedStatement ps = ConnectDB.getInstance()
	                                        .getConnection()
	                                        .prepareStatement(sql);
	        ps.setString(1, maNV);
	        ResultSet rs = ps.executeQuery();
	        return rs.next();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

}
