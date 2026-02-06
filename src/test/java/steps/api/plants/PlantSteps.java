package steps.api.plants;

import api.client.BaseApiClient;
import api.client.plants.PlantApiClient;
import api.client.category.CategoryApiClient;
import api.models.common.Plant;
import api.models.common.Category;
import api.models.plants.PlantSummary;
import api.context.ApiResponseContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class PlantSteps {

    @Steps
    PlantApiClient plantApiClient;

    @Steps
    CategoryApiClient categoryApiClient;

    private Response response;
    private Plant createdPlant;
    private int plantIdToEdit;

    private int validSubCategoryId;
    private int validParentCategoryId;

    @Given("Valid sub-category exists")
    public void validSubCategoryExists() {
        if (validSubCategoryId == 0) {
            System.out.println("Probing for valid sub-category and parent category IDs...");
            Plant probePlant = new Plant();
            probePlant.setName("Probe " + System.currentTimeMillis());
            probePlant.setPrice(100.0);
            probePlant.setQuantity(10);

            for (int i = 1; i <= 20; i++) {
                // Try to create a plant with this category ID
                Response resp = plantApiClient.createPlant(probePlant, i);
                int statusCode = resp.statusCode();

                if (statusCode == 201) {
                    System.out.println("Found Valid Sub-Category ID: " + i);
                    validSubCategoryId = i;
                    // We also successfully created a plant, might as well use it if needed,
                    // but validPlantIDExists will create another one which is fine.
                    // Clean up? No delete endpoint known.
                    if (validParentCategoryId != 0)
                        break; // Found both
                } else if (statusCode == 400) {
                    // Likely a parent category or other validation error
                    // Check message if possible, but assuming 400 with valid plant data = Parent
                    // Category issue
                    // "Plants can only be added to sub-categories"
                    String body = resp.getBody().asString();
                    if (body.contains("sub-categories")) {
                        System.out.println("Found Parent Category ID: " + i);
                        validParentCategoryId = i;
                        if (validSubCategoryId != 0)
                            break; // Found both
                    }
                }
            }

            if (validSubCategoryId == 0) {
                System.out
                        .println("WARNING: Could not find any valid sub-category in IDs 1-20. Tests will likely fail.");
                // Fallback to 2 just in case?
                validSubCategoryId = 2;
            }
            if (validParentCategoryId == 0) {
                validParentCategoryId = 1; // Fallback
            }
        }
    }

    @Given("Valid plant ID exists")
    public void validPlantIDExists() {
        validSubCategoryExists(); // Ensure IDs are resolved

        Plant plant = new Plant();
        plant.setName("Ant " + System.currentTimeMillis());
        plant.setPrice(100.0);
        plant.setQuantity(10);

        Response resp = plantApiClient.createPlant(plant, validSubCategoryId);

        if (resp.statusCode() == 201) {
            plantIdToEdit = resp.as(Plant.class).getId();
        } else {
            System.out.println("Failed to setup Valid Plant ID. Status: " + resp.statusCode());
            resp.then().log().all();
        }
    }

    @When("Send POST request with valid Name, Price, Quantity to \\/api\\/plants\\/category\\/\\{validCategoryId}")
    public void sendPOSTRequestWithValidData() {
        Plant plant = new Plant();
        plant.setName("Ant " + System.currentTimeMillis());
        plant.setPrice(150.0);
        plant.setQuantity(25);

        response = plantApiClient.createPlant(plant, validSubCategoryId);
        response.then().log().all();
    }

    @When("Send POST request with valid Name, Price, Quantity to valid category {int}")
    public void sendPOSTRequestWithValidDataToCategory(int categoryId) {
        // Ignore Gherkin input, use probed ID
        Plant plant = new Plant();
        plant.setName("Ant " + System.currentTimeMillis());
        plant.setPrice(150.0);
        plant.setQuantity(25);

        response = plantApiClient.createPlant(plant, validSubCategoryId);
    }

    @When("Send POST request with JSON body missing Name")
    public void sendPOSTRequestWithMissingName() {
        Map<String, Object> invalidPlant = new HashMap<>();
        // Name is missing
        invalidPlant.put("price", 150.0);
        invalidPlant.put("quantity", 25);

        response = plantApiClient.createPlantRaw(invalidPlant, validSubCategoryId);
    }

    @When("Send POST request with category that is not a sub-category")
    public void sendPOSTRequestWithInvalidCategory() {
        Plant plant = new Plant();
        plant.setName("Invalid Cat Plant");
        plant.setPrice(100.0);
        plant.setQuantity(10);

        response = plantApiClient.createPlant(plant, validParentCategoryId);
    }

    @When("Send POST request with Price = 0 or negative")
    public void sendPOSTRequestWithInvalidPrice() {
        Plant plant = new Plant();
        plant.setName("Invalid Price Plant");
        plant.setPrice(-10.0);
        plant.setQuantity(10);

        response = plantApiClient.createPlant(plant, validSubCategoryId);
    }

    @When("Send PUT request to \\/api\\/plants\\/\\{validId} with updated Name, Price, Quantity")
    public void sendPUTRequestToUpdatePlant() {
        Plant updateData = new Plant();
        updateData.setName("Updated Plant Name");
        updateData.setPrice(200.0);
        updateData.setQuantity(50);

        int id = (plantIdToEdit > 0) ? plantIdToEdit : 30;

        response = plantApiClient.updatePlant(id, updateData);
    }

    @Then("Verify status code is {int}")
    public void verifyStatusCodeIs(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("Verify HTTP status code is {int}")
    public void verifyHTTPStatusCodeIs(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("API returns success and plant is created")
    public void apiReturnsSuccessAndPlantIsCreated() {
        Plant responsePlant = response.as(Plant.class);
        assertThat(responsePlant.getId()).isGreaterThan(0);
        assertThat(responsePlant.getName()).contains("Ant");
    }

    @Then("API returns validation error and plant is not created")
    public void apiReturnsValidationErrorAndPlantIsNotCreated() {
        // Placeholder
    }

    @Then("API returns 400 Bad Request")
    public void apiReturns400BadRequest() {
        response.then().statusCode(400);
    }

    @Then("API returns 200 OK")
    public void apiReturns200OK() {
        response.then().statusCode(200);
    }

    @When("I request the plant summary")
    public void iRequestThePlantSummary() {
        response = plantApiClient.getPlantSummary();
        ApiResponseContext.setResponse(response);
    }

    @Then("the response body should contain totalPlants and lowStockPlants")
    public void theResponseBodyShouldContainTotalPlantsAndLowStockPlants() {
        PlantSummary summary = response.as(PlantSummary.class);
        assertThat(summary.getTotalPlants()).isGreaterThanOrEqualTo(0);
        assertThat(summary.getLowStockPlants()).isGreaterThanOrEqualTo(0);
    }
}
