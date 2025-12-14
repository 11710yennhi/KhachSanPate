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
    private static final int MAX_PHONG = 12;

    private List<Phong> timToHopPhongToiUu(List<Phong> dsPhong, int soNguoi) {

        if (dsPhong == null || dsPhong.isEmpty())
            return null;

        List<Phong> p3 = new ArrayList<>();
        List<Phong> p5 = new ArrayList<>();
        List<Phong> p7 = new ArrayList<>();

        for (Phong p : dsPhong) {
            int suc = p.getLoaiPhong().getSucChua();
            if (suc == 3) p3.add(p);
            else if (suc == 5) p5.add(p);
            else if (suc == 7) p7.add(p);
        }

        Comparator<Phong> cmpGia =
                Comparator.comparingDouble(p -> p.getLoaiPhong().getGia());

        p3.sort(cmpGia);
        p5.sort(cmpGia);
        p7.sort(cmpGia);

        List<Phong> best = null;
        int bestDu = Integer.MAX_VALUE;
        int bestSoPhong = Integer.MAX_VALUE;

        // duyệt số lượng phòng → rất nhanh vì chỉ 3 loại
        for (int i = 0; i <= p7.size(); i++) {
            for (int j = 0; j <= p5.size(); j++) {
                for (int k = 0; k <= p3.size(); k++) {

                    int soPhong = i + j + k;
                    if (soPhong == 0 || soPhong > MAX_PHONG)
                        continue;

                    int tongSuc = i * 7 + j * 5 + k * 3;
                    if (tongSuc < soNguoi)
                        continue;

                    int du = tongSuc - soNguoi;

                    boolean totHon =
                            du < bestDu ||
                           (du == bestDu && soPhong < bestSoPhong);

                    if (best == null || totHon) {
                        best = new ArrayList<>();
                        best.addAll(p7.subList(0, i));
                        best.addAll(p5.subList(0, j));
                        best.addAll(p3.subList(0, k));

                        bestDu = du;
                        bestSoPhong = soPhong;
                    }
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
