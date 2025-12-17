package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import dao.HoaDon_DAO;
import dao.ChiTietChiPhiPhatSinh_DAO;
import dao.ChiPhiPhatSinh_DAO;
import dao.Phong_DAO;
import dao.NhanVien_DAO;

import entity.HoaDon;
import entity.ChiTietChiPhiPhatSinh;
import entity.ChiPhiPhatSinh;
import entity.Phong;
import entity.NhanVien;
import entity.PhieuDatPhong;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Flow;

public class HoaDon_GUI extends JPanel implements ActionListener, MouseListener {

    private JTable tblHoaDon, tblChiTietCTPT, tblChiTietCPPS;
    private DefaultTableModel modelHD, modelCTPT, modelCTCPPS;

    private JTextField txtTim, txtTuNgay, txtDenNgay, txtTongTienPhong, txtTongTienCPPS,txtTongTien,txtTongThanhToan;
    private JDateChooser dateNgayBatDau, dateNgayKetThuc;
    private JButton btTim, btLoc, btHomNay, btTatCa;

    private JLabel lbMaHD, lbNgay, lbPhong, lbNhanVien;

    
    private HoaDon_DAO hdDAO;
    
    public HoaDon_GUI() {

        setLayout(new BorderLayout());
        setFont(new Font("Arial", Font.PLAIN, 13));

        hdDAO = new HoaDon_DAO();
        
        JPanel khung = new JPanel(new BorderLayout(10, 10));
        khung.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "DANH SÁCH HÓA ĐƠN",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16)
        ));
        add(khung, BorderLayout.CENTER);

        //====================== KHUNG TRÊN ============================
        JPanel pnBar = new JPanel();
        pnBar.setLayout(new BoxLayout(pnBar, BoxLayout.Y_AXIS));
        
        JPanel pnTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        dateNgayBatDau = new JDateChooser();
        dateNgayBatDau.setDateFormatString("dd/MM/yyyy");
        dateNgayBatDau.setDate(new Date());
        dateNgayBatDau.setPreferredSize(new Dimension(120,30));
        
        dateNgayKetThuc = new JDateChooser();
        dateNgayKetThuc.setDateFormatString("dd/MM/yyyy");
        dateNgayKetThuc.setDate(new Date());
        dateNgayKetThuc.setPreferredSize(new Dimension(120,30));
        
        btHomNay = new JButton("Hôm nay");
        btTatCa = new JButton("Tất cả");
        btLoc = new JButton("Lọc");

        pnTop.add(btTatCa);
        pnTop.add(btHomNay);
        pnTop.add(new JLabel("Từ ngày:"));
        pnTop.add(dateNgayBatDau);
        pnTop.add(new JLabel("Đến ngày:"));
        pnTop.add(dateNgayKetThuc);
        pnTop.add(btLoc);
        
        JPanel pnBot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtTim = new JTextField(10);
        btTim = new JButton("Tìm");

        pnBot.add(new JLabel("Nhập mã tìm:"));
        pnBot.add(txtTim);
        pnBot.add(btTim);
        
        pnBar.add(pnTop);
        pnBar.add(pnBot);
        khung.add(pnBar, BorderLayout.NORTH);
        
        
        //====================== BẢNG HÓA ĐƠN ============================
        String[] colHD = {"STT", "Mã hóa đơn", "Mã PDP", "Mã khuyến mãi", "PTTT", "Tổng tiền","Ngày tạo"};
        modelHD = new DefaultTableModel(colHD, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblHoaDon = new JTable(modelHD);
     // Thu nhỏ cột STT (cột 0)
        tblHoaDon.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblHoaDon.getColumnModel().getColumn(0).setMinWidth(40);
        tblHoaDon.getColumnModel().getColumn(0).setMaxWidth(40);
        
        khung.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);


        //====================== KHUNG CHI TIẾT HÓA ĐƠN ============================
        JPanel pnCT = new JPanel();
        pnCT.setLayout(new BoxLayout(pnCT, BoxLayout.Y_AXIS));
        pnCT.setPreferredSize(new Dimension(700, 0));
        pnCT.setBorder(BorderFactory.createTitledBorder("CHI TIẾT"));
        
        JPanel pnTT1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pnTT2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pnTT3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pnTT4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        lbMaHD = new JLabel("Mã hóa đơn: ");
        lbNgay = new JLabel("Ngày lập: ");
        lbPhong = new JLabel("Phòng: ");
        lbNhanVien = new JLabel("Nhân viên: ");
        
        
        pnTT1.add(lbMaHD);
        pnTT2.add(lbNhanVien);
        pnTT3.add(lbPhong);
        pnTT4.add(lbNgay);
        
        pnCT.add(pnTT1);
        pnCT.add(pnTT2);
        pnCT.add(pnTT3);
        pnCT.add(pnTT4);
