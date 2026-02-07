package steps.api.plants;

import api.client.plants.PlantsApiClient;
import api.models.common.Plant;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;
import api.client.BaseApiClient;
import api.context.ApiResponseContext;

import static org.assertj.core.api.Assertions.assertThat;

public class PlantsSteps {

    @Steps
    PlantsApiClient plantsApiClient;

    private Response response;

    @When("I request all plants")
    public void iRequestAllPlants() {
        response = plantsApiClient.getAllPlants();
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @When("I search for plants with name {string}")
    public void iSearchForPlantsWithName(String name) {
        response = plantsApiClient.searchPlantsByName(name);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the response body should contain a list of plants")
    public void theResponseBodyShouldContainAListOfPlants() {
        Plant[] plants = response.as(Plant[].class);
        assertThat(plants).isNotEmpty();

        // Verify each plant has required fields
        for (Plant plant : plants) {
            assertThat(plant.getId()).isGreaterThan(0);
            assertThat(plant.getName()).isNotNull();
            assertThat(plant.getPrice()).isGreaterThanOrEqualTo(0);
            assertThat(plant.getQuantity()).isGreaterThanOrEqualTo(0);
            assertThat(plant.getCategory()).isNotNull();
        }
    }

    @Then("all returned plants should have names containing {string}")
    public void allReturnedPlantsShouldHaveNamesContaining(String searchTerm) {
        api.models.plants.PagedPlantsResponse pagedResponse = response
                .as(api.models.plants.PagedPlantsResponse.class);
        Plant[] plants = pagedResponse.getContent();
        assertThat(plants).isNotEmpty();

        for (Plant plant : plants) {
            assertThat(plant.getName().toLowerCase())
                    .as("Plant name should contain search term")
                    .contains(searchTerm.toLowerCase());
        }
    }

    @Then("the returned plant count should be less than or equal to {int}")
    public void theReturnedPlantCountShouldBeLessThanOrEqualTo(int maxCount) {
        Plant[] plants = response.as(Plant[].class);
        assertThat(plants.length).isLessThanOrEqualTo(maxCount);
    }

    @When("I filter plants by category ID {int}")
    public void iFilterPlantsByCategoryID(int categoryId) {
        response = plantsApiClient.filterPlantsByCategory(categoryId);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("all returned plants should belong to category ID {int}")
    public void allReturnedPlantsShouldBelongToCategoryID(int categoryId) {
        api.models.plants.PagedPlantsResponse pagedResponse = response
                .as(api.models.plants.PagedPlantsResponse.class);
        Plant[] plants = pagedResponse.getContent();

        // If plants are returned, verify they all belong to the specified category
        if (plants != null && plants.length > 0) {
            for (Plant plant : plants) {
                assertThat(plant.getCategory().getId())
                        .as("Plant should belong to category " + categoryId)
                        .isEqualTo(categoryId);
            }
        }
        // Note: Empty results are acceptable - it means no plants exist for this
        // category
    }

    @When("I request plants sorted by {string} in {string} order")
    public void iRequestPlantsSortedByInOrder(String sortBy, String direction) {
        response = plantsApiClient.getSortedPlants(sortBy, direction);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the plants should be sorted by name in ascending order")
    public void thePlantsShouldBeSortedByNameInAscendingOrder() {
        api.models.plants.PagedPlantsResponse pagedResponse = response
                .as(api.models.plants.PagedPlantsResponse.class);
        Plant[] plants = pagedResponse.getContent();

        // Verify sorting only if there are multiple plants
        if (plants != null && plants.length > 1) {
            for (int i = 0; i < plants.length - 1; i++) {
                String currentName = plants[i].getName();
                String nextName = plants[i + 1].getName();
                assertThat(currentName.compareToIgnoreCase(nextName))
                        .as("Plants should be sorted by name in ascending order: " +
                                currentName + " should come before " + nextName)
                        .isLessThanOrEqualTo(0);
            }
        }
    }

    @Then("the plants should be sorted by price in descending order")
    public void thePlantsShouldBeSortedByPriceInDescendingOrder() {
        api.models.plants.PagedPlantsResponse pagedResponse = response
                .as(api.models.plants.PagedPlantsResponse.class);
        Plant[] plants = pagedResponse.getContent();

        // Verify sorting only if there are multiple plants
        if (plants != null && plants.length > 1) {
            for (int i = 0; i < plants.length - 1; i++) {
                double currentPrice = plants[i].getPrice();
                double nextPrice = plants[i + 1].getPrice();
                assertThat(currentPrice)
                        .as("Plants should be sorted by price in descending order: " +
                                currentPrice + " should be >= " + nextPrice)
                        .isGreaterThanOrEqualTo(nextPrice);
            }
        }
    }

    @Then("the plants should be sorted by quantity in ascending order")
    public void thePlantsShouldBeSortedByQuantityInAscendingOrder() {
        api.models.plants.PagedPlantsResponse pagedResponse = response
                .as(api.models.plants.PagedPlantsResponse.class);
        Plant[] plants = pagedResponse.getContent();

        // Verify sorting only if there are multiple plants
        if (plants != null && plants.length > 1) {
            for (int i = 0; i < plants.length - 1; i++) {
                int currentQuantity = plants[i].getQuantity();
                int nextQuantity = plants[i + 1].getQuantity();
                assertThat(currentQuantity)
                        .as("Plants should be sorted by quantity in ascending order: " +
                                currentQuantity + " should be <= " + nextQuantity)
                        .isLessThanOrEqualTo(nextQuantity);
            }
        }
    }
}
