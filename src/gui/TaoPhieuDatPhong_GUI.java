package gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import dao.*;
import entity.PhieuDatPhong;
import entity.Phong;
import entity.ChiPhiPhatSinh;
import entity.ChiTietChiPhiPhatSinh;
import entity.ChiTietPhieuDatPhong;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.LoaiPhong;
import entity.NhanVien;

public class TaoPhieuDatPhong_GUI extends JPanel implements ActionListener, MouseListener{
    private static final long serialVersionUID = 1L;
   
    // ====== BIẾN TOÀN CỤC ======
    private JDateChooser dateNgayNhan, dateNgayTra;
    private JTextField txtSDT, txtTenKH, txtNgayTao, txtTienCoc, txtNV, txtMKH, txtMPDP, txtNguoiLon, txtTreEm, txtThucTe, txtSoNguoiThuc,txtTienCocMoi;
    private JCheckBox chkVN;
    private DefaultTableModel dlp, dlctps;
    private JComboBox<String> cboTrangThai, cboChiPhi;
    private JButton btnInPhieu, btnXoa, btnThemCP, btnXoaCP, btnLuu, btnHuy, btnTT, btnXR, btnCapNhat, btnXNDP, btnTPS, btnGoiY;
    private JTable tblPhong, tblChiPhi;
    private JLabel lblTongTien, lblTongCP, lblTongTatCa,lblThongBao;
    private Color mauXanhDam, mauVangDong;
    private final Color MAU_PHONG_DA_CHON = new Color(214, 234, 248);
    private LoaiPhong_DAO dslp;
    private Phong_DAO dsp;
    private ChiTietPhieuDatPhong_DAO dsctpdp;
    private PhieuDatPhong_DAO pdp;
    private KhachHang_DAO khd;
    private NhanVien_DAO nvd;
    private ChiPhiPhatSinh_DAO cppsd;
    private ChiTietChiPhiPhatSinh_DAO ctcppsd;
    private LoaiPhong_DAO lpd;
    private HoaDon_DAO hdd;
    private JPanel pPhongTrong,pTop,pHang2, pHang1;
    private int soNguoiLon = 0;
    private int soTreEm = 0;
    private String mNV;
    private String maHD;
    private Set<String> dsPhongDaChon = new HashSet<>();
    
    public TaoPhieuDatPhong_GUI(String maNV) {
    	mNV= maNV;
        setLayout(new BorderLayout());
        setBackground(new Color(249, 249, 249));

        // ====== MÀU VÀ FONT ======
        mauXanhDam = new Color(30, 61, 89);
        mauVangDong = new Color(212, 175, 55);
        Font fontTieuDe = new Font("Segoe UI Semibold", Font.PLAIN, 16);
        Color xanhNgoc = new Color(72, 189, 157);
        Color nauNhat = new Color(90, 155, 210); 
        Color xanhDuong = new Color(79, 195, 247);
        Color doCanhBao = new Color(229, 57, 53);
        Color xamXanh = new Color(120, 144, 156);
        Color timNhat = new Color(171, 71, 188);
        
           // ====== KHỞI TẠO DAO ======
        dslp = new LoaiPhong_DAO();
        dsp = new Phong_DAO();
        dsctpdp = new ChiTietPhieuDatPhong_DAO();
        pdp = new PhieuDatPhong_DAO();
        khd = new KhachHang_DAO();
        nvd = new NhanVien_DAO();
        cppsd = new ChiPhiPhatSinh_DAO();
        ctcppsd = new ChiTietChiPhiPhatSinh_DAO();
        lpd = new LoaiPhong_DAO();
        hdd= new HoaDon_DAO();
        
        txtNguoiLon= new JTextField();
        txtTreEm= new JTextField();
        txtThucTe = new JTextField("0");

        // ====== CHIA KHUNG CHÍNH ======
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(0.7);
        splitPane.setResizeWeight(0.7);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);

        // ====== PANEL TRÁI ======
        JPanel pLeft = new JPanel();
        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
        pLeft.setBackground(new Color(249, 249, 249));
        pLeft.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        // --- KHUNG THÔNG TIN PHIẾU ---
        JPanel pThongTin = new JPanel(new GridBagLayout());
        pThongTin.setBackground(Color.WHITE);
        pThongTin.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(mauXanhDam, 1, true),
                "Thông tin phiếu đặt phòng",
                TitledBorder.LEFT, TitledBorder.TOP, fontTieuDe, mauXanhDam));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
     // KÍCH THƯỚC Ô NHẬP
        Dimension inputSize = new Dimension(220, 27);

        // THÊM KHOẢNG CÁCH GIỮA CÁC Ô
        gbc.insets = new Insets(5, 10, 5, 10);

     // HÀNG 0 – Mã phiếu + Ngày tạo
        gbc.gridy = 0;
        pThongTin.add(new JLabel("Mã phiếu:"), gbc);

        gbc.gridx = 1;
        txtMPDP = new JTextField();
        txtMPDP.setPreferredSize(inputSize);
        txtMPDP.setEditable(false);
        pThongTin.add(txtMPDP, gbc);

        // Ngày tạo
        gbc.gridx = 2;
        pThongTin.add(new JLabel("Ngày tạo:"), gbc);

        gbc.gridx = 3;
        txtNgayTao = new JTextField(LocalDate.now().toString());
        txtNgayTao.setEditable(false);
        txtNgayTao.setPreferredSize(inputSize);
        pThongTin.add(txtNgayTao, gbc);


        // HÀNG 1 – Mã KH + Tên KH
        gbc.gridy = 1;

        gbc.gridx = 0;
        pThongTin.add(new JLabel("Mã KH:"), gbc);

        gbc.gridx = 1;
        txtMKH = new JTextField();
        txtMKH.setEditable(false);
        txtMKH.setPreferredSize(inputSize);
        pThongTin.add(txtMKH, gbc);

        gbc.gridx = 2;
        pThongTin.add(new JLabel("Tên KH:"), gbc);

        gbc.gridx = 3;
        txtTenKH = new JTextField();
        txtTenKH.setEditable(false);
        txtTenKH.setPreferredSize(inputSize);
        pThongTin.add(txtTenKH, gbc);


        // HÀNG 2 – SDT + Người Việt Nam
        gbc.gridy = 2;

        gbc.gridx = 0;
        pThongTin.add(new JLabel("Số điện thoại:"), gbc);

        gbc.gridx = 1;
        txtSDT = new JTextField();
        txtSDT.setEditable(false);
        txtSDT.setPreferredSize(inputSize);
        pThongTin.add(txtSDT, gbc);

        gbc.gridx = 2;
        pThongTin.add(new JLabel("Người Việt Nam:"), gbc);

        gbc.gridx = 3;
        chkVN = new JCheckBox("Có");
        chkVN.setBackground(Color.WHITE);
        chkVN.setSelected(true);
        pThongTin.add(chkVN, gbc);


        // HÀNG 3 – Tổng tiền cọc + Tiền cọc mới
        gbc.gridy = 3;

        gbc.gridx = 0;
        pThongTin.add(new JLabel("Tổng tiền cọc:"), gbc);

        gbc.gridx = 1;
        txtTienCoc = new JTextField();
        txtTienCoc.setEditable(false);
        txtTienCoc.setPreferredSize(inputSize);
        pThongTin.add(txtTienCoc, gbc);

        gbc.gridx = 2;
        pThongTin.add(new JLabel("Tiền cọc mới:"), gbc);

        gbc.gridx = 3;
        txtTienCocMoi = new JTextField();
        txtTienCocMoi.setEditable(getFocusTraversalKeysEnabled());
        txtTienCocMoi.setPreferredSize(inputSize);
        pThongTin.add(txtTienCocMoi, gbc);


        // HÀNG 4 – Trạng thái + Nút xác nhận đặt phòng
        gbc.gridy = 4;

        gbc.gridx = 0;
        pThongTin.add(new JLabel("Trạng thái:"), gbc);

        gbc.gridx = 1;
        cboTrangThai = new JComboBox<>(new String[]{"Đã đặt", "Đang ở", "Hoàn thành", "Đã hủy"});
        pThongTin.add(cboTrangThai, gbc);

        // Nút xác nhận
        gbc.gridx = 3;
        btnInPhieu = new JButton("🖨 Xác nhận đặt phòng");
        btnInPhieu.setBackground(mauVangDong);
        btnInPhieu.setForeground(mauXanhDam);
        btnInPhieu.setFocusPainted(false);
        btnInPhieu.setEnabled(false);
        pThongTin.add(btnInPhieu, gbc);



        // --- KHUNG CHI TIẾT PHÒNG ---
        JPanel pChiTietPhong = new JPanel(new BorderLayout());
        pChiTietPhong.setBackground(Color.WHITE);
        pChiTietPhong.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(mauXanhDam, 1, true),
                "Chi tiết phòng thuê",
                TitledBorder.LEFT, TitledBorder.TOP, fontTieuDe, mauXanhDam));

        String[] cols = {"STT", "Mã phòng", "Loại phòng", "Ngày nhận", "Ngày trả Thực","Ngày trả", "Số đêm", "Giá", "Thành tiền"};
        dlp = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tblPhong = new JTable(dlp);
        

        pChiTietPhong.add(new JScrollPane(tblPhong), BorderLayout.CENTER);

        JPanel pPhongBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pPhongBtn.setBackground(Color.WHITE);
        btnXoa = new JButton("Xóa");
        lblTongTien = new JLabel("Tổng tiền: 0 VNĐ");
        btnXNDP= new JButton("Tạo Phiếu");
        btnTPS= new JButton("Trả phòng");
        pPhongBtn.add(btnXNDP);
        pPhongBtn.add(btnTPS);
        pPhongBtn.add(btnXoa);
        pPhongBtn.add(lblTongTien);       
        pChiTietPhong.add(pPhongBtn, BorderLayout.SOUTH);

        pLeft.add(pThongTin);
        pLeft.add(Box.createVerticalStrut(10));
        pLeft.add(pChiTietPhong);

        // ====== PANEL PHẢI ======
        JPanel pRight = new JPanel();
        pRight.setLayout(new BorderLayout(10, 10));
        pRight.setBackground(new Color(249, 249, 249));
        pRight.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15));

        // --- KHUNG DANH SÁCH PHÒNG TRỐNG ---
        pPhongTrong = taoBangPhongTrong(fontTieuDe, maNV);

        // --- KHUNG CHI PHÍ PHÁT SINH ---
        JPanel pChiPhi = taoBangChiPhi(fontTieuDe);

        // --- CHIA TỶ LỆ 60/40 ---
        JSplitPane splitRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pPhongTrong, pChiPhi);
        splitRight.setDividerLocation(0.6); // phòng trống 60%
        splitRight.setResizeWeight(0.6);
        splitRight.setContinuousLayout(true);
        splitRight.setBorder(null);

        pRight.add(splitRight, BorderLayout.CENTER);

        // --- BOTTOM ---
        JPanel pBottom = taoThanhToanCuoi(maNV);

        // ====== GẮN LÊN GIAO DIỆN ======
        splitPane.setLeftComponent(pLeft);
        splitPane.setRightComponent(pRight);

        add(splitPane, BorderLayout.CENTER);
        add(pBottom, BorderLayout.SOUTH);
        
