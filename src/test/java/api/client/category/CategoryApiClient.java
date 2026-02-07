package api.client.category;

import api.client.BaseApiClient;
import api.models.category.Category;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;

public class CategoryApiClient extends BaseApiClient {

    private static final String CATEGORIES_ENDPOINT = "/categories";

    @Step("Create category")
    public Response createCategory(Category category) {
        return getRequestSpec()
                .body(category)
                .when()
                .post(CATEGORIES_ENDPOINT);
    }
    
    @Step("Create category (raw)")
    public Response createCategory(Object body) {
        return getRequestSpec()
                .body(body)
                .when()
                .post(CATEGORIES_ENDPOINT);
    }

    @Step("Update category")
    public Response updateCategory(int id, Category category) {
        return getRequestSpec()
                .body(category)
                .when()
                .put(CATEGORIES_ENDPOINT + "/" + id);
    }

    @Step("Update category (raw)")
    public Response updateCategory(int id, Object body) {
        return getRequestSpec()
                .body(body)
                .when()
                .put(CATEGORIES_ENDPOINT + "/" + id);
    }

    @Step("Delete category")
    public Response deleteCategory(int id) {
        return getRequestSpec()
                .when()
                .delete(CATEGORIES_ENDPOINT + "/" + id);
    }

    @Step("Get all categories")
    public Response getAllCategories() {
        return getRequestSpec()
                .when()
                .get(CATEGORIES_ENDPOINT);
    }

    @Step("Get category by ID")
    public Response getCategoryById(int id) {
        return getRequestSpec()
                .when()
                .get(CATEGORIES_ENDPOINT + "/" + id);
    }
}
