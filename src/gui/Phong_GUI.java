package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import dao.ChiTietPhieuDatPhong_DAO;
import dao.LoaiPhong_DAO;
import dao.Phong_DAO;
import entity.LoaiPhong;
import entity.Phong;

public class Phong_GUI extends JPanel implements ActionListener, MouseListener {

    // ===== THEME =====
    private static final Color NAVY = new Color(10, 52, 89);
    private static final Color NAVY_DARK = new Color(7, 40, 68);
    private static final Color GOLD = new Color(218, 177, 55);

    private static final Color BG_TOP = new Color(245, 248, 252);
    private static final Color BG_BOT = new Color(236, 242, 250);

    private static final Color CARD_BG = Color.WHITE;
    private static final Color BORDER = new Color(220, 227, 235);
    private static final Color MUTED = new Color(102, 112, 133);

    private static final Color OK = new Color(0, 150, 80);
    private static final Color WARN = new Color(217, 119, 6);

    private static final String FONT_UI = "Segoe UI";
    private static final String FONT_EMOJI = "Segoe UI Emoji";

    private static final Dimension SEARCH_BTN_SIZE = new Dimension(52, 38);

    private JTextField txtMaPhong;
    private JComboBox<String> cboLoaiPhong, cboTrangThai;

    private JButton btnSearchMa, btnThem, btnLuu, btnXoaRong, btnChiTietLoaiPhong;

    private JTable table;
    private DefaultTableModel model;

    private final Phong_DAO phongDAO = new Phong_DAO();
    private final LoaiPhong_DAO loaiPhongDAO = new LoaiPhong_DAO();
    private final ChiTietPhieuDatPhong_DAO ctpdpDAO = new ChiTietPhieuDatPhong_DAO();

    private boolean isRowSelected = false;

    public Phong_GUI() {
        applyGlobalFontDefaults();

        setLayout(new BorderLayout(14, 14));
        setOpaque(false);

        add(buildHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(14, 14));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(12, 14, 14, 14));

        body.add(buildFormCard(), BorderLayout.NORTH);
        body.add(buildTableCard(), BorderLayout.CENTER);

        add(body, BorderLayout.CENTER);

        // load data
        loadComboLoaiPhong();
        loadTablePhong();
        clearForm();

