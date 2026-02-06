package api.client.plants;

import api.client.BaseApiClient;
import api.models.common.Plant;
import io.restassured.response.Response;
import java.util.Map;

public class PlantApiClient extends BaseApiClient {
    private static final String PLANTS_ENDPOINT = "/plants";
    private static final String CATEGORY_ENDPOINT = "/plants/category";

    public Response createPlant(Plant plant, int categoryId) {
        return getRequestSpec()
                .body(plant)
                .when()
                .post(CATEGORY_ENDPOINT + "/" + categoryId);
    }

    public Response createPlantRaw(Map<String, Object> plantData, Object categoryId) { // categoryId as Object to test
                                                                                       // invalid types if needed, or
                                                                                       // just append to path
        return getRequestSpec()
                .body(plantData)
                .when()
                .post(CATEGORY_ENDPOINT + "/" + categoryId);
    }

    // Overloaded for when we want to test invalid category ID in path specifically
    public Response createPlantRawSubPath(Map<String, Object> plantData, String subPath) {
        return getRequestSpec()
                .body(plantData)
                .when()
                .post(CATEGORY_ENDPOINT + "/" + subPath);
    }

    public Response updatePlant(int id, Plant plant) {
        return getRequestSpec()
                .body(plant)
                .when()
                .put(PLANTS_ENDPOINT + "/" + id);
    }

    public Response updatePlantRaw(int id, Map<String, Object> plantData) {
        return getRequestSpec()
                .body(plantData)
                .when()
                .put(PLANTS_ENDPOINT + "/" + id);
    }

    public Response getPlantSummary() {
        return getRequestSpec()
                .when()
                .get(PLANTS_ENDPOINT + "/summary");
    }
}
