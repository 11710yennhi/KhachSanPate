package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connectDB.ConnectDB;
import dao.KhachHang_DAO;
import entity.KhachHang;

public class KhachHang_GUI extends JPanel implements ActionListener, MouseListener {
	
	private static final Color NAVY_DARK = new Color(7, 40, 68);
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

		add(buildHeader(), BorderLayout.NORTH);

		JPanel body = new JPanel(new BorderLayout(12, 12));
		body.setBackground(Color.WHITE);

		body.add(buildBody(), BorderLayout.NORTH);
		body.add(buildTablePanel(), BorderLayout.CENTER);

		add(body, BorderLayout.CENTER);

		try {
			ConnectDB.getInstance().connect();
			loadKhachHangToTable();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL: " + e.getMessage());
		}
	}

	// Build UI
	private JPanel buildHeader() {
		JPanel header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
		header.setBackground(NAVY_DARK);
		header.setBorder(new EmptyBorder(14, 18, 14, 18));

		JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblSub = new JLabel("Pate Hotel • Quản lý thông tin khách lưu trú");
		lblSub.setForeground(Color.WHITE);
		lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

		header.add(lblTitle);
		header.add(Box.createVerticalStrut(4));
		header.add(lblSub);

		return header;
	}

	private JPanel buildBody() {
		JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
		infoPanel.setBorder(
				new CompoundBorder(new LineBorder(new Color(220, 220, 220), 1, true), new EmptyBorder(14, 14, 14, 14)));
		infoPanel.setBackground(Color.WHITE);

		JLabel lbTitle = new JLabel("Thông tin nhân viên");
		lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lbTitle.setForeground(NAVY_DARK);
		infoPanel.add(lbTitle, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(Color.WHITE);
		infoPanel.add(formPanel, BorderLayout.CENTER);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Hàng 1
		// Mã KhachHang
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(new JLabel("Mã khách hàng:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		txtMaKH = new JTextField();
		txtMaKH.setEditable(false);
		styleField(txtMaKH);
		formPanel.add(txtMaKH, gbc);

		// Họ tên
		gbc.gridx = 2;
		gbc.weightx = 0;
		formPanel.add(new JLabel("Họ tên:"), gbc);

		gbc.gridx = 3;
		gbc.weightx = 1;
		txtHoTen = new JTextField();
		styleField(txtHoTen);
		formPanel.add(txtHoTen, gbc);

		// Hàng 2
		// Số điện thoại
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		formPanel.add(new JLabel("Số điện thoại:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		JPanel sdtPanel = new JPanel(new BorderLayout(5, 0));
		sdtPanel.setBackground(Color.WHITE);

		txtSoDT = new JTextField();
		styleField(txtSoDT);

		btnTim = new JButton("🔍");
		btnTim.setFocusPainted(false);
		btnTim.setBackground(new Color(230, 240, 255));
		btnTim.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
		btnTim.addActionListener(this);

		sdtPanel.add(txtSoDT, BorderLayout.CENTER);
		sdtPanel.add(btnTim, BorderLayout.EAST);

		formPanel.add(sdtPanel, gbc);

		// Quốc tịch bằng checkbox
		gbc.gridx = 2;
		gbc.weightx = 0;
		formPanel.add(new JLabel("Là người Việt Nam:"), gbc);
		gbc.gridx = 3;
		chkLaNguoiVN = new JCheckBox();
		chkLaNguoiVN.setBackground(Color.WHITE);
		chkLaNguoiVN.setSelected(true);
		formPanel.add(chkLaNguoiVN, gbc);

		// Cột nút bên phải
		JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
//		buttonCol.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		buttonRow.setBackground(Color.WHITE);

		btnXoaRong = new JButton("Xóa rỗng");
		styleButton(btnXoaRong);
		btnXoaRong.setBackground(new Color(230, 240, 255));
		btnXoaRong.setForeground(NAVY_DARK);
		btnXoaRong.addActionListener(this);

		btnLamMoi = new JButton("Làm mới");
		styleButton(btnLamMoi);
		btnLamMoi.setBackground(new Color(218, 177, 55));
		btnLamMoi.setForeground(NAVY_DARK);
		btnLamMoi.addActionListener(this);

		btnSua = new JButton("Lưu");
		styleButton(btnSua);
		btnSua.setBackground(NAVY_DARK);
		btnSua.setForeground(Color.WHITE);
		btnSua.addActionListener(this);

		buttonRow.add(btnXoaRong);
		buttonRow.add(btnLamMoi);
		buttonRow.add(btnSua);

		infoPanel.add(buttonRow, BorderLayout.SOUTH);

		return infoPanel;
	}

	private JPanel buildTablePanel() {
		JPanel tableCard = new JPanel(new BorderLayout());
		tableCard.setBackground(Color.WHITE);
		tableCard.setBorder(
				new CompoundBorder(new LineBorder(new Color(220, 220, 220), 1, true), new EmptyBorder(12, 12, 12, 12)));

		JLabel lblTable = new JLabel("Danh sách khách hàng");
		lblTable.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTable.setForeground(NAVY_DARK);

		String[] cols = { "STT", "Mã KH", "Họ Tên", "Số Điện Thoại", "Là Người Việt Nam" };
		modelKH = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		table = new JTable(modelKH);
		styleTable(table);
		table.addMouseListener(this);

		tableCard.add(lblTable, BorderLayout.NORTH);
		tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

		return tableCard;
	}

	// =============Style
	private void styleField(JTextField txt) {
		txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txt.setPreferredSize(new Dimension(0, 32));
	}

	private void styleButton(JButton btn) {
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.setPreferredSize(new Dimension(120, 35));
		btn.setMaximumSize(new Dimension(120, 35));
		btn.setFocusPainted(false);
	}

	private void styleTable(JTable t) {
		t.setRowHeight(34);
		t.setGridColor(new Color(230, 235, 240));
		t.setShowHorizontalLines(true);
		t.setShowVerticalLines(false);
		t.setSelectionBackground(new Color(225, 238, 252));
		t.setSelectionForeground(NAVY_DARK);

		JTableHeader header = t.getTableHeader();
		header.setFont(new Font("Segoe UI", Font.BOLD, 13));
		header.setBackground(NAVY_DARK);
		header.setForeground(Color.WHITE);
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 38));

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int col) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
				if (!isSelected)
					c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 253));
				setBorder(new EmptyBorder(0, 10, 0, 10));
				return c;
			}
		};

		for (int i = 0; i < t.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}
	}

	// ================Events

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
					kh.LaNguoiVietNam() ? "✓" : "✗" });
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
				chkLaNguoiVN.setSelected(kh.LaNguoiVietNam());
				JOptionPane.showMessageDialog(this, "Đã tìm thấy khách hàng!");
			} else {
				JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng với SĐT này!");
			}
		} else if (o.equals(btnSua)) {
			if (txtMaKH.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Chọn thông tin khách hàng cần sửa!");
				return;
			}
			KhachHang kh = getFormData();
			if (kh != null) {
				if (khDAO.isSoDienThoaiTonTaiKhacMa(kh.getSoDienThoai(), kh.getMaKhachHang())) {
					JOptionPane.showMessageDialog(this, "Số điện thoại đã được sử dụng bởi khách khác!", "Cảnh báo",
							JOptionPane.WARNING_MESSAGE);
				} else if (khDAO.capNhatKhachHang(kh)) {
					JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
					loadKhachHangToTable();
				} else {
					JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
				}
			}
		} else if (o.equals(btnXoaRong)) {
			xoaRongForm();
		} else if (o.equals(btnLamMoi)) {
			loadKhachHangToTable();
			JOptionPane.showMessageDialog(this, "Thành công!");
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
