package api.client;

import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import io.restassured.specification.RequestSpecification;

public abstract class BaseApiClient {
    protected static final EnvironmentVariables ENV = SystemEnvironmentVariables.createEnvironmentVariables();

    protected String getBaseUrl() {
        return ENV.getProperty("environments.default.api.base.url", "http://localhost:8080/api");
    }

    protected RequestSpecification getRequestSpec() {
        return SerenityRest.given()
                .baseUri(getBaseUrl())
                .contentType("application/json")
                .accept("application/json");
    }

    protected RequestSpecification getAuthenticatedRequestSpec(String authToken) {
        return getRequestSpec().header("Authorization", authToken);
    }
}
