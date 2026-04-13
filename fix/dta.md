
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
