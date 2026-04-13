# 📊 BÁO CÁO PHÁT TRIỂN ỨNG DỤNG FINMATE

---

## 👤 1. Thông tin sinh viên

- Dự án: Ứng dụng quản lý tài chính Finmate
- Nhóm: Finmate Team
- Nhánh phát triển: feature/hai-newfeature

---

## 📌 2. Tổng quan hệ thống

### 2.1 Giới thiệu
Finmate là ứng dụng quản lý tài chính cá nhân được xây dựng trên nền tảng Android, nhằm hỗ trợ người dùng theo dõi thu nhập, chi tiêu và đưa ra các phân tích tài chính hiệu quả.

---

### 2.2 Mục tiêu hệ thống
- Quản lý tài chính cá nhân một cách dễ dàng
- Theo dõi dòng tiền theo thời gian thực
- Phân tích chi tiêu thông qua biểu đồ
- Hỗ trợ người dùng đưa ra quyết định tài chính

---

## 🧩 3. Phân tích yêu cầu

### 3.1 Đối tượng sử dụng
- Sinh viên
- Nhân viên văn phòng
- Người dùng cá nhân

---

### 3.2 Yêu cầu chức năng

#### 🔹 Người dùng
- Đăng ký tài khoản
- Đăng nhập hệ thống
- Đăng xuất

#### 🔹 Quản lý tài chính
- Thêm thu nhập
- Thêm chi tiêu
- Sửa / xóa giao dịch
- Xem danh sách giao dịch

#### 🔹 Thống kê
- Tính tổng thu nhập
- Tính tổng chi tiêu
- Hiển thị biểu đồ

---

### 3.3 Yêu cầu phi chức năng
- Giao diện thân thiện
- Dữ liệu bảo mật
- Hiệu năng ổn định
- Đồng bộ dữ liệu nhanh

---

## 🏗️ 4. Thiết kế hệ thống

### 4.1 Kiến trúc
Ứng dụng được xây dựng theo mô hình:
- UI Layer (Giao diện)
- Business Logic Layer (Xử lý)
- Data Layer (Firebase)

---

### 4.2 Công nghệ sử dụng
- Ngôn ngữ: Java / Kotlin
- IDE: Android Studio
- Firebase Authentication
- Firebase Realtime Database
- MPAndroidChart (hiển thị biểu đồ)

---

## 🗄️ 5. Thiết kế cơ sở dữ liệu

### 5.1 Các bảng chính

#### 📌 USERS
- userId
- email
- password

#### 📌 TRANSACTIONS
- transactionId
- amount
- type (income/expense)
- date
- userId

---

### 5.2 Mối quan hệ
- 1 User có nhiều Transactions

---

## 📊 6. Chức năng biểu đồ

- Hiển thị chi tiêu theo danh mục
- So sánh thu nhập và chi tiêu
- Trực quan hóa dữ liệu tài chính

---

## ⚙️ 7. Phần việc đã thực hiện

- Tạo tài liệu phát triển
- Nghiên cứu biểu đồ tài chính
- Phân tích yêu cầu hệ thống
- Chuẩn bị tích hợp Firebase

---

## 🚀 8. Hướng phát triển

- Hoàn thiện chức năng biểu đồ
- Tối ưu hiệu năng
- Cải thiện UI/UX
- Thêm đăng nhập Google

---

## 🐞 9. Khó khăn gặp phải

- Xử lý dữ liệu lớn
- Đồng bộ Firebase
- Thiết kế UI phù hợp

---

## 💡 10. Giải pháp

- Sử dụng Firebase Realtime Database
- Tối ưu truy vấn dữ liệu
- Áp dụng UI hiện đại

---

## 📌 11. Đánh giá

### Ưu điểm
- Dễ sử dụng
- Giao diện thân thiện
- Tính năng thiết thực

### Nhược điểm
- Chưa tối ưu hiệu năng
- Thiếu một số tính năng nâng cao

---

## 📅 12. Kết luận

Ứng dụng Finmate là một giải pháp hữu ích cho việc quản lý tài chính cá nhân. Trong tương lai, hệ thống có thể mở rộng thêm nhiều tính năng để đáp ứng nhu cầu người dùng.

---

## 🔥 13. Trạng thái
👉 Đang phát triển (In Progress)