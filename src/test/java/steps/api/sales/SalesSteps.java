package steps.api.sales;

import api.client.BaseApiClient;
import api.client.auth.AuthApiClient;
import api.client.plants.PlantsApiClient;
import api.client.sales.SalesApiClient;
import api.context.ApiResponseContext;
import api.models.auth.LoginResponse;
import api.models.common.Plant;
import api.models.sales.SaleResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesSteps {

    @Steps
    SalesApiClient salesApiClient;

    @Steps
    AuthApiClient authApiClient;

    @Steps
    PlantsApiClient plantsApiClient;

    private EnvironmentVariables environmentVariables;
    private Response response;
    private int initialSalesCount;
    private int firstAvailablePlantId;
    private int storedSaleId;

    // ===== Dynamic Plant Retrieval Steps =====
    
    @Given("a first available plant exists for sale creation")
    public void aFirstAvailablePlantExistsInTheSystem() {
        Response plantsResponse = plantsApiClient.getAllPlants();
        assertThat(plantsResponse.getStatusCode())
                .withFailMessage("Failed to retrieve plants list")
                .isEqualTo(200);
        
        List<Plant> plants = plantsResponse.jsonPath().getList("", Plant.class);
        assertThat(plants)
                .withFailMessage("No plants exist in the system. Please ensure plants are seeded in the database.")
                .isNotEmpty();
        
        firstAvailablePlantId = plants.get(0).getId();
    }

    @When("I create a sale for the first available plant with quantity {int}")
    public void iCreateASaleForTheFirstAvailablePlantWithQuantity(int quantity) {
        if (firstAvailablePlantId == 0) {
             // Try to fetch plants if not set
             Response plantsResponse = plantsApiClient.getAllPlants();
             if (plantsResponse.getStatusCode() == 200) {
                 try {
                     Plant[] plants = plantsResponse.as(Plant[].class);
                     if (plants.length > 0) {
                         firstAvailablePlantId = plants[0].getId();
                     }
                 } catch (Exception e) {
                     // Try paged fallback
                     try {
                         api.models.plants.PagedPlantsResponse paged = plantsResponse.as(api.models.plants.PagedPlantsResponse.class);
                         if (paged.getContent().length > 0) {
                             firstAvailablePlantId = paged.getContent()[0].getId();
                         }
                     } catch (Exception e2) {
                         // Ignore
                     }
                 }
             }
        }

        assertThat(firstAvailablePlantId)
                .withFailMessage("First available plant ID not set. Ensure 'a first available plant exists in the system' step was executed.")
                .isGreaterThan(0);
        
        response = salesApiClient.createSale(firstAvailablePlantId, quantity);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    // ===== Dynamic Sale Retrieval Steps =====

    @Given("an existing sale exists in the system")
    public void anExistingSaleExistsInTheSystem() {
        Response salesResponse = salesApiClient.getAllSales();
        assertThat(salesResponse.getStatusCode())
                .withFailMessage("Failed to retrieve sales list")
                .isEqualTo(200);
        
        SaleResponse[] sales = salesResponse.as(SaleResponse[].class);
        assertThat(sales)
                .withFailMessage("No sales exist in the system. Please ensure sales are seeded in the database.")
                .isNotEmpty();
        
        storedSaleId = sales[0].getId();
    }

    @When("I request the sale details by the stored sale ID")
    public void iRequestTheSaleDetailsByTheStoredSaleID() {
        assertThat(storedSaleId)
                .withFailMessage("Stored sale ID not set. Ensure 'an existing sale exists in the system' step was executed.")
                .isGreaterThan(0);
        
        response = salesApiClient.getSaleById(storedSaleId);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the response body should return correct sale details for the stored sale")
    public void theResponseBodyShouldReturnCorrectSaleDetailsForTheStoredSale() {
        SaleResponse sale = response.as(SaleResponse.class);
        assertThat(sale.getId()).isEqualTo(storedSaleId);
        assertThat(sale.getPlant()).isNotNull();
        assertThat(sale.getPlant().getName()).isNotNull();
        assertThat(sale.getQuantity()).isGreaterThanOrEqualTo(0);
        assertThat(sale.getTotalPrice()).isGreaterThanOrEqualTo(0);
        assertThat(sale.getSoldAt()).isNotNull();
    }

    @When("I attempt to delete the stored sale")
    public void iAttemptToDeleteTheStoredSale() {
        assertThat(storedSaleId)
                .withFailMessage("Stored sale ID not set. Ensure 'an existing sale exists in the system' step was executed.")
                .isGreaterThan(0);
        
        response = salesApiClient.deleteSale(storedSaleId);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    // ===== Original Steps (unchanged) =====

    @Given("I have the current sales count")
    public void iHaveTheCurrentSalesCount() {
        Response listResponse = salesApiClient.getAllSales();
        SaleResponse[] sales = listResponse.as(SaleResponse[].class);
        initialSalesCount = sales.length;
    }

    @Then("the response body should contain a validation message")
    public void theResponseBodyShouldContainAValidationMessage() {
        String body = response.getBody().asString();
        assertThat(body).containsIgnoringCase("quantity");
    }

    @Then("the response body should contain an insufficient stock error")
    public void theResponseBodyShouldContainAnInsufficientStockError() {
        String body = response.getBody().asString();
        assertThat(body).containsIgnoringCase("stock"); 
    }

    @Then("the sales count should remain unchanged")
    public void theSalesCountShouldRemainUnchanged() {
        Response listResponse = salesApiClient.getAllSales();
        SaleResponse[] sales = listResponse.as(SaleResponse[].class);
        assertThat(sales.length).isEqualTo(initialSalesCount);
    }

    @Given("a sale exists with ID {int}")
    public void aSaleExistsWithID(int id) {
        Response resp = salesApiClient.getSaleById(id);
        if (resp.getStatusCode() == 404) {
            throw new AssertionError("Prerequisite failed: Sale with ID " + id + " does not exist. Please seed the database.");
        }
        assertThat(resp.getStatusCode()).withFailMessage("Failed to check sale existence").isEqualTo(200);
    }

    @When("I request the sale details by ID {int}")
    public void iRequestTheSaleDetailsByID(int id) {
        response = salesApiClient.getSaleById(id);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the response body should return correct sale details having id {int}")
    public void theResponseBodyShouldReturnCorrectSaleDetailsHavingId(int id) {
        SaleResponse sale = response.as(SaleResponse.class);
        assertThat(sale.getId()).isEqualTo(id);
        assertThat(sale.getPlant()).isNotNull();
        assertThat(sale.getPlant().getName()).isNotNull();
        assertThat(sale.getQuantity()).isGreaterThanOrEqualTo(0);
        assertThat(sale.getTotalPrice()).isGreaterThanOrEqualTo(0);
        assertThat(sale.getSoldAt()).isNotNull();
    }

    @When("I request all sales")
    public void iRequestAllSales() {
        response = salesApiClient.getAllSales();
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the response body should contain a list of sales")
    public void theResponseBodyShouldContainAListOfSales() {
        SaleResponse[] sales = response.as(SaleResponse[].class);
        assertThat(sales).isNotEmpty();
    }

    @When("I create a sale for plant ID {int} with quantity {int}")
    public void iCreateASaleForPlantIDWithQuantity(int plantId, int quantity) {
        response = salesApiClient.createSale(plantId, quantity);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the response body should return correct sale details")
    public void theResponseBodyShouldReturnCorrectSaleDetails() {
        SaleResponse sale = response.as(SaleResponse.class);
        assertThat(sale.getId()).isGreaterThan(0);
        assertThat(sale.getPlant()).isNotNull();
        assertThat(sale.getQuantity()).isGreaterThan(0);
        assertThat(sale.getTotalPrice()).isGreaterThan(0);
        assertThat(sale.getSoldAt()).isNotNull();
    }

    @Then("the new sale should be present in the sales list")
    public void theNewSaleShouldBePresentInTheSalesList() {
        int newSaleId = response.as(SaleResponse.class).getId();
        Response listResponse = salesApiClient.getAllSales();
        SaleResponse[] sales = listResponse.as(SaleResponse[].class);
        assertThat(sales).extracting(SaleResponse::getId).contains(newSaleId);
    }

    private int deletedSaleId;

    @When("I delete the sale by ID from the response")
    public void iDeleteTheSaleByIDFromTheResponse() {
        deletedSaleId = response.as(SaleResponse.class).getId();
        response = salesApiClient.deleteSale(deletedSaleId);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the response status code should be {int} or {int}")
    public void theResponseStatusCodeShouldBeOr(int statusCode1, int statusCode2) {
        int actualStatusCode = response.getStatusCode();
        assertThat(actualStatusCode)
                .withFailMessage("Expected status code to be %d or %d but was %d", statusCode1, statusCode2, actualStatusCode)
                .isIn(statusCode1, statusCode2);
    }

    @When("I request the sale details by the deleted sale ID")
    public void iRequestTheSaleDetailsByTheDeletedSaleID() {
        response = salesApiClient.getSaleById(deletedSaleId);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the response body should indicate forbidden access")
    public void theResponseBodyShouldIndicateForbiddenAccess() {
        String body = response.getBody().asString();
        assertThat(body.toLowerCase())
                .containsAnyOf("forbidden", "access denied", "not authorized", "permission");
    }

    @When("I attempt to delete the sale with ID {int}")
    public void iAttemptToDeleteTheSaleWithID(int id) {
        response = salesApiClient.deleteSale(id);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @When("I request paginated sales with page {int} and size {int}")
    public void iRequestPaginatedSalesWithPageAndSize(int page, int size
    ) {
        response = salesApiClient.getPagedSales(page, size);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the response should contain at most {int} sales records")
    public void theResponseShouldContainAtMostSalesRecords(int maxSize) {
        // Parse the paged response - content array contains the sales
        java.util.List<?> content = response.jsonPath().getList("content");
        assertThat(content).isNotNull();
        assertThat(content.size()).isLessThanOrEqualTo(maxSize);
    }
}
