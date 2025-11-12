package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.LoaiPhong_DAO;
import dao.Phong_DAO;
import entity.LoaiPhong;
import entity.Phong;

public class Phong_GUI extends JPanel implements ActionListener, MouseListener {

    private JTextField textFieldMaPhong, textFieldTrangThai;
    private JComboBox<String> comboLoaiPhong;
    private JTable table;
    private JButton btnChiTietLoaiPhong, btnThem, btnLuu, btnTim;

    private Phong_DAO phongDAO;
    private LoaiPhong_DAO loaiPhongDAO;
    private DefaultTableModel tableModel;

    public Phong_GUI() {
        phongDAO = new Phong_DAO();
        loaiPhongDAO = new LoaiPhong_DAO();

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === Tiêu đề ===
        JLabel lblTitle = new JLabel("QUẢN LÝ PHÒNG", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
        add(lblTitle, BorderLayout.NORTH);

        // === Panel chính giữa ===
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        add(centerPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Tahoma", Font.PLAIN, 16);

        // --- Mã Phòng ---
        JLabel lblMaPhong = new JLabel("Mã Phòng:");
        lblMaPhong.setFont(labelFont);
        textFieldMaPhong = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(lblMaPhong, gbc);
        gbc.gridx = 1;
        centerPanel.add(textFieldMaPhong, gbc);
textFieldMaPhong.setEditable(false);

        // --- Trạng Thái ---
        JLabel lblTrangThai = new JLabel("Trạng Thái:");
        lblTrangThai.setFont(labelFont);
        textFieldTrangThai = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(lblTrangThai, gbc);
        gbc.gridx = 1;
        centerPanel.add(textFieldTrangThai, gbc);

        // --- Loại Phòng ---
        JLabel lblLoaiPhong = new JLabel("Loại Phòng:");
        lblLoaiPhong.setFont(labelFont);
        comboLoaiPhong = new JComboBox<>();
        comboLoaiPhong.setPreferredSize(new Dimension(200, 25));

        btnChiTietLoaiPhong = new JButton("🔍");
        btnChiTietLoaiPhong.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        btnChiTietLoaiPhong.setFocusPainted(false);
        btnChiTietLoaiPhong.setBackground(Color.WHITE);
        btnChiTietLoaiPhong.setBorder(BorderFactory.createEmptyBorder());
        btnChiTietLoaiPhong.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel loaiPhongPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        loaiPhongPanel.setBackground(new Color(245, 245, 245));
        loaiPhongPanel.add(comboLoaiPhong);
        loaiPhongPanel.add(btnChiTietLoaiPhong);

        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(lblLoaiPhong, gbc);
        gbc.gridx = 1;
        centerPanel.add(loaiPhongPanel, gbc);

        // ==== Nút Thêm, Lưu, Tìm kiếm ====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(new Color(245, 245, 245));
        btnThem = new JButton("Thêm");
        btnLuu = new JButton("Lưu");
        btnTim = new JButton("Tìm kiếm mã phòng");
        btnPanel.add(btnThem);
        btnPanel.add(btnLuu);
        btnPanel.add(btnTim);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(btnPanel, gbc);

        // ==== Ảnh phòng bên phải ====
        JLabel lblAnh = new JLabel();
        lblAnh.setHorizontalAlignment(SwingConstants.CENTER);
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblAnh.setPreferredSize(new Dimension(400, 250));
        lblAnh.setIcon(new ImageIcon(
                new ImageIcon("src/image/room_main.jpg").getImage()
                        .getScaledInstance(400, 250, Image.SCALE_SMOOTH)
        ));

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(lblAnh, gbc);

        // ==== Bảng danh sách phòng ====
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(245, 245, 245));
        JLabel lblDS = new JLabel("DANH SÁCH PHÒNG", SwingConstants.CENTER);
        lblDS.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblDS.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        tablePanel.add(lblDS, BorderLayout.NORTH);

        String[] columns = {"STT", "Mã Phòng", "Loại Phòng", "Trạng Thái", "Sức Chứa", "Giá Phòng"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.SOUTH);


        // ==== Đăng ký sự kiện ====
        btnThem.addActionListener(this);
        btnLuu.addActionListener(this);
        btnTim.addActionListener(this);
        table.addMouseListener(this);
        
        btnChiTietLoaiPhong.addActionListener(e -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(Phong_GUI.this), "Quản Lý Loại Phòng", true);
            dialog.setSize(1000, 600);
            dialog.setLocationRelativeTo(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.add(new LoaiPhong_GUI());
            dialog.setVisible(true);
        });


        loadComboLoaiPhong();
        loadTablePhong();

     comboLoaiPhong.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
             if (e.getStateChange() == ItemEvent.SELECTED) {
                 String selected = (String) comboLoaiPhong.getSelectedItem();
                 if (selected != null) {
                     String maLoai = selected.split(" - ")[0];
                     LoaiPhong lp = loaiPhongDAO.getLoaiPhongTheoMa(maLoai);
                     if (lp != null) {
                         String maPhongMoi = generateNextRoomCode(lp);
                         textFieldMaPhong.setText(maPhongMoi);
                     }
                 }
             }
         }
     });


    }

    // ==== Load dữ liệu lên JComboBox Loại Phòng ====
    private void loadComboLoaiPhong() {
        comboLoaiPhong.removeAllItems();
        List<LoaiPhong> dsLoai = loaiPhongDAO.getAllLoaiPhong();
        for (LoaiPhong lp : dsLoai) {
            comboLoaiPhong.addItem(lp.getMaLoaiPhong() + " - " + lp.getTenLoaiPhong());
        }
    }

    // ==== Load dữ liệu lên JTable ====
    private void loadTablePhong() {
        tableModel.setRowCount(0);
        List<Phong> dsPhong = phongDAO.getAllPhong();
        int stt = 1;
        for (Phong p : dsPhong) {
            Object[] row = {
                    stt++,
                    p.getMaPhong(),
                    p.getLoaiPhong() != null ? p.getLoaiPhong().getTenLoaiPhong() : "",
                    p.getTrangThai(),
                    p.getLoaiPhong() != null ? p.getLoaiPhong().getSucChua() : "",
                    p.getLoaiPhong() != null ? p.getLoaiPhong().getGia() : ""
            };
            tableModel.addRow(row);
        }
    }

