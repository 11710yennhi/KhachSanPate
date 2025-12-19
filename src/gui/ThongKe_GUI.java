package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

// JFreeChart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import dao.ChiTietPhieuDatPhong_DAO;
import dao.HoaDon_DAO;

public class ThongKe_GUI extends JPanel implements ActionListener {

    // ===== MÀU CHỦ ĐẠO =====
    private static final Color COLOR_NAVY = new Color(10, 52, 89);
    private static final Color COLOR_GOLD = new Color(243, 192, 63);
    private static final Color COLOR_CARD_BG = Color.WHITE;
    private static final Color COLOR_TEXT_DARK = new Color(30, 30, 30);
    private static final Color COLOR_KETLUAN_BG = new Color(255, 248, 220);
    private static final Color COLOR_BORDER_CHART = new Color(60, 60, 60);
    private static final Color COLOR_BORDER_SOFT = new Color(225, 225, 225);

    // ===== FONT =====
    private static Font F_UI(int style, int size) { return new Font("Segoe UI", style, size); }

    // ===== KÍCH THƯỚC =====
    private static final int HEIGHT_TOP_CHARTS = 230;
    private static final int HEIGHT_LINE = 190;
    private static final int HEIGHT_BOTTOM = 320;
    private static final int HEIGHT_KETLUAN = 150;

    private static final String TABLE_TITLE = "Bảng doanh thu Khách sạn pate";

    // ✅✅✅ CHỈNH SỬA: giữ lại centerPanel để bật/tắt giao diện thống kê
    private JPanel centerPanel;
    private boolean dashboardVisible = false;

    // ================== BÊN TRÁI ==================
    private JComboBox<Integer> cboThangTrai;
    private JComboBox<Integer> cboNamTrai;
    private JButton btnXemThangTrai;
    private JButton btnXemNamTrai;

    private JPanel panelChartTraiContainer;
    private JPanel panelChartDoanhThuTrai;
    private JPanel panelChartPhoBienLoaiPhongTrai;
    private JPanel panelLineTrai;

    private JTable tableTrai;
    private DefaultTableModel modelTrai;

    private JTextArea txtKetLuanTrai;
    private JPanel pnlKetLuanTrai;

    // ================== BÊN PHẢI ==================
    private JComboBox<Integer> cboThangPhai;
    private JComboBox<Integer> cboNamPhai;
    private JButton btnXemThangPhai;
    private JButton btnXemNamPhai;

    private JPanel panelChartPhaiContainer;
    private JPanel panelChartDoanhThuPhai;
    private JPanel panelChartPhoBienLoaiPhongPhai;
    private JPanel panelLinePhai;

    private JTable tablePhai;
    private DefaultTableModel modelPhai;

    private JTextArea txtKetLuanPhai;
    private JPanel pnlKetLuanPhai;

    // ===== DAO =====
    private HoaDon_DAO hoaDonDAO;
    private ChiTietPhieuDatPhong_DAO ctPdpDAO;
    private DecimalFormat df = new DecimalFormat("#,##0");

    public ThongKe_GUI() {
        hoaDonDAO = new HoaDon_DAO();
        ctPdpDAO = new ChiTietPhieuDatPhong_DAO();

        initGUI();
        loadDefaultValues();

        // ✅✅✅ CHỈNH SỬA: KHÔNG load dữ liệu sẵn nữa
        // capNhatThangTrai();
        // capNhatNamPhai();
    }

    private void initGUI() {
        setLayout(new BorderLayout(8, 8));
        setBackground(COLOR_NAVY);
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // ===== TIÊU ĐỀ =====
        JLabel lblTitle = new JLabel("THỐNG KÊ KHÁCH SẠN PATE", SwingConstants.CENTER);
        lblTitle.setFont(F_UI(Font.BOLD, 26));
        lblTitle.setForeground(COLOR_GOLD);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(2, 0, 6, 0));

        JPanel topFilterPanel = new JPanel(new java.awt.GridLayout(1, 2, 10, 0));
        topFilterPanel.setBackground(COLOR_NAVY);

        int yearNow = LocalDate.now().getYear();

        // --- BÊN TRÁI ---
        JPanel pnlFilterTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        styleFilterPanel(pnlFilterTrai, "Chọn Tháng/Năm");

        pnlFilterTrai.add(createFilterLabel("Tháng:"));
        cboThangTrai = new JComboBox<>();
        for (int m = 1; m <= 12; m++) cboThangTrai.addItem(m);
        pnlFilterTrai.add(cboThangTrai);

        pnlFilterTrai.add(createFilterLabel("Năm:"));
        cboNamTrai = new JComboBox<>();
        for (int y = 2020; y <= yearNow; y++) cboNamTrai.addItem(y);
        pnlFilterTrai.add(cboNamTrai);

        btnXemThangTrai = createPrimaryButton("Tháng");
        btnXemNamTrai = createSecondaryButton("Năm");
        pnlFilterTrai.add(btnXemThangTrai);
        pnlFilterTrai.add(btnXemNamTrai);

        // --- BÊN PHẢI ---
        JPanel pnlFilterPhai = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        styleFilterPanel(pnlFilterPhai, "Chọn Tháng/Năm");

        pnlFilterPhai.add(createFilterLabel("Tháng:"));
        cboThangPhai = new JComboBox<>();
        for (int m = 1; m <= 12; m++) cboThangPhai.addItem(m);
        pnlFilterPhai.add(cboThangPhai);

