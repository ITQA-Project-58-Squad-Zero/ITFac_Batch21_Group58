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

    public Response deleteSale(int id) {
        return getRequestSpec()
                .when()
                .delete(SALES_ENDPOINT + "/" + id);
    }

    public Response getPagedSales(int page, int size) {
        return getRequestSpec()
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get(SALES_ENDPOINT + "/page");
    }
}
