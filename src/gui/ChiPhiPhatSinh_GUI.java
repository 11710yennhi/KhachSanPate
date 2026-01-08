package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connectDB.ConnectDB;
import dao.ChiPhiPhatSinh_DAO;
import entity.ChiPhiPhatSinh;

public class ChiPhiPhatSinh_GUI extends JPanel implements ActionListener, MouseListener {

	private static final Color NAVY_DARK = new Color(7, 40, 68);

	private JTextField txtMaCP, txtGia, txtTenCP;
	private JTable table;
	private JComboBox<String> cboLoai;
	private DefaultTableModel model;
	private JButton btnThem, btnSua, btnTim, btnXoaRong;
	private ChiPhiPhatSinh_DAO dao = new ChiPhiPhatSinh_DAO();

	public ChiPhiPhatSinh_GUI() {
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
			loadDataToTable();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL: " + e.getMessage());
		}

	}

	// ========Build UI

	private JPanel buildHeader() {
		JPanel header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
		header.setBackground(NAVY_DARK);
		header.setBorder(new EmptyBorder(14, 18, 14, 18));

		JLabel title = new JLabel("CHI PHÍ PHÁT SINH");
		title.setFont(new Font("Segoe UI", Font.BOLD, 26));
		title.setForeground(Color.WHITE);
		title.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel sub = new JLabel("Pate Hotel • Quản lý chi phí dịch vụ & phạt");
		sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		sub.setForeground(Color.WHITE);
		sub.setAlignmentX(Component.LEFT_ALIGNMENT);

		header.add(title);
		header.add(Box.createVerticalStrut(4));
		header.add(sub);

		return header;
	}

	
