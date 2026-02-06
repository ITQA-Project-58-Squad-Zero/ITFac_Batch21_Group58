package api.client.sales;

import api.client.BaseApiClient;
import io.restassured.response.Response;

public class SalesApiClient extends BaseApiClient {
    private static final String SALES_ENDPOINT = "/sales";

    public Response getSaleById(int id) {
        return getRequestSpec()
                .when()
                .get(SALES_ENDPOINT + "/" + id);
    }

    public Response getAllSales() {
        return getRequestSpec()
                .when()
                .get(SALES_ENDPOINT);
    }

    public Response createSale(int plantId, int quantity) {
        return getRequestSpec()
                .queryParam("quantity", quantity)
                .when()
                .post(SALES_ENDPOINT + "/plant/" + plantId);
    }
}
