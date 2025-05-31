
import loipt.example.AccountService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class AccountServiceTest {

    private final AccountService accountService = new AccountService();

    // 1. Test đăng ký thành công với dữ liệu hợp lệ (hardcoded)
    @Test
    public void testRegisterAccountSuccess() {
        assertTrue(accountService.registerAccount("validUser", "strongPass123", "valid@example.com"));
    }

    // 2. Test password ngắn hơn 7 ký tự => đăng ký thất bại
    @Test
    public void testRegisterAccountWithShortPassword() {
        assertFalse(accountService.registerAccount("user1", "12345", "user1@example.com"));
    }

    // 3. Test email không hợp lệ
    @Test
    public void testRegisterAccountWithInvalidEmail() {
        assertFalse(accountService.registerAccount("user2", "password123", "invalid-email"));
    }

    // 4. Test username null hoặc rỗng
    @Test
    public void testRegisterAccountWithEmptyUsername() {
        assertFalse(accountService.registerAccount("", "password123", "user3@example.com"));
        assertFalse(accountService.registerAccount(null, "password123", "user3@example.com"));
    }

    // 5. Test hàm isValidEmail với nhiều trường hợp đúng/sai
    @Test
    public void testIsValidEmail() {
        assertTrue(accountService.isValidEmail("test@example.com"));
        assertTrue(accountService.isValidEmail("user.name-123@domain.co"));
        assertFalse(accountService.isValidEmail("user@domain"));
        assertFalse(accountService.isValidEmail("userdomain.com"));
        assertFalse(accountService.isValidEmail(null));
        assertFalse(accountService.isValidEmail(""));
    }

    // 6. Test đăng ký tài khoản dựa trên dữ liệu file CSV (data.csv)
    @Test
    public void testRegisterAccountFromCsv() throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("data.csv");
        assertNotNull(resource, "Không tìm thấy file data.csv");
        Path inputPath = Paths.get(resource.toURI());
        Reader reader = Files.newBufferedReader(inputPath);
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        List<String[]> outputRecords = new ArrayList<>();
        outputRecords.add(new String[]{"username", "password", "email", "expected", "actual", "passed"});
        for (CSVRecord record : csvParser) {
            String username = record.get("username");
            String password = record.get("password");
            String email = record.get("email");
            boolean expected = Boolean.parseBoolean(record.get("expected"));

            boolean actual = accountService.registerAccount(username, password, email);
            boolean passed = (actual == expected);

            System.out.printf("username=%s, password=%s, email=%s, expected=%b, actual=%b, passed=%b%n",
                    username, password, email, expected, actual, passed);

            outputRecords.add(new String[]{
                    username,
                    password,
                    email,
                    String.valueOf(expected),
                    String.valueOf(actual),
                    String.valueOf(passed)
            });

            assertEquals(expected, actual, "Test failed for username: " + username);
        }
        csvParser.close();
        reader.close();
        Path outputPath = Paths.get("target", "test-classes", "UnitTest.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (String[] row : outputRecords) {
                csvPrinter.printRecord((Object[]) row);
            }
        }

        System.out.println("Đã ghi kết quả test ra file: " + outputPath.toAbsolutePath());
    }
    // Test username hợp lệ
    @Test
    public void testIsValidUsername() {
        assertTrue(accountService.isValidUsername("user_123"));
        assertTrue(accountService.isValidUsername("UserName20"));
        assertFalse(accountService.isValidUsername(null));
        assertFalse(accountService.isValidUsername(""));
        assertFalse(accountService.isValidUsername("ab"));          // quá ngắn
        assertFalse(accountService.isValidUsername("this_is_a_very_long_username")); // quá dài
        assertFalse(accountService.isValidUsername("user!@#"));    // ký tự đặc biệt không cho phép
        assertFalse(accountService.isValidUsername("user name"));  // có dấu cách
    }
    // Test mật khẩu mạnh
    @Test
    public void testIsStrongPassword() {
        assertTrue(accountService.isStrongPassword("Abcdef1!"));
        assertTrue(accountService.isStrongPassword("StrongPass123$"));
        assertFalse(accountService.isStrongPassword(null));
        assertFalse(accountService.isStrongPassword("short1!"));        // < 8 ký tự
        assertFalse(accountService.isStrongPassword("alllowercase1!")); // thiếu chữ hoa
        assertFalse(accountService.isStrongPassword("ALLUPPERCASE1!")); // thiếu chữ thường
        assertFalse(accountService.isStrongPassword("NoNumber!"));      // thiếu số
        assertFalse(accountService.isStrongPassword("NoSpecial123"));   // thiếu ký tự đặc biệt
    }
}