//        btnInPhieu.setBackground(xanhDuong);
//        btnInPhieu.setForeground(Color.WHITE);

        btnXoa.setBackground(doCanhBao);
        btnXoa.setForeground(Color.WHITE);

        btnXoaCP.setBackground(doCanhBao);
        btnXoaCP.setForeground(Color.BLACK);

        btnThemCP.setBackground(nauNhat);
        btnThemCP.setForeground(Color.BLACK);

        btnLuu.setBackground(mauXanhDam);
        btnLuu.setForeground(Color.WHITE);

        btnHuy.setBackground(doCanhBao);
        btnHuy.setForeground(Color.WHITE);

        btnTT.setBackground(mauVangDong);
        btnTT.setForeground(Color.BLACK);

        btnXR.setBackground(nauNhat);
        btnXR.setForeground(Color.BLACK);

        btnCapNhat.setBackground(nauNhat);
        btnCapNhat.setForeground(Color.BLACK);

        btnXNDP.setBackground(nauNhat);
        btnXNDP.setForeground(Color.BLACK);

        btnTPS.setBackground(mauXanhDam);
        btnTPS.setForeground(Color.WHITE);

        btnGoiY.setBackground(mauXanhDam);
        btnGoiY.setForeground(Color.WHITE);
        
        kiemTraLayThongTinKHTuSDT();
        capNhatCBBCPPS();
        hienThiTienCocVaTienTongTienPhong();
        kiemTraDeThemCPPS();
        
//      btn.addActionListener(this);
      btnCapNhat.addActionListener(this);
      btnHuy.addActionListener(this);
      btnInPhieu.addActionListener(this);
      btnLuu.addActionListener(this);
