package gui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
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
import javax.swing.border.Border;
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
	, txtSoTienApDung, txtGiaTriGiam, txtNgayKetThuc, txtGiamToiDa;
	private JComboBox<String> cboLoaiKM;
    private JDateChooser dateNgayBatDau, dateNgayKetThuc;
    private JButton btnThem, btnluu, btnTimKiem;
    private JTable table;
    private DefaultTableModel modelKM;
    
    private KhuyenMai_DAO kmDAO = new KhuyenMai_DAO();
    
	public KhuyenMai_GUI() {
		setLayout(new BorderLayout());
	        initForm();
	        try {
	            ConnectDB.getInstance().connect();
	            loadKhuyenMaiToTable();
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL: " + e.getMessage());
	        }
	}
	private void initForm() {
		// ------------Form nhap lieu ------------
		JLabel lblmaKM = new JLabel("Mã khuyến mãi"); //1
		JLabel lbltenKM = new JLabel("Tên khuyến mãi"); //1
		JLabel lblngayTao = new JLabel("Ngày tạo"); //2
		JLabel lblngayBatDau = new JLabel("Ngày bắt đầu"); //3
		JLabel lblngayKetThuc = new JLabel("Ngày kết thúc"); //3
		JLabel lblloaiKM = new JLabel("Loại khuyến mãi"); //4
		JLabel lblsoTienApDung = new JLabel("Số tiền áp dụng"); //4
		JLabel lblgiaTriGiam = new JLabel("Giá trị giảm"); //5
		JLabel lblgiamToiDa = new JLabel("Giảm tối đa"); //5
		
		txtMaKM = new JTextField(50);
		txtTenKM = new JTextField(50);
		txtNgayTao = new JTextField(50);
		
		txtMaKM.setEditable(false);
		txtNgayTao.setEditable(false);
		
		cboLoaiKM = new JComboBox<>(new String[]{"Tien", "%"});
		txtSoTienApDung = new JTextField(50);
		txtGiaTriGiam = new JTextField(50);
		txtGiamToiDa = new JTextField(50);
		
		dateNgayBatDau = new JDateChooser();
        dateNgayBatDau.setDateFormatString("dd/MM/yyyy");
        dateNgayBatDau.setDate(new Date());
        
        dateNgayKetThuc = new JDateChooser();
        dateNgayKetThuc.setDateFormatString("dd/MM/yyyy");
        dateNgayKetThuc.setDate(new Date());
         
		JPanel khung = new JPanel();
		khung.setLayout(new BoxLayout(khung, BoxLayout.Y_AXIS));
		khung.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
				"Khuyen mai",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));
		
		JPanel mainpanel = new JPanel(new GridLayout(5, 3, 1, 10));
		
		mainpanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
		JPanel p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		JPanel p3= new JPanel();
		p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
		JPanel p4= new JPanel();
		p4.setLayout(new BoxLayout(p4, BoxLayout.Y_AXIS));
		JPanel p5= new JPanel();
		p5.setLayout(new BoxLayout(p5, BoxLayout.Y_AXIS));
		JPanel p6= new JPanel();
		p6.setLayout(new BoxLayout(p6, BoxLayout.Y_AXIS));
		JPanel p7= new JPanel();
		p7.setLayout(new BoxLayout(p7, BoxLayout.Y_AXIS));
		JPanel p8= new JPanel();
		p8.setLayout(new BoxLayout(p8, BoxLayout.Y_AXIS));
		JPanel p9= new JPanel();
		p9.setLayout(new BoxLayout(p9, BoxLayout.Y_AXIS));
		JPanel p10= new JPanel();
		p10.setLayout(new BoxLayout(p10, BoxLayout.Y_AXIS));
		
		Dimension sizetxt = new Dimension(400,30);
		txtMaKM.setMaximumSize(sizetxt);
		txtTenKM.setMaximumSize(sizetxt);
		txtNgayTao.setMaximumSize(sizetxt);
		dateNgayBatDau.setMaximumSize(sizetxt);
		dateNgayKetThuc.setMaximumSize(sizetxt);
		cboLoaiKM.setMaximumSize(sizetxt);
		txtSoTienApDung.setMaximumSize(sizetxt);
		txtGiaTriGiam.setMaximumSize(sizetxt);
		txtGiamToiDa.setMaximumSize(sizetxt);

		mainpanel.add(p1);
		mainpanel.add(p2);
		mainpanel.add(p3);
		mainpanel.add(p4);
		mainpanel.add(p5);
		mainpanel.add(p6);
		mainpanel.add(p7);
		mainpanel.add(p8);
		mainpanel.add(p9);
		mainpanel.add(p10);
		
		lblmaKM.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtMaKM.setAlignmentX(Component.LEFT_ALIGNMENT);
		lbltenKM.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtTenKM.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblngayTao.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtNgayTao.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblngayBatDau.setAlignmentX(Component.LEFT_ALIGNMENT);
		dateNgayBatDau.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblngayKetThuc.setAlignmentX(Component.LEFT_ALIGNMENT);
		dateNgayKetThuc.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblloaiKM.setAlignmentX(Component.LEFT_ALIGNMENT);
		cboLoaiKM.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblsoTienApDung.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtSoTienApDung.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblgiaTriGiam.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtGiaTriGiam.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblgiamToiDa.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtGiamToiDa.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		p1.add(lblmaKM);
		p1.add(txtMaKM);
		p3.add(lbltenKM);
		p3.add(txtTenKM);
		p4.add(lblngayTao);
		p4.add(txtNgayTao);
		p5.add(lblngayBatDau);
		p5.add(dateNgayBatDau);
		p6.add(lblngayKetThuc);
		p6.add(dateNgayKetThuc);
		p7.add(lblloaiKM);
		p7.add(cboLoaiKM);
		p8.add(lblsoTienApDung);
		p8.add(txtSoTienApDung);
		p9.add(lblgiaTriGiam);
		p9.add(txtGiaTriGiam);
		p10.add(lblgiamToiDa);
		p10.add(txtGiamToiDa);
		
		khung.add(mainpanel);
