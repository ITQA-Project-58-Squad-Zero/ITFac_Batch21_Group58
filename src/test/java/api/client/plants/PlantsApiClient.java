package api.client.plants;

import api.client.BaseApiClient;
import io.restassured.response.Response;

public class PlantsApiClient extends BaseApiClient {
    private static final String PLANTS_ENDPOINT = "/plants";

    public Response getAllPlants() {
        return getRequestSpec()
                .when()
                .get(PLANTS_ENDPOINT);
    }

    public Response searchPlantsByName(String name) {
        return getRequestSpec()
                .queryParam("name", name)
                .when()
                .get(PLANTS_ENDPOINT + "/paged");
    }

    public Response filterPlantsByCategory(int categoryId) {
        return getRequestSpec()
                .queryParam("categoryId", categoryId)
                .when()
                .get(PLANTS_ENDPOINT + "/paged");
    }

    public Response getSortedPlants(String sortBy, String direction) {
        return getRequestSpec()
                .queryParam("sort", sortBy + "," + direction)
                .when()
                .get(PLANTS_ENDPOINT + "/paged");
    }
}
