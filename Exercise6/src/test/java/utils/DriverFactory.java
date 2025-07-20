package utils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
public class DriverFactory {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ConcurrentHashMap<String, WebDriver> driverPool = new ConcurrentHashMap<>();
    private static final int DEFAULT_IMPLICIT_WAIT = 3;
    private static final int DEFAULT_PAGE_LOAD_TIMEOUT = 10;
    private static final String DEFAULT_WINDOW_SIZE = "1920,1080";
    private static final boolean HEADLESS_MODE = true;
    public enum BrowserType {
        CHROME, FIREFOX, EDGE
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    public static WebDriver initializeDriver(BrowserType browserType) {
        return initializeDriver(browserType, false);
    }
    public static WebDriver initializeDriver(BrowserType browserType, boolean headless) {
        headless = headless || HEADLESS_MODE;
        WebDriver driver = null;
        
        try {
        switch (browserType) {
            case CHROME:
                    driver = createChromeDriver(headless);
                    break;
            case FIREFOX:
                    driver = createFirefoxDriver(headless);
                    break;
            case EDGE:
                    driver = createEdgeDriver(headless);
                    break;
            default:
                    throw new IllegalArgumentException("Browser type not supported: " + browserType);
            }
            
            // Configure driver
            configureDriver(driver);
            
            // Set trong ThreadLocal
            driverThreadLocal.set(driver);
            
            System.out.println("✅ Initialized " + browserType + " driver" + (headless ? " (headless)" : ""));
            
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize driver: " + e.getMessage());
            throw new RuntimeException("Driver initialization failed", e);
        }
        
        return driver;
    }
    
    /**
     * Create Chrome WebDriver
     */
    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // ⚡ AGGRESSIVE PERFORMANCE OPTIMIZATIONS ⚡
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--disable-features=VizDisplayCompositor");
        
        // Speed optimizations
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-background-networking");
        options.addArguments("--disable-sync");
        options.addArguments("--disable-translate");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-images");
        // Note: NOT disabling JavaScript - needed for form validation
        options.addArguments("--disable-plugins-discovery");
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--disable-component-extensions-with-background-pages");
        
        // Memory optimizations
        options.addArguments("--memory-pressure-off");
        options.addArguments("--max_old_space_size=4096");
        
        // Window size
        options.addArguments("--window-size=" + getWindowSize());
        
        // Headless mode for maximum speed
        if (headless) {
            options.addArguments("--headless");
            options.addArguments("--disable-logging");
            options.addArguments("--silent");
        }
        
        // Disable notifications và pop-ups
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        
        // Set download behavior
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        
        return new ChromeDriver(options);
    }
    
    /**
     * Create Firefox WebDriver
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        // Disable notifications
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("dom.push.enabled", false);
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Create Edge WebDriver
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions options = new EdgeOptions();
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--window-size=" + getWindowSize());
        
        return new EdgeDriver(options);
    }
    
    /**
     * Configure driver với timeouts và window management
     */
    private static void configureDriver(WebDriver driver) {
        // Set timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(getPageLoadTimeout()));
        
        // Maximize window nếu không phải headless
        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            System.out.println("Could not maximize window (probably headless mode)");
        }
    }
    
    /**
     * Quit driver và clean up
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✅ Driver quit successfully");
            } catch (Exception e) {
                System.err.println("❌ Error quitting driver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
    
    /**
     * Quit all drivers in pool
     */
    public static void quitAllDrivers() {
        driverPool.values().forEach(driver -> {
            try {
                if (driver != null) {
                    driver.quit();
                }
            } catch (Exception e) {
                System.err.println("Error quitting driver: " + e.getMessage());
            }
        });
        driverPool.clear();
        
        // Also quit current thread driver
        quitDriver();
    }
    
    /**
     * Create driver với browser name string
     */
    public static WebDriver createDriver(String browserName) {
        return createDriver(browserName, false);
    }
    
    /**
     * Create driver với browser name string và headless option
     */
    public static WebDriver createDriver(String browserName, boolean headless) {
        BrowserType browserType;
        
        switch (browserName.toLowerCase()) {
            case "chrome":
            case "googlechrome":
                browserType = BrowserType.CHROME;
                break;
            case "firefox":
            case "ff":
                browserType = BrowserType.FIREFOX;
                break;
            case "edge":
            case "microsoftedge":
                browserType = BrowserType.EDGE;
                break;
            default:
                System.out.println("⚠️ Unknown browser: " + browserName + ", defaulting to Chrome");
                browserType = BrowserType.CHROME;
        }
        
        return initializeDriver(browserType, headless);
    }
    
    // ==================== CONFIGURATION METHODS ====================
    
    /**
     * Get browser từ system property hoặc default
     */
    public static String getBrowser() {
        return System.getProperty("browser", "chrome");
    }
    
    /**
     * Check nếu headless mode enabled
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"));
    }
    
    /**
     * Get implicit wait timeout
     */
    private static int getImplicitWait() {
        return Integer.parseInt(System.getProperty("implicit.wait", String.valueOf(DEFAULT_IMPLICIT_WAIT)));
    }
    
    /**
     * Get page load timeout
     */
    private static int getPageLoadTimeout() {
        return Integer.parseInt(System.getProperty("page.load.timeout", String.valueOf(DEFAULT_PAGE_LOAD_TIMEOUT)));
    }
    
    /**
     * Get window size
     */
    private static String getWindowSize() {
        return System.getProperty("window.size", DEFAULT_WINDOW_SIZE);
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Check if driver is alive
     */
    public static boolean isDriverAlive() {
        WebDriver driver = getDriver();
        if (driver == null) {
            return false;
        }
        
        try {
            driver.getCurrentUrl();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Restart driver nếu cần
     */
    public static WebDriver restartDriver() {
        String browser = getBrowser();
        boolean headless = isHeadless();
        
        quitDriver();
        return createDriver(browser, headless);
    }
    
    /**
     * Print driver info
     */
    public static void printDriverInfo() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                System.out.println("🔍 Driver Info:");
                System.out.println("   Current URL: " + driver.getCurrentUrl());
                System.out.println("   Page Title: " + driver.getTitle());
                System.out.println("   Window Handle: " + driver.getWindowHandle());
            } catch (Exception e) {
                System.out.println("Could not get driver info: " + e.getMessage());
            }
        } else {
            System.out.println("❌ No driver available");
        }
    }
} 