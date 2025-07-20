package pages;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.interactions.Actions;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;
    protected Actions actions;
    protected JavascriptExecutor jsExecutor;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration LONG_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration FAST_TIMEOUT = Duration.ofSeconds(3);
    private static final Duration POLLING_INTERVAL = Duration.ofMillis(100);
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        this.longWait = new WebDriverWait(driver, LONG_TIMEOUT);
        this.actions = new Actions(driver);
        this.jsExecutor = (JavascriptExecutor) driver;
        this.wait.pollingEvery(POLLING_INTERVAL);
        this.longWait.pollingEvery(POLLING_INTERVAL);
    }
    protected WebDriverWait getFastWait() {
        WebDriverWait fastWait = new WebDriverWait(driver, FAST_TIMEOUT);
        fastWait.pollingEvery(Duration.ofMillis(50));
        return fastWait;
    }
    protected WebElement waitForVisibility(By locator) {
        try {
            try {
                return getFastWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (TimeoutException fastTimeout) {
                System.out.println("🔍 Looking for element (fallback): " + locator);
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                System.out.println("✅ Found element: " + locator);
                return element;
            }
        } catch (TimeoutException e) {
            System.err.println("❌ Element not found after 10 seconds: " + locator);
            System.err.println("   Current URL: " + getCurrentUrl());
            System.err.println("   Page title: " + getPageTitle());
            try {
                System.err.println("🔍 Debugging: Looking for similar elements...");
                debugSimilarElements(locator);
            } catch (Exception debugEx) {
                System.err.println("   Debug search also failed: " + debugEx.getMessage());
            }
            
            throw new TimeoutException("Element not visible: " + locator + " on page: " + getCurrentUrl(), e);
        }
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected boolean waitForTextToBePresent(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    protected boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected <T> T waitFor(Function<WebDriver, T> condition) {
        return wait.until(condition);
    }
    protected void click(By locator) {
        WebElement element = waitForClickable(locator);
        highlightElement(element);
        element.click();
    }
    
    protected void fastClick(By locator) {

        try {
            WebElement element = getFastWait().until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
        } catch (TimeoutException e) {
            click(locator);
        }
    }

    protected void jsClick(By locator) {
        WebElement element = waitForPresence(locator);
        jsExecutor.executeScript("arguments[0].click();", element);
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        highlightElement(element);
        element.clear();
        element.sendKeys(text);
    }
    
    protected void fastType(By locator, String text) {
        try {
            WebElement element = getFastWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(text);
        } catch (TimeoutException e) {
            type(locator, text);
        }
    }

    protected void typeSlowly(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            waitFor(driver -> true);
        }
    }

    protected void selectByText(By locator, String optionText) {
        WebElement element = waitForVisibility(locator);
        Select dropdown = new Select(element);
        dropdown.selectByVisibleText(optionText);
    }

    protected void selectByValue(By locator, String value) {
        WebElement element = waitForVisibility(locator);
        Select dropdown = new Select(element);
        dropdown.selectByValue(value);
    }

    protected String getText(By locator) {
        return waitForVisibility(locator).getText().trim();
    }

    protected String getAttribute(By locator, String attribute) {
        return waitForPresence(locator).getAttribute(attribute);
    }

    protected String getCssValue(By locator, String cssProperty) {
        return waitForVisibility(locator).getCssValue(cssProperty);
    }
    protected boolean isElementVisible(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean isElementPresent(By locator) {
        try {
            waitForPresence(locator);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean isElementEnabled(By locator) {
        try {
            return waitForPresence(locator).isEnabled();
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean isElementSelected(By locator) {
        try {
            return waitForPresence(locator).isSelected();
        } catch (TimeoutException e) {
            return false;
        }
    }
    public void navigateTo(String url) {
        try {
            driver.get(url);
            waitForPageLoad();
            System.out.println("✅ Successfully navigated to: " + url);
        } catch (Exception e) {
            System.err.println("❌ Failed to navigate to: " + url);
            System.err.println("   Error: " + e.getMessage());
            System.err.println("   Current URL: " + getCurrentUrl());
            throw new RuntimeException("Navigation failed to: " + url, e);
        }
    }

    public void refreshPage() {
        driver.navigate().refresh();
        waitForPageLoad();
    }

    protected void waitForPageLoad() {
        longWait.until(webDriver -> 
            jsExecutor.executeScript("return document.readyState").equals("complete"));
    }

    protected void scrollToElement(By locator) {
        WebElement element = waitForPresence(locator);
        jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        waitFor(driver -> {
            try {
                return element.isDisplayed();
            } catch (Exception e) {
                return false;
            }
        });
    }

    protected void hoverOverElement(By locator) {
        WebElement element = waitForVisibility(locator);
        actions.moveToElement(element).perform();
    }

    protected void doubleClick(By locator) {
        WebElement element = waitForClickable(locator);
        actions.doubleClick(element).perform();
    }

    protected void rightClick(By locator) {
        WebElement element = waitForClickable(locator);
        actions.contextClick(element).perform();
    }
    protected boolean hasValidationError(By locator) {
        try {
            WebElement element = waitForPresence(locator);
            Boolean validityState = (Boolean) jsExecutor.executeScript(
                "return arguments[0].validity.valid;", element);
            return validityState != null && !validityState;
        } catch (Exception e) {
            return false;
        }
    }

    protected String getValidationMessage(By locator) {
        try {
            WebElement element = waitForPresence(locator);
            String message = (String) jsExecutor.executeScript(
                "return arguments[0].validationMessage;", element);
            return message != null ? message : "";
        } catch (Exception e) {
            return "";
        }
    }

    protected void clearField(By locator) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        // Trigger change event
        jsExecutor.executeScript("arguments[0].dispatchEvent(new Event('change'));", element);
    }
    protected void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    protected void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }

    protected String getAlertText() {
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert().getText();
    }
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }

    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    private void highlightElement(WebElement element) {
        try {
            String originalStyle = element.getAttribute("style");
            jsExecutor.executeScript(
                "arguments[0].setAttribute('style', 'border: 2px solid red; background-color: yellow;');", 
                element);
            waitFor(driver -> true);
            
            jsExecutor.executeScript(
                "arguments[0].setAttribute('style', arguments[1]);", 
                element, originalStyle);
        } catch (Exception e) {
        }
    }
    protected byte[] takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
    private void debugSimilarElements(By targetLocator) {
        try {
            List<WebElement> inputs = driver.findElements(By.tagName("input"));
            System.err.println("   Found " + inputs.size() + " input elements:");
            
            for (int i = 0; i < Math.min(inputs.size(), 5); i++) {
                WebElement input = inputs.get(i);
                String id = input.getAttribute("id");
                String name = input.getAttribute("name");
                String type = input.getAttribute("type");
                System.err.println("     Input " + (i+1) + ": id='" + id + "', name='" + name + "', type='" + type + "'");
            }
            List<WebElement> buttons = driver.findElements(By.tagName("button"));
            System.err.println("   Found " + buttons.size() + " button elements:");
            
            for (int i = 0; i < Math.min(buttons.size(), 3); i++) {
                WebElement button = buttons.get(i);
                String id = button.getAttribute("id");
                String text = button.getText();
                String type = button.getAttribute("type");
                System.err.println("     Button " + (i+1) + ": id='" + id + "', text='" + text + "', type='" + type + "'");
            }
        } catch (Exception e) {
            System.err.println("   Debug search failed: " + e.getMessage());
        }
    }
} 