package gui;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import connectDB.ConnectDB;
import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.NhanVien;

public class NhanVien_GUI extends JPanel implements ActionListener, MouseListener {

    // ===== Tone màu chủ đạo (navy + gold) =====
    private static final Color NAVY = new Color(10, 52, 89);
    private static final Color NAVY_DARK = new Color(7, 40, 68);
    private static final Color GOLD = new Color(218, 177, 55);
    private static final Color LIGHT_BG = new Color(245, 247, 250);
    private static final Color BORDER = new Color(220, 227, 235);

    private static final String FONT_UI = "Segoe UI";
    private static final String FONT_EMOJI = "Segoe UI Emoji";

    private static final Dimension SEARCH_BTN_SIZE = new Dimension(52, 38);
    private static final Dimension DATE_SIZE = new Dimension(240, 38);

    private JTextField txtMaNV, txtHoTen, txtSoDT, txtEmail;
    private JRadioButton rdoNam, rdoNu, rdoQuanLy, rdoNhanVien, rdoHoatDong, rdoNghi;
    private JTable table;
    private JDateChooser dateNgaySinh, dateNgayTao;

    private JButton btnThem, btnLuu, btnXoaRong;
    private JButton btnSearchMa, btnSearchSdt;

    private DefaultTableModel modelNV;
    private final NhanVien_DAO nvDAO = new NhanVien_DAO();

    private boolean isRowSelected = false;

    // ✅ Trạng thái cho phép nhập mã NV chỉ để tìm kiếm
    private boolean isSearchModeMaNV = false;

    private static final DateTimeFormatter VIEW_DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter PARSE_DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public NhanVien_GUI() {
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

        try {
            ConnectDB.getInstance().connect();
            loadNhanVienToTable();
            clearForm(); // ✅ clear sẽ tự sinh mã NV sẵn
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối CSDL: " + e.getMessage());
        }
    }

    // ================= UI BUILDERS =================

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel title = new JLabel("QUẢN LÝ NHÂN VIÊN");
        title.setForeground(Color.WHITE);
        title.setFont(new Font(FONT_UI, Font.BOLD, 26));

        JLabel sub = new JLabel("Pate Hotel • Nhập/Tra cứu/Cập nhật thông tin nhân sự");
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

        JLabel t = new JLabel("Thông tin nhân viên");
        t.setFont(new Font(FONT_UI, Font.BOLD, 16));
        t.setForeground(NAVY_DARK);
        card.add(t, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;

        // Row 0
        gbc.gridy = 0;
        addLabel(form, gbc, 0, "Mã nhân viên:");
        form.add(buildMaPanel(), fieldGbc(gbc, 1));

        addLabel(form, gbc, 2, "Họ tên:");
        txtHoTen = new JTextField();
        styleField(txtHoTen);
        form.add(txtHoTen, fieldGbc(gbc, 3));

        // Row 1
        gbc.gridy = 1;
        addLabel(form, gbc, 0, "Giới tính:");
        form.add(buildGenderPanel(), fieldGbc(gbc, 1));

        addLabel(form, gbc, 2, "Ngày sinh:");
        dateNgaySinh = new JDateChooser();
        dateNgaySinh.setDateFormatString("dd/MM/yyyy");
        styleDateChooser(dateNgaySinh);
        form.add(dateNgaySinh, fieldGbc(gbc, 3));

        // Row 2
        gbc.gridy = 2;
        addLabel(form, gbc, 0, "Số điện thoại:");
        form.add(buildSdtPanel(), fieldGbc(gbc, 1));

        addLabel(form, gbc, 2, "Email:");
        txtEmail = new JTextField();
        styleField(txtEmail);
        form.add(txtEmail, fieldGbc(gbc, 3));

        // Row 3
        gbc.gridy = 3;
        addLabel(form, gbc, 0, "Chức vụ:");
        form.add(buildRolePanel(), fieldGbc(gbc, 1));

        addLabel(form, gbc, 2, "Ngày tạo:");
        dateNgayTao = new JDateChooser();
        dateNgayTao.setDateFormatString("dd/MM/yyyy");
        dateNgayTao.setDate(new Date());
        styleDateChooser(dateNgayTao);
        form.add(dateNgayTao, fieldGbc(gbc, 3));

        // Row 4
        gbc.gridy = 4;
        addLabel(form, gbc, 0, "Trạng thái:");
        form.add(buildStatusPanel(), fieldGbc(gbc, 1));

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);