//-------------button--------
		btnThem = new JButton("Thêm");
        btnTimKiem = new JButton("Tìm kiếm");
        btnluu = new JButton("Lưu");
        
        JPanel pbtn = new JPanel();
        pbtn.setLayout(new BoxLayout(pbtn, BoxLayout.X_AXIS));
        pbtn.add(Box.createHorizontalStrut(5));
        pbtn.add(btnThem);
        pbtn.add(Box.createRigidArea(new Dimension(30, 30)));
        pbtn.add(btnTimKiem);
        pbtn.add(Box.createRigidArea(new Dimension(30, 30)));
        pbtn.add(btnluu);
        pbtn.add(Box.createRigidArea(new Dimension(1300, 30)));
        
        khung.add(pbtn);
//------------------Bảng dữ liệu-------------
      String[] cols = {"STT", "Mã Khuyến Mãi", "Tên Khuyến Mãi", "Ngày tạo", "Loại KM"
      		, "Số tiền áp dụng", "Giá trị giảm", "Giảm tối đa"
      		, "Ngày bắt đầu", "Ngày kết thúc"};
      modelKM = new DefaultTableModel(cols, 0);
      table = new JTable(modelKM);
      table.addMouseListener(this);
      table.setAlignmentX(Component.LEFT_ALIGNMENT);
      
      JScrollPane scroll = new JScrollPane(table);
      
      khung.add(new JScrollPane(table));
      
      loadKhuyenMaiToTable();
      generateMaKhuyenMai();
      
      this.add(khung);
      
      btnThem.addActionListener(this);
      btnTimKiem.addActionListener(this);
      btnluu.addActionListener(this);
}
	//============ Phương thức ===============
	
	private void generateMaKhuyenMai() {
	    try {
	    	txtNgayTao.setText(LocalDate.now().toString());
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
	    modelKM.setRowCount(0);

	    ArrayList<KhuyenMai> list = (ArrayList<KhuyenMai>) kmDAO.docTuBang();
	    int stt = 1;

	    for (KhuyenMai km : list) {
	        modelKM.addRow(new Object[]{
	                stt++,
	                km.getMaKhuyenMai(),
	                km.getTenKhuyenMai(),
	                km.getNgayTao(),
	                km.getLoaiKhuyenMai(),
	                km.getSoTienApDung(),
	                km.getGiaTriGiam(),
	                km.getGiamToiDa(),
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

	        double soTienApDung = Double.parseDouble(txtSoTienApDung.getText().trim());
	        double giaTriGiam = Double.parseDouble(txtGiaTriGiam.getText().trim());
	        double giamToiDa = Double.parseDouble(txtGiamToiDa.getText().trim());
	        
	        LocalDate ngayTao = LocalDate.now();
	        // ngày tạo lấy từ hệ thống

	        LocalDate ngayBatDau = dateNgayBatDau.getDate() == null
	                ? null
	                : new java.sql.Date(dateNgayBatDau.getDate().getTime()).toLocalDate();
	        LocalDate ngayKetThuc = dateNgayKetThuc.getDate() == null
	                ? null
	                : new java.sql.Date(dateNgayKetThuc.getDate().getTime()).toLocalDate();

	        // kiểm tra ràng buộc giống database
	        if (ngayBatDau == null || ngayKetThuc == null) {
	        	JOptionPane.showMessageDialog(this, "Ngày bắt đầu và ngày kết thúc không được để trống!");
	            return null;
	        }
	        if (ngayBatDau.isBefore(LocalDate.now())) {
	            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải >= ngày hiện tại!");
	           return null;
	        }
	        if (ngayKetThuc.isBefore(ngayBatDau)) {
	            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải >= ngày bắt đầu!");
	            return null;
	        }
	        if (soTienApDung < 0) {
	            JOptionPane.showMessageDialog(this, "Số tiền áp dụng không được âm!");
	            return null;
	        }
	        if (giaTriGiam <= 0) {
	            JOptionPane.showMessageDialog(this, "Giá trị giảm phải > 0!");
	            return null;
	        }
	        if (giamToiDa <= 0) {
	            JOptionPane.showMessageDialog(this, "Giảm tối đa phải > 0!");
	            return null;
	        }
	        return new KhuyenMai(
	                ma,
	                ten,
	                ngayTao,
	                ngayBatDau,
	                ngayKetThuc,
	                loai,
	                soTienApDung,
	                giaTriGiam,
	                giamToiDa
	        );

	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ, vui lòng kiểm tra lại!");
	        return null;
	    }
	}

	private void clearForm() {
	    generateMaKhuyenMai();
	    txtTenKM.setText("");
	    txtSoTienApDung.setText("");
	    txtGiaTriGiam.setText("");
	    txtGiamToiDa.setText(""); 
	    txtNgayTao.setText(LocalDate.now().toString());
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
//	    else if (o.equals(btnXoa)) {
//	    	int row = table.getSelectedRow();
//	        if (row == -1) {
//	            JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng muốn xóa!");
//	            return;
//	        }
//	        String ma = txtMaKM.getText().trim();
//	        int confirm = JOptionPane.showConfirmDialog(
//	                null,
//	                "Bạn có chắc muốn xóa khuyến mãi có mã: " + ma + " ?",
//	                "Xác nhận xóa",
//	                JOptionPane.YES_NO_OPTION
//	        );
//	        if (confirm == JOptionPane.YES_OPTION) {
//	            boolean check = kmDAO.delete(ma);
//	            if (check) {
//	                DefaultTableModel model = (DefaultTableModel) table.getModel();
//	                model.removeRow(row);
//	                
//	                clearForm();
//	                
//	                JOptionPane.showMessageDialog(null, "Xóa thành công!");
//	            } else {
//	                JOptionPane.showMessageDialog(null, "Xóa thất bại! (có thể do ràng buộc khóa ngoại)");
//	            }
//	        }
//	    }
//	    else if (o.equals(btnluu)) {
//	    	KhuyenMai km = getFormData();
//	        if (km == null) return; // kiểm tra dữ liệu hợp lệ
//
//	        if (kmDAO.update(km)) {
//	            JOptionPane.showMessageDialog(null, "Cập nhật khuyến mãi thành công!");
//	            loadKhuyenMaiToTable();
//	        } else {
//	            JOptionPane.showMessageDialog(null, "Cập nhật thất bại!");
//	        }
//	    }
	}
	@Override
	public void mouseClicked(MouseEvent e) {
//		int row = table.getSelectedRow();
//        if (row == -1) return;
//
//        txtMaKM.setText(table.getValueAt(row, 1).toString());
//        txtTenKM.setText(table.getValueAt(row, 2).toString());
//        txtNgayTao.setText(table.getValueAt(row, 3).toString());
//        cboLoaiKM.setSelectedItem(table.getValueAt(row, 4).toString());
//        txtDieuKienGiam.setText(table.getValueAt(row, 5).toString());
//        txtGiaTriGiam.setText(table.getValueAt(row, 6).toString());
//
//        // Xử lý ngày bắt đầu
//        String ngayBD = table.getValueAt(row, 7).toString();
//        if (ngayBD != null && !ngayBD.isEmpty()) {
//            java.sql.Date d = java.sql.Date.valueOf(ngayBD);
//            dateNgayBatDau.setDate(d);
//        } else {
//            dateNgayBatDau.setDate(null);
//        }
//
//        // Xử lý ngày kết thúc
//        String ngayKT = table.getValueAt(row, 8).toString();
//        if (ngayKT != null && !ngayKT.isEmpty()) {
//            java.sql.Date d = java.sql.Date.valueOf(ngayKT);
//            dateNgayKetThuc.setDate(d);
//        } else {
//            dateNgayKetThuc.setDate(null);
//        }
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
//
