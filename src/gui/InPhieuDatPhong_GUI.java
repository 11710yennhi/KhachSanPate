//package gui;
//
//import java.awt.BorderLayout;
//import java.awt.Component;
//import java.awt.Font;
//import java.awt.GridLayout;
//import java.util.List;
//
//import javax.swing.BorderFactory;
//import javax.swing.Box;
//import javax.swing.BoxLayout;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.SwingConstants;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableModel;
//
//import dao.ChiTietPhieuDatPhong_DAO;
//import dao.KhachHang_DAO;
//import entity.ChiTietPhieuDatPhong;
//import entity.KhachHang;
//import entity.PhieuDatPhong;
//
//
//public class InPhieuDatPhong_GUI extends JFrame {
//	private KhachHang_DAO khd;
//	private ChiTietPhieuDatPhong_DAO dsctpdp;
//
//    public InPhieuDatPhong_GUI(PhieuDatPhong phieu) {
//    	khd= new KhachHang_DAO();
//    	dsctpdp= new ChiTietPhieuDatPhong_DAO();
//    	
//    	KhachHang  kh = khd.getKhachHangTheoMa(phieu.getKhachHang().getMaKhachHang());
//    	List<ChiTietPhieuDatPhong>  danhSachCTPDP = dsctpdp.getChiTietTheoMaPhieu(phieu.getMaPhieuDatPhong());
//          
//        setTitle("Phiếu đặt phòng");
//        setSize(800, 700);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        // Panel chính
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BorderLayout(0, 10));
//        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
//        add(mainPanel);
//
//        // ===== PHẦN TIÊU ĐỀ =====
//        JLabel lblTitle = new JLabel("PHIẾU XÁC NHẬN ĐẶT PHÒNG " + phieu.getMaPhieuDatPhong(), SwingConstants.CENTER);
//        lblTitle.setFont(new Font("Serif", Font.BOLD, 20));
//        mainPanel.add(lblTitle, BorderLayout.NORTH);
//
//        // ===== PHẦN NỘI DUNG =====
//        JPanel centerPanel = new JPanel();
//        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
//        mainPanel.add(centerPanel, BorderLayout.CENTER);
//
//        // --- Thông tin khách sạn ---
//        JLabel lblHotel = new JLabel("Khách sạn Pate", SwingConstants.RIGHT);
//        lblHotel.setFont(new Font("SansSerif", Font.ITALIC, 14));
//        lblHotel.setAlignmentX(Component.RIGHT_ALIGNMENT);
//        centerPanel.add(lblHotel);
//        centerPanel.add(Box.createVerticalStrut(10));
//
//        // --- Thông tin khách hàng ---
//        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
//        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        infoPanel.add(new JLabel("Mã khách hàng: " + kh.getMaKhachHang()));
//        infoPanel.add(new JLabel("Họ tên khách hàng: " + kh.getHoTen()));
//        infoPanel.add(new JLabel("Số điện thoại: " + kh.getSoDienThoai()));
//        infoPanel.add(new JLabel("Quốc tịch Việt Nam: " + (kh.LaNguoiVietNam() ? "Có" : "Không")));
//        centerPanel.add(infoPanel);
//        centerPanel.add(Box.createVerticalStrut(15));
//
//        // --- Bảng chi tiết ---
//        String[] columns = {"STT", "Mã phòng", "Loại phòng", "Ngày nhận", "Ngày trả", "Số đêm", "Giá", "Thành tiền"};
//        DefaultTableModel model = new DefaultTableModel(columns, 0);
//        JTable table = new JTable(model);
//        table.setRowHeight(25);
//
//        int stt = 1;
//        for (ChiTietPhieuDatPhong p : danhSachCTPDP) {
//            Object[] row = {
//                stt++,
//                p.getPhong().getMaPhong(),
//                p.getPhong().getLoaiPhong().getTenLoaiPhong(),
//                p.getNgayNhanThuc(),
//                p.getNgayTraThuc(),
//                p.getSoNgay(),
//                String.format("%,.0f VNĐ", p.getPhong().getLoaiPhong().getGia()),
//                String.format("%,.0f VNĐ", p.getThanhTien()) // nếu có hàm tính tiền
//            };
//            model.addRow(row);
//        }
//
//        JScrollPane scroll = new JScrollPane(table);
//        centerPanel.add(scroll);
//        centerPanel.add(Box.createVerticalStrut(15));
//
//        // --- Tổng tiền & ghi chú ---
//        JPanel bottomPanel = new JPanel();
//        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
//        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
//
//        JLabel lblTongTien = new JLabel("Tổng tiền phòng: " + String.format("%,.0f VNĐ", phieu.getTongTienPhong()));
//        JLabel lblTienCoc = new JLabel("Tiền cọc: " + String.format("%,.0f VNĐ", phieu.getTienCoc()));
//
//        JLabel lblNote1 = new JLabel("Ghi chú: Khách hàng mang theo CCCD hoặc Passport khi nhận phòng.");
//        JLabel lblNote2 = new JLabel("Khách trên 14 tuổi cần giấy tờ tùy thân hợp lệ.");
//        JLabel lblNote3 = new JLabel("Khách dưới 14 tuổi cần được bảo lãnh.");
//
//        lblTongTien.setAlignmentX(Component.LEFT_ALIGNMENT);
//        lblTienCoc.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//        bottomPanel.add(lblTongTien);
//        bottomPanel.add(lblTienCoc);
//        bottomPanel.add(Box.createVerticalStrut(10));
//        bottomPanel.add(lblNote1);
//        bottomPanel.add(lblNote2);
//        bottomPanel.add(lblNote3);
//
//        centerPanel.add(bottomPanel);
//
//        setVisible(true);
//    }
//}
