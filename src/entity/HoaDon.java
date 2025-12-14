package entity;
import java.time.LocalDate;

public class HoaDon {

    // ===== THUỘC TÍNH =====
    private String maHoaDon;
    private PhieuDatPhong phieuDatPhong;
    private KhuyenMai khuyenMai;

    private double tongTienPhong;
    private double tongTienCPPS;
    private double tongTien;
    private double tongThanhToan;

    private String phuongThucThanhToan;
    private LocalDate ngayTao;

    // ===== CONSTRUCTOR KHÔNG THAM SỐ =====
    public HoaDon() {}

    // ===== CONSTRUCTOR ĐẦY ĐỦ =====
    public HoaDon(String maHoaDon, PhieuDatPhong phieuDatPhong, KhuyenMai khuyenMai,
                  double tongTienPhong, double tongTienCPPS, double tongTien,
                  double tongThanhToan, String phuongThucThanhToan, LocalDate ngayTao) {

        this.maHoaDon = maHoaDon;
        this.phieuDatPhong = phieuDatPhong;
        this.khuyenMai = khuyenMai;
        this.tongTienPhong = tongTienPhong;
        this.tongTienCPPS = tongTienCPPS;
        this.tongTien = tongTien;
        this.tongThanhToan = tongThanhToan;
        this.phuongThucThanhToan = phuongThucThanhToan;
        this.ngayTao = ngayTao;
    }

    // ===== GETTER + SETTER =====
    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public PhieuDatPhong getPhieuDatPhong() {
        return phieuDatPhong;
    }

    public void setPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
        this.phieuDatPhong = phieuDatPhong;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public double getTongTienPhong() {
        return tongTienPhong;
    }

    public void setTongTienPhong(double tongTienPhong) {
        this.tongTienPhong = tongTienPhong;
    }

    public double getTongTienCPPS() {
        return tongTienCPPS;
    }

    public void setTongTienCPPS(double tongTienCPPS) {
        this.tongTienCPPS = tongTienCPPS;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getTongThanhToan() {
        return tongThanhToan;
    }

    public void setTongThanhToan(double tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHoaDon='" + maHoaDon + '\'' +
                ", phieuDatPhong=" + phieuDatPhong +
                ", khuyenMai=" + khuyenMai +
                ", tongTienPhong=" + tongTienPhong +
                ", tongTienCPPS=" + tongTienCPPS +
                ", tongTien=" + tongTien +
                ", tongThanhToan=" + tongThanhToan +
                ", phuongThucThanhToan='" + phuongThucThanhToan + '\'' +
                ", ngayTao=" + ngayTao +
                '}';
    }
}
