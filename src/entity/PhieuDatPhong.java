package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhieuDatPhong {
    private String maPhieuDatPhong;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private LocalDate ngayTao;
    private String trangThai;
    private int soTreEm;
    private int soNguoiLon;

    private double tongTienPhong;             // thuộc tính dẫn xuất
    private double tongTienChiPhiPhatSinh;    // thuộc tính dẫn xuất
    private double tongTien;                  // thuộc tính dẫn xuất
    private double tienCoc;                   // thuộc tính dẫn xuất

    private List<ChiTietPhieuDatPhong> dsChiTiet;
    private List<ChiTietChiPhiPhatSinh> dsChiPhiPhatSinh;

    // ====== Constructor mặc định ======
    public PhieuDatPhong() {
        this.dsChiTiet = new ArrayList<>();
        this.dsChiPhiPhatSinh = new ArrayList<>();
    }

    // ====== Constructor với mã phiếu ======
    public PhieuDatPhong(String maPhieuDatPhong) {
        this.maPhieuDatPhong = maPhieuDatPhong;
        this.dsChiTiet = new ArrayList<>();
        this.dsChiPhiPhatSinh = new ArrayList<>();
    }

    // ====== Constructor đầy đủ ======
    public PhieuDatPhong(String maPhieuDatPhong, KhachHang khachHang, NhanVien nhanVien,
                         LocalDate ngayTao, String trangThai,
                         int soTreEm, int soNguoiLon) {
        this.maPhieuDatPhong = maPhieuDatPhong;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
        this.soTreEm = soTreEm;
        this.soNguoiLon = soNguoiLon;
        this.dsChiTiet = new ArrayList<>();
        this.dsChiPhiPhatSinh = new ArrayList<>();
        capNhatTongTien();
    }

    // ====== Getter & Setter ======
    public String getMaPhieuDatPhong() {
        return maPhieuDatPhong;
    }

    public void setMaPhieuDatPhong(String maPhieuDatPhong) {
        this.maPhieuDatPhong = maPhieuDatPhong;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getSoTreEm() {
        return soTreEm;
    }

    public void setSoTreEm(int soTreEm) {
        this.soTreEm = soTreEm;
    }

    public int getSoNguoiLon() {
        return soNguoiLon;
    }

    public void setSoNguoiLon(int soNguoiLon) {
        this.soNguoiLon = soNguoiLon;
    }

    public List<ChiTietPhieuDatPhong> getDsChiTiet() {
        return dsChiTiet;
    }

    public void setDsChiTiet(List<ChiTietPhieuDatPhong> dsChiTiet) {
        this.dsChiTiet = dsChiTiet;
        capNhatTongTien();
    }

    public List<ChiTietChiPhiPhatSinh> getDsChiPhiPhatSinh() {
        return dsChiPhiPhatSinh;
    }

    public void setDsChiPhiPhatSinh(List<ChiTietChiPhiPhatSinh> dsChiPhiPhatSinh) {
        this.dsChiPhiPhatSinh = dsChiPhiPhatSinh;
        capNhatTongTien();
    }

    // ====== Thêm chi tiết & chi phí ======
    public void themChiTiet(ChiTietPhieuDatPhong ct) {
        dsChiTiet.add(ct);
        capNhatTongTien();
    }

    public void themChiPhiPhatSinh(ChiTietChiPhiPhatSinh cp) {
        dsChiPhiPhatSinh.add(cp);
        capNhatTongTien();
    }

    // ====== Cập nhật tổng tiền ======
    public void capNhatTongTien() {
        tongTienPhong = 0;
        tongTienChiPhiPhatSinh = 0;

        if (dsChiTiet != null) {
            for (ChiTietPhieuDatPhong ct : dsChiTiet) {
                tongTienPhong += ct.getThanhTien();
            }
        }

        if (dsChiPhiPhatSinh != null) {
            for (ChiTietChiPhiPhatSinh cp : dsChiPhiPhatSinh) {
                tongTienChiPhiPhatSinh += cp.getThanhTien();
            }
        }

        tongTien = tongTienPhong + tongTienChiPhiPhatSinh;
        tienCoc = tongTienPhong * 0.5;
    }

    // ====== Getter cho các thuộc tính dẫn xuất ======
    public double getTongTienPhong() {
        return tongTienPhong;
    }

    public double getTongTienChiPhiPhatSinh() {
        return tongTienChiPhiPhatSinh;
    }

    public double getTongTien() {
        return tongTien;
    }

    public double getTienCoc() {
        return tienCoc;
    }

    // ====== HashCode & Equals ======
    @Override
    public int hashCode() {
        return Objects.hash(maPhieuDatPhong);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PhieuDatPhong other = (PhieuDatPhong) obj;
        return Objects.equals(maPhieuDatPhong, other.maPhieuDatPhong);
    }

    // ====== ToString ======
    @Override
    public String toString() {
        return "PhieuDatPhong {" +
                "maPhieuDatPhong='" + maPhieuDatPhong + '\'' +
                ", khachHang=" + khachHang +
                ", nhanVien=" + nhanVien +
                ", ngayTao=" + ngayTao +
                ", trangThai='" + trangThai + '\'' +
                ", soTreEm=" + soTreEm +
                ", soNguoiLon=" + soNguoiLon +
                ", tongTienPhong=" + tongTienPhong +
                ", tongTienChiPhiPhatSinh=" + tongTienChiPhiPhatSinh +
                ", tongTien=" + tongTien +
                ", tienCoc=" + tienCoc +
                ", dsChiTiet=" + dsChiTiet +
                ", dsChiPhiPhatSinh=" + dsChiPhiPhatSinh +
                '}';
    }
}