//Chi tiết 1==============================
        pnCT.add(Box.createVerticalStrut(10));
        JPanel pnTam1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnTam1.add(new JLabel("Chi tiết phòng thuê:"));
        pnCT.add(pnTam1);
        String[] colCTPT = {"STT", "Mã phòng", "Loại phòng", "Ngày nhận", "Ngày trả","Ngày trả thực", "Số đêm", "Giá", "Thành tiền"};
        modelCTPT = new DefaultTableModel(colCTPT, 0);
        tblChiTietCTPT = new JTable(modelCTPT);
        pnCT.add(new JScrollPane(tblChiTietCTPT));
        
        JPanel pnTongTienPhong = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTongTienPhong = new JTextField(12);
        txtTongTienPhong.setEditable(false);
        pnTongTienPhong.add(new JLabel("Tổng tiền phòng:"));
        pnTongTienPhong.add(txtTongTienPhong);
        pnCT.add(pnTongTienPhong);
        
//Chi tiết 2==============================

        pnCT.add(Box.createVerticalStrut(10));
        JPanel pnTam2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnTam2.add(new JLabel("Chi tiết chi phí phát sinh:"));
        pnCT.add(pnTam2);
        
        String[] colCTCPPS = {"STT","Tên chi phí", "Giá", "Số lượng", "Thành tiền"};
        modelCTCPPS = new DefaultTableModel(colCTCPPS, 0);
        tblChiTietCPPS = new JTable(modelCTCPPS);
        pnCT.add(new JScrollPane(tblChiTietCPPS));
        
        pnCT.add(Box.createVerticalStrut(10));
        JPanel pnTongTienCPPS = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTongTienCPPS = new JTextField(12);
        txtTongTienCPPS.setEditable(false);
        pnTongTienCPPS.add(new JLabel("Tổng tiền chí phí phát sinh:"));
        pnTongTienCPPS.add(txtTongTienCPPS);
        pnCT.add(pnTongTienCPPS);
//Tong Tien======================================

        JPanel pnTongTien = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTongTien = new JTextField(12);
        txtTongTien.setEditable(false);
        pnTongTien.add(new JLabel("Tổng tiền:"));
        pnTongTien.add(txtTongTien);
        pnCT.add(pnTongTien);
        
        JPanel pnTongThanhToan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTongThanhToan = new JTextField(12);
        txtTongThanhToan.setEditable(false);
        pnTongThanhToan.add(new JLabel("Tổng thanh toán:"));
        pnTongThanhToan.add(txtTongThanhToan);
        pnCT.add(pnTongThanhToan);

        add(pnCT, BorderLayout.EAST);

        //====================== LOAD DỮ LIỆU ============================
        loadHoaDon();

        //====================== SỰ KIỆN ============================
        tblHoaDon.addMouseListener(this);
        btTim.addActionListener(this);
        btTatCa.addActionListener(this);
        btHomNay.addActionListener(this);
        btLoc.addActionListener(this);
    }

//============Load bảng hóa đơn===============
    private void loadHoaDon() {
        modelHD.setRowCount(0);

        HoaDon_DAO dao = new HoaDon_DAO();
        List<Object[]> ds = dao.getDanhSachHoaDon();

        int stt = 1;
        for (Object[] r : ds) {
            modelHD.addRow(new Object[]{
                stt++,
                r[0], // maHoaDon
                r[1], // maPDP
                r[2], // maKhuyenMai
                r[3], // PTTT
                dinhDangTien((double) r[4]), // tongThanhToan
                r[5]  // ngayTao
            });
        }
    }
    private String dinhDangTien(double tien) {
        return String.format("%,.0f đ", tien);
    }
