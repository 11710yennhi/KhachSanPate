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

import dao.LoaiPhong_DAO;
import entity.LoaiPhong;

public class LoaiPhong_GUI extends JPanel implements ActionListener, MouseListener {

    // ===== Tone màu giống Phòng/Nhân viên (navy + gold) =====
    private static final Color NAVY = new Color(10, 52, 89);
    private static final Color NAVY_DARK = new Color(7, 40, 68);
    private static final Color GOLD = new Color(218, 177, 55);
    private static final Color LIGHT_BG = new Color(245, 247, 250);
    private static final Color BORDER = new Color(220, 227, 235);

    private static final String FONT_UI = "Segoe UI";
    private static final String FONT_EMOJI = "Segoe UI Emoji";
    private static final Dimension SEARCH_BTN_SIZE = new Dimension(52, 38);

    private JTextField txtMaLoai, txtTenLoai, txtSucChua, txtGia, txtMoTa;

    private JButton btnSearchMa, btnThem, btnLuu, btnXoaRong;

    private JTable table;
    private DefaultTableModel model;

    private final LoaiPhong_DAO dao = new LoaiPhong_DAO();

    private final DecimalFormat dfMoney = new DecimalFormat("#,##0");

    // ✅ trạng thái chọn dòng
    private boolean isRowSelected = false;

    public LoaiPhong_GUI() {
        applyGlobalFontDefaults();

        setLayout(new BorderLayout(12, 12));
        setBackground(LIGHT_BG);

        add(buildHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(12, 12));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(10, 12, 12, 12));

        body.add(buildFormCard(), BorderLayout.NORTH);
        body.add(buildTableCard(), BorderLayout.CENTER);

        add(body, BorderLayout.CENTER);

