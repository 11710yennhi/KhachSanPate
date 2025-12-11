package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import dao.ChiTietPhieuDatPhong_DAO;
import dao.KhachHang_DAO;
import dao.PhieuDatPhong_DAO;
import dao.Phong_DAO;
import entity.*;

public class InPhieuDatPhong_GUI extends JFrame implements ActionListener {

    private DefaultTableModel modelPhong;

    private PhieuDatPhong phieu;
    private KhachHang khachHang;
    private String maNV;
    private String trangThai;

    private PhieuDatPhong_DAO pdpDAO;
    private ChiTietPhieuDatPhong_DAO ctDAO;
    private KhachHang_DAO khd;
    private Phong_DAO pd;

    private JLabel lblTongTien, lblTienCoc, lblTienCocMoi;

    public InPhieuDatPhong_GUI(
            PhieuDatPhong phieu,
            DefaultTableModel modelPhong,
            KhachHang kh,
            String maNV,
            String trangThai,
            PhieuDatPhong p,
            double tienCocMoi) {

        this.phieu = phieu;
        this.modelPhong = modelPhong;
        this.khachHang = kh;
        this.maNV = maNV;
        this.trangThai = trangThai;

        pdpDAO = new PhieuDatPhong_DAO();
        ctDAO = new ChiTietPhieuDatPhong_DAO();
        khd = new KhachHang_DAO();
        pd = new Phong_DAO();

        setTitle("Phiếu đặt phòng");
        setSize(750, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(15, 20, 15, 20));
        add(main);

        // ===== TIÊU ĐỀ =====
        JLabel lblTitle = new JLabel("PHIẾU ĐẶT PHÒNG");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 26));
        main.add(lblTitle);

        main.add(Box.createVerticalStrut(5));

        // ===== THÔNG TIN KHÁCH SẠN =====
        JLabel ks1 = new JLabel("KHÁCH SẠN PATE");
        JLabel ks2 = new JLabel("Địa chỉ: 12 Nguyễn Văn Bảo, P4, Gò Vấp, TP.HCM");
        JLabel ks3 = new JLabel("Số điện thoại: 1233456789");
        JLabel ks4 = new JLabel("WiFi: pate – Pass: xincamon");

        ks1.setAlignmentX(Component.CENTER_ALIGNMENT);
        ks2.setAlignmentX(Component.CENTER_ALIGNMENT);
        ks3.setAlignmentX(Component.CENTER_ALIGNMENT);
        ks4.setAlignmentX(Component.CENTER_ALIGNMENT);

        ks1.setFont(new Font("SansSerif", Font.BOLD, 14));

        main.add(ks1);
        main.add(ks2);
        main.add(ks3);
        main.add(ks4);

        main.add(Box.createVerticalStrut(15));

        // ===== THÔNG TIN PHIẾU =====
        JPanel info = new JPanel(new GridLayout(5, 1, 2, 2));
        info.setOpaque(false);

        info.add(new JLabel("Mã phiếu: " + phieu.getMaPhieuDatPhong()));
        info.add(new JLabel("Mã khách hàng: " + kh.getMaKhachHang()));
        info.add(new JLabel("Tên khách hàng: " + kh.getHoTen()));
        info.add(new JLabel("Số điện thoại: " + kh.getSoDienThoai()));
        info.add(new JLabel("Quốc tịch: " + (kh.LaNguoiVietNam() ? "Việt Nam" : "Nước ngoài")));

        main.add(info);
        main.add(Box.createVerticalStrut(15));

        // ===== BẢNG PHÒNG =====
        String[] cols = {
                "STT", "Mã phòng", "Loại phòng",
                "Ngày nhận", "Ngày trả", "Ngày trả thực",
                "Số đêm", "Giá", "Thành tiền"
        };

        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.setFillsViewportHeight(true);

     // ===== CHỈNH KÍCH THƯỚC CỘT CHUẨN GIỐNG HÌNH =====
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getColumnModel().getColumn(0).setPreferredWidth(40);   // STT
        table.getColumnModel().getColumn(1).setPreferredWidth(80);   // Mã phòng
        table.getColumnModel().getColumn(2).setPreferredWidth(220);  // Loại phòng
        table.getColumnModel().getColumn(3).setPreferredWidth(90);  // Ngày nhận
        table.getColumnModel().getColumn(4).setPreferredWidth(90);  // Ngày trả
        table.getColumnModel().getColumn(5).setPreferredWidth(90);  // Ngày trả thực tế
        table.getColumnModel().getColumn(6).setPreferredWidth(60);   // Số đêm
        table.getColumnModel().getColumn(7).setPreferredWidth(90);   // Giá
        table.getColumnModel().getColumn(8).setPreferredWidth(150);  // Thành tiền

      
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        int stt = 1;
        for (int i = 0; i < modelPhong.getRowCount(); i++) {
            model.addRow(new Object[]{
                    stt++,
                    modelPhong.getValueAt(i, 1),
                    modelPhong.getValueAt(i, 2),
                    modelPhong.getValueAt(i, 3),
                    modelPhong.getValueAt(i, 4),
                    modelPhong.getValueAt(i, 5),
                    modelPhong.getValueAt(i, 6),
                    modelPhong.getValueAt(i, 7),
                    modelPhong.getValueAt(i, 8)
            });
        }

        JScrollPane scroll = new JScrollPane(table);
        main.add(scroll);

        main.add(Box.createVerticalStrut(15));

        // ===== TIỀN =====
        JPanel money = new JPanel(new GridLayout(3, 1));
        money.setOpaque(false);

        lblTongTien = new JLabel("Tổng tiền: " + String.format("%,.0f VNĐ", p.getTongTien()));
        lblTienCoc = new JLabel("Tổng tiền cọc: " + String.format("%,.0f VNĐ", p.getTienCoc()));
        lblTienCocMoi = new JLabel("Tiền cọc mới: " + String.format("%,.0f VNĐ", tienCocMoi));

        lblTongTien.setFont(new Font("SansSerif", Font.BOLD, 15));

        money.add(lblTongTien);
        money.add(lblTienCoc);
        money.add(lblTienCocMoi);

        main.add(money);

        main.add(Box.createVerticalStrut(20));

        // ===== KHÔNG CÓ NÚT XÁC NHẬN =====
        // đã bỏ hoàn toàn phần button

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Không còn xử lý gì vì đã bỏ nút
    }
}
