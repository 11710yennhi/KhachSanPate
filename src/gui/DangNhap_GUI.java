package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import dao.TaiKhoan_DAO;

public class DangNhap_GUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private static final Color GOLD = new Color(218, 177, 55);
    private static final Color NAVY = new Color(10, 52, 89);
	private static final Color NAVY_DARK = new Color(7, 40, 68);
	private static final Color BORDER = new Color(220, 227, 235);
	private static final Color LIGHT_BG = new Color(245, 247, 250);
	private static final String FONT_UI = "Segoe UI";
	
    JLabel lblten, lblMatKhau;
    JTextField txtTenDN;
    JPasswordField txtMatKhau;
    JButton btnDangNhap, btnXoaTrang;

    private TaiKhoan_DAO dao;

    public DangNhap_GUI() {
        dao = new TaiKhoan_DAO();
        setTitle("Đăng nhập");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        Font font = new Font("Arial", Font.BOLD, 16);

        /* ================= PANEL TRÁI (ĐĂNG NHẬP) ================= */
        JPanel pTieuDe = new JPanel(new GridBagLayout());
        JLabel lblTieuDe = new JLabel("ĐĂNG NHẬP");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 28));
        lblTieuDe.setForeground(NAVY);
        pTieuDe.add(lblTieuDe);
        pTieuDe.setPreferredSize(new Dimension(200, 100));
        pTieuDe.setOpaque(false);

        Box pBody = Box.createVerticalBox();
        Box pTkhoan, pMkhau, pNut;

        pBody.add(pTkhoan = Box.createHorizontalBox());
        pTkhoan.add(lblten = new JLabel("Tên đăng nhập:"));
        lblten.setFont(font);
        pTkhoan.add(Box.createHorizontalStrut(15));
        txtTenDN = new JTextField();
        txtTenDN.setPreferredSize(new Dimension(200, 28));
        pTkhoan.add(txtTenDN);

        pBody.add(Box.createVerticalStrut(10));

        pBody.add(pMkhau = Box.createHorizontalBox());
        pMkhau.add(lblMatKhau = new JLabel("Mật khẩu:"));
        lblMatKhau.setFont(font);
        lblMatKhau.setPreferredSize(lblten.getPreferredSize());
        pMkhau.add(Box.createHorizontalStrut(15));
        txtMatKhau = new JPasswordField();
        txtMatKhau.setPreferredSize(new Dimension(200, 28));
        pMkhau.add(txtMatKhau);

        pBody.add(Box.createVerticalStrut(30));

        pBody.add(pNut = Box.createHorizontalBox());
        btnDangNhap = new JButton("Đăng nhập");
        btnXoaTrang = new JButton("Xoá trắng");
        
        btnDangNhap.setBackground(NAVY);
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);

        btnXoaTrang.setBackground(GOLD);
        btnXoaTrang.setForeground(Color.WHITE);
        btnXoaTrang.setFocusPainted(false);

        pNut.add(btnDangNhap);
        pNut.add(Box.createHorizontalStrut(10));
        pNut.add(btnXoaTrang);

        JPanel pnlNoiDung = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlNoiDung.add(pBody);
        pnlNoiDung.setOpaque(false);

        Box pLeftContent = Box.createVerticalBox();
        pLeftContent.setPreferredSize(new Dimension(420, 260));

        pLeftContent.add(pTieuDe);
        pLeftContent.add(Box.createVerticalStrut(20));
        pLeftContent.add(pnlNoiDung);

        JPanel pnlLeft = new JPanel(new GridBagLayout());
        
        pnlLeft.setBackground(LIGHT_BG);

        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.weightx = 2.0;
        gbcLeft.weighty = 2.0;
        gbcLeft.anchor = GridBagConstraints.CENTER;
        gbcLeft.fill = GridBagConstraints.NONE;
        gbcLeft.insets = new Insets(0, 0, 0, 0);
        
        pnlLeft.add(pLeftContent, gbcLeft);
        

        /* ================= PANEL PHẢI (ẢNH FULL) ================= */
        ImageIcon icon = new ImageIcon(
        	    DangNhap_GUI.class.getResource("/img/KS_Dangnhapimg.jpg")
        	);
        	Image img = icon.getImage();

        JPanel pnlRight = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int panelW = getWidth();
                int panelH = getHeight();

                int imgW = img.getWidth(this);
                int imgH = img.getHeight(this);

                if (imgW <= 0 || imgH <= 0) return;

                double scale = Math.max(
                        (double) panelW / imgW,
                        (double) panelH / imgH
                );

                int drawW = (int) (imgW * scale);
                int drawH = (int) (imgH * scale);

                int x = (panelW - drawW) / 2;
                int y = (panelH - drawH) / 2;

                g.drawImage(img, x, y, drawW, drawH, this);
            }
        };

        /* ================= PANEL CHÍNH (1/3 - 2/3) ================= */
        JPanel pnlMain = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        // Trái: 1/3
        gbc.gridx = 0;
        gbc.weightx = 1;
        pnlMain.add(pnlLeft, gbc);

        // Phải: 2/3
        gbc.gridx = 1;
        gbc.weightx = 2;
        pnlMain.add(pnlRight, gbc);

        setContentPane(pnlMain);

        /* ================= SỰ KIỆN ================= */
        btnDangNhap.addActionListener(this);
        btnXoaTrang.addActionListener(this);

       }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == btnDangNhap)
			xuLyDangNhap();
		if (o == btnXoaTrang)
			xoaTrangThongTin();
	}

	public void xuLyDangNhap() {
		String tenDangNhap = txtTenDN.getText().trim();
		String matKhau = String.valueOf(txtMatKhau.getPassword());

		if (dao.kiemTraDangNhap(tenDangNhap, matKhau)) {
			PhienDangNhap.maNhanVienDangNhap = tenDangNhap;
	        JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
	        new TrangChinh_GUI(PhienDangNhap.maNhanVienDangNhap).setVisible(true);
	        this.setVisible(false);
	    } else {
	        JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không chính xác!");
	        txtTenDN.selectAll();
	        txtTenDN.requestFocus();
	    }
	}

	public void xoaTrangThongTin() {
		txtTenDN.setText("NV01062021030");
		txtMatKhau.setText("123456");
		txtTenDN.requestFocus();
	}


}