        loadData();
        clearForm(); // ✅ cho phép nhập mã để tìm/thêm
    }

    // ================= UI =================

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel title = new JLabel("LOẠI PHÒNG");
        title.setForeground(Color.WHITE);
        title.setFont(new Font(FONT_UI, Font.BOLD, 26));

        JLabel sub = new JLabel("Pate Hotel • Thêm/Cập nhật/Tìm kiếm loại phòng");
        sub.setForeground(new Color(210, 225, 240));
        sub.setFont(new Font(FONT_UI, Font.PLAIN, 13));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.add(title);
        left.add(Box.createVerticalStrut(4));
        left.add(sub);

        header.add(left, BorderLayout.WEST);
        return header;
    }

    private JPanel buildFormCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel t = new JLabel("Thông tin loại phòng");
        t.setFont(new Font(FONT_UI, Font.BOLD, 16));
        t.setForeground(NAVY_DARK);
        card.add(t, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Mã loại + tìm, Sức chứa
        gbc.gridy = 0;
        addLabel(form, gbc, 0, "Mã loại phòng:");
        form.add(buildMaLoaiPanel(), fieldGbc(gbc, 1));

        addLabel(form, gbc, 2, "Sức chứa:");
        txtSucChua = new JTextField();
        styleField(txtSucChua);
        form.add(txtSucChua, fieldGbc(gbc, 3));

        // Row 1: Tên loại, Mô tả
        gbc.gridy = 1;
        addLabel(form, gbc, 0, "Tên loại phòng:");
        txtTenLoai = new JTextField();
        styleField(txtTenLoai);
        form.add(txtTenLoai, fieldGbc(gbc, 1));

        addLabel(form, gbc, 2, "Mô tả:");
        txtMoTa = new JTextField();
        styleField(txtMoTa);
        form.add(txtMoTa, fieldGbc(gbc, 3));

        // Row 2: Giá
        gbc.gridy = 2;
        addLabel(form, gbc, 0, "Giá:");
        txtGia = new JTextField();
        styleField(txtGia);
        form.add(txtGia, fieldGbc(gbc, 1));

        // Format giá khi focus
        txtGia.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtGia.setText(txtGia.getText().replace(",", "").trim());
            }

            @Override
            public void focusLost(FocusEvent e) {
                String raw = txtGia.getText().replace(",", "").trim();
                if (raw.isEmpty()) return;
                try {
                    long v = Long.parseLong(raw);
                    txtGia.setText(dfMoney.format(v));
                } catch (Exception ex) {
                    // giữ nguyên
                }
            }
        });

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);

        btnXoaRong = createButton("Xóa rỗng", new Color(230, 236, 244), NAVY_DARK);
        btnThem = createButton("Thêm", GOLD, NAVY_DARK);
        btnLuu = createButton("Lưu", NAVY, Color.WHITE);

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
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel t = new JLabel("Danh sách loại phòng");
        t.setFont(new Font(FONT_UI, Font.BOLD, 16));
        t.setForeground(NAVY_DARK);
        card.add(t, BorderLayout.NORTH);

        String[] cols = {"Mã", "Tên", "Sức chứa", "Giá", "Mô tả"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; } // ✅ khóa sửa
        };

        table = new JTable(model);
        table.addMouseListener(this);
        styleTable(table);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(BORDER, 1, true));
        card.add(sp, BorderLayout.CENTER);

        double[] ratios = {1, 2, 1, 1, 5};
        double totalRatio = 10;

        sp.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int totalWidth = sp.getViewport().getWidth();
                for (int i = 0; i < ratios.length; i++) {
                    int colWidth = (int) ((ratios[i] / totalRatio) * totalWidth);
                    table.getColumnModel().getColumn(i).setPreferredWidth(colWidth);
                }
            }
        });

        return card;
    }

    private JPanel buildMaLoaiPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);

        txtMaLoai = new JTextField();
        styleField(txtMaLoai);

        // ✅ Enter để tìm nhanh
        txtMaLoai.addActionListener(e -> timTheoMaLoai());

        btnSearchMa = buildSearchButton("Tìm nhanh theo mã loại phòng");
        btnSearchMa.addActionListener(this);

        p.add(txtMaLoai, BorderLayout.CENTER);
        p.add(btnSearchMa, BorderLayout.EAST);
        return p;
    }

    // ================= DATA =================

    private void loadData() {
        model.setRowCount(0);
        List<LoaiPhong> list = dao.getAllLoaiPhong();
        for (LoaiPhong lp : list) {
            String giaTxt;
            try {
                giaTxt = dfMoney.format((long) lp.getGia());
            } catch (Exception e) {
                giaTxt = String.valueOf(lp.getGia());
            }

            model.addRow(new Object[]{
                    lp.getMaLoaiPhong(),
                    lp.getTenLoaiPhong(),
                    lp.getSucChua(),
                    giaTxt,
                    lp.getMoTa()
            });
        }
    }

    private void timTheoMaLoai() {
        String ma = txtMaLoai.getText().trim().toUpperCase();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã loại phòng để tìm!");
            return;
        }

        LoaiPhong lp = dao.findByMa(ma);
        if (lp == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy loại phòng với mã: " + ma);
            return;
        }

        focusLoaiPhongOnTable(ma);
    }

    private void focusLoaiPhongOnTable(String maLoai) {
        for (int i = 0; i < model.getRowCount(); i++) {
            String ma = model.getValueAt(i, 0).toString();
            if (ma.equalsIgnoreCase(maLoai)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                loadRowToForm(i);
                return;
            }
        }
        loadData();
    }

    // ================= FORM =================

    private void clearForm() {
        isRowSelected = false;
        table.clearSelection();

        txtMaLoai.setText("");
        txtTenLoai.setText("");
        txtSucChua.setText("");
        txtGia.setText("");
        txtMoTa.setText("");

        // ✅ cho nhập mã để tìm/thêm
        txtMaLoai.setEditable(true);
        txtMaLoai.setBackground(Color.WHITE);

        txtMaLoai.requestFocus();
    }

    private void loadRowToForm(int row) {
        if (row < 0) return;

        isRowSelected = true;

        String ma = model.getValueAt(row, 0).toString();
        String ten = model.getValueAt(row, 1).toString();
        String sucChua = model.getValueAt(row, 2).toString();
        String gia = model.getValueAt(row, 3).toString();
        String moTa = model.getValueAt(row, 4).toString();

        txtMaLoai.setText(ma);
        txtTenLoai.setText(ten);
        txtSucChua.setText(sucChua);
        txtGia.setText(gia);
        txtMoTa.setText(moTa);

        // ✅ click bảng -> khóa mã
        txtMaLoai.setEditable(false);
        txtMaLoai.setBackground(new Color(248, 250, 253));
    }

    private LoaiPhong getFromFormSafe() {
        String ma = txtMaLoai.getText().trim().toUpperCase();
        String ten = txtTenLoai.getText().trim();
        String sucChuaStr = txtSucChua.getText().trim();
        String giaStr = txtGia.getText().trim().replace(",", "");
        String moTa = txtMoTa.getText().trim();

        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã loại phòng không được để trống!");
            return null;
        }
        if (!ma.matches("^LP\\d{3}$")) {
            JOptionPane.showMessageDialog(this, "Mã loại phòng phải có dạng LP + 3 chữ số (ví dụ: LP001)!");
            return null;
        }

        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên loại phòng không được để trống!");
            return null;
        }
        if (ten.length() > 50) {
            JOptionPane.showMessageDialog(this, "Tên loại phòng không được vượt quá 50 ký tự!");
            return null;
        }

        int sucChua;
        try {
            sucChua = Integer.parseInt(sucChuaStr);
            if (sucChua <= 0) {
                JOptionPane.showMessageDialog(this, "Sức chứa phải là số nguyên dương!");
                return null;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Sức chứa phải là số nguyên!");
            return null;
        }

        double gia;
        try {
            gia = Double.parseDouble(giaStr);
            if (gia <= 0) {
                JOptionPane.showMessageDialog(this, "Giá phải là số dương!");
                return null;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá phải là số (có thể nhập 700000 hoặc 700,000)!");
            return null;
        }

        if (moTa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mô tả không được để trống!");
            return null;
        }

        return new LoaiPhong(ma, ten, sucChua, gia, moTa);
    }

    // ================= EVENTS =================

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnSearchMa) {
            timTheoMaLoai();
            return;
        }

        if (src == btnXoaRong) {
            clearForm();
            return;
        }

        if (src == btnThem) {
            LoaiPhong lp = getFromFormSafe();
            if (lp == null) return;

            if (dao.findByMa(lp.getMaLoaiPhong()) != null) {
                JOptionPane.showMessageDialog(this, "Mã loại phòng đã tồn tại! Hãy bấm Lưu để cập nhật.");
                return;
            }

            if (dao.insertLoaiPhong(lp)) {
                JOptionPane.showMessageDialog(this, "✅ Thêm thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Thêm thất bại!");
            }
            return;
        }

        if (src == btnLuu) {
            LoaiPhong lp = getFromFormSafe();
            if (lp == null) return;

            if (dao.findByMa(lp.getMaLoaiPhong()) != null) {
                if (dao.updateLoaiPhong(lp)) {
                    JOptionPane.showMessageDialog(this, "💾 Cập nhật thành công!");
                    loadData();
                    focusLoaiPhongOnTable(lp.getMaLoaiPhong());
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ Cập nhật thất bại! Kiểm tra mã.");
                }
            } else {
                if (dao.insertLoaiPhong(lp)) {
                    JOptionPane.showMessageDialog(this, "✅ Thêm thành công!");
                    loadData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ Thêm thất bại!");
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int r = table.getSelectedRow();
        if (r >= 0) loadRowToForm(r);
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // ================= STYLE HELPERS =================

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
        t.setRowHeight(34);
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
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 38));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 253));
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        };

        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void applyGlobalFontDefaults() {
        Font ui = new Font(FONT_UI, Font.PLAIN, 13);
        UIManager.put("Label.font", ui);
        UIManager.put("Button.font", ui);
        UIManager.put("TextField.font", ui);
        UIManager.put("Table.font", ui);
        UIManager.put("TableHeader.font", new Font(FONT_UI, Font.BOLD, 13));
        UIManager.put("TitledBorder.font", new Font(FONT_UI, Font.BOLD, 13));
    }

    // ===== CHẠY THỬ =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Quản Lý Loại Phòng - Pate Hotel");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
            f.add(new LoaiPhong_GUI());
            f.setVisible(true);
        });
    }
}
