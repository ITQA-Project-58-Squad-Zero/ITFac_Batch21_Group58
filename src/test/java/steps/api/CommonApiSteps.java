package steps.api;

import api.context.ApiResponseContext;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import api.client.BaseApiClient;
import io.cucumber.java.en.Then;
import static org.assertj.core.api.Assertions.assertThat;

public class CommonApiSteps {

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        Response response = ApiResponseContext.getResponse();
        if (response == null) {
            throw new IllegalStateException("No API response was set. Ensure a When step that performs an API call has run.");
        }
        response.then().statusCode(statusCode);
        assertThat(BaseApiClient.getLastResponse()).isNotNull();
        BaseApiClient.getLastResponse().then().statusCode(statusCode);
    }
}
