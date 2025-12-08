package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

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

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HoaDon_GUI extends JPanel implements ActionListener, MouseListener {

    private JTable tblHoaDon, tblChiTiet;
    private DefaultTableModel modelHD, modelCT;

    private JTextField txtTim, txtTuNgay, txtDenNgay, txtTongTien;

    private JButton btTim, btXoa, btLoc, btXoaTrang, btHomNay, btTatCa, btInHD;

    private JLabel lbMaHD, lbNgay, lbPhong, lbNhanVien;

    private HoaDon_DAO hoaDonDAO;
    private ChiTietChiPhiPhatSinh_DAO ctDAO;
    private ChiPhiPhatSinh_DAO cpsDAO;
    private Phong_DAO phongDAO;
    private NhanVien_DAO nvDAO;

    public HoaDon_GUI() {

        setLayout(new BorderLayout());
        setFont(new Font("Arial", Font.PLAIN, 13));

        hoaDonDAO = new HoaDon_DAO();
        ctDAO = new ChiTietChiPhiPhatSinh_DAO();
        cpsDAO = new ChiPhiPhatSinh_DAO();
        phongDAO = new Phong_DAO();
        nvDAO = new NhanVien_DAO();

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
        JPanel pnTop = new JPanel(new FlowLayout());
        txtTuNgay = new JTextField(8);
        txtDenNgay = new JTextField(8);

        btHomNay = new JButton("Hôm nay");
        btTatCa = new JButton("Tất cả");
        btLoc = new JButton("Lọc");

        pnTop.add(btTatCa);
        pnTop.add(btHomNay);
        pnTop.add(new JLabel("Từ ngày:"));
        pnTop.add(txtTuNgay);
        pnTop.add(new JLabel("Đến ngày:"));
        pnTop.add(txtDenNgay);
        pnTop.add(btLoc);
        khung.add(pnTop, BorderLayout.NORTH);

        //====================== BẢNG HÓA ĐƠN ============================
        String[] colHD = {"STT", "Mã hóa đơn", "Mã nhân viên", "Ngày lập", "Phòng", "Tổng tiền"};
        modelHD = new DefaultTableModel(colHD, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblHoaDon = new JTable(modelHD);
        khung.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        //====================== KHUNG DƯỚI ============================
        JPanel pnBot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtTim = new JTextField(10);
        btTim = new JButton("Tìm");
        btXoaTrang = new JButton("Xóa trắng");
        btXoa = new JButton("Xóa");

        pnBot.add(new JLabel("Nhập mã tìm:"));
        pnBot.add(txtTim);
        pnBot.add(btTim);
        pnBot.add(btXoa);
        pnBot.add(btXoaTrang);

        khung.add(pnBot, BorderLayout.SOUTH);

        //====================== KHUNG CHI TIẾT HÓA ĐƠN ============================
        JPanel pnCT = new JPanel();
        pnCT.setLayout(new BoxLayout(pnCT, BoxLayout.Y_AXIS));
        pnCT.setPreferredSize(new Dimension(350, 0));
        pnCT.setBorder(BorderFactory.createTitledBorder("CHI TIẾT"));

        lbMaHD = new JLabel("Mã hóa đơn: ");
        lbNgay = new JLabel("Ngày lập: ");
        lbPhong = new JLabel("Phòng: ");
        lbNhanVien = new JLabel("Nhân viên: ");

        pnCT.add(lbMaHD);
        pnCT.add(lbNhanVien);
        pnCT.add(lbNgay);
        pnCT.add(lbPhong);

        pnCT.add(Box.createVerticalStrut(10));

        String[] colCT = {"STT", "Tên phí", "Số lượng", "Đơn giá", "Thành tiền"};
        modelCT = new DefaultTableModel(colCT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblChiTiet = new JTable(modelCT);
        pnCT.add(new JScrollPane(tblChiTiet));

        pnCT.add(Box.createVerticalStrut(10));
        JPanel pnTong = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTongTien = new JTextField(12);
        txtTongTien.setEditable(false);
        pnTong.add(new JLabel("Tổng tiền:"));
        pnTong.add(txtTongTien);
        pnCT.add(pnTong);

        btInHD = new JButton("In hóa đơn");
        pnCT.add(btInHD);

        add(pnCT, BorderLayout.EAST);

        //====================== LOAD DỮ LIỆU ============================
//        loadHoaDon();

        //====================== SỰ KIỆN ============================
        tblHoaDon.addMouseListener(this);
        btTim.addActionListener(this);
        btXoaTrang.addActionListener(this);
        btTatCa.addActionListener(this);
        btHomNay.addActionListener(this);
        btLoc.addActionListener(this);
        btXoa.addActionListener(this);
        btInHD.addActionListener(this);
    }

    //===========================================================
    //                LOAD DANH SÁCH HÓA ĐƠN
    //===========================================================
//    public void loadHoaDon() {
//        modelHD.setRowCount(0);
//        List<HoaDon> ds = hoaDonDAO.getAllHoaDon();
//        int stt = 1;
//
//        for (HoaDon hd : ds) {
//            modelHD.addRow(new Object[]{
//                    stt++, hd.getMaHoaDon(),
//                    hd.getNhanVien().getMaNV(),
//                    hd.getNgayLap(),
//                    hd.getPhong().getMaPhong(),
//                    hd.getTongTien()
//            });
//        }
//    }

    //===========================================================
    //                HIỂN THỊ CHI TIẾT HÓA ĐƠN
    //===========================================================
//    public void showChiTiet() {
//        int r = tblHoaDon.getSelectedRow();
//        if (r == -1) return;
//
//        String maHD = modelHD.getValueAt(r, 1).toString();
//
//        lbMaHD.setText("Mã hóa đơn: " + maHD);
//        lbNhanVien.setText("Nhân viên: " + modelHD.getValueAt(r, 2).toString());
//        lbNgay.setText("Ngày lập: " + modelHD.getValueAt(r, 3).toString());
//        lbPhong.setText("Phòng: " + modelHD.getValueAt(r, 4).toString());
//        txtTongTien.setText(modelHD.getValueAt(r, 5).toString());
//
//        modelCT.setRowCount(0);
//        List<ChiTietChiPhiPhatSinh> ds = ctDAO.getChiTietByMaHD(maHD);
//        int stt = 1;
//
//        for (ChiTietChiPhiPhatSinh ct : ds) {
//            ChiPhiPhatSinh fee = cpsDAO.getChiPhi(ct.getChiPhi().getMaCP());
//
//            modelCT.addRow(new Object[]{
//                    stt++,
//                    fee.getTenChiPhi(),
//                    ct.getSoLuong(),
//                    fee.getDonGia(),
//                    ct.getThanhTien()
//            });
//        }
//    }


    //===========================================================
    //                ACTION EVENTS
    //===========================================================
    @Override
    public void actionPerformed(ActionEvent e) {

//        Object o = e.getSource();
//
//        if (o.equals(btTatCa)) loadHoaDon();
//
//        if (o.equals(btXoaTrang)) {
//            txtTim.setText("");
//            txtTuNgay.setText("");
//            txtDenNgay.setText("");
//        }
//
//        if (o.equals(btHomNay)) {
//            modelHD.setRowCount(0);
//            List<HoaDon> ds = hoaDonDAO.getAllHoaDon();
//            int stt = 1;
//            LocalDate now = LocalDate.now();
//
//            for (HoaDon hd : ds) {
//                if (hd.getNgayLap().equals(now)) {
//                    modelHD.addRow(new Object[]{
//                            stt++, hd.getMaHoaDon(),
//                            hd.getNhanVien().getMaNV(),
//                            hd.getNgayLap(),
//                            hd.getPhong().getMaPhong(),
//                            hd.getTongTien()
//                    });
//                }
//            }
//        }
//
//        if (o.equals(btTim)) {
//            String ma = txtTim.getText().trim();
//            if (ma.equals("")) {
//                JOptionPane.showMessageDialog(this, "Nhập mã hóa đơn!");
//                return;
//            }
//
//            modelHD.setRowCount(0);
//            List<HoaDon> ds = hoaDonDAO.getAllHoaDon();
//            int stt = 1;
//
//            for (HoaDon hd : ds) {
//                if (hd.getMaHoaDon().equalsIgnoreCase(ma)) {
//                    modelHD.addRow(new Object[]{
//                            stt++, hd.getMaHoaDon(),
//                            hd.getNhanVien().getMaNV(),
//                            hd.getNgayLap(),
//                            hd.getPhong().getMaPhong(),
//                            hd.getTongTien()
//                    });
//                }
//            }
//        }
//
//        if (o.equals(btLoc)) {
//            DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//            try {
//                LocalDate tu = LocalDate.parse(txtTuNgay.getText(), f);
//                LocalDate den = LocalDate.parse(txtDenNgay.getText(), f);
//
//                modelHD.setRowCount(0);
//                int stt = 1;
//
//                for (HoaDon hd : hoaDonDAO.getAllHoaDon()) {
//                    if (!hd.getNgayLap().isBefore(tu) && !hd.getNgayLap().isAfter(den)) {
//                        modelHD.addRow(new Object[]{
//                                stt++, hd.getMaHoaDon(),
//                                hd.getNhanVien().getMaNV(),
//                                hd.getNgayLap(),
//                                hd.getPhong().getMaPhong(),
//                                hd.getTongTien()
//                        });
//                    }
//                }
//
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Ngày không hợp lệ! Định dạng dd/MM/yyyy");
//            }
//        }
//
//        if (o.equals(btXoa)) {
//            int r = tblHoaDon.getSelectedRow();
//            if (r == -1) {
//                JOptionPane.showMessageDialog(this, "Chọn hóa đơn để xóa!");
//                return;
//            }
//
//            String ma = modelHD.getValueAt(r, 1).toString();
//
//            if (JOptionPane.showConfirmDialog(this, "Xóa hóa đơn " + ma + "?") == JOptionPane.YES_OPTION) {
//                ctDAO.xoaChiTietTheoMaHD(ma);
//                hoaDonDAO.xoaHoaDon(ma);
//                loadHoaDon();
//                modelCT.setRowCount(0);
//            }
//        }
//
//        if (o.equals(btInHD)) inHoaDon();
    }

    @Override public void mouseClicked(MouseEvent e) { 
//    	showChiTiet(); 
    	}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

}
