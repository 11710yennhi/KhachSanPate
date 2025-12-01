package dao;

import java.sql.*;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.LoaiPhong;

public class LoaiPhong_DAO {

    // ✅ Lấy toàn bộ danh sách loại phòng trong CSDL
    public ArrayList<LoaiPhong> getAllLoaiPhong() {
        ArrayList<LoaiPhong> dsLoaiPhong = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();
            String sql = "SELECT * FROM LoaiPhong";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String maLoaiPhong = rs.getString("maLoaiPhong");
                String tenLoaiPhong = rs.getString("tenLoaiPhong");
                int sucChua = rs.getInt("sucChua");
                double gia = rs.getDouble("gia");
                String moTa = rs.getString("moTa");

                LoaiPhong lp = new LoaiPhong(maLoaiPhong, tenLoaiPhong, sucChua, gia, moTa);
                dsLoaiPhong.add(lp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return dsLoaiPhong;
    }

    // ✅ Thêm mới loại phòng
    public boolean insertLoaiPhong(LoaiPhong lp) {
        int n = 0;
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "INSERT INTO LoaiPhong (maLoaiPhong, tenLoaiPhong, sucChua, gia, moTa) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, lp.getMaLoaiPhong());
            ps.setString(2, lp.getTenLoaiPhong());
            ps.setInt(3, lp.getSucChua());
            ps.setDouble(4, lp.getGia());
            ps.setString(5, lp.getMoTa());
            n = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // ✅ Cập nhật loại phòng
    public boolean updateLoaiPhong(LoaiPhong lp) {
        int n = 0;
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "UPDATE LoaiPhong SET tenLoaiPhong=?, sucChua=?, gia=?, moTa=? WHERE maLoaiPhong=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, lp.getTenLoaiPhong());
            ps.setInt(2, lp.getSucChua());
            ps.setDouble(3, lp.getGia());
            ps.setString(4, lp.getMoTa());
            ps.setString(5, lp.getMaLoaiPhong());
            n = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // ✅ Xóa loại phòng
    public boolean deleteLoaiPhong(String maLoaiPhong) {
        int n = 0;
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "DELETE FROM LoaiPhong WHERE maLoaiPhong = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maLoaiPhong);
            n = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // ✅ Lấy loại phòng theo mã
    public LoaiPhong getLoaiPhongTheoMa(String maLoaiPhong) {
        LoaiPhong lp = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();
            String sql = "SELECT * FROM LoaiPhong WHERE maLoaiPhong = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maLoaiPhong);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String tenLoaiPhong = rs.getString("tenLoaiPhong");
                int sucChua = rs.getInt("sucChua");
                double gia = rs.getDouble("gia");
                String moTa = rs.getString("moTa");
                lp = new LoaiPhong(maLoaiPhong, tenLoaiPhong, sucChua, gia, moTa);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lp;
    }

    // ✅ Lấy loại phòng theo tên
    public LoaiPhong getLoaiPhongTheoTen(String tenLoaiPhong) {
        LoaiPhong lp = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDB.getConnection();
            String sql = "SELECT * FROM LoaiPhong WHERE tenLoaiPhong = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, tenLoaiPhong);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String maLoaiPhong = rs.getString("maLoaiPhong");
                int sucChua = rs.getInt("sucChua");
                double gia = rs.getDouble("gia");
                String moTa = rs.getString("moTa");
                lp = new LoaiPhong(maLoaiPhong, tenLoaiPhong, sucChua, gia, moTa);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lp;
    }

    // ✅ Hàm hỗ trợ cho GUI (alias)
    public LoaiPhong findByMa(String ma) {
        return getLoaiPhongTheoMa(ma);
    }
}
