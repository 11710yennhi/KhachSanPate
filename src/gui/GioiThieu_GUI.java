package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class GioiThieu_GUI extends JPanel {
	public GioiThieu_GUI() {

		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// ===== HEADER =====
		JLabel lblTitle = new JLabel("PATE HOTEL", JLabel.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTitle.setForeground(new Color(30, 58, 138));
		lblTitle.setBorder(new EmptyBorder(20, 10, 10, 10));
		
		JLabel lblsubTitle = new JLabel("HỆ THỐNG QUẢN LÝ THÔNG TIN ĐẶT PHÒNG KHÁCH SẠN", JLabel.CENTER);
		lblsubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblsubTitle.setForeground(Color.DARK_GRAY);

		JLabel lblVersion = new JLabel("Version 1.0.0 – Cập nhật: 12/2025", JLabel.CENTER);
		lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblVersion.setForeground(Color.DARK_GRAY);

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Color.WHITE);
		header.add(lblTitle, BorderLayout.NORTH);
		header.add(lblsubTitle, BorderLayout.CENTER);
		header.add(lblVersion, BorderLayout.SOUTH);

		add(header, BorderLayout.NORTH);

		// ===== BODY =====
		JTextArea txtContent = new JTextArea();
		txtContent.setText("Pate Hotel - HỆ THỐNG QUẢN LÝ là phần mềm hỗ trợ quản lý "
				+ "toàn bộ hoạt động lưu trú của khách sạn một cách hiệu quả, " + "chính xác và dễ sử dụng.\n\n"
				+ "Chức năng chính:\n" + "• Quản lý đặt phòng & lưu trú\n" + "• Quản lý phòng, loại phòng\n"
				+ "• Quản lý khách hàng\n" + "• Quản lý nhân viên & tài khoản\n"
				+ "• Quản lý hóa đơn & chi phí phát sinh\n" + "• Thống kê\n\n"
				+ "Thông tin liên hệ:\n" + "• Email: hotelpate@gmail.com\n"
				+ "• Website: https://hotelpate.com\n");
		txtContent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtContent.setLineWrap(true);
		txtContent.setWrapStyleWord(true);
		txtContent.setEditable(false);
		txtContent.setOpaque(false);
		txtContent.setBorder(new EmptyBorder(20, 40, 20, 40));
		txtContent.setFocusable(false);
		txtContent.setCursor(null);  

		JPanel body = new JPanel();
		body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
		body.setBackground(Color.WHITE);

		// Nội dung chính
		body.add(txtContent);

		// Lời cảm ơn
		JLabel lblThanks = new JLabel(
		        "<html><div style='text-align: center;'>"
		      + "<i>Cảm ơn quý khách đã tin tưởng và sử dụng hệ thống Pate Hotel.</i>"
		      + "</div></html>",
		        SwingConstants.CENTER
		);
		lblThanks.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblThanks.setForeground(new Color(100, 100, 100));
		lblThanks.setBorder(new EmptyBorder(10, 40, 10, 40));
		lblThanks.setAlignmentX(Component.CENTER_ALIGNMENT);

		body.add(lblThanks);

		add(body, BorderLayout.CENTER);


		// ===== FOOTER (PHẦN BẠN YÊU CẦU) =====
		JLabel txtFooter = new JLabel(
		        "<html><div style='text-align: center;'>"
		      + "Công nghệ: Java Swing – SQL Server<br>"
		      + "© 2025 – Pate Hotel<br>"
		      + "Phần mềm phục vụ mục đích học tập và quản lý nội bộ"
		      + "</div></html>",
		        SwingConstants.CENTER
		);
		txtFooter.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		txtFooter.setForeground(Color.GRAY);
		txtFooter.setBorder(new EmptyBorder(10, 40, 20, 40));

		JPanel footer = new JPanel(new BorderLayout());
		footer.setBackground(Color.WHITE);
		footer.add(txtFooter, BorderLayout.CENTER);

		add(footer, BorderLayout.SOUTH);
	}

	// ===== TEST NHANH =====
	public static void main(String[] args) {
		JFrame frame = new JFrame("About");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 600);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(new GioiThieu_GUI());
		frame.setVisible(true);
	}
}
