package gui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import entity.Phong;

public class GoiYPhong_GUI extends JDialog {

    private int soLuongNguoi;
    private List<Phong> dsPhongTrong;

    public GoiYPhong_GUI(List<Phong> dsPhongTrong, int soLuongNguoi) {
        this.dsPhongTrong = dsPhongTrong;
        this.soLuongNguoi = soLuongNguoi;
   
        setTitle("Gợi ý lựa chọn phòng");
        setSize(600, 450);
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("🔍 Tổ hợp phòng tối ưu cho " + soLuongNguoi + " người");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        contentPanel.add(lblTitle);
        contentPanel.add(Box.createVerticalStrut(10));

        // Thêm bảng
        contentPanel.add(taoBangToHopPhong());

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(e -> dispose());
        add(btnOK, BorderLayout.SOUTH);
    }

    // ================================
    // 1) Tìm tổ hợp tối ưu
    // ================================
    private List<Phong> timToHopPhongToiUu(List<Phong> ds, int soNguoi) {

        ds = ds.stream()
                .sorted(Comparator.comparingDouble(p -> p.getLoaiPhong().getGia()))
                .collect(Collectors.toList());

        int n = ds.size();
        List<Phong> best = null;
        int minPhong = Integer.MAX_VALUE;
        double minGia = Double.MAX_VALUE;

        for (int mask = 1; mask < (1 << n); mask++) {
            List<Phong> t = new ArrayList<>();
            int sucChua = 0;
            double gia = 0;

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    Phong p = ds.get(i);
                    t.add(p);
                    sucChua += p.getLoaiPhong().getSucChua();
                    gia += p.getLoaiPhong().getGia();
                }
            }

            if (sucChua >= soNguoi) {
                if (t.size() < minPhong || (t.size() == minPhong && gia < minGia)) {
                    best = t;
                    minPhong = t.size();
                    minGia = gia;
                }
            }
        }

        return best;
    }

    // ================================
    // 2) Tạo bảng JTable + tổng kết
    // ================================
    private JPanel taoBangToHopPhong() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        List<Phong> best = timToHopPhongToiUu(dsPhongTrong, soLuongNguoi);

        if (best == null) {
            JLabel lbl = new JLabel("❌ Không tìm được tổ hợp phòng phù hợp!");
            lbl.setFont(new Font("Arial", Font.BOLD, 14));
            lbl.setForeground(Color.RED);
            panel.add(lbl);
            return panel;
        }

        // ============================
        // Dữ liệu bảng
        // ============================
        String[] col = {"Loại phòng", "Sức chứa", "Giá (VNĐ)"};
        Object[][] data = new Object[best.size()][3];

        int tongSuc = 0;
        int tongTien = 0;

        for (int i = 0; i < best.size(); i++) {
            data[i][0] = best.get(i).getLoaiPhong().getTenLoaiPhong();
            data[i][1] = best.get(i).getLoaiPhong().getSucChua();
            data[i][2] = best.get(i).getLoaiPhong().getGia();

            tongSuc += best.get(i).getLoaiPhong().getSucChua();
            tongTien += best.get(i).getLoaiPhong().getGia();
        }

        JTable tbl = new JTable(data, col);
        tbl.setRowHeight(26);
        tbl.setFont(new Font("Arial", Font.PLAIN, 13));
        tbl.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // ⭐ Giảm kích thước bảng cho gọn
        int height = best.size() * 28 + 35;
        tbl.setPreferredScrollableViewportSize(new Dimension(500, height));

        JScrollPane sc = new JScrollPane(tbl);
        panel.add(sc);
        panel.add(Box.createVerticalStrut(10));

        // ============================
        // Tổng kết theo từng loại phòng
        // ============================
        Map<String, Long> thongKeLoaiPhong =
                best.stream().collect(Collectors.groupingBy(
                        p -> p.getLoaiPhong().getTenLoaiPhong(),
                        Collectors.counting()
                ));

        JLabel lblTong = new JLabel("• Tổng số phòng: " + best.size());
        lblTong.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTong);

        for (String loai : thongKeLoaiPhong.keySet()) {
            JLabel lb = new JLabel("   ➤ " + loai + ": " + thongKeLoaiPhong.get(loai) + " phòng");
            lb.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(lb);
        }

        JLabel lblSuc = new JLabel("• Tổng sức chứa: " + tongSuc + " người");
        JLabel lblTien = new JLabel("• Tổng tiền: " + tongTien + " VNĐ");

        lblSuc.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTien.setFont(new Font("Arial", Font.BOLD, 15));

        panel.add(lblSuc);
        panel.add(lblTien);

        return panel;
    }
}
