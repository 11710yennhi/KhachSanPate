package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import dao.NhanVien_DAO;
import entity.NhanVien;

public class TrangChinh_GUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private CardLayout cardLayout;
    private JPanel pnlContent, pnlMenu;
    private JButton btnDangChon;
    private JButton btnSubDangChon;
    private final Map<String, JPanel> panelCache = new HashMap<>();

    // ===== CACHE ICON =====
    private static final Map<String, ImageIcon> ICON_CACHE = new HashMap<>();

    // ===== MÀU =====
    private final Color MAU_MENU_NEN = new Color(30, 61, 89);
    private final Color MAU_NUT = new Color(45, 85, 120);
    private final Color MAU_HOVER = new Color(70, 130, 180);
    private final Color MAU_CHON = new Color(90, 155, 210);
    private final Color MAU_NEN = new Color(249, 249, 249);
    private final Color MAU_SUB = new Color(55, 105, 145);
    private final Color MAU_SUB_CHON = new Color(90, 155, 210);
    private final Color MAU_DANG_XUAT = new Color(241, 196, 15);

    private boolean coQuyenQuanLy;
    private boolean moQLDP = false;
    private final String maNV;

    // ===== DAO =====
    private final NhanVien_DAO nvd = new NhanVien_DAO();

    public TrangChinh_GUI(String maNhanVien) {
        this.maNV = maNhanVien;
        this.coQuyenQuanLy = kiemTraPhanQuyen(maNV);

        setTitle("Pate Hotel - Hệ thống quản lý khách sạn");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        getContentPane().setBackground(MAU_NEN);

        taoTieuDe();
        taoMenuTrai();
        taoNoiDung();
        
        showPanel("Dashboard");
        for (Component c : pnlMenu.getComponents()) {
            if (c instanceof JButton btn) {
                if ("Dashboard".equals(btn.getText())) {
                    doiNutDangChon(btn);
                    break;
                }
            }
        }
    }

    // ===== PHÂN QUYỀN =====
    private boolean kiemTraPhanQuyen(String maNhanVien) {
        NhanVien nv = nvd.findByMa(maNhanVien);
        return nv == null || nv.isChucVu();
    }

    // ===== TIÊU ĐỀ =====
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

        Timer timer = new Timer(1000, e ->
                time.setText(java.time.LocalTime.now().withNano(0).toString())
        );
        timer.start();

        p.add(lbl, BorderLayout.CENTER);
        p.add(time, BorderLayout.EAST);
        add(p, BorderLayout.NORTH);
    }

    // ===== MENU TRÁI =====
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
        addMenu("Giới Thiệu", "gioithieu");
        addMenu("Dashboard", "Dashboard");
        addMenuQLDP();      
        addMenu("Hóa Đơn", "hoadon");
        
        if (coQuyenQuanLy) {
            addMenu("Quản Lý Phòng", "phong");
            addMenu("Khuyến Mãi", "khuyenmai");
            addMenu("Chi Phí Phát Sinh", "chiphiphatsinh");
            addMenu("Thống Kê", "thongke");
            addMenu("Nhân Viên", "nhanvien");
        }
        addMenu("Khách Hàng", "khachhang");
        addMenu("Hướng Dẫn Sử Dụng", "huongdan");
        addMenu("Tài Khoản", "taikhoan");

        pnlMenu.add(Box.createVerticalGlue());
        pnlMenu.add(taoNutDangXuat());
        add(pnlMenu, BorderLayout.WEST);
    }

    // ===== NỘI DUNG =====
    private void taoNoiDung() {
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        pnlContent.setBackground(MAU_NEN);

        add(pnlContent, BorderLayout.CENTER);
    }
    private void showPanel(String name) {
        if (!panelCache.containsKey(name)) {
            JPanel panel = null;

            switch (name) {
            case "Giới Thiệu":
                panel = new GioiThieu_GUI();
                break;
                case "Dashboard":
                    panel = new Dashboard_GUI();
                    break;
                case "Danh Sách Phiếu Đặt Phòng":
                    panel = new DanhSachPhieuDatPhong_GUI(maNV);
                    break;

                case "Tạo Phiếu Đặt Phòng":
                    panel = new TaoPhieuDatPhong_GUI(maNV);
                    break;
                case "Quản Lý Phòng":
                    panel = new Phong_GUI();
                    break;
                case "Hóa Đơn":
                    panel = new HoaDon_GUI();
                    break;
                case "Thống Kê":
                    panel = new ThongKe_GUI();
                    break;
                case "Chi Phí Phát Sinh":
                    panel = new ChiPhiPhatSinh_GUI();
                    break;
                case "Khách Hàng":
                    panel = new KhachHang_GUI();
                    break;
                case "Nhân Viên":
                    panel = new NhanVien_GUI();
                    break;
                case "Khuyến Mãi":
                    panel = new KhuyenMai_GUI();
                    break;
                case "Tài Khoản":
                    panel = new TaiKhoan_GUI(maNV);
                    break;
                case "Hướng Dẫn Sử Dụng":
                    panel = new HuongDanSuDung_GUI();
                    break;
            }

            if (panel != null) {
                panelCache.put(name, panel);
                pnlContent.add(panel, name);
            }
        }

        cardLayout.show(pnlContent, name);
    }


    // ===== MENU BUTTON =====
    private void addMenu(String text, String iconName) {
        JButton btn = taoNutMenu(text, iconName);
        btn.addActionListener(e -> {
            resetSubDangChon();
            doiNutDangChon(btn);
            showPanel(text);
        });
        pnlMenu.add(btn);
        pnlMenu.add(Box.createRigidArea(new Dimension(0, 4)));
    }

    private void addMenuQLDP() {
        JButton btn = taoNutMenu("Quản Lý Đặt Phòng", "quanlydatphong");

        JPanel sub = new JPanel();
        sub.setLayout(new BoxLayout(sub, BoxLayout.Y_AXIS));
        sub.setBackground(MAU_MENU_NEN);
        sub.setVisible(false);

        JButton ds = taoSubMenu("Danh Sách Phiếu Đặt Phòng");
        JButton tao = taoSubMenu("Tạo Phiếu Đặt Phòng");

        ds.addActionListener(e -> {
            doiSubDangChon(ds);
            showPanel("Danh Sách Phiếu Đặt Phòng");
        });

        tao.addActionListener(e -> {
            doiSubDangChon(tao);
            showPanel("Tạo Phiếu Đặt Phòng");
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

    // ===== NÚT =====
    private JButton taoNutMenu(String text, String iconName) {
        JButton btn = new JButton(text);
        btn.setIcon(loadIcon(iconName));
        btn.setMaximumSize(new Dimension(230, 38));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(18);
        btn.setBackground(MAU_NUT);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 10));
        btn.setFocusPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != btnDangChon) btn.setBackground(MAU_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                if (btn != btnDangChon) btn.setBackground(MAU_NUT);
            }
        });
        return btn;
    }

    private JButton taoSubMenu(String text) {
        JButton btn = new JButton("• " + text);
        btn.setMaximumSize(new Dimension(230, 28));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(MAU_SUB);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(3, 14, 3, 10));
        btn.setFocusPainted(false);

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


    // ===== CHỌN =====
    private void doiNutDangChon(JButton btn) {
        if (btnDangChon != null) btnDangChon.setBackground(MAU_NUT);
        btnDangChon = btn;
        btn.setBackground(MAU_CHON);
    }

    private void doiSubDangChon(JButton btn) {
        if (btnSubDangChon != null) btnSubDangChon.setBackground(MAU_SUB);
        btnSubDangChon = btn;
        btn.setBackground(MAU_SUB_CHON);
    }

    private void resetSubDangChon() {
        if (btnSubDangChon != null) {
            btnSubDangChon.setBackground(MAU_SUB);
            btnSubDangChon = null;
        }
    }

    // ===== ICON CACHE =====
    private ImageIcon loadIcon(String name) {
        return ICON_CACHE.computeIfAbsent(name, n -> {
            ImageIcon icon = new ImageIcon("src/img/" + n + ".png");
            Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        });
    }

    private JButton taoNutDangXuat() {
        JButton btn = new JButton("Đăng Xuất");
        btn.setMaximumSize(new Dimension(230, 36));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(MAU_DANG_XUAT);
        btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        btn.setFocusPainted(false);

        btn.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn đăng xuất?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dispose();
                new DangNhap_GUI().setVisible(true);
            }
        });
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrangChinh_GUI(PhienDangNhap.maNhanVienDangNhap).setVisible(true));

    }
}
