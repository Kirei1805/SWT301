import loipt.example.AccountService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountServiceTest {

    private AccountService accountService;

    @BeforeAll
    void initAll() {
        accountService = new AccountService();
        System.out.println("== Initializing AccountService ==");
    }

    @AfterAll
    void tearDownAll() {
        System.out.println("== All tests completed ==");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("→ Starting a new test case...");
    }

    @AfterEach
    void afterEach() {
        System.out.println("→ Finished executing test case.");
    }

    @Test
    @DisplayName("1. Successful registration with valid input")
    public void testRegisterAccountSuccess() {
        assertTrue(accountService.registerAccount("validUser", "strongPass123", "valid@example.com"));
    }

    @Test
    @DisplayName("2. Registration fails when password is shorter than 7 characters")
    public void testRegisterAccountWithShortPassword() {
        assertFalse(accountService.registerAccount("user1", "12345", "user1@example.com"));
    }

    @Test
    @DisplayName("3. Registration fails when email is invalid")
    public void testRegisterAccountWithInvalidEmail() {
        assertFalse(accountService.registerAccount("user2", "password123", "invalid-email"));
    }

    @Test
    @DisplayName("4. Registration fails when username is null or empty")
    public void testRegisterAccountWithEmptyUsername() {
        assertFalse(accountService.registerAccount("", "password123", "user3@example.com"));
        assertFalse(accountService.registerAccount(null, "password123", "user3@example.com"));
    }

    @Test
    @DisplayName("5. Email validation with multiple valid and invalid cases")
    public void testIsValidEmail() {
        assertTrue(accountService.isValidEmail("test@example.com"));
        assertTrue(accountService.isValidEmail("user.name-123@domain.co"));
        assertFalse(accountService.isValidEmail("user@domain"));
        assertFalse(accountService.isValidEmail("userdomain.com"));
        assertFalse(accountService.isValidEmail(null));
        assertFalse(accountService.isValidEmail(""));
    }

    @Test
    @DisplayName("6. Registration with input data from CSV file")
    public void testRegisterAccountFromCsv() throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("data.csv");
        assertNotNull(resource, "data.csv file not found");
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
                    username, password, email,
                    String.valueOf(expected),
                    String.valueOf(actual),
                    String.valueOf(passed)
            });

            assertEquals(expected, actual, "Test failed for username: " + username);
        }

        csvParser.close();
        reader.close();

        Path outputPath = Paths.get("target", "test-classes", "UnitTest.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            for (String[] row : outputRecords) {
                csvPrinter.printRecord((Object[]) row);
            }
        }

        System.out.println("Test results have been written to file: " + outputPath.toAbsolutePath());
    }

    @Test
    @DisplayName("7. Username validation with valid and invalid inputs")
    public void testIsValidUsername() {
        assertTrue(accountService.isValidUsername("user_123"));
        assertTrue(accountService.isValidUsername("UserName20"));
        assertFalse(accountService.isValidUsername(null));
        assertFalse(accountService.isValidUsername(""));
        assertFalse(accountService.isValidUsername("ab"));
        assertFalse(accountService.isValidUsername("this_is_a_very_long_username"));
        assertFalse(accountService.isValidUsername("user!@#"));
        assertFalse(accountService.isValidUsername("user name"));
    }

    @Test
    @DisplayName("8. Password strength validation")
    public void testIsStrongPassword() {
        assertTrue(accountService.isStrongPassword("Abcdef1!"));
        assertTrue(accountService.isStrongPassword("StrongPass123$"));
        assertFalse(accountService.isStrongPassword(null));
        assertFalse(accountService.isStrongPassword("short1!"));
        assertFalse(accountService.isStrongPassword("alllowercase1!"));
        assertFalse(accountService.isStrongPassword("ALLUPPERCASE1!"));
        assertFalse(accountService.isStrongPassword("NoNumber!"));
        assertFalse(accountService.isStrongPassword("NoSpecial123"));
    }
}
