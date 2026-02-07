package api.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;
import net.serenitybdd.rest.SerenityRest;

public class BaseApiClient {

    private static final ThreadLocal<String> authToken = new ThreadLocal<>();
    private EnvironmentVariables environmentVariables;

    protected String getBaseUrl() {
        return EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty("api.base.url");
    }

    protected RequestSpecification getRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .setContentType("application/json");
        
        if (authToken.get() != null) {
            builder.addHeader("Authorization", "Bearer " + authToken.get());
        }
        
        return SerenityRest.given().spec(builder.build());
    }

    public static void setAuthToken(String token) {
        authToken.set(token);
    }

    public static String getAuthToken() {
        return authToken.get();
    }
}
