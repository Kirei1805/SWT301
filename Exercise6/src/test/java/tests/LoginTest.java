package tests;

import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import utils.DriverFactory;

public class LoginTest {
    WebDriver driver;
    LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        System.out.println("[Setup] Initializing WebDriver...");
        driver = DriverFactory.getDriver();

        String loginUrl = "http://localhost:8080/Login";
        System.out.println("[Setup] Opening URL: " + loginUrl);
        driver.get(loginUrl);
        System.out.println("[Setup] Current URL: " + driver.getCurrentUrl());

        loginPage = new LoginPage(driver);
    }

    @Test
    public void testValidLogin() {
        System.out.println("[Test] Running testValidLogin...");

        loginPage.enterUsername("student@example.com");
        loginPage.enterPassword("123456");
        loginPage.clickLogin();

        System.out.println("[Test] Submitted login form");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("[Teardown] Quitting driver...");
        if (driver != null) {
            driver.quit();
        }
    }
}