private JPanel buildBody() {

		JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
		infoPanel.setBorder(
				new CompoundBorder(new LineBorder(new Color(220, 220, 220), 1, true), new EmptyBorder(14, 14, 14, 14)));
		infoPanel.setBackground(Color.WHITE);

		JLabel lbTitle = new JLabel("Thông tin chi phí phát sinh");
		lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lbTitle.setForeground(NAVY_DARK);
		infoPanel.add(lbTitle, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		// Mã chi phí
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		formPanel.add(new JLabel("Mã chi phí:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		txtMaCP = new JTextField();
		txtMaCP.setEditable(false);
		formPanel.add(txtMaCP, gbc);
		styleField(txtMaCP);

		// Tên chi phí
		gbc.gridx = 2;
		gbc.weightx = 0;
		formPanel.add(new JLabel("Tên chi phí:"), gbc);

		gbc.gridx = 3;
		gbc.weightx = 2; // cho tên rộng hơn
		txtTenCP = new JTextField();
		formPanel.add(txtTenCP, gbc);
		styleField(txtTenCP);

		// Nút tìm
		gbc.gridx = 4;
		gbc.weightx = 0;
		btnTim = new JButton("🔍");
		btnTim.addActionListener(this);
		formPanel.add(btnTim, gbc);

		// Giá
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		formPanel.add(new JLabel("Giá:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		txtGia = new JTextField();
		formPanel.add(txtGia, gbc);
		styleField(txtGia);

		// Loại chi phí
		gbc.gridx = 2;
		gbc.weightx = 0;
		formPanel.add(new JLabel("Loại chi phí:"), gbc);

		gbc.gridx = 3;
		gbc.weightx = 2;
		cboLoai = new JComboBox<>(new String[] { "Dịch vụ", "Phạt" });
		formPanel.add(cboLoai, gbc);

		infoPanel.add(formPanel, BorderLayout.CENTER);

		// ==== Buttons ====

		JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttonRow.setBackground(Color.WHITE);

		btnXoaRong = new JButton("Xóa rỗng");
		styleButton(btnXoaRong);
		btnXoaRong.setBackground(new Color(230, 240, 255));
		btnXoaRong.setForeground(NAVY_DARK);
		buttonRow.add(btnXoaRong);
		btnXoaRong.addActionListener(this);

		btnThem = new JButton("Thêm");
		styleButton(btnThem);
		btnThem.setBackground(new Color(218, 177, 55));
		btnThem.setForeground(NAVY_DARK);
		buttonRow.add(btnThem);
		btnThem.addActionListener(this);

		btnSua = new JButton("Lưu");
		styleButton(btnSua);
		btnSua.setBackground(NAVY_DARK);
		btnSua.setForeground(Color.WHITE);
		buttonRow.add(btnSua);
		btnSua.addActionListener(this);

		infoPanel.add(buttonRow, BorderLayout.SOUTH);

		return infoPanel;
	}

	private JPanel buildTablePanel() {
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(Color.WHITE);
		card.setBorder(
				new CompoundBorder(new LineBorder(new Color(220, 220, 220), 1, true), new EmptyBorder(12, 12, 12, 12)));

		JLabel title = new JLabel("Danh sách chi phí phát sinh");
		title.setFont(new Font("Segoe UI", Font.BOLD, 16));
		title.setForeground(NAVY_DARK);

		String[] columns = { "STT", "Mã CP", "Tên CP", "Loại", "Giá" };
		model = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		table = new JTable(model);
		styleTable(table);
		table.addMouseListener(this);

		card.add(title, BorderLayout.NORTH);
		card.add(new JScrollPane(table), BorderLayout.CENTER);
		return card;

	}

	// ==============Style

	private void styleField(JTextField txt) {
		txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txt.setPreferredSize(new Dimension(0, 32));
	}

	private void styleButton(JButton btn) {
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
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

	// ==============Events
	// Đọc dữ liệu từ DB lên bảng
	private void loadDataToTable() {
		model.setRowCount(0);
		List<ChiPhiPhatSinh> list = dao.getAllChiPhiPhatSinh();
		int stt = 1;
		for (ChiPhiPhatSinh cp : list) {
			model.addRow(new Object[] { stt++, cp.getMaChiPhiPhatSinh(), cp.getTenChiPhiPhatSinh(),
					cp.getLoaiChiPhiPhatSinh(), cp.getGia() });
		}
	}
	// Đọc dữ liệu kết quả tìm kiếm lên bảng
	private void loadDataSearchToTable(String ten) {
		model.setRowCount(0);
		List<ChiPhiPhatSinh> list = dao.getDsChiPhiTheoTen(ten);
		int stt = 1;
		for (ChiPhiPhatSinh cp : list) {
			model.addRow(new Object[] { stt++, cp.getMaChiPhiPhatSinh(), cp.getTenChiPhiPhatSinh(),
					cp.getLoaiChiPhiPhatSinh(), cp.getGia() });
		}
	}
	
	// Lấy dữ liệu từ form và kiểm tra tính hợp lệ
	private ChiPhiPhatSinh getChiPhiFromForm(String ma) {
		String ten = txtTenCP.getText().trim();
		String loai = cboLoai.getSelectedItem().toString();
		double gia = -1;
		// Kiểm tra tên chi phí
		if (ten.isEmpty() || ten.length() > 100) {
			JOptionPane.showMessageDialog(this, "Tên chi phí phát sinh không được rỗng và có tối đa 100 ký tự!");
			return null;
		}

		// Kiểm tra giá
		try {
			gia = Double.parseDouble(txtGia.getText().trim());
			if (gia <= 0) {
				JOptionPane.showMessageDialog(this, "Giá phải là số và lớn hơn 0!");
				return null;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Giá phải là số và lớn hơn 0!");
			return null;
		}

		// Tạo đối tượng ChiPhiPhatSinh
		return new ChiPhiPhatSinh(ma, ten, loai, gia);
	}

	// Làm sạch các trường nhập liệu
	private void clearFields() {
		txtMaCP.setText("");
		txtTenCP.setText("");
		cboLoai.setSelectedIndex(0);
		txtGia.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnThem)) {
			String ma = dao.taoMaCPPSTuDong();
			ChiPhiPhatSinh cp = getChiPhiFromForm(ma);
			if (cp != null) {
				if (dao.insertChiPhi(cp)) {
					JOptionPane.showMessageDialog(this, "Thêm thành công!");
					loadDataToTable();
					clearFields();
				} else {
					JOptionPane.showMessageDialog(this, "Thêm thất bại!");
				}
			}
		} else if (o.equals(btnSua)) {
			String ma = txtMaCP.getText().trim();
			ChiPhiPhatSinh cp = getChiPhiFromForm(ma);
			if (cp != null) {
				if (dao.updateChiPhi(cp)) {
					JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
					loadDataToTable();
				} else {
					JOptionPane.showMessageDialog(this, "Mã không tồn tại!");
				}
			}
		} else if (o.equals(btnTim)) {
			// String ma = txtMaCP.getText().trim();
			// if (ma.isEmpty()) {
			// JOptionPane.showMessageDialog(this, "Vui lòng nhập mã cần tìm!");
			// return;
			// }
			//
			// ChiPhiPhatSinh cp = dao.getChiPhiTheoMa(ma);
			// if (cp != null) {
			// txtTenCP.setText(cp.getTenChiPhiPhatSinh());
			// txtGia.setText(String.valueOf(cp.getGia()));
			// cboLoai.setSelectedItem(cp.getLoaiChiPhiPhatSinh());
			// } else {
			// JOptionPane.showMessageDialog(this, "Không tìm thấy mã: " + ma);
			// }
			String ten = txtTenCP.getText().trim();
			if (ten.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập tên cần tìm!");
				loadDataToTable();
				return;				
			}
			loadDataSearchToTable(ten);
			
		} else if (o.equals(btnXoaRong)) {
			clearFields();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		if (row != -1) {
			txtMaCP.setText(model.getValueAt(row, 1).toString());
			txtTenCP.setText(model.getValueAt(row, 2).toString());
			cboLoai.setSelectedItem(model.getValueAt(row, 3).toString());
			txtGia.setText(model.getValueAt(row, 4).toString());
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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Chi Phí Phát Sinh");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.add(new ChiPhiPhatSinh_GUI());
			frame.setVisible(true);
		});
	}
}
