package steps.api;

import api.client.BaseApiClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;


import static org.assertj.core.api.Assertions.assertThat;

public class VerifyAuthSteps {

    @Given("the API authentication hook has run")
    public void theApiAuthenticationHookHasRun() {
        // The hook @Before("@api_auth") in ApiAuthenticationHooks runs automatically before this scenario.
        // No explicit action is needed here, checking the token is done in the Then step.
    }

    @Then("a valid token should be stored")
    public void aValidTokenShouldBeStored() {
        String token = BaseApiClient.getAuthToken();
        assertThat(token)
                .withFailMessage("Auth token should not be null or empty. Check if ApiAuthenticationHooks ran correctly.")
                .isNotNull()
                .isNotEmpty();
    }
}