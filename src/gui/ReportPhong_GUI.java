package gui;

import entity.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportPhong_GUI extends JDialog {

    public ReportPhong_GUI(PhieuDatPhong phieu, KhachHang kh, DefaultTableModel modelPhong) {

        setTitle("Xem trước hóa đơn");
        setSize(480, 700);
        setLocationRelativeTo(null);
        setModal(true);

        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(new EmptyBorder(15, 20, 15, 20));
        add(main);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(area);
        main.add(scroll, BorderLayout.CENTER);

        StringBuilder bill = new StringBuilder();

        // ===== HEADER =====
        bill.append("          🐾 KHÁCH SẠN PATE 🐾\n");
        bill.append("12 Nguyễn Văn Bảo, Phường 4, Gò Vấp, TP.HCM\n");
        bill.append("ĐT: (028) 1234 5678\n");
        bill.append("----------------------------------------------\n");

        // ===== THÔNG TIN =====
        bill.append(String.format("Ngày lập:      %s\n", phieu.getNgayTao()));
        bill.append(String.format("Mã phiếu:      %s\n", phieu.getMaPhieuDatPhong()));
        bill.append(String.format("Khách hàng:    %s\n", kh.getHoTen()));
        bill.append(String.format("SĐT:           %s\n", kh.getSoDienThoai()));
        bill.append(String.format("Quốc tịch:     %s\n",
                kh.LaNguoiVietNam() ? "Việt Nam" : "Nước ngoài"));
        bill.append("----------------------------------------------\n");

        bill.append("Phòng  Loại     Đêm   Giá     Thành tiền\n");
        bill.append("----------------------------------------------\n");

        // ===== DANH SÁCH PHÒNG =====
        double tongTienPhong = 0;

        for (int i = 0; i < modelPhong.getRowCount(); i++) {

            String maPhong = modelPhong.getValueAt(i, 1).toString();
            String loai = modelPhong.getValueAt(i, 2).toString();
            int soDem = Integer.parseInt(modelPhong.getValueAt(i, 6).toString());
            double gia = Double.parseDouble(modelPhong.getValueAt(i, 7).toString());
            double thanhTien = Double.parseDouble(modelPhong.getValueAt(i, 8).toString());

            tongTienPhong += thanhTien;

            bill.append(String.format("%-6s %-8s %-5d %-8.0f %-10.0f\n",
                    maPhong, loai, soDem, gia, thanhTien));
        }

        bill.append("----------------------------------------------\n");

        // ===== TỔNG KẾT =====
        bill.append(String.format("Tiền phòng:     %, .0f VNĐ\n", tongTienPhong));
        bill.append(String.format("Tiền cọc:       %, .0f VNĐ\n", phieu.getTienCoc()));
        bill.append(String.format("Tổng cộng:      %, .0f VNĐ\n",
                tongTienPhong - phieu.getTienCoc()));
        bill.append("----------------------------------------------\n");

        bill.append("Thanh toán: Tiền mặt / Chuyển khoản\n\n");

        bill.append("Wifi: PATE | Pass: camonquykhach\n");
        bill.append("Cảm ơn quý khách! Hẹn gặp lại ❤️\n");

        area.setText(bill.toString());

        // Nút đóng
        JButton btn = new JButton("Đóng");
        btn.addActionListener(e -> dispose());

        JPanel south = new JPanel();
        south.add(btn);
        main.add(south, BorderLayout.SOUTH);
    }
}