private String generateNextRoomCode(LoaiPhong loaiPhong) {
        // Xác định prefix theo loại phòng
        String prefix;
        String tenLoai = loaiPhong.getTenLoaiPhong().toLowerCase();

        if (tenLoai.contains("tiêu chuẩn") || tenLoai.contains("standard"))
            prefix = "P1";
        else if (tenLoai.contains("cao cấp") || tenLoai.contains("deluxe"))
            prefix = "P2";
        else if (tenLoai.contains("gia đình"))
            prefix = "P3";
        else
            prefix = "P9"; // Loại không xác định

        // Lấy mã cao nhất hiện có cho loại này
        String lastCode = phongDAO.getLastRoomCodeByLoai(prefix);

        if (lastCode == null) {
            return prefix + "01";
        }

        // Tách phần số sau prefix, tăng lên 1
        String numberPart = lastCode.substring(prefix.length());
        int nextNumber = Integer.parseInt(numberPart) + 1;

        // Giữ đúng định dạng (vd: P101, P102...)
        return prefix + String.format("%02d", nextNumber);
    }


private boolean validateForm(String maPhong, String trangThai, LoaiPhong loaiPhong) {
    // ===== KIỂM TRA MÃ PHÒNG =====
    if (maPhong == null || maPhong.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Mã phòng không được để trống!");
        return false;
    }

    maPhong = maPhong.trim().toUpperCase();

    // Định dạng phải là P + 3 số (VD: P101)
    if (!maPhong.matches("^P\\d{3}$")) {
        JOptionPane.showMessageDialog(this, "Mã phòng phải có dạng Pxxx (ví dụ: P101).");
        return false;
    }

    // ===== KIỂM TRA TRẠNG THÁI =====
    if (trangThai == null || trangThai.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Trạng thái phòng không được để trống!");
        return false;
    }

    String trangThaiNormalized = trangThai.trim().toLowerCase();

    if (!(trangThaiNormalized.equals("bình thường")
            || trangThaiNormalized.equals("bảo trì"))) {
        JOptionPane.showMessageDialog(this,
                "Trạng thái phải là một trong các giá trị: Bình thường, Bảo trì");
        return false;
    }


    // ===== KIỂM TRA LOẠI PHÒNG =====
    if (loaiPhong == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!");
        return false;
    }

    // ===== KIỂM TRA CÁC THUỘC TÍNH CỦA LOẠI PHÒNG =====
    if (loaiPhong.getMaLoaiPhong() == null || loaiPhong.getMaLoaiPhong().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Mã loại phòng không được để trống!");
        return false;
    }

    if (loaiPhong.getTenLoaiPhong() == null || loaiPhong.getTenLoaiPhong().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Tên loại phòng không được để trống!");
        return false;
    }

    if (loaiPhong.getGia() <= 0) {
        JOptionPane.showMessageDialog(this, "Giá phòng phải lớn hơn 0!");
        return false;
    }

    if (loaiPhong.getSucChua() <= 0) {
        JOptionPane.showMessageDialog(this, "Sức chứa phải lớn hơn 0!");
        return false;
    }

    // Nếu tất cả đều hợp lệ
    return true;
}




    // ==== Xử lý sự kiện ====
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
//
//        if (o == btnThem) {
//        	
//            textFieldMaPhong.setText("");
//            textFieldTrangThai.setText("");
//            if (comboLoaiPhong.getItemCount() > 0)
//                comboLoaiPhong.setSelectedIndex(0);
//
//        } 
          if (o == btnThem) {
            String maPhong = textFieldMaPhong.getText().trim();
            String trangThai = textFieldTrangThai.getText().trim();
            String selected = (String) comboLoaiPhong.getSelectedItem();

            if (selected == null || maPhong.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng và trạng thái!");
                return;
            }

            String maLoai = selected.split(" - ")[0];
            LoaiPhong lp = loaiPhongDAO.getLoaiPhongTheoMa(maLoai);

            if (!validateForm(maPhong, trangThai, lp))
                return;

            Phong p = new Phong(maPhong, lp, trangThai);
            if (phongDAO.themPhong(p))
                JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
            else
                JOptionPane.showMessageDialog(this, "Thêm phòng thất bại!");

            loadTablePhong();
        }

        else if (o == btnLuu) {
            String maPhong = textFieldMaPhong.getText().trim();
            String trangThai = textFieldTrangThai.getText().trim();
            String selected = (String) comboLoaiPhong.getSelectedItem();
            if (maPhong.isEmpty() || selected == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            String maLoai = selected.split(" - ")[0];
            LoaiPhong lp = loaiPhongDAO.getLoaiPhongTheoMa(maLoai);
            
            // GỌI HÀM KIỂM TRA DỮ LIỆU
            if (!validateForm(maPhong, trangThai, lp)) {
                return; // nếu dữ liệu không hợp lệ thì dừng lại
            }
            Phong p = new Phong(maPhong, lp, trangThai);

            // Kiểm tra tồn tại
            if (phongDAO.timPhongTheoMa(maPhong) != null) {
                if (phongDAO.capNhatPhong(p)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật phòng thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật phòng thất bại!");
                }
            } else {
                if (phongDAO.themPhong(p)) {
                    JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm phòng thất bại!");
                }
            }
            loadTablePhong();

        } else if (o == btnTim) {
            String maPhong = JOptionPane.showInputDialog(this, "Nhập mã phòng cần tìm:");
            if (maPhong != null && !maPhong.trim().isEmpty()) {
                Phong p = phongDAO.timPhongTheoMa(maPhong.trim());
                if (p != null) {
                    textFieldMaPhong.setText(p.getMaPhong());
                    textFieldTrangThai.setText(p.getTrangThai());
                    if (p.getLoaiPhong() != null) {
                        for (int i = 0; i < comboLoaiPhong.getItemCount(); i++) {
                            if (comboLoaiPhong.getItemAt(i).startsWith(p.getLoaiPhong().getMaLoaiPhong())) {
                                comboLoaiPhong.setSelectedIndex(i);
                                break;
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy phòng!");
                }
            }
        }
    }

    // ==== MouseListener để điền thông tin khi click vào JTable ====
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            textFieldMaPhong.setText((String) table.getValueAt(row, 1));
            textFieldTrangThai.setText((String) table.getValueAt(row, 3));
            String tenLoai = (String) table.getValueAt(row, 2);
            for (int i = 0; i < comboLoaiPhong.getItemCount(); i++) {
                if (comboLoaiPhong.getItemAt(i).endsWith(tenLoai)) {
                    comboLoaiPhong.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // ===== CHẠY THỬ =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Quản Lý Phòng");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
            f.add(new Phong_GUI());
            f.setVisible(true);
        });
    }
}
