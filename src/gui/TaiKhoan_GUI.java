package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoan_GUI extends JPanel implements ActionListener{
	// ===== THEME =====
    private final Color NAVY = new Color(10, 52, 89);
    private final Color NAVY_DARK = new Color(7, 40, 68);
    private final Color GOLD = new Color(218, 177, 55);
    private final Color BG = new Color(245, 247, 250);
    private final Color BORDER = new Color(220, 227, 235);

    private final Font FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    
	private TaiKhoan_DAO dao;
	private JTextField txtTaiKhoan, txtMatKhau,txtMatKhauMoi, txtLapLai;
    private JLabel lblTaiKhoan, lblMatKhau,lblMatKhauMoi, lblLapLai;
    private JButton btnXoaRong, btnDoiMatKhau;
    private JTable table;
    private DefaultTableModel model;
    
    private String maNhanVien;
    private JTextField txtMaNV, txtHoTen, txtGioiTinh, txtNgaySinh,
    txtSDT, txtEmail, txtChucVu, txtTrangThai;

	public TaiKhoan_GUI(String maNV) {
		this.maNhanVien = maNV;
		dao = new TaiKhoan_DAO();
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 120));
		setBackground(Color.WHITE);
		
        JPanel mainPanel = new JPanel(); 
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        
        Dimension labelSize = new Dimension(120, 25);
        Dimension textSize = new Dimension(200, 25);
        Dimension buttonSize = new Dimension(80, 25);

        //Dòng 1
        Box boxTaiKhoan = Box.createHorizontalBox();
        lblTaiKhoan = new JLabel("Tài khoản:");
        lblTaiKhoan.setPreferredSize(labelSize);
        txtTaiKhoan = new JTextField();
        txtTaiKhoan.setPreferredSize(textSize);
        txtTaiKhoan.setEditable(false);
        
        boxTaiKhoan.add(lblTaiKhoan);
        boxTaiKhoan.add(Box.createHorizontalStrut(10));
        boxTaiKhoan.add(txtTaiKhoan);
        
        //Dòng 2
        Box boxMatKhau = Box.createHorizontalBox();
        lblMatKhau = new JLabel("Mật khẩu cũ:");
        lblMatKhau.setPreferredSize(labelSize);
        txtMatKhau = new JTextField();
        txtMatKhau.setPreferredSize(textSize);
        txtMatKhau.setEditable(false);
        txtMatKhau.setBackground(new Color(240,240,240));

        boxMatKhau.add(lblMatKhau);
        boxMatKhau.add(Box.createHorizontalStrut(10));
        boxMatKhau.add(txtMatKhau);
        
        //Dòng 3
        Box boxMatKhauMoi = Box.createHorizontalBox();
        lblMatKhauMoi = new JLabel("Mật khẩu mới:");
        lblMatKhauMoi.setPreferredSize(labelSize);
        txtMatKhauMoi = new JTextField();
        txtMatKhauMoi.setPreferredSize(textSize);
        
        boxMatKhauMoi.add(lblMatKhauMoi);
        boxMatKhauMoi.add(Box.createHorizontalStrut(10));
        boxMatKhauMoi.add(txtMatKhauMoi);
        
        //Dòng 4
        Box boxLapLai = Box.createHorizontalBox();
        lblLapLai = new JLabel("Lặp lại mật khẩu:");
        lblLapLai.setPreferredSize(labelSize);
        txtLapLai = new JTextField();
        txtLapLai.setPreferredSize(textSize);
        
        boxLapLai.add(lblLapLai);
        boxLapLai.add(Box.createHorizontalStrut(10));
        boxLapLai.add(txtLapLai);
        
        
        //Dòng 4
        Box boxbutton = Box.createHorizontalBox();
        btnDoiMatKhau = createButton("Đổi mật khẩu", NAVY, Color.WHITE);
        btnXoaRong = createButton("Xóa rỗng", NAVY, Color.WHITE);
        
        btnXoaRong.setPreferredSize(buttonSize);
        btnDoiMatKhau.setPreferredSize(buttonSize);
        
        boxbutton.add(btnXoaRong);
        boxbutton.add(Box.createHorizontalStrut(10));
        boxbutton.add(btnDoiMatKhau);
        
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(boxTaiKhoan);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(boxMatKhau);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(boxMatKhauMoi);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(boxLapLai);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(boxbutton);
        
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setBackground(Color.WHITE);
        wrapper.add(mainPanel);
        
        JPanel pnlThongTinNV = new JPanel();
        pnlThongTinNV.setLayout(new BoxLayout(pnlThongTinNV, BoxLayout.Y_AXIS));
        pnlThongTinNV.setBackground(Color.WHITE);
        pnlThongTinNV.setBorder(
            BorderFactory.createTitledBorder("Thông tin nhân viên")
        );

        Dimension lbSize = new Dimension(120, 25);
        Dimension tfSize = new Dimension(220, 25);

        // hàm tạo dòng
        pnlThongTinNV.add(createInfoRow("Mã NV:", txtMaNV = createReadOnlyField(tfSize), lbSize));
        pnlThongTinNV.add(createInfoRow("Họ tên:", txtHoTen = createReadOnlyField(tfSize), lbSize));
        pnlThongTinNV.add(createInfoRow("Giới tính:", txtGioiTinh = createReadOnlyField(tfSize), lbSize));
        pnlThongTinNV.add(createInfoRow("Ngày sinh:", txtNgaySinh = createReadOnlyField(tfSize), lbSize));
        pnlThongTinNV.add(createInfoRow("SĐT:", txtSDT = createReadOnlyField(tfSize), lbSize));
        pnlThongTinNV.add(createInfoRow("Email:", txtEmail = createReadOnlyField(tfSize), lbSize));
        pnlThongTinNV.add(createInfoRow("Chức vụ:", txtChucVu = createReadOnlyField(tfSize), lbSize));
        pnlThongTinNV.add(createInfoRow("Trạng thái:", txtTrangThai = createReadOnlyField(tfSize), lbSize));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setPreferredSize(new Dimension(900, 520));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 30, 10, 30);

        // Dòng 1: Thông tin nhân viên
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(pnlThongTinNV, gbc);

        // Dòng 2: Đổi mật khẩu
        gbc.gridy = 1;
        centerPanel.add(wrapper, gbc);

        add(centerPanel, BorderLayout.CENTER);

        
        btnXoaRong.addActionListener(this);
        btnDoiMatKhau.addActionListener(this);
        loadTaiKhoan();
        loadThongTinNhanVien();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == btnXoaRong) {
			txtMatKhauMoi.setText("");
			txtLapLai.setText("");
		}
		if (obj == btnDoiMatKhau) {

		    String matKhauCu = txtMatKhau.getText().trim();
		    String matKhauMoi = txtMatKhauMoi.getText().trim();
		    String lapLai = txtLapLai.getText().trim();

		    if (matKhauMoi.isEmpty() || lapLai.isEmpty()) {
		        JOptionPane.showMessageDialog(this,
		                "Vui lòng nhập đầy đủ mật khẩu mới!",
		                "Thông báo",
		                JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    if (!matKhauMoi.equals(lapLai)) {
		        JOptionPane.showMessageDialog(this,
		                "Mật khẩu nhập lại không khớp!",
		                "Lỗi",
		                JOptionPane.ERROR_MESSAGE);
		        return;
		    }

		    if (matKhauMoi.equals(matKhauCu)) {
		        JOptionPane.showMessageDialog(this,
		                "Mật khẩu mới phải khác mật khẩu cũ!",
		                "Lỗi",
		                JOptionPane.ERROR_MESSAGE);
		        return;
		    }

		    boolean ok = dao.doiMatKhau(maNhanVien, matKhauMoi);

		    if (ok) {
		        JOptionPane.showMessageDialog(this,
		                "Đổi mật khẩu thành công!");
		        txtMatKhau.setText(matKhauMoi);
		        txtMatKhauMoi.setText("");
		        txtLapLai.setText("");
		    } else {
		        JOptionPane.showMessageDialog(this,
		                "Đổi mật khẩu thất bại!",
		                "Lỗi",
		                JOptionPane.ERROR_MESSAGE);
		    }
		}

	}
	private JButton createButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(FONT_BOLD);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10,18,10,18));
        return b;
    }
	private void loadTaiKhoan() {
	    TaiKhoan tk = dao.getTaiKhoanTheoMaNV(maNhanVien);

	    if (tk == null) {
	        JOptionPane.showMessageDialog(this,
	                "Không tìm thấy tài khoản của nhân viên!",
	                "Lỗi",
	                JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    txtTaiKhoan.setText(tk.getMaNhanVien());
	    txtMatKhau.setText(tk.getMatKhau());

	    // clear ô nhập mới
	    txtMatKhauMoi.setText("");
	    txtLapLai.setText("");
	}

	private void loadThongTinNhanVien() {
	    NhanVien nv = dao.getNhanVienTheoMa(maNhanVien);

	    if (nv == null) {
	        JOptionPane.showMessageDialog(this,
	                "Không tìm thấy thông tin nhân viên!",
	                "Lỗi",
	                JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    txtMaNV.setText(nv.getMaNhanVien());
	    txtHoTen.setText(nv.getHoten());
	    txtGioiTinh.setText(nv.isGioiTinh() ? "Nam" : "Nữ");
	    txtNgaySinh.setText(
	        nv.getNgaySinh() != null ? nv.getNgaySinh().toString() : ""
	    );
	    txtSDT.setText(nv.getSoDienThoai());
	    txtEmail.setText(nv.getEmail());
	    txtChucVu.setText(nv.isChucVu() ? "Quản lý" : "Nhân viên");
	    txtTrangThai.setText(nv.isTrangThai() ? "Đang làm" : "Nghỉ việc");
	}


	private JPanel createInfoRow(String label, JTextField field, Dimension lbSize) {
	    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    p.setBackground(Color.WHITE);
	    JLabel lb = new JLabel(label);
	    lb.setPreferredSize(lbSize);
	    p.add(lb);
	    p.add(field);
	    return p;
	}

	private JTextField createReadOnlyField(Dimension size) {
	    JTextField tf = new JTextField();
	    tf.setPreferredSize(size);
	    tf.setEditable(false);
	    tf.setBackground(Color.WHITE);
	    return tf;
	}

	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test TaiKhoan_GUI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setContentPane(new TaiKhoan_GUI(PhienDangNhap.maNhanVienDangNhap));
            frame.pack();              // tự căn theo layout
            frame.setLocationRelativeTo(null); // giữa màn hình
            frame.setVisible(true);
        });
    }
}
