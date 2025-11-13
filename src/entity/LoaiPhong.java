package entity;

import java.util.Objects;

public class LoaiPhong {

    private String maLoaiPhong;
    private String tenLoaiPhong;
    private int sucChua;
    private double gia;
    private String moTa;

    public LoaiPhong() {
    }

    public LoaiPhong(String maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
    }

    public LoaiPhong(String maLoaiPhong, String tenLoaiPhong, int sucChua, double gia, String moTa) {
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoaiPhong = tenLoaiPhong;
        this.sucChua = sucChua;
        this.gia = gia;
        this.moTa = moTa;
    }

    public String getMaLoaiPhong() {
        return maLoaiPhong;
    }

    public void setMaLoaiPhong(String maLoaiPhong) {
        if (maLoaiPhong == null || maLoaiPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã loại phòng không được để trống");
        }
        this.maLoaiPhong = maLoaiPhong;
    }

    public String getTenLoaiPhong() {
        return tenLoaiPhong;
    }

    public void setTenLoaiPhong(String tenLoaiPhong) {
        if (tenLoaiPhong == null || tenLoaiPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại phòng không được để trống");
        }
        this.tenLoaiPhong = tenLoaiPhong;
    }

    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        if (sucChua <= 0) {
            throw new IllegalArgumentException("Sức chứa phải lớn hơn 0");
        }
        this.sucChua = sucChua;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        if (gia <= 0) {
            throw new IllegalArgumentException("Giá phải lớn hơn 0");
        }
        this.gia = gia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        if (moTa == null || moTa.trim().isEmpty()) {
            throw new IllegalArgumentException("Mô tả không được để trống");
        }
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return "LoaiPhong [maLoaiPhong=" + maLoaiPhong + ", tenLoaiPhong=" + tenLoaiPhong
                + ", sucChua=" + sucChua + ", gia=" + gia + ", moTa=" + moTa + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(maLoaiPhong);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        LoaiPhong other = (LoaiPhong) obj;
        return Objects.equals(maLoaiPhong, other.maLoaiPhong);
    }
}
