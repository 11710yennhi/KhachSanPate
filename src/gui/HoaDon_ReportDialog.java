package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import dao.HoaDon_DAO;

public class HoaDon_ReportDialog extends JDialog {

    private JTable tblPhong, tblCPPS;
    private DefaultTableModel modelPhong, modelCPPS;

    private JLabel lbMaHD, lbNgay, lbNV, lbPhong, lbKM;
    private JLabel lbTongPhong, lbTongCPPS, lbTongTien, lbTongThanhToan;

    private HoaDon_DAO hdDAO = new HoaDon_DAO();

    public HoaDon_ReportDialog(
            String maHD,
            String maPDP,
            DefaultTableModel modelPhong,
            DefaultTableModel modelCPPS,
            String tongPhong,
            String tongCPPS,
            String tongTien,
            String tongThanhToan
    ) {
        setTitle("In hóa đơn");
        setSize(800, 900);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(10,10));

        add(createHeader(maHD), BorderLayout.NORTH);
        add(createCenter(modelPhong, modelCPPS), BorderLayout.CENTER);
        add(createFooter(
            tongPhong,
            tongCPPS,
            tongTien,
            tongThanhToan
        ), BorderLayout.SOUTH);
    }

    
    private JPanel createHeader(String maHD) {

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(new EmptyBorder(10, 10, 10, 10));

        HoaDon_DAO dao = new HoaDon_DAO();
        Object[] data = dao.xuLyThongTinHoaDonTrongReport(maHD);

        /* ===== NẾU KHÔNG CÓ DỮ LIỆU ===== */
        if (data == null) {
            JLabel lbErr = new JLabel("❌ Không tìm thấy thông tin hóa đơn");
            lbErr.setForeground(Color.RED);
            lbErr.setAlignmentX(Component.CENTER_ALIGNMENT);
            wrapper.add(lbErr);
            return wrapper; // ✅ return panel HỢP LỆ
        }

        /* ================= HEADER THƯƠNG HIỆU ================= */
        JLabel lbTen = new JLabel("🐾 KHÁCH SẠN PATE 🐾");
        lbTen.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbTen.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbDiaChi = new JLabel(
            "12 Nguyễn Văn Bảo, Phường 4, Gò Vấp, TP.HCM"
        );
        lbDiaChi.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbDT = new JLabel("ĐT: (028) 1234 5678");
        lbDT.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        wrapper.add(lbTen);
        wrapper.add(Box.createVerticalStrut(5));
        wrapper.add(lbDiaChi);
        wrapper.add(lbDT);
        wrapper.add(Box.createVerticalStrut(8));
        wrapper.add(sep);
        wrapper.add(Box.createVerticalStrut(10));

        /* ================= THÔNG TIN HÓA ĐƠN ================= */
        JPanel pnInfo = new JPanel();
        pnInfo.setLayout(new BoxLayout(pnInfo, BoxLayout.Y_AXIS));
        pnInfo.setBorder(
            BorderFactory.createTitledBorder("Thông tin hóa đơn")
        );
        pnInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pnInfo.add(createInfoRow("Mã hóa đơn:", data[0]));
        pnInfo.add(createInfoRow("Ngày lập:", data[1]));
        pnInfo.add(createInfoRow("Nhân viên:", data[2]));
        pnInfo.add(createInfoRow(
            "Khuyến mãi:",
            data[3] != null ? data[3] : "Không"
        ));

        wrapper.add(pnInfo);

        return wrapper;
    }


    private JPanel createInfoRow(String title, Object value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));

        JLabel lbTitle = new JLabel(title);
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel lbValue = new JLabel(value.toString());
        lbValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        row.add(lbTitle);
        row.add(lbValue);

        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        return row;
    }

    private JPanel createCenter(
            DefaultTableModel modelPhong,
            DefaultTableModel modelCPPS
    ) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JTable tblPhong = new JTable(copyModel(modelPhong));
        JTable tblCPPS  = new JTable(copyModel(modelCPPS));

        p.add(new JLabel("Chi tiết phòng thuê"));
        p.add(new JScrollPane(tblPhong));
        p.add(Box.createVerticalStrut(10));
        p.add(new JLabel("Chi phí phát sinh"));
        p.add(new JScrollPane(tblCPPS));

        return p;
    }

    private DefaultTableModel copyModel(DefaultTableModel src) {
        DefaultTableModel m = new DefaultTableModel();
        for (int i = 0; i < src.getColumnCount(); i++)
            m.addColumn(src.getColumnName(i));

        for (int r = 0; r < src.getRowCount(); r++) {
            Object[] row = new Object[src.getColumnCount()];
            for (int c = 0; c < src.getColumnCount(); c++)
                row[c] = src.getValueAt(r, c);
            m.addRow(row);
        }
        return m;
    }

    private JPanel createFooter(
            String tongPhong,
            String tongCPPS,
            String tongTien,
            String tongThanhToan
    ) {
        JPanel p = new JPanel(new GridLayout(4,1,5,5));
        p.setBorder(BorderFactory.createTitledBorder("Thanh toán"));

        p.add(new JLabel("Tổng tiền phòng: " + tongPhong));
        p.add(new JLabel("Tổng chi phí phát sinh: " + tongCPPS));
        p.add(new JLabel("Tổng tiền: " + tongTien));
        p.add(new JLabel("Tổng thanh toán: " + tongThanhToan));

        return p;
    }

    
    private double tinhTong(DefaultTableModel m, int col) {
        double t = 0;
        for (int i=0;i<m.getRowCount();i++)
            t += ((Number)m.getValueAt(i,col)).doubleValue();
        return t;
    }


}


