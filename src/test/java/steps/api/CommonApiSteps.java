package steps.api;

import api.client.BaseApiClient;
import io.cucumber.java.en.Then;
import static org.assertj.core.api.Assertions.assertThat;

public class CommonApiSteps {

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        assertThat(BaseApiClient.getLastResponse()).isNotNull();
        BaseApiClient.getLastResponse().then().statusCode(statusCode);
    }
}
