package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import dao.ChiTietPhieuDatPhong_DAO;
import dao.KhachHang_DAO;
import dao.PhieuDatPhong_DAO;
import entity.*;

public class DanhSachPhieuDatPhong_GUI extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTextField txtSDT, txtMaPhieu;
    private JComboBox<String> cbbLoc;
    private JPanel pnlDanhSach;

    private PhieuDatPhong_DAO phieuDAO;
    private ChiTietPhieuDatPhong_DAO ctDAO;
    private List<PhieuDatPhong> dsPhieu;
    private KhachHang_DAO khd;

    public DanhSachPhieuDatPhong_GUI() {
        phieuDAO = new PhieuDatPhong_DAO();
        ctDAO = new ChiTietPhieuDatPhong_DAO();
        dsPhieu = new ArrayList<>();
        khd = new KhachHang_DAO();

        setLayout(new BorderLayout());
        add(khungTimKiem(), BorderLayout.NORTH);
        add(khungDanhSach(), BorderLayout.CENTER);

        taiDuLieu();
//        hienThiDanhSach(dsPhieu);
        locTheoTrangThai();
    }
    private JPanel khungTimKiem() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Tìm kiếm & Lọc", TitledBorder.LEFT, TitledBorder.TOP));
        pnl.setPreferredSize(new Dimension(0, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 0;

        JLabel lblSDT = new JLabel("SĐT KH:");
        JLabel lblMa = new JLabel("Mã phiếu:");
        JLabel lblLoc = new JLabel("Trạng thái:");

        txtSDT = new JTextField(14);        // dài hơn
        txtMaPhieu = new JTextField(14);    // dài hơn

        cbbLoc = new JComboBox<>(new String[] {
                "Đã đặt", "Tới ngày nhận","Chưa nhận phòng", "Tới ngày trả","Trễ hạn trả phòng", "Đang ở", "Đã hủy", "Hoàn thành"
        });
        Color mauXanhDam = new Color(30, 61, 89);
        Color mauVangDong = new Color(212, 175, 55);
        Color mauNen = new Color(249, 249, 249);
        Color mauHover = new Color(45, 85, 120);

  
        JButton btnLocSDT = new JButton("🔍");
        btnLocSDT.setBackground(mauXanhDam);
        btnLocSDT.setForeground(Color.WHITE);

    
        JButton btnLocMa = new JButton("🔍");
        btnLocMa.setBackground(mauXanhDam);
        btnLocMa.setForeground(Color.WHITE);

    
        JButton btnLocTrangThai = new JButton("🔍");
        btnLocTrangThai.setBackground(mauVangDong);
        btnLocTrangThai.setForeground(Color.BLACK);

 
        JButton btnReset = new JButton("♻️");
        btnReset.setBackground(new Color(102, 187, 106));
        btnReset.setForeground(Color.WHITE);



        btnLocSDT.addActionListener(e -> locTheoSDT());
        btnLocMa.addActionListener(e -> locTheoMa());
        btnLocTrangThai.addActionListener(e -> locTheoTrangThai());
        btnReset.addActionListener(e -> {
//            txtSDT.setText("");
//            txtMaPhieu.setText("");
            taiDuLieu();
            cbbLoc.setSelectedIndex(0);
            locTheoTrangThai();
        });


        // ======== SẮP XẾP 1 HÀNG ========

        gbc.gridx = 0; pnl.add(lblSDT, gbc);
        gbc.gridx = 1; pnl.add(txtSDT, gbc);
        gbc.gridx = 2; pnl.add(btnLocSDT, gbc);

        gbc.gridx = 3; pnl.add(lblMa, gbc);
        gbc.gridx = 4; pnl.add(txtMaPhieu, gbc);
        gbc.gridx = 5; pnl.add(btnLocMa, gbc);

        gbc.gridx = 6; pnl.add(lblLoc, gbc);
        gbc.gridx = 7; pnl.add(cbbLoc, gbc);
        gbc.gridx = 8; pnl.add(btnLocTrangThai, gbc);

        gbc.gridx = 9; pnl.add(btnReset, gbc);

        return pnl;
    }

    
    private void locTheoSDT() {
        String sdt = txtSDT.getText().trim();

        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập SĐT để lọc");
            return;
        }

        // 🔹 Tìm khách hàng theo SĐT chính xác
        KhachHang kh = khd.getKhachHangTheoSDT(sdt);

        if (kh == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng có SĐT: " + sdt);
            return;
        }

        // 🔹 Lọc các phiếu của đúng khách hàng đó
        List<PhieuDatPhong> kq = new ArrayList<>();
        for (PhieuDatPhong p : dsPhieu) {
            if (p.getKhachHang() != null
                    && p.getKhachHang().getMaKhachHang().equals(kh.getMaKhachHang())) {
                kq.add(p);
            }
        }

        // 🔹 Nếu không có phiếu
        if (kq.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Khách hàng này chưa có phiếu đặt phòng");
        }

        hienThiDanhSach(kq);
    }


    private void locTheoMa() {
        String ma = txtMaPhieu.getText().trim().toLowerCase();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã phiếu để lọc");
            return;
        }

        List<PhieuDatPhong> kq = new ArrayList<>();

        for (PhieuDatPhong p : dsPhieu) {
            String maP = p.getMaPhieuDatPhong();
            if (maP != null && maP.toLowerCase().contains(ma)) {
                kq.add(p);
            }
        }

        hienThiDanhSach(kq);
    }


    private void locTheoTrangThai() {
        String loc = (String) cbbLoc.getSelectedItem();

        List<PhieuDatPhong> kq = new ArrayList<>();

        for (PhieuDatPhong p : dsPhieu) {
            String tt = xacDinhTrangThai(p);
            if (tt.equals(loc)) kq.add(p);
        }

        hienThiDanhSach(kq);
    }


    private JScrollPane khungDanhSach() {
        pnlDanhSach = new JPanel(new GridLayout(0, 4, 10, 10));
        pnlDanhSach.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        return new JScrollPane(pnlDanhSach);
    }

    /**
     * Tải danh sách phiếu và với mỗi phiếu load danh sách chi tiết
     */
    private void taiDuLieu() {
        dsPhieu.clear();
        List<PhieuDatPhong> all = phieuDAO.getAllPhieuDatPhong();
        if (all == null) return;

        for (PhieuDatPhong p : all) {

            // ======= LOAD KHÁCH HÀNG =======
            if (p.getKhachHang() != null && p.getKhachHang().getMaKhachHang() != null) {
                KhachHang kh = khd.getKhachHangTheoMa(p.getKhachHang().getMaKhachHang());
                p.setKhachHang(kh);  // Gán ngược lại cho phiếu
            }

            // ====== LOAD CHI TIẾT ======
            List<ChiTietPhieuDatPhong> dsCT =
                    ctDAO.getChiTietTheoMaPhieu(p.getMaPhieuDatPhong());

            if (dsCT == null) dsCT = new ArrayList<>();
            p.setDsChiTiet(dsCT);

            dsPhieu.add(p);
        }
    }


    private void hienThiDanhSach(List<PhieuDatPhong> ds) {
        pnlDanhSach.removeAll();

        if (ds == null || ds.isEmpty()) {
            JLabel lb = new JLabel("Không có phiếu nào");
            lb.setHorizontalAlignment(SwingConstants.CENTER);
            pnlDanhSach.setLayout(new GridLayout(1,1));
            pnlDanhSach.add(lb);
        } else {
            pnlDanhSach.setLayout(new GridLayout((ds.size()+3)/4, 4, 10, 10));
            for (PhieuDatPhong p : ds) {
                pnlDanhSach.add(taoCard(p));
            }
        }

        pnlDanhSach.revalidate();
        pnlDanhSach.repaint();
    }

    private JPanel taoCard(PhieuDatPhong p) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createTitledBorder(p.getMaPhieuDatPhong()));
        card.setBackground(Color.WHITE);
        card.setOpaque(true);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setToolTipText("Nhấp để xem chi tiết phiếu");

        String ten = p.getKhachHang() != null ? p.getKhachHang().getHoTen() : "-";
        String sdt = p.getKhachHang() != null ? p.getKhachHang().getSoDienThoai() : "-";

        JLabel lblTen = new JLabel("Khách: " + ten);
        JLabel lblSDT = new JLabel("SĐT: " + sdt);
        JLabel lblTrangThai = new JLabel("Trạng thái: " + xacDinhTrangThai(p));

        card.add(Box.createVerticalStrut(6));
        card.add(lblTen);
        card.add(lblSDT);
        card.add(Box.createVerticalStrut(6));
        card.add(lblTrangThai);
        card.add(Box.createVerticalGlue());

        // Mouse click mở TaoPhieuDatPhong_GUI với phiếu p
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Mở form chi tiết / sửa phiếu
                JFrame frame = new JFrame("Phiếu: " + p.getMaPhieuDatPhong());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setContentPane(new TaoPhieuDatPhong_GUI(p,"NV01122018001"));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(235, 245, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    /**
     * Xác định trạng thái của phiếu dựa trên toàn bộ dsChiTiet (chỉ dùng ngayNhanThuc và ngayTra)
     */
    private String xacDinhTrangThai(PhieuDatPhong p) {
        if (p == null || p.getDsChiTiet() == null || p.getDsChiTiet().isEmpty()) return "Không có chi tiết";

        LocalDate today = LocalDate.now();

        Optional<LocalDate> minNhan = p.getDsChiTiet().stream()
                .map(ChiTietPhieuDatPhong::getNgayNhanThuc)
                .filter(d -> d != null)
                .min(LocalDate::compareTo);

        Optional<LocalDate> maxTra = p.getDsChiTiet().stream()
                .map(ChiTietPhieuDatPhong::getNgayTraThuc)
                .filter(d -> d != null)
                .max(LocalDate::compareTo);

        LocalDate nhanSomNhat = minNhan.orElse(null);
        LocalDate traTreNhat = maxTra.orElse(null);
     // Đã hủy luôn ưu tiên
        if (p.getTrangThai().equals("Đã hủy")) {
            return "Đã hủy";
        }
        // Trễ hạn trả phòng
        if (traTreNhat != null 
                && today.isAfter(traTreNhat) 
                &&(p.getTrangThai().equals("Đang ở"))) {
            return "Trễ hạn trả phòng";
        }
        // Tới ngày trả
        if (traTreNhat != null 
                && today.isEqual(traTreNhat) 
                && p.getTrangThai().equals("Đang ở")) {
            return "Tới ngày trả";
        }
        // Đang ở (ưu tiên cao)
        if (p.getTrangThai().equals("Đang ở")) {
            return "Đang ở";
        }

        // Chưa nhận phòng (quá ngày nhận nhưng chưa check-in)
        if (nhanSomNhat != null 
                && nhanSomNhat.isBefore(today) 
                && p.getTrangThai().equals("Đã đặt")) {
            return "Chưa nhận phòng";
        }

        // Tới ngày nhận
        if (nhanSomNhat != null 
                && today.isEqual(nhanSomNhat) 
                && p.getTrangThai().equals("Đã đặt")) {
            return "Tới ngày nhận";
        }

     

        // Đã đặt (chưa tới ngày nhận)
        if (nhanSomNhat != null 
                && today.isBefore(nhanSomNhat) 
                && p.getTrangThai().equals("Đã đặt")) {
            return "Đã đặt";
        }

        // Mặc định
        return "Hoàn thành";

    }

    /**
     * Tìm theo SĐT, mã phiếu và trạng thái chọn trong combobox
     */
    private List<PhieuDatPhong> timKiem() {
    	

        String sdt = txtSDT.getText().trim();
        String ma = txtMaPhieu.getText().trim();
        String loc = (String) cbbLoc.getSelectedItem();

        // ===== 1. VALIDATE ĐIỀU KIỆN NHẬP =====
        if (sdt.isEmpty() && ma.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập SĐT hoặc Mã phiếu để tìm kiếm",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
            return new ArrayList<>();
        }

        // ===== 2. VALIDATE SĐT =====
        if (!sdt.isEmpty()) {
            if (!sdt.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(
                        this,
                        "SĐT phải gồm đúng 10 chữ số",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE
                );
                return new ArrayList<>();
            }
        }

        List<PhieuDatPhong> ketQua = new ArrayList<>();
        String maTim = ma.toLowerCase();

        // ===== 3. LỌC DANH SÁCH =====
        for (PhieuDatPhong p : dsPhieu) {

            // --- SĐT ---
            if (!sdt.isEmpty()) {
                String sdtKH = (p.getKhachHang() != null)
                        ? p.getKhachHang().getSoDienThoai()
                        : null;

                if (!sdt.equals(sdtKH)) continue;
            }

            // --- MÃ PHIẾU (TƯƠNG ĐƯƠNG) ---
            if (!ma.isEmpty()) {
                String maP = p.getMaPhieuDatPhong();
                if (maP == null || !maP.toLowerCase().contains(maTim))
                    continue;
            }

            // --- TRẠNG THÁI ---
            if (loc != null) {
                String trangThai = xacDinhTrangThai(p);
                if (!loc.equals(trangThai))
                    continue;
            }

            ketQua.add(p);
        }

        // ===== 4. THÔNG BÁO =====
        if (ketQua.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không tìm thấy phiếu đặt phòng phù hợp",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        return ketQua;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        hienThiDanhSach(timKiem());
    }

    // Helpers
    public void forceReloadFromDAO() {
//        taiDuLieu();
//        hienThiDanhSach(dsPhieu);
    	taiDuLieu();
    	cbbLoc.setSelectedIndex(0);
    	locTheoTrangThai();

    }

    public List<PhieuDatPhong> getDsPhieuHienTai() {
        return Collections.unmodifiableList(dsPhieu);
    }
}