//     btnTinh.addActionListener(this);
      btnThemCP.addActionListener(this);
      btnXR.addActionListener(this);
      btnTT.addActionListener(this);
      btnXoa.addActionListener(this);
      btnXNDP.addActionListener(this);
      btnTPS.addActionListener(this);        
      btnXoaCP.addActionListener(this);
      btnGoiY.addActionListener(this);
    }

  
 
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o= e.getSource();
		if (o.equals(btnInPhieu)&&kiemTraDuLieuNhap()&&dieuKienNguoi()) {
		    if (!ktraThongTinPDP())
		        return;
		    KhachHang checkKH= khd.getKhachHangTheoSDT(txtSDT.getText());
		    if(checkKH==null) {
		    	checkKH= new KhachHang( taoMaKhachHangTuDong(), txtTenKH.getText(), txtSDT.getText(), chkVN.isSelected() );
		    }		 
		    int soNguoiLonVL = (txtNguoiLon.getText().trim().isEmpty()
                    || txtNguoiLon.getText().trim().equals("0"))? 1 : Integer.parseInt(txtNguoiLon.getText().trim());

		    int soTreEmVL = (txtTreEm.getText().trim().isEmpty()
                    || txtTreEm.getText().trim().equals("0"))? 1 : Integer.parseInt(txtTreEm.getText().trim());

		    PhieuDatPhong phieuTam = new PhieuDatPhong(
		       taoMaPhieuDatPhongTuDong(),
		        checkKH,
		        new NhanVien(txtNV.getText()),
		        LocalDate.now(),
		        cboTrangThai.getSelectedItem().toString(),
		        soNguoiLonVL,
		        soTreEmVL
		    );
		    int luaChon = JOptionPane.showConfirmDialog( null, "Bấm Yes để xác nhận tạo phiếu", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
		    if (luaChon == JOptionPane.YES_OPTION) {
		    	xuLyNutInPhieu();
		    	 new InPhieuDatPhong_GUI( phieuTam, (DefaultTableModel) tblPhong.getModel(), checkKH, txtNV.getText(),
				 cboTrangThai.getSelectedItem().toString(), doiTuongTongTienPhong(), Double.parseDouble(txtTienCocMoi.getText()));
		    	txtMPDP.setText(phieuTam.getMaPhieuDatPhong());
		    	txtMKH.setText(phieuTam.getKhachHang().getMaKhachHang());
				 getDSLP();
		    } 

		   
		}

		else if(o.equals(btnXNDP)) {
			if(dieuKienNguoi()) {
				moKhungNhap();
				kiemTraDeThemCPPS();
			}
			
		}
		else if(o.equals(btnThemCP)) {
			themCPPSXB();
			hienThiTienCocVaTienTongTienPhong();
		}
		else if(o.equals(btnXoa)) {
			xoaPhong();
			hienThiTienCocVaTienTongTienPhong();
		}
		else if(o.equals(btnTPS)) {
			    int x = tblPhong.getSelectedRow();
			    if (x == -1) {
			        JOptionPane.showMessageDialog(null, "Vui lòng chọn phòng cần trả sớm!");
			        return;
			    }
			    if(dlp.getRowCount()==1) {
					JOptionPane.showMessageDialog(null,"Phiếu đặt phòng này chỉ 1 phòng bạn hãy bấm nút hủy đặt phòng!");
					return;
				}
			    String maPhongTam= dlp.getValueAt(x, 1).toString();
//			    new TraPhong_GUI(txtMPDP.getText(), maPhongTam);
			    new TraPhong_GUI(txtMPDP.getText(), maPhongTam, (DefaultTableModel) tblPhong.getModel(), x); 
			    hienThiTienCocVaTienTongTienPhong();
		}else if(o.equals(btnXoaCP)) {
			xoaCPPSXB();
		}else if(o.equals(btnLuu)) {
			if( kiemTraDuLieuNhap()&&dieuKienNguoi()&&kiemTraTrangThai(pdp.timPhieuDatPhongTheoMa(txtMPDP.getText().trim()))) {
				if(xuLyNutLuu()) {
					kiemTraDeThemCPPS();
					JOptionPane.showMessageDialog(null,"Lưu thành công!");
					getDSLP();
					return;
				}else {
					JOptionPane.showMessageDialog(null,"Lưu thất bại!");
					return;
				}				
				
			}
			
		}
		else if(o.equals(btnXR)) {
			String mpdp= txtMPDP.getText();
			if(mpdp.trim().length()!=0) {
				moKhoaTatCaTruong();
				txtMPDP.setText("");
				txtMKH.setText("");
				txtTenKH.setText("");
				txtTenKH.setEditable(false);
				txtSDT.setText("");
				txtSDT.setEditable(false);
				txtNgayTao.setText(LocalDate.now().toString());
				dlp.setRowCount(0);
				dlctps.setRowCount(0);
				soNguoiLon= 0;
			   	soTreEm= 0;
			    txtSoNguoiThuc.setText("0");  
			    kiemTraDeThemCPPS();
				
			}
		}else if (o.equals(btnGoiY)) {
		    List<Phong> dsPhongTrong = layDanhSachPhongTrong();
		    int soNguoi = 0;
		    try {
		        soNguoi = Integer.parseInt(txtSoNguoiThuc.getText().trim())==0? 1: Integer.parseInt(txtSoNguoiThuc.getText().trim());
		    } catch (Exception e2) {
		        JOptionPane.showMessageDialog(null, "Vui lòng nhập số người thực hợp lệ!");
		        return;
		    }
		    if(dsPhongTrong!=null) {
		    	 GoiYPhong_GUI goiY = new GoiYPhong_GUI(dsPhongTrong, soNguoi);
				    goiY.setVisible(true);
		    }
		   
		}
		else if(o.equals(btnTT)) {
			if(kiemTraDuLieuNhap()&&dieuKienNguoi()&&dieuKienThanhToan()) {
				 int luaChon = JOptionPane.showConfirmDialog( null, "Bạn có chắn thanh toán không", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
				    if (luaChon == JOptionPane.YES_OPTION) {
				    	cboTrangThai.setSelectedItem("Hoàn thành");
				    	xuLyNutLuu();
				    	
				    	String maPDP = txtMPDP.getText();

				    	ThanhToanFrame_GUI f = new ThanhToanFrame_GUI(
				    	    maPDP,
				    	    txtTenKH.getText(),
				    	    txtNV.getText(),
				    	    convertTableModelToArray(tblPhong),
				    	    convertTableModelToArray(tblChiPhi),
				    	    lblTongTien.getText().split(":")[1].trim(),
				    	    lblTongCP.getText().split(":")[1].trim()
				    	);

				    	f.setVisible(true);	
				    	khoaTatCaTruong();
				    	
				    	
				    }
			}
		}else if (o.equals(btnHuy)) {
		    if (!kiemTraDuLieuNhap()) return;
		    int chon = JOptionPane.showConfirmDialog(
		            null,
		            "Bạn có chắc chắn muốn hủy đặt phòng?",
		            "Xác nhận",
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE
		    );
		    if (chon == JOptionPane.YES_OPTION) {
		        huyDatPhong();  
		       PhieuDatPhong p= doiTuongTongTienPhong();
		       KhuyenMai km= new KhuyenMai("Không");
		       hdd.themHoaDon(taoMaHoaDon(), txtMPDP.getText(), null, "Chuyển khoản", p.getTongTienPhong(), 0.0, p.getTongTien(), LocalDate.now());
		       
		    }
		}
		else if(o.equals(btnCapNhat)) {
		        	if(xuLyChonNgay()) {
		        		 getDSLP();
		                 pPhongTrong.revalidate();
		                 pPhongTrong.repaint();
		        	}
		}
	}
	public void xoaPhong() {
	    int x = tblPhong.getSelectedRow();
	    if (x == -1) {
	        JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng cần xóa!");
	        return;
	    }

	    String maPhong = dlp.getValueAt(x, 1).toString();

	    // Lấy danh sách chi tiết phiếu từ DB
	    List<ChiTietPhieuDatPhong> ds = dsctpdp.getChiTietTheoMaPhieu(txtMPDP.getText());

	    for (ChiTietPhieuDatPhong ct : ds) {
	        if (ct.getPhong() == null) continue;

	        if (maPhong.equals(ct.getPhong().getMaPhong())) {
	            JOptionPane.showMessageDialog(
	                    null,
	                    "Phòng này đã có trong hệ thống,\n" +
	                    "không thể xóa.\n" +
	                    "Bạn có thể chọn trả phòng sớm!",
	                    "Không thể xóa",
	                    JOptionPane.WARNING_MESSAGE
	            );
	            return;
	        }
	    }

	    // Nếu KHÔNG có trong DB → cho xóa
	    dlp.removeRow(x);
	    capNhatSoThuTu();
	    hienThiTienCocVaTienTongTienPhong();

	    JOptionPane.showMessageDialog(null, "Xóa thành công!");
	}

	
	private void capNhatSoThuTu() {
	    for (int i = 0; i < dlp.getRowCount(); i++) {
	        dlp.setValueAt(i + 1, i, 0); 
	    }
	}

	public boolean ktraThongTinPDP() {
		String tenKH= txtTenKH.getText();
		String sdt= txtSDT.getText();
		if(sdt.trim().length()==0) {
			JOptionPane.showMessageDialog(null,"Vui lòng nhập số điện thoại khách hàng!");
			txtSDT.requestFocus();
			return false;
		}
		if(tenKH.trim().length()==0) {
			JOptionPane.showMessageDialog(null,"Vui lòng nhập tên khách hàng!");
			txtTenKH.requestFocus();
			return false;
		}
		return true;
	}
	
	// ========================== KIỂM TRA DỮ LIỆU NHẬP ==========================
    public boolean kiemTraDuLieuNhap() {
        if (dlp.getRowCount()==0) {
            JOptionPane.showMessageDialog(null, "Phiếu chỉ được tạo chỉ khi phòng được thêm!");
            return false;
        }
        if(tblPhong.getRowCount()==-1) {
        	Date ngayNhan = dateNgayNhan.getDate();
            Date ngayTra = dateNgayTra.getDate();

            // Kiểm tra đã chọn ngày chưa
            if (ngayNhan == null) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày nhận!");
                return false;
            }
            if (ngayTra == null) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày trả!");
                return false;
            }

            // Kiểm tra logic ngày
            if (ngayTra.before(ngayNhan)) {
                JOptionPane.showMessageDialog(null, "Ngày trả không thể trước ngày nhận!");
                return false;
            }
        }
        if(txtSDT.getText().trim().length()==0) {
        	JOptionPane.showMessageDialog(null,"Vui lòng nhạp số điện thoại!");
        	return false;
        }
        
        String sdt = txtSDT.getText().trim();
        if (!sdt.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Số điện thoại chỉ được chứa chữ số!");
            return false;
        } else if (sdt.length() != 10) {
            JOptionPane.showMessageDialog(null, "Số điện thoại phải có đúng 10 số!");
            return false;
        }
         if(txtTenKH.getText().trim().length()==0) {
        	 JOptionPane.showMessageDialog(null,"Vui lòng nhập tên khách hàng!");
        	  txtTenKH.requestFocus();
        	  return false;
         }
        
        for (KhachHang khh : khd.getAllKhachHang()) {
            if (!khh.getMaKhachHang().equals(txtMKH.getText()) &&
                khh.getSoDienThoai().equals(txtSDT.getText())) {
                JOptionPane.showMessageDialog(null, "Số điện thoại này đã tồn tại cho khách hàng khác!");
                txtSDT.requestFocus();
                return false;
            }
        }

        return true;
    }

	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
	
	   // ====== KHUNG CHI PHÍ PHÁT SINH ======
    private JPanel taoBangChiPhi(Font f) {
        JPanel pChiPhi = new JPanel(new BorderLayout());
        pChiPhi.setBackground(Color.WHITE);
        pChiPhi.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(mauXanhDam, 1, true),
                "Chi phí phát sinh",
                TitledBorder.LEFT, TitledBorder.TOP, f, mauXanhDam));

        JPanel pChiPhiTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pChiPhiTop.setBackground(Color.WHITE);
        cboChiPhi = new JComboBox<>();
        btnThemCP = new JButton("Thêm");
        btnXoaCP = new JButton("Xóa");
        pChiPhiTop.add(new JLabel("Loại chi phí:"));
        pChiPhiTop.add(cboChiPhi);
        pChiPhiTop.add(btnThemCP);
        pChiPhiTop.add(btnXoaCP);
        pChiPhi.add(pChiPhiTop, BorderLayout.NORTH);

        dlctps = new DefaultTableModel(
                new String[]{"Tên chi phí", "Giá", "Số lượng", "Thành tiền", "Mã"}, 0) {
        	
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tblChiPhi = new JTable(dlctps);
        tblChiPhi.getColumnModel().getColumn(4).setMinWidth(0);
        tblChiPhi.getColumnModel().getColumn(4).setMaxWidth(0);
        tblChiPhi.getColumnModel().getColumn(4).setWidth(0);
        pChiPhi.add(new JScrollPane(tblChiPhi), BorderLayout.CENTER);

        lblTongCP = new JLabel("Tổng chi phí: 0 VNĐ", SwingConstants.RIGHT);
        lblTongCP.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        pChiPhi.add(lblTongCP, BorderLayout.SOUTH);

        return pChiPhi;
    }

    private JPanel taoThanhToanCuoi(String maNV) {
        JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pBottom.setBackground(new Color(249, 249, 249));
        JLabel lblNV = new JLabel("Nhân viên: ");
        txtNV = new JTextField(maNV);
        txtNV.setEditable(false);
        btnLuu = new JButton("💾 Lưu");
        btnHuy = new JButton("❌ Hủy đặt phòng");
        btnTT = new JButton("💳 Thanh toán");
        btnXR = new JButton("Tạo phiếu mới");
        lblTongTatCa = new JLabel("Tổng thanh toán: 0 VNĐ");
        pBottom.add(lblNV);
        pBottom.add(txtNV);
        pBottom.add(btnLuu);
        pBottom.add(btnHuy);
        pBottom.add(btnTT);
        pBottom.add(btnXR);
        pBottom.add(lblTongTatCa);
        return pBottom;
    }

    
    
    
    // ====== CÁC HÀM HỖ TRỢ ======
    private String taoMaPhieuDatPhongTuDong() {

    	 // Lấy ngày hiện tại
        LocalDate ngayHienTai = LocalDate.now();
        String ngay = String.format("%02d", ngayHienTai.getDayOfMonth());
        String thang = String.format("%02d", ngayHienTai.getMonthValue());
        String nam = String.valueOf(ngayHienTai.getYear());

        // Lấy danh sách phiếu hiện có (từ database hoặc list)
        List<PhieuDatPhong> danhSachPhieu = pdp.getAllPhieuDatPhong();

        // Đếm số phiếu trong ngày hiện tại
        int dem = 0;
        for (PhieuDatPhong p : danhSachPhieu) {
            if (p.getMaPhieuDatPhong().contains("PDP" + ngay + thang + nam)) {
                dem++;
            }
        }

        // Tăng số thứ tự lên 1
        dem++;

        // Ghép lại chuỗi mã theo định dạng
        String maPhieu = String.format("PDP%s%s%s%03d", ngay, thang, nam, dem);
        return maPhieu; 

    }

    
    
    private String taoMaKhachHangTuDong() {
    	 // Lấy ngày hiện tại
        LocalDate ngayHienTai = LocalDate.now();
        String ngay = String.format("%02d", ngayHienTai.getDayOfMonth());
        String thang = String.format("%02d", ngayHienTai.getMonthValue());
        String nam = String.valueOf(ngayHienTai.getYear());

        // Lấy danh sách khách hàng hiện có (từ database hoặc DAO)
        List<KhachHang> danhSachKhachHang = khd.getAllKhachHang();

        // Đếm số khách hàng tạo trong ngày hiện tại
        int dem = 0;
        for (KhachHang kh : danhSachKhachHang) {
            if (kh.getMaKhachHang().contains("KH" + ngay + thang + nam)) {
                dem++;
            }
        }

        // Tăng số thứ tự
        dem++;

        // Ghép chuỗi mã theo định dạng KHddMMyyyy###
        String maKH = String.format("KH%s%s%s%03d", ngay, thang, nam, dem);
        return maKH;


    }


    public void getDSLP() {

        // ===== 1. LẤY NGÀY =====
        Date dNhan = dateNgayNhan.getDate();
        Date dTra  = dateNgayTra.getDate();

        if (dNhan == null || dTra == null) {
            return;
        }

        LocalDate ngayNhanMoi = dNhan.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate ngayTraMoi = dTra.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

//        if (!ngayTraMoi.isAfter(ngayNhanMoi)) {
//            JOptionPane.showMessageDialog(
//                    null,
//                    "Ngày trả phải lớn hơn ngày nhận"
//            );
//            return;
//        }

        // ===== 2. CLEAR UI =====
        pPhongTrong.removeAll();
        pPhongTrong.setLayout(new BorderLayout());
        pPhongTrong.add(pTop, BorderLayout.NORTH);

        JPanel allSections = new JPanel();
        allSections.setLayout(new BoxLayout(allSections, BoxLayout.Y_AXIS));
        allSections.setBackground(Color.WHITE);

        // ===== 3. LẤY DỮ LIỆU =====
        List<LoaiPhong> dsLoaiPhong = dslp.getAllLoaiPhong();

        // DAO xử lý trùng ngày + trạng thái
        List<Phong> dsPhongTrong =
                dsp.getDSPhongTrongTheoNgay(ngayNhanMoi, ngayTraMoi);

        // ===== 4. GROUP THEO LOẠI =====
        Map<String, List<Phong>> mapPhongTheoLoai = new HashMap<>();
        for (Phong p : dsPhongTrong) {
            mapPhongTheoLoai
                    .computeIfAbsent(
                            p.getLoaiPhong().getMaLoaiPhong(),
                            k -> new ArrayList<>()
                    )
                    .add(p);
        }

        // ===== 5. RENDER =====
        for (LoaiPhong lp : dsLoaiPhong) {

            List<Phong> dsPhongTheoLoai =
                    mapPhongTheoLoai.get(lp.getMaLoaiPhong());

            if (dsPhongTheoLoai == null || dsPhongTheoLoai.isEmpty())
                continue;

            List<String> danhSachPhong = new ArrayList<>();
            for (Phong p : dsPhongTheoLoai) {
                danhSachPhong.add(p.getMaPhong());
            }

            String title =
                    lp.getTenLoaiPhong()
                    + " - " + lp.getGia()
                    + " VND / 1 đêm";

            JPanel section = createRoomSection(
                    title,
                    danhSachPhong,
                    mauXanhDam,
                    mauVangDong
            );

            section.setBorder(
                    BorderFactory.createEmptyBorder(6, 6, 6, 6)
            );

            allSections.add(section);
        }

        // ===== 6. SCROLL =====
        JScrollPane jsp = new JScrollPane(
                allSections,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        jsp.getVerticalScrollBar().setUnitIncrement(12);
        jsp.setBorder(null);
        jsp.getViewport().setBackground(Color.WHITE);

        pPhongTrong.add(jsp, BorderLayout.CENTER);
        pPhongTrong.revalidate();
        pPhongTrong.repaint();
    }

    private boolean xuLyChonNgay() {
        // Lấy ngày nhận và ngày trả
        Date ngayNhan = dateNgayNhan.getDate();
        Date ngayTra = dateNgayTra.getDate();

        // Nếu chưa chọn đủ 2 ngày
        if (ngayNhan == null || ngayTra == null) {
        	JOptionPane.showMessageDialog(null,
                    "Vui lòng chọn ngày nhận và ngày trả đúng trước khi chọn phòng!",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Lấy ngày hiện tại (bỏ phần giờ để so sánh chuẩn)
        LocalDate homNay = LocalDate.now();
        LocalDate nhan = ngayNhan.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate tra = ngayTra.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 
        if (nhan.isBefore(homNay)) {
            JOptionPane.showMessageDialog(this,
                "Ngày nhận phải từ hôm nay trở đi!",
                "Lỗi chọn ngày",
                JOptionPane.ERROR_MESSAGE);
            dateNgayNhan.setDate(null);
            return false;
        }

        // 
        if (!tra.isAfter(nhan)) {
            JOptionPane.showMessageDialog(this,
                "Ngày trả phải lớn hơn ngày nhận!",
                "Lỗi chọn ngày",
                JOptionPane.ERROR_MESSAGE);
            dateNgayTra.setDate(null);
            return false;
        }

        //
        long khoangCach = ChronoUnit.DAYS.between(nhan, tra);
        if (khoangCach > 31) {
            JOptionPane.showMessageDialog(this,
                "Khách sạn chỉ cho phép đặt tối đa 31 ngày!",
                "Lỗi chọn ngày",
                JOptionPane.ERROR_MESSAGE);
            dateNgayTra.setDate(null);
            return false;
        }
        return true;
    }



    private JPanel createRoomSection(String title, List<String> dsMaPhong, Color mauXanhDam, Color mauVangDong) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);

        // ====== Tiêu đề loại phòng ======
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(mauXanhDam);
        lbl.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        p.add(lbl, BorderLayout.NORTH);

        // ====== Grid chứa các phòng ======
        JPanel grid = new JPanel(new GridLayout(0, 8, 8, 8)); // 8 cột, khoảng cách 8px
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // ====== Tạo nút cho từng mã phòng ======
        for (String maPhong : dsMaPhong) {
            JButton btnPhong = new JButton(maPhong);
            btnPhong.setBackground(Color.WHITE);
            btnPhong.setFocusPainted(false);
            btnPhong.setBorder(BorderFactory.createLineBorder(mauXanhDam, 1, true));
            btnPhong.setForeground(mauXanhDam);
            btnPhong.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnPhong.setMargin(new Insets(2, 2, 2, 2));

            btnPhong.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (!dsPhongDaChon.contains(maPhong)) {
                        btnPhong.setBackground(mauVangDong);
                        btnPhong.setForeground(Color.WHITE);
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (!dsPhongDaChon.contains(maPhong)) {
                        btnPhong.setBackground(Color.WHITE);
                        btnPhong.setForeground(mauXanhDam);
                    }
                }
            });

            //  Sự kiện khi người dùng click vào phòng
            btnPhong.addActionListener(e -> {
            	if (dsPhongDaChon.contains(maPhong)) {
                    JOptionPane.showMessageDialog(null,
                        "Phòng này đã được chọn!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Phong phong = dsp.timPhongTheoMa(maPhong);
                if (phong == null) return;
                

                // ⚠️ Kiểm tra ngày nhận & ngày trả đã được chọn chưa
                if (dateNgayNhan.getDate() == null || dateNgayTra.getDate() == null) {
                    JOptionPane.showMessageDialog(null,
                        "Vui lòng chọn ngày nhận và ngày trả đúng trước khi chọn phòng!",
                        "Thiếu thông tin",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Chuyển sang LocalDate
                LocalDate ngayNhan = dateNgayNhan.getDate()
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate ngayTra = dateNgayTra.getDate()
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                // Kiểm tra hợp lệ ngày nhận - ngày trả
                if (ngayTra.isBefore(ngayNhan)) {
                    JOptionPane.showMessageDialog(null,
                        "Ngày trả phải sau ngày nhận!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kiểm tra trùng phòng trong bảng
                for (int i = 0; i < dlp.getRowCount(); i++) {
                    String ma = dlp.getValueAt(i, 1).toString(); // cột Mã phòng
                    if (ma.equals(maPhong)) {
                        JOptionPane.showMessageDialog(null,
                            "Phòng này đã được chọn!",
                            "Thông báo",
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if(dlp.getRowCount()>11) {
                    	JOptionPane.showMessageDialog(null,"Chỉ được đặt tối đa 12 phòng");
                    	return;
                    }
                    if(!xuLyChonNgay()) return;
                }

                //  Tính số đêm ở và thành tiền
                long soDemO = ChronoUnit.DAYS.between(ngayNhan, ngayTra);
                double gia = phong.getLoaiPhong().getGia();
                double thanhTien = soDemO * gia;
                
                dlp.addRow(new Object[]{
                    dlp.getRowCount() + 1,                 // STT
                    phong.getMaPhong(),                    // Mã phòng
                    phong.getLoaiPhong().getTenLoaiPhong(),// Loại phòng
                    ngayNhan,                              // Ngày nhận
                    ngayTra,                               // Ngày trả
                    ngayTra,
                    soDemO,                                // Số đêm ở
                    gia,                                   // Giá/đêm
                    thanhTien                              // Thành tiền
                });
                dsPhongDaChon.add(maPhong);
                btnPhong.setBackground(MAU_PHONG_DA_CHON);
                btnPhong.setForeground(mauXanhDam);
               
                hienThiTienCocVaTienTongTienPhong();

            });

            grid.add(btnPhong);
        }

        // ====== Thêm grid vào panel ======
        p.add(grid, BorderLayout.CENTER);
        return p;
    }
     
    
    // ====== KHUNG DANH SÁCH PHÒNG TRỐNG ======
    private JPanel taoBangPhongTrong(Font f, String maNV) {
        JPanel pPhongTrong = new JPanel(new BorderLayout());
        pPhongTrong.setBackground(Color.WHITE);
        pPhongTrong.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(mauXanhDam, 1, true),
                "Danh sách phòng trống",
                TitledBorder.LEFT, TitledBorder.TOP, f, mauXanhDam));

         pTop = new JPanel();
        pTop.setLayout(new BoxLayout(pTop, BoxLayout.Y_AXIS));
        pTop.setBackground(Color.WHITE);

        // --- Hàng 1: Ngày nhận & Ngày trả ---
         pHang1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pHang1.setBackground(Color.WHITE);
        pHang1.add(new JLabel("Ngày nhận:"));
        dateNgayNhan = new JDateChooser();
        dateNgayNhan.setPreferredSize(new Dimension(120, 26));
        dateNgayNhan.setDateFormatString("yyyy/MM/dd");
        pHang1.add(dateNgayNhan);
        pHang1.add(new JLabel("Ngày trả:"));
        dateNgayTra = new JDateChooser();
        dateNgayTra.setPreferredSize(new Dimension(120, 26));
        dateNgayTra.setDateFormatString("yyyy/MM/dd");
        pHang1.add(dateNgayTra);

        // --- Hàng 2: Số lượng người ---
        pHang2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pHang2.setBackground(Color.WHITE);
        pHang2.add(new JLabel("Số lượng người:"));
        txtSoNguoiThuc = new JTextField("0");
        txtSoNguoiThuc.setPreferredSize(new Dimension(50, 26));
        txtSoNguoiThuc.setEditable(false);
        pHang2.add(txtSoNguoiThuc);

        JButton btnThemNguoi = new JButton("+");
        btnThemNguoi.setFocusPainted(false);
        btnThemNguoi.setBackground(mauVangDong);
        btnThemNguoi.setForeground(mauXanhDam);
        btnThemNguoi.setPreferredSize(new Dimension(45, 26));
        pHang2.add(btnThemNguoi);

        btnCapNhat = new JButton("🔄 Cập nhật");
        btnGoiY    = new JButton("💡 Gợi ý phòng");
        btnCapNhat.setFocusPainted(false);
        btnCapNhat.setBackground(mauVangDong);
        btnCapNhat.setForeground(mauXanhDam);
        btnGoiY.setFocusPainted(false);
        btnGoiY.setBackground(mauVangDong);
        btnGoiY.setForeground(mauXanhDam);
        pHang2.add(Box.createHorizontalStrut(20));
        pHang2.add(btnCapNhat);
        pHang2.add(Box.createHorizontalStrut(15));
        pHang2.add(btnGoiY);


        pTop.add(pHang1);
        pTop.add(pHang2);

         lblThongBao = new JLabel("Vui lòng chọn ngày nhận và ngày trả để xem phòng trống.", SwingConstants.CENTER);
        lblThongBao.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        lblThongBao.setForeground(Color.GRAY);
        lblThongBao.setBorder(BorderFactory.createEmptyBorder(40, 10, 40, 10));

        pPhongTrong.add(pTop, BorderLayout.NORTH);
        pPhongTrong.add(lblThongBao, BorderLayout.CENTER);

        // ====== SỰ KIỆN ======
//        btnCapNhat.addActionListener(e -> {
//        	if(xuLyChonNgay()) {
//        		 getDSLP();
//                 pPhongTrong.revalidate();
//                 pPhongTrong.repaint();
//        	}
//           
//        });

        btnThemNguoi.addActionListener(e -> {
            JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nhập số người", true);
            dlg.setSize(300, 200);
            dlg.setLocationRelativeTo(this);
            dlg.setLayout(new GridBagLayout());
            dlg.getContentPane().setBackground(Color.WHITE);

            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(6, 8, 6, 8);
            g.fill = GridBagConstraints.HORIZONTAL;

            JLabel lblNguoiLon = new JLabel("Số người lớn:");
            txtNguoiLon.setText(String.valueOf(soNguoiLon));

            JLabel lblTreEm = new JLabel("Số trẻ em:");
            txtTreEm.setText(String.valueOf(soTreEm));

            JLabel lblThucTe = new JLabel("Số người ở thực:");
            txtThucTe = new JTextField("0");
            txtThucTe.setEditable(false);

            JButton btnOK = new JButton("Xác nhận");
            btnOK.setBackground(mauVangDong);
            btnOK.setForeground(mauXanhDam);

            g.gridx = 0; g.gridy = 0; dlg.add(lblNguoiLon, g);
            g.gridx = 1; dlg.add(txtNguoiLon, g);
            g.gridx = 0; g.gridy = 1; dlg.add(lblTreEm, g);
            g.gridx = 1; dlg.add(txtTreEm, g);
            g.gridx = 0; g.gridy = 2; dlg.add(lblThucTe, g);
            g.gridx = 1; dlg.add(txtThucTe, g);
            g.gridx = 1; g.gridy = 3; dlg.add(btnOK, g);

            // ==========================
            // LISTENER TÍNH LẠI TỰ ĐỘNG
            // ==========================
            DocumentListener listener = new DocumentListener() {
                private void update() {
                    try {
                        int nl = Integer.parseInt(txtNguoiLon.getText());
                        int te = Integer.parseInt(txtTreEm.getText());
                        int thuc = nl + (int) Math.ceil(te / 2.0);
                        txtThucTe.setText(String.valueOf(thuc));
                    } catch (NumberFormatException ex) {
                        txtThucTe.setText("0");
                    }
                }
                public void insertUpdate(DocumentEvent e) { update(); }
                public void removeUpdate(DocumentEvent e) { update(); }
                public void changedUpdate(DocumentEvent e) { update(); }
            };

            txtNguoiLon.getDocument().addDocumentListener(listener);
            txtTreEm.getDocument().addDocumentListener(listener);

            // ⭐⭐ QUAN TRỌNG: GỌI UPDATE() KHI MỞ LẠI DIALOG ⭐⭐
            SwingUtilities.invokeLater(() -> {
                listener.insertUpdate(null);
            });

            btnOK.addActionListener(ev -> {

                String nl = txtNguoiLon.getText().trim();
                String te = txtTreEm.getText().trim();

                int nguoiLon;
                int treEm;

                // 1. Người lớn: rỗng → 1
                if (nl.isEmpty()) {
                    nguoiLon = 1;
                } else {
                    try {
                        nguoiLon = Integer.parseInt(nl);
                    } catch (NumberFormatException a) {
                        JOptionPane.showMessageDialog(
                            dlg,
                            "Số người lớn phải là số!",
                            "Lỗi nhập liệu",
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                }

                // 2. Trẻ em: rỗng → 0
                if (te.isEmpty()) {
                    treEm = 0;
                } else {
                    try {
                        treEm = Integer.parseInt(te);
                    } catch (NumberFormatException b) {
                        JOptionPane.showMessageDialog(
                            dlg,
                            "Số trẻ em phải là số!",
                            "Lỗi nhập liệu",
                            JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                }

             // 3. Không cho số âm
                if (nguoiLon < 0 || treEm < 0) {
                    JOptionPane.showMessageDialog(
                        dlg,
                        "Số người không được âm!",
                        "Dữ liệu không hợp lệ",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // 🔹 Trẻ em tính 1/2 người, làm tròn LÊN
                int treEmDaTinh = (int) Math.ceil(treEm / 2.0);

                // 🔹 Tổng số người ở thực
                int tong = nguoiLon + treEmDaTinh;

                // 4. Giới hạn khách sạn
                if (tong > 35) {
                    JOptionPane.showMessageDialog(
                        dlg,
                        "Tổng số người tối đa là 35!",
                        "Quá giới hạn",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // 5. OK
                soNguoiLon = nguoiLon;
                soTreEm    = treEm;
                txtSoNguoiThuc.setText(String.valueOf(tong));

                dlg.dispose();

            });


            dlg.setVisible(true);
        });

        return pPhongTrong;
    }
    public boolean dieuKienNguoi() {
    	int sln= Integer.parseInt(txtSoNguoiThuc.getText());
    	int tongSucChua=0;
    	for(int i=0;i<dlp.getRowCount();i++) {
    		Phong tam= dsp.timPhongTheoMa(dlp.getValueAt(i, 1).toString());
    		tongSucChua+=tam.getLoaiPhong().getSucChua();
    	}
    	
    	if(tongSucChua<sln) {
    		JOptionPane.showMessageDialog(null,"Sức chứa của phòng không đủ!"
    				+ "Còn thiếu "+ (sln-tongSucChua)+ " người!" );
    		return false;
    	}
    	return true;
    }
    public void moKhungNhap() {
    	int i= dlp.getRowCount();
    	if(i>0) {
//    		if(txtMPDP.getText().trim().equals("")) {
//    			txtMPDP.setText(taoMaPhieuDatPhongTuDong());
//    		}
    		txtMPDP.setText("");
    	KhachHang tam= khd.getKhachHangTheoSDT(txtSDT.getText().trim());
    	if(tam==null) {
    		txtMKH.setText("");
    	}else {
    		txtMKH.setText(tam.getMaKhachHang());
    	}  
    	KhachHang t= khd.getKhachHangTheoMa(txtMKH.getText().trim());
    	if(t==null) {
    		txtMKH.setText("");
    	}else {
    		txtMKH.setText(tam.getMaKhachHang());
    	}  
    	txtSDT.setEditable(true);
    	txtTenKH.setEditable(true);
    	btnInPhieu.setEnabled(true);
    	}
    	else JOptionPane.showMessageDialog(null,"Vui lòng chọn phòng trước khi xác nhận");
    	
    }

    public void kiemTraLayThongTinKHTuSDT() {
    	txtSDT.addFocusListener(new FocusAdapter() {
    	    @Override
    	    public void focusLost(FocusEvent e) {
    	        String sdt = txtSDT.getText().trim();
    	        if (sdt.isEmpty()) return;

    	        KhachHang kh = khd.getKhachHangTheoSDT(sdt);
    	        if (kh != null) {
    	            txtMKH.setText(kh.getMaKhachHang());
    	            txtTenKH.setText(kh.getHoTen());
    	            // Nếu bạn có checkbox hoặc combobox cho quốc tịch:
    	            chkVN.setSelected(kh.LaNguoiVietNam());
    	        } 
    	    }
    	});


    }
   
    
    public void capNhatCBBCPPS() {
    	List<ChiPhiPhatSinh> dscpps= cppsd.getAllChiPhiPhatSinh();
    	for(ChiPhiPhatSinh tam: dscpps) {
    		cboChiPhi.addItem(tam.getTenChiPhiPhatSinh() +"-"+ tam.getGia());
    	}
    }
    
    public void themCPPSXB() {
        // Lấy mục được chọn trong combobox
        String mucChon = (String) cboChiPhi.getSelectedItem();
        if (mucChon == null || mucChon.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn chi phí cần thêm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cắt tên chi phí và giá (định dạng: "TênChiPhi-Giá")
        String[] tach = mucChon.split("-");
        if (tach.length != 2) {
            JOptionPane.showMessageDialog(null, "Dữ liệu chi phí không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tenChiPhi = tach[0].trim();
        double gia = 0;
        try {
            gia = Double.parseDouble(tach[1].trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Giá chi phí không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Nhập số lượng
        String soLuongStr = JOptionPane.showInputDialog(null, "Nhập số lượng cho " + tenChiPhi + ":");
        if (soLuongStr == null) return; // người dùng bấm Cancel

        int soLuongMoi;
        try {
            soLuongMoi = Integer.parseInt(soLuongStr);
            if (soLuongMoi <= 0) {
                JOptionPane.showMessageDialog(null, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Số lượng phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra trùng chi phí trong bảng
        for (int i = 0; i < dlctps.getRowCount(); i++) {
            String ten = dlctps.getValueAt(i, 0).toString();
            if (ten.equalsIgnoreCase(tenChiPhi)) {
                // Nếu trùng thì cập nhật số lượng
                int soLuongHienTai = Integer.parseInt(dlctps.getValueAt(i, 2).toString());
                int tongSoLuong = soLuongHienTai + soLuongMoi;

                if (tongSoLuong > 50) {
                    JOptionPane.showMessageDialog(null,
                        "Tổng số lượng của " + tenChiPhi + " không được vượt quá 50!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Cập nhật lại số lượng và thành tiền
                double thanhTienMoi = gia * tongSoLuong;
                dlctps.setValueAt(tongSoLuong, i, 2);
                dlctps.setValueAt(thanhTienMoi, i, 3);

//                JOptionPane.showMessageDialog(null, "Đã cập nhật lại số lượng của " + tenChiPhi + " thành " + tongSoLuong + ".");
                return;
            }
        }

        // Nếu chưa có, thêm mới dòng
        if (soLuongMoi > 50) {
            JOptionPane.showMessageDialog(null, "Số lượng không được vượt quá 50!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ChiPhiPhatSinh tam= cppsd.getChiPhiTheoTen(tenChiPhi.trim());
        double thanhTien = gia * soLuongMoi;
        Object[] dongMoi = { tenChiPhi, gia, soLuongMoi, thanhTien,tam.getMaChiPhiPhatSinh()};
        dlctps.addRow(dongMoi);
    }

    public void xoaCPPSXB() {
        int row = tblChiPhi.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng để xoá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy dữ liệu hiện tại
        String tenChiPhi = dlctps.getValueAt(row, 0).toString();
        double gia = Double.parseDouble(dlctps.getValueAt(row, 1).toString());
        int soLuongHienTai = Integer.parseInt(dlctps.getValueAt(row, 2).toString());
        String soLuongStr = JOptionPane.showInputDialog(null,
                "Nhập số lượng muốn xoá (tối đa " + soLuongHienTai + "):");

        if (soLuongStr == null) return; // user bấm cancel

        int soLuongXoa;
        try {
            soLuongXoa = Integer.parseInt(soLuongStr);
            if (soLuongXoa <= 0) {
                JOptionPane.showMessageDialog(null, "Số lượng phải là số nguyên dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (soLuongXoa > soLuongHienTai) {
                JOptionPane.showMessageDialog(null,
                        "Không được xoá quá số lượng hiện tại (" + soLuongHienTai + ")!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Số lượng phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Xử lý xoá
        int soLuongConLai = soLuongHienTai - soLuongXoa;

        if (soLuongConLai == 0) {
            // Xoá cả dòng
            dlctps.removeRow(row);
        } else {
            // Cập nhật số lượng và thành tiền
            double thanhTienMoi = soLuongConLai * gia;
            dlctps.setValueAt(soLuongConLai, row, 2);
            dlctps.setValueAt(thanhTienMoi, row, 3);
        }

        // JOptionPane.showMessageDialog(null, "Đã xoá số lượng thành công!");
    }

    public PhieuDatPhong doiTuongTongTienPhong() {
        PhieuDatPhong tam = new PhieuDatPhong();
    	KhachHang kh= khd.getKhachHangTheoMa(txtMKH.getText());
    	
    	if(kh!=null) {   		   
            tam.setKhachHang(kh);
    	}
 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < dlp.getRowCount(); i++) {
            Object oNhan = dlp.getValueAt(i, 3);
            Object oTraThuc  = dlp.getValueAt(i, 4);
            Object oTra = dlp.getValueAt(i, 5);
            Object oMaPhong = dlp.getValueAt(i, 1);
            // Chặn null 
            if (oNhan == null || oTra == null || oTraThuc == null || oMaPhong == null) {
                System.out.println("Dòng " + i + " có ô dữ liệu rỗng → bỏ qua.");
                continue;
            }

            LocalDate ngayNhan = LocalDate.parse(oNhan.toString(), formatter);
            LocalDate ngayTra  = LocalDate.parse(oTra.toString(), formatter);
            LocalDate ngayTraThuc = LocalDate.parse(oTraThuc.toString(), formatter);

            String maPhong = oMaPhong.toString().trim();
            Phong p = dsp.timPhongTheoMa(maPhong);

            if (p == null) {
                System.out.println("Không tìm thấy phòng: " + maPhong);
                JOptionPane.showMessageDialog(null, "Không tìm thấy phòng: " + maPhong);
                continue;
            }

            String trangThai = String.valueOf(cboTrangThai.getSelectedItem());

            ChiTietPhieuDatPhong ct = new ChiTietPhieuDatPhong(
                tam, p, ngayNhan, ngayTraThuc, ngayTra
            );

            tam.themChiTiet(ct);
        }
        for (int i = 0; i < dlctps.getRowCount(); i++) {

            Object oMaCP = dlctps.getValueAt(i, 4);
            Object oSoLuong = dlctps.getValueAt(i, 2);

            if (oMaCP == null || oSoLuong == null) {
                System.out.println("Chi phí dòng " + i + " bị thiếu dữ liệu → bỏ qua.");
                continue;
            }

            ChiPhiPhatSinh cpps = cppsd.getChiPhiTheoMa(oMaCP.toString());
            if (cpps == null) {
                System.out.println("Không tìm thấy chi phí: " + oMaCP.toString());
                continue;
            }
            int sl = 0;
            try {
                sl = Integer.parseInt(oSoLuong.toString());
            } catch (Exception e) {
                System.out.println("Số lượng không hợp lệ tại dòng " + i);
                continue;
            }

            ChiTietChiPhiPhatSinh ctcppss = new ChiTietChiPhiPhatSinh(
                tam, cpps, sl
            );

            tam.themChiPhiPhatSinh(ctcppss);
        }

        return tam;
    }
    public double tinhTienCocMoi() {

        PhieuDatPhong pdpTam = new PhieuDatPhong(txtMPDP.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < dlp.getRowCount(); i++) {

            Object oNhan = dlp.getValueAt(i, 3);
            Object oTraThuc = dlp.getValueAt(i, 4);
            Object oTra = dlp.getValueAt(i, 5);
            Object oMaPhong = dlp.getValueAt(i, 1);

            if (oNhan == null || oTra == null || oTraThuc == null || oMaPhong == null)
                continue;

            LocalDate ngayNhan = LocalDate.parse(oNhan.toString(), formatter);
            LocalDate ngayTraThuc = LocalDate.parse(oTraThuc.toString(), formatter);
            LocalDate ngayTra = LocalDate.parse(oTra.toString(), formatter);

            long soNgayO = ChronoUnit.DAYS.between(ngayNhan, ngayTra);

            // Ở 1 ngày → không lấy cọc
            if (soNgayO <= 1)
                continue;

            String maPhong = oMaPhong.toString().trim();

            // Đã tồn tại trong DB → không tính cọc mới
            if (pdp.daCoPhong(txtMPDP.getText(), maPhong))
                continue;

            Phong p = dsp.timPhongTheoMa(maPhong);
            if (p == null)
                continue;

            ChiTietPhieuDatPhong ct = new ChiTietPhieuDatPhong(
                pdpTam,
                p,
                ngayNhan,
                ngayTraThuc,
                ngayTra
            );

            pdpTam.themChiTiet(ct);
        }

        return pdpTam.getTienCoc();
    }



    public void hienThiTienCocVaTienTongTienPhong() {
        PhieuDatPhong phieu = doiTuongTongTienPhong();
        DecimalFormat df = new DecimalFormat("#,###");
        txtTienCoc.setText(df.format(phieu.getTienCoc()));
        lblTongTien.setText("Tổng tiền: " + df.format(phieu.getTongTienPhong()));
        lblTongCP.setText("Tổng tiền: " + df.format(phieu.getTongTienChiPhiPhatSinh()));
        lblTongTatCa.setText("Tổng thanh toán: " + df.format(phieu.getTongTien()));
        String maTam = txtMPDP.getText().trim();
        if (maTam.isEmpty()) {
            txtTienCocMoi.setText("0");
            txtTienCocMoi.setEditable(false);
            return;
        }
        txtTienCocMoi.setText(String.valueOf(tinhTienCocMoi()));
        
    }
    public TaoPhieuDatPhong_GUI(PhieuDatPhong pdphong, String maNV) {
    	this(maNV);   	
    	getDuLieu(pdphong, maNV);
    	kiemTraDeThemCPPS(); 	
    }
    
   public void getDuLieu(PhieuDatPhong pdphong, String maNV){		
	txtMPDP.setText(pdphong.getMaPhieuDatPhong());
   	txtMKH.setText(pdphong.getKhachHang().getMaKhachHang());
   	txtTenKH.setText(pdphong.getKhachHang().getHoTen());
   	txtTenKH.setEditable(true);
   	txtSDT.setText(pdphong.getKhachHang().getSoDienThoai());
   	txtSDT.setEditable(true);
   	txtNgayTao.setText(pdphong.getNgayTao().toString());
   	cboTrangThai.setSelectedItem(pdphong.getTrangThai());
   	soNguoiLon= pdphong.getSoNguoiLon();
   	soTreEm= pdphong.getSoTreEm();
   	 int nl =soNguoiLon;
        int te = soTreEm;
        int thuc = nl + (int) Math.ceil(te / 2.0);
        txtSoNguoiThuc.setText(String.valueOf(thuc));        
   	chkVN.setSelected(pdphong.getKhachHang().LaNguoiVietNam());
   	btnInPhieu.setEnabled(true);
   	
   	dlp.setRowCount(0);
   	List<ChiTietPhieuDatPhong> dspctp= dsctpdp.getChiTietTheoMaPhieu(pdphong.getMaPhieuDatPhong());
   	for(ChiTietPhieuDatPhong tam: dspctp) {
   		LocalDate ngayNhan = tam.getNgayNhanThuc(); // != null 
//   		        ? tam.getNgayNhanThuc()
//   		        : tam.getNgayNhanThuc();

   		LocalDate ngayTra = tam.getNgayTraThuc() != null 
   		        ? tam.getNgayTraThuc()
   		        : tam.getNgayTra();

   		// Nếu vẫn null → set mặc định
   		if (ngayNhan == null) ngayNhan = LocalDate.now();
   		if (ngayTra == null) ngayTra = ngayNhan.plusDays(1);
   		long soDemO = Math.max(0, ChronoUnit.DAYS.between(ngayNhan, ngayTra));
   		Phong phong = tam.getPhong() == null ? new Phong() : tam.getPhong();
   		LoaiPhong loai = phong.getLoaiPhong() == null ? new LoaiPhong() : phong.getLoaiPhong();
   		double gia = (loai.getGia() > 0) ? loai.getGia() : 1;
   		double thanhTien = soDemO * gia;
           dlp.addRow(new Object[]{ dlp.getRowCount() + 1, phong.getMaPhong(), phong.getLoaiPhong().getTenLoaiPhong(),
           		tam.getNgayNhanThuc(), tam.getNgayTraThuc(), tam.getNgayTra(), soDemO, gia, thanhTien });
           hienThiTienCocVaTienTongTienPhong();
           if(pdphong.getTrangThai().equals("Đã hủy")) {
       		khoaTatCaTruong();
       	}
   	}
   	dlctps.setRowCount(0);
   	List<ChiTietChiPhiPhatSinh> dsctcppss= ctcppsd.getChiTietTheoMaPhieu(pdphong.getMaPhieuDatPhong());
   	for(ChiTietChiPhiPhatSinh tam: dsctcppss) {
   		ChiPhiPhatSinh t= cppsd.getChiPhiTheoMa(tam.getChiPhiPhatSinh().getMaChiPhiPhatSinh());
   		dlctps.addRow(new Object[] { t.getTenChiPhiPhatSinh(),t.getGia(), tam.getSoLuong(),
   				t.getGia()*tam.getSoLuong(),tam.getChiPhiPhatSinh().getMaChiPhiPhatSinh()});
   				hienThiTienCocVaTienTongTienPhong();    		
   	}
   	if(pdphong.getTrangThai().equals("Hoàn thành")||pdphong.getTrangThai().equals("Đã hủy")) {
   		khoaTatCaTruong();
   	}
   }
   
   
    private boolean xuLyNutLuu() {
        if (!kiemTraDuLieuNhap()) {
            JOptionPane.showMessageDialog(null, "Không có thông tin phiếu đặt phòng!");
            return false;   
        }

        try {
            String maPDP = txtMPDP.getText().trim();

            // ====== 1. KHÁCH HÀNG ======
            String maKH = txtMKH.getText().trim();
            String tenKH = txtTenKH.getText().trim();
            String sdt = txtSDT.getText().trim();
            boolean laVN = chkVN.isSelected();

            KhachHang kh = new KhachHang(maKH, tenKH, sdt, laVN);

            KhachHang khTonTai = khd.getKhachHangTheoMa(maKH);
            if (khTonTai == null) {
                khd.themKhachHang(kh);
            } else {
                khd.capNhatKhachHang(kh);
            }

            // ====== 2. PHIẾU ĐẶT PHÒNG ======
            PhieuDatPhong pd = new PhieuDatPhong();
            pd.setMaPhieuDatPhong(maPDP);
            pd.setKhachHang(kh);
            pd.setNhanVien(new NhanVien(txtNV.getText().trim()));
            pd.setNgayTao(LocalDate.now());
            pd.setTrangThai(cboTrangThai.getSelectedItem().toString());
            pd.setSoNguoiLon(
                    (txtNguoiLon.getText().trim().isEmpty() || txtNguoiLon.getText().trim().equals("0"))
                            ? 1 : Integer.parseInt(txtNguoiLon.getText().trim()));
            pd.setSoTreEm(
                    (txtTreEm.getText().trim().isEmpty() || txtTreEm.getText().trim().equals("0"))
                            ? 0 : Integer.parseInt(txtTreEm.getText().trim()));

            boolean laPhieuMoi = (pdp.timPhieuDatPhongTheoMa(maPDP) == null);

            if (laPhieuMoi) {
                pdp.themPhieuDatPhong(pd);
            } else {
                pdp.capNhatPhieuDatPhong(pd);

              
                dsctpdp.xoaCTTheoMaPhieu(maPDP);
                ctcppsd.xoaChiPhiTheoMaPhieu(maPDP);
            }

           String  trangThai= cboTrangThai.getSelectedItem().toString();
            for (int i = 0; i < tblPhong.getRowCount(); i++) {
                String maPhong = tblPhong.getValueAt(i, 1).toString();
                LocalDate ngayNhan = LocalDate.parse(tblPhong.getValueAt(i, 3).toString());
                LocalDate ngayTraThuc = LocalDate.parse(tblPhong.getValueAt(i, 4).toString());
                LocalDate ngayTra = LocalDate.parse(tblPhong.getValueAt(i, 5).toString());
                //String trangThai = tblPhong.getValueAt(i, 6).toString();
                

                Phong p = new Phong(maPhong);
                ChiTietPhieuDatPhong ct = new ChiTietPhieuDatPhong(
                        pd, p, ngayNhan, ngayTraThuc, ngayTra
                );

                dsctpdp.themChiTietPhieuDatPhong(ct);
                pd.themChiTiet(ct);
            }

            
            for (int i = 0; i < tblChiPhi.getRowCount(); i++) {
                String maCP = tblChiPhi.getValueAt(i, 4).toString();
                int soLuong = Integer.parseInt(tblChiPhi.getValueAt(i, 2).toString());

                ChiPhiPhatSinh cp = new ChiPhiPhatSinh(maCP);
                ChiTietChiPhiPhatSinh ctCP = new ChiTietChiPhiPhatSinh(pd, cp, soLuong);

                ctcppsd.themChiTietChiPhi(ctCP);
                pd.themChiPhiPhatSinh(ctCP);
            }

            return true;   

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Lỗi khi lưu phiếu: " + ex.getMessage());
            return false; 
        }
    }

    public List<Phong> layDanhSachPhongTrong() {
        Date dNhan = dateNgayNhan.getDate();
        Date dTra  = dateNgayTra.getDate();
//        if (dNhan == null || dTra == null) {
//            JOptionPane.showMessageDialog(
//                null,
//                "Vui lòng chọn ngày nhận và ngày trả!"
//            );
//            return null;
//        }
        if (!xuLyChonNgay())
            return null;
        LocalDate ngayNhan = dNhan.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate ngayTra = dTra.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return dsp.getDSPhongTrongTheoNgay(ngayNhan, ngayTra);
    }

    
   public boolean xuLyNutInPhieu(){
	   if (!kiemTraDuLieuNhap()) {
           JOptionPane.showMessageDialog(null, "Không có thông tin phiếu đặt phòng!");
           return false;   
       }

       try {
    	   String maPDP = txtMPDP.getText().trim();
    	   if(maPDP.length()==0) {
    		   maPDP= taoMaPhieuDatPhongTuDong();
    	   }
           // ====== 1. KHÁCH HÀNG ======
           String maKH = txtMKH.getText().trim();
           if(maKH.length()==0) {
        	   maKH= taoMaKhachHangTuDong();
           }
           String tenKH = txtTenKH.getText().trim();
           String sdt = txtSDT.getText().trim();
           boolean laVN = chkVN.isSelected();

           KhachHang kh = new KhachHang(maKH, tenKH, sdt, laVN);

           KhachHang khTonTai = khd.getKhachHangTheoMa(maKH);
           if (khTonTai == null) {
               khd.themKhachHang(kh);
           } else {
               khd.capNhatKhachHang(kh);
           }

           // ====== 2. PHIẾU ĐẶT PHÒNG ======
           PhieuDatPhong pd = new PhieuDatPhong();
           pd.setMaPhieuDatPhong(maPDP);
           pd.setKhachHang(kh);
           pd.setNhanVien(new NhanVien(txtNV.getText().trim()));
           pd.setNgayTao(LocalDate.now());
           pd.setTrangThai(cboTrangThai.getSelectedItem().toString());
           pd.setSoNguoiLon(
                   (txtNguoiLon.getText().trim().isEmpty() || txtNguoiLon.getText().trim().equals("0"))
                           ? 1 : Integer.parseInt(txtNguoiLon.getText().trim()));
           pd.setSoTreEm(
                   (txtTreEm.getText().trim().isEmpty() || txtTreEm.getText().trim().equals("0"))
                           ? 1 : Integer.parseInt(txtTreEm.getText().trim()));

           boolean laPhieuMoi = (pdp.timPhieuDatPhongTheoMa(maPDP) == null);

           if (laPhieuMoi) {
               pdp.themPhieuDatPhong(pd);
           } else {
               pdp.capNhatPhieuDatPhong(pd);           
               dsctpdp.xoaCTTheoMaPhieu(maPDP);
               ctcppsd.xoaChiPhiTheoMaPhieu(maPDP);
           }         
           for (int i = 0; i < tblPhong.getRowCount(); i++) {
               String maPhong = tblPhong.getValueAt(i, 1).toString();
               LocalDate ngayNhan = LocalDate.parse(tblPhong.getValueAt(i, 3).toString());
               LocalDate ngayTraThuc = LocalDate.parse(tblPhong.getValueAt(i, 4).toString());
               LocalDate ngayTra = LocalDate.parse(tblPhong.getValueAt(i, 5).toString());
               String trangThai = tblPhong.getValueAt(i, 6).toString();

               Phong p = new Phong(maPhong);
               ChiTietPhieuDatPhong ct = new ChiTietPhieuDatPhong(
                       pd, p, ngayNhan, ngayTraThuc, ngayTra
               );

               dsctpdp.themChiTietPhieuDatPhong(ct);
               pd.themChiTiet(ct);
           }
           return true;   
       } catch (Exception ex) {
           ex.printStackTrace();
           return false; 
       }
    }
   
   public boolean dieuKienThanhToan() {

	    LocalDate today = LocalDate.now();
	    String ma = txtMPDP.getText().trim();

	    if (ma.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "Bạn chưa chọn phiếu!");
	        return false;
	    }

	    PhieuDatPhong p = pdp.timPhieuDatPhongTheoMa(ma);
	    if (p == null) {
	        JOptionPane.showMessageDialog(null, "Không tìm thấy phiếu đặt phòng!");
	        return false;
	    }

//	    // ===== CHỈ XỬ LÝ KHI ĐANG Ở =====
//	    if (!p.getTrangThai().equalsIgnoreCase("Đang ở")) {
//	        JOptionPane.showMessageDialog(null,
//	                "Phiếu không ở trạng thái ĐANG Ở!");
//	        return false;
//	    }

	    List<ChiTietPhieuDatPhong> ds =
	            dsctpdp.getChiTietTheoMaPhieu(ma);

	    if (ds == null || ds.isEmpty()) {
	        JOptionPane.showMessageDialog(null,
	                "Phiếu không có phòng nào!");
	        return false;
	    }

	    return true;
	}

   public void huyDatPhong() {

	    PhieuDatPhong p = pdp.timPhieuDatPhongTheoMa(txtMPDP.getText().trim());
	    if (p == null) {
	        JOptionPane.showMessageDialog(null, "Không tìm thấy phiếu đặt phòng!");
	        return;
	    }

	    if (p.getTrangThai().equalsIgnoreCase("Đang ở")) {
	        JOptionPane.showMessageDialog(
	                null,
	                "Khách đang ở!\nKhông thể hủy, vui lòng tiến hành THANH TOÁN."
	        );
	        return;
	    }

	    if (!p.getTrangThai().equalsIgnoreCase("Đã đặt")) {
	        JOptionPane.showMessageDialog(
	                null,
	                "Chỉ được hủy khi phiếu ở trạng thái ĐÃ ĐẶT!"
	        );
	        return;
	    }

	    List<ChiTietPhieuDatPhong> dsct =
	            dsctpdp.getChiTietTheoMaPhieu(p.getMaPhieuDatPhong());

	    if (dsct == null || dsct.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "Phiếu không có chi tiết phòng!");
	        return;
	    }

	    LocalDate homNay = LocalDate.now();

	    for (int i = 0; i < dsct.size(); i++) {

	        ChiTietPhieuDatPhong ct = dsct.get(i);

	        LocalDate ngayNhan = ct.getNgayNhanThuc();
	        LocalDate ngayTraDuKien = ct.getNgayTra();

	        if (ngayNhan == null || ngayTraDuKien == null) continue;

	        long tongSoDem = ChronoUnit.DAYS.between(ngayNhan, ngayTraDuKien);

	        LocalDate ngayTraThucMoi;

	        // ===== CASE 1: 1 ĐÊM =====
	        if(tongSoDem <= 1) {
	        	ngayTraThucMoi = ngayNhan;
	            dlp.setValueAt(ngayNhan.toString(), i, 4);
	            dlp.setValueAt(ngayNhan.toString(), i, 5);
	        }
	      
	        // ===== CASE 2: HỦY SỚM (TRƯỚC ≥ 3 NGÀY) =====
	        else if (homNay.isBefore(ngayNhan.minusDays(3))) {

	            ngayTraThucMoi = ngayNhan;
	            dlp.setValueAt(ngayNhan.toString(), i, 4);
	            dlp.setValueAt(ngayNhan.toString(), i, 5);
	        }
	        // ===== CASE 3: HỦY SÁT NGÀY → TÍNH NGÀY GIỮA =====
	        else {

	            long epochGiua =
	                    (ngayNhan.toEpochDay() + ngayTraDuKien.toEpochDay()) / 2;
	            LocalDate ngayGiua = LocalDate.ofEpochDay(epochGiua);

	            if (homNay.isAfter(ngayGiua)) {
	                ngayTraThucMoi = homNay.plusDays(1);
	            } else {
	                ngayTraThucMoi = ngayGiua;
	            }

	            dlp.setValueAt(ngayTraThucMoi.toString(), i, 4);
	        }

	        long soDemO = ChronoUnit.DAYS.between(ngayNhan, ngayTraThucMoi);
	        if (soDemO < 0) soDemO = 0;

	        Phong phong = dsp.timPhongTheoMa(ct.getPhong().getMaPhong());
	        double gia = phong.getLoaiPhong().getGia();

	        dlp.setValueAt(soDemO, i, 6);
	        dlp.setValueAt(gia, i, 7);
	        dlp.setValueAt(soDemO * gia, i, 8);
	    }

	    p.setTrangThai("Đã hủy");
	    cboTrangThai.setSelectedItem("Đã hủy");

	    hienThiTienCocVaTienTongTienPhong();
	    xuLyNutLuu();
	    khoaTatCaTruong();
	   

	    new HuyPhieuDatPhong_GUI(
	            p.getMaPhieuDatPhong(),
	            doiTuongTongTienPhong(),
	            dlp
	    ).setVisible(true);
	}



   private void khoaTatCaTruong() {
	    // Khóa JDateChooser
	    dateNgayNhan.setEnabled(false);
	    dateNgayTra.setEnabled(false);
	    // Khóa JTextField
	    txtSDT.setEditable(false);
	    txtTenKH.setEditable(false);
	    txtNgayTao.setEditable(false);
	    txtTienCoc.setEditable(false);
	    txtNV.setEditable(false);
	    txtMKH.setEditable(false);
	    txtMPDP.setEditable(false);
	    txtNguoiLon.setEditable(false);
	    txtTreEm.setEditable(false);
	    txtThucTe.setEditable(false);
	    txtSoNguoiThuc.setEditable(false);
	    txtTienCocMoi.setEditable(false);
	    // Khóa JButton
	    btnInPhieu.setEnabled(false);
	    btnXoa.setEnabled(false);
	    btnThemCP.setEnabled(false);
	    btnXoaCP.setEnabled(false);
	    btnLuu.setEnabled(false);
	    btnHuy.setEnabled(false);
	    btnTT.setEnabled(false);
	    btnXR.setEnabled(true);
	    btnCapNhat.setEnabled(false);
	    btnXNDP.setEnabled(false);
	    btnTPS.setEnabled(false);
	    btnGoiY.setEnabled(false);
	}
   private void moKhoaTatCaTruong() {
	    // Mở JDateChooser
	    dateNgayNhan.setEnabled(true);
	    dateNgayTra.setEnabled(true);
	    // Mở JTextField
//	    txtSDT.setEditable(true);
//	    txtTenKH.setEditable(true);
	    txtNguoiLon.setEditable(true);
	    txtTreEm.setEditable(true);
	    txtThucTe.setEditable(true);
	    txtSoNguoiThuc.setEditable(true);
	    // Mở JButton
	    btnInPhieu.setEnabled(true);
	    btnXoa.setEnabled(true);
	    btnThemCP.setEnabled(true);
	    btnXoaCP.setEnabled(true);
	    btnLuu.setEnabled(true);
	    btnHuy.setEnabled(true);
	    btnTT.setEnabled(true);
	    btnXR.setEnabled(true);
	    btnCapNhat.setEnabled(true);
	    btnXNDP.setEnabled(true);
	    btnTPS.setEnabled(true);
	    btnGoiY.setEnabled(true);
	    lblTongTien.setText("Tổng tiền: 0");
	    lblTongCP.setText("Tổng tiền: 0");
	    lblTongTatCa.setText("Tổng tiền: 0");
	    txtTienCoc.setText("0");
	    txtTienCocMoi.setText("0");
	    
	    cboTrangThai.setSelectedIndex(0);
	}
   private Object[][] convertTableModelToArray(JTable table) {
	    DefaultTableModel model = (DefaultTableModel) table.getModel();
	    int rowCount = model.getRowCount();
	    int colCount = model.getColumnCount();

	    Object[][] data = new Object[rowCount][colCount];

	    for (int r = 0; r < rowCount; r++) {
	        for (int c = 0; c < colCount; c++) {
	            data[r][c] = model.getValueAt(r, c);
	        }
	    }
	    return data;
	}

   protected String taoMaHoaDon() {
       LocalDate now = LocalDate.now();
       String ngay = now.format(DateTimeFormatter.ofPattern("ddMMyyyy"));

       HoaDon_DAO dao = new HoaDon_DAO();
       String maCu = dao.getMaHoaDonCuoiTrongNgay(ngay);

       int soMoi = 1;
       if (maCu != null) {
           String sttCu = maCu.substring(maCu.length() - 3);
           soMoi = Integer.parseInt(sttCu) + 1;
       }

       return "HD" + ngay + String.format("%03d", soMoi);
   }
  public void kiemTraDeThemCPPS() {
	  if(!cboTrangThai.getSelectedItem().equals("Đang ở")) {
		  btnThemCP.setEnabled(false);
		  btnXoaCP.setEnabled(false);
	  }else {
		  btnThemCP.setEnabled(true);
		  btnXoaCP.setEnabled(true);
	  }
  }
  public boolean kiemTraTrangThai(PhieuDatPhong p) {

	    if (p == null) {
	        JOptionPane.showMessageDialog(null,
	            "Bạn không được đổi trạng thái khi không có dữ liệu!",
	            "Thông báo",
	            JOptionPane.WARNING_MESSAGE);
	        return false;
	    }

	    List<ChiTietPhieuDatPhong> ds =
	        dsctpdp.getChiTietTheoMaPhieu(p.getMaPhieuDatPhong());

	    if (ds == null || ds.isEmpty()) {
	        JOptionPane.showMessageDialog(null,
	            "Không có danh sách phòng chi tiết",
	            "Thông báo",
	            JOptionPane.WARNING_MESSAGE);
	        return false;
	    }

	    LocalDate today = LocalDate.now();

	    LocalDate nhanSomNhat = ds.stream()
	        .map(ChiTietPhieuDatPhong::getNgayNhanThuc)
	        .filter(d -> d != null)
	        .min(LocalDate::compareTo)
	        .orElse(null);
	    if (nhanSomNhat != null && today.isBefore(nhanSomNhat)&&!cboTrangThai.getSelectedItem().equals("Đã đặt")) {
	        JOptionPane.showMessageDialog(null,
	            "Bạn không được đổi trạng thái là Đang ở khi chưa tới ngày nhận phòng!",
	            "Thông báo",
	            JOptionPane.WARNING_MESSAGE);
	        return false;
	    }

	    return true;
	}

}
