package api.client.category;

import api.client.BaseApiClient;
import io.restassured.response.Response;
import api.models.category.Category;

public class CategoryApiClient extends BaseApiClient {
    private static final String CATEGORIES_ENDPOINT = "/categories";

    public Response createCategory(Category category) {
        return getRequestSpec()
                .body(category)
                .when()
                .post(CATEGORIES_ENDPOINT);
    }
    
    // Overload for raw maps or other objects if needed for testing invalid inputs
    public Response createCategory(Object body) {
        return getRequestSpec()
                .body(body)
                .when()
                .post(CATEGORIES_ENDPOINT);
    }

    public Response updateCategory(int id, Category category) {
        return getRequestSpec()
                .body(category)
                .when()
                .put(CATEGORIES_ENDPOINT + "/" + id);
    }

    public Response updateCategory(int id, Object body) {
        return getRequestSpec()
                .body(body)
                .when()
                .put(CATEGORIES_ENDPOINT + "/" + id);
    }

    public Response deleteCategory(int id) {
        return getRequestSpec()
                .when()
                .delete(CATEGORIES_ENDPOINT + "/" + id);
    }

    public Response getAllCategories() {
        return getRequestSpec()
                .when()
                .get(CATEGORIES_ENDPOINT);
    }

    public Response getCategoryById(int id) {
        return getRequestSpec()
                .when()
                .get(CATEGORIES_ENDPOINT + "/" + id);
    }
}
