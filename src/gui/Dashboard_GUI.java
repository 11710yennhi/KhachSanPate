package gui;

import dao.ChiTietPhieuDatPhong_DAO;
import dao.HoaDon_DAO;
import dao.PhieuDatPhong_DAO;
import dao.Phong_DAO;
import entity.ChiTietPhieuDatPhong;
import entity.PhieuDatPhong;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Dashboard_GUI extends JPanel {

    // ===== THEME =====
    private static final Color COLOR_NAVY = new Color(10, 52, 89);
    private static final Color COLOR_GOLD = new Color(243, 192, 63);

    private static final Color BG1 = new Color(10, 52, 89);
    private static final Color BG2 = new Color(17, 88, 140);

    private static final Color CARD_BG = Color.WHITE;
    private static final Color MUTED = new Color(102, 112, 133);

    private static final Color UP = new Color(0, 150, 80);
    private static final Color DOWN = new Color(220, 50, 50);
    private static final Color WARN = new Color(217, 119, 6);

    // ===== GRADIENT COLORS FOR METRIC CARDS =====
    private static final Color GRAD_DT_TOP   = new Color(255, 224, 153);
    private static final Color GRAD_DT_BOT   = new Color(255, 255, 255);

    private static final Color GRAD_CI_TOP   = new Color(190, 224, 255);
    private static final Color GRAD_CI_BOT   = new Color(255, 255, 255);

    private static final Color GRAD_SAFE_TOP = new Color(216, 198, 255);
    private static final Color GRAD_SAFE_BOT = new Color(255, 255, 255);

    // ===== CONFIG =====
    private final Phong_DAO pDao = new Phong_DAO();
    private final HoaDon_DAO hdDAO = new HoaDon_DAO();
    private final PhieuDatPhong_DAO pdpDAO = new PhieuDatPhong_DAO();
    private final ChiTietPhieuDatPhong_DAO ctPdpDAO = new ChiTietPhieuDatPhong_DAO();
    private final DecimalFormat dfMoney = new DecimalFormat("#,##0");

    private static final long BASE_CASH_IN_SAFE = 10_000_000L;

    // header
    private JButton btnLamMoi;

    // metric cards
    private MetricCard cardDoanhThu;
    private MetricCard cardCheckin;
    private MetricCard cardTienKet;

    // charts
    private JPanel pnlPieNguonDoanhThu;
    private JPanel pnlPiePhongHomNay;
    private JPanel pnlBarMatDo;

    // warning
    private JPanel pnlCanhBaoBody;

    public Dashboard_GUI() {
        setOpaque(false);
        initGUI();
        loadDashboard();
    }

    // ================== UI ==================
    private void initGUI() {
        setLayout(new BorderLayout(14, 14));
        setBorder(new EmptyBorder(14, 14, 14, 14));

        // ===== HEADER =====
        JPanel header = new GradientPanel(BG1, BG2);
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel title = new JLabel("Dashboard - QLKS Pate");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        header.add(title, BorderLayout.WEST);

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnLamMoi.setBackground(COLOR_GOLD);
        btnLamMoi.setForeground(COLOR_NAVY);
        btnLamMoi.setBorder(new CompoundBorder(new RoundBorder(14, COLOR_GOLD), new EmptyBorder(8, 14, 8, 14)));
        btnLamMoi.addActionListener(e -> loadDashboard());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(btnLamMoi);

        header.add(right, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ===== CONTENT =====
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.weightx = 1;
        g.fill = GridBagConstraints.BOTH;
        g.insets = new Insets(10, 0, 0, 0);

        // ===== ROW 1: metric cards =====
        JPanel row1 = new JPanel(new GridLayout(1, 3, 16, 16));
        row1.setOpaque(false);

        cardDoanhThu = new MetricCard("DOANH THU THÁNG NÀY", "💰", true);
        cardCheckin  = new MetricCard("LƯỢT CHECK-IN THÀNH CÔNG", "📌", true);
        cardTienKet  = new MetricCard("TIỀN TRONG KÉT (HÔM NAY)", "💵", true);

        row1.add(cardDoanhThu);
        row1.add(cardCheckin);
        row1.add(cardTienKet);

        g.gridy = 0;
        g.weighty = 0.10; // ✅ giảm nhẹ row 1
        content.add(row1, g);

        // ===== ROW 2: 2 pie (RỘNG / TO HƠN) =====
        JPanel row2 = new JPanel(new GridLayout(1, 2, 16, 16));
        row2.setOpaque(false);

        pnlPieNguonDoanhThu = new JPanel(new BorderLayout());
        pnlPieNguonDoanhThu.setBackground(CARD_BG);
        JPanel card4 = wrapCard(" NGUỒN DOANH THU THÁNG NÀY", pnlPieNguonDoanhThu);

        pnlPiePhongHomNay = new JPanel(new BorderLayout());
        pnlPiePhongHomNay.setBackground(CARD_BG);
        JPanel card5 = wrapCard(" TÌNH TRẠNG PHÒNG HÔM NAY", pnlPiePhongHomNay);

        row2.add(card4);
        row2.add(card5);

        g.gridy = 1;
        g.weighty = 0.74; // ✅ tăng mạnh row 2 để 2 pie “rộng/to” ra
        content.add(row2, g);

        // ===== ROW 3: Warning + Bar (THU HẸP) =====
        JPanel row3 = new JPanel(new GridBagLayout());
        row3.setOpaque(false);

        GridBagConstraints r = new GridBagConstraints();
        r.gridy = 0;
        r.weighty = 1;
        r.fill = GridBagConstraints.BOTH;

        // Warning card
        pnlCanhBaoBody = new JPanel(new BorderLayout(10, 10));
        pnlCanhBaoBody.setBackground(CARD_BG);
        pnlCanhBaoBody.setBorder(new EmptyBorder(8, 8, 8, 8)); // ✅ giảm padding cho gọn
        JPanel card6 = wrapCard("CẢNH BÁO NGÀY HIỆN TẠI", pnlCanhBaoBody);

        r.gridx = 0;
        r.weightx = 0.55; // ✅ cảnh báo hẹp hơn (giữ như bạn set)
        r.insets = new Insets(0, 0, 0, 0);
        row3.add(card6, r);

        // Bar card
        pnlBarMatDo = new JPanel(new BorderLayout());
        pnlBarMatDo.setBackground(CARD_BG);
        pnlBarMatDo.setBorder(new EmptyBorder(4, 4, 4, 4)); // ✅ giảm padding để card gọn
        JPanel card7 = wrapCard(" MẬT ĐỘ PHÒNG Ở THEO NGÀY CỦA THÁNG NÀY", pnlBarMatDo);

        r.gridx = 1;
        r.weightx = 2.45; // ✅ bar rộng hơn trong row 3
        r.insets = new Insets(0, 16, 0, 0);
        row3.add(card7, r);

        g.gridy = 2;
        g.weighty = 0.16; // ✅ giảm row 3 để “thu hẹp cảnh báo + mật độ phòng”
        content.add(row3, g);

        add(content, BorderLayout.CENTER);
    }

    // ================== LOAD DATA ==================
    private void loadDashboard() {
        LocalDate today = LocalDate.now();
        int thang = today.getMonthValue();
        int nam = today.getYear();

        int prevThang = thang - 1;
        int prevNam = nam;
        if (prevThang == 0) { prevThang = 12; prevNam = nam - 1; }

        // ===== DOANH THU =====
        double nowPhong = hdDAO.getDoanhThuPhongTheoThang(thang, nam);
        double nowDv    = hdDAO.getDoanhThuDichVuTheoThang(thang, nam);
        double nowPhat  = hdDAO.getDoanhThuPhatTheoThang(thang, nam);
        double doanhThuNow = nowPhong + nowDv + nowPhat;

        double prevPhong = hdDAO.getDoanhThuPhongTheoThang(prevThang, prevNam);
        double prevDv    = hdDAO.getDoanhThuDichVuTheoThang(prevThang, prevNam);
        double prevPhat  = hdDAO.getDoanhThuPhatTheoThang(prevThang, prevNam);
        double doanhThuPrev = prevPhong + prevDv + prevPhat;

        cardDoanhThu.setValue(dfMoney.format(doanhThuNow) + " VND");
        cardDoanhThu.setDeltaMoney(doanhThuNow, doanhThuPrev, "So với tháng trước");

        // ===== CHECK-IN =====
        int soPhieuNow  = ctPdpDAO.getSoPhieuCheckInThanhCongTrongThang(thang, nam);
        int soPhieuPrev = ctPdpDAO.getSoPhieuCheckInThanhCongTrongThang(prevThang, prevNam);

        cardCheckin.setValue(soPhieuNow + " lượt");
        cardCheckin.setDeltaInt(soPhieuNow, soPhieuPrev, "So với tháng trước");

        // ===== TIỀN KÉT =====
        double tienMatHomNay = hdDAO.getTienMatKetTrongNgay(today);
        long tienTrongKet = BASE_CASH_IN_SAFE + Math.round(tienMatHomNay);

        cardTienKet.setValue(dfMoney.format(tienTrongKet) + " VND");
        cardTienKet.setSubText("Mặc định 10.000.000 + thu tiền mặt hôm nay");
        cardTienKet.setAccent(COLOR_NEUTRAL());

        // ===== PIEs =====
        buildPieNguonDoanhThu(thang, nam);
        buildPiePhongHomNay(today);

        // ===== WARNING =====
        buildCanhBaoNoChart();

        // ===== BAR =====
        buildBarMatDo_IntegerY(thang, nam);
    }

    // ===================== PIE: NGUỒN DOANH THU =====================
    private void buildPieNguonDoanhThu(int thang, int nam) {
        double dtPhong = hdDAO.getDoanhThuPhongTheoThang(thang, nam);
        double dtDv    = hdDAO.getDoanhThuDichVuTheoThang(thang, nam);
        double dtPhat  = hdDAO.getDoanhThuPhatTheoThang(thang, nam);

        pnlPieNguonDoanhThu.removeAll();

        if (dtPhong <= 0 && dtDv <= 0 && dtPhat <= 0) {
            pnlPieNguonDoanhThu.add(emptyState("Không có dữ liệu doanh thu tháng " + thang + "/" + nam), BorderLayout.CENTER);
        } else {
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Tiền phòng", dtPhong);
            dataset.setValue("Dịch vụ", dtDv);
            dataset.setValue("Phí phạt", dtPhat);

            JFreeChart chart = ChartFactory.createPieChart(null, dataset, true, true, false);
            stylePieBigger(chart);

            ChartPanel cp = new ChartPanel(chart);
            cp.setBorder(new EmptyBorder(0, 0, 0, 0));
            cp.setOpaque(false);
            cp.setMouseWheelEnabled(true);

            pnlPieNguonDoanhThu.add(cp, BorderLayout.CENTER);
        }

        pnlPieNguonDoanhThu.revalidate();
        pnlPieNguonDoanhThu.repaint();
    }

    // ===================== PIE: TÌNH TRẠNG PHÒNG =====================
    private void buildPiePhongHomNay(LocalDate today) {
        int coKhach = ctPdpDAO.countPhongDangCoKhach();
        int baoTri  = pDao.countPhongBaoTri();
        int trong   = pDao.getAllPhong().size() - coKhach - baoTri;
        if (trong < 0) trong = 0;

        pnlPiePhongHomNay.removeAll();

        DefaultPieDataset ds = new DefaultPieDataset();
        ds.setValue("Đang ở", coKhach);
        ds.setValue("Trống", trong);
        ds.setValue("Bảo trì", baoTri);

        JFreeChart chart = ChartFactory.createPieChart(null, ds, true, true, false);
        stylePieBigger(chart);

        ChartPanel cp = new ChartPanel(chart);
        cp.setBorder(new EmptyBorder(0, 0, 0, 0));
        cp.setOpaque(false);
        cp.setMouseWheelEnabled(true);

        pnlPiePhongHomNay.add(cp, BorderLayout.CENTER);

        pnlPiePhongHomNay.revalidate();
        pnlPiePhongHomNay.repaint();
    }

    // ===================== BAR: MẬT ĐỘ (Y = integer) =====================
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

    private void buildBarMatDo_IntegerY(int thang, int nam) {
        Map<Integer, Integer> map = tinhSoPhongDangOTheoNgay(thang, nam);

        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            ds.addValue(e.getValue(), "Số phòng đang ở", String.valueOf(e.getKey()));
        }

        JFreeChart chart = ChartFactory.createBarChart(null, "Ngày", "Số phòng", ds);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();

        yAxis.setLowerBound(0);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setTickUnit(new NumberTickUnit(1));

        plot.setBackgroundPaint(new Color(250, 250, 252));
        plot.setRangeGridlinePaint(new Color(230, 232, 236));

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(0.02);
        renderer.setMaximumBarWidth(0.06);

        pnlBarMatDo.removeAll();
        ChartPanel cp = new ChartPanel(chart);
        cp.setMouseWheelEnabled(true);
        pnlBarMatDo.add(cp, BorderLayout.CENTER);
        pnlBarMatDo.revalidate();
        pnlBarMatDo.repaint();
    }

    // ===================== WARNING: NO CHART =====================
    private String xacDinhTrangThai(PhieuDatPhong p) {
        List<ChiTietPhieuDatPhong> ds = ctPdpDAO.getChiTietTheoMaPhieu(p.getMaPhieuDatPhong());
        if (p == null || ds == null || ds.isEmpty()) return "Không có chi tiết";

        LocalDate today = LocalDate.now();

        Optional<LocalDate> minNhan = ds.stream()
                .map(ChiTietPhieuDatPhong::getNgayNhanThuc)
                .filter(d -> d != null)
                .min(LocalDate::compareTo);

        Optional<LocalDate> maxTra = ds.stream()
                .map(ChiTietPhieuDatPhong::getNgayTraThuc)
                .filter(d -> d != null)
                .max(LocalDate::compareTo);

        String trangThai = p.getTrangThai();
        LocalDate nhanSomNhat = minNhan.orElse(null);
        LocalDate traTreNhat = maxTra.orElse(null);

        if ("Đã hủy".equals(trangThai)) return "Đã hủy";

        if ("Đang ở".equals(trangThai)) {
            if (traTreNhat != null && today.isAfter(traTreNhat)) return "Trễ hạn trả phòng";
            if (traTreNhat != null && today.isEqual(traTreNhat)) return "Tới ngày trả";
            return "Đang ở";
        }

        if ("Đã đặt".equals(trangThai)) {
            if (nhanSomNhat != null && nhanSomNhat.isBefore(today)) return "Chưa nhận phòng";
            if (nhanSomNhat != null && today.isEqual(nhanSomNhat)) return "Tới ngày nhận";
            if (nhanSomNhat != null && today.isBefore(nhanSomNhat)) return "Đã đặt";
        }

        return "Hoàn thành";
    }

    private int countCheckTrangThaiPDP(String trangThai) {
        List<PhieuDatPhong> ds = pdpDAO.getAllPhieuDatPhong();
        return (int) ds.stream().filter(pdp -> xacDinhTrangThai(pdp).equals(trangThai)).count();
    }

    private void buildCanhBaoNoChart() {
        pnlCanhBaoBody.removeAll();

        int canIn  = countCheckTrangThaiPDP("Tới ngày nhận");
        int canOut = countCheckTrangThaiPDP("Tới ngày trả");

        JPanel kpiCol = new JPanel(new GridLayout(2, 1, 0, 8)); // ✅ gọn hơn
        kpiCol.setOpaque(false);
        kpiCol.add(buildKpiPill("Check-in cần xử lý", canIn, new Color(225, 248, 235), UP));
        kpiCol.add(buildKpiPill("Check-out cần xử lý", canOut, new Color(255, 242, 226), WARN));

        JLabel tip = new JLabel("<html><span style='color:#667085'>Lưu ý:</span> Nhấn Làm mới sau khi có phát sinh phiếu mới.</html>");
        tip.setFont(new Font("Tahoma", Font.PLAIN, 12));
        tip.setForeground(MUTED);

        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.add(Box.createVerticalStrut(6));
        wrap.add(kpiCol);
        wrap.add(Box.createVerticalGlue());
        wrap.add(Box.createVerticalStrut(6));
        wrap.add(tip);

        pnlCanhBaoBody.add(wrap, BorderLayout.CENTER);
        pnlCanhBaoBody.revalidate();
        pnlCanhBaoBody.repaint();
    }

    private JPanel buildKpiPill(String title, int value, Color bg, Color accent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bg);
        p.setBorder(new CompoundBorder(new RoundBorder(14, bg.darker()), new EmptyBorder(10, 12, 10, 12)));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Tahoma", Font.BOLD, 12));
        t.setForeground(COLOR_NAVY);

        JLabel v = new JLabel(String.valueOf(value), SwingConstants.RIGHT);
        v.setFont(new Font("Tahoma", Font.BOLD, 20));
        v.setForeground(accent);

        p.add(t, BorderLayout.WEST);
        p.add(v, BorderLayout.EAST);
        return p;
    }

    // ===================== PIE STYLE: TO HƠN =====================
    private void stylePieBigger(JFreeChart chart) {
        chart.setBackgroundPaint(CARD_BG);
        chart.setPadding(new RectangleInsets(2, 2, 2, 2));
        chart.setTitle((TextTitle) null);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(CARD_BG);
        plot.setOutlineVisible(false);

        plot.setInteriorGap(0.02);
        plot.setLabelGap(0.02);
        plot.setShadowPaint(null);

        plot.setLabelFont(new Font("Tahoma", Font.PLAIN, 11));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0} ({2})",
                new DecimalFormat("#,##0"),
                new DecimalFormat("0%")
        ));

        LegendTitle legend = chart.getLegend();
        if (legend != null) {
            legend.setItemFont(new Font("Tahoma", Font.PLAIN, 11));
            legend.setPosition(RectangleEdge.RIGHT);
        }
    }

    private JComponent emptyState(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Tahoma", Font.ITALIC, 13));
        lbl.setForeground(MUTED);
        return lbl;
    }

    // ===================== CARD WRAPPER =====================
    private JPanel wrapCard(String title, JComponent body) {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.setBorder(new ShadowBorder(18));

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(CARD_BG);
        inner.setBorder(new CompoundBorder(
                new RoundBorder(18, new Color(235, 238, 244)),
                new EmptyBorder(6, 6, 6, 6)
        ));

        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 232, 236), 1),
                title
        );
        tb.setTitleFont(new Font("Tahoma", Font.BOLD, 12));
        tb.setTitleColor(COLOR_NAVY);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(CARD_BG);
        container.setBorder(tb);
        container.add(body, BorderLayout.CENTER);

        inner.add(container, BorderLayout.CENTER);
        outer.add(inner, BorderLayout.CENTER);
        return outer;
    }

    // ===================== METRIC CARD =====================
    private class MetricCard extends JPanel {
        private final JLabel lblTitle = new JLabel();
        private final JLabel lblIcon = new JLabel();
        private final JLabel lblValue = new JLabel();
        private final JLabel lblDelta = new JLabel();
        private final JLabel lblSub = new JLabel();
        private final boolean compact;

        public MetricCard(String title, String icon, boolean compact) {
            this.compact = compact;

            setLayout(new BorderLayout(10, 8));
            setOpaque(false);
            setBorder(new ShadowBorder(18));

            int pad = compact ? 10 : 14;
            int valueSize = compact ? 22 : 26;

            Color[] grad = pickGradientForMetric(title);
            JPanel inner = new GradientCardPanel(grad[0], grad[1]);
            inner.setLayout(new BorderLayout(10, 6));
            inner.setBorder(new CompoundBorder(
                    new RoundBorder(18, new Color(235, 238, 244)),
                    new EmptyBorder(pad, pad, pad, pad)
            ));

            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);

            lblTitle.setText(title);
            lblTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
            lblTitle.setForeground(COLOR_NAVY);

            lblIcon.setText(icon);
            lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, compact ? 20 : 22));
            lblIcon.setHorizontalAlignment(SwingConstants.RIGHT);

            top.add(lblTitle, BorderLayout.WEST);
            top.add(lblIcon, BorderLayout.EAST);

            lblValue.setText("0");
            lblValue.setFont(new Font("Tahoma", Font.BOLD, valueSize));
            lblValue.setForeground(COLOR_NEUTRAL());

            lblDelta.setText("—");
            lblDelta.setFont(new Font("Tahoma", Font.BOLD, 12));
            lblDelta.setForeground(MUTED);

            lblSub.setText(" ");
            lblSub.setFont(new Font("Tahoma", Font.PLAIN, 12));
            lblSub.setForeground(MUTED);

            JPanel mid = new JPanel();
            mid.setOpaque(false);
            mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));
            mid.add(lblValue);
            mid.add(Box.createVerticalStrut(compact ? 3 : 6));
            mid.add(lblDelta);
            if (!compact) mid.add(Box.createVerticalStrut(4));
            mid.add(lblSub);

            inner.add(top, BorderLayout.NORTH);
            inner.add(mid, BorderLayout.CENTER);

            add(inner, BorderLayout.CENTER);
        }

        public void setValue(String v) { lblValue.setText(v); }
        public void setSubText(String s) { lblSub.setText(s); }
        public void setAccent(Color c) { lblValue.setForeground(c); }

        public void setDeltaMoney(double now, double prev, String hint) {
            double diff = now - prev;
            String arrow = diff > 0 ? "▲" : (diff < 0 ? "▼" : "•");
            Color c = diff > 0 ? UP : (diff < 0 ? DOWN : MUTED);

            lblValue.setForeground(c);
            lblDelta.setForeground(c);

            lblDelta.setText(arrow + " " + dfMoney.format(Math.abs(diff)) + " VND (" + hint + ")");
            lblSub.setText("Tháng trước: " + dfMoney.format(prev) + " VND");
        }

        public void setDeltaInt(int now, int prev, String hint) {
            int diff = now - prev;
            String arrow = diff > 0 ? "▲" : (diff < 0 ? "▼" : "•");

            Color c;
            if (prev == 0 && now > 0) c = UP;
            else c = diff > 0 ? UP : (diff < 0 ? DOWN : MUTED);

            lblValue.setForeground(c);
            lblDelta.setForeground(c);

            lblDelta.setText(arrow + " " + Math.abs(diff) + " (" + hint + ")");
            lblSub.setText("Tháng trước: " + prev + " lượt");
        }
    }

    private Color[] pickGradientForMetric(String title) {
        if (title != null) {
            String t = title.toUpperCase();
            if (t.contains("DOANH THU")) return new Color[]{GRAD_DT_TOP, GRAD_DT_BOT};
            if (t.contains("CHECK-IN") || t.contains("CHECKIN")) return new Color[]{GRAD_CI_TOP, GRAD_CI_BOT};
            if (t.contains("TRONG KÉT") || t.contains("TIỀN")) return new Color[]{GRAD_SAFE_TOP, GRAD_SAFE_BOT};
        }
        return new Color[]{CARD_BG, CARD_BG};
    }

    private static class GradientCardPanel extends JPanel {
        private final Color top;
        private final Color bottom;

        public GradientCardPanel(Color top, Color bottom) {
            this.top = top;
            this.bottom = bottom;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private Color COLOR_NEUTRAL() { return new Color(60, 70, 90); }

    // ===================== BACKGROUND PANEL =====================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, new Color(245, 248, 252),
                getWidth(), getHeight(), new Color(236, 242, 250));
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    // ===================== STYLED BORDERS =====================
    private static class GradientPanel extends JPanel {
        private final Color c1, c2;
        public GradientPanel(Color c1, Color c2) { this.c1 = c1; this.c2 = c2; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        public RoundBorder(int radius, Color color) { this.radius = radius; this.color = color; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) { return new Insets(8, 8, 8, 8); }
    }

    private static class ShadowBorder extends AbstractBorder {
        private final int radius;
        public ShadowBorder(int radius) { this.radius = radius; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 28));
            g2.fillRoundRect(x + 2, y + 3, w - 4, h - 4, radius, radius);
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) { return new Insets(6, 6, 10, 10); }
    }

    // ===== Test =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Dashboard - QLKS Pate");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
            f.setContentPane(new Dashboard_GUI());
            f.setVisible(true);
        });
    }
}
