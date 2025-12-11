package entity;

import java.util.Objects;

public class ChiTietChiPhiPhatSinh {
    private PhieuDatPhong phieuDatPhong;
    private ChiPhiPhatSinh chiPhiPhatSinh;
    private int soLuong;
    private double thanhTien; // thuộc tính dẫn xuất

    public ChiTietChiPhiPhatSinh() {
    }

    public ChiTietChiPhiPhatSinh(PhieuDatPhong phieuDatPhong, ChiPhiPhatSinh chiPhiPhatSinh, int soLuong) {
        this.phieuDatPhong = phieuDatPhong;
        this.chiPhiPhatSinh = chiPhiPhatSinh;
        this.soLuong = soLuong;
        tinhThanhTien();
    }

    public PhieuDatPhong getPhieuDatPhong() {
        return phieuDatPhong;
    }

    public void setPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
        if (phieuDatPhong == null) {
            throw new IllegalArgumentException("PhieuDatPhong không được null");
        }
        this.phieuDatPhong = phieuDatPhong;
    }

    public ChiPhiPhatSinh getChiPhiPhatSinh() {
        return chiPhiPhatSinh;
    }

    public void setChiPhiPhatSinh(ChiPhiPhatSinh chiPhiPhatSinh) {
        if (chiPhiPhatSinh == null) {
            throw new IllegalArgumentException("ChiPhiPhatSinh không được null");
        }
        this.chiPhiPhatSinh = chiPhiPhatSinh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        this.soLuong = soLuong;
        tinhThanhTien();
    }

    public double getThanhTien() {
        if (chiPhiPhatSinh != null) {
            thanhTien = chiPhiPhatSinh.getGia() * soLuong;
        } else {
            thanhTien = 0;
        }
        return thanhTien;
    }



    public void tinhThanhTien() {
        if (chiPhiPhatSinh != null)
            this.thanhTien = chiPhiPhatSinh.getGia() * soLuong;
        else
            this.thanhTien = 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phieuDatPhong, chiPhiPhatSinh);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ChiTietChiPhiPhatSinh other = (ChiTietChiPhiPhatSinh) obj;
        return Objects.equals(phieuDatPhong, other.phieuDatPhong)
                && Objects.equals(chiPhiPhatSinh, other.chiPhiPhatSinh);
    }

    @Override
    public String toString() {
        return "ChiTietChiPhiPhatSinh [phieuDatPhong=" + phieuDatPhong.getMaPhieuDatPhong()
                + ", chiPhiPhatSinh=" + chiPhiPhatSinh.getTenChiPhiPhatSinh()
                + ", soLuong=" + soLuong + ", thanhTien=" + thanhTien + "]";
    }
}
