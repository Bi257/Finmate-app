# Finmate-app

# 💜 FinMate — Trợ lý Tài chính & Quản lý Thuế TNCN

> **"Tự động hóa hoàn toàn. Không cần nhập tay. Thuế đúng luật."**
![Platform](https://img.shields.io/badge/Platform-Android-green?style=flat-square&logo=android)
![Language](https://img.shields.io/badge/Language-Kotlin-purple?style=flat-square&logo=kotlin)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean-blue?style=flat-square)
![Min SDK](https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-orange?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

---

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
SMS từ ngân hàng
       │
       ▼
SmsReceiver (BroadcastReceiver)
       │  Regex parse
       ▼
SmsRepository (StateFlow)
       │
       ▼
HomeViewModel ──────► HomeScreen (UI)
       │                    │
       │              SMS Alert Card
       │              [Ghi nhận] [Bỏ qua]
       ▼
AddTransactionUseCase
       │
       ▼
TransactionRepositoryImpl
       │
  ┌────┴────┐
  ▼         ▼
Room DB  Firebase
(local)  (cloud backup)
```

---

## 🗄 Cấu trúc Database

### Entity 1: `transactions`

```sql
CREATE TABLE transactions (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    title       TEXT    NOT NULL,
    amount      REAL    NOT NULL,
    type        TEXT    NOT NULL,  -- "income" | "expense"
    date        INTEGER NOT NULL,  -- Unix timestamp milliseconds
    note        TEXT    DEFAULT ''
);
```

**Indexes:**
```sql
CREATE INDEX idx_transactions_date ON transactions(date DESC);
CREATE INDEX idx_transactions_type ON transactions(type);
```

**Ví dụ dữ liệu:**
```
id=1, title="Lương tháng 4", amount=20000000, type="income", date=1712844000000, note="Lương · Công ty ABC"
id=2, title="Thuê nhà 45 Nguyễn Huệ", amount=5000000, type="income", date=1712844000000, note="Cho thuê"
id=3, title="VCB SMS", amount=500000, type="expense", date=1712847600000, note="ND: thăm đau"
```

---

### Entity 2: `users`

```sql
CREATE TABLE users (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    fullName      TEXT    NOT NULL,
    email         TEXT    NOT NULL UNIQUE,
    passwordHash  TEXT    NOT NULL,  -- SHA-256 hash
    taxCode       TEXT    DEFAULT '',
    dependents    INTEGER DEFAULT 0,
    createdAt     INTEGER NOT NULL   -- Unix timestamp
);
```

**Bảo mật:** Mật khẩu được hash SHA-256 trước khi lưu:
```kotlin
fun hashPassword(password: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}
```

---

### Entity 3: `exchange_rates`

```sql
CREATE TABLE exchange_rates (
    currency    TEXT    PRIMARY KEY,  -- "USD", "EUR", "JPY"
    rateToVnd   REAL    NOT NULL,
    updatedAt   INTEGER NOT NULL      -- Unix timestamp
);
```

---

### Database Version History

| Version | Thay đổi |
|---|---|
| 1 | `transactions` table |
| 2 | Thêm `exchange_rates` table |
| 3 | Thêm column `note` vào `transactions` |
| 4 | Thêm `users` table |

---

### DAOs

```kotlin
// TransactionDao
@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getByType(type: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'income'")
    suspend fun getTotalIncome(): Double
}

// UserDao
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :hash LIMIT 1")
    suspend fun login(email: String, hash: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?
}
```

---

## 🌐 API & Luồng dữ liệu

### API 1: Tỷ giá ngoại tệ

**Endpoint:** `https://api.exchangerate-api.com/v4/latest/{base}`

**Request:**
```
GET https://api.exchangerate-api.com/v4/latest/USD
```

**Response:**
```json
{
  "base": "USD",
  "date": "2026-04-11",
  "rates": {
    "VND": 25480.0,
    "EUR": 0.92,
    "JPY": 149.5,
    "GBP": 0.79,
    "XAU": 0.000425
  }
}
```

**Cách tính tỷ giá:**
```kotlin
val usdToVnd = rates["VND"] ?: 25_480.0
val eurToVnd = usdToVnd / (rates["EUR"] ?: 0.92)
val jpyToVnd = usdToVnd / (rates["JPY"] ?: 149.5)
```

**Caching Strategy:**
```
Request đến
    │
    ▼
Cache còn mới? (< 30 phút)
    │
   Yes ──────────────► Trả về cached data
    │
   No
    │
    ▼
Gọi API thật
    │
   OK ──────────────► Cập nhật cache → Trả về data
    │
  Fail ─────────────► Trả về cached data cũ (fallback)
                       hoặc giá mặc định nếu chưa có cache
```

---

### API 2: Giá vàng

Tính từ tỷ giá XAU/USD:
```kotlin
// 1 lượng vàng = 37.5g ≈ 1.2 troy oz
val goldUsdPerOz  = 1.0 / (rates["XAU"] ?: 0.000425)
val sjcBuyPrice   = goldUsdPerOz * usdToVnd / 37.5 * 1.15  // +15% thuế+phí
val sjcSellPrice  = goldUsdPerOz * usdToVnd / 37.5 * 1.18  // +18%
```

---

### Engine tính thuế TNCN

**Biểu lũy tiến 7 bậc (TT 111/2013):**

```kotlin
fun calculateProgressiveTax(taxableIncome: Double): Double = when {
    taxableIncome <= 0             -> 0.0
    taxableIncome <= 5_000_000     -> taxableIncome * 0.05
    taxableIncome <= 10_000_000    -> taxableIncome * 0.10 - 250_000
    taxableIncome <= 18_000_000    -> taxableIncome * 0.15 - 750_000
    taxableIncome <= 32_000_000    -> taxableIncome * 0.20 - 1_650_000
    taxableIncome <= 52_000_000    -> taxableIncome * 0.25 - 3_250_000
    taxableIncome <= 80_000_000    -> taxableIncome * 0.30 - 5_850_000
    else                           -> taxableIncome * 0.35 - 9_850_000
}
```

**Luồng tính thuế đầy đủ:**
```
Tổng thu nhập (grossIncome)
         │
         ▼
  - BHXH (8%) + BHYT (1.5%) + BHTN (1%) = 10.5%
         │
         ▼
  - Giảm trừ bản thân: 11,000,000 VNĐ/tháng
         │
         ▼
  - Giảm trừ người phụ thuộc: N × 4,400,000 VNĐ/tháng
         │
         ▼
  = Thu nhập tính thuế (taxableIncome)
         │
         ▼
  Áp dụng biểu lũy tiến 7 bậc
         │
         ▼
  = Thuế TNCN phải nộp (taxAmount)
```

---

### Luồng SMS → Giao dịch

```
SMS nhận được
     │
     ▼
SmsReceiver.onReceive()
     │
     ▼
parseBankSms(sender, body)
     │
     ├── Kiểm tra từ khóa ngân hàng
     │   ["số dư", "tk", "vcb", "bidv", "vnd"...]
     │
     ├── Regex bóc tách số tiền
     │   Pattern: ([+-]?\d{1,3}(?:[.,]\d{3})+)
     │
     ├── Xác định Thu/Chi
     │   Income: ["+", "nhận", "ghi có", "tiền vào"]
     │   Expense: ["ghi nợ", "thanh toán", "tiền ra"]
     │
     ├── Bóc tách nội dung
     │   Pattern: (?:ND|Noi dung)[:\s]*(.*)
     │
     └── Tạo Transaction object
          │
          ▼
     SmsRepository.insertNotification()
          │
          ▼
     HomeViewModel.onSmsReceived()
          │
          ▼
     Hiển thị SMS Alert Card trên Dashboard
          │
     [Ghi nhận]        [Bỏ qua]
          │
          ▼
     AddTransactionUseCase → Room DB
```

---

## 📁 Cấu trúc thư mục

```
app/src/main/java/com/example/baitapdidongcuoiki/
│
├── 📂 api/                          # API layer
│   ├── ExchangeApi.kt               # Retrofit interface
│   ├── ExchangeResponse.kt          # Response model
│   └── RetrofitInstance.kt          # Singleton Retrofit
│
├── 📂 data/
│   ├── 📂 local/                    # Room Database
│   │   ├── AppDatabase.kt           # Database config, version 4
│   │   ├── TransactionDao.kt        # CRUD giao dịch
│   │   ├── UserDao.kt               # Auth queries
│   │   ├── ExchangeRateDao.kt       # Cache tỷ giá
│   │   ├── ExchangeRateEntity.kt
│   │   └── 📂 entity/
│   │       ├── TransactionEntity.kt
│   │       └── UserEntity.kt
│   │
│   ├── 📂 mapper/
│   │   └── TransactionMapper.kt     # Entity ↔ Domain model
│   │
│   ├── 📂 remote/
│   │   ├── ApiService.kt
│   │   └── 📂 dto/
│   │       └── ExchangeRateDto.kt
│   │
│   └── 📂 repository/
│       ├── AuthRepository.kt        # Đăng nhập, session
│       ├── ExchangeRateRepository.kt # Tỷ giá + cache
│       ├── TransactionRepositoryImpl.kt
│       ├── RemoteRepository.kt
│       └── FakeTransactionRepository.kt (testing)
│
├── 📂 di/                           # Dependency Injection (Hilt)
│   ├── AppModule.kt                 # DB, DAO, UseCase providers
│   └── RepositoryModule.kt         # Repository providers
│
├── 📂 domain/
│   ├── 📂 model/                    # Pure Kotlin models
│   │   ├── Transaction.kt
│   │   └── Notification.kt
│   │
│   ├── 📂 repository/               # Interfaces
│   │   └── TransactionRepository.kt
│   │
│   └── 📂 usecase/                  # Business logic
│       ├── AddTransactionUseCase.kt
│       ├── GetTransactionsUseCase.kt
│       ├── CalculateTaxUseCase.kt   # Engine thuế 7 bậc
│       └── UseCases.kt              # Wrapper
│
├── 📂 export/
│   ├── ExcelExporter.kt             # Apache POI — 3 sheet
│   └── PdfExporter.kt               # iText7 — 02/QTT-TNCN
│
├── 📂 service/
│   ├── SmsReceiver.kt               # BroadcastReceiver
│   ├── SmsRepository.kt             # StateFlow SMS data
│   └── NotificationListener.kt     # NotificationListenerService
│
├── 📂 ui/
│   ├── 📂 components/               # Shared composables
│   │   ├── TransactionItem.kt
│   │   ├── SummaryCard.kt
│   │   ├── TaxCard.kt
│   │   ├── NotificationItem.kt
│   │   └── QuickActionButton.kt
│   │
│   ├── 📂 screen/
│   │   ├── 📂 auth/
│   │   │   ├── LoginScreen.kt       # Đăng nhập / Đăng ký
│   │   │   └── AuthViewModel.kt
│   │   │
│   │   ├── 📂 home/
│   │   │   ├── HomeScreen.kt        # Dashboard chính
│   │   │   ├── HomeViewModel.kt
│   │   │   └── AddTransactionScreen.kt
│   │   │
│   │   ├── 📂 wallet/
│   │   │   └── WalletScreen.kt      # Ví & lịch sử
│   │   │
│   │   ├── 📂 tax/
│   │   │   ├── TaxScreen.kt         # Tính thuế + tỷ giá
│   │   │   └── TaxViewModel.kt
│   │   │
│   │   ├── 📂 report/
│   │   │   ├── ReportScreen.kt      # Donut chart + xuất file
│   │   │   └── ReportViewModel.kt
│   │   │
│   │   └── 📂 notification/
│   │       ├── NotificationScreen.kt # Deadline thuế + SMS
│   │       └── NotificationViewModel.kt
│   │
│   └── 📂 theme/
│       ├── Color.kt                 # Tím hồng gradient palette
│       ├── Theme.kt
│       └── Type.kt
│
├── 📂 util/
│   ├── Resource.kt                  # Sealed class Loading/Success/Error
│   └── FileExportHelper.kt
│
├── MainActivity.kt                  # Nav host, auth guard, bottom nav
└── MyApp.kt                        # Hilt Application class
```

---

## 🛠 Công nghệ sử dụng

### Core
| Thư viện | Phiên bản | Mục đích |
|---|---|---|
| Kotlin | 2.0 | Ngôn ngữ chính |
| Jetpack Compose | BOM 2025 | UI framework |
| Compose Navigation | 2.7+ | Điều hướng màn hình |
| Material3 | 1.2.1 | Design system |

### Architecture
| Thư viện | Phiên bản | Mục đích |
|---|---|---|
| Hilt | 2.51 | Dependency Injection |
| ViewModel | 2.7+ | State management |
| Room | 2.6+ | Local database |
| DataStore | 1.0 | Key-value storage (session) |

### Network
| Thư viện | Phiên bản | Mục đích |
|---|---|---|
| Retrofit | 2.9.0 | HTTP client |
| OkHttp | 4.12 | HTTP logging |
| Gson | 2.10 | JSON parsing |

### Export
| Thư viện | Phiên bản | Mục đích |
|---|---|---|
| Apache POI | 5.2.3 | Xuất Excel .xlsx |
| iText7 | 7.2.5 | Xuất PDF tờ khai |

### Async
| Thư viện | Phiên bản | Mục đích |
|---|---|---|
| Kotlin Coroutines | 1.7+ | Async programming |
| Flow / StateFlow | built-in | Reactive state |

---

## 🚀 Hướng dẫn cài đặt

### Yêu cầu
- Android Studio **Hedgehog** trở lên
- JDK **17**
- Android SDK **36** (compileSdk)
- Min SDK **26** (Android 8.0)

### Clone & Build
```bash
git clone https://github.com/your-team/finmate.git
cd finmate
```

Mở Android Studio → **Open** → chọn thư mục `ck_dd`

### Cấu hình
Kiểm tra `local.properties`:
```properties
sdk.dir=/path/to/your/Android/Sdk
```

### Chạy app
```bash
./gradlew assembleDebug
# hoặc Run trong Android Studio với Ctrl+R
```

### Permissions cần cấp khi chạy
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.RECEIVE_SMS"/>
<uses-permission android:name="android.permission.READ_SMS"/>
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
```

Khi app chạy lần đầu → dialog xin quyền SMS xuất hiện → **Cho phép** để dùng tính năng tự động đọc ngân hàng.

---

## ❓ Câu hỏi phản biện

*Các câu hỏi thường gặp từ giảng viên và câu trả lời chuẩn bị sẵn:*

---

**Q1: Tại sao dùng Room thay vì SQLite thuần?**

> Room là abstraction layer trên SQLite, cung cấp compile-time verification cho SQL queries — nếu query sai, app báo lỗi lúc build chứ không crash lúc runtime. Room còn tích hợp sẵn với Flow/Coroutines, giúp observe dữ liệu realtime mà không cần boilerplate. SQLite thuần không có những lợi ích này.

---

**Q2: Tại sao dùng MVVM thay vì MVC hay MVP?**

> MVVM tách biệt hoàn toàn UI khỏi business logic. ViewModel tồn tại qua configuration changes (xoay màn hình) nhờ `ViewModelStore`. Với Jetpack Compose, ViewModel + StateFlow là cặp bài trùng — UI chỉ observe state và re-compose khi state thay đổi, không cần callback hay interface như MVP.

---

**Q3: Clean Architecture có phức tạp không? Có cần thiết cho project nhỏ không?**

> Đúng là overhead ban đầu cao hơn. Nhưng lợi ích rõ ràng: Domain layer (UseCase, Model) không phụ thuộc vào Android framework — có thể unit test thuần Java/Kotlin mà không cần emulator. Khi đổi từ Room sang SQLite hay từ Retrofit sang Ktor, chỉ cần thay Data layer, Domain và Presentation không đổi. Đây là điểm mấu chốt khi project scale lên.

---

**Q4: SMS Receiver hoạt động thế nào? Có bỏ lỡ SMS không?**

> `BroadcastReceiver` được đăng ký trong `AndroidManifest.xml` với `RECEIVE_SMS` priority cao. Android đảm bảo deliver broadcast đến tất cả receiver trước khi xóa SMS khỏi queue. Tuy nhiên từ Android 8.0+, background BroadcastReceiver bị giới hạn — giải pháp là đăng ký `RECEIVE_SMS` explicit trong Manifest (không phải dynamic), đảm bảo nhận được dù app đang background.

---

**Q5: Regex parse SMS có chính xác 100% không?**

> Không có regex nào chính xác 100% vì mỗi ngân hàng có format SMS khác nhau và có thể thay đổi theo thời gian. Giải pháp của nhóm: dùng nhiều pattern regex lồng nhau theo thứ tự ưu tiên, fallback gracefully nếu parse thất bại. Quan trọng hơn — người dùng luôn có bước xác nhận trước khi ghi nhận, không tự động insert vào DB mà không hỏi.

---

**Q6: Nếu API tỷ giá down thì sao?**

> `ExchangeRateRepository` implement 3 tầng fallback: (1) Trả cached data trong memory nếu còn mới dưới 30 phút, (2) Trả cached data cũ nếu API fail dù đã hết hạn, (3) Trả giá mặc định hardcode nếu chưa từng fetch thành công. App không crash — chỉ hiển thị timestamp cập nhật cũ để người dùng biết.

---

**Q7: Tại sao không lưu mật khẩu bằng bcrypt mà dùng SHA-256?**

> Bcrypt tốt hơn vì có salt tự động và work factor có thể điều chỉnh. SHA-256 đơn giản hơn và đủ cho scope project học thuật này. Trong production thực tế, nhóm sẽ dùng Android Keystore + bcrypt hoặc delegate hoàn toàn cho Firebase Auth.

---

**Q8: StateFlow vs LiveData — tại sao chọn StateFlow?**

> StateFlow là Kotlin-first, tích hợp tự nhiên với Coroutines và Flow operators (`map`, `filter`, `combine`). LiveData là lifecycle-aware nhưng chỉ hoạt động trong Android context — không thể dùng trong Domain layer thuần Kotlin. StateFlow có thể được collect từ bất kỳ Coroutine scope nào, linh hoạt hơn nhiều.

---

**Q9: Tính thuế lũy tiến có đúng với công thức rút gọn không?**

> Có. Công thức rút gọn `income * rate - constant` là cách tính nhanh equivalent với cách tính từng bậc. Ví dụ bậc 3 (10-18tr): `income * 0.15 - 750,000` = `5tr*5% + 5tr*10% + (income-10tr)*15%` = `250,000 + 500,000 + income*0.15 - 1,500,000` = `income*0.15 - 750,000`. Kết quả y hệt nhau, chỉ là công thức tối giản hơn.

---

**Q10: Offline-first hoạt động thế nào khi không có mạng?**

> Room là **source of truth** — mọi UI đều observe từ Room Flow, không observe trực tiếp từ API. Khi có mạng: fetch API → lưu vào Room → Flow tự emit → UI cập nhật. Khi không có mạng: UI vẫn hiển thị dữ liệu từ Room, chỉ phần tỷ giá hiển thị "Cập nhật lần cuối: [timestamp]" thay vì số mới nhất.

---

## 📊 Thống kê dự án

| Chỉ số | Giá trị |
|---|---|
| Tổng số file Kotlin | 52 files |
| Màn hình chính | 6 screens |
| Bảng Database | 3 tables |
| API endpoints | 2 (tỷ giá + giá vàng) |
| Regex patterns | 8 patterns (parse SMS) |
| Bậc thuế | 7 bậc (TT 111/2013) |
| Sheet Excel | 3 sheets |
| Min Android | 8.0 (API 26) |

---

## 👨‍👩‍👧‍👦 Thành viên nhóm

| Thành viên | MSSV | Vai trò |
|---|---|---|
| Lưu Viễn Dương | --- | Core Database & Architecture |
| Huỳnh Thanh Ngân | --- | Automation & Reporting |
| Nguyễn Hữu Quốc Hải | --- | Cloud Integration & Security |
| Hồ Như Trâm | --- | UI/UX Design & Data Visualization |

---

## 📄 License

```
MIT License — Copyright (c) 2026 FinMate Team
```

---

<div align="center">

**💜 FinMate — Trợ lý tài chính cho người Việt**

*Theo dõi thu nhập · Tính thuế tự động · Xuất báo cáo ngay*

</div>



