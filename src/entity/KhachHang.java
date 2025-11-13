package entity;

import java.util.Objects;

public class KhachHang {
	private String maKhachHang;
	private String hoTen;
	private String soDienThoai;
	private boolean nguoiVietNam;
	
	// Constructor
	public KhachHang(String maKhachHang, String hoTen, String soDienThoai, boolean nguoiVietNam) {
		this.maKhachHang = maKhachHang;
		this.hoTen = hoTen;
		this.soDienThoai = soDienThoai;
		this.nguoiVietNam = nguoiVietNam;
	}
	
	public KhachHang(String maKhachHang) {
		this.maKhachHang = maKhachHang;
	}
	
	public KhachHang() {
	}
	
	// Getter & Setter
	public String getMaKhachHang() {
		return maKhachHang;
	}
	public void setMaKhachHang(String maKhachHang) {
		this.maKhachHang = maKhachHang;
	}
	public String getHoTen() {
		return hoTen;
	}
	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}
	public String getSoDienThoai() {
		return soDienThoai;
	}
	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}
	public boolean isNguoiVietNam() {
		return nguoiVietNam;
	}
	public void setNguoiVietNam(boolean nguoiVietNam) {
		this.nguoiVietNam = nguoiVietNam;
	}

	// hashCode & equals
	@Override
	public int hashCode() {
		return Objects.hash(maKhachHang);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		KhachHang other = (KhachHang) obj;
		return Objects.equals(maKhachHang, other.maKhachHang);
	}
	
	// toString
	@Override
	public String toString() {
		return "KhachHang [maKhachHang=" + maKhachHang + ", hoTen=" + hoTen + 
				", soDienThoai=" + soDienThoai + ", nguoiVietNam=" + nguoiVietNam + "]";
	}
}
