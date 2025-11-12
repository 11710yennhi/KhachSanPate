USE QLKS
SET DATEFORMAT DMY
GO

INSERT INTO NhanVien (maNhanVien, hoTen, gioiTinh, ngaySinh, soDienThoai, email, chucVu, ngayTao, trangThai)
VALUES
('NV01122018001', N'Đặng Thị Hồng Hà', 0, '25/01/1994', '0977123456', 'ha@gmail.com', 0, '01/12/2018', 1),
('NV10022019001', N'Lê Quốc Bảo', 1, '15/03/1995', '0912345678', 'bao@gmail.com', 1, '10/02/2019', 1),
('NV15062020001', N'Nguyễn Thị Yến Nhi', 0, '21/07/1998', '0987654321', 'nhi@gmail.com', 1, '15/06/2020', 1),
('NV20032021001', N'Đoàn Thị Thảo Nguyên', 0, '05/12/2000', '0905123456', 'nguyen@gmail.com', 0, '20/03/2021', 1),
('NV12092022001', N'Trần Quốc Cường', 1, '01/10/1997', '0933123123', 'cuong@gmail.com', 0, '12/09/2022', 0);
GO

INSERT INTO TaiKhoan (maNhanVien, matKhau)
VALUES
('NV01122018001', '123456'),
('NV10022019001', '123456'),
('NV15062020001', '123456'),
('NV20032021001', '123456'),
('NV12092022001', '123456');
GO

INSERT INTO KhachHang (maKhachHang, hoTen, soDienThoai, nguoiVietNam)
VALUES
('KH15062019001', N'Phạm Thanh Tâm', '0909009009', 1),
('KH21072019001', N'Lý Hoàng Phúc', '0911222333', 1),
('KH12032020001', N'Trần Hồng Anh', '0988777666', 1),
('KH05092021001', N'John Smith', '0845123456', 0),
('KH22042022001', N'Võ Ngọc Trâm', '0933444555', 1),
('KH11062022001', N'Nguyễn Minh Hào', '0965123123', 1)
GO


INSERT INTO LoaiPhong (maLoaiPhong, tenLoaiPhong, sucChua, gia, moTa)
VALUES
('LP001', N'Phòng Tiêu Chuẩn (Standard)', 2, 700000, N'Phù hợp tối đa 2 khách. Bao gồm wifi, điều hòa, tivi, dọn phòng hằng ngày.'),
('LP002', N'Phòng Cao Cấp (Deluxe)', 3, 1000000, N'Dành cho tối đa 3 khách. Bao gồm wifi, điều hòa, tivi, dọn phòng hằng ngày.'),
('LP003', N'Phòng Gia Đình (Family)', 6, 1300000, N'Phù hợp gia đình tối đa 6 người. Bao gồm wifi, điều hòa, tivi, dọn phòng hằng ngày.')
GO

INSERT INTO Phong (maPhong, maLoaiPhong, trangThai)
VALUES
-- 15 phòng Standard
('P101', 'LP001', N'Bình thường'),
('P102', 'LP001', N'Bình thường'),
('P103', 'LP001', N'Bình thường'),
('P104', 'LP001', N'Bình thường'),
('P105', 'LP001', N'Bình thường'),
('P106', 'LP001', N'Bình thường'),
('P107', 'LP001', N'Bình thường'),
('P108', 'LP001', N'Bình thường'),
('P109', 'LP001', N'Bình thường'),
('P110', 'LP001', N'Bình thường'),
('P111', 'LP001', N'Bình thường'),
('P112', 'LP001', N'Bình thường'),
('P113', 'LP001', N'Bình thường'),
('P114', 'LP001', N'Bình thường'),
('P115', 'LP001', N'Bình thường'),
-- 10 phòng Deluxe
('P201', 'LP002', N'Bình thường'),
('P202', 'LP002', N'Bình thường'),
('P203', 'LP002', N'Bình thường'),
('P204', 'LP002', N'Bình thường'),
('P205', 'LP002', N'Bình thường'),
('P206', 'LP002', N'Bình thường'),
('P207', 'LP002', N'Bình thường'),
('P208', 'LP002', N'Bình thường'),
('P209', 'LP002', N'Bình thường'),
('P210', 'LP002', N'Bình thường'),
-- 5 phòng Family
('P301', 'LP003', N'Bình thường'),
('P302', 'LP003', N'Bình thường'),
('P303', 'LP003', N'Bình thường'),
('P304', 'LP003', N'Bình thường'),
('P305', 'LP003', N'Bình thường')
GO

INSERT INTO ChiPhiPhatSinh (maChiPhiPhatSinh, tenChiPhiPhatSinh, loaiChiPhiPhatSinh, gia)
VALUES
-- Dịch vụ
('DV10102025001', N'Giặt ủi', N'Dịch vụ', 50000),
('DV10102025002', N'Minibar', N'Dịch vụ', 100000),
('DV10102025003', N'Giường phụ', N'Dịch vụ', 200000),
-- Phạt
('DV10102025004', N'Mất chìa khóa phòng', N'Phạt', 200000),
('DV10102025005', N'Hư hỏng thiết bị', N'Phạt', 300000)

--BACKUP DATABASE QLKS
--TO DISK = 'F:\PhatTrienUngDung\NopCK\sql\QLKS.bak'
--WITH FORMAT,
--     MEDIANAME = 'QLKS',
--     NAME = 'Full Backup of QLKS'