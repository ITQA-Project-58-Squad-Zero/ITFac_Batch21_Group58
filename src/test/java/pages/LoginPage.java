package pages;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

import java.time.Duration;

@DefaultUrl("/ui/login")
public class LoginPage extends PageObject {

    @FindBy(name = "username")
    WebElementFacade usernameField;

    @FindBy(name = "password")
    WebElementFacade passwordField;

    @FindBy(css = "button[type='submit']")
    WebElementFacade loginButton;

    @FindBy(css = "div.alert.alert-danger.text-center")
    WebElementFacade errorMessage;

    public void openPage() {
        getDriver().get("http://localhost:8080/ui/login");
        System.out.println("Explicitly navigated to: " + getDriver().getCurrentUrl());
    }

    public void enterUsername(String username) {
        usernameField.waitUntilVisible().type(username);
    }

    public void enterPassword(String password) {
        passwordField.waitUntilVisible().type(password);
    }

    public void clickLogin() {
        loginButton.waitUntilClickable().click();
    }

    public boolean isDashboardDisplayed() {
        System.out.println("Current URL: " + getDriver().getCurrentUrl());
        return getDriver().getCurrentUrl().contains("dashboard");
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }

    public void waitForSuccessfulLogin() {
        System.out.println("Waiting for redirect after login...");
        System.out.println("Current URL before wait: " + getDriver().getCurrentUrl());

        try {
            waitForCondition()
                    .withTimeout(Duration.ofSeconds(10))
                    .until(driver -> {
                        String currentUrl = driver.getCurrentUrl();
                        System.out.println("Checking URL: " + currentUrl);
                        return currentUrl.contains("dashboard") || currentUrl.contains("Plants");
                    });
        } catch (Exception e) {
            System.out.println("Failed to redirect. Final URL: " + getDriver().getCurrentUrl());
            throw e;
        }
    }
}