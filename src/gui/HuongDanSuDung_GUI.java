package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class HuongDanSuDung_GUI extends JPanel {

    // ===== THEME =====
    private static final Color BG_TOP = new Color(230, 242, 255);
    private static final Color BG_BOTTOM = new Color(245, 247, 251);

    private static final Color CARD = Color.WHITE;
    private static final Color BORDER = new Color(226, 232, 240);

    // Header xanh thường (không gradient)
    private static final Color HEADER_BLUE = new Color(10, 52, 89);
    private static final Color HEADER_SUB = new Color(225, 240, 255);

    private static final Color TEXT = new Color(31, 41, 55);
    private static final Color MUTED = new Color(107, 114, 128);

    // Content
    private JEditorPane editor;
    private JScrollPane scrollEditor;

    public HuongDanSuDung_GUI() {
        setLayout(new BorderLayout(16, 16));
        setOpaque(false);
        setBorder(new EmptyBorder(16, 16, 16, 16));

        taoGiaoDien();
        hienThiNoiDungHuongDan();
    }

    // ====== NỀN FULL TRANG ======
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, BG_TOP, 0, h, BG_BOTTOM);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);

        g2.dispose();
    }

    // ================= GIAO DIỆN =================
    private void taoGiaoDien() {

        // ===== Header (xanh thường) =====
        JPanel header = new SolidHeaderPanel();
        header.setLayout(new BorderLayout(12, 12));
        header.setBorder(new EmptyBorder(16, 18, 16, 18));
        header.setPreferredSize(new Dimension(0, 92));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("HƯỚNG DẪN SỬ DỤNG");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 26));

        JLabel lblSub = new JLabel("Quy trình nghiệp vụ nhân viên • Quy trình quản lý • Quy định vận hành");
        lblSub.setForeground(HEADER_SUB);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        left.add(lblTitle);
        left.add(Box.createVerticalStrut(4));
        left.add(lblSub);

        header.add(left, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ===== BODY (chỉ 1 content) =====
        ShadowRoundPanel contentCard = new ShadowRoundPanel(18);
        contentCard.setBackground(CARD);
        contentCard.setLayout(new BorderLayout());
        contentCard.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(0, 0, 0, 0)
        ));

        editor = new JEditorPane();
        editor.setContentType("text/html");
        editor.setEditable(false);
        editor.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        editor.setOpaque(false);

        scrollEditor = new JScrollPane(editor);
        scrollEditor.setBorder(BorderFactory.createEmptyBorder());
        scrollEditor.getViewport().setBackground(CARD);
        scrollEditor.getVerticalScrollBar().setUnitIncrement(16);

        contentCard.add(scrollEditor, BorderLayout.CENTER);
        add(contentCard, BorderLayout.CENTER);
    }

    // ================= NỘI DUNG HƯỚNG DẪN =================
    private void hienThiNoiDungHuongDan() {
        String css =
                "<style>"
                        + "body{font-family:Segoe UI, Arial; font-size:14px;color:#1f2937; line-height:1.65; padding:22px;}"
                        + "h1{font-size:24px; margin:0 0 8px 0; color:#0f172a;}"
                        + "h2{font-size:16px; margin:18px 0 8px 0; color:#0f172a;}"
                        + "h3{font-size:14px; margin:14px 0 6px 0; color:#0f172a;}"
                        + "p{margin:7px 0;}"
                        + ".muted{color:#6b7280;}"
                        + ".badge{display:inline-block; padding:3px 10px; border-radius:999px; background:#e8f1fd; color:#1565c0; font-size:12px; font-weight:700;}"
                        + ".box{background:#f8fafc; border:1px solid #e5e7eb; border-radius:14px; padding:14px; margin:12px 0;}"
                        + ".warn{background:#fff7ed; border:1px solid #fed7aa;}"
                        + ".ok{background:#f0fdf4; border:1px solid #bbf7d0;}"
                        + "ul{margin:8px 0 8px 18px;}"
                        + "li{margin:5px 0;}"
                        + "table{border-collapse:separate; border-spacing:0; width:100%; margin:12px 0; overflow:hidden; border-radius:12px;}"
                        + "th,td{border:1px solid #e5e7eb; padding:10px; text-align:left; vertical-align:top;}"
                        + "th{background:#f1f5f9;}"
                        + ".toc a{color:#1565c0; text-decoration:none; font-weight:600;}"
                        + ".toc a:hover{text-decoration:underline;}"
                        + "</style>";

        String body =
                "<h1>Hướng dẫn sử dụng – Nghiệp vụ nhân viên <span class='badge'>QLKS Pate</span></h1>"
                      //  + "<p class='muted'>Tài liệu này chỉ tập trung vào <b>quy trình nghiệp vụ</b> và <b>quy định vận hành</b> để nhân viên thao tác đúng – nhanh – hạn chế sai sót.</p>"

//                        + "<div class='box toc'>"
//                        + "<h2>Mục lục</h2>"
//                        + "<ul>"
//                        + "<li><a href='#nv'>I. Quy trình nghiệp vụ nhân viên</a></li>"
//                        + "<li><a href='#ql'>II. Quy trình nghiệp vụ nhân viên quản lý</a></li>"
//                        + "<li><a href='#qd'>III. Quy định & nguyên tắc áp dụng</a></li>"
//                        + "</ul>"
//                        + "</div>"

                        // ===== I. Nhân viên =====
                        + "<a name='nv'></a>"
                        + "<h2>I. Quy trình nghiệp vụ nhân viên</h2>"
                        + "<div class='box'>"
                        + "<h3>1) Tiếp nhận đặt phòng (tại quầy/điện thoại)</h3>"
                        + "<ul>"
                        + "<li>B1: Hỏi nhu cầu: <b>ngày nhận</b> – <b>ngày trả</b> – <b>số người</b> – <b>loại phòng</b>.</li>"
                        + "<li>B2: Kiểm tra phòng trống theo ngày.</li>"
                        + "<li>B3: Nhập thông tin khách: <b>Họ tên</b>, <b>SĐT</b>, (CCCD nếu có).</li>"
                        + "<li>B4: Xác nhận <b>tiền cọc</b> theo quy định → lưu phiếu.</li>"
                        + "<li>B5: Nhắc khách chính sách <b>đổi/hủy</b> (mốc 24h).</li>"
                        + "</ul>"
                        + "</div>"

                        + "<div class='box'>"
                        + "<h3>2) Check-in (nhận phòng)</h3>"
                        + "<ul>"
                        + "<li>B1: Tra cứu phiếu theo <b>SĐT</b> hoặc <b>mã phiếu</b>.</li>"
                        + "<li>B2: Đối chiếu <b>giấy tờ</b> (CCCD/Passport) và thông tin đặt phòng.</li>"
                        + "<li>B3: Xác nhận thời gian lưu trú, số người, thu phần còn lại (nếu có).</li>"
                        + "<li>B4: Cập nhật trạng thái phiếu: <b>Đang ở</b> và trạng thái phòng tương ứng.</li>"
                        + "<li>B5: Bàn giao chìa khóa/thẻ phòng và nhắc quy định giờ giấc – tài sản.</li>"
                        + "</ul>"
                        + "</div>"

                        + "<div class='box'>"
                        + "<h3>3) Ghi nhận chi phí phát sinh (trong thời gian khách ở)</h3>"
                        + "<ul>"
                        + "<li>B1: Chọn phiếu/khách đang ở.</li>"
                        + "<li>B2: Thêm phát sinh: minibar/giặt ủi/bồi thường…</li>"
                        + "<li>B3: Ghi rõ <b>mô tả</b> + <b>số tiền</b> + thời điểm phát sinh để dễ đối chiếu.</li>"
                        + "</ul>"
                        + "</div>"

                        + "<div class='box'>"
                        + "<h3>4) Check-out (trả phòng) & xuất hóa đơn</h3>"
                        + "<ul>"
                        + "<li>B1: Tra cứu phiếu → kiểm tra phát sinh và khuyến mãi (nếu có).</li>"
                        + "<li>B2: Tính tổng tiền: tiền phòng + phát sinh + phạt (nếu có) – khuyến mãi – trừ cọc.</li>"
                        + "<li>B3: Thu tiền/hoàn tiền (nếu thừa) → in/xuất hóa đơn.</li>"
                        + "<li>B4: Cập nhật trạng thái phiếu: <b>Hoàn thành</b>; phòng chuyển sang <b>Dọn dẹp</b> (hoặc theo quy trình nội bộ).</li>"
                        + "</ul>"
                        + "</div>"

                        // ===== II. Quản lý =====
                        + "<a name='ql'></a>"
                        + "<h2>II. Quy trình nghiệp vụ nhân viên quản lý</h2>"
                        + "<div class='box'>"
                        + "<h3>1) Kiểm soát tình trạng phòng & chất lượng vận hành</h3>"
                        + "<ul>"
                        + "<li>Đầu ca: kiểm tra danh sách phòng <b>Trống/Đang ở/Đặt trước/Dọn dẹp/Bảo trì</b>.</li>"
                        + "<li>Kiểm tra các phòng tới ngày nhận/trả để nhắc lễ tân xử lý.</li>"
                        + "<li>Phòng bảo trì phải có lý do + thời gian dự kiến hoàn tất.</li>"
                        + "</ul>"
                        + "</div>"

                        + "<div class='box'>"
                        + "<h3>2) Duyệt và quản lý khuyến mãi</h3>"
                        + "<ul>"
                        + "<li>Kiểm tra điều kiện mã, thời hạn, giới hạn áp dụng.</li>"
                        + "<li>Nguyên tắc: <b>mỗi hóa đơn chỉ áp dụng 01 mã</b> (không chồng).</li>"
                        + "</ul>"
                        + "</div>"

                        + "<div class='box'>"
                        + "<h3>3) Quản lý nhân sự & tài khoản</h3>"
                        + "<ul>"
                        + "<li>Phân quyền đúng vai trò: hạn chế thao tác sai chức năng.</li>"
                        + "<li>Không dùng chung tài khoản; đổi mật khẩu định kỳ.</li>"
                        + "</ul>"
                        + "</div>"

                        + "<div class='box ok'>"
                        + "<h3>4) Đối chiếu cuối ngày/cuối ca</h3>"
                        + "<ul>"
                        + "<li>Đối chiếu doanh thu, số hóa đơn, phát sinh và tiền mặt.</li>"
                        + "<li>Kiểm tra các phiếu còn treo: chưa nhận, tới ngày trả, trễ hạn…</li>"
                        + "</ul>"
                        + "</div>"

                        // ===== III. Quy định =====
                        + "<a name='qd'></a>"
                        + "<h2>III. Quy định & nguyên tắc áp dụng</h2>"

                        + "<table>"
                        + "<tr><th>Nội dung</th><th>Quy định</th></tr>"
                        + "<tr><td>Giờ nhận – trả</td><td>Check-in từ <b>14:00</b>, check-out trước <b>12:00</b> hôm sau</td></tr>"
                        + "<tr><td>Tiền cọc</td><td>1 đêm: <b>100%</b>; từ 2 đêm: <b>50%</b> giá trị đặt phòng</td></tr>"
                        + "<tr><td>Khuyến mãi</td><td>Mỗi hóa đơn áp dụng <b>01 mã</b>, không chồng</td></tr>"
                        + "<tr><td>Trẻ em</td><td>&lt; 6 tuổi: miễn phí; ≥ 7 tuổi: tính như 1 người</td></tr>"
                        + "</table>"

                        + "<div class='box warn'>"
                        + "<h3>Phạt trả phòng trễ</h3>"
                        + "<ul>"
                        + "<li>12:00–&lt;15:00: +30% giá phòng/đêm</li>"
                        + "<li>15:00–&lt;18:00: +50% giá phòng/đêm</li>"
                        + "<li>≥18:00: tính như 1 đêm mới</li>"
                        + "</ul>"
                        + "</div>"

                        + "<div class='box warn'>"
                        + "<h3>Đổi / hủy đặt phòng</h3>"
                        + "<ul>"
                        + "<li>Đổi/hủy trước <b>≥ 24h</b>: xử lý theo chính sách hoàn cọc của khách sạn.</li>"
                        + "<li>Đổi/hủy <b>&lt; 24h</b> trước check-in: xem như hủy, tạo phiếu mới (nếu đặt lại) theo quy định nội bộ.</li>"
                        + "<li>Khách đến trễ không báo: giữ phòng tối đa đến <b>23:59</b> ngày check-in.</li>"
                        + "</ul>"
                        + "</div>"

                        + "<div class='box'>"
                        + "<h3>Nguyên tắc an toàn dữ liệu</h3>"
                        + "<ul>"
                        + "<li>Chỉ nhân sự được phân quyền mới thao tác các mục nhạy cảm.</li>"
                        + "<li>Không chia sẻ mật khẩu; không ghi mật khẩu ra giấy tại quầy.</li>"
                        + "<li>Luôn thoát tài khoản khi đổi ca.</li>"
                        + "</ul>"
                        + "</div>"

                        + "<p class='muted'>Cuối cùng: nếu phát sinh tình huống ngoài quy định, báo ngay quản lý để xử lý thống nhất.</p>";

        editor.setText("<html><head>" + css + "</head><body>" + body + "</body></html>");
        editor.setCaretPosition(0);
    }

    // ================= UI COMPONENTS =================

    // Header xanh thường (solid)
    private static class SolidHeaderPanel extends JPanel {
        public SolidHeaderPanel() {
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            g2.setColor(HEADER_BLUE);
            g2.fillRoundRect(0, 0, w, h, 22, 22);

            g2.setColor(new Color(255, 255, 255, 60));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 22, 22);

            g2.dispose();
        }
    }

    // Card bo góc + shadow
    private static class ShadowRoundPanel extends JPanel {
        private final int radius;
        public ShadowRoundPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            // shadow
            g2.setColor(new Color(0, 0, 0, 16));
            g2.fillRoundRect(4, 5, w - 8, h - 9, radius, radius);

            // bg
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ================= TEST RIÊNG =================
    public static void testUI() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            JFrame f = new JFrame("Test - Hướng dẫn sử dụng | Khách sạn Pate");
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            HuongDanSuDung_GUI panel = new HuongDanSuDung_GUI();
            f.setContentPane(panel);

            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }

    public static void main(String[] args) {
        testUI();
    }
}
