package pages.login;

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