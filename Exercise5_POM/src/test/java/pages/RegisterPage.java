package pages;
import org.openqa.selenium.*;
public class RegisterPage extends BasePage {
    public RegisterPage(WebDriver driver) {
        super(driver);
    }
    private By firstName = By.id("firstName");
    private By lastName = By.id("lastName");
    private By email = By.id("userEmail");
    private By genderMale = By.cssSelector("label[for='gender-radio-1']");
    private By mobile = By.id("userNumber");
    private By submitBtn = By.id("submit");
    private By successTitle = By.id("example-modal-sizes-title-lg");
    private By validationError = By.cssSelector(".was-validated .invalid-feedback");
    public void navigate() {
        navigateTo("https://demoqa.com/automation-practice-form");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            ((JavascriptExecutor) driver).executeScript("document.getElementById('fixedban').style.display='none';");
            ((JavascriptExecutor) driver).executeScript("document.getElementsByTagName('footer')[0].style.display='none';");
        } catch (Exception ignored) {}

        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 300);");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public void fillForm(String fName, String lName, String mail, String phone) {
        navigate();

        try {
            waitForVisibility(firstName);
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            waitForVisibility(firstName);
        }

        type(firstName, fName);
        type(lastName, lName);
        type(email, mail);

        try {
            WebElement genderElement = driver.findElement(genderMale);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", genderElement);
        } catch (Exception e) {
            driver.findElement(genderMale).click();
        }

        type(mobile, phone);

        try {
            WebElement submitElement = driver.findElement(submitBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitElement);
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        try {
            WebElement submitElement = driver.findElement(submitBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitElement);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            driver.findElement(submitBtn).click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isSubmitSuccess() {
        try {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            boolean success = false;

            if (isElementVisible(successTitle)) {
                success = true;
            }
            if (driver.findElements(By.cssSelector(".modal-content")).size() > 0) {
                success = true;
            }

            if (driver.findElements(By.xpath("//*[contains(text(), 'Thanks for submitting the form')]")).size() > 0) {
                success = true;
            }
            
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("success") || currentUrl.contains("thank")) {
                success = true;
            }
            
            return success;
        } catch (Exception e) {
            return isElementVisible(successTitle);
        }
    }

    public boolean hasValidationErrors() {
        try {
            Thread.sleep(1000);

            boolean hasErrors = false;

            if (driver.findElements(By.cssSelector(".was-validated .invalid-feedback")).size() > 0) {
                hasErrors = true;
            }

            if (driver.findElements(By.cssSelector("input:invalid")).size() > 0) {
                hasErrors = true;
            }

            if (isElementVisible(successTitle)) {
                return false;
            }
            
            return hasErrors;
        } catch (Exception e) {
            return !isElementVisible(successTitle);
        }
    }
}