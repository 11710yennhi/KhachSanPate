package gui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class HuongDanSuDung_GUI extends JPanel {

    private JComboBox<String> cbbChucNang;
    private JTextArea txtHuongDan;

    private List<String> dsChucNang;
    private Map<String, String> noiDungHuongDan;

    public HuongDanSuDung_GUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        taoDuLieu();
        taoGiaoDien();
        ganSuKien();
    }

    // ================= DỮ LIỆU =================
    private void taoDuLieu() {
        dsChucNang = Arrays.asList(
            "Trang Chủ",
            "Quản Lý Đặt Phòng",
            "Danh Sách Phiếu Đặt Phòng",
            "Tạo Phiếu Đặt Phòng",
            "Quản Lý Phòng",
            "Khuyến Mãi",
            "Chi Phí Phát Sinh",
            "Hóa Đơn",
            "Thống Kê",
            "Khách Hàng",
            "Nhân Viên",
            "Tài Khoản"
        );

        noiDungHuongDan = new HashMap<>();

        noiDungHuongDan.put("Trang Chủ",
            "MỤC ĐÍCH:\n"
          + "- Hiển thị tổng quan hệ thống\n\n"
          + "CHỨC NĂNG:\n"
          + "- Truy cập nhanh các chức năng\n"
          + "- Theo dõi tình trạng hoạt động");

        noiDungHuongDan.put("Quản Lý Đặt Phòng",
            "MỤC ĐÍCH:\n"
          + "- Quản lý hoạt động đặt phòng\n\n"
          + "BAO GỒM:\n"
          + "- Tạo phiếu đặt phòng\n"
          + "- Xem danh sách phiếu đặt");

        noiDungHuongDan.put("Danh Sách Phiếu Đặt Phòng",
                "MỤC LỤC:\n"
              + "1. Mục đích màn hình\n"
              + "2. Cách xem danh sách phiếu đặt phòng\n"
              + "3. Ý nghĩa ngày nhận – ngày trả\n"
              + "4. Ý nghĩa các trạng thái phiếu\n"
              + "5. Lọc theo trạng thái\n"
              + "6. Tìm kiếm theo SĐT khách hàng\n"
              + "7. Tìm kiếm theo mã phiếu\n"
              + "8. Xem chi tiết và chỉnh sửa phiếu\n"
              + "9. Làm mới dữ liệu\n\n"

              + "1. MỤC ĐÍCH MÀN HÌNH:\n"
              + "- Màn hình dùng để quản lý toàn bộ phiếu đặt phòng trong hệ thống.\n"
              + "- Nhân viên có thể theo dõi tình trạng lưu trú của khách theo thời gian thực.\n"
              + "- Hỗ trợ tìm kiếm nhanh phiếu theo khách hàng, mã phiếu hoặc trạng thái.\n\n"

              + "2. CÁCH XEM DANH SÁCH PHIẾU ĐẶT PHÒNG:\n"
              + "- Khi mở màn hình, hệ thống tự động tải tất cả phiếu đặt phòng từ cơ sở dữ liệu.\n"
              + "- Mỗi phiếu được hiển thị dưới dạng thẻ (card).\n"
              + "- Thông tin hiển thị gồm: mã phiếu, tên khách hàng, số điện thoại và trạng thái.\n\n"

              + "3. Ý NGHĨA NGÀY NHẬN – NGÀY TRẢ:\n"
              + "- Ngày nhận phòng: là ngày khách bắt đầu lưu trú tại khách sạn.\n"
              + "- Ngày trả phòng: là ngày khách kết thúc lưu trú.\n"
              + "- Hệ thống so sánh các ngày này với ngày hiện tại để xác định trạng thái phiếu.\n"
              + "- Nếu một phiếu có nhiều phòng, hệ thống sẽ lấy ngày nhận sớm nhất và ngày trả muộn nhất.\n\n"

              + "4. Ý NGHĨA CÁC TRẠNG THÁI PHIẾU:\n"
              + "- Đã đặt: Phiếu đã được tạo nhưng chưa tới ngày nhận phòng.\n"
              + "- Tới ngày nhận: Ngày hiện tại trùng với ngày nhận phòng.\n"
              + "- Chưa nhận phòng: Đã quá ngày nhận nhưng khách chưa check-in.\n"
              + "- Đang ở: Khách đã nhận phòng và đang lưu trú.\n"
              + "- Tới ngày trả: Hôm nay là ngày trả phòng.\n"
              + "- Trễ hạn trả phòng: Đã quá ngày trả nhưng khách chưa trả phòng.\n"
              + "- Đã hủy: Phiếu đã bị hủy và không còn hiệu lực.\n"
              + "- Hoàn thành: Phiếu đã kết thúc và khách đã trả phòng.\n\n"

              + "5. LỌC THEO TRẠNG THÁI:\n"
              + "- Chọn trạng thái cần xem trong danh sách lọc trạng thái.\n"
              + "- Nhấn nút tìm để hệ thống hiển thị các phiếu phù hợp.\n"
              + "- Chỉ những phiếu có trạng thái đúng với lựa chọn mới được hiển thị.\n\n"

              + "6. TÌM KIẾM THEO SỐ ĐIỆN THOẠI KHÁCH HÀNG:\n"
              + "- Nhập số điện thoại của khách hàng (gồm đúng 10 chữ số).\n"
              + "- Nhấn biểu tượng kính lúp để thực hiện tìm kiếm.\n"
              + "- Hệ thống sẽ hiển thị tất cả phiếu thuộc khách hàng đó.\n\n"

              + "7. TÌM KIẾM THEO MÃ PHIẾU:\n"
              + "- Nhập mã phiếu đặt phòng vào ô tìm kiếm.\n"
              + "- Có thể nhập một phần mã, hệ thống sẽ tìm các mã tương ứng.\n"
              + "- Nhấn biểu tượng kính lúp để xem kết quả.\n\n"

              + "8. XEM CHI TIẾT VÀ CHỈNH SỬA PHIẾU:\n"
              + "- Nhấp chuột vào thẻ phiếu trong danh sách.\n"
              + "- Màn hình chi tiết phiếu sẽ được mở ra.\n"
              + "- Tại đây, nhân viên có thể xem thông tin phòng, ngày ở và trạng thái phiếu.\n\n"

              + "9. LÀM MỚI DỮ LIỆU:\n"
              + "- Nhấn nút làm mới để tải lại toàn bộ danh sách phiếu.\n"
              + "- Trạng thái lọc sẽ được đưa về mặc định ban đầu.\n"
        );

        noiDungHuongDan.put("Tạo Phiếu Đặt Phòng",
                "MỤC LỤC:\n"
              + "1. Mục đích chức năng\n"
              + "2. Nhập thời gian lưu trú\n"
              + "3. Nhập số lượng người\n"
              + "4. Gợi ý và chọn phòng\n"
              + "5. Nhập thông tin khách hàng\n"
              + "6. Xác nhận và tạo phiếu\n"
              + "7. Các lưu ý và lỗi thường gặp\n\n"

              + "1. MỤC ĐÍCH CHỨC NĂNG:\n"
              + "- Chức năng dùng để tạo phiếu đặt phòng cho khách hàng.\n"
              + "- Nhân viên có thể đặt phòng theo thời gian lưu trú và số lượng người.\n"
              + "- Hệ thống hỗ trợ gợi ý phòng phù hợp và kiểm tra tính hợp lệ dữ liệu.\n\n"

              + "2. NHẬP THỜI GIAN LƯU TRÚ:\n"
              + "- Nhân viên nhập ngày nhận phòng và ngày trả phòng theo yêu cầu khách.\n"
              + "- Ngày nhận phải nhỏ hơn ngày trả.\n"
              + "- Nếu ngày nhận lớn hơn hoặc bằng ngày trả, hệ thống sẽ hiển thị thông báo lỗi.\n\n"

              + "3. NHẬP SỐ LƯỢNG NGƯỜI:\n"
              + "- Nhân viên nhập số lượng người dự kiến lưu trú.\n"
              + "- Thông tin này được dùng để gợi ý phòng có sức chứa phù hợp.\n\n"

              + "4. GỢI Ý VÀ CHỌN PHÒNG:\n"
              + "- Nhấn nút Gợi ý phòng để hệ thống tìm phòng trống theo thời gian và số lượng người.\n"
              + "- Nếu có phòng phù hợp, hệ thống hiển thị danh sách phòng gợi ý.\n"
              + "- Nhân viên có thể chọn phòng theo gợi ý hoặc bấm Cập nhật phòng để tự chọn.\n"
              + "- Nếu không còn phòng trống, hệ thống sẽ hiển thị thông báo kết thúc thao tác.\n\n"

              + "5. NHẬP THÔNG TIN KHÁCH HÀNG:\n"
              + "- Sau khi chọn phòng, nhấn Tạo phiếu để tiếp tục.\n"
              + "- Hệ thống tạo mã phiếu đặt phòng và mã khách hàng.\n"
              + "- Nhân viên nhập số điện thoại khách hàng.\n"
              + "- Nếu số điện thoại đã tồn tại, hệ thống tự động hiển thị thông tin khách.\n"
              + "- Nếu chưa tồn tại, nhân viên nhập đầy đủ thông tin khách hàng mới.\n\n"

              + "6. XÁC NHẬN VÀ TẠO PHIẾU:\n"
              + "- Nhân viên bấm Xác nhận đặt phòng.\n"
              + "- Hệ thống hiển thị hộp thoại xác nhận.\n"
              + "- Chọn Yes để hoàn tất tạo phiếu.\n"
              + "- Hệ thống lưu phiếu đặt phòng, thông tin khách hàng và phòng thuê.\n"
              + "- Sau khi lưu thành công, hệ thống hiển thị bill phiếu đặt phòng.\n\n"

              + "7. CÁC LƯU Ý VÀ LỖI THƯỜNG GẶP:\n"
              + "- Ngày nhận phải nhỏ hơn ngày trả.\n"
              + "- Phòng được chọn phải đủ sức chứa cho số lượng người.\n"
              + "- Không thể tạo phiếu nếu không còn phòng trống trong thời gian đã chọn.\n"
              + "- Cần nhập đúng định dạng số điện thoại khách hàng.\n"
        );


        noiDungHuongDan.put("Quản Lý Phòng",
            "CHỨC NĂNG:\n"
          + "- Thêm, sửa, cập nhật phòng\n"
          + "- Xem tình trạng phòng");

        noiDungHuongDan.put("Khuyến Mãi",
            "CHỨC NĂNG:\n"
          + "- Quản lý chương trình khuyến mãi\n"
          + "- Áp dụng cho hóa đơn");

        noiDungHuongDan.put("Chi Phí Phát Sinh",
            "CHỨC NĂNG:\n"
          + "- Ghi nhận chi phí phát sinh\n"
          + "- Cộng vào hóa đơn");

        noiDungHuongDan.put("Hóa Đơn",
            "CHỨC NĂNG:\n"
          + "- Thanh toán\n"
          + "- In hóa đơn");

        noiDungHuongDan.put("Thống Kê",
            "CHỨC NĂNG:\n"
          + "- Thống kê doanh thu\n"
          + "- Báo cáo sử dụng phòng");

        noiDungHuongDan.put("Khách Hàng",
            "CHỨC NĂNG:\n"
          + "- Quản lý thông tin khách hàng");

        noiDungHuongDan.put("Nhân Viên",
            "CHỨC NĂNG:\n"
          + "- Quản lý nhân viên\n"
          + "- Phân quyền");

        noiDungHuongDan.put("Tài Khoản",
            "CHỨC NĂNG:\n"
          + "- Quản lý tài khoản\n"
          + "- Đổi mật khẩu");
    }

    // ================= GIAO DIỆN =================
    private void taoGiaoDien() {

        // ----- PANEL TRÊN (10%) -----
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlTop.setPreferredSize(new Dimension(0, 80)); // ~10%

        JLabel lblTitle = new JLabel("HƯỚNG DẪN SỬ DỤNG");
        lblTitle.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));

        cbbChucNang = new JComboBox<>(dsChucNang.toArray(new String[0]));
        cbbChucNang.setPreferredSize(new Dimension(280, 35));

        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlRight.setBackground(Color.WHITE);
        pnlRight.add(new JLabel("Chức năng: "));
        pnlRight.add(cbbChucNang);

        pnlTop.add(lblTitle, BorderLayout.WEST);
        pnlTop.add(pnlRight, BorderLayout.EAST);

        add(pnlTop, BorderLayout.NORTH);

        // ----- NỘI DUNG (90%) -----
        txtHuongDan = new JTextArea();
        txtHuongDan.setEditable(false);
        txtHuongDan.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtHuongDan.setLineWrap(true);
        txtHuongDan.setWrapStyleWord(true);
        txtHuongDan.setMargin(new Insets(15, 20, 15, 20));

        JScrollPane scroll = new JScrollPane(txtHuongDan);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        // Mặc định
        cbbChucNang.setSelectedIndex(0);
        txtHuongDan.setText(noiDungHuongDan.get("Trang Chủ"));
    }

    // ================= SỰ KIỆN =================
    private void ganSuKien() {
        cbbChucNang.addActionListener(e -> {
            String key = (String) cbbChucNang.getSelectedItem();
            txtHuongDan.setText(
                noiDungHuongDan.getOrDefault(
                    key,
                    "Chưa có hướng dẫn cho chức năng này."
                )
            );
        });
    }
}