//==============load label chi tiết===================
    private void hienThiThongTinHoaDon(String maHoaDon) {

        HoaDon_DAO dao = new HoaDon_DAO();
        Object[] data = dao.getThongTinHoaDon(maHoaDon);

        if (data == null) return;

        lbMaHD.setText("Mã hóa đơn: " + data[0]);
        lbNgay.setText("Ngày lập: " + data[1]);
        lbNhanVien.setText("Nhân viên: " + data[2]);
        lbPhong.setText("Phòng: " + data[3]);
    }
    private long tinhTongTienPhong() {
        long tong = 0;

        for (int i = 0; i < modelCTPT.getRowCount(); i++) {
            Object value = modelCTPT.getValueAt(i, 8); // cột Thành tiền

            if (value instanceof Number) {
                tong += ((Number) value).longValue();
            }
        }
        return tong;
    }


    private long tinhTongTienCPPS() {
        long tong = 0;

        for (int i = 0; i < modelCTCPPS.getRowCount(); i++) {
            Object value = modelCTCPPS.getValueAt(i, 4); // Thành tiền

            if (value instanceof Number) {
                tong += ((Number) value).longValue();
            }
        }
        return tong;
    }
    
    private void loadHoaDonHomNay() {
        modelHD.setRowCount(0); // clear bảng

        HoaDon_DAO dao = new HoaDon_DAO();
        ResultSet rs = dao.getHoaDonHomNay();

        int stt = 1;
        try {
            while (rs != null && rs.next()) {
                modelHD.addRow(new Object[]{
                    stt++,
                    rs.getString("maHoaDon"),
                    rs.getString("maPhieuDatPhong"),
                    rs.getString("maKhuyenMai"),
                    rs.getString("phuongThucThanhToan"),
                    rs.getDouble("tongThanhToan"),
                    rs.getDate("ngayTao")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHoaDonTheoKhoangNgay(Date tuNgay, Date denNgay) {
        modelHD.setRowCount(0); // clear bảng
        ResultSet rs = hdDAO.getHoaDonTheoKhoangNgay(tuNgay, denNgay);

        int stt = 1;
        try {
            while (rs != null && rs.next()) {
                modelHD.addRow(new Object[]{
                    stt++,
                    rs.getString("maHoaDon"),
                    rs.getString("maPhieuDatPhong"),
                    rs.getString("maKhuyenMai"),
                    rs.getString("phuongThucThanhToan"),
                    rs.getDouble("tongThanhToan"),
                    rs.getDate("ngayTao")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadHoaDonTuResultSet(ResultSet rs) {

        modelHD.setRowCount(0);

        int stt = 1;
        try {
            while (rs != null && rs.next()) {
                modelHD.addRow(new Object[]{
                    stt++,
                    rs.getString("maHoaDon"),
                    rs.getString("maPhieuDatPhong"),
                    rs.getString("maKhuyenMai"),
                    rs.getString("phuongThucThanhToan"),
                    rs.getDouble("tongThanhToan"),
                    rs.getDate("ngayTao")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //===========================================================
    //                ACTION EVENTS
    //===========================================================
    @Override
    public void actionPerformed(ActionEvent e) {
    	Object o = e.getSource();
    	if (o.equals(btTatCa)) loadHoaDon();
    	else if (o.equals(btHomNay)) {
    	    loadHoaDonHomNay();
    	}
    	else if (o.equals(btLoc)) {
    	    java.util.Date tuNgay = dateNgayBatDau.getDate();
    	    java.util.Date denNgay = dateNgayKetThuc.getDate();

    	    if (tuNgay == null || denNgay == null) {
    	        JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày!");
    	        return;
    	    }

    	    if (tuNgay.after(denNgay)) {
    	        JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được sau ngày kết thúc!");
    	        return;
    	    }

    	    loadHoaDonTheoKhoangNgay(tuNgay, denNgay);
    	}
    	else if (o.equals(btTim)) {

    	    String maTim = txtTim.getText().trim();

    	    if (maTim.isEmpty()) {
    	        JOptionPane.showMessageDialog(this, "Vui lòng nhập mã hóa đơn cần tìm!");
    	        return;
    	    }

    	    HoaDon_DAO dao = new HoaDon_DAO();
    	    ResultSet rs = dao.timHoaDonTheoMa(maTim);

    	    loadHoaDonTuResultSet(rs);
    	}

    }

    @Override public void mouseClicked(MouseEvent e) { 
    	int row = tblHoaDon.getSelectedRow();
        if (row == -1) return;
        // CỘT 1 = Mã hóa đơn
        String maHoaDon = tblHoaDon.getValueAt(row, 1).toString();
        String maPDP = tblHoaDon.getValueAt(row, 2).toString();
        String tongThanhToanStr = tblHoaDon.getValueAt(row, 5).toString();
        
        double tongThanhToan = Double.parseDouble(
                tongThanhToanStr.replaceAll("[^0-9.]", "")
        );
        
        hienThiThongTinHoaDon(maHoaDon);
        hdDAO.loadCTPhongByMaPDP(maPDP, modelCTPT);
        hdDAO.loadCTCPPSByMaPDP(maPDP, modelCTCPPS);
        
     // ===== TÍNH & HIỂN THỊ =====
        double tongPhong = tinhTongTienPhong();
        double tongCPPS  = tinhTongTienCPPS();

        double tongTien = tongPhong + tongCPPS;
        
        txtTongTienPhong.setText(dinhDangTien(tongPhong));
        txtTongTienCPPS.setText(dinhDangTien(tongCPPS));
        txtTongTien.setText(dinhDangTien(tongTien));
        txtTongThanhToan.setText(dinhDangTien(tongThanhToan));
    	}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

}
