package entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ChiTietPhieuDatPhong {
    private PhieuDatPhong phieuDatPhong;
    private Phong phong;
    private LocalDate ngayNhanThuc;
    private LocalDate ngayTraThuc;
    private String trangThai;

    
    public ChiTietPhieuDatPhong() {
    }

    public ChiTietPhieuDatPhong(PhieuDatPhong phieuDatPhong, Phong phong,
                                LocalDate ngayNhanThuc, LocalDate ngayTraThuc, String trangThai) {
        this.phieuDatPhong = phieuDatPhong;
        this.phong = phong;
        this.ngayNhanThuc = ngayNhanThuc;
        this.ngayTraThuc = ngayTraThuc;
        this.trangThai = trangThai;
        getThanhTien();
    }

    // ===== Getter & Setter =====
    public PhieuDatPhong getPhieuDatPhong() {
        return phieuDatPhong;
    }

    public void setPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
        this.phieuDatPhong = phieuDatPhong;
    }

    public Phong getPhong() {
        return phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
    }

    public LocalDate getNgayNhanThuc() {
        return ngayNhanThuc;
    }

    public void setNgayNhanThuc(LocalDate ngayNhanThuc) {
        this.ngayNhanThuc = ngayNhanThuc;
    }

    public LocalDate getNgayTraThuc() {
        return ngayTraThuc;
    }

    public void setNgayTraThuc(LocalDate ngayTraThuc) {
        this.ngayTraThuc = ngayTraThuc;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // ===== Thuộc tính dẫn xuất =====
    public double getThanhTien() {
        if (phong == null || ngayNhanThuc == null || ngayTraThuc == null) {
            return 0;
        }

        return phong.getLoaiPhong().getGia() * getSoNgay();
    }

    public int getSoNgay() {
        if (ngayNhanThuc == null || ngayTraThuc == null) {
            return 0;
        }

        long soNgay = ChronoUnit.DAYS.between(ngayNhanThuc, ngayTraThuc);
        if (soNgay <= 0) {
            soNgay = 1; // Tối thiểu 1 ngày
        }

        return (int) soNgay;
    }


    @Override
    public int hashCode() {
        return Objects.hash(phieuDatPhong, phong);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ChiTietPhieuDatPhong other = (ChiTietPhieuDatPhong) obj;
        return Objects.equals(phieuDatPhong, other.phieuDatPhong)
                && Objects.equals(phong, other.phong);
    }

	@Override
	public String toString() {
		return "ChiTietPhieuDatPhong [phieuDatPhong=" + phieuDatPhong + ", phong=" + phong + ", ngayNhanThuc="
				+ ngayNhanThuc + ", ngayTraThuc=" + ngayTraThuc + ", trangThai=" + trangThai + "]";
	}

    
   
}
