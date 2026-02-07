package hooks;

import api.client.BaseApiClient;
import api.client.auth.AuthApiClient;
import api.models.auth.LoginResponse;
import io.cucumber.java.Before;
import org.assertj.core.api.Assertions;
import net.serenitybdd.annotations.Steps;
import net.thucydides.model.util.EnvironmentVariables;

public class ApiAuthenticationHooks {

    @Steps
    AuthApiClient authApiClient;

    private EnvironmentVariables environmentVariables;

    @Before("@admin_auth")
    public void authenticateAdmin() {
        login("credentials.admin.username", "credentials.admin.password", "admin", "admin123");
    }

    @Before("@user_auth")
    public void authenticateUser() {
        login("credentials.user.username", "credentials.user.password", "testuser", "test123");
    }

    private void login(String userProp, String passProp, String defaultUser, String defaultPass) {
        String username = environmentVariables.getProperty(userProp);
        String password = environmentVariables.getProperty(passProp);

        if (username == null) username = defaultUser;
        if (password == null) password = defaultPass;

        LoginResponse response = authApiClient.login(username, password);
        Assertions.assertThat(response.getToken()).as("Login must return a token").isNotNull();
        BaseApiClient.setAuthToken(response.getToken());
    }
}
