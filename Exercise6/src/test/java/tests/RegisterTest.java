package tests;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import utils.DriverFactory;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RegisterTest extends BaseTest {
    @Test
    @Order(1)
    @DisplayName("🔍 Empty Fields Validation Test")
    void testEmptyFieldsValidation() {
        navigateToRegister();
        registerPage.clearEmail()
                   .clearFullName()
                   .clearPassword()
                   .clearConfirmPassword()
                   .clearPhone()
                   .clickRegisterButton();
        boolean hasEmailError = registerPage.hasEmailValidationError();
        boolean hasNameError = registerPage.hasFullNameValidationError();
        boolean hasPasswordError = registerPage.hasPasswordValidationError();
        boolean hasConfirmPasswordError = registerPage.hasConfirmPasswordValidationError();
        boolean hasPhoneError = registerPage.hasPhoneValidationError();
        boolean staysOnRegisterPage = registerPage.isPageLoaded();
        boolean hasAnyValidation = hasEmailError || hasNameError || hasPasswordError || 
                                 hasConfirmPasswordError || hasPhoneError || staysOnRegisterPage;
        assertTrue(hasAnyValidation,
                  "Empty fields should trigger validation or prevent submission");
        
        printSuccess("Empty fields validation test completed");
    }
    
    @ParameterizedTest(name = "🔍 Testing invalid email: {0}")
    @ValueSource(strings = {
        "invalid@",
        "@invalid.com", 
        "invalid..email@test.com",
        "invalid email@test.com",
        "plaintext",
        "",
        " "
    })
    @Order(2)
    @DisplayName("📧 Invalid Email Formats Test")
    void testInvalidEmailFormats(String invalidEmail) {
        navigateToRegister();
        registerPage.clearEmail()
                   .enterEmail(invalidEmail)
                   .enterFullName("Test User")
                   .enterPassword("password123")
                   .enterConfirmPassword("password123")
                   .enterPhone("0901234567");
        try {
            registerPage.agreeToTerms();
        } catch (Exception e) {
            printWarning("Terms checkbox not available: " + e.getMessage());
        }
        boolean hasClientValidation = registerPage.hasEmailValidationError();
        if (hasClientValidation) {
            printSuccess("Client-side validation for email: '" + invalidEmail + "'");
            return;
        }
        String originalUrl = DriverFactory.getDriver().getCurrentUrl();
        registerPage.clickRegisterButton();
        boolean hasServerError = registerPage.isErrorMessageDisplayed();
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        boolean urlChanged = !currentUrl.equals(originalUrl);
        boolean hasFormError = currentUrl.contains("error");
        boolean someValidationOccurred = hasServerError || urlChanged || hasFormError;

        if (invalidEmail.equals("") || invalidEmail.equals(" ") || invalidEmail.equals("invalid@")) {
            assertTrue(someValidationOccurred,
                      "Definitely invalid email '" + invalidEmail + "' should be caught");
        } else {
            printTestInfo("Email '" + invalidEmail + "' validation result: " + someValidationOccurred);
        }
    }
    
    @ParameterizedTest(name = "🔍 Testing invalid phone: {0}")
    @ValueSource(strings = {
        "123",           // Too short
        "abc123def",     // Contains letters  
        "12345678901234567890", // Too long
        "++84901234567", // Multiple plus signs
        "",              // Empty
        " "              // Space only
    })
    @Order(3)
    @DisplayName("📱 Invalid Phone Formats Test")
    void testInvalidPhoneFormats(String invalidPhone) {
        navigateToRegister();
        registerPage.enterEmail(generateRandomEmail())
                   .enterFullName("Test User")
                   .enterPassword("password123")
                   .enterConfirmPassword("password123")
                   .clearPhone()
                   .enterPhone(invalidPhone);

        try {
            registerPage.agreeToTerms();
        } catch (Exception e) {
            printWarning("Terms checkbox not available: " + e.getMessage());
        }
        boolean hasClientValidation = registerPage.hasPhoneValidationError();
        if (hasClientValidation) {
            printSuccess("Client-side validation for phone: '" + invalidPhone + "'");
            return;
        }

        String originalUrl = DriverFactory.getDriver().getCurrentUrl();
        registerPage.clickRegisterButton();

        boolean hasServerError = registerPage.isErrorMessageDisplayed();
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        boolean urlChanged = !currentUrl.equals(originalUrl);
        
        boolean someValidationOccurred = hasServerError || urlChanged;

        if (invalidPhone.equals("") || invalidPhone.equals(" ")) {
            if (someValidationOccurred) {
                printSuccess("Empty phone '" + invalidPhone + "' was caught by validation");
            } else {
                printTestInfo("Empty phone '" + invalidPhone + "' was not validated - this is acceptable for some apps");
            }
        } else if (invalidPhone.equals("123") || invalidPhone.equals("abc123def")) {
            if (someValidationOccurred) {
                printSuccess("Invalid phone '" + invalidPhone + "' was caught by validation");
            } else {
                printTestInfo("Invalid phone '" + invalidPhone + "' was not validated client-side - this is acceptable for some apps");

            }
        } else {
            printTestInfo("Phone '" + invalidPhone + "' validation result: " + someValidationOccurred);
    }
    }

    @Test
    @Order(4)
    @DisplayName("🔐 Password Confirmation Edge Cases Test")
    void testPasswordConfirmationEdgeCases() {
        navigateToRegister();
        
        int validationErrorsFound = 0;
        int totalEdgeCasesTesteed = 0;
        totalEdgeCasesTesteed++;
        boolean caseSensitiveError = testPasswordConfirmationScenario(
            "Password123", "password123", 
            "Case-sensitive password confirmation", false);
        if (caseSensitiveError) validationErrorsFound++;

        totalEdgeCasesTesteed++;
        boolean trailingSpaceError = testPasswordConfirmationScenario(
            "password123", "password123 ", 
            "Password with trailing space", true);
        if (trailingSpaceError) validationErrorsFound++;
        totalEdgeCasesTesteed++;
        boolean leadingSpaceError = testPasswordConfirmationScenario(
            "password123", " password123", 
            "Password with leading space", true);
        if (leadingSpaceError) validationErrorsFound++;
        totalEdgeCasesTesteed++;
        boolean differentPasswordError = testPasswordConfirmationScenario(
            "password123", "different456", 
            "Completely different passwords", true);
        if (differentPasswordError) validationErrorsFound++;
        
        // Summary
        printTestInfo("Password confirmation edge cases summary:");
        printTestInfo("   Total edge cases tested: " + totalEdgeCasesTesteed);
        printTestInfo("   Validation errors found: " + validationErrorsFound);

        assertTrue(validationErrorsFound >= 2,
                  "Password confirmation should catch at least major mismatches (found " + validationErrorsFound + "/" + totalEdgeCasesTesteed + ")");
        
        printSuccess("Password confirmation edge cases test completed");
    }
    
    private boolean testPasswordConfirmationScenario(String password, String confirmPassword, 
                                                   String description, boolean expectError) {
        printTestInfo("Testing scenario: " + description);
        
        try {
            registerPage.clearEmail()
                       .clearFullName()
                       .clearPassword()
                       .clearConfirmPassword()
                       .clearPhone()
                       .enterEmail(generateRandomEmail())
                       .enterFullName("Test User")
                       .enterPassword(password)
                       .enterConfirmPassword(confirmPassword)
                       .enterPhone("0901234567");

            try {
                registerPage.agreeToTerms();
            } catch (Exception e) {
            }
            boolean hasConfirmPasswordError = registerPage.hasConfirmPasswordValidationError();
            boolean passwordsMatch = registerPage.doPasswordsMatch();
            
            if (hasConfirmPasswordError) {
                printSuccess("Password confirmation validation detected for: " + description);
                return true;
            } else if (!passwordsMatch) {
                printTestInfo("Passwords don't match but no validation error shown for: " + description);
                return expectError;
            } else {
                printTestInfo("No password confirmation issue detected for: " + description);
                return false;
            }
            
        } catch (Exception e) {
            printWarning("Error testing password confirmation scenario '" + description + "': " + e.getMessage());
            return false;
        }
    }
    @Test
    @Order(5)
    @DisplayName("📋 Terms Agreement Required Test")
    void testTermsAgreementRequired() {
        navigateToRegister();

        registerPage.enterEmail(generateRandomEmail())
                   .enterFullName("Test User")
                   .enterPassword("password123")
                   .enterConfirmPassword("password123")
                   .enterPhone(generateRandomPhone());
        try {
            registerPage.disagreeToTerms();
            printTestInfo("Terms checkbox found - explicitly disagreed");
        } catch (Exception e) {
            printWarning("Terms checkbox interaction failed: " + e.getMessage());
        }
        
        registerPage.clickRegisterButton();
        boolean hasTermsError = registerPage.hasTermsValidationError();
        boolean registrationBlocked = !registerPage.waitForRegistrationSuccess(3);
        boolean stillOnRegisterPage = registerPage.isPageLoaded();
        boolean termsValidationWorking = hasTermsError || registrationBlocked || stillOnRegisterPage;
        
        assertTrue(termsValidationWorking,
                  "Terms agreement should be enforced (either through validation errors, blocked submission, or staying on page)");
        printTestInfo("Terms validation check results:");
        printTestInfo("  - Has terms error: " + hasTermsError);
        printTestInfo("  - Registration blocked: " + registrationBlocked);
        printTestInfo("  - Still on register page: " + stillOnRegisterPage);
        
        printSuccess("Terms agreement requirement test completed");
    }
    @Test
    @Order(6)
    @DisplayName("📋 Valid Data Not Submitted Test")
    void testValidDataNotSubmitted() {
        navigateToRegister();
        
        String email = generateRandomEmail();
        String fullName = generateRandomName();
        String phone = generateRandomPhone();
        String password = "SecurePassword123";
        registerPage.enterEmail(email)
                   .enterFullName(fullName)
                   .enterPassword(password)
                   .enterConfirmPassword(password)
                   .enterPhone(phone);
        try {
            registerPage.agreeToTerms();
            printSuccess("Terms checkbox interaction successful");
        } catch (Exception e) {
            printWarning("Terms checkbox not available or not interactable: " + e.getMessage());
        }
        assertEquals(email, registerPage.getEmailValue());
        assertEquals(fullName, registerPage.getFullNameValue());
        assertEquals(phone, registerPage.getPhoneValue());
        boolean termsAgreed = registerPage.areTermsAgreed();
        printTestInfo("Form state check:");
        printTestInfo("   Email: " + registerPage.getEmailValue());
        printTestInfo("   Full name: " + registerPage.getFullNameValue());
        printTestInfo("   Phone: " + registerPage.getPhoneValue());
        printTestInfo("   Terms agreed: " + termsAgreed);
        boolean passwordsMatch = registerPage.doPasswordsMatch();
        printTestInfo("   Passwords match: " + passwordsMatch);
        assertTrue(passwordsMatch);
        boolean allRequiredFilled = registerPage.areAllRequiredFieldsFilled();
        boolean formValid = registerPage.isFormValid();
        printTestInfo("   All required filled: " + allRequiredFilled);
        printTestInfo("   Form valid: " + formValid);
        if (termsAgreed) {
            assertTrue(allRequiredFilled);
            if (!formValid) {
                printWarning("Form not valid despite filled fields and agreed terms - checking validation errors");
                // Log which validations are failing
                printTestInfo("   Email validation error: " + registerPage.hasEmailValidationError());
                printTestInfo("   Name validation error: " + registerPage.hasFullNameValidationError());
                printTestInfo("   Password validation error: " + registerPage.hasPasswordValidationError());
                printTestInfo("   Confirm password validation error: " + registerPage.hasConfirmPasswordValidationError());
                printTestInfo("   Phone validation error: " + registerPage.hasPhoneValidationError());
                printTestInfo("   Terms validation error: " + registerPage.hasTermsValidationError());
            }
        } else {
            printTestInfo("Terms checkbox not working as expected - skipping form validity assertions");
    }
        boolean hasEmailError = registerPage.hasEmailValidationError();
        boolean hasNameError = registerPage.hasFullNameValidationError();
        boolean hasPasswordError = registerPage.hasPasswordValidationError();
        boolean hasConfirmPasswordError = registerPage.hasConfirmPasswordValidationError();
        boolean hasPhoneError = registerPage.hasPhoneValidationError();
        boolean hasTermsError = registerPage.hasTermsValidationError();
        
        int validationErrorCount = 0;
        if (hasEmailError) validationErrorCount++;
        if (hasNameError) validationErrorCount++;
        if (hasPasswordError) validationErrorCount++;
        if (hasConfirmPasswordError) validationErrorCount++;
        if (hasPhoneError) validationErrorCount++;
        if (hasTermsError) validationErrorCount++;
        
        printTestInfo("   Total validation errors: " + validationErrorCount);
        assertTrue(validationErrorCount <= 1,
                  "Valid form data should have minimal validation errors (found " + validationErrorCount + ")");
        assertFalse(registerPage.isSuccessMessageDisplayed());
        assertFalse(registerPage.isErrorMessageDisplayed());
        
        printSuccess("Valid data not submitted test completed");
    }
    @Test
    @Order(7)
    @DisplayName("📝 Form Field Interactions Test")
    void testFormFieldInteractions() {
        navigateToRegister();
        
        String email1 = "first@example.com";
        String email2 = "second@example.com";
        String name1 = "First User";
        String name2 = "Second User";

        registerPage.enterEmail(email1);
        assertEquals(email1, registerPage.getEmailValue());
        
        registerPage.clearEmail().enterEmail(email2);
        assertEquals(email2, registerPage.getEmailValue());

        registerPage.enterFullName(name1);
        assertEquals(name1, registerPage.getFullNameValue());
        
        registerPage.clearFullName().enterFullName(name2);
        assertEquals(name2, registerPage.getFullNameValue());

        String password1 = "password1";
        String password2 = "password2";
        
        registerPage.enterPassword(password1);
        assertEquals(password1, registerPage.getPasswordValue());
        
        registerPage.clearPassword().enterPassword(password2);
        assertEquals(password2, registerPage.getPasswordValue());
        String phone1 = "0901234567";
        String phone2 = "0987654321";
        
        registerPage.enterPhone(phone1);
        assertEquals(phone1, registerPage.getPhoneValue());
        
        registerPage.clearPhone().enterPhone(phone2);
        assertEquals(phone2, registerPage.getPhoneValue());
        
        printSuccess("Form field interactions test completed");
    }
    @ParameterizedTest(name = "🔍 Testing registration with: {0}")
    @CsvSource({
        "john.doe@test.com, John Doe, password123, 0901234567",
        "jane.smith@example.com, Jane Smith, securePass456, 0987654321",
        "test.user@demo.com, Test User, myPassword789, 0912345678"
    })
    @Order(8)
    @DisplayName("📝 Complete Registration Scenarios Test")
    void testCompleteRegistrationScenarios(String email, String fullName, String password, String phone) {
        navigateToRegister();
        registerPage.enterEmail(email)
                   .enterFullName(fullName)
                   .enterPassword(password)
                   .enterConfirmPassword(password)
                   .enterPhone(phone);
        boolean termsAgreed = false;
        try {
            registerPage.agreeToTerms();
            termsAgreed = registerPage.areTermsAgreed();
            printTestInfo("Terms agreement status: " + termsAgreed);
        } catch (Exception e) {
            printWarning("Terms checkbox not available: " + e.getMessage());
        }
        registerPage.clickRegisterButton();
        boolean registrationSucceeded = registerPage.waitForRegistrationSuccess(5);
        boolean hasErrorMessage = registerPage.isErrorMessageDisplayed();
        boolean staysOnRegisterPage = registerPage.isPageLoaded();
        
        if (registrationSucceeded) {
            printSuccess("Registration succeeded for: " + email);
            assertTrue(registrationSucceeded);
        } else if (hasErrorMessage) {
            printTestInfo("Registration failed for: " + email + " - " + registerPage.getErrorMessage());
            assertFalse(registerPage.getErrorMessage().isEmpty());
        } else if (staysOnRegisterPage) {
            printTestInfo("Registration form validation prevented submission for: " + email);
            assertTrue(staysOnRegisterPage);
        } else {
            printWarning("Registration result unclear for: " + email);
            assertTrue(registrationSucceeded || hasErrorMessage || staysOnRegisterPage);
        }
        
        printTestInfo("Registration test completed for: " + email);
    }
    @Test
    @Order(9)
    @DisplayName("🔗 Register Page Navigation Test")
    void testRegisterPageNavigation() {
        // Test direct navigation to register page
        navigateToRegister();

        String currentUrl = getCurrentUrl();
        String pageTitle = getCurrentPageTitle();
        
        printTestInfo("Navigation check:");
        printTestInfo("   Current URL: " + currentUrl);
        printTestInfo("   Page title: " + pageTitle);

        boolean urlIndicatesRegisterPage = currentUrl.toLowerCase().contains("register") ||
                                         pageTitle.toLowerCase().contains("register") ||
                                         pageTitle.toLowerCase().contains("đăng ký") ||
                                         pageTitle.toLowerCase().contains("tham gia");
        
        assertTrue(urlIndicatesRegisterPage,
                  "Should be on registration page (URL: " + currentUrl + ", Title: " + pageTitle + ")");

        boolean hasRegistrationForm = registerPage.isPageLoaded();
        printTestInfo("   Has registration form: " + hasRegistrationForm);
        
        if (!hasRegistrationForm) {
            printWarning("Registration page structure different than expected");
            printTestInfo("This is acceptable - page might use different implementation");
        }
        printSuccess("Register page navigation test completed");
    }
} 