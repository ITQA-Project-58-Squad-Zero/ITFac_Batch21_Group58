package steps.api;

import api.context.ApiResponseContext;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

public class CommonApiSteps {

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        Response response = ApiResponseContext.getResponse();
        if (response == null) {
            throw new IllegalStateException("No API response was set. Ensure a When step that performs an API call has run.");
        }
        response.then().statusCode(statusCode);
    }
}
