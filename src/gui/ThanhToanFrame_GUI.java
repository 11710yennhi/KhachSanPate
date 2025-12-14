package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.HoaDon_DAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ThanhToanFrame_GUI extends JFrame implements ActionListener{

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
    private JLabel lblTienThoi;
    private JButton btnXacNhan;
    
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

        JLabel title = new JLabel("Hóa đơn", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(20));

        // ===== Thông tin chung =====
        lblMaHD = createInfoLabel("Mã hóa đơn: ---");
        lblTenKH = createInfoLabel("Tên khách hàng: ---");
        lblNgayNhanTra = createInfoLabel("Ngày nhận: ---    Ngày trả: ---");
        lblMaNV = createInfoLabel("Mã nhân viên: ---");

        content.add(lblMaHD);
        content.add(Box.createVerticalStrut(6));
        content.add(lblTenKH);
        content.add(Box.createVerticalStrut(6));
        content.add(lblNgayNhanTra);
        content.add(Box.createVerticalStrut(6));
        content.add(lblMaNV);
        content.add(Box.createVerticalStrut(18));

        // ===== Bảng phòng =====
        content.add(createSectionLabel("Chi tiết phòng thuê:"));
        tablePhong = createTable(
                new String[]{"STT", "Mã phòng", "Loại phòng", "Ngày nhận", "Ngày trả","Ngày trả thực","Số đêm", "Giá", "Thành tiền"},
                new Object[][]{}
        );
        scrollPhong = new JScrollPane(tablePhong);
        scrollPhong.setPreferredSize(new Dimension(800, 200));
        scrollPhong.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPhong.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        content.add(scrollPhong);
        content.add(Box.createVerticalStrut(18));

        // ===== Bảng chi phí =====
        content.add(createSectionLabel("Chi phí phát sinh:"));
        tableChiPhi = createTable(
                new String[]{"STT", "Tên chi phí", "Số lượng", "Giá", "Thành tiền"},
                new Object[][]{}
        );
        scrollChiPhi = new JScrollPane(tableChiPhi);
        scrollChiPhi.setPreferredSize(new Dimension(800, 150));
        scrollChiPhi.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollChiPhi.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        content.add(scrollChiPhi);
        content.add(Box.createVerticalStrut(18));

        // ===== Tổng tiền =====
        JPanel pAmounts = new JPanel();
        pAmounts.setLayout(new BoxLayout(pAmounts, BoxLayout.Y_AXIS));
        pAmounts.setBackground(Color.WHITE);

        lblTong = createInfoLabel("Tổng: 0");
        lblTongTien = createInfoLabel("Tổng tiền: 0");
        lblKM = createInfoLabel("Khuyến mãi: 0");
        lblTongTatCa = createInfoLabel("Thành tiền: 0");
        lblTienDua = createInfoLabel("Tiền khách đưa: 0");
        lblTienThoi = createInfoLabel("Tiền thối lại: 0");

        pAmounts.add(lblTong);
        pAmounts.add(lblTongTien);
        pAmounts.add(lblKM);
        pAmounts.add(lblTongTatCa);
        pAmounts.add(lblTienDua);
        pAmounts.add(lblTienThoi);

        content.add(pAmounts);
        content.add(Box.createVerticalStrut(20));

        // ===== Nút xác nhận =====
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBtn.setBackground(Color.WHITE);
        btnXacNhan = new JButton("Xác nhận thanh toán");
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        pBtn.add(btnXacNhan);
        content.add(pBtn);

        root.add(content, BorderLayout.CENTER);
        add(new JScrollPane(root), BorderLayout.CENTER);
        //===action===========
        btnXacNhan.addActionListener(this);
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
            newData[i][0] = i + 1;   // STT
            newData[i][1] = data[i][1]; // Mã phòng
            newData[i][2] = data[i][2]; // Loại phòng
            newData[i][3] = data[i][3]; // Ngày nhận
            newData[i][4] = data[i][4]; // Ngày trả
            newData[i][5] = data[i][5]; // Ngày trả thực (mới thêm)
            newData[i][6] = data[i][6]; // Số đêm (đã có trong data)
            newData[i][7] = data[i][7]; // Giá
            newData[i][8] = data[i][8]; // Thành tiền
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
    //===========generate hóa đơn=============
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
        
        tongTienPhong = tongTienPhong.replaceAll("[^\\d.]", "");
        tongThanhToan = tongThanhToan.replaceAll("[^\\d.]", "");

        double tongTienPhongdouble = Double.parseDouble(tongTienPhong);
        double tongThanhToandouble = Double.parseDouble(tongThanhToan);


        double tongTatCa = tongTienPhongdouble + tongThanhToandouble;
        
        DecimalFormat df = new DecimalFormat("#,###.##");
        String tongTatCaStr = df.format(tongTatCa);
        
        lblTongTatCa.setText("Tổng tất cả: " + tongTatCaStr);
        //KhuyenMai
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnXacNhan)) {

            try {
                // ===== LẤY MÃ HÓA ĐƠN =====
                String maHD = lblMaHD.getText().replace("Mã hóa đơn: ", "").trim();

                // ===== LẤY MÃ PHIẾU ĐẶT PHÒNG =====
                String maPDP = maPDPglobal;
                // ===== LẤY MÃ KHUYẾN MÃI (NẾU CÓ) =====
                String maKM = null; 
                // Nếu bạn có ô nhập KM thì lấy ở đây, ví dụ:
                // maKM = txtKhuyenMai.getText().trim();
                // (Nếu null → DAO sẽ tự set NULL)

                // ===== LẤY SỐ TIỀN =====
                String tongTienPhongStr = lblTong.getText().replace("Tổng tiền phòng: ", "").replace(",", "").trim();
                String tongCPPSStr     = lblTongTien.getText().replace("Chi phí phát sinh: ", "").replace(",", "").trim();
                String tongThanhStr    = lblTongTatCa.getText().replace("Tổng tất cả: ", "").replace(",", "").trim();

                double tongTienPhong  = Double.parseDouble(tongTienPhongStr);
                double tongTienCPPS   = Double.parseDouble(tongCPPSStr);
                double tongThanhToan  = Double.parseDouble(tongThanhStr);

                // Tổng tiền = tiền phòng + chi phí
                double tongTien = tongTienPhong + tongTienCPPS;

                // ===== PHƯƠNG THỨC THANH TOÁN =====
                String phuongThucTT = "Tiền mặt"; // nếu có combobox → lấy từ combobox

                // ===== NGÀY TẠO =====
                LocalDate ngayTao = LocalDate.now();

                // ===== GỌI DAO LƯU HÓA ĐƠN =====
                HoaDon_DAO dao = new HoaDon_DAO();

                boolean ok = dao.themHoaDon(
                        maHD,
                        maPDP,
                        maKM,
                        phuongThucTT,
                        tongTienPhong,
                        tongTienCPPS,
                        tongThanhToan,
                        ngayTao
                );
                

                if (ok) {
                    JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
                    dispose(); // đóng giao diện
                } else {
                    JOptionPane.showMessageDialog(this, "Lưu hóa đơn thất bại!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi thanh toán: " + ex.getMessage());
            }
        }
    }
    
    
}
