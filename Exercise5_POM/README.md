# Exercise 5: Kiểm thử tự động với Selenium WebDriver và JUnit 5

## Mô tả
Dự án này áp dụng mô hình Page Object Model (POM) để kiểm thử tự động trang web demoqa.com với chức năng đăng ký form.
 
## Cấu trúc dự án

```
src/test/java/
├── pages/
│   ├── BasePage.java          # Lớp cơ sở cho tất cả Page Objects
│   ├── LoginPage.java         # Page Object cho trang đăng nhập
│   └── RegisterPage.java      # Page Object cho trang đăng ký
├── tests/
│   ├── BaseTest.java          # Lớp cơ sở cho tất cả Test classes
│   ├── LoginTest.java         # Test cases cho đăng nhập
│   └── RegisterTest.java      # Test cases cho đăng ký
└── utils/
    └── DriverFactory.java     # Factory để tạo WebDriver
```

## Các tính năng đã triển khai

### 1. Page Object Model (POM)
- **BasePage**: Chứa các phương thức chung (click, type, wait, navigate)
- **LoginPage**: Xử lý các thao tác trên trang đăng nhập
- **RegisterPage**: Xử lý các thao tác trên trang đăng ký

### 2. Base Classes
- **BaseTest**: Khởi tạo WebDriver và quản lý lifecycle
- **DriverFactory**: Tạo và cấu hình WebDriver

### 3. Test Cases
- **LoginTest**: 
  - Test đăng nhập thành công
  - Test đăng nhập thất bại
  - Parameterized tests với @CsvSource
  - Parameterized tests với file CSV
- **RegisterTest**:
  - Test đăng ký thành công
  - Test đăng ký với trường trống
  - Parameterized tests với nhiều bộ dữ liệu

### 4. Dependencies
- Selenium Java 4.21.0
- JUnit 5 5.10.2
- JUnit Jupiter Params
- WebDriverManager 5.7.0

## Cách chạy test

### Sử dụng Maven
```bash
mvn clean test
```

### Sử dụng IDE
1. Mở project trong IntelliJ IDEA hoặc Eclipse
2. Chạy test từ IDE

### Chạy test cụ thể
```bash
# Chạy LoginTest
mvn test -Dtest=LoginTest

# Chạy RegisterTest
mvn test -Dtest=RegisterTest
```

## Kết quả mong đợi

### LoginTest
- ✅ Test đăng nhập thành công với credentials hợp lệ
- ✅ Test đăng nhập thất bại với credentials không hợp lệ
- ✅ Parameterized tests với nhiều bộ dữ liệu

### RegisterTest
- ✅ Test đăng ký thành công với dữ liệu hợp lệ
- ✅ Test validation với trường trống
- ✅ Parameterized tests với các trường hợp khác nhau

## Lưu ý quan trọng

1. **JavaScript được bật**: RegisterTest cần JavaScript để hoạt động
2. **Thời gian chờ**: Đã tăng lên 15 giây để xử lý trang web load chậm
3. **Xử lý quảng cáo**: Tự động ẩn quảng cáo và footer có thể che form
4. **Fallback mechanisms**: Có các phương thức dự phòng cho các thao tác click

## Troubleshooting

### Lỗi TimeoutException
- Kiểm tra kết nối internet
- Tăng thời gian chờ trong BasePage
- Kiểm tra xem trang web có thay đổi cấu trúc không

### Lỗi Element not found
- Kiểm tra locators có đúng không
- Đảm bảo trang web đã load hoàn toàn
- Kiểm tra JavaScript có được bật không 