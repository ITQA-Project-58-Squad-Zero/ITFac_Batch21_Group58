package api.client.categories;

import api.client.BaseApiClient;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CategoriesApiClient extends BaseApiClient {
    private static final String CATEGORIES_ENDPOINT = "/categories";

     
    public Response getAllCategories() {
        return getRequestSpec()
                .when()
                .get(CATEGORIES_ENDPOINT);
    }

     
    public Response getAllCategories(String name, Integer parentId) {
        RequestSpecification spec = getRequestSpec();
        if (name != null && !name.isEmpty()) {
            spec = spec.queryParam("name", name);
        }
        if (parentId != null) {
            spec = spec.queryParam("parentId", parentId);
        }
        return spec.when().get(CATEGORIES_ENDPOINT);
    }

     
    public Response getCategoryById(int id) {
        return getRequestSpec()
                .when()
                .get(CATEGORIES_ENDPOINT + "/" + id);
    }

     
    public Response getMainCategories() {
        return getRequestSpec()
                .when()
                .get(CATEGORIES_ENDPOINT + "/main");
    }

     
    public Response getCategoriesSummary() {
        return getRequestSpec()
                .when()
                .get(CATEGORIES_ENDPOINT + "/summary");
    }
}
