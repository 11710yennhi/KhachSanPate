package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import connectDB.ConnectDB;
import dao.KhuyenMai_DAO;
import entity.KhuyenMai;

public class KhuyenMai_GUI extends JPanel implements ActionListener, MouseListener {

    // ===== THEME (KHÔNG TẠO CLASS MỚI) =====
    private final Color NAVY = new Color(10, 52, 89);
    private final Color NAVY_DARK = new Color(7, 40, 68);
    private final Color GOLD = new Color(218, 177, 55);
    private final Color BG = new Color(245, 247, 250);
    private final Color BORDER = new Color(220, 227, 235);

    private final Font FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    private JTextField txtMaKM, txtTenKM, txtNgayTao, txtSoTienApDung,
            txtGiaTriGiam, txtGiamToiDa;
    private JComboBox<String> cboLoaiKM;
    private JDateChooser dateNgayBatDau, dateNgayKetThuc;
    private JButton btnThem, btnluu, btnTimKiem;
    private JTable table;
    private DefaultTableModel modelKM;

    private KhuyenMai_DAO kmDAO = new KhuyenMai_DAO();

    // ================= CONSTRUCTOR =================
    public KhuyenMai_GUI() {
        setLayout(new BorderLayout(12, 12));
        setBackground(BG);

        initForm();

        try {
            ConnectDB.getInstance().connect();
            loadKhuyenMaiToTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL");
        }
    }

