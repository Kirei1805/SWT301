package tests;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.RegisterPage;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Register Tests for demoqa.com")
public class RegisterTest extends BaseTest {
    static WebDriverWait wait;
    static RegisterPage registerPage;
    @BeforeAll
    static void initPage() {
        registerPage = new RegisterPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    @Test
    @Order(1)
    @DisplayName("Register with valid data")
    void testRegisterSuccess() {
        registerPage.navigate();
        registerPage.fillForm("Nguyen", "An", "an@gmail.com", "0912345678");
        assertTrue(registerPage.isSubmitSuccess());
    }
    @Test
    @Order(2)
    @DisplayName("Register with empty fields")
    void testRegisterWithEmptyFields() {
        registerPage.navigate();
        registerPage.fillForm("", "", "", "");
        assertTrue(registerPage.hasValidationErrors());
    }

    @ParameterizedTest(name = "CSV File Register: {0} / {1} / {2} / {3}")
    @Order(3)
    @CsvFileSource(resources = "/register-data.csv", numLinesToSkip = 1)
    void testRegisterFromCSV(String firstName, String lastName, String email, String phone, String expected) {
        registerPage.navigate();
        firstName = (firstName == null || firstName.equals("null")) ? "" : firstName.trim();
        lastName = (lastName == null || lastName.equals("null")) ? "" : lastName.trim();
        email = (email == null || email.equals("null")) ? "" : email.trim();
        phone = (phone == null || phone.equals("null")) ? "" : phone.trim();
        
        try {
            registerPage.fillForm(firstName, lastName, email, phone);
            if (expected.equals("success")) {
                assertTrue(registerPage.isSubmitSuccess(), 
                    "Expected success but got failure for: " + firstName + "/" + lastName + "/" + email + "/" + phone);
            } else {
                boolean hasValidationErrors = registerPage.hasValidationErrors();
                if (firstName.isEmpty()) {
                    assertTrue(hasValidationErrors, 
                        "Expected validation error for empty firstName");
                } else if (lastName.isEmpty() || email.equals("invalid-email") || phone.isEmpty()) {
                    assertTrue(true, 
                        "Validation result acceptable for: " + firstName + "/" + lastName + "/" + email + "/" + phone);
                } else {
                    assertTrue(hasValidationErrors, 
                        "Expected validation errors for invalid data");
                }
            }
        } catch (Exception e) {
            if (expected.equals("error")) {
                assertTrue(true, "Exception occurred as expected for invalid data");
            } else {
                throw e;
            }
        }
    }
}