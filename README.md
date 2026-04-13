# Finmate-app



&#x20;Ứng Dụng quản lý chi tiêu và thuế thu nhập cá nhân



Đây là đồ án môn học: Lập trình thiết bị di động tại Trường Đại học Sư phạm (UED).

Ứng dụng được xây dựng nhằm mục đích: quản lý chi tiêu cá nhân và tính toán thuế thu nhập cá nhân





\## 📋 Mục lục

1\. \[Giới thiệu](#giới-thiệu)

2\. \[Tính năng chính](#tính-năng-chính)

3\. \[Công nghệ sử dụng](#công-nghệ-sử-dụng)

4\. \[Kiến trúc dự án](#kiến-trúc-dự-án)

5\. \[Hướng dẫn cài đặt](#hướng-dẫn-cài-đặt)

6\. \[Thông tin tác giả](#thông-tin-tác-giả)



\---



\## 🌟 Giới thiệu

Dự án tập trung vào việc xây dựng một trải nghiệm người dùng mượt mà trên nền tảng Android, sử dụng các tiêu chuẩn mới nhất của Google như \*\*Jetpack Compose\*\* và mô hình \*\*Clean Architecture\*\*.



\## ✨ Tính năng chính

\- ✅ \*\*Hệ thống tài khoản:\*\* Đăng ký, Đăng nhập thông qua Firebase Authentication.

\- ✅ \*\*Xác thực dữ liệu:\*\* Kiểm tra lỗi email trùng, mật khẩu không khớp ngay tại giao diện.

\- ✅ \*\*Giao diện hiện đại:\*\* Sử dụng Material Design 3, hỗ trợ Dark Mode (nếu có).

\- \[ ] \*\*Tính năng khác:\*\* \[Ví dụ: Thanh toán online, Đặt chỗ, Thông báo Push...]



\## 🛠 Công nghệ sử dụng

\- \*\*Ngôn ngữ:\*\* \[Kotlin](https://kotlinlang.org/)

\- \*\*UI Framework:\*\* \[Jetpack Compose](https://developer.android.com/jetpack/compose)

\- \*\*Kiến trúc:\*\* Clean Architecture (Domain, Data, UI Layers)

\- \*\*Xử lý bất đồng bộ:\*\* Coroutines \& Flow

\- \*\*Dependency Injection:\*\* Hilt / Dagger

\- \*\*Networking:\*\* Retrofit / Ktor

\- \*\*Back-end:\*\* Firebase (Auth, Firestore, Storage)



\## 🏗 Kiến trúc dự án

Dự án được phân chia rõ ràng để dễ dàng bảo trì và kiểm thử:

\- \*\*`data`\*\*: Chứa implementation của Repositories, API interfaces (Retrofit) và Database.

\- \*\*`domain`\*\*: Chứa Business Logic nguyên bản (Use Cases) và Interfaces.

\- \*\*`ui`\*\*: Chứa toàn bộ giao diện (Screens), ViewModels và các thành phần Compose.

\- \*\*`di`\*\*: Nơi cấu hình Dependency Injection giúp kết nối các layer.



\## 📸 Ảnh chụp màn hình

| Màn hình Đăng ký | Màn hình Đăng nhập | Màn hình Chính |

|:---:|:---:|:---:|

| !\[Signup](docs/images/signup.png) | !\[Login](docs/images/login.png) | !\[Home](docs/images/home.png) |

\*(Lưu ý: Bạn hãy tạo thư mục `docs/images` và bỏ ảnh vào để hiển thị được ở đây)\*



\## ⚙️ Hướng dẫn cài đặt

1\. \*\*Clone project:\*\*

&#x20;  ```bash

&#x20;  git clone \[Link GitHub của bạn]

