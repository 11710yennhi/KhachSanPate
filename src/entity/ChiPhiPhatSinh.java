package entity;

import java.util.Objects;

public class ChiPhiPhatSinh {
	private String maChiPhiPhatSinh;
	private String tenChiPhiPhatSinh;
	private String loaiChiPhiPhatSinh;
	private double gia;
	
	// Constructor
	public ChiPhiPhatSinh(String maChiPhiPhatSinh, String tenChiPhiPhatSinh,
			String loaiChiPhiPhatSinh,double gia) {
		this.maChiPhiPhatSinh = maChiPhiPhatSinh;
		this.tenChiPhiPhatSinh = tenChiPhiPhatSinh;
		this.loaiChiPhiPhatSinh = loaiChiPhiPhatSinh;
		this.gia = gia;
	}
	
	public ChiPhiPhatSinh(String maChiPhiPhatSinh) {
		this.maChiPhiPhatSinh = maChiPhiPhatSinh;
	}
	
	public ChiPhiPhatSinh() {
	}
	
	// Getter & Setter
	public String getMaChiPhiPhatSinh() {
		return maChiPhiPhatSinh;
	}
	public void setMaChiPhiPhatSinh(String maChiPhiPhatSinh) {
		this.maChiPhiPhatSinh = maChiPhiPhatSinh;
	}
	public String getTenChiPhiPhatSinh() {
		return tenChiPhiPhatSinh;
	}
	public void setTenChiPhiPhatSinh(String tenChiPhiPhatSinh) {
		this.tenChiPhiPhatSinh = tenChiPhiPhatSinh;
	}
	public String getLoaiChiPhiPhatSinh() {
		return loaiChiPhiPhatSinh;
	}
	public void setLoaiChiPhiPhatSinh(String loaiChiPhiPhatSinh) {
		this.loaiChiPhiPhatSinh = loaiChiPhiPhatSinh;
	}
	public double getGia() {
		return gia;
	}
	public void setGia(double gia) {
		this.gia = gia;
	}
	
	// hashCode & equals
	@Override
	public int hashCode() {
		return Objects.hash(maChiPhiPhatSinh);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ChiPhiPhatSinh other = (ChiPhiPhatSinh) obj;
		return Objects.equals(maChiPhiPhatSinh, other.maChiPhiPhatSinh);
	}
	
	// toString
	@Override
	public String toString() {
		return "ChiPhiPhatSinh [maChiPhiPhatSinh=" + maChiPhiPhatSinh + ", tenChiPhiPhatSinh=" + tenChiPhiPhatSinh
				+ ", loaiChiPhiPhatSinh=" + loaiChiPhiPhatSinh + ", gia=" + gia + "]";
	}
}