        cboLoaiPhong.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) autoGenerateMaPhong();
        });
    }

    // ================= BACKGROUND =================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, BG_TOP, getWidth(), getHeight(), BG_BOT);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    // ================= UI =================
    private JPanel buildHeader() {
        JPanel header = new GradientHeaderPanel(NAVY, new Color(17, 88, 140));
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel title = new JLabel("QUẢN LÝ PHÒNG");
        title.setForeground(Color.WHITE);
        title.setFont(new Font(FONT_UI, Font.BOLD, 26));

        JLabel sub = new JLabel("Pate Hotel • Thêm/Cập nhật/Tìm kiếm thông tin phòng");
        sub.setForeground(new Color(210, 225, 240));
        sub.setFont(new Font(FONT_UI, Font.PLAIN, 13));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.add(title);
        left.add(Box.createVerticalStrut(4));
        left.add(sub);

//        // chip nhỏ bên phải cho đẹp
//        JPanel chip = buildChip("● Đang hoạt động", new Color(225, 248, 235), OK);
//
//        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
//        right.setOpaque(false);
//        right.add(chip);

        header.add(left, BorderLayout.WEST);
     //   header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel buildFormCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(new ShadowBorder(18), new CompoundBorder(
                new RoundBorder(18, new Color(235, 238, 244)),
                new EmptyBorder(14, 14, 14, 14)
        )));

        JLabel t = new JLabel("Thông tin phòng");
        t.setFont(new Font(FONT_UI, Font.BOLD, 16));
        t.setForeground(NAVY_DARK);
        card.add(t, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Mã phòng + tìm
        gbc.gridy = 0;
        addLabel(form, gbc, 0, "Mã phòng:");
        form.add(buildMaPhongPanel(), fieldGbc(gbc, 1));

        // Row 0: Trạng thái
        addLabel(form, gbc, 2, "Trạng thái:");
        cboTrangThai = new JComboBox<>(new String[]{"Trống", "Bảo trì"});
        styleCombo(cboTrangThai);
        form.add(cboTrangThai, fieldGbc(gbc, 3));

        // Row 1: Loại phòng + nút chi tiết
        gbc.gridy = 1;
        addLabel(form, gbc, 0, "Loại phòng:");
        form.add(buildLoaiPhongPanel(), fieldGbc(gbc, 1));

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);

        btnXoaRong = createButton("Xóa rỗng", new Color(230, 236, 244), NAVY_DARK);
        btnThem   = createButton("Thêm", GOLD, NAVY_DARK);
        btnLuu    = createButton("Lưu", NAVY, Color.WHITE);

        installButtonHover(btnXoaRong, new Color(218, 226, 237), new Color(230, 236, 244));
        installButtonHover(btnThem, new Color(205, 165, 50), GOLD);
        installButtonHover(btnLuu, new Color(8, 44, 77), NAVY);

        btnXoaRong.addActionListener(this);
        btnThem.addActionListener(this);
        btnLuu.addActionListener(this);

        btnRow.add(btnXoaRong);
        btnRow.add(btnThem);
        btnRow.add(btnLuu);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.add(form, BorderLayout.CENTER);
        center.add(btnRow, BorderLayout.SOUTH);

        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildTableCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(new ShadowBorder(18), new CompoundBorder(
                new RoundBorder(18, new Color(235, 238, 244)),
                new EmptyBorder(10, 10, 10, 10)
        )));

        JLabel t = new JLabel("Danh sách phòng");
        t.setFont(new Font(FONT_UI, Font.BOLD, 16));
        t.setForeground(NAVY_DARK);
        card.add(t, BorderLayout.NORTH);

        String[] cols = {"STT", "Mã phòng", "Loại phòng", "Trạng thái", "Sức chứa", "Giá phòng"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.addMouseListener(this);
        styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(new Color(230, 232, 236), 1, true));
        sp.getViewport().setBackground(Color.WHITE);

        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildMaPhongPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);

        txtMaPhong = new JTextField();
        styleField(txtMaPhong);
        addFocusRing(txtMaPhong);

        txtMaPhong.addActionListener(e -> timTheoMaPhong());

        btnSearchMa = buildSearchButton("Tìm nhanh theo mã phòng");
        btnSearchMa.addActionListener(this);

        p.add(txtMaPhong, BorderLayout.CENTER);
        p.add(btnSearchMa, BorderLayout.EAST);
        return p;
    }

    private JPanel buildLoaiPhongPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);

        cboLoaiPhong = new JComboBox<>();
        styleCombo(cboLoaiPhong);
        addFocusRing(cboLoaiPhong);

        btnChiTietLoaiPhong = new JButton("✏️");
        btnChiTietLoaiPhong.setFont(new Font(FONT_EMOJI, Font.PLAIN, 18));
        btnChiTietLoaiPhong.setToolTipText("Quản lý loại phòng");
        btnChiTietLoaiPhong.setFocusPainted(false);
        btnChiTietLoaiPhong.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnChiTietLoaiPhong.setBackground(new Color(238, 242, 247));
        btnChiTietLoaiPhong.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(4, 0, 4, 0)));
        btnChiTietLoaiPhong.setPreferredSize(new Dimension(52, 38));
        installButtonHover(btnChiTietLoaiPhong, new Color(225, 233, 242), new Color(238, 242, 247));
        btnChiTietLoaiPhong.addActionListener(e -> openLoaiPhongDialog());

        p.add(cboLoaiPhong, BorderLayout.CENTER);
        p.add(btnChiTietLoaiPhong, BorderLayout.EAST);
        return p;
    }

    // ================= DATA =================
    private void loadComboLoaiPhong() {
        cboLoaiPhong.removeAllItems();
        List<LoaiPhong> dsLoai = loaiPhongDAO.getAllLoaiPhong();
        for (LoaiPhong lp : dsLoai) {
            cboLoaiPhong.addItem(lp.getMaLoaiPhong() + " - " + lp.getTenLoaiPhong());
        }
    }

    private void loadTablePhong() {
        model.setRowCount(0);
        List<Phong> ds = phongDAO.getAllPhong();
        int stt = 1;
        DecimalFormat df = new DecimalFormat("#,##0");

        for (Phong p : ds) {
            Object[] row = {
                    stt++,
                    p.getMaPhong(),
                    p.getLoaiPhong() != null ? p.getLoaiPhong().getTenLoaiPhong() : "",
                    p.getTrangThai(),
                    p.getLoaiPhong() != null ? p.getLoaiPhong().getSucChua() : "",
                    p.getLoaiPhong() != null ? df.format(p.getLoaiPhong().getGia()) : ""
            };
            model.addRow(row);
        }
    }

    private void autoGenerateMaPhong() {
        String selected = (String) cboLoaiPhong.getSelectedItem();
        if (selected == null) return;

        String maLoai = selected.split(" - ")[0].trim();
        LoaiPhong lp = loaiPhongDAO.getLoaiPhongTheoMa(maLoai);
        if (lp == null) return;

        String maPhongMoi = generateNextRoomCode(lp);
        txtMaPhong.setText(maPhongMoi);
    }

    private String generateNextRoomCode(LoaiPhong loaiPhong) {
        String tenLoai = loaiPhong.getTenLoaiPhong() == null ? "" : loaiPhong.getTenLoaiPhong().toLowerCase();

        String prefix;
        if (tenLoai.contains("tiêu chuẩn") || tenLoai.contains("standard")) prefix = "P1";
        else if (tenLoai.contains("cao cấp") || tenLoai.contains("deluxe")) prefix = "P2";
        else if (tenLoai.contains("gia đình") || tenLoai.contains("family")) prefix = "P3";
        else prefix = "P9";

        String lastCode = phongDAO.getLastRoomCodeByLoai(prefix);
        if (lastCode == null) return prefix + "01";

        String numberPart = lastCode.substring(prefix.length());
        int nextNumber = Integer.parseInt(numberPart) + 1;
        return prefix + String.format("%02d", nextNumber);
    }

    private boolean validateForm(String maPhong, String trangThai, LoaiPhong loaiPhong) {
        if (maPhong == null || maPhong.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã phòng không được để trống!");
            return false;
        }

        maPhong = maPhong.trim().toUpperCase();

        if (!maPhong.matches("^P\\d{3}$")) {
            JOptionPane.showMessageDialog(this, "Mã phòng phải có dạng Pxxx (ví dụ: P101).");
            return false;
        }

        if (trangThai == null || trangThai.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn trạng thái!");
            return false;
        }

        String tt = trangThai.trim().toLowerCase();
        if (!(tt.equals("trống") || tt.equals("bảo trì"))) {
            JOptionPane.showMessageDialog(this, "Trạng thái chỉ được: Trống hoặc Bảo trì.");
            return false;
        }

        if (loaiPhong == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!");
            return false;
        }

        if (loaiPhong.getGia() <= 0 || loaiPhong.getSucChua() <= 0) {
            JOptionPane.showMessageDialog(this, "Giá phòng và sức chứa phải hợp lệ!");
            return false;
        }

        return true;
    }

    // ================= SEARCH =================
    private void timTheoMaPhong() {
        String ma = txtMaPhong.getText().trim().toUpperCase();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã phòng để tìm!");
            return;
        }

        Phong p = phongDAO.timPhongTheoMa(ma);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy phòng với mã: " + ma);
            return;
        }

        focusPhongOnTable(p.getMaPhong());
    }

    private void focusPhongOnTable(String maPhong) {
        for (int i = 0; i < model.getRowCount(); i++) {
            String ma = model.getValueAt(i, 1).toString();
            if (ma.equalsIgnoreCase(maPhong)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                loadRowToForm(i);
                return;
            }
        }
        loadTablePhong();
    }

    // ================= FORM =================
    private void clearForm() {
        isRowSelected = false;
        table.clearSelection();

        if (cboLoaiPhong.getItemCount() > 0) cboLoaiPhong.setSelectedIndex(0);
        cboTrangThai.setSelectedItem("Trống");

        autoGenerateMaPhong();
        txtMaPhong.requestFocus();
    }

    private void loadRowToForm(int row) {
        if (row < 0) return;
        isRowSelected = true;

        String maPhong = model.getValueAt(row, 1).toString();
        String tenLoai = model.getValueAt(row, 2).toString();
        String trangThai = model.getValueAt(row, 3).toString();

        txtMaPhong.setText(maPhong);

        for (int i = 0; i < cboTrangThai.getItemCount(); i++) {
            if (cboTrangThai.getItemAt(i).equalsIgnoreCase(trangThai)) {
                cboTrangThai.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < cboLoaiPhong.getItemCount(); i++) {
            String item = cboLoaiPhong.getItemAt(i);
            if (item != null && item.toLowerCase().endsWith(tenLoai.toLowerCase())) {
                cboLoaiPhong.setSelectedIndex(i);
                break;
            }
        }
    }

    // ================= EVENTS =================
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == btnSearchMa) { timTheoMaPhong(); return; }
        if (o == btnXoaRong) { clearForm(); return; }

        if (o == btnThem) {
            String maPhong = txtMaPhong.getText().trim().toUpperCase();
            String trangThai = (String) cboTrangThai.getSelectedItem();

            String selected = (String) cboLoaiPhong.getSelectedItem();
            if (selected == null) { JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!"); return; }

            String maLoai = selected.split(" - ")[0].trim();
            LoaiPhong lp = loaiPhongDAO.getLoaiPhongTheoMa(maLoai);

            if (!validateForm(maPhong, trangThai, lp)) return;

            if (phongDAO.timPhongTheoMa(maPhong) != null) {
                JOptionPane.showMessageDialog(this, "Mã phòng đã tồn tại! Hãy bấm Lưu để cập nhật.");
                return;
            }

            Phong p = new Phong(maPhong, lp, trangThai);
            if (phongDAO.themPhong(p)) {
                JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
                loadTablePhong();
                clearForm();
            } else JOptionPane.showMessageDialog(this, "Thêm phòng thất bại!");
            return;
        }

        if (o == btnLuu) {
            String maPhong = txtMaPhong.getText().trim().toUpperCase();
            String trangThai = (String) cboTrangThai.getSelectedItem();

            String selected = (String) cboLoaiPhong.getSelectedItem();
            if (selected == null) { JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!"); return; }

            String maLoai = selected.split(" - ")[0].trim();
            LoaiPhong lp = loaiPhongDAO.getLoaiPhongTheoMa(maLoai);

            if (!validateForm(maPhong, trangThai, lp)) return;

            Phong p = new Phong(maPhong, lp, trangThai);

            if (phongDAO.timPhongTheoMa(maPhong) != null && !ctpdpDAO.isPhongDangO(maPhong)) {
                if (phongDAO.capNhatPhong(p)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật phòng thành công!");
                    loadTablePhong();
                } else JOptionPane.showMessageDialog(this, "Cập nhật phòng thất bại!");
            } else JOptionPane.showMessageDialog(this, "Phòng đang ở không thể bảo trì!");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) loadRowToForm(row);
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // ================= Dialog Loại phòng =================
    private void openLoaiPhongDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Quản Lý Loại Phòng", true);
        dialog.setSize(1000, 700);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.add(new LoaiPhong_GUI());
        dialog.setVisible(true);

        loadComboLoaiPhong();
        autoGenerateMaPhong();
    }

    // ================= STYLE HELPERS =================
    private JPanel buildChip(String text, Color bg, Color accent) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        p.setOpaque(true);
        p.setBackground(bg);
        p.setBorder(new CompoundBorder(new RoundBorder(999, bg.darker()), new EmptyBorder(4, 10, 4, 10)));

        JLabel lb = new JLabel(text);
        lb.setFont(new Font(FONT_UI, Font.BOLD, 12));
        lb.setForeground(accent);
        p.add(lb);
        return p;
    }

    private void addLabel(JPanel p, GridBagConstraints gbc, int gridx, String text) {
        gbc.gridx = gridx;
        gbc.weightx = 0;

        JLabel lb = new JLabel(text);
        lb.setFont(new Font(FONT_UI, Font.PLAIN, 14));
        lb.setForeground(NAVY_DARK);
        p.add(lb, gbc);
    }

    private GridBagConstraints fieldGbc(GridBagConstraints gbc, int gridx) {
        GridBagConstraints c = (GridBagConstraints) gbc.clone();
        c.gridx = gridx;
        c.weightx = 1;
        return c;
    }

    private void styleField(JTextField f) {
        f.setFont(new Font(FONT_UI, Font.PLAIN, 14));
        f.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(8, 10, 8, 10)));
        f.setBackground(Color.WHITE);
        f.setPreferredSize(new Dimension(280, 38));
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setFont(new Font(FONT_UI, Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        cb.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(3, 6, 3, 6)));
        cb.setPreferredSize(new Dimension(280, 38));
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font(FONT_UI, Font.BOLD, 14));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0, 25), 1, true), new EmptyBorder(10, 16, 10, 16)));
        return b;
    }

    private JButton buildSearchButton(String tip) {
        JButton b = new JButton();
        b.setToolTipText(tip);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(new Color(238, 242, 247));
        b.setBorder(new LineBorder(BORDER, 1, true));
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setMargin(new Insets(0, 0, 0, 0));

        b.setPreferredSize(SEARCH_BTN_SIZE);
        b.setMinimumSize(SEARCH_BTN_SIZE);
        b.setMaximumSize(SEARCH_BTN_SIZE);

        setMagnifierText(b);
        installButtonHover(b, new Color(225, 233, 242), new Color(238, 242, 247));
        return b;
    }

    private void setMagnifierText(JButton b) {
        String icon = "🔍";
        Font emojiFont = new Font(FONT_EMOJI, Font.PLAIN, 16);

        if (emojiFont.canDisplayUpTo(icon) == -1) {
            b.setText(icon);
            b.setFont(emojiFont);
        } else {
            b.setText("Tìm");
            b.setFont(new Font(FONT_UI, Font.BOLD, 12));
        }
    }

    private void styleTable(JTable t) {
        t.setRowHeight(36);
        t.setFont(new Font(FONT_UI, Font.PLAIN, 13));
        t.setGridColor(new Color(230, 235, 240));
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.setSelectionBackground(new Color(225, 238, 252));
        t.setSelectionForeground(NAVY_DARK);

        JTableHeader header = t.getTableHeader();
        header.setFont(new Font(FONT_UI, Font.BOLD, 13));
        header.setBackground(NAVY_DARK);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setReorderingAllowed(false);

        // renderer chung + zebra
        DefaultTableCellRenderer base = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 253));
                setBorder(new EmptyBorder(0, 10, 0, 10));
                setForeground(NAVY_DARK);
                return c;
            }
        };

        // STT center
        DefaultTableCellRenderer center = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = base.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        // Giá phòng right
        DefaultTableCellRenderer right = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = base.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        };

        // Trạng thái màu
        DefaultTableCellRenderer status = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = base.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String s = value == null ? "" : value.toString().toLowerCase();
                if (!isSelected) {
                    if (s.contains("trống")) setForeground(OK);
                    else if (s.contains("bảo trì")) setForeground(WARN);
                    else setForeground(NAVY_DARK);
                }
                setFont(new Font(FONT_UI, Font.BOLD, 13));
                return c;
            }
        };

        for (int i = 0; i < t.getColumnCount(); i++) t.getColumnModel().getColumn(i).setCellRenderer(base);
        t.getColumnModel().getColumn(0).setCellRenderer(center); // STT
        t.getColumnModel().getColumn(4).setCellRenderer(center); // Sức chứa
        t.getColumnModel().getColumn(5).setCellRenderer(right);  // Giá
        t.getColumnModel().getColumn(3).setCellRenderer(status); // Trạng thái

        // set width gợi ý
        t.getColumnModel().getColumn(0).setPreferredWidth(60);
        t.getColumnModel().getColumn(1).setPreferredWidth(110);
        t.getColumnModel().getColumn(2).setPreferredWidth(200);
        t.getColumnModel().getColumn(3).setPreferredWidth(120);
        t.getColumnModel().getColumn(4).setPreferredWidth(100);
        t.getColumnModel().getColumn(5).setPreferredWidth(140);
    }

    private void applyGlobalFontDefaults() {
        Font ui = new Font(FONT_UI, Font.PLAIN, 13);
        UIManager.put("Label.font", ui);
        UIManager.put("Button.font", ui);
        UIManager.put("TextField.font", ui);
        UIManager.put("ComboBox.font", ui);
        UIManager.put("Table.font", ui);
        UIManager.put("TableHeader.font", new Font(FONT_UI, Font.BOLD, 13));
        UIManager.put("TitledBorder.font", new Font(FONT_UI, Font.BOLD, 13));
    }

    // focus ring đẹp
    private void addFocusRing(final JComponent c) {
        final Border normal = c.getBorder();
        final Border focus = new CompoundBorder(new LineBorder(new Color(46, 144, 250), 2, true),
                new EmptyBorder(7, 9, 7, 9));
        c.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { c.setBorder(focus); }
            @Override public void focusLost(FocusEvent e) { c.setBorder(normal); }
        });
    }

    // hover button
    private void installButtonHover(final JButton b, final Color hover, final Color normal) {
        b.setOpaque(true);
        b.setBorderPainted(true);
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e)  { b.setBackground(normal); }
        });
    }

    // ================= BORDER CLASSES =================
    private static class GradientHeaderPanel extends JPanel {
        private final Color c1, c2;
        public GradientHeaderPanel(Color c1, Color c2) { this.c1 = c1; this.c2 = c2; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
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
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(8, 8, 8, 8); }
    }

    private static class ShadowBorder extends AbstractBorder {
        private final int radius;
        public ShadowBorder(int radius) { this.radius = radius; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 22));
            g2.fillRoundRect(x + 2, y + 3, w - 4, h - 4, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(6, 6, 10, 10); }
    }

    // ===== CHẠY THỬ =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Quản Lý Phòng - Pate Hotel");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
            f.setContentPane(new Phong_GUI());
            f.setVisible(true);
        });
    }
}
