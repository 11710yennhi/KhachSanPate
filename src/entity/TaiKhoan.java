package entity;

public class TaiKhoan {
	private String maNhanVien;
	private String matKhau;
	
	public TaiKhoan(String maNhanVien, String matKhau) {
		this.maNhanVien = maNhanVien;
		this.matKhau = matKhau;
	}
	
	public String getMaNhanVien() {
		return maNhanVien;
	}
	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}
	public String getMatKhau() {
		return matKhau;
	}
	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}
	
}
