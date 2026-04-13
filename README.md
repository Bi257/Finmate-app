# Finmate-app



Ứng Dụng quản lý chi tiêu và thuế thu nhập cá nhân



Đây là đồ án môn học: Lập trình thiết bị di động tại Trường Đại học Sư phạm (UED).

Ứng dụng được xây dựng nhằm mục đích: quản lý chi tiêu cá nhân và tính toán thuế thu nhập cá nhân





\## Mục lục

I. \[Giới thiệu]

II. Tính năng chính:

Ứng dụng được thiết kế tập trung vào trải nghiệm người dùng và tính bảo mật, bao gồm các chức năng cốt lõi sau:



1\. Hệ thống xác thực \& Bảo mật (Authentication)


Đăng ký/Đăng nhập: Hỗ trợ tạo tài khoản qua Email và mật khẩu.



Xác thực dữ liệu: Kiểm tra định dạng Email, độ mạnh mật khẩu và so khớp mật khẩu ngay tại giao diện.



Firebase Auth: Duy trì trạng thái đăng nhập giúp người dùng không phải thao tác lại nhiều lần.



Xử lý lỗi: Hiển thị thông báo trực quan (Email đã tồn tại, sai mật khẩu,...) qua Snackbar/Toast.



2\. Quản lý tài chính \& Thuế TNCN:


Theo dõi chi tiêu: Ghi chép các khoản thu nhập và chi tiêu hàng ngày.



Tính toán thuế TNCN: Tự động tính toán mức thuế dựa trên thu nhập, các khoản giảm trừ gia cảnh và bảo hiểm (theo biểu thuế lũy tiến).



Quản lý danh mục: Phân loại chi tiêu (Ăn uống, Di chuyển, Giải trí,...) để dễ dàng theo dõi.



Tìm kiếm \& Bộ lọc: Truy xuất lịch sử giao dịch nhanh chóng theo thời gian hoặc danh mục.



3\. Giao diện \& Trải nghiệm (UI/UX):


Material Design 3: Giao diện hiện đại, tối giản với thư viện Jetpack Compose.



Responsive Design: Tương thích hoàn hảo trên nhiều kích thước màn hình (Phone, Tablet).



Hiệu ứng mượt mà: Chuyển cảnh và tương tác người dùng được tối ưu hóa.

3\. \[Công nghệ sử dụng]

Ứng dụng kết hợp sức mạnh của các thư viện và công cụ hiện đại nhất trong hệ sinh thái Android:



Ngôn ngữ chính:  (Sử dụng Coroutines \& Flow để xử lý bất đồng bộ).



UI Framework:  (Khai báo giao diện hiện đại).



Back-end \& Database: \* : Xác thực người dùng.



: Lưu trữ dữ liệu thu chi thời gian thực.



: Lưu trữ dữ liệu offline (nếu có).



Dependency Injection:  (Giảm sự phụ thuộc và dễ dàng testing).



Kiến trúc: Clean Architecture chia làm 3 layer (Data - Domain - UI).



Navigation: Jetpack Compose Navigation (Điều hướng màn hình).



Logging \& Debugging: Timber \& Git.

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

