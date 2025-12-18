package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connectDB.ConnectDB;
import entity.ChiTietPhieuDatPhong;
import entity.PhieuDatPhong;
import entity.Phong;
import entity.LoaiPhong;

public class ChiTietPhieuDatPhong_DAO {

    // ===================== LẤY CHI TIẾT THEO MÃ PHIẾU =====================
    public List<ChiTietPhieuDatPhong> getChiTietTheoMaPhieu(String maPhieuDatPhong) {
        List<ChiTietPhieuDatPhong> dsCT = new ArrayList<>();

        String sql = """
            SELECT ctpdp.maPhieuDatPhong, ctpdp.maPhong,
                   ctpdp.ngayNhanThuc, ctpdp.ngayTraThuc, ctpdp.ngayTra,
                   p.trangThai,
                   lp.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, lp.moTa
            FROM ChiTietPhieuDatPhong ctpdp
            JOIN Phong p ON ctpdp.maPhong = p.maPhong
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
            WHERE ctpdp.maPhieuDatPhong = ?
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhieuDatPhong);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                dsCT.add(mapResultSetToCT(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsCT;
    }

    // ===================== THÊM CHI TIẾT PHIẾU =====================
    public boolean themChiTietPhieuDatPhong(ChiTietPhieuDatPhong ct) {

        String sql = """
            INSERT INTO ChiTietPhieuDatPhong
                (maPhieuDatPhong, maPhong, ngayNhanThuc, ngayTraThuc, ngayTra)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ct.getPhieuDatPhong().getMaPhieuDatPhong());
            ps.setString(2, ct.getPhong().getMaPhong());

