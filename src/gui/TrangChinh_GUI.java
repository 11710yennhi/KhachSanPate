package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TrangChinh_GUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private CardLayout cardLayout;
    private JPanel pnlContent;
    private JButton btnDangXuat;
    private JPanel pTrai;

    // Biến lưu trạng thái mở rộng menu
    private boolean quanLyDatPhongMo = false;

    public TrangChinh_GUI() {
        setTitle("Pate Hotel - Hệ thống quản lý khách sạn");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        Color mauXanhDam = new Color(30, 61, 89);
        Color mauVangDong = new Color(212, 175, 55);
        Color mauNen = new Color(249, 249, 249);
        getContentPane().setBackground(mauNen);

        // ======= TIÊU ĐỀ =======
        JPanel pTieuDe = new JPanel(new BorderLayout());
        pTieuDe.setPreferredSize(new Dimension(getWidth(), 60));
        pTieuDe.setBackground(mauXanhDam);

        JLabel lblChaoMung = new JLabel("🏨 Pate Hotel - Chào mừng bạn đến với hệ thống quản lý", SwingConstants.CENTER);
        lblChaoMung.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        lblChaoMung.setForeground(Color.WHITE);
        pTieuDe.add(lblChaoMung, BorderLayout.CENTER);

        JLabel lblThoiGian = new JLabel();
        lblThoiGian.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblThoiGian.setForeground(Color.WHITE);
        lblThoiGian.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));
        pTieuDe.add(lblThoiGian, BorderLayout.EAST);

        Timer timer = new Timer(1000, e -> lblThoiGian.setText(java.time.LocalTime.now().withNano(0).toString()));
        timer.start();

        // ======= MENU TRÁI =======
        pTrai = new JPanel();
        pTrai.setBackground(mauXanhDam);
        pTrai.setPreferredSize(new Dimension(230, getHeight()));
        pTrai.setLayout(new BoxLayout(pTrai, BoxLayout.Y_AXIS));
        pTrai.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Logo
        ImageIcon logoIcon = new ImageIcon("src/image/logoPate.png");
        JLabel lblLogo = new JLabel();
        Image scaled = logoIcon.getImage().getScaledInstance(180, 130, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(scaled));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        pTrai.add(lblLogo);

        // ======= NÚT MENU =======
        addMenuButton("Trang Chủ", mauXanhDam, mauVangDong);
        addMenuQuanLyDatPhong(mauXanhDam, mauVangDong);
        addMenuButton("Quản Lý Phòng", mauXanhDam, mauVangDong);
        addMenuButton("Khuyến Mãi", mauXanhDam, mauVangDong);
        addMenuButton("Chi Phí Phát Sinh", mauXanhDam, mauVangDong);
        addMenuButton("Hóa Đơn", mauXanhDam, mauVangDong);
        addMenuButton("Thống Kê", mauXanhDam, mauVangDong);
        addMenuButton("Khách Hàng", mauXanhDam, mauVangDong);
        addMenuButton("Nhân Viên", mauXanhDam, mauVangDong);
        addMenuButton("Tài Khoản", mauXanhDam, mauVangDong);

        // ======= NÚT ĐĂNG XUẤT =======
        btnDangXuat = new JButton("Đăng xuất");
        btnDangXuat.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDangXuat.setMaximumSize(new Dimension(200, 40));
        btnDangXuat.setForeground(mauXanhDam);
        btnDangXuat.setBackground(mauVangDong);
        btnDangXuat.setFocusPainted(false);
        btnDangXuat.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        pTrai.add(Box.createVerticalGlue());
        pTrai.add(btnDangXuat);

        // ======= PANEL CHÍNH =======
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        pnlContent.setBackground(mauNen);

        //pnlContent.add(new TrangChu_GUI(), "Trang Chủ");
        pnlContent.add(new DanhSachPhieuDatPhong_GUI(), "Danh Sách Phiếu Đặt Phòng");

        pnlContent.add(new TaoPhieuDatPhong_GUI("NV01122018001"), "Tạo Phiếu Đặt Phòng");
        pnlContent.add(new TaoPhieuDatPhong_GUI("NV01122018001"), "Tạo Phiếu Đặt Phòng");
        pnlContent.add(new Phong_GUI(), "Phòng");
        pnlContent.add(new HoaDon_GUI(), "Hóa Đơn");
       pnlContent.add(new NhanVien_GUI(), "Nhân Viên");
       pnlContent.add(new ChiPhiPhatSinh_GUI(), "Chi Phí Phát Sinh");
       pnlContent.add(new Phong_GUI(), "Quản Lý Phòng");
      pnlContent.add(new KhachHang_GUI(), "Khách Hàng");
      pnlContent.add(new KhuyenMai_GUI(), "Khuyến Mãi");

  //      pnlContent.add(new NhanVien_testtt(), "Nhân Viên");

       

        // ======= ADD TO FRAME =======
        add(pTieuDe, BorderLayout.NORTH);
        add(pTrai, BorderLayout.WEST);
        add(pnlContent, BorderLayout.CENTER);

        btnDangXuat.addActionListener(this);
    }

    // ====================== TẠO NÚT MENU CHÍNH ======================
    private void addMenuButton(String label, Color mauNen, Color mauHover) {
        JButton btn = new JButton(label);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setFocusPainted(false);
        btn.setBackground(mauNen);
        btn.setForeground(Color.WHITE);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 40)));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(mauHover);
                btn.setForeground(mauNen);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(mauNen);
                btn.setForeground(Color.WHITE);
            }
        });

        btn.addActionListener(e -> cardLayout.show(pnlContent, label));

        pTrai.add(btn);
        pTrai.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    // ====================== TẠO MENU "QUẢN LÝ ĐẶT PHÒNG" CÓ NHÁNH ======================
    private void addMenuQuanLyDatPhong(Color mauNen, Color mauHover) {
        JButton btnQLDP = new JButton("Quản Lý Đặt Phòng ▸");
        btnQLDP.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnQLDP.setMaximumSize(new Dimension(200, 40));
        btnQLDP.setFocusPainted(false);
        btnQLDP.setBackground(mauNen);
        btnQLDP.setForeground(Color.WHITE);
        btnQLDP.setHorizontalAlignment(SwingConstants.LEFT);
        btnQLDP.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnQLDP.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 40)));

        // Panel chứa 2 nút con
        JPanel pnlSubMenu = new JPanel();
        pnlSubMenu.setLayout(new BoxLayout(pnlSubMenu, BoxLayout.Y_AXIS));
        pnlSubMenu.setBackground(mauNen);
        pnlSubMenu.setVisible(false);

        JButton btnDS = new JButton("   ⤷ Danh Sách Phiếu Đặt Phòng");
        JButton btnTao = new JButton("   ⤷ Tạo Phiếu Đặt Phòng");

        for (JButton b : new JButton[]{btnDS, btnTao}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(200, 35));
            b.setFocusPainted(false);
            b.setBackground(new Color(37, 73, 107));
            b.setForeground(Color.WHITE);
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    b.setBackground(mauHover);
                    b.setForeground(mauNen);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    b.setBackground(new Color(37, 73, 107));
                    b.setForeground(Color.WHITE);
                }
            });

            pnlSubMenu.add(b);
            pnlSubMenu.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        // Sự kiện toggle
        btnQLDP.addActionListener(e -> {
            quanLyDatPhongMo = !quanLyDatPhongMo;
            pnlSubMenu.setVisible(quanLyDatPhongMo);
            btnQLDP.setText(quanLyDatPhongMo ? "Quản Lý Đặt Phòng ▼" : "Quản Lý Đặt Phòng ▸");
            pTrai.revalidate();
            pTrai.repaint();
        });

        // Chuyển trang
        btnDS.addActionListener(e -> cardLayout.show(pnlContent, "Danh Sách Phiếu Đặt Phòng"));
        btnTao.addActionListener(e -> cardLayout.show(pnlContent, "Tạo Phiếu Đặt Phòng"));

        pTrai.add(btnQLDP);
        pTrai.add(pnlSubMenu);
        pTrai.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDangXuat) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát chương trình?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrangChinh_GUI().setVisible(true));
        
    }
}
