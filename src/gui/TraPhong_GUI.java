package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import dao.ChiTietPhieuDatPhong_DAO;
import dao.Phong_DAO;
import dao.PhieuDatPhong_DAO;
import entity.ChiTietPhieuDatPhong;
import entity.Phong;

public class TraPhong_GUI implements ActionListener {

    private PhieuDatPhong_DAO pdpd;
    private ChiTietPhieuDatPhong_DAO ctpdpd;
    private Phong_DAO dsp;

    public TraPhong_GUI(String maPDP, String maPhong, DefaultTableModel dlp, int x) {

        pdpd = new PhieuDatPhong_DAO();
        ctpdpd = new ChiTietPhieuDatPhong_DAO();
        dsp = new Phong_DAO();

        // Lấy chi tiết phiếu đặt phòng
        List<ChiTietPhieuDatPhong> dsct = ctpdpd.getChiTietTheoMaPhieu(maPDP);
        boolean found = false;

        for (ChiTietPhieuDatPhong ct : dsct) {

            if (ct.getPhong().getMaPhong().trim().equals(maPhong.trim())) {
                found = true;

                LocalDate ngayNhan = ct.getNgayNhanThuc();
                LocalDate ngayTra = ct.getNgayTra();
                LocalDate homNay = LocalDate.now();

                if (ngayNhan == null || ngayTra == null) {
                    JOptionPane.showMessageDialog(null, "Thiếu ngày nhận hoặc ngày trả thực tế!");
                    return;
                }

                // ===================== KIỂM TRA QUÁ SỚM =====================
                LocalDate tru2Ngay = ngayNhan.minusDays(2);
             // ❌ Trả quá sớm (trước ngày nhận 2 ngày trở lên)
                if (homNay.isBefore(tru2Ngay)) {
                    LocalDate ngayTraMoi = ngayNhan;

                    // ===================== UPDATE LÊN UI =====================
                    dlp.setValueAt(ngayTraMoi.toString(), x, 5); 
                    dlp.setValueAt(ngayTraMoi.toString(), x, 4); 

                    long soDemO = ChronoUnit.DAYS.between(ngayNhan, ngayTraMoi);
                    if (soDemO < 1) soDemO = 0;

                    Phong phong = dsp.timPhongTheoMa(maPhong);
                   
                    double gia = phong.getLoaiPhong().getGia();
                    double thanhTien = soDemO * gia;

                    dlp.setValueAt(soDemO, x, 6);  
                    dlp.setValueAt(gia, x, 7);    
                    dlp.setValueAt(thanhTien, x, 8);

                    JOptionPane.showMessageDialog(
                        null,
                        "Trả phòng thành công!"
                    );

                    return;
                }


                // ❌ Quá hạn (>= ngày trả thực tế)
                if (!homNay.isBefore(ngayTra)) {
                    JOptionPane.showMessageDialog(null, "Phòng đã quá hạn để trả!");
                    return;
                }

                // ===================== TÍNH NGÀY GIỮA =====================
                long soNgayDat = ChronoUnit.DAYS.between(ngayNhan, ngayTra);
                if (soNgayDat <= 0) soNgayDat = 1;

                long soNgayToiThieu = (long) Math.ceil(soNgayDat / 2.0);
                LocalDate ngayGiua = ngayNhan.plusDays(soNgayToiThieu);

                // ===================== XỬ LÝ TRẢ PHÒNG =====================
                int chon = JOptionPane.showConfirmDialog(
                        null,
                        "Bạn có chắc muốn trả phòng sớm không?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION
                );
                if (chon != JOptionPane.YES_OPTION) return;

                LocalDate ngayTraMoi;

                // === LOGIC BẠN YÊU CẦU ===
                LocalDate homNayTru2 = homNay.minusDays(2);

                // 1) Nếu hôm nay ≥ ngày giữa → trả = hôm nay + 1
                if (!homNay.isBefore(ngayGiua)) {
                    ngayTraMoi = homNay.plusDays(1);

                } else {
                    // 2) Nếu hôm nay - 2 < ngày giữa → trả = ngày giữa
                    if (homNayTru2.isBefore(ngayGiua)) {
                        ngayTraMoi = ngayGiua;
                    } else {
                        // fallback an toàn (ít khi xảy ra)
                        ngayTraMoi = ngayGiua;
                    }
                }

                // ===================== UPDATE LÊN UI =====================
                dlp.setValueAt(ngayTraMoi.toString(), x, 4);

                long soDemO = ChronoUnit.DAYS.between(ngayNhan, ngayTraMoi);
                if (soDemO < 1) soDemO = 1;

                Phong phong = dsp.timPhongTheoMa(maPhong);
                double gia = phong.getLoaiPhong().getGia();
                double thanhTien = soDemO * gia;

                dlp.setValueAt(soDemO, x, 6);
                dlp.setValueAt(gia, x, 7);
                dlp.setValueAt(thanhTien, x, 8);

                return;
            }
        }

        // Nếu không tìm thấy phòng trong phiếu
        if (!found) {
            JOptionPane.showMessageDialog(
                    null,
                    "Phòng " + maPhong + " chưa được thêm vào phiếu đặt phòng " + maPDP + "!\n"
                    + "Bạn có thể xóa phòng này khỏi danh sách."
            );
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    
   

}