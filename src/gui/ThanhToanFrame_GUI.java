package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

import dao.HoaDon_DAO;
import dao.KhuyenMai_DAO;
import dao.ThanhToan_DAO;
import entity.KhuyenMai;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class ThanhToanFrame_GUI extends JFrame implements ActionListener{
	
	private JComboBox<KhuyenMai> cboKhuyenMai;
	private JComboBox<String> cboPhuongThucTT;
    private JLabel lblMaHD;
    private JLabel lblTenKH;
    private JLabel lblNgayNhanTra;
    private JLabel lblMaNV;

    private JTable tablePhong;
    private JTable tableChiPhi;
    private JScrollPane scrollPhong;
    private JScrollPane scrollChiPhi;

    private JLabel lblTong;
    private JLabel lblTongTien;
    private JLabel lblKM;
    private JLabel lblTongTatCa;
    private JLabel lblTienDua;

    private JButton btnXacNhan;
    
    private JTextField txtTienDua;
    private JLabel lblTienThoi;
    private long tongThanhToan = 0; // set khi tính tổng




    private String maPDPglobal;
    public ThanhToanFrame_GUI() {
    }
    public ThanhToanFrame_GUI(
            String maPDP,
            String tenKH,
            String maNV,
            Object[][] dsPhong,
            Object[][] dsChiPhi,
            String tongTienPhong,
            String tongThanhToan
    ) {
    	this.maPDPglobal = maPDP;
    	
        initComponents();

        String maHD = taoMaHoaDonMoi();

        // Load dữ liệu từ database
        loadData(maHD, maPDP, tenKH, maNV, dsPhong, dsChiPhi, tongTienPhong, tongThanhToan);
    }

    private void initComponents() {
        setTitle("Hóa đơn thanh toán");
        setSize(900, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(240, 240, 250));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        /* ================= TITLE ================= */
        JLabel title = new JLabel("Hóa đơn");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(20));

        /* ================= INFO ================= */
        lblMaHD = createInfoLabel("Mã hóa đơn: ---");
        lblTenKH = createInfoLabel("Tên khách hàng: ---");
        lblNgayNhanTra = createInfoLabel("Ngày nhận: ---    Ngày trả: ---");
        lblMaNV = createInfoLabel("Mã nhân viên: ---");

        JPanel infoBox = new JPanel();
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setBackground(Color.WHITE);

        for (JLabel lb : new JLabel[]{lblMaHD, lblTenKH, lblNgayNhanTra, lblMaNV}) {
            lb.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoBox.add(lb);
            infoBox.add(Box.createVerticalStrut(6));
        }

        JPanel infoWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        infoWrap.setBackground(Color.WHITE);
        infoWrap.add(infoBox);

        content.add(infoWrap);
        content.add(Box.createVerticalStrut(20));

        /* ================= TABLE PHÒNG ================= */
        JLabel lblPhong = createSectionLabel("Chi tiết phòng thuê:");
        left(lblPhong);
        lblPhong.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblPhong);

        tablePhong = createTable(
            new String[]{"STT", "Mã phòng", "Loại phòng", "Ngày nhận", "Ngày trả",
                         "Ngày trả thực", "Số đêm", "Giá", "Thành tiền"},
            new Object[][]{}
        );

        scrollPhong = new JScrollPane(tablePhong);
        scrollPhong.setPreferredSize(new Dimension(820, 200));

        JPanel phongWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        phongWrap.setBackground(Color.WHITE);
        phongWrap.add(scrollPhong);

        content.add(phongWrap);
        content.add(Box.createVerticalStrut(20));

        /* ================= TABLE CHI PHÍ ================= */
        JLabel lblCP = createSectionLabel("Chi phí phát sinh:");
        lblCP.setAlignmentX(Component.LEFT_ALIGNMENT);
        left(lblCP);
        content.add(lblCP);

        tableChiPhi = createTable(
            new String[]{"STT", "Tên chi phí", "Số lượng", "Giá", "Thành tiền"},
            new Object[][]{}
        );

        scrollChiPhi = new JScrollPane(tableChiPhi);
        scrollChiPhi.setPreferredSize(new Dimension(820, 150));

        JPanel chiPhiWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        chiPhiWrap.setBackground(Color.WHITE);
        chiPhiWrap.add(scrollChiPhi);

        content.add(chiPhiWrap);
        content.add(Box.createVerticalStrut(20));

        /* ================= TỔNG TIỀN ================= */
        lblTong        = createInfoLabel("Tổng tiền phòng: 0");
        lblTongTien    = createInfoLabel("Chi phí phát sinh: 0");
        lblKM          = createInfoLabel("Khuyến mãi: 0");
        lblTongTatCa   = createInfoLabel("Thành tiền: 0");
        
        JLabel lblTienDuaTitle = createInfoLabel("Tiền khách đưa:");
        
        txtTienDua = new JTextField();
        txtTienDua.setMaximumSize(new Dimension(240, 32));
        txtTienDua.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtTienDua.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                try {
                    String raw = txtTienDua.getText().replaceAll("[^0-9]", "");
                    if (!raw.isEmpty()) {
                        long v = Long.parseLong(raw);
                        txtTienDua.setText(dinhDangTien(v).replace(" đ", ""));
                    }
                } catch (Exception ignored) {}
            }
        });

        
        txtTienDua.getDocument().addDocumentListener(new DocumentListener() {

            private void tinhTienThoi() {
                try {
                    String raw = txtTienDua.getText().replaceAll("[^0-9]", "");
                    if (raw.isEmpty()) {
                        lblTienThoi.setText("Tiền thối lại: 0 đ");
                        return;
                    }

                    long tienDua = Long.parseLong(raw);
                    long tienThoi = tienDua - tongThanhToan;
                    if (tienThoi < 0) tienThoi = 0;

                    lblTienThoi.setText("Tiền thối lại: " + dinhDangTien(tienThoi));

                } catch (Exception ignored) {}
            }

            @Override public void insertUpdate(DocumentEvent e) { tinhTienThoi(); }
            @Override public void removeUpdate(DocumentEvent e) { tinhTienThoi(); }
            @Override public void changedUpdate(DocumentEvent e) {}
        });


        


        lblTienThoi = createInfoLabel("Tiền thối lại: 0");
        
        cboKhuyenMai = new JComboBox<>();
        cboPhuongThucTT = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản"});
      //====================================================
        
        Dimension comboSize = new Dimension(240, 32);
        cboKhuyenMai.setMaximumSize(comboSize);
        cboPhuongThucTT.setMaximumSize(comboSize);

     // ===== PANEL TỔNG TIỀN =====
        JPanel pAmounts = new JPanel();
        pAmounts.setLayout(new BoxLayout(pAmounts, BoxLayout.Y_AXIS));
        pAmounts.setBackground(Color.WHITE);


        // Labels
        lblTong.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTongTien.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblKM.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTongTatCa.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTienThoi.setAlignmentX(Component.LEFT_ALIGNMENT);

        Dimension fieldSize = new Dimension(240, 32);
        cboKhuyenMai.setMaximumSize(fieldSize);
        cboPhuongThucTT.setMaximumSize(fieldSize);
        txtTienDua.setMaximumSize(fieldSize);

        
        left(lblTong);
        left(lblTongTien);
        left(lblKM);
        left(cboKhuyenMai);
        left(lblTongTatCa);
        left(cboPhuongThucTT);
        left(lblTienDuaTitle);
        left(txtTienDua);
        left(lblTienThoi);

        pAmounts.add(lblTong);
        pAmounts.add(Box.createVerticalStrut(6));

        pAmounts.add(lblTongTien);
        pAmounts.add(Box.createVerticalStrut(6));

        pAmounts.add(lblKM);
        pAmounts.add(Box.createVerticalStrut(6));

        pAmounts.add(cboKhuyenMai);
        pAmounts.add(Box.createVerticalStrut(8));

        pAmounts.add(lblTongTatCa);
        pAmounts.add(Box.createVerticalStrut(10));

        pAmounts.add(createInfoLabel("Phương thức thanh toán:"));
        pAmounts.add(cboPhuongThucTT);
        pAmounts.add(Box.createVerticalStrut(10));

        pAmounts.add(createInfoLabel("Tiền khách đưa:"));
        pAmounts.add(txtTienDua);
        pAmounts.add(Box.createVerticalStrut(6));

        pAmounts.add(lblTienThoi);

        // ===== WRAP CĂN TRÁI =====
        JPanel amountWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        amountWrap.setBackground(Color.WHITE);
        amountWrap.add(pAmounts);

        content.add(amountWrap);
        content.add(Box.createVerticalStrut(25));

        /* ================= BUTTON ================= */
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);

        btnXacNhan = new JButton("Xác nhận thanh toán");
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPanel.add(btnXacNhan);

        content.add(btnPanel);

        root.add(content, BorderLayout.CENTER);
        add(new JScrollPane(root));

        loadKhuyenMai();
        btnXacNhan.addActionListener(this);

    }


    private void left(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }


    
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(btnXacNhan)) {

            try {
            	KhuyenMai km = (KhuyenMai) cboKhuyenMai.getSelectedItem();
            	
                String maHD = lblMaHD.getText().replace("Mã hóa đơn: ", "").trim();
                String maPDP = maPDPglobal;
                String maKM = (km == null) ? null : km.getMaKhuyenMai();
                String phuongThucTT = (String) cboPhuongThucTT.getSelectedItem();
                if (phuongThucTT == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn phương thức thanh toán!");
                    return;
                }

                long tongTienPhong = parseTien(lblTong.getText());
                long tongCPPS      = parseTien(lblTongTien.getText());
                long tongThanhToan = parseTien(lblTongTatCa.getText());


                LocalDate ngayTao = LocalDate.now();

                ThanhToan_DAO dao = new ThanhToan_DAO();
                
                boolean ok = dao.thanhToan(
                        maHD,
                        maPDP,
                        maKM,
                        phuongThucTT,
                        tongTienPhong,
                        tongCPPS,
                        tongThanhToan,
                        ngayTao
                );

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thanh toán thất bại!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }
    
    
    
    
    

 // ==== Helper UI ====
    private JLabel createInfoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return lbl;
    }

    private JLabel createSectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setBorder(new EmptyBorder(6, 0, 6, 0));
        return lbl;
    }

    private JTable createTable(String[] columns, Object[][] data) {
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(20);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 255, 245));
        return table;
    }

    // ==== Setters ====
    public void setThongTinChung(String maHD, String tenKH, String ngayNhan, String ngayTra, String maNV) {
        lblMaHD.setText("Mã hóa đơn: " + maHD);
        lblTenKH.setText("Tên khách hàng: " + tenKH);
        lblNgayNhanTra.setText("Ngày nhận: " + ngayNhan + "    Ngày trả: " + ngayTra);
        lblMaNV.setText("Mã nhân viên: " + maNV);
    }

    public void setPhongTable(Object[][] data) {

        String[] cols = {
            "STT", "Mã phòng", "Loại phòng",
            "Ngày nhận", "Ngày trả", "Ngày trả thực",
            "Số đêm", "Giá", "Thành tiền"
        };

        Object[][] newData = new Object[data.length][9];

        for (int i = 0; i < data.length; i++) {

            LocalDate ngayNhan = (LocalDate) data[i][3];
            LocalDate ngayTra = (LocalDate) data[i][4];
            int soDem = ((Long) data[i][6]).intValue();
            LocalDate ngayHienTai = LocalDate.now();
            
            LocalDate ngayTraThuc = tinhNgayTraThuc(
                ngayNhan, ngayTra, soDem, ngayHienTai
            );
            int soDemThuc = (int) ChronoUnit.DAYS.between(ngayNhan, ngayTraThuc);
            
            long giaMotDem = ((Number) data[i][7]).longValue();
            long thanhTien = soDemThuc * giaMotDem;
            
            newData[i][0] = i + 1;
            newData[i][1] = data[i][1];
            newData[i][2] = data[i][2];
            newData[i][3] = ngayNhan;
            newData[i][4] = ngayTra;
            newData[i][5] = ngayTraThuc; // SET Ở ĐÂY
            newData[i][6] = soDemThuc;
            newData[i][7] = giaMotDem;
            newData[i][8] = thanhTien;
        }

        tablePhong.setModel(new DefaultTableModel(newData, cols));
    }




    public void setChiPhiTable(Object[][] data) {
        String[] cols = {"STT", "Tên chi phí", "Số lượng", "Giá", "Thành tiền"};
        Object[][] newData = new Object[data.length][5];

        for (int i = 0; i < data.length; i++) {
            newData[i][0] = i + 1;      // STT
            newData[i][1] = data[i][0]; // Tên chi phí
            newData[i][2] = data[i][1]; // Số lượng
            newData[i][3] = data[i][2]; // Giá
            newData[i][4] = data[i][3]; // Thành tiền
        }

        tableChiPhi.setModel(new DefaultTableModel(newData, cols));
    }

    public void setTongTien(String tong, String km, String tongTien, String dua, String thoi) {
        lblTong.setText("Tổng: " + tong);
        lblKM.setText("Khuyến mãi: " + km);
        lblTongTien.setText("Tổng tiền: " + tongTien);
        lblTienDua.setText("Tiền khách đưa: " + dua);
        lblTienThoi.setText("Tiền thối lại: " + thoi);
    }
    //===========generate mã hóa đơn=============
    protected String taoMaHoaDonMoi() {
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
    
    private String formatDate(Date d) {
        if (d == null) return "---";
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(d);
    }
//==============================loaddata============================
    private void loadData(
            String maHD,
            String maPDP,
            String tenKH,
            String maNV,
            Object[][] dsPhong,
            Object[][] dsChiPhi,
            String tongTienPhong,
            String tongThanhToan
    ) {
        HoaDon_DAO dao = new HoaDon_DAO();
        Date[] ngay = dao.getNgayNhanTraByMaPDP(maPDP);

        String ngayNhan = formatDate(ngay[0]);
        String ngayTra = formatDate(ngay[1]);

        // Set thông tin chung
        setThongTinChung(maHD, tenKH, ngayNhan, ngayTra, maNV);

        // Set bảng
        setPhongTable(dsPhong);
        setChiPhiTable(dsChiPhi);

        // Tổng tiền
        lblTong.setText("Tổng tiền phòng: " + tongTienPhong);
        lblTongTien.setText("Chi phí phát sinh: " + tongThanhToan);
        
        capNhatTongTien();
//        tongTienPhong = tongTienPhong.replaceAll("[^\\d.]", "");
//        tongThanhToan = tongThanhToan.replaceAll("[^\\d.]", "");
//
//        double tongTienPhongdouble = Double.parseDouble(tongTienPhong);
//        double tongThanhToandouble = Double.parseDouble(tongThanhToan);
//
//
//        double tongTatCa = tongTienPhongdouble + tongThanhToandouble;
//        
//        DecimalFormat df = new DecimalFormat("#,###.##");
//        String tongTatCaStr = df.format(tongTatCa);
//        
//        lblTongTatCa.setText("Tổng tất cả: " + tongTatCaStr);
        //KhuyenMai
    }
    //======================Tinh ngay tra thuc ================================
    public LocalDate tinhNgayTraThuc(
            LocalDate ngayNhan,
            LocalDate ngayTraDuKien,
            int soDem,
            LocalDate ngayHienTai
    ) {
        // ngày cọc = ngày nhận + (số đêm / 2)
        LocalDate ngayCoc = ngayNhan.plusDays(soDem / 2);

        // 1. Trả đúng ngày
        if (ngayHienTai.isEqual(ngayTraDuKien)) {
            return ngayTraDuKien;
        }

        // 2. Trả trước ngày cọc
        if (ngayHienTai.isBefore(ngayCoc)) {
            return ngayCoc;
        }

        // 3. Trả từ ngày cọc trở lên
        if (!ngayHienTai.isAfter(ngayTraDuKien)) {
            return ngayHienTai.plusDays(1); // ngày phạt
        }

        // Mặc định
        return ngayTraDuKien;
    }
    
//    private long parseTien(String text, String label) {
//        return Long.parseLong(
//            text.replace(label, "")
//                .replace(".", "")
//                .replace(",", "")
//                .replace("₫", "")
//                .trim()
//        );
//    }
    
    private void loadKhuyenMai() {
        cboKhuyenMai.removeAllItems();

        cboKhuyenMai.addItem(null); // Không áp dụng KM

        KhuyenMai_DAO dao = new KhuyenMai_DAO();
        for (KhuyenMai km : dao.getKhuyenMaiConHieuLuc()) {
            cboKhuyenMai.addItem(km);
        }
        
        cboKhuyenMai.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                capNhatTongTien();
            }
        });

    }
    private long tinhTienGiam(long tongTienPhong) {

        KhuyenMai km = (KhuyenMai) cboKhuyenMai.getSelectedItem();
        if (km == null) return 0;

        // Không đủ điều kiện áp dụng
        if (tongTienPhong < km.getSoTienApDung()) return 0;

        // Giảm theo %
        if ("%".equalsIgnoreCase(km.getLoaiKhuyenMai())) {

            double tiLe = km.getGiaTriGiam() / 100.0;   // 🔥 QUAN TRỌNG
            long tienGiam = (long) (tongTienPhong * tiLe);

            // Áp dụng giảm tối đa
            if (km.getGiamToiDa() > 0) {
                tienGiam = Math.min(tienGiam, (long) km.getGiamToiDa());
            }

            return tienGiam;
        }

        // Giảm theo tiền
        return Math.min((long) km.getGiaTriGiam(), tongTienPhong);
    }

    private void capNhatTongTien() {

        long tongTienPhong = parseTien(lblTong.getText());
        long tongCPPS = parseTien(lblTongTien.getText());

        long tienGiam = tinhTienGiam(tongTienPhong);

        tongThanhToan = tongTienPhong + tongCPPS - tienGiam;
        if (tongThanhToan < 0) tongThanhToan = 0;

        lblKM.setText("Khuyến mãi: -" + dinhDangTien(tienGiam));
        lblTongTatCa.setText("Tổng tất cả: " + dinhDangTien(tongThanhToan));
    }

    
    private long parseTien(String text) {
        return Long.parseLong(text.replaceAll("[^0-9]", ""));
    }

    private String dinhDangTien(long tien) {
        return String.format("%,d đ", tien);
    }
    

}
