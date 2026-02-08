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

    public Response createPlant(String name, double price, int quantity, int categoryId) {
        String body = String.format(
                "{\"name\":\"%s\",\"price\":%.2f,\"quantity\":%d}",
                name, price, quantity);
        return getRequestSpec()
                .body(body)
                .when()
                .post(PLANTS_ENDPOINT + "/category/" + categoryId);
    }

    public Response createPlantMissingName(double price, int quantity, int categoryId) {
        String body = String.format(
                "{\"price\":%.2f,\"quantity\":%d}",
                price, quantity);
        return getRequestSpec()
                .body(body)
                .when()
                .post(PLANTS_ENDPOINT + "/category/" + categoryId);
    }

    public Response updatePlant(int plantId, String name, double price, int quantity) {
        String body = String.format(
                "{\"name\":\"%s\",\"price\":%.2f,\"quantity\":%d}",
                name, price, quantity);
        return getRequestSpec()
                .body(body)
                .when()
                .put(PLANTS_ENDPOINT + "/" + plantId);
    }

    public Response getPlantById(int plantId) {
        return getRequestSpec()
                .when()
                .get(PLANTS_ENDPOINT + "/" + plantId);
    }

    public Response deletePlant(int plantId) {
        return getRequestSpec()
                .when()
                .delete(PLANTS_ENDPOINT + "/" + plantId);
    }

    public Response updatePlantWithoutToken(int plantId, String name, double price, int quantity) {
        String body = String.format(
                "{\"name\":\"%s\",\"price\":%.2f,\"quantity\":%d}",
                name, price, quantity);
        return getRequestSpecWithoutAuth()
                .body(body)
                .when()
                .put(PLANTS_ENDPOINT + "/" + plantId);
    }

    public Response deletePlantWithoutToken(int plantId) {
        return getRequestSpecWithoutAuth()
                .when()
                .delete(PLANTS_ENDPOINT + "/" + plantId);
    }
}