            ps.setDate(3, ct.getNgayNhanThuc() == null ? null : Date.valueOf(ct.getNgayNhanThuc()));
            ps.setDate(4, ct.getNgayTraThuc() == null ? null : Date.valueOf(ct.getNgayTraThuc()));
            ps.setDate(5, ct.getNgayTra() == null ? null : Date.valueOf(ct.getNgayTra()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===================== LẤY TẤT CẢ CHI TIẾT =====================
    public List<ChiTietPhieuDatPhong> getAllChiTietPhieuDatPhong() {
        List<ChiTietPhieuDatPhong> dsCT = new ArrayList<>();

        String sql = """
            SELECT ctpdp.maPhieuDatPhong, ctpdp.maPhong,
                   ctpdp.ngayNhanThuc, ctpdp.ngayTraThuc, ctpdp.ngayTra,
                   p.trangThai,
                   lp.maLoaiPhong, lp.tenLoaiPhong, lp.sucChua, lp.gia, lp.moTa
            FROM ChiTietPhieuDatPhong ctpdp
            JOIN Phong p ON ctpdp.maPhong = p.maPhong
            JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsCT.add(mapResultSetToCT(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsCT;
    }

    // ===================== XÓA THEO MÃ PHIẾU =====================
    public boolean xoaCTTheoMaPhieu(String maPhieu) {
        String sql = "DELETE FROM ChiTietPhieuDatPhong WHERE maPhieuDatPhong = ?";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maPhieu);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===================== PHÒNG NHẬN TỪ HÔM NAY =====================
    public List<ChiTietPhieuDatPhong> getChiTietPhongTuHomNay() {
        List<ChiTietPhieuDatPhong> dsCT = new ArrayList<>();

        String sql = """
            SELECT ctpdp.maPhieuDatPhong, ctpdp.maPhong,
                   ctpdp.ngayNhanThuc, ctpdp.ngayTraThuc, ctpdp.ngayTra,
                   p.trangThai,
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
                dsCT.add(mapResultSetToCT(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsCT;
    }

    // ===================== MAP RESULTSET =====================
    private ChiTietPhieuDatPhong mapResultSetToCT(ResultSet rs) throws SQLException {

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
                rs.getString("trangThai")
        );

        LocalDate ngayNhan = rs.getDate("ngayNhanThuc") == null ? null
                : rs.getDate("ngayNhanThuc").toLocalDate();

        LocalDate ngayTraThuc = rs.getDate("ngayTraThuc") == null ? null
                : rs.getDate("ngayTraThuc").toLocalDate();

        LocalDate ngayTra = rs.getDate("ngayTra") == null ? null
                : rs.getDate("ngayTra").toLocalDate();

        return new ChiTietPhieuDatPhong(phieu, phong, ngayNhan, ngayTraThuc, ngayTra);
    }
    public boolean isPhongDangO(String maPhong) {
	    String sql = """
	        SELECT COUNT(*) AS cnt
	        FROM ChiTietPhieuDatPhong ct
	        JOIN PhieuDatPhong pdp
	            ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
	        WHERE pdp.trangThai = N'Đang ở'
	          AND ct.maPhong = ?
	          AND ct.ngayNhanThuc <= CAST(GETDATE() AS DATE)
	          AND ct.ngayTraThuc  > CAST(GETDATE() AS DATE)
	    """;

	    try (Connection con = ConnectDB.getInstance().getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, maPhong);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("cnt") > 0;
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}
 // ===========DASHBOARD==============
    // 6) Cảnh báo hôm nay: cần check-in / check-out
    public int countCanCheckInHomNay() {
        int result = 0;
        String sql = """
            SELECT COUNT(DISTINCT ct.maPhieuDatPhong) AS cnt
        		FROM ChiTietPhieuDatPhong ct
        			JOIN PhieuDatPhong pdp ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
        		WHERE pdp.trangThai = N'Đã đặt'
        			AND ct.ngayNhanThuc = CAST(GETDATE() AS DATE)

        """;
        try (Connection con = ConnectDB.getInstance().getConnection()) {
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) result = rs.getInt("cnt");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return result;
    }
    
    public int countCanCheckOutHomNay() {
    	int result = 0;
        String sql = """
            SELECT COUNT(DISTINCT ct.maPhieuDatPhong) AS cnt
        		FROM ChiTietPhieuDatPhong ct
        			JOIN PhieuDatPhong pdp ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
        		WHERE pdp.trangThai = N'Đang ở'
        			AND ct.ngayTraThuc = CAST(GETDATE() AS DATE)

        """;
        try (Connection con = ConnectDB.getInstance().getConnection()) {
			PreparedStatement stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) result = rs.getInt("cnt");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return result;
    }
    
    //==========Thong ke
    public Map<String, Integer> getTongSoNgayOPhongTheoThang(int thang, int nam) {
		Map<String, Integer> result = new HashMap<>();

		String sql = """
				SELECT
					lp.tenLoaiPhong,
					SUM(
						DATEDIFF(
						    DAY,
					        CASE WHEN ct.ngayNhanThuc < ? THEN ? ELSE ct.ngayNhanThuc END,
					        CASE WHEN ct.ngayTra > ? THEN ? ELSE ct.ngayTra END
					    )
					) AS soLuotO
				FROM ChiTietPhieuDatPhong ct
					JOIN PhieuDatPhong pdp ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
					JOIN Phong p ON ct.maPhong = p.maPhong
					JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
				WHERE
					pdp.trangThai <> N'Đã hủy'
					AND ct.ngayNhanThuc < ?
					AND ct.ngayTra  > ?
				GROUP BY lp.tenLoaiPhong;
							""";
		LocalDate start = LocalDate.of(nam, thang, 1);
		LocalDate end = start.plusMonths(1);

		try (Connection con = ConnectDB.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setDate(1, java.sql.Date.valueOf(start));
			ps.setDate(2, java.sql.Date.valueOf(start));
			ps.setDate(3, java.sql.Date.valueOf(end));
			ps.setDate(4, java.sql.Date.valueOf(end));
			ps.setDate(5, java.sql.Date.valueOf(end));
			ps.setDate(6, java.sql.Date.valueOf(start));

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result.put(rs.getString("tenLoaiPhong"), rs.getInt("soLuotO"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public Map<String, Integer> getTongSoNgayOPhongTheoNam(int nam) {
		Map<String, Integer> map = new HashMap<>();

		String sql = """
				SELECT
					lp.tenLoaiPhong,
					SUM(
						DATEDIFF(
						    DAY,
					        CASE WHEN ct.ngayNhanThuc < ? THEN ? ELSE ct.ngayNhanThuc END,
					        CASE WHEN ct.ngayTra > ? THEN ? ELSE ct.ngayTra END
					    )
					) AS soLuot
				FROM ChiTietPhieuDatPhong ct
					JOIN PhieuDatPhong pdp ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
					JOIN Phong p ON ct.maPhong = p.maPhong
					JOIN LoaiPhong lp ON p.maLoaiPhong = lp.maLoaiPhong
					WHERE
					    pdp.trangThai <> N'Đã hủy'
					    AND ct.ngayNhanThuc < ?
					    AND ct.ngayTra  > ?
					GROUP BY lp.tenLoaiPhong;
								""";
		LocalDate start = LocalDate.of(nam, 1, 1);
		LocalDate end = LocalDate.of(nam + 1, 1, 1);

		try (Connection con = ConnectDB.getInstance().getConnection()) {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setDate(1, java.sql.Date.valueOf(start));
			ps.setDate(2, java.sql.Date.valueOf(start));
			ps.setDate(3, java.sql.Date.valueOf(end));
			ps.setDate(4, java.sql.Date.valueOf(end));
			ps.setDate(5, java.sql.Date.valueOf(end));
			ps.setDate(6, java.sql.Date.valueOf(start));

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("tenLoaiPhong"), rs.getInt("soLuot"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	public List<LocalDate[]> getDanhSachPhongDangOTrongThang(int thang, int nam) {
		List<LocalDate[]> list = new ArrayList<>();

		String sql = """

						SELECT ct.ngayNhanThuc, ct.ngayTra
				FROM ChiTietPhieuDatPhong ct
				JOIN PhieuDatPhong pdp
				    ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
				WHERE pdp.trangThai <> N'Đã hủy'
				  AND ct.ngayNhanThuc < ?
				  AND ct.ngayTra  > ?
						  """;

		LocalDate start = LocalDate.of(nam, thang, 1);
		LocalDate end = start.plusMonths(1);

		try (Connection con = ConnectDB.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setDate(1, Date.valueOf(end));
			ps.setDate(2, Date.valueOf(start));

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new LocalDate[] { rs.getDate("ngayNhanThuc").toLocalDate(),
						rs.getDate("ngayTra").toLocalDate() });
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<LocalDate[]> getDanhSachPhongDangOTrongNam(int nam) {
		List<LocalDate[]> list = new ArrayList<>();

		String sql = """

						      SELECT ct.ngayNhanThuc, ct.ngayTra
				FROM ChiTietPhieuDatPhong ct
				JOIN PhieuDatPhong pdp
				    ON ct.maPhieuDatPhong = pdp.maPhieuDatPhong
				WHERE pdp.trangThai <> N'Đã hủy'
				  AND ct.ngayNhanThuc < ?
				  AND ct.ngayTra  > ?
						""";

		LocalDate start = LocalDate.of(nam, 1, 1);
		LocalDate end = LocalDate.of(nam + 1, 1, 1);

		try (Connection con = ConnectDB.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setDate(1, Date.valueOf(end));
			ps.setDate(2, Date.valueOf(start));

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new LocalDate[] { rs.getDate("ngayNhanThuc").toLocalDate(),
						rs.getDate("ngayTra").toLocalDate() });
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	// 2) Số phiếu đặt phòng "check-in thành công" trong tháng
    // =========================
    public int getSoPhieuCheckInThanhCongTrongThang(int thang, int nam) {
        int result = 0;
        String sql = """
            SELECT COUNT(DISTINCT p.maPhieuDatPhong) AS soPhieu
            FROM ChiTietPhieuDatPhong ct
            JOIN PhieuDatPhong p ON ct.maPhieuDatPhong = p.maPhieuDatPhong
            WHERE ct.ngayNhanThuc IS NOT NULL
              AND YEAR(ct.ngayNhanThuc) = ?
              AND MONTH(ct.ngayNhanThuc) = ?
              AND p.trangThai <> N'Đã hủy'
              AND p.trangThai <> N'Đã đặt'
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nam);
            ps.setInt(2, thang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) result = rs.getInt("soPhieu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public int countPhongDangCoKhach() {
        int result = 0;

        String sql = """
            SELECT COUNT(*) AS cnt
            FROM ChiTietPhieuDatPhong ct
            JOIN PhieuDatPhong p 
                ON ct.maPhieuDatPhong = p.maPhieuDatPhong
            WHERE p.trangThai = N'Đang ở'
              AND ct.ngayNhanThuc <= CAST(GETDATE() AS DATE)
              AND ct.ngayTra  > CAST(GETDATE() AS DATE)
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                result = rs.getInt("cnt");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
