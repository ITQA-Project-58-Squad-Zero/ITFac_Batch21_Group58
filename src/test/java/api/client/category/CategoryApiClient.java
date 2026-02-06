package api.client.category;

import api.client.BaseApiClient;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;

public class CategoryApiClient extends BaseApiClient {

    private static final String CATEGORY_ENDPOINT = "/api/categories";

    @Step("Get all categories")
    public Response getAllCategories() {
        return getRequestSpec()
                .when()
                .get(CATEGORY_ENDPOINT);
    }
}
