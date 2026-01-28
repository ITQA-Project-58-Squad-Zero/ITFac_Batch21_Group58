package hooks;

import io.cucumber.java.Before;
import net.serenitybdd.annotations.Steps;
import pages.LoginPage;

public class AuthenticationHooks {

    @Steps
    LoginPage loginPage;

    @Before("@login_as_admin")
    public void loginAsAdmin() {
        System.out.println("Hook: Logging in as Admin");
        loginPage.openPage();
        loginPage.enterUsername("admin");
        loginPage.enterPassword("admin123");
        loginPage.clickLogin();
        loginPage.waitForSuccessfulLogin();
    }

    @Before("@login_as_user")
    public void loginAsUser() {
        System.out.println("Hook: Logging in as User");
        loginPage.openPage();
        // Assuming default user credentials
        loginPage.enterUsername("testuser");
        loginPage.enterPassword("test123");
        loginPage.clickLogin();
        loginPage.waitForSuccessfulLogin();
    }
}
