package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import dao.LoaiPhong_DAO;
import entity.LoaiPhong;

public class LoaiPhong_GUI extends JPanel implements ActionListener, MouseListener {

    private JTextField txtMaLoai, txtTenLoai, txtSucChua, txtGia, txtMoTa;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnThem, btnLuu, btnTim;
    private LoaiPhong_DAO dao = new LoaiPhong_DAO();

    public LoaiPhong_GUI() {
        initGUI();
        loadData();
    }

    private void initGUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("QUẢN LÝ LOẠI PHÒNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 26));
        lblTitle.setForeground(new Color(30, 60, 114));
        add(lblTitle, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        infoPanel.setBorder(new TitledBorder("Thông tin loại phòng"));
        infoPanel.setBackground(Color.WHITE);
        add(infoPanel, BorderLayout.NORTH);

        // ==== FORM ====
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);

        Dimension lblSize = new Dimension(120, 25);
        Dimension txtSize = new Dimension(250, 25);

        int horizontalGap = 50;
        int verticalGap = 10;

        // HÀNG 1: Mã loại phòng + Sức chứa
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, horizontalGap, verticalGap));
        row1.setBackground(Color.WHITE);

        JLabel lblMa = new JLabel("Mã loại phòng:");
        lblMa.setPreferredSize(lblSize);
        txtMaLoai = new JTextField(20);
        txtMaLoai.setPreferredSize(txtSize);

        JLabel lblSucChua = new JLabel("Sức chứa:");
        lblSucChua.setPreferredSize(lblSize);
        txtSucChua = new JTextField(20);
        txtSucChua.setPreferredSize(txtSize);

        row1.add(lblMa);
        row1.add(txtMaLoai);
        row1.add(lblSucChua);
        row1.add(txtSucChua);
        formPanel.add(row1);

        // HÀNG 2: Tên loại phòng + Mô tả
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, horizontalGap, verticalGap));
        row2.setBackground(Color.WHITE);

        JLabel lblTen = new JLabel("Tên loại phòng:");
        lblTen.setPreferredSize(lblSize);
        txtTenLoai = new JTextField(20);
        txtTenLoai.setPreferredSize(txtSize);

        JLabel lblMoTa = new JLabel("Mô tả:");
        lblMoTa.setPreferredSize(lblSize);
        txtMoTa = new JTextField(20);
        txtMoTa.setPreferredSize(txtSize);

        row2.add(lblTen);
        row2.add(txtTenLoai);
        row2.add(lblMoTa);
        row2.add(txtMoTa);
        formPanel.add(row2);

        // HÀNG 3: Giá
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, horizontalGap, verticalGap));
        row3.setBackground(Color.WHITE);

        JLabel lblGia = new JLabel("Giá:");
        lblGia.setPreferredSize(lblSize);
        txtGia = new JTextField(20);
        txtGia.setPreferredSize(txtSize);

        row3.add(lblGia);
        row3.add(txtGia);
        formPanel.add(row3);

        infoPanel.add(formPanel, BorderLayout.CENTER);

        // Nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnThem = new JButton("Thêm");
        btnLuu = new JButton("Lưu");
        btnTim = new JButton("Tìm mã");
        for (JButton b : new JButton[]{btnThem, btnLuu, btnTim}) {
            b.addActionListener(this);
            buttonPanel.add(b);
        }
        infoPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ==== Table ====
