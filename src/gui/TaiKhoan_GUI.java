package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoan_GUI extends JPanel implements ActionListener, MouseListener{

	private TaiKhoan_DAO dao;
	private JTextField txtTaiKhoan, txtMatKhau,txtMatKhauMoi, txtLapLai;
    private JLabel lblTaiKhoan, lblMatKhau,lblMatKhauMoi, lblLapLai;
    private JButton btnXoaRong, btnDoiMatKhau;
    private JTable table;
    private DefaultTableModel model;
	public TaiKhoan_GUI() {
		dao = new TaiKhoan_DAO();
		setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(); 
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
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
        btnXoaRong = new JButton("Xóa rỗng");
        btnDoiMatKhau = new JButton("Đổi mật khẩu");
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
        wrapper.add(mainPanel);

        add(wrapper, BorderLayout.WEST); 

        // Bảng hiển thị bàn
        model = new DefaultTableModel(new String[]{"Tài khoản", "Mật khẩu"}, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        table.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
     
        add(scrollPane, BorderLayout.EAST);

        btnXoaRong.addActionListener(this);
        btnDoiMatKhau.addActionListener(this);
        table.addMouseListener(this);
        loadTaiKhoanLenTable();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == btnXoaRong) {
			txtMatKhauMoi.setText("");
			txtLapLai.setText("");
		}
		if(obj == btnDoiMatKhau) {
			String taiKhoan = txtTaiKhoan.getText().trim();
			String matKhauCu = txtMatKhau.getText().trim();
			String matKhauMoi = txtMatKhauMoi.getText().trim();
			String lapLaiMatKhau = txtLapLai.getText().trim();
			
			if (taiKhoan.isEmpty() || matKhauCu.isEmpty() || matKhauMoi.isEmpty() || lapLaiMatKhau.isEmpty()) {
		        JOptionPane.showMessageDialog(this, "Chọn 1 tài khoản và điền đầy đủ thông tin!");
		        return;
		    }
			if (!matKhauMoi.equals(lapLaiMatKhau)) {
		        JOptionPane.showMessageDialog(this, "Mật khẩu mới và lặp lại không khớp!");
		        return;
		    }

		    if (dao.doiMatKhau(taiKhoan, matKhauMoi)) {
		        JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
		        txtMatKhau.setText(matKhauMoi);
		        txtMatKhauMoi.setText("");
		        txtLapLai.setText("");
		        loadTaiKhoanLenTable();
		    } else {
		        JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại!");
		    }
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		if (row != -1) {
			txtTaiKhoan.setText(table.getValueAt(row, 0).toString());
			txtMatKhau.setText(table.getValueAt(row, 1).toString());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void loadTaiKhoanLenTable() {
	    ArrayList<TaiKhoan> ds = dao.docTaiKhoanVaMatKhau(); 

	    model.setRowCount(0);

	    for (TaiKhoan nv : ds) {
	        model.addRow(new Object[] { nv.getMaNhanVien(), nv.getMatKhau()});
	    }
	}


}
