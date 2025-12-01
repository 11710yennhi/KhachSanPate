package gui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import connectDB.ConnectDB;
import dao.KhuyenMai_DAO;
import dao.NhanVien_DAO;
import entity.KhuyenMai;
import entity.NhanVien;

public class KhuyenMai_GUI extends JPanel implements ActionListener, MouseListener{
	private JTextField txtMaKM, txtTenKM, txtNgayTao, txtNgayBatDau
	, txtDieuKienGiam, txtGiaTriGiam, txtNgayKetThuc;
	private JComboBox<String> cboLoaiKM;
    private JDateChooser dateNgayBatDau, dateNgayKetThuc;
    private JButton btnThem, btnluu, btnTimKiem, btnXoa;
    private JTable table;
    private DefaultTableModel modelKM;
    
    private KhuyenMai_DAO kmDAO = new KhuyenMai_DAO();
    
	public KhuyenMai_GUI() {
		 setLayout(new BorderLayout(10, 10));
	        setBackground(Color.WHITE);

	        // ===== TIÊU ĐỀ =====
	        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
	        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 26));
	        lblTitle.setForeground(new Color(30, 60, 114));
	        add(lblTitle, BorderLayout.NORTH);

	        // ===== KHỞI TẠO GIAO DIỆN =====
	        initForm();

	        // ===== KẾT NỐI SQL & LOAD DỮ LIỆU =====
	        try {
	            ConnectDB.getInstance().connect();
	            loadKhuyenMaiToTable();
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL: " + e.getMessage());
	        }
	}
	private void initForm() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new TitledBorder("Khuyến mãi"));
        p.setBackground(Color.WHITE);
        add(p, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        // Mã KM, loại KM
        mainPanel.add(new JLabel("Mã khuyến mãi:"));
        txtMaKM = new JTextField();
        txtMaKM.setEditable(false);
        mainPanel.add(txtMaKM);

        mainPanel.add(new JLabel("Loại khuyến mãi:"));
        cboLoaiKM = new JComboBox<>(new String[]{"Tien", "%"});
        mainPanel.add(cboLoaiKM);

        // Tên KM, Điều kiện Giảm
        mainPanel.add(new JLabel("Tên khuyến mãi:"));
        txtTenKM= new JTextField();
        mainPanel.add(txtTenKM);
        
        mainPanel.add(new JLabel("Điều kiện giảm"));
        txtDieuKienGiam = new JTextField();
        mainPanel.add(txtDieuKienGiam);
        
        // Thời gian tạo, giá trị giảm
        mainPanel.add(new JLabel("Thời gian tạo:"));
        txtNgayTao = new JTextField(java.time.LocalDate.now().toString());
        txtNgayTao.setEditable(false);
        mainPanel.add(txtNgayTao);
        
        mainPanel.add(new JLabel("Giá trị giảm:"));
        txtGiaTriGiam = new JTextField();
        mainPanel.add(txtGiaTriGiam);

        // Ngày bắt đầu, ngày kết thúc
        
        mainPanel.add(new JLabel("Ngày bắt đầu:"));
        dateNgayBatDau = new JDateChooser();
        dateNgayBatDau.setDateFormatString("dd/MM/yyyy");
        dateNgayBatDau.setDate(new Date());
        mainPanel.add(dateNgayBatDau);

        mainPanel.add(new JLabel("Ngày kết thúc:"));
        dateNgayKetThuc = new JDateChooser();
        dateNgayKetThuc.setDateFormatString("dd/MM/yyyy");
        dateNgayKetThuc.setDate(new Date());
        mainPanel.add(dateNgayKetThuc);
        
        // Cột nút
        JPanel buttonCol = new JPanel();
        buttonCol.setLayout(new BoxLayout(buttonCol, BoxLayout.Y_AXIS));
        buttonCol.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonCol.setBackground(Color.WHITE);

        btnThem = new JButton("Thêm");
        btnluu = new JButton("Lưu");
        btnTimKiem = new JButton("Tìm kiếm");
        btnXoa = new JButton("Xóa");

        Dimension btnSize = new Dimension(120, 35);

        for (JButton btn : new JButton[]{btnThem, btnluu, btnTimKiem, btnXoa}) {
            btn.setFont(new Font("Tahoma", Font.PLAIN, 14));
            btn.setBackground(new Color(220, 230, 250));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setPreferredSize(btnSize);
            btn.setMaximumSize(btnSize);
            btn.setMinimumSize(btnSize); // (optional) để chắc chắn layout không co lại
            buttonCol.add(btn);
            buttonCol.add(Box.createVerticalStrut(10));
            btn.addActionListener(this);
        }
        
        //========== ADD vào p ==========
        p.add(mainPanel, BorderLayout.CENTER);
        p.add(buttonCol, BorderLayout.EAST);

        // Bảng dữ liệu
        String[] cols = {"STT", "Mã Khuyến Mãi", "Tên Khuyến Mãi", "Ngày tạo", "Loại KM"
        		, "Điều kiện giảm", "Giá trị giảm"
        		, "Ngày bắt đầu", "Ngày kết thúc"};
        modelKM = new DefaultTableModel(cols, 0);
        table = new JTable(modelKM);
        table.addMouseListener(this);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        loadKhuyenMaiToTable();
        generateMaKhuyenMai();
    }
	
	//============ Phương thức ===============
	
	private void generateMaKhuyenMai() {
	    try {
	        // Lấy ngày hiện tại dạng ddMMyyyy
	        String date = new SimpleDateFormat("ddMMyyyy").format(new Date());

	        // Đọc toàn bộ DS Khuyến Mãi
	        ArrayList<KhuyenMai> list = (ArrayList<KhuyenMai>) kmDAO.docTuBang();

	        int max = 0;
	        for (KhuyenMai km : list) {
	            String ma = km.getMaKhuyenMai();
	            if (ma.startsWith("KM" + date)) {
	                // Lấy 3 số cuối
	                int stt = Integer.parseInt(ma.substring(ma.length() - 3));
	                if (stt > max) {
	                    max = stt;
	                }
	            }
	        }

	        // +1 để tạo mã mới
	        int next = max + 1;
	        String stt = String.format("%03d", next);

	        txtMaKM.setText("KM" + date + stt);

	    } catch (Exception e) {
	        e.printStackTrace();
	        txtMaKM.setText("KM" + new SimpleDateFormat("ddMMyyyy").format(new Date()) + "001");
	    }
	}


	
	private void loadKhuyenMaiToTable() {
		generateMaKhuyenMai();
	    modelKM.setRowCount(0); // Xóa dữ liệu cũ trong table
	    ArrayList<KhuyenMai> list = (ArrayList<KhuyenMai>) kmDAO.docTuBang(); // Lấy dữ liệu từ DB
	    int stt = 1;

	    for (KhuyenMai km : list) {
	        modelKM.addRow(new Object[]{
	                stt++,
	                km.getMaKhuyenMai(),
	                km.getTenKhuyenMai(),
	                km.getNgayTao(),
	                km.getLoaiKhuyenMai(),
	                km.getDieuKien(),
	                km.getGiaTriGiam(),
	                km.getNgayBatDau(),
	                km.getNgayKetThuc()   
	        });
	    }
	}
	private KhuyenMai getFormData() {
	    try {
	        String ma = txtMaKM.getText().trim();
	        String ten = txtTenKM.getText().trim();
	        String loai = (String) cboLoaiKM.getSelectedItem();

	        double dieuKien = Double.parseDouble(txtDieuKienGiam.getText().trim());
	        double giaTriGiam = Double.parseDouble(txtGiaTriGiam.getText().trim());

	        LocalDate ngayTao = LocalDate.now();
	        LocalDate ngayBatDau = null;
	        LocalDate ngayKetThuc = null;

	        if (dateNgayBatDau.getDate() != null)
	            ngayBatDau = new java.sql.Date(dateNgayBatDau.getDate().getTime()).toLocalDate();

	        if (dateNgayKetThuc.getDate() != null)
	            ngayKetThuc = new java.sql.Date(dateNgayKetThuc.getDate().getTime()).toLocalDate();

	        return new KhuyenMai(ma, ten, ngayTao, ngayBatDau, ngayKetThuc, loai, dieuKien, giaTriGiam);

	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ, vui lòng kiểm tra lại!");
	        return null;
	    }
	}




	private void clearForm() {
	    generateMaKhuyenMai();
	    txtTenKM.setText("");
	    txtDieuKienGiam.setText("");
	    txtGiaTriGiam.setText("");
	    txtNgayTao.setText(java.time.LocalDate.now().toString());
	    dateNgayBatDau.setDate(null);
	    dateNgayKetThuc.setDate(null);
	    cboLoaiKM.setSelectedIndex(0);
	    txtMaKM.requestFocus();
	}
	
	

	// =============== ACtion =================
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
	    
	    if (o.equals(btnThem)) {
	    	generateMaKhuyenMai();
	        KhuyenMai km = getFormData();
	        if (km != null) {
	            if (kmDAO.create(km)) {
	                JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!");
	                loadKhuyenMaiToTable();
	                clearForm();
	            } else {
	                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
	            }
	        }

	    } 
	    else if (o.equals(btnXoa)) {
	    	int row = table.getSelectedRow();
	        if (row == -1) {
	            JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng muốn xóa!");
	            return;
	        }
	        String ma = txtMaKM.getText().trim();
	        int confirm = JOptionPane.showConfirmDialog(
	                null,
	                "Bạn có chắc muốn xóa khuyến mãi có mã: " + ma + " ?",
	                "Xác nhận xóa",
	                JOptionPane.YES_NO_OPTION
	        );
	        if (confirm == JOptionPane.YES_OPTION) {
	            boolean check = kmDAO.delete(ma);
	            if (check) {
	                DefaultTableModel model = (DefaultTableModel) table.getModel();
	                model.removeRow(row);
	                
	                clearForm();
	                
	                JOptionPane.showMessageDialog(null, "Xóa thành công!");
	            } else {
	                JOptionPane.showMessageDialog(null, "Xóa thất bại! (có thể do ràng buộc khóa ngoại)");
	            }
	        }
	    }
	    else if (o.equals(btnluu)) {
	    	KhuyenMai km = getFormData();
	        if (km == null) return; // kiểm tra dữ liệu hợp lệ

	        if (kmDAO.update(km)) {
	            JOptionPane.showMessageDialog(null, "Cập nhật khuyến mãi thành công!");
	            loadKhuyenMaiToTable();
	        } else {
	            JOptionPane.showMessageDialog(null, "Cập nhật thất bại!");
	        }
	    }
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
        if (row == -1) return;

        txtMaKM.setText(table.getValueAt(row, 1).toString());
        txtTenKM.setText(table.getValueAt(row, 2).toString());
        txtNgayTao.setText(table.getValueAt(row, 3).toString());
        cboLoaiKM.setSelectedItem(table.getValueAt(row, 4).toString());
        txtDieuKienGiam.setText(table.getValueAt(row, 5).toString());
        txtGiaTriGiam.setText(table.getValueAt(row, 6).toString());

        // Xử lý ngày bắt đầu
        String ngayBD = table.getValueAt(row, 7).toString();
        if (ngayBD != null && !ngayBD.isEmpty()) {
            java.sql.Date d = java.sql.Date.valueOf(ngayBD);
            dateNgayBatDau.setDate(d);
        } else {
            dateNgayBatDau.setDate(null);
        }

        // Xử lý ngày kết thúc
        String ngayKT = table.getValueAt(row, 8).toString();
        if (ngayKT != null && !ngayKT.isEmpty()) {
            java.sql.Date d = java.sql.Date.valueOf(ngayKT);
            dateNgayKetThuc.setDate(d);
        } else {
            dateNgayKetThuc.setDate(null);
        }
	}

	@Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

 // ===== CHẠY THỬ =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f1 = new JFrame("Quản lý KM");
            f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f1.setExtendedState(JFrame.MAXIMIZED_BOTH);
            f1.add(new KhuyenMai_GUI());
            f1.setVisible(true);
        });
    }
}

