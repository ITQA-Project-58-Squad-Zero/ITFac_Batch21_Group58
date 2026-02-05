package api.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

public class BaseApiClient {

    private static String authToken;
    private static ThreadLocal<Response> lastResponse = new ThreadLocal<>();
    private EnvironmentVariables environmentVariables;

    public static void setLastResponse(Response response) {
        lastResponse.set(response);
    }

    public static Response getLastResponse() {
        return lastResponse.get();
    }

    protected String getBaseUrl() {
        return EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");
    }

    protected RequestSpecification getRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .setContentType("application/json");
        
        if (authToken != null) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }
        
        return SerenityRest.given().spec(builder.build());
    }

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static String getAuthToken() {
        return authToken;
    }
}
