package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import entity.*;

public class InPhieuDatPhong_GUI extends JFrame {

    private PhieuDatPhong phieu;
    private KhachHang kh;
    private DefaultTableModel modelPhong;

    public InPhieuDatPhong_GUI(
            PhieuDatPhong phieu,
            DefaultTableModel modelPhong,
            KhachHang kh,
            String maNV,
            String trangThai,
            PhieuDatPhong p,
            double tienCocMoi,
            double tienCocCu
    ) {
        this.phieu = phieu;
        this.modelPhong = modelPhong;
        this.kh = kh;

        setTitle("Phiếu đặt phòng");
        setSize(780, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(15, 25, 15, 25));
        add(main);

        // ================= TIÊU ĐỀ =================
        JLabel lblTitle = new JLabel("PHIẾU ĐẶT PHÒNG");
        lblTitle.setFont(new Font("Serif", Font.BOLD, 26));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(lblTitle);

        main.add(Box.createVerticalStrut(10));

        // ================= THÔNG TIN KHÁCH SẠN =================
        JPanel hotel = new JPanel();
        hotel.setLayout(new BoxLayout(hotel, BoxLayout.Y_AXIS));
        hotel.setOpaque(false);
        hotel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Font fontHotel = new Font("SansSerif", Font.PLAIN, 13);
        Font fontHotelBold = new Font("SansSerif", Font.BOLD, 14);

        JLabel lblTenKS = new JLabel("KHÁCH SẠN PATE");
        lblTenKS.setFont(fontHotelBold);
        lblTenKS.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDiaChi = new JLabel("Địa chỉ: 12 Nguyễn Văn Bảo, P4, Gò Vấp, TP.HCM");
        lblDiaChi.setFont(fontHotel);
        lblDiaChi.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSDT = new JLabel("SĐT: 1233456789");
        lblSDT.setFont(fontHotel);
        lblSDT.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblWifi = new JLabel("WiFi: pate – Pass: xincamon");
        lblWifi.setFont(fontHotel);
        lblWifi.setAlignmentX(Component.CENTER_ALIGNMENT);

        hotel.add(lblTenKS);
        hotel.add(Box.createVerticalStrut(2));
        hotel.add(lblDiaChi);
        hotel.add(Box.createVerticalStrut(2));
        hotel.add(lblSDT);
        hotel.add(Box.createVerticalStrut(2));
        hotel.add(lblWifi);


        main.add(hotel);
        main.add(Box.createVerticalStrut(15));

        // ================= THÔNG TIN PHIẾU =================
        JPanel info = new JPanel(new GridLayout(5, 1, 2, 2));
        info.setBorder(new CompoundBorder(
                new LineBorder(Color.BLACK),
                new EmptyBorder(8, 10, 8, 10)
        ));
        info.setMaximumSize(new Dimension(720, 140));
        info.setOpaque(false);

        info.add(new JLabel("Mã phiếu: " + phieu.getMaPhieuDatPhong()));
        info.add(new JLabel("Khách hàng: " + kh.getHoTen()));
        info.add(new JLabel("Mã KH: " + kh.getMaKhachHang()));
        info.add(new JLabel("SĐT: " + kh.getSoDienThoai()));
        info.add(new JLabel("Quốc tịch: " + (kh.LaNguoiVietNam() ? "Việt Nam" : "Nước ngoài")));

        main.add(info);
        main.add(Box.createVerticalStrut(15));

        // ================= BẢNG PHÒNG =================
        String[] cols = {
                "STT", "Phòng", "Loại phòng",
                "Ngày nhận", "Ngày trả", "Ngày trả TT",
                "Số đêm", "Giá", "Thành tiền"
        };

        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(70);
        table.getColumnModel().getColumn(7).setPreferredWidth(80);
        table.getColumnModel().getColumn(8).setPreferredWidth(130);

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

        int soDong = model.getRowCount();
        int chieuCaoBang = soDong * table.getRowHeight()
                + table.getTableHeader().getPreferredSize().height;

        JPanel bangPanel = new JPanel(new BorderLayout());
        bangPanel.setBorder(new LineBorder(Color.BLACK));
        bangPanel.setMaximumSize(new Dimension(720, chieuCaoBang + 2));

        bangPanel.add(table.getTableHeader(), BorderLayout.NORTH);
        bangPanel.add(table, BorderLayout.CENTER);

        main.add(bangPanel);
        main.add(Box.createVerticalStrut(15));

        // ================= TIỀN =================
        JPanel money = new JPanel(new GridLayout(5, 1, 4, 4));
        money.setBorder(new CompoundBorder(
                new LineBorder(Color.BLACK),
                new EmptyBorder(8, 10, 8, 10)
        ));
        money.setMaximumSize(new Dimension(720, 160));
        money.setOpaque(false);

        JLabel lblTongTien = new JLabel("Tổng tiền phòng: "
                + String.format("%,.0f VNĐ", p.getTongTien()));
        JLabel lblTienCoc = new JLabel("Tổng tiền cọc: "
                + String.format("%,.0f VNĐ", p.getTienCoc()));
        JLabel lblTienCocMoi = new JLabel("Tiền cọc mới: "
                + String.format("%,.0f VNĐ", tienCocMoi));
        JLabel lblTienCocCu = new JLabel("Tiền cọc cũ: "
                + String.format("%,.0f VNĐ", tienCocCu));
        JLabel lblPhaiTT = new JLabel("Tiền cọc phải thanh toán: "
                + String.format("%,.0f VNĐ", (p.getTienCoc() - tienCocCu)));

        lblTongTien.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblPhaiTT.setFont(new Font("SansSerif", Font.BOLD, 14));

        money.add(lblTongTien);
        money.add(lblTienCoc);
        money.add(lblTienCocMoi);
        money.add(lblTienCocCu);
        money.add(lblPhaiTT);

        main.add(money);
        main.add(Box.createVerticalStrut(20));

        setVisible(true);
    }
}
