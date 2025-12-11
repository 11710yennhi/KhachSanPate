package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import dao.*;
import entity.*;

public class HuyPhieuDatPhong_GUI extends JFrame {

    private PhieuDatPhong_DAO pdpDAO = new PhieuDatPhong_DAO();
    private ChiTietPhieuDatPhong_DAO ctDAO = new ChiTietPhieuDatPhong_DAO();
    private KhachHang_DAO khDAO = new KhachHang_DAO();
    private Phong_DAO phongDAO = new Phong_DAO();
    private ChiPhiPhatSinh_DAO cpDAO = new ChiPhiPhatSinh_DAO();
    private ChiTietChiPhiPhatSinh_DAO ctcpDAO = new ChiTietChiPhiPhatSinh_DAO();

    private PhieuDatPhong phieu;
    private KhachHang khach;
    private List<ChiTietPhieuDatPhong> dsPhong;
    private List<ChiTietChiPhiPhatSinh> dsChiPhi;
    private PhieuDatPhong tam;

    public HuyPhieuDatPhong_GUI(String maPhieu, PhieuDatPhong p) {
        tam = p;

        phieu = pdpDAO.timPhieuDatPhongTheoMa(maPhieu);
        dsPhong = ctDAO.getChiTietTheoMaPhieu(maPhieu);
        khach = khDAO.getKhachHangTheoMa(phieu.getKhachHang().getMaKhachHang());
        dsChiPhi = ctcpDAO.getChiTietTheoMaPhieu(maPhieu);

        setTitle("PHIẾU HỦY ĐẶT PHÒNG");
        setSize(800, 860);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(20, 35, 20, 35));
        add(main);

        JLabel lbl = new JLabel("PHIẾU HỦY ĐẶT PHÒNG");
        lbl.setFont(new Font("Serif", Font.BOLD, 26));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(lbl);

        main.add(Box.createVerticalStrut(10));

        addKS(main);
        addThongTinPhieu(main);
        addBangPhong(main);
        addBangChiPhi(main);
        addTien(main);

        setVisible(true);
    }

    private void addKS(JPanel main) {
        JLabel ks1 = new JLabel("KHÁCH SẠN PATE");
        JLabel ks2 = new JLabel("Địa chỉ: 12 Nguyễn Văn Bảo, P4, Gò Vấp, TP.HCM");
        JLabel ks3 = new JLabel("Số điện thoại: 1233456789");

        ks1.setFont(new Font("SansSerif", Font.BOLD, 15));
        ks1.setAlignmentX(Component.CENTER_ALIGNMENT);
        ks2.setAlignmentX(Component.CENTER_ALIGNMENT);
        ks3.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(ks1);
        main.add(ks2);
        main.add(ks3);
        main.add(Box.createVerticalStrut(15));
    }

    private void addThongTinPhieu(JPanel main) {
        JPanel info = new JPanel(new GridLayout(5, 1, 5, 5));
        info.setOpaque(false);

        info.add(new JLabel("Mã phiếu: " + phieu.getMaPhieuDatPhong()));
        info.add(new JLabel("Tên khách hàng: " + khach.getHoTen()));
        info.add(new JLabel("Số điện thoại: " + khach.getSoDienThoai()));
        info.add(new JLabel("Ngày lập phiếu: " + phieu.getNgayTao()));

        main.add(info);
        main.add(Box.createVerticalStrut(15));
    }

    private void addBangPhong(JPanel main) {

        JLabel title = new JLabel("DANH SÁCH PHÒNG");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        main.add(title);

        String[] cols = {
                "STT", "Mã phòng", "Loại phòng",
                "Ngày nhận", "Ngày trả", "Ngày trả thực",
                "Số đêm", "Giá", "Thành tiền"
        };

        DefaultTableModel m = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tbl = new JTable(m);
        tbl.setRowHeight(25);
        tbl.setEnabled(false);
        tbl.getTableHeader().setReorderingAllowed(false);

        // Tự fit vào khung không cần kéo
        tbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        int stt = 1;
        for (ChiTietPhieuDatPhong ct : dsPhong) {

            LocalDate nhan = ct.getNgayNhanThuc();
            LocalDate tra = ct.getNgayTra();

            long soDem = Math.max(0, ChronoUnit.DAYS.between(nhan, tra));
            double gia = ct.getPhong().getLoaiPhong().getGia();
            double thanhTien = gia * soDem;

            m.addRow(new Object[]{
                    stt++,
                    ct.getPhong().getMaPhong(),
                    ct.getPhong().getLoaiPhong().getTenLoaiPhong(),
                    nhan,
                    ct.getNgayTra(),
                    tra,
                    soDem,
                    String.format("%,.0f", gia),
                    String.format("%,.0f", thanhTien)
            });
        }

        JScrollPane sp = new JScrollPane(tbl);
        sp.setPreferredSize(new Dimension(720, 180));
        main.add(sp);
        main.add(Box.createVerticalStrut(15));
    }

    private void addBangChiPhi(JPanel main) {

        JLabel title = new JLabel("CHI PHÍ PHÁT SINH");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        main.add(title);

        String[] cols = {"Tên chi phí", "Giá", "Số lượng", "Thành tiền"};
        DefaultTableModel m = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tbl = new JTable(m);
        tbl.setRowHeight(25);
        tbl.setEnabled(false);
        tbl.getTableHeader().setReorderingAllowed(false);
        tbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        for (ChiTietChiPhiPhatSinh ct : dsChiPhi) {

            ChiPhiPhatSinh cp = cpDAO.getChiPhiTheoMa(ct.getChiPhiPhatSinh().getMaChiPhiPhatSinh());
            double thanhTien = cp.getGia() * ct.getSoLuong();

            m.addRow(new Object[]{
                    cp.getTenChiPhiPhatSinh(),
                    String.format("%,.0f", cp.getGia()),
                    ct.getSoLuong(),
                    String.format("%,.0f", thanhTien)
            });
        }

        JScrollPane sp = new JScrollPane(tbl);
        sp.setPreferredSize(new Dimension(720, 150));
        main.add(sp);
        main.add(Box.createVerticalStrut(15));
    }

    private void addTien(JPanel main) {

        JPanel money = new JPanel();
        money.setLayout(new GridLayout(5, 1, 3, 3));
        money.setOpaque(false);

        // Font nhỏ hơn để không làm phiếu dài quá
        Font normal = new Font("SansSerif", Font.PLAIN, 14);
        Font bold = new Font("SansSerif", Font.BOLD, 16);

        // Bắt đầu từ đây chữ đậm + hơi to
        money.add(labelBold("Tổng tiền phòng: " + formatVND(tam.getTongTienPhong()), bold));
        money.add(labelBold("Tổng tiền chi phí phát sinh: " + formatVND(tam.getTongTienChiPhiPhatSinh()), bold));
        money.add(labelBold("TỔNG TIỀN: " + formatVND(tam.getTongTien()), bold));
        money.add(labelBold("TIỀN ĐÃ CỌC: " + formatVND(tam.getTienCoc()), bold));

        money.add(labelBold(
                "TIỀN KHÁCH CẦN TRẢ: " + formatVND(tam.getTongTien() - tam.getTienCoc()),
                bold
        ));

        main.add(money);
    }


    private JLabel labelBold(String text, Font f) {
        JLabel lb = new JLabel(text);
        lb.setFont(f);
        return lb;
    }

    private JLabel labelNormal(String text, Font f) {
        JLabel lb = new JLabel(text);
        lb.setFont(f);
        return lb;
    }

    private String formatVND(double v) {
        return String.format("%,.0f VNĐ", v);
    }
}