        pnlFilterPhai.add(createFilterLabel("Năm:"));
        cboNamPhai = new JComboBox<>();
        for (int y = 2020; y <= yearNow; y++) cboNamPhai.addItem(y);
        pnlFilterPhai.add(cboNamPhai);

        btnXemThangPhai = createPrimaryButton("Tháng");
        btnXemNamPhai = createSecondaryButton("Năm");
        pnlFilterPhai.add(btnXemThangPhai);
        pnlFilterPhai.add(btnXemNamPhai);

        topFilterPanel.add(pnlFilterTrai);
        topFilterPanel.add(pnlFilterPhai);

        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.setBackground(COLOR_NAVY);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);
        pnlNorth.add(topFilterPanel, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);

        // ===== TRUNG TÂM (THỐNG KÊ) =====
        centerPanel = new JPanel(new java.awt.GridLayout(1, 2, 10, 0));
        centerPanel.setBackground(COLOR_NAVY);

        String[] cols = { "Kỳ", "Doanh thu phòng", "Dịch vụ", "Phí phạt", "Tổng doanh thu" };

        // ================== PANEL TRÁI ==================
        JPanel leftPanel = new JPanel(new BorderLayout(6, 6));
        leftPanel.setBackground(COLOR_CARD_BG);
        leftPanel.setBorder(createCardBorder(""));

        panelChartTraiContainer = new JPanel(new java.awt.GridLayout(1, 2, 10, 0));
        panelChartTraiContainer.setBackground(COLOR_CARD_BG);
        panelChartTraiContainer.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
        panelChartTraiContainer.setPreferredSize(new Dimension(400, HEIGHT_TOP_CHARTS));

        panelChartDoanhThuTrai = new JPanel(new BorderLayout());
        panelChartDoanhThuTrai.setBackground(COLOR_CARD_BG);

        panelChartPhoBienLoaiPhongTrai = new JPanel(new BorderLayout());
        panelChartPhoBienLoaiPhongTrai.setBackground(COLOR_CARD_BG);

        panelChartTraiContainer.add(panelChartDoanhThuTrai);
        panelChartTraiContainer.add(panelChartPhoBienLoaiPhongTrai);
        leftPanel.add(panelChartTraiContainer, BorderLayout.NORTH);

        panelLineTrai = new JPanel(new BorderLayout());
        panelLineTrai.setBackground(COLOR_CARD_BG);
        panelLineTrai.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        panelLineTrai.setPreferredSize(new Dimension(400, HEIGHT_LINE));
        leftPanel.add(panelLineTrai, BorderLayout.CENTER);

        modelTrai = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableTrai = new JTable(modelTrai);
        styleTable(tableTrai);

        JScrollPane scrollTrai = new JScrollPane(tableTrai);
        scrollTrai.setBorder(createTableBorder(TABLE_TITLE));

        txtKetLuanTrai = createConclusionAreaBold();
        pnlKetLuanTrai = createKetLuanPanel(txtKetLuanTrai);

        JPanel pnlBottomLeft = new JPanel(new BorderLayout(0, 6));
        pnlBottomLeft.setBackground(COLOR_CARD_BG);
        pnlBottomLeft.add(scrollTrai, BorderLayout.CENTER);
        pnlBottomLeft.add(pnlKetLuanTrai, BorderLayout.SOUTH);
        pnlBottomLeft.setPreferredSize(new Dimension(400, HEIGHT_BOTTOM));
        leftPanel.add(pnlBottomLeft, BorderLayout.SOUTH);

        // ================== PANEL PHẢI ==================
        JPanel rightPanel = new JPanel(new BorderLayout(6, 6));
        rightPanel.setBackground(COLOR_CARD_BG);
        rightPanel.setBorder(createCardBorder(""));

        panelChartPhaiContainer = new JPanel(new java.awt.GridLayout(1, 2, 10, 0));
        panelChartPhaiContainer.setBackground(COLOR_CARD_BG);
        panelChartPhaiContainer.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
        panelChartPhaiContainer.setPreferredSize(new Dimension(400, HEIGHT_TOP_CHARTS));

        panelChartDoanhThuPhai = new JPanel(new BorderLayout());
        panelChartDoanhThuPhai.setBackground(COLOR_CARD_BG);

        panelChartPhoBienLoaiPhongPhai = new JPanel(new BorderLayout());
        panelChartPhoBienLoaiPhongPhai.setBackground(COLOR_CARD_BG);

        panelChartPhaiContainer.add(panelChartDoanhThuPhai);
        panelChartPhaiContainer.add(panelChartPhoBienLoaiPhongPhai);
        rightPanel.add(panelChartPhaiContainer, BorderLayout.NORTH);

        panelLinePhai = new JPanel(new BorderLayout());
        panelLinePhai.setBackground(COLOR_CARD_BG);
        panelLinePhai.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        panelLinePhai.setPreferredSize(new Dimension(400, HEIGHT_LINE));
        rightPanel.add(panelLinePhai, BorderLayout.CENTER);

        modelPhai = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablePhai = new JTable(modelPhai);
        styleTable(tablePhai);

        JScrollPane scrollPhai = new JScrollPane(tablePhai);
        scrollPhai.setBorder(createTableBorder(TABLE_TITLE));

        txtKetLuanPhai = createConclusionAreaBold();
        pnlKetLuanPhai = createKetLuanPanel(txtKetLuanPhai);

        JPanel pnlBottomRight = new JPanel(new BorderLayout(0, 6));
        pnlBottomRight.setBackground(COLOR_CARD_BG);
        pnlBottomRight.add(scrollPhai, BorderLayout.CENTER);
        pnlBottomRight.add(pnlKetLuanPhai, BorderLayout.SOUTH);
        pnlBottomRight.setPreferredSize(new Dimension(400, HEIGHT_BOTTOM));
        rightPanel.add(pnlBottomRight, BorderLayout.SOUTH);

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        //  ẩn thống kê lúc mới chạy
        centerPanel.setVisible(false);

        add(centerPanel, BorderLayout.CENTER);

        // ===== SỰ KIỆN =====
        btnXemThangTrai.addActionListener(this);
        btnXemNamTrai.addActionListener(this);
        btnXemThangPhai.addActionListener(this);
        btnXemNamPhai.addActionListener(this);
    }

    //  hàm bật dashboard
    private void showDashboard() {
        if (!dashboardVisible) {
            dashboardVisible = true;
            centerPanel.setVisible(true);
            revalidate();
            repaint();
        }
    }

    // ========== UI HELPERS ==========
    private void styleFilterPanel(JPanel panel, String title) {
        panel.setBackground(COLOR_NAVY);
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_GOLD, 1),
                title
        );
        tb.setTitleColor(COLOR_GOLD);
        tb.setTitleFont(F_UI(Font.BOLD, 12));
        panel.setBorder(tb);
    }

    private JLabel createFilterLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(F_UI(Font.PLAIN, 12));
        return lbl;
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(COLOR_GOLD);
        btn.setForeground(COLOR_NAVY);
        btn.setFocusPainted(false);
        btn.setFont(F_UI(Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        return btn;
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(255, 243, 200));
        btn.setForeground(COLOR_NAVY);
        btn.setFocusPainted(false);
        btn.setFont(F_UI(Font.PLAIN, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        return btn;
    }

    private TitledBorder createCardBorder(String title) {
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_GOLD, 1),
                title
        );
        tb.setTitleColor(COLOR_NAVY);
        tb.setTitleFont(F_UI(Font.BOLD, 13));
        return tb;
    }

    private TitledBorder createTableBorder(String title) {
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BORDER_SOFT, 1),
                title
        );
        tb.setTitleFont(F_UI(Font.BOLD, 12));
        tb.setTitleColor(COLOR_NAVY);
        return tb;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(24);
        table.setFont(F_UI(Font.PLAIN, 12));
        table.setForeground(COLOR_TEXT_DARK);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(COLOR_NAVY);
        header.setForeground(Color.WHITE);
        header.setFont(F_UI(Font.BOLD, 12));
    }

    private JTextArea createConclusionAreaBold() {
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);

        ta.setFont(F_UI(Font.BOLD, 13));
        ta.setForeground(COLOR_NAVY);
        ta.setBackground(COLOR_KETLUAN_BG);
        ta.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return ta;
    }

    private JPanel createKetLuanPanel(JTextArea ta) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(COLOR_KETLUAN_BG);
        p.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER_SOFT),
                        BorderFactory.createEmptyBorder(0, 5, 5, 5)
                )
        );
        p.setPreferredSize(new Dimension(400, HEIGHT_KETLUAN));
        p.add(ta, BorderLayout.CENTER);
        return p;
    }

    private void loadDefaultValues() {
        LocalDate now = LocalDate.now();
        int thangNow = now.getMonthValue();
        int namNow = now.getYear();

        cboThangTrai.setSelectedItem(thangNow);
        cboNamTrai.setSelectedItem(namNow);

        cboThangPhai.setSelectedItem(thangNow);
        cboNamPhai.setSelectedItem(namNow);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // ✅✅✅ CHỈNH SỬA: bấm nút mới hiện thống kê
        showDashboard();

        if (src == btnXemThangTrai) capNhatThangTrai();
        else if (src == btnXemNamTrai) capNhatNamTrai();
        else if (src == btnXemThangPhai) capNhatThangPhai();
        else if (src == btnXemNamPhai) capNhatNamPhai();
    }

    // ================== WRAPPER ==================
    private void capNhatThangTrai() {
        int thang = (Integer) cboThangTrai.getSelectedItem();
        int nam = (Integer) cboNamTrai.getSelectedItem();

        capNhatTheoThang(thang, nam, panelChartDoanhThuTrai, modelTrai, txtKetLuanTrai, true);
        capNhatPhoBienLoaiPhongTheoThang(thang, nam, panelChartPhoBienLoaiPhongTrai);
        capNhatLinePhongDangOTheoThang(thang, nam, panelLineTrai);
    }

    private void capNhatNamTrai() {
        int nam = (Integer) cboNamTrai.getSelectedItem();

        capNhatTheoNam(nam, panelChartDoanhThuTrai, modelTrai, txtKetLuanTrai, true);
        capNhatPhoBienLoaiPhongTheoNam(nam, panelChartPhoBienLoaiPhongTrai);
        capNhatLinePhongDangOTheoNam(nam, panelLineTrai);
    }

    private void capNhatThangPhai() {
        int thang = (Integer) cboThangPhai.getSelectedItem();
        int nam = (Integer) cboNamPhai.getSelectedItem();

        capNhatTheoThang(thang, nam, panelChartDoanhThuPhai, modelPhai, txtKetLuanPhai, true);
        capNhatPhoBienLoaiPhongTheoThang(thang, nam, panelChartPhoBienLoaiPhongPhai);
        capNhatLinePhongDangOTheoThang(thang, nam, panelLinePhai);
    }

    private void capNhatNamPhai() {
        int nam = (Integer) cboNamPhai.getSelectedItem();

        capNhatTheoNam(nam, panelChartDoanhThuPhai, modelPhai, txtKetLuanPhai, true);
        capNhatPhoBienLoaiPhongTheoNam(nam, panelChartPhoBienLoaiPhongPhai);
        capNhatLinePhongDangOTheoNam(nam, panelLinePhai);
    }

    // ================== DOANH THU THEO THÁNG ==================
    private void capNhatTheoThang(int thang, int nam,
                                  JPanel panelChart,
                                  DefaultTableModel model,
                                  JTextArea txtKetLuan,
                                  boolean gopKetLuanHieuSuat) {

        double dtPhongNow = hoaDonDAO.getDoanhThuPhongTheoThang(thang, nam);
        double dtDvNow = hoaDonDAO.getDoanhThuDichVuTheoThang(thang, nam);
        double dtPhatNow = hoaDonDAO.getDoanhThuPhatTheoThang(thang, nam);
        double tongNow = dtPhongNow + dtDvNow + dtPhatNow;

        int thangTruoc = thang - 1;
        int namTruoc = nam;
        if (thangTruoc == 0) { thangTruoc = 12; namTruoc = nam - 1; }

        double dtPhongPrev = hoaDonDAO.getDoanhThuPhongTheoThang(thangTruoc, namTruoc);
        double dtDvPrev = hoaDonDAO.getDoanhThuDichVuTheoThang(thangTruoc, namTruoc);
        double dtPhatPrev = hoaDonDAO.getDoanhThuPhatTheoThang(thangTruoc, namTruoc);
        double tongPrev = dtPhongPrev + dtDvPrev + dtPhatPrev;

        int namCungKy = nam - 1;
        double dtPhongLastYear = hoaDonDAO.getDoanhThuPhongTheoThang(thang, namCungKy);
        double dtDvLastYear = hoaDonDAO.getDoanhThuDichVuTheoThang(thang, namCungKy);
        double dtPhatLastYear = hoaDonDAO.getDoanhThuPhatTheoThang(thang, namCungKy);
        double tongLastYear = dtPhongLastYear + dtDvLastYear + dtPhatLastYear;

        panelChart.removeAll();
        if (tongNow <= 0) {
            panelChart.add(createNoDataLabel("Không có dữ liệu doanh thu cho tháng " + thang + "/" + nam), BorderLayout.CENTER);
        } else {
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Tiền phòng", dtPhongNow);
            dataset.setValue("Dịch vụ", dtDvNow);
            dataset.setValue("Phí phạt", dtPhatNow);

            JFreeChart chart = ChartFactory.createPieChart(
                    "Cơ cấu doanh thu tháng " + thang + "/" + nam, dataset, true, true, false
            );
            stylePieChartDoanhThu(chart);

            ChartPanel cp = createNiceChartPanel(chart, true);
            panelChart.add(cp, BorderLayout.CENTER);
        }
        panelChart.revalidate();
        panelChart.repaint();

        model.setRowCount(0);
        model.addRow(new Object[] { "Tháng " + thang + "/" + nam, formatCurrency(dtPhongNow), formatCurrency(dtDvNow), formatCurrency(dtPhatNow), formatCurrency(tongNow) });
        model.addRow(new Object[] { "Tháng trước (" + thangTruoc + "/" + namTruoc + ")", formatCurrency(dtPhongPrev), formatCurrency(dtDvPrev), formatCurrency(dtPhatPrev), formatCurrency(tongPrev) });
        model.addRow(new Object[] { "Cùng kỳ năm trước (" + thang + "/" + namCungKy + ")", formatCurrency(dtPhongLastYear), formatCurrency(dtDvLastYear), formatCurrency(dtPhatLastYear), formatCurrency(tongLastYear) });

        String ketLuan = gopKetLuanHieuSuat
                ? buildKetLuanThangFullTheoThuTuMoi(tongNow, tongPrev, tongLastYear, thang, nam)
                : buildKetLuanThangText(tongNow, tongPrev, tongLastYear, thang, nam);

        txtKetLuan.setText(ketLuan);
    }

    private String buildKetLuanThangText(double now, double prev, double lastYear, int thang, int nam) {
        StringBuilder sb = new StringBuilder();
        sb.append("Doanh thu tháng ").append(thang).append("/").append(nam).append(": ").append(formatCurrency(now)).append("\n");

        int thangTruoc = thang - 1;
        int namTruoc = nam;
        if (thangTruoc == 0) { thangTruoc = 12; namTruoc = nam - 1; }

        double chenhPrev = now - prev;
        sb.append("So với ").append(thangTruoc).append("/").append(namTruoc).append(": ")
          .append(prev == 0 ? "tăng từ 0 lên " + formatCurrency(now)
                           : (chenhPrev >= 0 ? "tăng " : "giảm ") + formatCurrency(Math.abs(chenhPrev)))
          .append("\n");

        int namCungKy = nam - 1;
        double chenhLast = now - lastYear;
        sb.append("So với cùng kỳ ").append(thang).append("/").append(namCungKy).append(": ")
          .append(lastYear == 0 ? "tăng từ 0 lên " + formatCurrency(now)
                                : (chenhLast >= 0 ? "tăng " : "giảm ") + formatCurrency(Math.abs(chenhLast)));

        return sb.toString();
    }

    private String buildKetLuanThangFullTheoThuTuMoi(double now, double prev, double lastYear, int thang, int nam) {
        StringBuilder sb = new StringBuilder();
        sb.append("KẾT LUẬN THÁNG").append("\n");
        sb.append(ketLuanPhongDangOTheoThang(thang, nam)).append("\n");
        sb.append(ketLuanLoaiPhongUaChuongTheoThang(thang, nam)).append("\n");
        sb.append(buildKetLuanThangText(now, prev, lastYear, thang, nam));
        return sb.toString();
    }

    // ================== DOANH THU THEO NĂM ==================
    private void capNhatTheoNam(int nam,
                                JPanel panelChart,
                                DefaultTableModel model,
                                JTextArea txtKetLuan,
                                boolean gopKetLuanHieuSuat) {

        double dtPhongNow = hoaDonDAO.getDoanhThuPhongTheoNam(nam);
        double dtDvNow = hoaDonDAO.getDoanhThuDichVuTheoNam(nam);
        double dtPhatNow = hoaDonDAO.getDoanhThuPhatTheoNam(nam);
        double tongNow = dtPhongNow + dtDvNow + dtPhatNow;

        double dtPhongPrev = hoaDonDAO.getDoanhThuPhongTheoNam(nam - 1);
        double dtDvPrev = hoaDonDAO.getDoanhThuDichVuTheoNam(nam - 1);
        double dtPhatPrev = hoaDonDAO.getDoanhThuPhatTheoNam(nam - 1);
        double tongPrev = dtPhongPrev + dtDvPrev + dtPhatPrev;

        int thangCao = hoaDonDAO.getThangDoanhThuCaoNhat(nam);
        int thangThap = hoaDonDAO.getThangDoanhThuThapNhat(nam);
        double dtThangCao = hoaDonDAO.getTongDoanhThuThang(thangCao, nam);
        double dtThangThap = hoaDonDAO.getTongDoanhThuThang(thangThap, nam);

        panelChart.removeAll();
        if (tongNow <= 0) {
            panelChart.add(createNoDataLabel("Không có dữ liệu doanh thu cho năm " + nam), BorderLayout.CENTER);
        } else {
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Tiền phòng", dtPhongNow);
            dataset.setValue("Dịch vụ", dtDvNow);
            dataset.setValue("Phí phạt", dtPhatNow);

            JFreeChart chart = ChartFactory.createPieChart(
                    "Cơ cấu doanh thu năm " + nam, dataset, true, true, false
            );
            stylePieChartDoanhThu(chart);

            ChartPanel cp = createNiceChartPanel(chart, true);
            panelChart.add(cp, BorderLayout.CENTER);
        }
        panelChart.revalidate();
        panelChart.repaint();

        model.setRowCount(0);
        model.addRow(new Object[] { "Năm " + nam, formatCurrency(dtPhongNow), formatCurrency(dtDvNow), formatCurrency(dtPhatNow), formatCurrency(tongNow) });
        model.addRow(new Object[] { "Năm " + (nam - 1), formatCurrency(dtPhongPrev), formatCurrency(dtDvPrev), formatCurrency(dtPhatPrev), formatCurrency(tongPrev) });

        String ketLuan = gopKetLuanHieuSuat
                ? buildKetLuanNamFullTheoThuTuMoi(tongNow, tongPrev, nam, thangCao, dtThangCao, thangThap, dtThangThap)
                : buildKetLuanNamText(tongNow, tongPrev, nam, thangCao, dtThangCao, thangThap, dtThangThap);

        txtKetLuan.setText(ketLuan);
    }

    private String buildKetLuanNamText(double now, double prev, int nam,
                                       int thangCao, double dtThangCao,
                                       int thangThap, double dtThangThap) {

        StringBuilder sb = new StringBuilder();
        sb.append("Doanh thu năm ").append(nam).append(": ").append(formatCurrency(now)).append("\n");

        double chenh = now - prev;
        sb.append("So với năm ").append(nam - 1).append(": ")
          .append(prev == 0 ? "tăng từ 0 lên " + formatCurrency(now)
                           : (chenh >= 0 ? "tăng " : "giảm ") + formatCurrency(Math.abs(chenh)))
          .append("\n");

        sb.append("Cao nhất: tháng ").append(thangCao).append(" (").append(formatCurrency(dtThangCao)).append(") | ");
        sb.append("Thấp nhất: tháng ").append(thangThap).append(" (").append(formatCurrency(dtThangThap)).append(")");
        return sb.toString();
    }

    private String buildKetLuanNamFullTheoThuTuMoi(double now, double prev, int nam,
                                                  int thangCao, double dtThangCao,
                                                  int thangThap, double dtThangThap) {

        StringBuilder sb = new StringBuilder();
        sb.append("KẾT LUẬN NĂM").append("\n");
        sb.append(ketLuanPhongDangOTheoNam(nam)).append("\n");
        sb.append(ketLuanLoaiPhongUaChuongTheoNam(nam)).append("\n");
        sb.append(buildKetLuanNamText(now, prev, nam, thangCao, dtThangCao, thangThap, dtThangThap));
        return sb.toString();
    }

    // ================== PIE: PHỔ BIẾN LOẠI PHÒNG ==================
    private void capNhatPhoBienLoaiPhongTheoThang(int thang, int nam, JPanel targetPanel) {
        targetPanel.removeAll();

        PieDataset ds = taoDatasetLoaiPhongTheoThang(thang, nam);
        if (ds == null) {
            targetPanel.add(createNoDataLabel("Không có dữ liệu loại phòng " + thang + "/" + nam), BorderLayout.CENTER);
        } else {
            JFreeChart chart = ChartFactory.createPieChart(
                    "Phổ biến loại phòng - " + thang + "/" + nam, ds, true, true, false
            );
            stylePieChartLoaiPhong(chart);

            ChartPanel cp = createNiceChartPanel(chart, true);
            targetPanel.add(cp, BorderLayout.CENTER);
        }

        targetPanel.revalidate();
        targetPanel.repaint();
    }

    private void capNhatPhoBienLoaiPhongTheoNam(int nam, JPanel targetPanel) {
        targetPanel.removeAll();

        PieDataset ds = taoDatasetLoaiPhongTheoNam(nam);
        if (ds == null) {
            targetPanel.add(createNoDataLabel("Không có dữ liệu loại phòng năm " + nam), BorderLayout.CENTER);
        } else {
            JFreeChart chart = ChartFactory.createPieChart(
                    "Phổ biến loại phòng - " + nam, ds, true, true, false
            );
            stylePieChartLoaiPhong(chart);

            ChartPanel cp = createNiceChartPanel(chart, true);
            targetPanel.add(cp, BorderLayout.CENTER);
        }

        targetPanel.revalidate();
        targetPanel.repaint();
    }

    private PieDataset taoDatasetLoaiPhongTheoThang(int thang, int nam) {
        Map<String, Integer> data = ctPdpDAO.getTongSoNgayOPhongTheoThang(thang, nam);
        if (data == null || data.isEmpty()) return null;

        DefaultPieDataset ds = new DefaultPieDataset();
        for (Map.Entry<String, Integer> e : data.entrySet()) ds.setValue(e.getKey(), e.getValue());
        return ds;
    }

    private PieDataset taoDatasetLoaiPhongTheoNam(int nam) {
        Map<String, Integer> data = ctPdpDAO.getTongSoNgayOPhongTheoNam(nam);
        if (data == null || data.isEmpty()) return null;

        DefaultPieDataset ds = new DefaultPieDataset();
        for (Map.Entry<String, Integer> e : data.entrySet()) ds.setValue(e.getKey(), e.getValue());
        return ds;
    }

    // ================== BAR: PHÒNG ĐANG CÓ KHÁCH ==================
    private void capNhatLinePhongDangOTheoThang(int thang, int nam, JPanel targetPanel) {
        targetPanel.removeAll();

        ChartPanel cp = taoBarChartPhongDangOTheoThang(thang, nam);
        if (cp == null) {
            targetPanel.add(createNoDataLabel("Không có dữ liệu phòng đang ở " + thang + "/" + nam), BorderLayout.CENTER);
        } else {
            setTinhChartPanel(cp);
            cp.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_CHART, 1));
            targetPanel.add(cp, BorderLayout.CENTER);
        }

        targetPanel.revalidate();
        targetPanel.repaint();
    }

    private void capNhatLinePhongDangOTheoNam(int nam, JPanel targetPanel) {
        targetPanel.removeAll();

        ChartPanel cp = taoBarChartPhongDangOTheoNam(nam);
        if (cp == null) {
            targetPanel.add(createNoDataLabel("Không có dữ liệu phòng đang ở năm " + nam), BorderLayout.CENTER);
        } else {
            setTinhChartPanel(cp);
            cp.setBorder(BorderFactory.createLineBorder(COLOR_BORDER_CHART, 1));
            targetPanel.add(cp, BorderLayout.CENTER);
        }

        targetPanel.revalidate();
        targetPanel.repaint();
    }

    private void setTinhChartPanel(ChartPanel cp) {
        cp.setMouseWheelEnabled(false);
        cp.setDomainZoomable(false);
        cp.setRangeZoomable(false);
        cp.setPopupMenu(null);
        cp.setMouseZoomable(false);
    }

    private Map<Integer, Integer> tinhSoPhongDangOTheoNgay(int thang, int nam) {
        Map<Integer, Integer> map = new LinkedHashMap<>();

        List<LocalDate[]> list = ctPdpDAO.getDanhSachPhongDangOTrongThang(thang, nam);
        if (list == null || list.isEmpty()) return map;

        LocalDate start = LocalDate.of(nam, thang, 1);
        int soNgay = start.lengthOfMonth();

        for (int day = 1; day <= soNgay; day++) {
            LocalDate current = LocalDate.of(nam, thang, day);
            int count = 0;

            for (LocalDate[] range : list) {
                if (!current.isBefore(range[0]) && current.isBefore(range[1])) count++;
            }
            map.put(day, count);
        }
        return map;
    }

    private CategoryDataset taoDatasetPhongDangOTheoThang(int thang, int nam) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Integer, Integer> data = tinhSoPhongDangOTheoNgay(thang, nam);

        for (Map.Entry<Integer, Integer> e : data.entrySet()) {
            dataset.addValue(e.getValue(), "Số phòng đang ở", String.valueOf(e.getKey()));
        }
        return dataset;
    }

    private ChartPanel taoBarChartPhongDangOTheoThang(int thang, int nam) {
        Map<Integer, Integer> dataCheck = tinhSoPhongDangOTheoNgay(thang, nam);
        if (dataCheck == null || dataCheck.isEmpty()) return null;

        CategoryDataset dataset = taoDatasetPhongDangOTheoThang(thang, nam);

        JFreeChart chart = ChartFactory.createBarChart(
            "Số phòng đang có khách ở trong tháng " + thang + "/" + nam,
            "Ngày", "Số phòng", dataset
        );

        styleBarChartPhongDangOTheoThang(chart);
        return new ChartPanel(chart);
    }

    private void styleBarChartPhongDangOTheoThang(JFreeChart chart) {
        chart.setBackgroundPaint(COLOR_CARD_BG);
        chart.getTitle().setFont(F_UI(Font.BOLD, 13));
        chart.getTitle().setPaint(COLOR_NAVY);

        chart.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(250, 250, 252));
        plot.setOutlinePaint(COLOR_BORDER_SOFT);
        plot.setRangeGridlinePaint(new Color(230, 232, 236));
        plot.setDomainGridlinesVisible(false);

        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setLabelFont(F_UI(Font.PLAIN, 12));
        xAxis.setTickLabelFont(F_UI(Font.PLAIN, 11));

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setLabelFont(F_UI(Font.PLAIN, 12));
        yAxis.setTickLabelFont(F_UI(Font.PLAIN, 11));
        yAxis.setLowerBound(0);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(0.02);
        renderer.setMaximumBarWidth(0.06);
    }

    private Map<Integer, Double> tinhSoPhongDangOTrungBinhTheoThang(int nam) {
        Map<Integer, Double> map = new LinkedHashMap<>();

        List<LocalDate[]> list = ctPdpDAO.getDanhSachPhongDangOTrongNam(nam);
        if (list == null || list.isEmpty()) return map;

        for (int thang = 1; thang <= 12; thang++) {
            LocalDate start = LocalDate.of(nam, thang, 1);
            int soNgay = start.lengthOfMonth();

            int tongPhongNgay = 0;
            for (int day = 1; day <= soNgay; day++) {
                LocalDate current = LocalDate.of(nam, thang, day);
                for (LocalDate[] range : list) {
                    if (!current.isBefore(range[0]) && current.isBefore(range[1])) tongPhongNgay++;
                }
            }

            double trungBinh = (double) tongPhongNgay / soNgay;
            map.put(thang, trungBinh);
        }
        return map;
    }

    private CategoryDataset taoDatasetPhongDangOTheoNam(int nam) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Integer, Double> data = tinhSoPhongDangOTrungBinhTheoThang(nam);

        for (Map.Entry<Integer, Double> e : data.entrySet()) {
            dataset.addValue(e.getValue(), "Số phòng TB/Tháng", String.valueOf(e.getKey()));
        }
        return dataset;
    }

    private ChartPanel taoBarChartPhongDangOTheoNam(int nam) {
        Map<Integer, Double> dataCheck = tinhSoPhongDangOTrungBinhTheoThang(nam);
        if (dataCheck == null || dataCheck.isEmpty()) return null;

        CategoryDataset dataset = taoDatasetPhongDangOTheoNam(nam);

        JFreeChart chart = ChartFactory.createBarChart(
            "Số phòng đang ở trung bình theo tháng năm " + nam,
            "Tháng", "Số phòng (TB/Tháng)", dataset
        );

        styleBarChartPhongDangOTheoNam(chart);
        return new ChartPanel(chart);
    }

    private void styleBarChartPhongDangOTheoNam(JFreeChart chart) {
        chart.setBackgroundPaint(COLOR_CARD_BG);
        chart.getTitle().setFont(F_UI(Font.BOLD, 13));
        chart.getTitle().setPaint(COLOR_NAVY);

        chart.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(250, 250, 252));
        plot.setOutlinePaint(COLOR_BORDER_SOFT);
        plot.setRangeGridlinePaint(new Color(230, 232, 236));
        plot.setDomainGridlinesVisible(false);

        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setLabelFont(F_UI(Font.PLAIN, 12));
        xAxis.setTickLabelFont(F_UI(Font.PLAIN, 11));

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setLabelFont(F_UI(Font.PLAIN, 12));
        yAxis.setTickLabelFont(F_UI(Font.PLAIN, 11));
        yAxis.setLowerBound(0);
        yAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(0.02);
        renderer.setMaximumBarWidth(0.08);
    }

    // ================== KẾT LUẬN GỘP ==================
    private String ketLuanPhongDangOTheoThang(int thang, int nam) {
        Map<Integer, Integer> data = tinhSoPhongDangOTheoNgay(thang, nam);
        if (data == null || data.isEmpty()) return "Phòng đang ở: Không có dữ liệu.";

        int min = Collections.min(data.values());
        int max = Collections.max(data.values());

        List<Integer> ngayMin = new ArrayList<>();
        List<Integer> ngayMax = new ArrayList<>();

        for (Map.Entry<Integer, Integer> e : data.entrySet()) {
            if (e.getValue() == min) ngayMin.add(e.getKey());
            if (e.getValue() == max) ngayMax.add(e.getKey());
        }

        return String.format(
            "Phòng đang ở: thấp nhất %d (ngày %s), cao nhất %d (ngày %s).",
            min, dinhDangKhoangGiaTri(ngayMin), max, dinhDangKhoangGiaTri(ngayMax)
        );
    }

    private String ketLuanPhongDangOTheoNam(int nam) {
        Map<Integer, Double> data = tinhSoPhongDangOTrungBinhTheoThang(nam);
        if (data == null || data.isEmpty()) return "Phòng đang ở TB: Không có dữ liệu.";

        double min = Collections.min(data.values());
        double max = Collections.max(data.values());

        List<Integer> thangMin = new ArrayList<>();
        List<Integer> thangMax = new ArrayList<>();

        for (Map.Entry<Integer, Double> e : data.entrySet()) {
            if (Double.compare(e.getValue(), min) == 0) thangMin.add(e.getKey());
            if (Double.compare(e.getValue(), max) == 0) thangMax.add(e.getKey());
        }

        return String.format(
            "Phòng đang ở TB: thấp nhất %.1f (tháng %s), cao nhất %.1f (tháng %s).",
            min, dinhDangKhoangGiaTri(thangMin), max, dinhDangKhoangGiaTri(thangMax)
        );
    }

    private String ketLuanLoaiPhongUaChuongTheoThang(int thang, int nam) {
        Map<String, Integer> data = ctPdpDAO.getTongSoNgayOPhongTheoThang(thang, nam);
        if (data == null || data.isEmpty()) return "Loại phòng: Không có dữ liệu.";

        int max = Collections.max(data.values());
        List<String> loaiMax = new ArrayList<>();
        for (Map.Entry<String, Integer> e : data.entrySet()) if (e.getValue() == max) loaiMax.add(e.getKey());

        return String.format("Loại phòng ưa chuộng: %s (%d ngày-ở).", String.join(", ", loaiMax), max);
    }

    private String ketLuanLoaiPhongUaChuongTheoNam(int nam) {
        Map<String, Integer> data = ctPdpDAO.getTongSoNgayOPhongTheoNam(nam);
        if (data == null || data.isEmpty()) return "Loại phòng: Không có dữ liệu.";

        int max = Collections.max(data.values());
        List<String> loaiMax = new ArrayList<>();
        for (Map.Entry<String, Integer> e : data.entrySet()) if (e.getValue() == max) loaiMax.add(e.getKey());

        return String.format("Loại phòng ưa chuộng: %s (%d ngày-ở).", String.join(", ", loaiMax), max);
    }

    private String dinhDangKhoangGiaTri(List<Integer> list) {
        if (list == null || list.isEmpty()) return "";
        Collections.sort(list);

        StringBuilder sb = new StringBuilder();
        int start = list.get(0);
        int prev = start;

        for (int i = 1; i < list.size(); i++) {
            int curr = list.get(i);
            if (curr == prev + 1) prev = curr;
            else {
                appendRange(sb, start, prev);
                sb.append(", ");
                start = prev = curr;
            }
        }
        appendRange(sb, start, prev);
        return sb.toString();
    }

    private void appendRange(StringBuilder sb, int start, int end) {
        if (start == end) sb.append(start);
        else sb.append(start).append("–").append(end);
    }

    // ================== SUPPORT ==================
    private JLabel createNoDataLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(F_UI(Font.ITALIC, 13));
        lbl.setForeground(Color.DARK_GRAY);
        return lbl;
    }

    private ChartPanel createNiceChartPanel(JFreeChart chart, boolean darkBorder) {
        ChartPanel cp = new ChartPanel(chart);
        setTinhChartPanel(cp);
        cp.setBackground(COLOR_CARD_BG);
        cp.setBorder(BorderFactory.createLineBorder(darkBorder ? COLOR_BORDER_CHART : COLOR_BORDER_SOFT, 1));
        return cp;
    }

    private String formatCurrency(double value) {
        return df.format(value) + " VND";
    }

    private void stylePieChartDoanhThu(JFreeChart chart) {
        chart.setBackgroundPaint(COLOR_CARD_BG);
        chart.getTitle().setFont(F_UI(Font.BOLD, 14));
        chart.getTitle().setPaint(COLOR_NAVY);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(F_UI(Font.PLAIN, 11));
        plot.setBackgroundPaint(COLOR_CARD_BG);
        plot.setOutlineVisible(false);
        plot.setSimpleLabels(false);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})"));

        if (chart.getLegend() != null) chart.getLegend().setItemFont(F_UI(Font.PLAIN, 11));
    }

    private void stylePieChartLoaiPhong(JFreeChart chart) {
        chart.setBackgroundPaint(COLOR_CARD_BG);
        chart.getTitle().setFont(F_UI(Font.BOLD, 14));
        chart.getTitle().setPaint(COLOR_NAVY);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(F_UI(Font.PLAIN, 11));
        plot.setBackgroundPaint(COLOR_CARD_BG);
        plot.setOutlineVisible(false);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})"));

        if (chart.getLegend() != null) chart.getLegend().setItemFont(F_UI(Font.PLAIN, 11));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Thống kê khách sạn Pate");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new ThongKe_GUI());
            frame.setVisible(true);
        });
    }
}
