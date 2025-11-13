package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import connectDB.ConnectDB;
import entity.KhuyenMai;

public class KhuyenMai_DAO {
    private ArrayList<KhuyenMai> dskm;

    public KhuyenMai_DAO() {
        dskm = new ArrayList<KhuyenMai>();
    }

    // Đọc toàn bộ bảng KhuyenMai
    public List<KhuyenMai> docTuBang() {
        dskm.clear();
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "SELECT * FROM KhuyenMai";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String maKM = rs.getString("maKhuyenMai");
                String tenKM = rs.getString("tenKhuyenMai");
                LocalDate ngayTao = rs.getDate("ngayTao") != null ? rs.getDate("ngayTao").toLocalDate() : null;
                LocalDate ngayBatDau = rs.getDate("ngayBatDau") != null ? rs.getDate("ngayBatDau").toLocalDate() : null;
                LocalDate ngayKetThuc = rs.getDate("ngayKetThuc") != null ? rs.getDate("ngayKetThuc").toLocalDate() : null;
                String loaiKM = rs.getString("loaiKhuyenMai");
                double dieuKien = rs.getDouble("dieuKien");
                double giaTriGiam = rs.getDouble("giaTriGiam");

                KhuyenMai km = new KhuyenMai(maKM, tenKM, ngayTao, ngayBatDau, ngayKetThuc, loaiKM, dieuKien, giaTriGiam);
                dskm.add(km);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dskm;
    }

    // Lấy khuyến mãi theo mã
    public KhuyenMai getKhuyenMaiTheoMa(String maKM) {
        KhuyenMai km = null;
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "SELECT * FROM KhuyenMai WHERE maKhuyenMai = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maKM);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String tenKM = rs.getString("tenKhuyenMai");
                LocalDate ngayTao = rs.getDate("ngayTao") != null ? rs.getDate("ngayTao").toLocalDate() : null;
                LocalDate ngayBatDau = rs.getDate("ngayBatDau") != null ? rs.getDate("ngayBatDau").toLocalDate() : null;
                LocalDate ngayKetThuc = rs.getDate("ngayKetThuc") != null ? rs.getDate("ngayKetThuc").toLocalDate() : null;
                String loaiKM = rs.getString("loaiKhuyenMai");
                double dieuKien = rs.getDouble("dieuKien");
                double giaTriGiam = rs.getDouble("giaTriGiam");

                km = new KhuyenMai(maKM, tenKM, ngayTao, ngayBatDau, ngayKetThuc, loaiKM, dieuKien, giaTriGiam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return km;
    }

    // Thêm khuyến mãi
    public boolean create(KhuyenMai km) {
        int n = 0;
        String sql = "INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, ngayTao, ngayBatDau, ngayKetThuc, loaiKhuyenMai, dieuKien, giaTriGiam) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, km.getMaKhuyenMai());
            ps.setString(2, km.getTenKhuyenMai());

            if (km.getNgayTao() != null) {
                ps.setDate(3, java.sql.Date.valueOf(km.getNgayTao()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }

            if (km.getNgayBatDau() != null) {
                ps.setDate(4, java.sql.Date.valueOf(km.getNgayBatDau()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }

            if (km.getNgayKetThuc() != null) {
                ps.setDate(5, java.sql.Date.valueOf(km.getNgayKetThuc()));
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }

            ps.setString(6, km.getLoaiKhuyenMai());
            ps.setDouble(7, km.getDieuKien());
            ps.setDouble(8, km.getGiaTriGiam());

            n = ps.executeUpdate();
        } catch (java.sql.SQLIntegrityConstraintViolationException ex) {
            // Mã trùng (PRIMARY KEY) hoặc vi phạm ràng buộc duy nhất
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi: Mã khuyến mãi đã tồn tại hoặc vi phạm ràng buộc: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm Khuyến Mãi: " + ex.getMessage());
        }
        return n > 0;
    }


    // Xóa khuyến mãi
    public boolean delete(String maKM) {
        int n = 0;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "DELETE FROM KhuyenMai WHERE maKhuyenMai = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maKM);
            n = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // Cập nhật khuyến mãi
    public boolean update(KhuyenMai km) {
        int n = 0;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "UPDATE KhuyenMai SET tenKhuyenMai = ?, ngayTao = ?, ngayBatDau = ?, ngayKetThuc = ?, loaiKhuyenMai = ?, dieuKien = ?, giaTriGiam = ? WHERE maKhuyenMai = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, km.getTenKhuyenMai());
            ps.setDate(2, km.getNgayTao() != null ? Date.valueOf(km.getNgayTao()) : null);
            ps.setDate(3, km.getNgayBatDau() != null ? Date.valueOf(km.getNgayBatDau()) : null);
            ps.setDate(4, km.getNgayKetThuc() != null ? Date.valueOf(km.getNgayKetThuc()) : null);
            ps.setString(5, km.getLoaiKhuyenMai());
            ps.setDouble(6, km.getDieuKien());
            ps.setDouble(7, km.getGiaTriGiam());
            ps.setString(8, km.getMaKhuyenMai());

            n = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
}
