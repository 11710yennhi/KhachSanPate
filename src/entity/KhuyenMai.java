package entity;

import java.time.LocalDate;
import java.util.Objects;

public class KhuyenMai {
	private String maKhuyenMai;
	private String tenKhuyenMai;
	private LocalDate ngayTao;
	private LocalDate ngayBatDau;
	private LocalDate ngayKetThuc;
	private String loaiKhuyenMai;
	private double dieuKien;
	private double giaTriGiam;
	
	//Constructor
	
	public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, LocalDate ngayTao, LocalDate ngayBatDau, LocalDate ngayKetThuc,
			String loaiKhuyenMai, double dieuKien, double giaTriGiam) {
		this.maKhuyenMai = maKhuyenMai;
		this.tenKhuyenMai = tenKhuyenMai;
		this.ngayTao = ngayTao;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.loaiKhuyenMai = loaiKhuyenMai;
		this.dieuKien = dieuKien;
		this.giaTriGiam = giaTriGiam;
	}
	
	public KhuyenMai(String maKhuyenMai) {
		this.maKhuyenMai = maKhuyenMai;
	}

	public KhuyenMai() {
	}
	
	//Getter setter

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

	public double getDieuKien() {
		return dieuKien;
	}

	public void setDieuKien(double dieuKien) {
		this.dieuKien = dieuKien;
	}

	public double getGiaTriGiam() {
		return giaTriGiam;
	}

	public void setGiaTriGiam(double giaTriGiam) {
		this.giaTriGiam = giaTriGiam;
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

	//toString()
	
	@Override
	public String toString() {
		return "KhuyenMai [maKhuyenMai=" + maKhuyenMai + ", tenKhuyenMai=" + tenKhuyenMai + ", ngayTao=" + ngayTao
				+ ", ngayBatDau=" + ngayBatDau + ", ngayKetThuc=" + ngayKetThuc + ", loaiKhuyenMai=" + loaiKhuyenMai
				+ ", dieuKien=" + dieuKien + ", giaTriGiam=" + giaTriGiam + "]";
	}
	

}
