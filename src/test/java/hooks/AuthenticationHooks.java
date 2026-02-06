package hooks;

import io.cucumber.java.Before;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import pages.login.LoginPage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationHooks {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHooks.class);

    private LoginPage loginPage;
    private EnvironmentVariables environmentVariables = SystemEnvironmentVariables.currentEnvironmentVariables();

    private String getProperty(String key) {
        return environmentVariables.getProperty(key);
    }
    
    private LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        return loginPage;
    }

    @Before("@login_as_admin")
    public void loginAsAdmin() {
        LOGGER.info("Hook: Logging in as Admin");
        LoginPage page = getLoginPage();
        page.openPage();
        page.enterUsername(getProperty("credentials.admin.username"));
        page.enterPassword(getProperty("credentials.admin.password"));
        page.clickLogin();
        page.waitForSuccessfulLogin();
    }

    @Before("@login_as_user")
    public void loginAsUser() {
        LOGGER.info("Hook: Logging in as User");
        LoginPage page = getLoginPage();
        page.openPage();
        page.enterUsername(getProperty("credentials.user.username"));
        page.enterPassword(getProperty("credentials.user.password"));
        page.clickLogin();
        page.waitForSuccessfulLogin();
    }
}
