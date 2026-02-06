package steps.api;

import api.client.BaseApiClient;
import io.cucumber.java.en.Given;
import static org.assertj.core.api.Assertions.assertThat;

public class CommonApiAuthSteps {

    @Given("the admin has a valid session")
    public void theAdminHasAValidSession() {
        assertThat(BaseApiClient.getAuthToken()).isNotNull();
    }

    @Given("the user has a valid session")
    public void theUserHasAValidSession() {
        assertThat(BaseApiClient.getAuthToken()).isNotNull();
    }
}
