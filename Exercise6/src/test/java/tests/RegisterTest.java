package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pages.RegisterPage;
import utils.DriverFactory;
public class RegisterTest {
    WebDriver driver;
    RegisterPage registerPage;
    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.getDriver();
        driver.get("http://localhost:8080/Register/Student");
        registerPage = new RegisterPage(driver);
    }

    @Test
    public void testRegisterStudentSuccessfully() {
        registerPage.enterEmail("student@example.com");
        registerPage.enterFullName("Nguyen Van A");
        registerPage.enterPassword("123456");
        registerPage.enterConfirmPassword("123456");
        registerPage.enterPhone("0123456789");
        registerPage.agreeToTerms();
        registerPage.clickRegister();

    }
    @Test
    public void testPasswordMismatch() {
        registerPage.enterEmail("student2@example.com");
        registerPage.enterFullName("Nguyen Van B");
        registerPage.enterPassword("123456");
        registerPage.enterConfirmPassword("654321");
        registerPage.enterPhone("0123456789");
        registerPage.agreeToTerms();
        registerPage.clickRegister();

        System.out.println("[Test] Submitted form with password mismatch");
    }

    @Test
    public void testInvalidEmail() {
        registerPage.enterEmail("invalid-email");
        registerPage.enterFullName("Nguyen Van C");
        registerPage.enterPassword("123456");
        registerPage.enterConfirmPassword("123456");
        registerPage.enterPhone("0123456789");
        registerPage.agreeToTerms();
        registerPage.clickRegister();

        System.out.println("[Test] Submitted form with invalid email");
    }

    @Test
    public void testInvalidPhoneNumber() {
        registerPage.enterEmail("student4@example.com");
        registerPage.enterFullName("Nguyen Van D");
        registerPage.enterPassword("123456");
        registerPage.enterConfirmPassword("123456");
        registerPage.enterPhone("123"); // Số điện thoại quá ngắn
        registerPage.agreeToTerms();
        registerPage.clickRegister();

        System.out.println("[Test] Submitted form with invalid phone number");
    }

    @Test
    public void testWithoutAgreeingTerms() {
        registerPage.enterEmail("student5@example.com");
        registerPage.enterFullName("Nguyen Van E");
        registerPage.enterPassword("123456");
        registerPage.enterConfirmPassword("123456");
        registerPage.enterPhone("0123456789");
        registerPage.clickRegister();
        System.out.println("[Test] Submitted form without agreeing to terms");

    }
    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
