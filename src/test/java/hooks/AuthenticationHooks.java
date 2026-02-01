package hooks;

import io.cucumber.java.Before;
import net.serenitybdd.annotations.Steps;
import net.thucydides.model.util.EnvironmentVariables;
import pages.login.LoginPage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationHooks {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHooks.class);

    LoginPage loginPage;

    private EnvironmentVariables environmentVariables;

    @Before("@login_as_admin")
    public void loginAsAdmin() {
        LOGGER.info("Hook: Logging in as Admin");
        loginPage.openPage();
        loginPage.enterUsername(environmentVariables.getProperty("credentials.admin.username"));
        loginPage.enterPassword(environmentVariables.getProperty("credentials.admin.password"));
        loginPage.clickLogin();
        loginPage.waitForSuccessfulLogin();
    }

    @Before("@login_as_user")
    public void loginAsUser() {
        LOGGER.info("Hook: Logging in as User");
        loginPage.openPage();
        loginPage.enterUsername(environmentVariables.getProperty("credentials.user.username"));
        loginPage.enterPassword(environmentVariables.getProperty("credentials.user.password"));
        loginPage.clickLogin();
        loginPage.waitForSuccessfulLogin();
    }
}