        btnThem = createButton("Thêm", GOLD, NAVY_DARK);
        btnLuu = createButton("Lưu", NAVY, Color.WHITE);
        btnXoaRong = createButton("Xóa rỗng", new Color(230, 236, 244), NAVY_DARK);

        btnThem.addActionListener(this);
        btnLuu.addActionListener(this);
        btnXoaRong.addActionListener(this);

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

        JLabel t = new JLabel("Danh sách nhân viên");
        t.setFont(new Font(FONT_UI, Font.BOLD, 16));
        t.setForeground(NAVY_DARK);
        card.add(t, BorderLayout.NORTH);

        String[] cols = {"STT", "Mã NV", "Họ Tên", "Giới Tính", "Ngày Sinh", "SĐT", "Email", "Chức Vụ", "Ngày Tạo", "Trạng Thái"};
        modelNV = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(modelNV);
        table.addMouseListener(this);
        styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(BORDER, 1, true));
        card.add(sp, BorderLayout.CENTER);

        return card;
    }

    private JPanel buildMaPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);

        txtMaNV = new JTextField();
        styleField(txtMaNV);

        // ✅ mặc định KHÔNG cho nhập mã (vì mã tự sinh)
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(248, 250, 253));
        txtMaNV.setToolTipText("Mã nhân viên tự phát sinh. Bấm 🔍 để nhập mã tìm kiếm.");

        btnSearchMa = buildSearchButton("Tìm nhanh theo mã nhân viên");
        btnSearchMa.addActionListener(this);

        // ✅ Enter chỉ dùng khi đang ở search mode
        txtMaNV.addActionListener(e -> {
            if (isSearchModeMaNV) timTheoMa();
        });

        p.add(txtMaNV, BorderLayout.CENTER);
        p.add(btnSearchMa, BorderLayout.EAST);
        return p;
    }

    private JPanel buildSdtPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);

        txtSoDT = new JTextField();
        styleField(txtSoDT);

        btnSearchSdt = buildSearchButton("Tìm nhanh theo số điện thoại");
        btnSearchSdt.addActionListener(this);

        txtSoDT.addActionListener(e -> timTheoSoDT());

        p.add(txtSoDT, BorderLayout.CENTER);
        p.add(btnSearchSdt, BorderLayout.EAST);
        return p;
    }

    private JPanel buildGenderPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        p.setOpaque(false);

        rdoNam = new JRadioButton("Nam");
        rdoNu = new JRadioButton("Nữ");
        styleRadio(rdoNam); styleRadio(rdoNu);

        ButtonGroup g = new ButtonGroup();
        g.add(rdoNam); g.add(rdoNu);
        rdoNu.setSelected(true);

        p.add(rdoNam);
        p.add(rdoNu);
        return p;
    }

    private JPanel buildRolePanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        p.setOpaque(false);

        rdoQuanLy = new JRadioButton("Quản lý");
        rdoNhanVien = new JRadioButton("Nhân viên");
        styleRadio(rdoQuanLy); styleRadio(rdoNhanVien);

        ButtonGroup g = new ButtonGroup();
        g.add(rdoQuanLy); g.add(rdoNhanVien);
        rdoNhanVien.setSelected(true);

        p.add(rdoQuanLy);
        p.add(rdoNhanVien);
        return p;
    }

    private JPanel buildStatusPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        p.setOpaque(false);

        rdoHoatDong = new JRadioButton("Đang làm");
        rdoNghi = new JRadioButton("Nghỉ");
        styleRadio(rdoHoatDong); styleRadio(rdoNghi);

        ButtonGroup g = new ButtonGroup();
        g.add(rdoHoatDong); g.add(rdoNghi);
        rdoHoatDong.setSelected(true);

        p.add(rdoHoatDong);
        p.add(rdoNghi);
        return p;
    }

    // ================= DATA / LOGIC =================

    private void loadNhanVienToTable() {
        modelNV.setRowCount(0);
        ArrayList<NhanVien> list = (ArrayList<NhanVien>) nvDAO.docTuBang();
        int stt = 1;

        for (NhanVien nv : list) {
            modelNV.addRow(new Object[]{
                    stt++,
                    nv.getMaNhanVien(),
                    nv.getHoten(),
                    nv.isGioiTinh() ? "Nam" : "Nữ",
                    nv.getNgaySinh() != null ? nv.getNgaySinh().format(VIEW_DATE_FMT) : "",
                    nv.getSoDienThoai(),
                    nv.getEmail(),
                    nv.isChucVu() ? "Quản lý" : "Nhân viên",
                    nv.getNgayTao() != null ? nv.getNgayTao().format(VIEW_DATE_FMT) : "",
                    nv.isTrangThai() ? "Đang làm" : "Nghỉ"
            });
        }
    }

    private void clearForm() {
        isRowSelected = false;
        table.clearSelection();

        // ✅ tự sinh mã sẵn, KHÔNG cho nhập
        LocalDate ngayTao = LocalDate.now();
        String maMoi = nvDAO.generateMaNhanVien(ngayTao);
        txtMaNV.setText(maMoi);
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(248, 250, 253));
        isSearchModeMaNV = false;

        txtHoTen.setText("");
        txtSoDT.setText("");
        txtEmail.setText("");

        rdoNu.setSelected(true);
        rdoNhanVien.setSelected(true);
        rdoHoatDong.setSelected(true);

        dateNgaySinh.setDate(null);
        dateNgayTao.setDate(new Date());

        txtHoTen.requestFocus();
    }

    private LocalDate toLocalDate(JDateChooser chooser) {
        if (chooser.getDate() == null) return null;
        return chooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // ✅ check trùng SĐT / Email (loại trừ chính nó khi update)
    private boolean isDuplicateSoDT(String soDT, String excludeMaNV) {
        ArrayList<NhanVien> list = (ArrayList<NhanVien>) nvDAO.docTuBang();
        for (NhanVien nv : list) {
            if (nv == null) continue;
            if (excludeMaNV != null && excludeMaNV.equalsIgnoreCase(nv.getMaNhanVien())) continue;
            if (soDT.equals(nv.getSoDienThoai())) return true;
        }
        return false;
    }

    private boolean isDuplicateEmail(String email, String excludeMaNV) {
        ArrayList<NhanVien> list = (ArrayList<NhanVien>) nvDAO.docTuBang();
        for (NhanVien nv : list) {
            if (nv == null) continue;
            if (excludeMaNV != null && excludeMaNV.equalsIgnoreCase(nv.getMaNhanVien())) continue;
            if (email.equalsIgnoreCase(nv.getEmail())) return true;
        }
        return false;
    }

    // ===== Validate (18-60, không trùng SĐT/Gmail) =====
    private boolean validateFormForSave(boolean isInsert) {
        String hoTen = txtHoTen.getText().trim();
        String soDT = txtSoDT.getText().trim();
        String email = txtEmail.getText().trim();
        String ma = txtMaNV.getText().trim();

        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Thiếu mã nhân viên!");
            return false;
        }

        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống!");
            return false;
        }

        LocalDate ngaySinh = toLocalDate(dateNgaySinh);
        if (ngaySinh == null) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không được để trống!");
            return false;
        }

        LocalDate today = LocalDate.now();
        if (ngaySinh.isAfter(today)) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ (không được lớn hơn hôm nay).");
            return false;
        }

        int age = Period.between(ngaySinh, today).getYears();
        if (age < 18 || age > 60) {
            JOptionPane.showMessageDialog(this, "Nhân viên phải từ đủ 18 tuổi đến 60 tuổi!");
            return false;
        }

        if (!soDT.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải bắt đầu bằng 0 và gồm đúng 10 số!");
            return false;
        }

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email không được để trống!");
            return false;
        }
        if (email.length() > 100) {
            JOptionPane.showMessageDialog(this, "Email tối đa 100 ký tự!");
            return false;
        }
        if (!email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
            JOptionPane.showMessageDialog(this, "Email phải đúng dạng abc@gmail.com");
            return false;
        }

        // ✅ kiểm tra trùng
        // - insert: exclude = null
        // - update: exclude = ma hiện tại
        String exclude = isInsert ? null : ma;

        if (isDuplicateSoDT(soDT, exclude)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại bị trùng! Vui lòng nhập SĐT khác.");
            return false;
        }

        if (isDuplicateEmail(email, exclude)) {
            JOptionPane.showMessageDialog(this, "Email bị trùng! Vui lòng nhập email khác.");
            return false;
        }

        return true;
    }

    private NhanVien getFormDataForInsertOrUpdate(boolean isInsert) {
        if (!validateFormForSave(isInsert)) return null;

        String ma;
        LocalDate ngayTao = toLocalDate(dateNgayTao);
        if (ngayTao == null) ngayTao = LocalDate.now();

        // ✅ yêu cầu: mã NV luôn tự sinh khi thêm, không cho nhập
        if (isInsert) {
            ma = nvDAO.generateMaNhanVien(ngayTao);
            txtMaNV.setText(ma);
        } else {
            ma = txtMaNV.getText().trim();
        }

        String ten = txtHoTen.getText().trim();
        String sdt = txtSoDT.getText().trim();
        String email = txtEmail.getText().trim();

        boolean gt = rdoNam.isSelected();
        boolean cv = rdoQuanLy.isSelected();
        boolean tt = rdoHoatDong.isSelected();

        LocalDate ngaySinh = toLocalDate(dateNgaySinh);

        return new NhanVien(ma, ten, gt, ngaySinh, sdt, email, cv, ngayTao, tt);
    }

    // ================= SEARCH UX =================

    private void enableSearchModeMaNV() {
        isSearchModeMaNV = true;
        txtMaNV.setEditable(true);
        txtMaNV.setBackground(Color.WHITE);
        txtMaNV.setText("");
        txtMaNV.requestFocus();
        txtMaNV.selectAll();
        txtMaNV.setToolTipText("Nhập mã NV để tìm, xong bấm Enter hoặc 🔍.");
    }

    private void disableSearchModeMaNVAndRestoreGenerated() {
        isSearchModeMaNV = false;
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(248, 250, 253));
        // trả lại mã tự sinh sẵn để chuẩn bị thêm
        LocalDate ngayTao = toLocalDate(dateNgayTao);
        if (ngayTao == null) ngayTao = LocalDate.now();
        txtMaNV.setText(nvDAO.generateMaNhanVien(ngayTao));
        txtMaNV.setToolTipText("Mã nhân viên tự phát sinh. Bấm 🔍 để nhập mã tìm kiếm.");
    }

    private void timTheoMa() {
        String ma = txtMaNV.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã nhân viên để tìm!");
            return;
        }
        NhanVien nv = nvDAO.findByMa(ma);
        if (nv == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên với mã: " + ma);
            return;
        }
        focusNhanVienOnTable(nv.getMaNhanVien());
    }

    private void timTheoSoDT() {
        String sdt = txtSoDT.getText().trim();
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập số điện thoại để tìm!");
            return;
        }
        if (!sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "SĐT tìm kiếm phải bắt đầu bằng 0 và gồm đúng 10 số!");
            return;
        }
        NhanVien nv = nvDAO.findBySoDT(sdt);
        if (nv == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên với SĐT: " + sdt);
            return;
        }
        focusNhanVienOnTable(nv.getMaNhanVien());
    }

    private void focusNhanVienOnTable(String maNV) {
        for (int i = 0; i < modelNV.getRowCount(); i++) {
            String ma = modelNV.getValueAt(i, 1).toString();
            if (ma.equalsIgnoreCase(maNV)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                loadRowToForm(i);
                return;
            }
        }
        loadNhanVienToTable();
    }

    private void loadRowToForm(int row) {
        if (row < 0) return;

        isRowSelected = true;
        isSearchModeMaNV = false;

        txtMaNV.setText(modelNV.getValueAt(row, 1).toString());
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(248, 250, 253));

        txtHoTen.setText(modelNV.getValueAt(row, 2).toString());

        String gt = modelNV.getValueAt(row, 3).toString();
        rdoNam.setSelected(gt.equalsIgnoreCase("Nam"));
        rdoNu.setSelected(gt.equalsIgnoreCase("Nữ"));

        String ns = modelNV.getValueAt(row, 4).toString();
        if (!ns.isBlank()) {
            LocalDate d = LocalDate.parse(ns, PARSE_DATE_FMT);
            dateNgaySinh.setDate(Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else dateNgaySinh.setDate(null);

        txtSoDT.setText(modelNV.getValueAt(row, 5).toString());
        txtEmail.setText(modelNV.getValueAt(row, 6).toString());

        String cv = modelNV.getValueAt(row, 7).toString();
        rdoQuanLy.setSelected(cv.equalsIgnoreCase("Quản lý"));
        rdoNhanVien.setSelected(cv.equalsIgnoreCase("Nhân viên"));

        String nt = modelNV.getValueAt(row, 8).toString();
        if (!nt.isBlank()) {
            LocalDate d = LocalDate.parse(nt, PARSE_DATE_FMT);
            dateNgayTao.setDate(Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else dateNgayTao.setDate(new Date());

        String tt = modelNV.getValueAt(row, 9).toString();
        rdoHoatDong.setSelected(tt.equalsIgnoreCase("Đang làm"));
        rdoNghi.setSelected(tt.equalsIgnoreCase("Nghỉ"));
    }

    // ================= EVENTS =================

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == btnSearchMa) {
            // ✅ bấm 🔍: nếu đang không search mode -> bật search mode để nhập mã
            // nếu đang search mode -> thực hiện tìm luôn
            if (!isSearchModeMaNV) {
                enableSearchModeMaNV();
            } else {
                timTheoMa();
            }
            return;
        }

        if (o == btnSearchSdt) { timTheoSoDT(); return; }

        if (o == btnXoaRong) {
            clearForm();
            return;
        }

        if (o == btnThem) {
            // ✅ thêm: luôn tự sinh mã, user không nhập mã
            NhanVien nv = getFormDataForInsertOrUpdate(true);
            if (nv == null) return;

            if (nvDAO.create(nv)) {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công! Mã: " + nv.getMaNhanVien());
                loadNhanVienToTable();
                taoTaiKhoanMacDinh();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
            return;
        }

        if (o == btnLuu) {
            if (!isRowSelected) {
                JOptionPane.showMessageDialog(this, "Hãy chọn 1 nhân viên trong bảng rồi bấm Lưu để cập nhật!");
                return;
            }

            NhanVien nv = getFormDataForInsertOrUpdate(false);
            if (nv == null) return;

            if (nvDAO.update(nv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadNhanVienToTable();
                focusNhanVienOnTable(nv.getMaNhanVien());
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
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

    // ================= STYLE HELPERS (giữ nguyên style của bạn) =================

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
        f.setPreferredSize(new Dimension(260, 38));
    }

    private void styleDateChooser(JDateChooser dc) {
        dc.setFont(new Font(FONT_UI, Font.PLAIN, 14));
        dc.setPreferredSize(DATE_SIZE);
        dc.setMinimumSize(DATE_SIZE);

        if (dc.getDateEditor() != null && dc.getDateEditor().getUiComponent() instanceof JTextField tf) {
            tf.setFont(new Font(FONT_UI, Font.PLAIN, 14));
            tf.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(8, 10, 8, 30)));
            tf.setBackground(Color.WHITE);
            tf.setColumns(12);
            tf.setPreferredSize(DATE_SIZE);
        }
    }

    private void styleRadio(JRadioButton r) {
        r.setOpaque(false);
        r.setFont(new Font(FONT_UI, Font.PLAIN, 14));
        r.setForeground(NAVY_DARK);
        r.setFocusPainted(false);
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
        UIManager.put("RadioButton.font", ui);
        UIManager.put("Table.font", ui);
        UIManager.put("TableHeader.font", new Font(FONT_UI, Font.BOLD, 13));
        UIManager.put("TitledBorder.font", new Font(FONT_UI, Font.BOLD, 13));
    }
    
    private void taoTaiKhoanMacDinh() {

        String maNV = txtMaNV.getText().trim();

        if (maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên!");
            return;
        }

        TaiKhoan_DAO tkDAO = new TaiKhoan_DAO();

        // kiểm tra đã tồn tại tài khoản chưa
        if (tkDAO.kiemTraTonTai(maNV)) {
            JOptionPane.showMessageDialog(this, "Tài khoản đã tồn tại!");
            return;
        }

        boolean ok = tkDAO.taoTaiKhoanMacDinh(maNV);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                "Tạo tài khoản thành công!\n" +
                "Tài khoản: " + maNV + "\n" +
                "Mật khẩu mặc định: 123456"
            );
        } else {
            JOptionPane.showMessageDialog(this, "Tạo tài khoản thất bại!");
        }
    }

    // ===== chạy thử =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Pate Hotel - Hệ thống quản lý khách sạn");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
            f.add(new NhanVien_GUI());
            f.setVisible(true);
        });
    }
}
