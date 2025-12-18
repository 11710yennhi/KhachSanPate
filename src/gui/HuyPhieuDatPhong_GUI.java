package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.*;
import entity.*;

public class HuyPhieuDatPhong_GUI extends JFrame {

    // ===== DAO (chỉ lấy thông tin phiếu & khách) =====
    private PhieuDatPhong_DAO pdpDAO = new PhieuDatPhong_DAO();
    private KhachHang_DAO khDAO = new KhachHang_DAO();

    // ===== DATA =====
    private PhieuDatPhong phieu;      // phiếu gốc (hiển thị thông tin)
    private PhieuDatPhong tam;        // phiếu đã chốt tiền
    private KhachHang khach;
    private DefaultTableModel modelDaChot; // BẢNG ĐÃ CHỐT

    // ===== CONSTRUCTOR =====
    public HuyPhieuDatPhong_GUI(String maPhieu,
                                PhieuDatPhong pDaChot,
                                DefaultTableModel dlpDaChot) {

        this.tam = pDaChot;
        this.modelDaChot = dlpDaChot;

        this.phieu = pdpDAO.timPhieuDatPhongTheoMa(maPhieu);
        this.khach = khDAO.getKhachHangTheoMa(
                phieu.getKhachHang().getMaKhachHang()
        );

        setTitle("PHIẾU HỦY ĐẶT PHÒNG");
        setSize(650, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(15, 25, 15, 25));
        add(main);

        addTieuDe(main);
        addThongTinKhachSan(main);
        addThongTinPhieu(main);
        addBangPhong(main);
        addTien(main);

        setVisible(true);
    }

    // ===== UI =====

    private void addTieuDe(JPanel main) {
        JLabel lbl = new JLabel("PHIẾU HỦY ĐẶT PHÒNG");
        lbl.setFont(new Font("Serif", Font.BOLD, 22));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(lbl);
        main.add(Box.createVerticalStrut(10));
    }

    private void addThongTinKhachSan(JPanel main) {
        JLabel ks1 = new JLabel("KHÁCH SẠN PATE");
        JLabel ks2 = new JLabel("Địa chỉ: 12 Nguyễn Văn Bảo, P4, Gò Vấp, TP.HCM");
        JLabel ks3 = new JLabel("Số điện thoại: 1233456789");

        ks1.setFont(new Font("SansSerif", Font.BOLD, 14));
        ks1.setAlignmentX(Component.CENTER_ALIGNMENT);
        ks2.setAlignmentX(Component.CENTER_ALIGNMENT);
        ks3.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(ks1);
        main.add(ks2);
        main.add(ks3);
        main.add(Box.createVerticalStrut(15));
    }

    private void addThongTinPhieu(JPanel main) {
    	JPanel info = new JPanel(new GridLayout(5, 1, 3, 3));
        info.setOpaque(false);

        info.add(new JLabel("Mã phiếu: " + phieu.getMaPhieuDatPhong()));
        info.add(new JLabel("Tên khách hàng: " + khach.getHoTen()));
        info.add(new JLabel("Số điện thoại: " + khach.getSoDienThoai()));
        info.add(new JLabel("Ngày lập phiếu: " + phieu.getNgayTao()));
        info.add(new JLabel("Trạng thái: Đã hủy"));

        main.add(info);
        main.add(Box.createVerticalStrut(15));
    }

    // ===== BẢNG PHÒNG (CHỈ LẤY MODEL ĐÃ TRUYỀN) =====
    private void addBangPhong(JPanel main) {

        JLabel title = new JLabel("DANH SÁCH PHÒNG");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        main.add(title);

        JTable tbl = new JTable(modelDaChot);
        tbl.setRowHeight(25);
        tbl.setEnabled(false);
        tbl.getTableHeader().setReorderingAllowed(false);
        tbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane sp = new JScrollPane(tbl);
        sp.setPreferredSize(new Dimension(600, 150));
        main.add(sp);
        main.add(Box.createVerticalStrut(15));
    }

    // ===== TIỀN (ĐÃ ĐÚNG – KHÔNG ĐỤNG) =====
    private void addTien(JPanel main) {

        JPanel money = new JPanel(new GridLayout(5, 1, 3, 3));
        money.setOpaque(false);

        Font bold = new Font("SansSerif", Font.BOLD, 14);

        money.add(labelBold("Tổng tiền phòng: " +
                formatVND(tam.getTongTienPhong()), bold));

        money.add(labelBold("TIỀN ĐÃ CỌC: " +
                formatVND(tam.getTienCoc()), bold));

        money.add(labelBold("TIỀN KHÁCH CẦN TRẢ: " +
                formatVND(tam.getTongTien() - tam.getTienCoc()), bold));

        main.add(money);
    }

    // ===== UTILS =====
    private JLabel labelBold(String text, Font f) {
        JLabel lb = new JLabel(text);
        lb.setFont(f);
        return lb;
    }

    private String formatVND(double v) {
        return String.format("%,.0f VNĐ", v);
    }
}
