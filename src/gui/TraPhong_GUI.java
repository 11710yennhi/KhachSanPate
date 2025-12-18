package gui;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import dao.ChiTietPhieuDatPhong_DAO;
import dao.PhieuDatPhong_DAO;
import dao.Phong_DAO;
import entity.ChiTietPhieuDatPhong;
import entity.PhieuDatPhong;
import entity.Phong;

public class TraPhong_GUI {

    private ChiTietPhieuDatPhong_DAO ctpdpd = new ChiTietPhieuDatPhong_DAO();
    private Phong_DAO phongDAO = new Phong_DAO();
    private PhieuDatPhong_DAO pdpd= new PhieuDatPhong_DAO();

    public TraPhong_GUI(String maPDP, String maPhong, DefaultTableModel model, int row) {

        List<ChiTietPhieuDatPhong> dsCT = ctpdpd.getChiTietTheoMaPhieu(maPDP);

        for (ChiTietPhieuDatPhong ct : dsCT) {

            if (!ct.getPhong().getMaPhong().equalsIgnoreCase(maPhong))
                continue;

            LocalDate ngayNhan = ct.getNgayNhanThuc();
            LocalDate ngayTra = ct.getNgayTra();
            LocalDate homNay = LocalDate.now();

            if (ngayNhan == null || ngayTra == null) {
                JOptionPane.showMessageDialog(null, "Thiếu ngày nhận hoặc ngày trả!");
                return;
            }

            // ===== TÍNH NGÀY GIỮA =====
            long tongSoNgay = ChronoUnit.DAYS.between(ngayNhan, ngayTra);
            if (tongSoNgay <= 0) tongSoNgay = 0;

            LocalDate ngayGiua = ngayNhan.plusDays(tongSoNgay / 2);

            // ===== TRẢ TRƯỚC ≥ 3 NGÀY (FREE) =====
            if (homNay.isBefore(ngayNhan.minusDays(2))) {
                capNhatUI(model, row, ngayNhan, ngayNhan, ngayNhan);
                tinhTien(model, row, maPhong, ngayNhan, ngayNhan);
                JOptionPane.showMessageDialog(null, "Trả phòng trước ≥ 3 ngày – không tính phí!");
                return;
            }

            // ===== XÁC NHẬN =====
            int chon = JOptionPane.showConfirmDialog(
                    null,
                    "Bạn có chắc muốn trả phòng sớm?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );
            if (chon != JOptionPane.YES_OPTION) return;

            LocalDate ngayTraMoi;
            PhieuDatPhong tam= pdpd.timPhieuDatPhongTheoMa(maPDP);
            String trangThai = tam.getTrangThai();

            // ===== TRẠNG THÁI ĐANG Ở =====
            if (trangThai.equalsIgnoreCase("Đang ở")) {

                if (homNay.isBefore(ngayGiua)) {
                    ngayTraMoi = ngayGiua;
                } else {
                    ngayTraMoi = homNay.plusDays(1);
                }

            }
            // ===== TRẠNG THÁI ĐÃ ĐẶT =====
            else {
                ngayTraMoi = ngayGiua;
            }

            capNhatUI(model, row, ngayNhan, ngayTraMoi, ngayTraMoi);
            tinhTien(model, row, maPhong, ngayNhan, ngayTraMoi);
            JOptionPane.showMessageDialog(null, "Trả phòng thành công!");
            return;
        }

        JOptionPane.showMessageDialog(null, "Phòng này chưa lưu vào hệ thống bạn có thể xóa!");
    }

    // ===================== HÀM HỖ TRỢ =====================

    private void capNhatUI(DefaultTableModel model, int row,
                           LocalDate ngayNhan, LocalDate ngayTraThuc, LocalDate ngayTra) {
        model.setValueAt(ngayNhan.toString(), row, 3);
        model.setValueAt(ngayTraThuc.toString(), row, 4);
        model.setValueAt(ngayTra.toString(), row, 5);
    }

    private void tinhTien(DefaultTableModel model, int row,
                          String maPhong, LocalDate ngayNhan, LocalDate ngayTra) {

        long soDem = ChronoUnit.DAYS.between(ngayNhan, ngayTra);
        if (soDem < 0) soDem = 0;

        Phong p = phongDAO.timPhongTheoMa(maPhong);
        double gia = p.getLoaiPhong().getGia();
        double thanhTien = soDem * gia;

        model.setValueAt(soDem, row, 6);
        model.setValueAt(gia, row, 7);
        model.setValueAt(thanhTien, row, 8);
    }
}