//        String[] cols = {"Mã", "Tên", "Sức chứa", "Giá", "Mô tả"};
//        model = new DefaultTableModel(cols, 0);
//        table = new JTable(model);
//        table.addMouseListener(this);
//        JScrollPane sp = new JScrollPane(table);
//        add(sp, BorderLayout.CENTER);
        String[] cols = {"Mã", "Tên", "Sức chứa", "Giá", "Mô tả"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.addMouseListener(this);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

     // Tỉ lệ chiều rộng cột: Mã : Tên : Sức chứa : Giá : Mô tả = 1:2:1:1:5
     double[] ratios = {1, 2, 1, 1, 5};
     double totalRatio = 10;  // 1+2+1+1+5

     JScrollPane sp = new JScrollPane(table);
     add(sp, BorderLayout.CENTER);

     // Tự động resize khi panel thay đổi kích thước
     sp.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
             int totalWidth = sp.getViewport().getWidth(); // tổng chiều rộng hiển thị bảng
             for (int i = 0; i < ratios.length; i++) {
                 int colWidth = (int) ((ratios[i] / totalRatio) * totalWidth);
                 table.getColumnModel().getColumn(i).setPreferredWidth(colWidth);
             }
         }
     });


    }

    private void loadData() {
        model.setRowCount(0);
        List<LoaiPhong> list = dao.getAllLoaiPhong();
        for (LoaiPhong lp : list) {
            model.addRow(new Object[]{
                    lp.getMaLoaiPhong(),
                    lp.getTenLoaiPhong(),
                    lp.getSucChua(),
                    lp.getGia(),
                    lp.getMoTa()
            });
        }
    }

    // ==== Lấy dữ liệu từ form, an toàn với số và dữ liệu rỗng ====
    private LoaiPhong getFromFormSafe() {
        String ma = txtMaLoai.getText().trim();
        String ten = txtTenLoai.getText().trim();
        String sucChuaStr = txtSucChua.getText().trim();
        String giaStr = txtGia.getText().trim();
        String moTa = txtMoTa.getText().trim();

        // Kiểm tra xem có nhập đầy đủ thông tin không
        
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã loại phòng không được để trống!");
            return null;
        }
        // Kiểm tra định dạng mã loại phòng: LP + số
        if (!ma.matches("LP\\d{3}")) {
            JOptionPane.showMessageDialog(this, "Mã loại phòng phải có dạng LP + 3 chữ số, ví dụ LP001!");
            return null;
        }
        
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên loại phòng không được để trống!");
            return null;
        }
        // Kiểm tra tên loại phòng không vượt quá 50 ký tự
        if (ten.length() > 50) {
            JOptionPane.showMessageDialog(this, "Tên loại phòng không được vượt quá 50 ký tự!");
            return null;
        }

        if (sucChuaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sức chứa không được để trống!");
            return null;
        }
        // Kiểm tra sức chứa
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

        if (giaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá không được để trống!");
            return null;
        }

        // Kiểm tra giá
        double gia;
        try {
            gia = Double.parseDouble(giaStr);
            if (gia <= 0) {
                JOptionPane.showMessageDialog(this, "Giá phải là số thực dương!");
                return null;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá phải là số!");
            return null;
        }

        if (ma.isEmpty() || ten.isEmpty() || sucChuaStr.isEmpty() || giaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return null;
        }
        
        if (moTa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mô tả không được để trống!");
            return null;
        }
        
        // Trả về đối tượng LoaiPhong nếu tất cả kiểm tra đều hợp lệ
        return new LoaiPhong(ma, ten, sucChua, gia, moTa);
    }

    private void clearForm() {
        txtMaLoai.setText("");
        txtTenLoai.setText("");
        txtSucChua.setText("");
        txtGia.setText("");
        txtMoTa.setText("");
        table.clearSelection();
    }

    private void selectRow(String ma) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(ma)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnThem) {
            LoaiPhong lp = getFromFormSafe();
            if (lp != null) {
                if (dao.insertLoaiPhong(lp)) {
                    JOptionPane.showMessageDialog(this, "✅ Thêm thành công!");
                    loadData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ Thêm thất bại! Mã có thể đã tồn tại.");
                }
            }
        } else if (src == btnLuu) {
            LoaiPhong lp = getFromFormSafe();
            if (lp != null) {
                if (dao.updateLoaiPhong(lp)) {
                    JOptionPane.showMessageDialog(this, "💾 Cập nhật thành công!");
                    loadData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ Cập nhật thất bại! Kiểm tra mã.");
                }
            }
        } else if (src == btnTim) {
            String ma = txtMaLoai.getText().trim();
            if (ma.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã cần tìm!");
                return;
            }
            LoaiPhong lp = dao.findByMa(ma);
            if (lp != null) {
                txtTenLoai.setText(lp.getTenLoaiPhong());
                txtSucChua.setText(String.valueOf(lp.getSucChua()));
                txtGia.setText(String.valueOf(lp.getGia()));
                txtMoTa.setText(lp.getMoTa());
                selectRow(ma);
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Không tìm thấy mã: " + ma);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int r = table.getSelectedRow();
        if (r != -1) {
            txtMaLoai.setText(model.getValueAt(r, 0).toString());
            txtTenLoai.setText(model.getValueAt(r, 1).toString());
            txtSucChua.setText(model.getValueAt(r, 2).toString());
            txtGia.setText(model.getValueAt(r, 3).toString());
            txtMoTa.setText(model.getValueAt(r, 4).toString());
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // ==== Test frame độc lập ====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Quản lý loại phòng");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
            f.add(new LoaiPhong_GUI());
            f.setVisible(true);
        });
    }
}
