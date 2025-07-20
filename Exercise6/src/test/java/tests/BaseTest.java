package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import pages.LoginPage;
import pages.RegisterPage;
import utils.DriverFactory;
import utils.DriverFactory.BrowserType;

import static org.junit.jupiter.api.Assertions.*;

public class BaseTest {
    protected LoginPage loginPage;
    protected RegisterPage registerPage;

    protected String baseUrl = "http://localhost:8080";
    protected String browser = "chrome";
    protected boolean headless = true;
    @BeforeAll
    static void beforeAll() {
        System.out.println("🚀 Starting JUnit 5 Test Suite Execution");
        System.out.println("========================================");
        System.setProperty("browser", "chrome");
        System.setProperty("headless", "true");
        
        System.out.println("✅ Test suite initialized successfully");
    }
    
    @AfterAll
    static void afterAll() {
        System.out.println("🏁 Test Suite Execution Completed");
        System.out.println("==================================");
    }
    @BeforeEach
    void setupTest(TestInfo testInfo) {
        System.out.println("\n🔧 Setting up test: " + testInfo.getDisplayName());
        try {
            String browserName = System.getProperty("browser", "chrome");
            boolean headlessMode = Boolean.parseBoolean(System.getProperty("headless", "true"));
            BrowserType browserType = BrowserType.valueOf(browserName.toUpperCase());
            DriverFactory.initializeDriver(browserType, headlessMode);
            loginPage = new LoginPage(DriverFactory.getDriver());
            registerPage = new RegisterPage(DriverFactory.getDriver());
            System.out.println("✅ Test setup completed for: " + testInfo.getDisplayName());
        } catch (Exception e) {
            System.err.println("❌ Test setup failed: " + e.getMessage());
            throw new RuntimeException("Failed to setup test", e);
        }
    }
    @AfterEach
    void teardownTest(TestInfo testInfo) {
        System.out.println("🧹 Cleaning up test: " + testInfo.getDisplayName());
        try {
            captureScreenshot("Final state");
            DriverFactory.quitDriver();
            System.out.println("✅ Test cleanup completed for: " + testInfo.getDisplayName());
        } catch (Exception e) {
            System.err.println("⚠️ Test cleanup error: " + e.getMessage());
        }
    }
    protected void navigateToLogin() {
        loginPage.open();
        System.out.println("🔗 Navigated to Login page");
    }
    
    protected void navigateToRegister() {
        registerPage.open();
        System.out.println("🔗 Navigated to Register page");
    }
    /**
     * Assert validation error exists
     */
    protected void assertValidationError(boolean hasError, String fieldName) {
        assertTrue(hasError, fieldName + " should have validation error");
    }
    
    /**
     * Assert no validation error
     */
    protected void assertNoValidationError(boolean hasError, String fieldName) {
        assertFalse(hasError, fieldName + " should not have validation error");
    }
    
    /**
     * Assert element is visible
     */
    protected void assertElementVisible(boolean isVisible, String elementName) {
        assertTrue(isVisible, elementName + " should be visible");
    }
    
    /**
     * Assert text contains expected value
     */
    protected void assertTextContains(String actualText, String expectedText, String fieldName) {
        assertTrue(actualText.toLowerCase().contains(expectedText.toLowerCase()), 
                  fieldName + " should contain '" + expectedText + "' but was '" + actualText + "'");
    }
    
    /**
     * Assert URL contains expected path
     */
    protected void assertUrlContains(String expectedPath) {
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        assertTrue(currentUrl.contains(expectedPath), 
                  "URL should contain '" + expectedPath + "' but was '" + currentUrl + "'");
    }
    /**
     * Capture screenshot for debugging
     */
    protected void captureScreenshot(String description) {
        try {
            if (DriverFactory.getDriver() != null) {
                TakesScreenshot takesScreenshot = (TakesScreenshot) DriverFactory.getDriver();
                byte[] screenshot = takesScreenshot.getScreenshotAs(OutputType.BYTES);
                System.out.println("📸 Screenshot captured: " + description + " (" + screenshot.length + " bytes)");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to capture screenshot: " + e.getMessage());
        }
    }
    
    /**
     * Wait for a short period (use sparingly)
     */
    protected void waitFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Generate random email for testing
     */
    protected String generateRandomEmail() {
        long timestamp = System.currentTimeMillis();
        return "test" + timestamp + "@example.com";
    }
    
    /**
     * Generate random name for testing
     */
    protected String generateRandomName() {
        String[] firstNames = {"John", "Jane", "Mike", "Sarah", "David", "Lisa"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Davis", "Wilson", "Miller"};
        
        int firstIndex = (int) (Math.random() * firstNames.length);
        int lastIndex = (int) (Math.random() * lastNames.length);
        
        return firstNames[firstIndex] + " " + lastNames[lastIndex];
    }
    
    /**
     * Generate random phone number for testing
     */
    protected String generateRandomPhone() {
        // Generate Vietnamese phone number format
        long timestamp = System.currentTimeMillis() % 100000000; // Get last 8 digits
        return "09" + String.format("%08d", timestamp);
    }
    
    /**
     * Get current page title
     */
    protected String getCurrentPageTitle() {
        return DriverFactory.getDriver().getTitle();
    }
    
    /**
     * Get current URL
     */
    protected String getCurrentUrl() {
        return DriverFactory.getDriver().getCurrentUrl();
    }
    
    /**
     * Print test execution info
     */
    protected void printTestInfo(String message) {
        System.out.println("ℹ️ " + message);
    }
    
    /**
     * Print success message
     */
    protected void printSuccess(String message) {
        System.out.println("✅ " + message);
    }
    
    /**
     * Print warning message
     */
    protected void printWarning(String message) {
        System.out.println("⚠️ " + message);
    }
    
    /**
     * Print error message
     */
    protected void printError(String message) {
        System.err.println("❌ " + message);
    }
} 