    // ================= INIT FORM =================
    private void initForm() {

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("QUẢN LÝ KHUYẾN MÃI");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

     // ===== FORM =====
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(16, 5, 16, 500)
        ));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== INIT FIELD =====
        txtMaKM = createField(false);
        txtTenKM = createField(true);
        txtNgayTao = createField(false);
        txtSoTienApDung = createField(true);
        txtGiaTriGiam = createField(true);
        txtGiamToiDa = createField(true);

        cboLoaiKM = new JComboBox<>(new String[]{"Tiền", "%"});
        styleCombo(cboLoaiKM);

        dateNgayBatDau = new JDateChooser();
        dateNgayKetThuc = new JDateChooser();

        styleDateChooser(dateNgayBatDau);
        styleDateChooser(dateNgayKetThuc);

        // mặc định hôm nay
        Date today = new Date();
        dateNgayBatDau.setDate(today);
        dateNgayKetThuc.setDate(today);


        // ===== ROW 0 =====
        gbc.gridy = 0;
        addLabel(form, gbc, 0, "Mã KM");
        addField(form, gbc, 1, txtMaKM);
        addLabel(form, gbc, 2, "Ngày tạo");
        addField(form, gbc, 3, txtNgayTao);

        // ===== ROW 1 =====
        gbc.gridy = 1;
        addLabel(form, gbc, 0, "Tên KM");
        addField(form, gbc, 1, txtTenKM);
        addLabel(form, gbc, 2, "Loại KM");
        addField(form, gbc, 3, cboLoaiKM);

        // ===== ROW 2 =====
        gbc.gridy = 2;
        addLabel(form, gbc, 0, "Số tiền áp dụng");
        addField(form, gbc, 1, txtSoTienApDung);
        addLabel(form, gbc, 2, "Giá trị giảm");
        addField(form, gbc, 3, txtGiaTriGiam);

        // ===== ROW 3 =====
        gbc.gridy = 3;
        addLabel(form, gbc, 0, "Giảm tối đa");
        addField(form, gbc, 1, txtGiamToiDa);
        addLabel(form, gbc, 2, "");      // giữ layout
        addField(form, gbc, 3, new JLabel(""));

        // ===== ROW 4 =====
        gbc.gridy = 4;
        addLabel(form, gbc, 0, "Ngày bắt đầu");
        addField(form, gbc, 1, dateNgayBatDau);
        addLabel(form, gbc, 2, "Ngày kết thúc");
        addField(form, gbc, 3, dateNgayKetThuc);

        // ===== WRAPPER =====
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setOpaque(false);
        formWrapper.setBorder(new EmptyBorder(16, 24, 16, 24));
        formWrapper.add(form, BorderLayout.CENTER);

        card.add(formWrapper, BorderLayout.CENTER);


        // ===== BUTTON =====
        btnThem = createButton("Thêm", GOLD, NAVY_DARK);
        btnluu = createButton("Lưu", NAVY, Color.WHITE);
        btnTimKiem = createButton("Tìm kiếm", new Color(230,236,244), NAVY_DARK);

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pBtn.setOpaque(false);
        pBtn.add(btnThem);
        pBtn.add(btnluu);
        pBtn.add(btnTimKiem);

        card.add(pBtn, BorderLayout.SOUTH);

        // ===== TABLE =====
        String[] cols = {"STT","Mã KM","Tên KM","Ngày tạo","Loại",
                "Số tiền áp dụng","Giá trị giảm","Giảm tối đa",
                "Ngày bắt đầu","Ngày kết thúc"};

        modelKM = new DefaultTableModel(cols, 0);
        table = new JTable(modelKM);
        table.addMouseListener(this);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        center.add(card, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        // ===== EVENT =====
        btnThem.addActionListener(this);
        btnluu.addActionListener(this);
        btnTimKiem.addActionListener(this);

        generateMaKhuyenMai();
    }
    //============ hỗ trợ giao diện==================
    private void styleDateChooser(JDateChooser dc) {
        // format
        dc.setDateFormatString("dd/MM/yyyy");

        // lấy editor (JTextField bên trong)
        JTextField editor = ((JTextField) dc.getDateEditor().getUiComponent());

        editor.setFont(FONT);                       // cùng font
        editor.setBackground(Color.WHITE);          // nền trắng
        editor.setForeground(Color.BLACK);

        editor.setBorder(new EmptyBorder(6, 8, 6, 8)); // padding trong

        Dimension size = new Dimension(300, 38);    // giống JTextField
        dc.setPreferredSize(size);
        dc.setMinimumSize(size);
        
        Border fieldBorder = new LineBorder(BORDER, 1, true);
        editor.setBorder(fieldBorder);
        dc.setBorder(fieldBorder);
    }


    private void addLabel(JPanel p, GridBagConstraints gbc, int x, String text) {
        gbc.gridx = x;
        gbc.weightx = 0;
        p.add(label(text), gbc);
    }

    private void addField(JPanel p, GridBagConstraints gbc, int x, Component c) {
        gbc.gridx = x;
        gbc.weightx = 1; // FIELD GIÃN
        p.add(c, gbc);
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
	        // ===== LẤY DỮ LIỆU =====
	        String ma = txtMaKM.getText().trim();
	        String ten = txtTenKM.getText().trim();
	        String loai = (String) cboLoaiKM.getSelectedItem();

	        if (ma.isEmpty() || ten.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Mã và tên khuyến mãi không được để trống!");
	            return null;
	        }

	        double soTienApDung = Double.parseDouble(txtSoTienApDung.getText().trim());
	        double giaTriGiam   = Double.parseDouble(txtGiaTriGiam.getText().trim());
	        double giamToiDa    = Double.parseDouble(txtGiamToiDa.getText().trim());

	        LocalDate ngayTao = LocalDate.now();

	        LocalDate ngayBatDau = dateNgayBatDau.getDate() == null
	                ? null
	                : new java.sql.Date(dateNgayBatDau.getDate().getTime()).toLocalDate();

	        LocalDate ngayKetThuc = dateNgayKetThuc.getDate() == null
	                ? null
	                : new java.sql.Date(dateNgayKetThuc.getDate().getTime()).toLocalDate();

	        // ===== CHECK NGÀY =====
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

	        // ===== CHECK SỐ TIỀN =====
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

	        // ===== CHECK THEO LOẠI KM =====
	        if ("%".equals(loai)) {

	            // chỉ cho số nguyên
	            if (giaTriGiam % 1 != 0) {
	                JOptionPane.showMessageDialog(this, "Giảm theo % chỉ được nhập số nguyên!");
	                return null;
	            }

	            // >1 và <100
	            if (giaTriGiam <= 1 || giaTriGiam >= 100) {
	                JOptionPane.showMessageDialog(this, "Giảm theo % phải lớn hơn 1 và nhỏ hơn 100!");
	                return null;
	            }

	        } else { // giảm theo tiền

	            if (giaTriGiam > soTienApDung && soTienApDung > 0) {
	                JOptionPane.showMessageDialog(this, "Giá trị giảm không được lớn hơn số tiền áp dụng!");
	                return null;
	            }
	        }

	        // ===== OK → TẠO OBJECT =====
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

	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số!");
	        return null;
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
        txtSoTienApDung.setText(table.getValueAt(row, 5).toString());
        txtGiaTriGiam.setText(table.getValueAt(row, 6).toString());
        txtGiamToiDa.setText(table.getValueAt(row, 7).toString());
        
        // Xử lý ngày bắt đầu
        String ngayBD = table.getValueAt(row, 8).toString();
        if (ngayBD != null && !ngayBD.isEmpty()) {
            java.sql.Date d = java.sql.Date.valueOf(ngayBD);
            dateNgayBatDau.setDate(d);
        } else {
            dateNgayBatDau.setDate(null);
        }

        // Xử lý ngày kết thúc
        String ngayKT = table.getValueAt(row, 9).toString();
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
    
    // ================= STYLE HELPER =================
    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT);
        l.setForeground(NAVY_DARK);

        // padding: top, left, bottom, right
        l.setBorder(new EmptyBorder(0, 100, 0, 0)); // đẩy vô bên trái ?px

        return l;
    }


    private JTextField createField(boolean editable) {
        JTextField f = new JTextField();
        f.setEditable(editable);
        f.setFont(FONT);
        f.setBorder(new CompoundBorder(
                new LineBorder(BORDER,1,true),
                new EmptyBorder(8,10,8,10)
        ));
        return f;
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setFont(FONT);
        cb.setBorder(new LineBorder(BORDER,1,true));
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

    private void styleTable(JTable t) {
        t.setRowHeight(34);
        t.setFont(FONT);
        t.setSelectionBackground(new Color(225,238,252));
        t.setShowVerticalLines(false);

        JTableHeader h = t.getTableHeader();
        h.setFont(FONT_BOLD);
        h.setBackground(NAVY_DARK);
        h.setForeground(Color.WHITE);
    }
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
