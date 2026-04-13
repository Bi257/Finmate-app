# 📊 BÁO CÁO ĐỒ ÁN CHI TIẾT

## Ứng dụng Quản Lý Tài Chính Cá Nhân - FINMATE

---

# 1. GIỚI THIỆU CHUNG

## 1.1 Khái niệm quản lý tài chính cá nhân

Quản lý tài chính cá nhân là quá trình theo dõi, phân tích và kiểm soát các nguồn thu nhập và chi tiêu của một cá nhân nhằm tối ưu hóa việc sử dụng tiền bạc. Việc quản lý tài chính hiệu quả giúp cá nhân đạt được các mục tiêu tài chính như tiết kiệm, đầu tư và giảm thiểu rủi ro tài chính.

## 1.2 Vai trò của ứng dụng Finmate

Ứng dụng Finmate được xây dựng nhằm hỗ trợ người dùng:

* Theo dõi thu nhập và chi tiêu
* Phân tích thói quen tài chính
* Đưa ra quyết định tài chính hợp lý

## 1.3 Mục tiêu hệ thống

* Xây dựng hệ thống quản lý tài chính dễ sử dụng
* Cung cấp dữ liệu trực quan qua biểu đồ
* Đồng bộ dữ liệu realtime

---

# 2. TỔNG QUAN HỆ THỐNG

## 2.1 Mô tả hệ thống

Finmate là ứng dụng Android cho phép người dùng quản lý tài chính cá nhân thông qua các chức năng như nhập liệu giao dịch, xem thống kê và phân tích dữ liệu.

## 2.2 Phạm vi hệ thống

* Nền tảng: Android
* Người dùng: cá nhân

## 2.3 Đối tượng sử dụng

* Sinh viên
* Người đi làm
* Người quản lý chi tiêu cá nhân

---

# 3. PHÂN TÍCH YÊU CẦU

## 3.1 Yêu cầu chức năng

### 3.1.1 Quản lý tài khoản

* Đăng ký
* Đăng nhập
* Đăng xuất

### 3.1.2 Quản lý giao dịch

* Thêm giao dịch
* Sửa giao dịch
* Xóa giao dịch
* Xem danh sách giao dịch

### 3.1.3 Thống kê

* Tổng thu nhập
* Tổng chi tiêu
* Biểu đồ

## 3.2 Yêu cầu phi chức năng

* Bảo mật dữ liệu
* Hiệu năng ổn định
* Giao diện thân thiện

---

# 4. THIẾT KẾ HỆ THỐNG

## 4.1 Kiến trúc hệ thống

Hệ thống được thiết kế theo mô hình 3 tầng:

* Presentation Layer
* Business Logic Layer
* Data Layer

## 4.2 Sơ đồ kiến trúc

* Client: Android App
* Server: Firebase

---

# 5. THIẾT KẾ CƠ SỞ DỮ LIỆU

## 5.1 Thực thể USER

* userId
* email
* password

## 5.2 Thực thể TRANSACTION

* transactionId
* amount
* type
* date

## 5.3 Quan hệ

* 1 User - N Transaction

---

# 6. THIẾT KẾ CHỨC NĂNG

## 6.1 Use Case

* User đăng nhập
* User thêm giao dịch
* User xem báo cáo

## 6.2 Activity Diagram

Mô tả luồng hoạt động của người dùng khi sử dụng hệ thống.

---

# 7. CÔNG NGHỆ SỬ DỤNG

* Android Studio
* Java / Kotlin
* Firebase
* MPAndroidChart

---

# 8. TRIỂN KHAI HỆ THỐNG

## 8.1 Môi trường phát triển

* Windows
* Android Studio

## 8.2 Quy trình phát triển

* Phân tích
* Thiết kế
* Lập trình
* Kiểm thử

---

# 9. KIỂM THỬ

## 9.1 Kiểm thử chức năng

* Test đăng nhập
* Test thêm giao dịch

## 9.2 Kiểm thử hiệu năng

* Kiểm tra tốc độ tải dữ liệu

---

# 10. ĐÁNH GIÁ

## Ưu điểm

* Dễ sử dụng
* Giao diện đẹp

## Nhược điểm

* Chưa tối ưu hoàn toàn

---

# 11. KẾT LUẬN

Hệ thống Finmate đáp ứng tốt nhu cầu quản lý tài chính cá nhân.

---

# 12. HƯỚNG PHÁT TRIỂN

* AI phân tích tài chính
* Đồng bộ đa thiết bị

---

# 13. PHỤ LỤC

## 13.1 Thuật ngữ

* Firebase: nền tảng backend

## 13.2 Tài liệu tham khảo

* Google Firebase Docs
* Android Developer Docs

---

# 🔥 GHI CHÚ

Bạn có thể tiếp tục mở rộng thêm các phần:

* ERD chi tiết
* Sequence Diagram
* Code mẫu
* API mô tả chi tiết
