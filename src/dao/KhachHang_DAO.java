package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHang_DAO {

    private ArrayList<KhachHang> dskh;

    public KhachHang_DAO() {
        dskh = new ArrayList<>();
    }

    // 🔹 Lấy toàn bộ khách hàng
    public List<KhachHang> getAllKhachHang() {
        dskh.clear();
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "SELECT maKhachHang, hoTen, soDienThoai, laNguoiVietNam FROM KhachHang";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String maKH = rs.getString("maKhachHang");
                String hoTen = rs.getString("hoTen");
                String soDienThoai = rs.getString("soDienThoai");
                boolean laNguoiVietNam = rs.getBoolean("laNguoiVietNam");

                KhachHang kh = new KhachHang(maKH, hoTen, soDienThoai, laNguoiVietNam);
                dskh.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dskh;
    }

    // 🔹 Lấy khách hàng theo mã
    public KhachHang getKhachHangTheoMa(String maKH) {
        KhachHang kh = null;
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hoTen = rs.getString("hoTen");
                String soDienThoai = rs.getString("soDienThoai");
                boolean laNguoiVietNam = rs.getBoolean("laNguoiVietNam");
                kh = new KhachHang(maKH, hoTen, soDienThoai, laNguoiVietNam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kh;
    }

    // 🔹 Lấy khách hàng theo SĐT (chính xác)
    public KhachHang getKhachHangTheoSDT(String sdt) {
        KhachHang kh = null;
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String maKH = rs.getString("maKhachHang");
                String hoTen = rs.getString("hoTen");
                boolean laNguoiVietNam = rs.getBoolean("laNguoiVietNam");
                kh = new KhachHang(maKH, hoTen, sdt, laNguoiVietNam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }

    // 🔹 Tìm khách hàng theo SĐT gần đúng (dành cho tìm kiếm)
    public List<KhachHang> timKhachHangTheoSDTGanDung(String soDT) {
        List<KhachHang> dsKhachHang = new ArrayList<>();
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "SELECT * FROM KhachHang WHERE soDienThoai LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + soDT + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String maKH = rs.getString("maKhachHang");
                String hoTen = rs.getString("hoTen");
                String soDienThoai = rs.getString("soDienThoai");
                boolean laNguoiVietNam = rs.getBoolean("laNguoiVietNam");

                KhachHang kh = new KhachHang(maKH, hoTen, soDienThoai, laNguoiVietNam);
                dsKhachHang.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKhachHang;
    }

    // 🔹 Tạo mã khách hàng tự động theo ngày
    public String taoMaKhachHangTuDong() {
        LocalDate ngayHienTai = LocalDate.now();
        String ngay = String.format("%02d", ngayHienTai.getDayOfMonth());
        String thang = String.format("%02d", ngayHienTai.getMonthValue());
        String nam = String.valueOf(ngayHienTai.getYear());

        List<KhachHang> danhSach = getAllKhachHang();
        int dem = 0;
        for (KhachHang kh : danhSach) {
            if (kh.getMaKhachHang().contains("KH" + ngay + thang + nam)) {
                dem++;
            }
        }
        dem++;
        return String.format("KH%s%s%s%03d", ngay, thang, nam, dem);
    }

    // 🔹 Thêm khách hàng
    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKhachHang, hoTen, soDienThoai, laNguoiVietNam) VALUES (?, ?, ?, ?)";
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getSoDienThoai());
            ps.setBoolean(4, kh.LaNguoiVietNam());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật khách hàng
    public boolean capNhatKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET hoTen = ?, soDienThoai = ?, laNguoiVietNam = ? WHERE maKhachHang = ?";
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSoDienThoai());
            ps.setBoolean(3, kh.LaNguoiVietNam());
            ps.setString(4, kh.getMaKhachHang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isSoDienThoaiTonTaiKhacMa(String sdt, String maKH) {
        String sql = """
            SELECT 1 FROM KhachHang
            WHERE soDienThoai = ?
              AND maKhachHang <> ?
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sdt);
            ps.setString(2, maKH);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            return false;
        }
    }
    
    public List<KhachHang> getKhachHangHomNay() {
        List<KhachHang> dsKH = new ArrayList<>();

        String sql = """
            SELECT maKhachHang, hoTen, soDienThoai, laNguoiVietNam
            FROM KhachHang
            WHERE ngayTao = CAST(GETDATE() AS DATE)
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("maKhachHang"),
                    rs.getString("hoTen"),
                    rs.getString("soDienThoai"),
                    rs.getBoolean("laNguoiVietNam")
                );
                dsKH.add(kh);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsKH;
    }
}
