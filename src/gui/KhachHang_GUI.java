package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import connectDB.ConnectDB;
import dao.KhachHang_DAO;
import entity.KhachHang;

public class KhachHang_GUI extends JPanel implements ActionListener, MouseListener {

	private JTextField txtMaKH, txtHoTen, txtSoDT;
	private JCheckBox chkLaNguoiVN;
	private JButton btnSua, btnTim;
	private JTable table;
	private DefaultTableModel modelKH;

	private KhachHang_DAO khDAO = new KhachHang_DAO();
	private JButton btnXoaRong;
	private JButton btnLamMoi;

	public KhachHang_GUI() {
		setLayout(new BorderLayout(10, 10));
		setBackground(Color.WHITE);

		JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 26));
		lblTitle.setForeground(new Color(0, 77, 153));
		add(lblTitle, BorderLayout.NORTH);

		initForm();

		try {
			ConnectDB.getInstance().connect();
			loadKhachHangToTable();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL: " + e.getMessage());
		}
	}

	private void initForm() {
		JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
		infoPanel.setBorder(new TitledBorder("Thông tin khách hàng"));
		infoPanel.setBackground(Color.WHITE);
		add(infoPanel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
		formPanel.setBackground(Color.WHITE);

		// Hàng 1
		formPanel.add(new JLabel("Mã khách hàng:"));
		txtMaKH = new JTextField();
		txtMaKH.setEditable(false); // readonly
//        setTextFieldHeight(txtMaKH);
		formPanel.add(txtMaKH);

		formPanel.add(new JLabel("Họ tên:"));
		txtHoTen = new JTextField();
//        setTextFieldHeight(txtHoTen);
		formPanel.add(txtHoTen);

		// Hàng 2
		formPanel.add(new JLabel("Số điện thoại:"));
		JPanel sdtPanel = new JPanel(new BorderLayout(5, 0));
		sdtPanel.setBackground(Color.WHITE);
		txtSoDT = new JTextField(10);
//        setTextFieldHeight(txtSoDT);
		btnTim = new JButton("🔍");
		btnTim.setBackground(new Color(200, 220, 250));
		btnTim.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
		btnTim.addActionListener(this);
		sdtPanel.add(txtSoDT, BorderLayout.CENTER);
		sdtPanel.add(btnTim, BorderLayout.EAST);
		formPanel.add(sdtPanel);

		// Quốc tịch bằng checkbox
		formPanel.add(new JLabel("Là người Việt Nam:"));
		chkLaNguoiVN = new JCheckBox();
		chkLaNguoiVN.setBackground(Color.WHITE);
		chkLaNguoiVN.setSelected(true);
		formPanel.add(chkLaNguoiVN);

		infoPanel.add(formPanel, BorderLayout.CENTER);

		// Cột nút bên phải
		JPanel buttonCol = new JPanel();
		buttonCol.setLayout(new BoxLayout(buttonCol, BoxLayout.Y_AXIS));
		buttonCol.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		buttonCol.setBackground(Color.WHITE);

		btnSua = new JButton("Lưu");
		Dimension btnSize = new Dimension(120, 35);
		btnSua.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSua.setBackground(new Color(220, 230, 250));
		btnSua.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSua.setPreferredSize(btnSize);
		btnSua.setMaximumSize(btnSize);
		btnSua.addActionListener(this);
//        buttonCol.add(btnSua);

		btnXoaRong = new JButton("Xóa rỗng");
		btnXoaRong.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnXoaRong.setBackground(new Color(220, 230, 250));
		btnXoaRong.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnXoaRong.setPreferredSize(btnSize);
		btnXoaRong.setMaximumSize(btnSize);
		btnXoaRong.addActionListener(this);
//        buttonCol.add(btnXoaRong);
		
		btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnLamMoi.setBackground(new Color(220, 230, 250));
		btnLamMoi.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLamMoi.setPreferredSize(btnSize);
		btnLamMoi.setMaximumSize(btnSize);
		btnLamMoi.addActionListener(this);

		// buttonCol.add(Box.createVerticalGlue()); // đẩy nút xuống giữa
		buttonCol.add(btnSua);
		buttonCol.add(Box.createVerticalStrut(5));
		buttonCol.add(btnXoaRong);
		buttonCol.add(Box.createVerticalStrut(5));
		buttonCol.add(btnLamMoi);
		// buttonCol.add(Box.createVerticalGlue()); // đẩy nút lên trên

		infoPanel.add(buttonCol, BorderLayout.EAST);

		// Bảng
		String[] cols = { "STT", "Mã KH", "Họ Tên", "Số Điện Thoại", "Là Người Việt Nam" };
		modelKH = new DefaultTableModel(cols, 0);
		table = new JTable(modelKH);
		table.addMouseListener(this);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

//    // Giảm chiều cao JTextField để đều với JLabel
//    private void setTextFieldHeight(JTextField txt) {
//        Dimension size = txt.getPreferredSize();
//        size.height = 15;
//        txt.setPreferredSize(size);
//    }

	private void xoaRongForm() {
		txtHoTen.setText("");
		txtMaKH.setText("");
		txtSoDT.setText("");
		chkLaNguoiVN.setSelected(false);
		txtMaKH.requestFocus();
	}

	private void loadKhachHangToTable() {
		modelKH.setRowCount(0);
		ArrayList<KhachHang> list = (ArrayList<KhachHang>) khDAO.getAllKhachHang();
		int stt = 1;
		for (KhachHang kh : list) {
			modelKH.addRow(new Object[] { stt++, kh.getMaKhachHang(), kh.getHoTen(), kh.getSoDienThoai(),
					kh.isNguoiVietNam() ? "✓" : "✗" });
		}
	}

	private KhachHang getFormData() {
		try {
			String ma = txtMaKH.getText().trim();
			String ten = txtHoTen.getText().trim();
			String sdt = txtSoDT.getText().trim();
			boolean laVN = chkLaNguoiVN.isSelected();

			if (ten.isEmpty() || sdt.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
				return null;
			} else if (!sdt.matches("0\\d{9}")) {
				JOptionPane.showMessageDialog(this, "Số điện thoại bắt đầu bằng số 0 và có 10 kí số!");
				return null;
			}

			return new KhachHang(ma, ten, sdt, laVN);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi đọc dữ liệu từ form!");
			return null;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnTim)) {
			String sdt = txtSoDT.getText().trim();
			if (sdt.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại cần tìm!");
				return;
			}

			KhachHang kh = khDAO.getKhachHangTheoSDT(sdt);

			if (kh != null) {
				txtMaKH.setText(kh.getMaKhachHang());
				txtHoTen.setText(kh.getHoTen());
				chkLaNguoiVN.setSelected(kh.isNguoiVietNam());
				JOptionPane.showMessageDialog(this, "Đã tìm thấy khách hàng!");
			} else {
				JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng với SĐT này!");
			}
		} else if (o.equals(btnSua)) {
			KhachHang kh = getFormData();
			if (kh != null) {
				if (khDAO.capNhatKhachHang(kh)) {
					JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
					loadKhachHangToTable();
				} else {
					JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
				}
			}
		} else if (o.equals(btnXoaRong)) {
			xoaRongForm();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		if (row >= 0) {
			txtMaKH.setText(modelKH.getValueAt(row, 1).toString());
			txtHoTen.setText(modelKH.getValueAt(row, 2).toString());
			txtSoDT.setText(modelKH.getValueAt(row, 3).toString());
			String laVN = modelKH.getValueAt(row, 4).toString();
			chkLaNguoiVN.setSelected(laVN.equals("✓"));
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	// --------------------CHẠY THỬ----------------------
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame f = new JFrame("Quản Lý Khách Hàng");
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setExtendedState(JFrame.MAXIMIZED_BOTH);
			f.add(new KhachHang_GUI());
			f.setVisible(true);
		});
	}
}
