package api.client.categories;

import api.client.BaseApiClient;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CategoriesApiClient extends BaseApiClient {
    private static final String CATEGORIES_ENDPOINT = "/categories";

    /**
     * GET /api/categories with optional query params name and parentId (empty for TC001).
     */
    public Response getAllCategories() {
        return getRequestSpec()
                .when()
                .get(CATEGORIES_ENDPOINT);
    }

    /**
     * GET /api/categories with optional name and parentId query params.
     */
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

    /**
     * GET /api/categories/{id}
     */
    public Response getCategoryById(int id) {
        return getRequestSpec()
                .when()
                .get(CATEGORIES_ENDPOINT + "/" + id);
    }

    /**
     * GET /api/categories/main - returns only main (parent) categories.
     */
    public Response getMainCategories() {
        return getRequestSpec()
                .when()
                .get(CATEGORIES_ENDPOINT + "/main");
    }

    /**
     * GET /api/categories/summary - returns mainCategories and subCategories counts.
     */
    public Response getCategoriesSummary() {
        return getRequestSpec()
                .when()
                .get(CATEGORIES_ENDPOINT + "/summary");
    }
}
