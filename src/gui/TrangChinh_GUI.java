package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TrangChinh_GUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private CardLayout cardLayout;
    private JPanel pnlContent, pnlMenu;
    private JButton btnDangChon;

    // ===== MÀU CHỦ ĐẠO =====
    private final Color MAU_MENU_NEN = new Color(30, 61, 89);      // nền menu
    private final Color MAU_NUT = new Color(45, 85, 120);          // nút thường
    private final Color MAU_HOVER = new Color(70, 130, 180);       // hover
    private final Color MAU_CHON = new Color(90, 155, 210);        // đang chọn
    private final Color MAU_NEN = new Color(249, 249, 249);
    private final Color MAU_SUB = new Color(55, 105, 145);
    private JButton btnSubDangChon = null;
//    private final Color MAU_SUB = new Color(65, 110, 160);
    private final Color MAU_SUB_CHON = new Color(90, 155, 210);


    private boolean moQLDP = false;

    public TrangChinh_GUI() {
        setTitle("Pate Hotel - Hệ thống quản lý khách sạn");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        getContentPane().setBackground(MAU_NEN);

        taoTieuDe();
        taoMenuTrai();
        taoNoiDung();
        
        pnlMenu.add(Box.createVerticalGlue());
        JButton btnDangXuat = new JButton("Đăng xuất");
        btnDangXuat.setMaximumSize(new Dimension(230, 42));
        btnDangXuat.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDangXuat.setBackground(new Color(212, 175, 55)); // vàng đồng
        btnDangXuat.setForeground(new Color(30, 61, 89));
        btnDangXuat.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        btnDangXuat.setFocusPainted(false);
        btnDangXuat.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        btnDangXuat.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn đăng xuất?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );
            if (c == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        pnlMenu.add(btnDangXuat);

    }

    // ================= TIÊU ĐỀ =================
    private void taoTieuDe() {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(getWidth(), 60));
        p.setBackground(MAU_MENU_NEN);

        JLabel lbl = new JLabel("PATE HOTEL - HỆ THỐNG QUẢN LÝ", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        lbl.setForeground(Color.WHITE);

        JLabel time = new JLabel();
        time.setForeground(Color.WHITE);
        time.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        new Timer(1000, e ->
                time.setText(java.time.LocalTime.now().withNano(0).toString())
        ).start();

        p.add(lbl, BorderLayout.CENTER);
        p.add(time, BorderLayout.EAST);

        add(p, BorderLayout.NORTH);
    }

    // ================= MENU TRÁI =================
    private void taoMenuTrai() {
        pnlMenu = new JPanel();
        pnlMenu.setBackground(MAU_MENU_NEN);
        pnlMenu.setPreferredSize(new Dimension(260, getHeight()));
        pnlMenu.setLayout(new BoxLayout(pnlMenu, BoxLayout.Y_AXIS));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel logo = new JLabel("PATE HOTEL");
        logo.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setBorder(BorderFactory.createEmptyBorder(10, 0, 25, 0));
        pnlMenu.add(logo);

        addMenu("Trang Chủ", "trangchu");
        addMenuQLDP();
        addMenu("Quản Lý Phòng", "phong");
        addMenu("Khuyến Mãi", "khuyenmai");
        addMenu("Chi Phí Phát Sinh", "chiphiphatsinh");
        addMenu("Hóa Đơn", "hoadon");
        addMenu("Thống Kê", "thongke");
        addMenu("Khách Hàng", "khachhang");
        addMenu("Nhân Viên", "nhanvien");
        addMenu("Tài Khoản", "taikhoan");
        addMenu("HDSD", "huongdan");


        add(pnlMenu, BorderLayout.WEST);
    }

    // ================= NỘI DUNG =================
    private void taoNoiDung() {
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        pnlContent.setBackground(MAU_NEN);

        pnlContent.add(new DanhSachPhieuDatPhong_GUI(), "Danh Sách Phiếu Đặt Phòng");
        pnlContent.add(new TaoPhieuDatPhong_GUI("NV01122018001"), "Tạo Phiếu Đặt Phòng");
        pnlContent.add(new Phong_GUI(), "Quản Lý Phòng");
        pnlContent.add(new HoaDon_GUI(), "Hóa Đơn");
        pnlContent.add(new ChiPhiPhatSinh_GUI(), "Chi Phí Phát Sinh");
        pnlContent.add(new KhachHang_GUI(), "Khách Hàng");
        pnlContent.add(new NhanVien_GUI(), "Nhân Viên");
        pnlContent.add(new KhuyenMai_GUI(), "Khuyến Mãi");
        pnlContent.add(new HuongDanSuDung_GUI(), "HDSD");


        add(pnlContent, BorderLayout.CENTER);
    }

    // ================= MENU BUTTON =================
    private void addMenu(String text, String iconName) {
        JButton btn = taoNutMenu(text, iconName);

        btn.addActionListener(e -> {
            resetSubDangChon();   
            doiNutDangChon(btn);
            cardLayout.show(pnlContent, text);
        });


        pnlMenu.add(btn);
        pnlMenu.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    // ================= QL ĐẶT PHÒNG =================
    private void addMenuQLDP() {
        JButton btn = taoNutMenu("Quản Lý Đặt Phòng", "quanlydatphong");

        JPanel sub = new JPanel();
        sub.setLayout(new BoxLayout(sub, BoxLayout.Y_AXIS));
        sub.setBackground(MAU_MENU_NEN);
        sub.setVisible(false);
        sub.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 

        JButton ds = taoSubMenu("Danh Sách Phiếu Đặt Phòng");
        JButton tao = taoSubMenu("Tạo Phiếu Đặt Phòng");

        ds.addActionListener(e -> {
            doiSubDangChon(ds);
            cardLayout.show(pnlContent, "Danh Sách Phiếu Đặt Phòng");
        });

        tao.addActionListener(e -> {
            doiSubDangChon(tao);
            cardLayout.show(pnlContent, "Tạo Phiếu Đặt Phòng");
        });


        sub.add(ds);
        sub.add(Box.createRigidArea(new Dimension(0, 6)));
        sub.add(tao);

        btn.addActionListener(e -> {
            doiNutDangChon(btn);
            moQLDP = !moQLDP;
            sub.setVisible(moQLDP);
            pnlMenu.revalidate();
        });

        pnlMenu.add(btn);
        pnlMenu.add(sub);
        pnlMenu.add(Box.createRigidArea(new Dimension(0, 14)));
    }

    // ================= TẠO NÚT MENU =================
    private JButton taoNutMenu(String text, String iconName) {
        JButton btn = new JButton(text);
        btn.setIcon(loadIcon(iconName));
        btn.setMaximumSize(new Dimension(230, 52));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(18);

        btn.setBackground(MAU_NUT);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 12));
        btn.setFocusPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != btnDangChon)
                    btn.setBackground(MAU_HOVER);
            }

            public void mouseExited(MouseEvent e) {
                if (btn != btnDangChon)
                    btn.setBackground(MAU_NUT);
            }
        });

        return btn;
    }

    private JButton taoSubMenu(String text) {
        JButton btn = new JButton("• " + text);
        btn.setMaximumSize(new Dimension(210, 32));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(MAU_SUB);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 10));
        btn.setFocusPainted(false);

        // Hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != btnSubDangChon)
                    btn.setBackground(MAU_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != btnSubDangChon)
                    btn.setBackground(MAU_SUB);
            }
        });

        return btn;
    }

    // ================= CHỌN MENU =================
    private void doiNutDangChon(JButton btn) {
        if (btnDangChon != null)
            btnDangChon.setBackground(MAU_NUT);

        btnDangChon = btn;
        btn.setBackground(MAU_CHON);
    }

    // ================= LOAD ICON =================
    private ImageIcon loadIcon(String name) {
        ImageIcon icon = new ImageIcon("src/img/" + name + ".png");
        Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void doiSubDangChon(JButton btn) {
        if (btnSubDangChon != null)
            btnSubDangChon.setBackground(MAU_SUB);

        btnSubDangChon = btn;
        btn.setBackground(MAU_SUB_CHON);
    }
    private void resetSubDangChon() {
        if (btnSubDangChon != null) {
            btnSubDangChon.setBackground(MAU_SUB);
            btnSubDangChon = null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrangChinh_GUI().setVisible(true));
    }
}
