package loipt.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
class User {
    private static final Logger logger = LoggerFactory.getLogger(User.class);
    // Đặt name và age là non-public và cung cấp phương thức truy cập
    private String name;
    private int age;
    // Constructor để khởi tạo user
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    // Phương thức truy cập (getter)
    public String getName() {
        return name;
    }
    // Phương thức truy cập (getter)
    public int getAge() {
        return age;
    }
    // Phương thức display sử dụng logger thay vì System.out.println
    public void display() {
        // Sử dụng logger để ghi thông tin thay vì System.out.println
        logger.info("Name: {}, Age: {}", name, age);
    }
}

