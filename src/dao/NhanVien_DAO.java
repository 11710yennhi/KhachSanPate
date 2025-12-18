package dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.NhanVien;

public class NhanVien_DAO {

    private static final DateTimeFormatter ID_DATE_FMT = DateTimeFormatter.ofPattern("ddMMyyyy");

    public List<NhanVien> docTuBang() {
        List<NhanVien> dsnv = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien ORDER BY maNhanVien";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) dsnv.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsnv;
    }

    public NhanVien findByMa(String maNhanVien) {
        String sql = "SELECT * FROM NhanVien WHERE maNhanVien = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NhanVien findBySoDT(String soDT) {
        String sql = "SELECT * FROM NhanVien WHERE soDienThoai = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, soDT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean create(NhanVien nv) {
        String sql = """
            INSERT INTO NhanVien (maNhanVien, hoTen, gioiTinh, ngaySinh, soDienThoai, email, chucVu, ngayTao, trangThai)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nv.getMaNhanVien());
            ps.setString(2, nv.getHoten());
            ps.setBoolean(3, nv.isGioiTinh());

            ps.setDate(4, Date.valueOf(nv.getNgaySinh())); // ngày sinh đã validate không null
            ps.setString(5, nv.getSoDienThoai());
            ps.setString(6, nv.getEmail());
            ps.setBoolean(7, nv.isChucVu());

            if (nv.getNgayTao() != null) ps.setDate(8, Date.valueOf(nv.getNgayTao()));
            else ps.setNull(8, Types.DATE);

            ps.setBoolean(9, nv.isTrangThai());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(NhanVien nv) {
        String sql = """
            UPDATE NhanVien
            SET hoTen = ?, gioiTinh = ?, ngaySinh = ?, soDienThoai = ?, email = ?, chucVu = ?, ngayTao = ?, trangThai = ?
            WHERE maNhanVien = ?
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nv.getHoten());
            ps.setBoolean(2, nv.isGioiTinh());
            ps.setDate(3, Date.valueOf(nv.getNgaySinh())); // ngày sinh đã validate không null
            ps.setString(4, nv.getSoDienThoai());
            ps.setString(5, nv.getEmail());
            ps.setBoolean(6, nv.isChucVu());

            if (nv.getNgayTao() != null) ps.setDate(7, Date.valueOf(nv.getNgayTao()));
            else ps.setNull(7, Types.DATE);

            ps.setBoolean(8, nv.isTrangThai());
            ps.setString(9, nv.getMaNhanVien());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE maNhanVien = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // NV + ddMMyyyy + 3 số (VD: NV20092025001)
    public String generateMaNhanVien(LocalDate ngayPhatSinh) {
        if (ngayPhatSinh == null) ngayPhatSinh = LocalDate.now();
        String datePart = ngayPhatSinh.format(ID_DATE_FMT);
        String prefix = "NV" + datePart;

        String sql = """
            SELECT MAX(CAST(RIGHT(maNhanVien, 3) AS INT)) AS maxSeq
            FROM NhanVien
            WHERE maNhanVien LIKE ?
        """;

        int next = 1;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int maxSeq = rs.getInt("maxSeq");
                    if (!rs.wasNull()) next = maxSeq + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prefix + String.format("%03d", next);
    }

    private NhanVien mapRow(ResultSet rs) throws SQLException {
        String maNV = rs.getString("maNhanVien");
        String hoTen = rs.getString("hoTen");
        boolean gioiTinh = rs.getBoolean("gioiTinh");

        Date ns = rs.getDate("ngaySinh");
        LocalDate ngaySinh = (ns != null) ? ns.toLocalDate() : null;

        String soDT = rs.getString("soDienThoai");
        String email = rs.getString("email");
        boolean chucVu = rs.getBoolean("chucVu");

        Date nt = rs.getDate("ngayTao");
        LocalDate ngayTao = (nt != null) ? nt.toLocalDate() : null;

        boolean trangThai = rs.getBoolean("trangThai");

        return new NhanVien(maNV, hoTen, gioiTinh, ngaySinh, soDT, email, chucVu, ngayTao, trangThai);
    }
}
