package entity;

import java.time.LocalDate;
import java.util.Objects;

import java.time.LocalDate;

public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private LocalDate ngayTao;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String loaiKhuyenMai;
    private double soTienApDung;
    private double giaTriGiam;
    private double giamToiDa;

    public KhuyenMai() {}

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, LocalDate ngayTao,
                     LocalDate ngayBatDau, LocalDate ngayKetThuc, String loaiKhuyenMai,
                     double soTienApDung, double giaTriGiam, double giamToiDa) {
        this.maKhuyenMai = maKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.ngayTao = ngayTao;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.loaiKhuyenMai = loaiKhuyenMai;
        this.soTienApDung = soTienApDung;
        this.giaTriGiam = giaTriGiam;
        this.giamToiDa = giamToiDa;
    }
    
    public KhuyenMai(String maKhuyenMai) {
		super();
		this.maKhuyenMai = maKhuyenMai;
	}

	public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getLoaiKhuyenMai() {
        return loaiKhuyenMai;
    }

    public void setLoaiKhuyenMai(String loaiKhuyenMai) {
        this.loaiKhuyenMai = loaiKhuyenMai;
    }

    public double getSoTienApDung() {
        return soTienApDung;
    }

    public void setSoTienApDung(double soTienApDung) {
        this.soTienApDung = soTienApDung;
    }

    public double getGiaTriGiam() {
        return giaTriGiam;
    }

    public void setGiaTriGiam(double giaTriGiam) {
        this.giaTriGiam = giaTriGiam;
    }

    public double getGiamToiDa() {
        return giamToiDa;
    }

    public void setGiamToiDa(double giamToiDa) {
        this.giamToiDa = giamToiDa;
    }

    @Override
    public String toString() {
        return "KhuyenMai{" +
                "maKhuyenMai='" + maKhuyenMai + '\'' +
                ", tenKhuyenMai='" + tenKhuyenMai + '\'' +
                ", ngayTao=" + ngayTao +
                ", ngayBatDau=" + ngayBatDau +
                ", ngayKetThuc=" + ngayKetThuc +
                ", loaiKhuyenMai='" + loaiKhuyenMai + '\'' +
                ", soTienApDung=" + soTienApDung +
                ", giaTriGiam=" + giaTriGiam +
                ", giamToiDa=" + giamToiDa +
                '}';
    }

	//hashCode() and equal()

	@Override
	public int hashCode() {
		return Objects.hash(maKhuyenMai);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhuyenMai other = (KhuyenMai) obj;
		return Objects.equals(maKhuyenMai, other.maKhuyenMai);
	}

}
