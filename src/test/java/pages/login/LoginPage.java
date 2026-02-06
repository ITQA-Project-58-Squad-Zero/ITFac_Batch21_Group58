package pages.login;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;

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
        // Wait for page to load and elements to be available
        waitForRenderedElements(By.name("username"));
    }

    public void enterUsername(String username) {
        // Find element explicitly to avoid @FindBy initialization race conditions
        WebElementFacade usernameInput = $(By.name("username"));
        usernameInput.waitUntilVisible().type(username);
    }

    public void enterPassword(String password) {
        // Find element explicitly to avoid @FindBy initialization race conditions
        WebElementFacade passwordInput = $(By.name("password"));
        passwordInput.waitUntilVisible().type(password);
    }

    public void clickLogin() {
        // Find element explicitly to avoid @FindBy initialization race conditions
        WebElementFacade submitButton = $(By.cssSelector("button[type='submit']"));
        submitButton.waitUntilClickable().click();
    }

    public boolean isDashboardDisplayed() {
        return getDriver().getCurrentUrl().contains("dashboard");
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }

    public void waitForSuccessfulLogin() {
        waitForCondition()
                .withTimeout(Duration.ofSeconds(10))
                .until(driver -> {
                    String currentUrl = driver.getCurrentUrl();
                    return currentUrl.contains("dashboard") || currentUrl.contains("Plants");
                });
    }
}