package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    public static WebDriver createDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // Bật JavaScript (cần thiết cho RegisterTest)
        // prefs.put("profile.managed_default_content_settings.javascript", 1); // 1: Cho phép JavaScript
        options.addArguments("--incognito");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");

        return new ChromeDriver(options);
    }
}