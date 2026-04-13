

## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
          │                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
│                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
│
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
          │                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
│                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
│
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
          │                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
│                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
│
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
          │                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
│                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
│
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
          │                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
│                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
│
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
          │                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
│                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
│
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
          │                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
│                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
│
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
          │                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```


## 📋 Mục lục

1. [Giới thiệu dự án](#-giới-thiệu-dự-án)
2. [Tính năng chính](#-tính-năng-chính)
3. [Phân công thành viên](#-phân-công-thành-viên)
4. [Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [Cấu trúc Database](#-cấu-trúc-database)
6. [API & Luồng dữ liệu](#-api--luồng-dữ-liệu)
7. [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
8. [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
9. [Hướng dẫn cài đặt](#-hướng-dẫn-cài-đặt)
10. [Câu hỏi phản biện](#-câu-hỏi-phản-biện)

---

## 🌟 Giới thiệu dự án

Trong thời đại thanh toán không tiền mặt, người dùng nhận hàng chục tin nhắn biến động số dư mỗi ngày nhưng lại không biết mình đang tiêu bao nhiêu, kiếm được bao nhiêu, và quan trọng hơn — **phải đóng bao nhiêu thuế**.

**FinMate** ra đời để giải quyết đúng 3 nỗi đau đó:

| Vấn đề                             | Giải pháp FinMate                                                |
|------------------------------------|------------------------------------------------------------------|
| Ghi chép thủ công mất thời gian    | Tự động đọc SMS ngân hàng, không cần nhập tay                    |
| Không biết số thuế TNCN phải đóng  | Engine tính thuế lũy tiến 7 bậc theo TT 111/2013                 |
| Báo cáo tài chính lộn xộn          | Xuất Excel 3 sheet + PDF tờ khai 02/QTT-TNCN chuẩn Tổng cục Thuế |

---

## ✨ Tính năng chính

### 1. 🔐 Xác thực người dùng
- Đăng ký / Đăng nhập bằng Email + Mật khẩu
- Mật khẩu được hash **SHA-256** trước khi lưu — không lưu plain text
- Session persist qua **DataStore** — không cần đăng nhập lại sau khi tắt app
- Lưu hồ sơ thuế: Mã số thuế cá nhân, số người phụ thuộc

### 2. 📊 Dashboard tổng quan
- Hiển thị tổng thu nhập tháng hiện tại
- Thuế TNCN ước tính cập nhật theo thời gian thực
- Biểu đồ cột thu nhập **6 tháng gần nhất** tính từ dữ liệu Room thực
- Danh sách giao dịch gần đây nhóm theo ngày

### 3. 📩 Đọc SMS ngân hàng tự động
- Đăng ký `BroadcastReceiver` nhận SMS từ các ngân hàng: **VCB, TCB, BIDV, ACB, MB, MBBank**
- Regex tự động bóc tách: số tiền, loại giao dịch (ghi có/ghi nợ), nội dung chuyển khoản
- Hiển thị SMS alert trên Dashboard — người dùng xác nhận hoặc bỏ qua
- Tự động phân loại: lương, cho thuê, chi tiêu

### 4. 💰 Quản lý thu nhập đa nguồn
- **5 nguồn thu nhập:** Lương/tiền công · Cho thuê tài sản · Cổ tức/đầu tư · Freelance · Khác
- Preview thuế ước tính cập nhật **realtime** ngay khi nhập số tiền
- Lọc giao dịch theo nguồn, nhóm theo ngày
- Tìm kiếm và sắp xếp giao dịch

### 5. 🧮 Engine tính thuế TNCN
- Áp dụng đúng **Thông tư 111/2013/TT-BTC** — biểu lũy tiến 7 bậc (5% → 35%)
- Tính đầy đủ: BHXH + BHYT + BHTN + giảm trừ bản thân (11tr) + giảm trừ người phụ thuộc (4.4tr/người)
- Hiển thị chi tiết từng bậc: phần thu nhập rơi vào và số thuế phát sinh tại bậc đó
- Tính thuế vãng lai (freelance): khấu trừ 10% tại nguồn

### 6. 💱 Tỷ giá & Giá vàng thời gian thực
- Gọi **exchangerate-api.com** lấy tỷ giá USD, EUR, JPY, GBP
- Giá vàng SJC tính từ XAU/USD quốc tế
- Cache **30 phút** với Redis-like in-memory để tránh spam API
- Quy đổi nhanh ngay trên màn hình

### 7. 📄 Xuất báo cáo
- **Excel 3 sheet** (Apache POI):
    - Sheet 1: Thu nhập chi tiết theo ngày
    - Sheet 2: Tính thuế lũy tiến từng bậc
    - Sheet 3: Tổng hợp 6 tháng gần nhất
- **PDF tờ khai** (iText7): Mẫu 02/QTT-TNCN theo TT 80/2021/TT-BTC
- Chia sẻ file qua **FileProvider** — mở ngay sau khi xuất

### 8. 🔔 Nhắc nhở hạn nộp thuế
- Tự động tính deadline theo tháng hiện tại
- Cảnh báo: khai thuế hàng tháng, quyết toán quý, nộp thuế tạm tính
- Badge "KHẨN" cho deadline gần nhất

---

## 👥 Phân công thành viên

### 1. Lưu Viễn Dương — *Core Database & Architecture*

> *"Người xây nền móng — không có nền thì không có app"*

**Phụ trách:**
- Thiết kế và triển khai **Room Database** với 3 entity: `TransactionEntity`, `UserEntity`, `ExchangeRateEntity`
- Cấu hình **Hilt Dependency Injection** toàn app — AppModule, RepositoryModule
- Implement **Clean Architecture** 3 tầng: Data → Domain → Presentation
- Viết toàn bộ **DAO layer**: TransactionDao, UserDao, ExchangeRateDao
- Kết nối **API tỷ giá** (exchangerate-api.com) qua Retrofit + OkHttp
- Implement `ExchangeRateRepository` với cache 30 phút
- Xử lý **offline-first**: đồng bộ dữ liệu khi có mạng, đọc từ Room khi offline

**Files chính:**
```
data/local/AppDatabase.kt
data/local/entity/TransactionEntity.kt
data/local/entity/UserEntity.kt
data/local/TransactionDao.kt
data/local/UserDao.kt
di/AppModule.kt
api/ExchangeApi.kt
api/RetrofitInstance.kt
data/repository/ExchangeRateRepository.kt
```

---

### 2. Huỳnh Thanh Ngân — *Automation & Reporting*

> *"Thám tử SMS và người vận chuyển dữ liệu"*

**Phụ trách:**
- Implement **BroadcastReceiver** đọc SMS ngân hàng (`SmsReceiver.kt`)
- Viết **Regex engine** bóc tách: số tiền, loại giao dịch, nội dung, số dư từ SMS của VCB, TCB, BIDV, ACB, MB
- Implement **NotificationListenerService** đọc push notification ngân hàng (iOS-style)
- Xây dựng `SmsRepository` — flow dữ liệu từ SMS vào ViewModel
- Xuất file **Excel 3 sheet** với Apache POI — formatting, style, header, tổng kết
- Xuất file **PDF tờ khai** 02/QTT-TNCN với iText7 — bảng lũy tiến, chữ ký, logo
- Implement **FileProvider** để chia sẻ file sau khi xuất

**Files chính:**
```
service/SmsReceiver.kt
service/SmsRepository.kt
service/NotificationListener.kt
export/ExcelExporter.kt
export/PdfExporter.kt
util/FileExportHelper.kt
```

---

### 3. Nguyễn Hữu Quốc Hải — *Cloud Integration & Security*

> *"Giữ cho dữ liệu luôn an toàn trên mây"*

**Phụ trách:**
- Tích hợp **Firebase Authentication** — Google Sign-In, Email/Password
- Cấu hình **Firebase Firestore** — đồng bộ giao dịch lên cloud
- Implement `AuthRepository` — đăng ký, đăng nhập, đăng xuất, session DataStore
- Xử lý bảo mật: mật khẩu hash **SHA-256**, không lưu plain text
- Implement **offline/online sync**: Room là source of truth, Firestore là backup
- Cấu hình **Firebase Cloud Messaging** — push notification hạn nộp thuế
- Security rules Firestore: người dùng chỉ đọc được data của chính mình

**Files chính:**
```
data/repository/AuthRepository.kt
ui/screen/auth/AuthViewModel.kt
ui/screen/auth/LoginScreen.kt
di/AppModule.kt (Firebase providers)
```

---

### 4. Hồ Như Trâm — *UI/UX Design & Data Visualization*

> *"Biến những con số đau lòng thành biểu đồ màu sắc rực rỡ"*

**Phụ trách:**
- Thiết kế toàn bộ **UI/UX** với Jetpack Compose — theme tím hồng gradient xuyên suốt
- Xây dựng **5 màn hình chính**: Dashboard, Thu nhập, Thuế, Báo cáo, Thông báo
- Implement **biểu đồ** với Canvas API:
    - Biểu đồ đường thu chi (HomeScreen)
    - Biểu đồ cột 6 tháng (Dashboard)
    - Donut chart cơ cấu thu nhập (ReportScreen)
- Implement **animation**: số đếm lên, bar chart vẽ dần, tax bracket sáng từng dòng
- Xây dựng `AddTransactionScreen` với preview thuế realtime
- Implement **SMS Alert Card** với swipe-to-dismiss
- Responsive layout, dark mode support

**Files chính:**
```
ui/screen/home/HomeScreen.kt
ui/screen/home/AddTransactionScreen.kt
ui/screen/tax/TaxScreen.kt
ui/screen/report/ReportScreen.kt
ui/screen/notification/NotificationScreen.kt
ui/screen/wallet/WalletScreen.kt
ui/theme/Color.kt
ui/theme/Theme.kt
ui/components/
```

---

## 🏗 Kiến trúc hệ thống

FinMate áp dụng **Clean Architecture** kết hợp **MVVM** pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  HomeScreen  │  │   TaxScreen  │  │ ReportScreen │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                 │                  │           │
│  ┌──────▼───────┐  ┌──────▼───────┐  ┌──────▼───────┐  │
│  │ HomeViewModel│  │ TaxViewModel │  │ReportViewModel│  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
└─────────┼─────────────────┼──────────────────┼──────────┘
│                 │                  │
┌─────────▼─────────────────▼──────────────────▼──────────┐
│                    DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Use Cases     │  │          Models               │  │
│  │ GetTransactions │  │  Transaction, User, TaxResult │  │
│  │ AddTransaction  │  │  ExchangeRates, GoldPrice     │  │
│  │ CalculateTax    │  └──────────────────────────────┘  │
│  └─────────────────┘                                     │
└───────────────────────────────┬──────────────────────────┘
│
┌───────────────────────────────▼──────────────────────────┐
│                     DATA LAYER                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │     Room     │  │   Retrofit   │  │  DataStore   │   │
│  │  (Local DB)  │  │  (REST API)  │  │  (Session)   │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
│  ┌──────────────┐  ┌──────────────┐                      │
│  │  Firebase    │  │ SmsReceiver  │                      │
│  │  Firestore   │  │(BroadcastRcv)│                      │
│  └──────────────┘  └──────────────┘                      │
└──────────────────────────────────────────────────────────┘
```

### Luồng dữ liệu (Data Flow)

```
