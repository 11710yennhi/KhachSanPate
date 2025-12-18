package dao;

import connectDB.ConnectDB;
import entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class HoaDon_DAO {
//	private PhieuDatPhong_DAO pdtDAO = new PhieuDatPhong_DAO();
//    private ChiTietPhieuDatPhong_DAO ctPDPDAO = new ChiTietPhieuDatPhong_DAO();
//    private ChiTietChiPhiPhatSinh_DAO cpDAO = new ChiTietChiPhiPhatSinh_DAO();
//    private KhuyenMai_DAO kmDAO = new KhuyenMai_DAO();
	public HoaDon_DAO() {
    }
	
	public List<Object[]> getDanhSachHoaDon() {
	    List<Object[]> list = new ArrayList<>();

	    String sql = """
	        SELECT 
	            maHoaDon,
	            maPhieuDatPhong,
	            maKhuyenMai,
	            phuongThucThanhToan,
	            tongThanhToan,
	            ngayTao
	        FROM HoaDon
	        ORDER BY ngayTao DESC
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            Object[] row = new Object[]{
	                rs.getString("maHoaDon"),
	                rs.getString("maPhieuDatPhong"),
	                rs.getString("maKhuyenMai"),
	                rs.getString("phuongThucThanhToan"),
	                rs.getDouble("tongThanhToan"),
	                rs.getDate("ngayTao")
	            };
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}
//========================Xử lý hiển thị thông tin cơ bản ======================
	public Object[] getThongTinHoaDon(String maHoaDon) {

        String sql = """
            SELECT 
                hd.maHoaDon,
                hd.ngayTao,
                nv.hoTen AS tenNhanVien,
                STRING_AGG(p.maPhong, ', ') AS danhSachPhong
            FROM HoaDon hd
            JOIN PhieuDatPhong pdp ON hd.maPhieuDatPhong = pdp.maPhieuDatPhong
            JOIN NhanVien nv ON pdp.maNhanVien = nv.maNhanVien
            JOIN ChiTietPhieuDatPhong ctpdp ON pdp.maPhieuDatPhong = ctpdp.maPhieuDatPhong
            JOIN Phong p ON ctpdp.maPhong = p.maPhong
            WHERE hd.maHoaDon = ?
            GROUP BY hd.maHoaDon, hd.ngayTao, nv.hoTen
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Object[] {
                    rs.getString("maHoaDon"),
                    rs.getDate("ngayTao"),
                    rs.getString("tenNhanVien"),
                    rs.getString("danhSachPhong")
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
//=======================Xử lý bảng chi tiết phòng===========================
	public void loadCTPhongByMaPDP(String maPDP, DefaultTableModel model) {

	    String sql = """
	        SELECT 
	            p.maPhong,
	            lp.tenLoaiPhong,
	            ctpdp.ngayNhanThuc,
	            ctpdp.ngayTra,
	            ctpdp.ngayTraThuc,
	            DATEDIFF(DAY, ctpdp.ngayNhanThuc, ctpdp.ngayTraThuc) AS soDem,
	            lp.gia,
	            DATEDIFF(DAY, ctpdp.ngayNhanThuc, ctpdp.ngayTraThuc) * lp.gia AS thanhTien
	        FROM ChiTietPhieuDatPhong ctpdp
	        JOIN Phong p ON ctpdp.maPhong = p.maPhong
	        JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
	        WHERE ctpdp.maPhieuDatPhong = ?
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, maPDP);
	        ResultSet rs = ps.executeQuery();

	        model.setRowCount(0);
	        int stt = 1;

	        while (rs.next()) {
	            model.addRow(new Object[]{
	                stt++,
	                rs.getString("maPhong"),
	                rs.getString("tenLoaiPhong"),
	                rs.getDate("ngayNhanThuc"),
	                rs.getDate("ngayTra"),
	                rs.getDate("ngayTraThuc"),
	                rs.getInt("soDem"),
	                rs.getDouble("gia"),
	                rs.getDouble("thanhTien")
	            });
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
//===========================Load chí phí phát sinh======================
	public void loadCTCPPSByMaPDP(String maPDP, DefaultTableModel model) {

	    String sql = """
	        SELECT 
	            cpps.tenChiPhiPhatSinh,
	            cpps.gia,
	            ct.soLuong,
	            cpps.gia * ct.soLuong AS thanhTien
	        FROM ChiTietChiPhiPhatSinh ct
	        JOIN ChiPhiPhatSinh cpps 
	            ON ct.maChiPhiPhatSinh = cpps.maChiPhiPhatSinh
	        WHERE ct.maPhieuDatPhong = ?
	    """;

	    try (Connection con = ConnectDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, maPDP);
	        ResultSet rs = ps.executeQuery();

	        model.setRowCount(0);
	        int stt = 1;

	        while (rs.next()) {
	            model.addRow(new Object[]{
	                stt++,
	                rs.getString("tenChiPhiPhatSinh"),
	                rs.getDouble("gia"),
	                rs.getInt("soLuong"),
	                rs.getDouble("thanhTien")
	            });
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
//=======================Lấy hóa đơn hôm nay==========================
	public ResultSet getHoaDonHomNay() {
	    String sql = """
	        SELECT 
	            maHoaDon,
	            maPhieuDatPhong,
	            maKhuyenMai,
	            phuongThucThanhToan,
	            tongThanhToan,
	            ngayTao
	        FROM HoaDon
	        WHERE ngayTao = CAST(GETDATE() AS DATE)
	        ORDER BY ngayTao DESC
	    """;

	    try {
	        Connection con = ConnectDB.getInstance().getConnection();
	        return con.prepareStatement(sql).executeQuery();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	//=======================Lấy hóa đơn theo datechooser==========================	
	public ResultSet getHoaDonTheoKhoangNgay(java.util.Date tuNgay, java.util.Date denNgay) {

	    String sql = """
	        SELECT 
	            maHoaDon,
	            maPhieuDatPhong,
	            maKhuyenMai,
	            phuongThucThanhToan,
	            tongThanhToan,
	            ngayTao
	        FROM HoaDon
	        WHERE ngayTao BETWEEN ? AND ?
	        ORDER BY ngayTao DESC
	    """;

	    try {
	        Connection con = ConnectDB.getInstance().getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);

	        ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
	        ps.setDate(2, new java.sql.Date(denNgay.getTime()));

	        return ps.executeQuery();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
//============================= tìm hóa đơn theo mã hóa đơn =========================
	public ResultSet timHoaDonTheoMa(String maHD) {

	    String sql = """
	        SELECT 
	            maHoaDon,
	            maPhieuDatPhong,
	            maKhuyenMai,
	            phuongThucThanhToan,
	            tongThanhToan,
	            ngayTao
	        FROM HoaDon
	        WHERE maHoaDon LIKE ?
	        ORDER BY ngayTao DESC
	    """;

	    try {
	        Connection con = ConnectDB.getInstance().getConnection();
	        PreparedStatement ps = con.prepareStatement(sql);

	        ps.setString(1, "%" + maHD + "%");

	        return ps.executeQuery();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
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
	//======DASHBOARD
	
		// 1) Doanh thu theo THÁNG trong NĂM
	    // =========================
	    public double[] getDoanhThu12ThangTrongNam_Chot(int nam) {
	        double[] arr = new double[12]; // index 0..11 tương ứng tháng 1..12
	        String sql = """
	            SELECT MONTH(hd.ngayTao) AS thang, COALESCE(SUM(hd.tongThanhToan), 0) AS doanhThu
	            FROM HoaDon hd
	            WHERE YEAR(hd.ngayTao) = ?
	            GROUP BY MONTH(hd.ngayTao)
	            ORDER BY MONTH(hd.ngayTao)
	        """;

	        try (Connection con = ConnectDB.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, nam);
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    int thang = rs.getInt("thang");
	                    double dt = rs.getDouble("doanhThu");
	                    if (thang >= 1 && thang <= 12) arr[thang - 1] = dt;
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return arr;
	    }
	    
	   
	    
	    // 3) Tiền mặt đã kết trong ngày (theo Hóa đơn)
	    // =========================
	    public double getTienMatKetTrongNgay(LocalDate ngay) {
	        double result = 0;
	        String sql = """
	            SELECT COALESCE(SUM(hd.tongThanhToan), 0) AS tienMat
	            FROM HoaDon hd
	            WHERE hd.ngayTao = ?
	              AND (hd.phuongThucThanhToan LIKE N'%Tiền mặt%')
	        """;

	        try (Connection con = ConnectDB.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setDate(1, java.sql.Date.valueOf(ngay));
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) result = rs.getDouble("tienMat");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }

	    //================THONG KE
	 // 1) Doanh thu tiền phòng theo tháng/năm (lấy từ HoaDon.tongTienPhong)
	    public double getDoanhThuPhongTheoThang(int thang, int nam) {
	        double result = 0;
	        String sql = """
	            SELECT COALESCE(SUM(hd.tongTienPhong), 0) AS doanhThuPhong
	            FROM HoaDon hd
	            WHERE YEAR(hd.ngayTao) = ? AND MONTH(hd.ngayTao) = ?
	        """;

	        try (Connection con = ConnectDB.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, nam);
	            ps.setInt(2, thang);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) result = rs.getDouble("doanhThuPhong");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }

	    // 2) Doanh thu dịch vụ theo tháng/năm
	    //    (tính từ chi tiết CPPS, nhưng lọc theo tháng/năm của HoaDon.ngayTao)
	    public double getDoanhThuDichVuTheoThang(int thang, int nam) {
	        double result = 0;
	        String sql = """
	            SELECT COALESCE(SUM(cps.gia * ct.soLuong), 0) AS doanhThuDichVu
	            FROM HoaDon hd
	            JOIN PhieuDatPhong pdp ON hd.maPhieuDatPhong = pdp.maPhieuDatPhong
	            JOIN ChiTietChiPhiPhatSinh ct ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
	            JOIN ChiPhiPhatSinh cps ON cps.maChiPhiPhatSinh = ct.maChiPhiPhatSinh
	            WHERE YEAR(hd.ngayTao) = ? AND MONTH(hd.ngayTao) = ?
	              AND LOWER(cps.loaiChiPhiPhatSinh) LIKE N'%dịch vụ%'
	        """;

	        try (Connection con = ConnectDB.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, nam);
	            ps.setInt(2, thang);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) result = rs.getDouble("doanhThuDichVu");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }

	    // 3) Doanh thu phí phạt theo tháng/năm
	    public double getDoanhThuPhatTheoThang(int thang, int nam) {
	        double result = 0;
	        String sql = """
	            SELECT COALESCE(SUM(cps.gia * ct.soLuong), 0) AS doanhThuPhat
	            FROM HoaDon hd
	            JOIN PhieuDatPhong pdp ON hd.maPhieuDatPhong = pdp.maPhieuDatPhong
	            JOIN ChiTietChiPhiPhatSinh ct ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
	            JOIN ChiPhiPhatSinh cps ON cps.maChiPhiPhatSinh = ct.maChiPhiPhatSinh
	            WHERE YEAR(hd.ngayTao) = ? AND MONTH(hd.ngayTao) = ?
	              AND LOWER(cps.loaiChiPhiPhatSinh) LIKE N'%phạt%'
	        """;

	        try (Connection con = ConnectDB.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, nam);
	            ps.setInt(2, thang);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) result = rs.getDouble("doanhThuPhat");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }

	    // 4) Tổng doanh thu tháng (lấy từ HoaDon.tongThanhToan) => số chốt
	    public double getTongDoanhThuThang(int thang, int nam) {
	        double result = 0;
	        String sql = """
	            SELECT COALESCE(SUM(hd.tongThanhToan), 0) AS tongDoanhThu
	            FROM HoaDon hd
	            WHERE YEAR(hd.ngayTao) = ? AND MONTH(hd.ngayTao) = ?
	        """;

	        try (Connection con = ConnectDB.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, nam);
	            ps.setInt(2, thang);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) result = rs.getDouble("tongDoanhThu");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	    
	    public double getTongDoanhThuNgay() {
	        double result = 0;
	        String sql = """
	            SELECT COALESCE(SUM(hd.tongThanhToan), 0) AS tongDoanhThu
	            FROM HoaDon hd
	            WHERE hd.ngayTao = Getdate()
	        """;

	        try (Connection con = ConnectDB.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) result = rs.getDouble("tongDoanhThu");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	    
	    public double getTongDoanhThuThangTruoc(int thang, int nam) {
	        int thangTruoc = thang - 1;
	        int namTruoc = nam;
	        if (thangTruoc == 0) {
	            thangTruoc = 12;
	            namTruoc = nam - 1;
	        }
	        return getTongDoanhThuThang(thangTruoc, namTruoc);
	    }

	    public double getTongDoanhThuThangCungKyNamTruoc(int thang, int nam) {
	        return getTongDoanhThuThang(thang, nam - 1);
	    }
	    
	    public double getDoanhThuPhongTheoNam(int nam) {
	        double result = 0;
	        String sql = """
	            SELECT COALESCE(SUM(hd.tongTienPhong), 0) AS doanhThuPhong
	            FROM HoaDon hd
	            WHERE YEAR(hd.ngayTao) = ?
	        """;

	        try (Connection con = ConnectDB.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, nam);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) result = rs.getDouble("doanhThuPhong");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }

	    public double getDoanhThuDichVuTheoNam(int nam) {
	        double result = 0;
	        String sql = """
	            SELECT COALESCE(SUM(cps.gia * ct.soLuong), 0) AS doanhThuDichVu
	            FROM HoaDon hd
	            JOIN PhieuDatPhong pdp ON hd.maPhieuDatPhong = pdp.maPhieuDatPhong
	            JOIN ChiTietChiPhiPhatSinh ct ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
	            JOIN ChiPhiPhatSinh cps ON cps.maChiPhiPhatSinh = ct.maChiPhiPhatSinh
	            WHERE YEAR(hd.ngayTao) = ?
	              AND LOWER(cps.loaiChiPhiPhatSinh) LIKE N'%dịch vụ%'
	        """;

	        try (Connection con = ConnectDB.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, nam);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) result = rs.getDouble("doanhThuDichVu");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }

	    public double getDoanhThuPhatTheoNam(int nam) {
	        double result = 0;
	        String sql = """
	            SELECT COALESCE(SUM(cps.gia * ct.soLuong), 0) AS doanhThuPhat
	            FROM HoaDon hd
	            JOIN PhieuDatPhong pdp ON hd.maPhieuDatPhong = pdp.maPhieuDatPhong
	            JOIN ChiTietChiPhiPhatSinh ct ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
	            JOIN ChiPhiPhatSinh cps ON cps.maChiPhiPhatSinh = ct.maChiPhiPhatSinh
	            WHERE YEAR(hd.ngayTao) = ?
	              AND LOWER(cps.loaiChiPhiPhatSinh) LIKE N'%phạt%'
	        """;

	        try (Connection con = ConnectDB.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, nam);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) result = rs.getDouble("doanhThuPhat");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }

	    // Tổng doanh thu năm: lấy từ HoaDon.tongThanhToan (số chốt)
	    public double getTongDoanhThuNam(int nam) {
	        double result = 0;
	        String sql = """
	            SELECT COALESCE(SUM(hd.tongThanhToan), 0) AS tongDoanhThu
	            FROM HoaDon hd
	            WHERE YEAR(hd.ngayTao) = ?
	        """;

	        try (Connection con = ConnectDB.getInstance().getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, nam);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) result = rs.getDouble("tongDoanhThu");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return result;
	    }

	    public double getTongDoanhThuNamTruoc(int nam) {
	        return getTongDoanhThuNam(nam - 1);
	    }
	    
	    //  THÁNG CAO NHẤT / THẤP NHẤT (DÙNG TỔNG DOANH THU CHỐT)
	    // =========================

	    public int getThangDoanhThuCaoNhat(int nam) {
	        double max = -1;
	        int thangMax = 1;
	        for (int thang = 1; thang <= 12; thang++) {
	            double dt = getTongDoanhThuThang(thang, nam);
	            if (dt > max) {
	                max = dt;
	                thangMax = thang;
	            }
	        }
	        return thangMax;
	    }

	    public int getThangDoanhThuThapNhat(int nam) {
	        double min = Double.MAX_VALUE;
	        int thangMin = 1;
	        for (int thang = 1; thang <= 12; thang++) {
	            double dt = getTongDoanhThuThang(thang, nam);
	            if (dt < min) {
	                min = dt;
	                thangMin = thang;
	            }
	        }
	        return thangMin;
	    }
	    
}
