package steps.api.sales;

import api.client.BaseApiClient;
import api.client.auth.AuthApiClient;
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

import static org.assertj.core.api.Assertions.assertThat;

public class SalesSteps {

    @Steps
    SalesApiClient salesApiClient;

    @Steps
    AuthApiClient authApiClient;

    private EnvironmentVariables environmentVariables;
    private Response response;
    private int initialSalesCount;

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
}
