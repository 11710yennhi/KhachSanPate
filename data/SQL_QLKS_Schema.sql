CREATE DATABASE QLKS;
GO
USE QLKS;
GO

CREATE TABLE NhanVien (
    maNhanVien      VARCHAR(13)  PRIMARY KEY,
    hoTen           NVARCHAR(50) NOT NULL,
    gioiTinh        BIT          DEFAULT 0,
    ngaySinh        DATE,
    soDienThoai     VARCHAR(10)  NOT NULL UNIQUE,
    email           VARCHAR(100),
    chucVu          BIT          DEFAULT 0,
    ngayTao         DATE         DEFAULT GETDATE(),
    trangThai       BIT          DEFAULT 1
)

CREATE TABLE TaiKhoan (
    maNhanVien  VARCHAR(13)  PRIMARY KEY,
    matKhau     VARCHAR(255) NOT NULL,
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
);

CREATE TABLE KhuyenMai (
    maKhuyenMai    VARCHAR(13)   PRIMARY KEY,
    tenKhuyenMai   NVARCHAR(100),
    ngayTao        DATE          DEFAULT GETDATE(),
    ngayBatDau     DATE          NOT NULL CHECK (ngayBatDau >= CAST(GETDATE() AS DATE)),
    ngayKetThuc    DATE          NOT NULL,
    loaiKhuyenMai  NVARCHAR(50)  NOT NULL,
    soTienApDung   FLOAT         NOT NULL,
    giaTriGiam     FLOAT         NOT NULL CHECK (giaTriGiam > 0),
    giamToiDa      FLOAT         CHECK (giamToiDa > 0),
    CONSTRAINT CK_KhuyenMai_NgayHopLe CHECK (ngayKetThuc >= ngayBatDau)
);

CREATE TABLE KhachHang (
    maKhachHang   VARCHAR(13)   PRIMARY KEY,
    hoTen         NVARCHAR(50)  NOT NULL,
    soDienThoai   VARCHAR(10)   UNIQUE,
    laNguoiVietNam BIT          DEFAULT 1
);


CREATE TABLE LoaiPhong (
    maLoaiPhong   VARCHAR(5)     PRIMARY KEY,
    tenLoaiPhong  NVARCHAR(100)  UNIQUE,
    sucChua       INT            CHECK (sucChua > 0),
    gia           FLOAT          CHECK (gia > 0),
    moTa          NVARCHAR(255)
);

CREATE TABLE Phong (
    maPhong      VARCHAR(4)   PRIMARY KEY,
    maLoaiPhong  VARCHAR(5)   NOT NULL,
    trangThai    NVARCHAR(50),
    FOREIGN KEY (maLoaiPhong) REFERENCES LoaiPhong(maLoaiPhong)
);

CREATE TABLE ChiPhiPhatSinh (
    maChiPhiPhatSinh   VARCHAR(15)   PRIMARY KEY,
    tenChiPhiPhatSinh  NVARCHAR(100) UNIQUE NOT NULL,
    loaiChiPhiPhatSinh NVARCHAR(100) NOT NULL,
    gia                FLOAT         CHECK (gia > 0)
);


CREATE TABLE PhieuDatPhong (
    maPhieuDatPhong  VARCHAR(14)  PRIMARY KEY,
    maKhachHang      VARCHAR(13)  NOT NULL,
    maNhanVien       VARCHAR(13)  NOT NULL,
    ngayTao          DATE         DEFAULT GETDATE(),
    trangThai        NVARCHAR(50),
    soTreEm          INT          CHECK (soTreEm >= 0),
    soNguoiLon       INT          CHECK (soNguoiLon > 0),
    FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang),
    FOREIGN KEY (maNhanVien)  REFERENCES NhanVien(maNhanVien)
);

CREATE TABLE ChiTietPhieuDatPhong (
    maPhieuDatPhong  VARCHAR(14)  NOT NULL,
    maPhong          VARCHAR(4)   NOT NULL,
    ngayNhanThuc     DATE,
    ngayTraThuc      DATE,
    ngayTra          DATE,
    PRIMARY KEY (maPhieuDatPhong, maPhong),
    FOREIGN KEY (maPhieuDatPhong) REFERENCES PhieuDatPhong(maPhieuDatPhong),
    FOREIGN KEY (maPhong)         REFERENCES Phong(maPhong)
);

CREATE TABLE ChiTietChiPhiPhatSinh (
    maPhieuDatPhong     VARCHAR(14) NOT NULL,
    maChiPhiPhatSinh    VARCHAR(15) NOT NULL,
    soLuong             INT         DEFAULT 1 CHECK (soLuong > 0),
    PRIMARY KEY (maPhieuDatPhong, maChiPhiPhatSinh),
    FOREIGN KEY (maChiPhiPhatSinh)    REFERENCES ChiPhiPhatSinh(maChiPhiPhatSinh),
    FOREIGN KEY (maPhieuDatPhong)     REFERENCES PhieuDatPhong(maPhieuDatPhong)
);

CREATE TABLE HoaDon (
    maHoaDon           VARCHAR(13)  PRIMARY KEY,
    maPhieuDatPhong    VARCHAR(14)  NOT NULL UNIQUE,
    maKhuyenMai        VARCHAR(13),
    phuongThucThanhToan NVARCHAR(15),
    tongTienPhong      FLOAT,
    tongTienCPPS       FLOAT,
    tongThanhToan      FLOAT,
    ngayTao            DATE         DEFAULT GETDATE(),
    FOREIGN KEY (maKhuyenMai)      REFERENCES KhuyenMai(maKhuyenMai),
    FOREIGN KEY (maPhieuDatPhong)  REFERENCES PhieuDatPhong(maPhieuDatPhong)
);
