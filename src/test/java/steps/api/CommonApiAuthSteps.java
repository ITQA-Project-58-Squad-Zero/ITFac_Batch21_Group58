package steps.api;

import api.client.BaseApiClient;
import io.cucumber.java.en.Given;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonApiAuthSteps {

    @Given("the admin has a valid session")
    public void theAdminHasAValidSession() {
        // Authentication handled by @admin_auth hook
        assertThat(BaseApiClient.getAuthToken()).isNotNull();
    }

    @Given("the user has a valid session")
    public void theUserHasAValidSession() {
        // Authentication handled by @user_auth hook
        assertThat(BaseApiClient.getAuthToken()).isNotNull();
    }
}
