package api.client.auth;

import api.client.BaseApiClient;
import api.models.auth.LoginRequest;
import api.models.auth.LoginResponse;
import io.restassured.response.Response;

public class AuthApiClient extends BaseApiClient {
    private static final String AUTH_ENDPOINT = "/auth/login";

    public LoginResponse login(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);
        
        // Use getRequestSpec() from BaseApiClient which sets the Base URI
        Response response = getRequestSpec()
                .body(request)
                .when()
                .post(AUTH_ENDPOINT);

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Login failed: " + response.getStatusCode() + " - " + response.getBody().asString());
        }
        
        return response.as(LoginResponse.class);
    }
}