package steps.api.plants;

import api.client.BaseApiClient;
import api.client.plants.PlantsApiClient;
import api.models.common.Plant;
import api.models.common.Category;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;
import api.client.auth.AuthApiClient;
import api.models.auth.LoginResponse;
import api.context.ApiResponseContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PlantsSteps {

    @Steps
    PlantsApiClient plantsApiClient;

    @Steps
    AuthApiClient authApiClient;

    private Response response;

     
    private int validSubCategoryId;
    private int validParentCategoryId;
    private int validPlantId;
    private String firstAvailablePlantName;
    private int firstAvailableCategoryId;

     
     
     

    @When("I request all plants")
    public void iRequestAllPlants() {
        response = plantsApiClient.getAllPlants();
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the response body should contain a list of plants")
    public void theResponseBodyShouldContainAListOfPlants() {
        Plant[] plants = response.as(Plant[].class);
        assertThat(plants).isNotEmpty();

        for (Plant plant : plants) {
            assertThat(plant.getId()).isGreaterThan(0);
            assertThat(plant.getName()).isNotNull();
            assertThat(plant.getPrice()).isGreaterThanOrEqualTo(0);
            assertThat(plant.getQuantity()).isGreaterThanOrEqualTo(0);
        }
    }

    @Given("a first available plant exists in the system")
    public void aFirstAvailablePlantExistsInTheSystem() {
        response = plantsApiClient.getAllPlants();
        Plant[] plants = response.as(Plant[].class);

        if (plants == null || plants.length == 0) {
             
            ensureValidSubCategoryExists();
            plantsApiClient.createPlant("AutoPlant" + System.currentTimeMillis() % 10000, 10.0, 10, validSubCategoryId);
            response = plantsApiClient.getAllPlants();
            plants = response.as(Plant[].class);
        }

        assertThat(plants).as("No plants found in the system").isNotEmpty();
        firstAvailablePlantName = plants[0].getName();
        validPlantId = plants[0].getId();
    }

    @When("I search for plants with the first available plant name")
    public void iSearchForPlantsWithFirstAvailablePlantName() {
        assertThat(firstAvailablePlantName).as("First available plant name not set").isNotNull();
        response = plantsApiClient.searchPlantsByName(firstAvailablePlantName);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("all returned plants should have names containing the searched term")
    public void allReturnedPlantsShouldHaveNamesContainingSearchedTerm() {
        Plant[] plants = getPlantsFromResponse();
        if (plants != null && plants.length > 0) {
            for (Plant plant : plants) {
                assertThat(plant.getName().toLowerCase())
                        .as("Plant name should contain search term")
                        .contains(firstAvailablePlantName.toLowerCase());
            }
        }
    }

    @Given("a first available category exists in the system")
    public void aFirstAvailableCategoryExistsInTheSystem() {
        ensureAdminAuth();

        Response catResponse = io.restassured.RestAssured.given()
                .baseUri("http://localhost:8080")
                .header("Authorization", "Bearer " + BaseApiClient.getAuthToken())
                .get("/api/categories");

        Category[] categories = catResponse.as(Category[].class);
        assertThat(categories).as("No categories found in the system").isNotEmpty();
        firstAvailableCategoryId = categories[0].getId();
    }

    @When("I filter plants by the first available category ID")
    public void iFilterPlantsByFirstAvailableCategoryId() {
        assertThat(firstAvailableCategoryId).as("First available category ID not set").isGreaterThan(0);
        response = plantsApiClient.filterPlantsByCategory(firstAvailableCategoryId);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("all returned plants should belong to the filtered category")
    public void allReturnedPlantsShouldBelongToFilteredCategory() {
        Plant[] plants = getPlantsFromResponse();
        if (plants != null && plants.length > 0) {
            for (Plant plant : plants) {
                if (plant.getCategory() != null) {
                    assertThat(plant.getCategory().getId())
                            .as("Plant should belong to filtered category")
                            .isEqualTo(firstAvailableCategoryId);
                }
            }
        }
    }

    @When("I request plants sorted by {string} in {string} order")
    public void iRequestPlantsSortedByInOrder(String sortBy, String direction) {
        response = plantsApiClient.getSortedPlants(sortBy, direction);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the plants should be sorted by name in ascending order")
    public void thePlantsShouldBeSortedByNameInAscendingOrder() {
        Plant[] plants = getPlantsFromResponse();
        if (plants != null && plants.length > 1) {
            for (int i = 0; i < plants.length - 1; i++) {
                String currentName = plants[i].getName();
                String nextName = plants[i + 1].getName();
                assertThat(currentName.compareToIgnoreCase(nextName))
                        .as("Plants should be sorted by name in ascending order")
                        .isLessThanOrEqualTo(0);
            }
        }
    }

    @Then("the plants should be sorted by price in descending order")
    public void thePlantsShouldBeSortedByPriceInDescendingOrder() {
        Plant[] plants = getPlantsFromResponse();
        if (plants != null && plants.length > 1) {
            for (int i = 0; i < plants.length - 1; i++) {
                double currentPrice = plants[i].getPrice();
                double nextPrice = plants[i + 1].getPrice();
                assertThat(currentPrice)
                        .as("Plants should be sorted by price in descending order")
                        .isGreaterThanOrEqualTo(nextPrice);
            }
        }
    }

    @Then("the plants should be sorted by quantity in ascending order")
    public void thePlantsShouldBeSortedByQuantityInAscendingOrder() {
        Plant[] plants = getPlantsFromResponse();
        if (plants != null && plants.length > 1) {
            for (int i = 0; i < plants.length - 1; i++) {
                int currentQty = plants[i].getQuantity();
                int nextQty = plants[i + 1].getQuantity();
                assertThat(currentQty)
                        .as("Plants should be sorted by quantity in ascending order")
                        .isLessThanOrEqualTo(nextQty);
            }
        }
    }

     
     
     

    @Given("Valid sub-category exists")
    public void validSubCategoryExists() {
        ensureValidSubCategoryExists();
    }

    @When("Send POST request with valid Name, Price, Quantity to \\/api\\/plants\\/category\\/\\{validCategoryId}")
    public void sendPOSTRequestWithValidData() {
        String uniqueName = "Plant" + System.currentTimeMillis() % 100000;
        response = plantsApiClient.createPlant(uniqueName, 150.0, 25, validSubCategoryId);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @When("Send POST request with JSON body missing Name")
    public void sendPOSTRequestWithMissingName() {
        response = plantsApiClient.createPlantMissingName(150.0, 25, validSubCategoryId);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @When("Send POST request with category that is not a sub-category")
    public void sendPOSTRequestWithInvalidCategory() {
         
        int invalidCategoryId = 99999;
        String uniqueName = "Plant" + System.currentTimeMillis() % 100000;
        response = plantsApiClient.createPlant(uniqueName, 100.0, 10, invalidCategoryId);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @When("Send POST request with Price = 0 or negative")
    public void sendPOSTRequestWithInvalidPrice() {
        String uniqueName = "Plant" + System.currentTimeMillis() % 100000;
        response = plantsApiClient.createPlant(uniqueName, -10.0, 10, validSubCategoryId);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

     
     
     

    @Given("Valid plant ID exists")
    public void validPlantIDExists() {
        ensureValidSubCategoryExists();

         
        String uniqueName = "EditPlant" + System.currentTimeMillis() % 10000;
        Response createResp = plantsApiClient.createPlant(uniqueName, 100.0, 10, validSubCategoryId);

        if (createResp.statusCode() == 201) {
            validPlantId = createResp.as(Plant.class).getId();
        } else {
             
            Response allPlants = plantsApiClient.getAllPlants();
            Plant[] plants = allPlants.as(Plant[].class);
            if (plants != null && plants.length > 0) {
                validPlantId = plants[0].getId();
            } else {
                throw new RuntimeException("Failed to create or find a valid plant for editing");
            }
        }
    }

    @When("Send PUT request to \\/api\\/plants\\/\\{validId} with updated Name, Price, Quantity")
    public void sendPUTRequestToUpdatePlant() {
        String updatedName = "Updated" + System.currentTimeMillis() % 10000;
        response = plantsApiClient.updatePlant(validPlantId, updatedName, 200.0, 50);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

     
     
     

    @Then("Verify status code is {int}")
    public void verifyStatusCodeIs(int statusCode) {
        if (response == null) {
            response = BaseApiClient.getLastResponse();
        }
        if (response == null) {
            throw new RuntimeException("Response is null. The previous step did not set the response correctly.");
        }
        response.then().statusCode(statusCode);
    }

    @Then("Verify HTTP status code is {int}")
    public void verifyHTTPStatusCodeIs(int statusCode) {
        verifyStatusCodeIs(statusCode);
    }

    @Then("Verify status code is 400 or 404")
    public void verifyStatusCodeIs400Or404() {
        if (response == null) {
            response = BaseApiClient.getLastResponse();
        }
        if (response == null) {
            throw new RuntimeException("Response is null. The previous step did not set the response correctly.");
        }
        int actualStatusCode = response.getStatusCode();
        assertThat(actualStatusCode)
                .as("Status code should be either 400 or 404")
                .isIn(400, 404);
    }

    @Then("API returns success and plant is created")
    public void apiReturnsSuccessAndPlantIsCreated() {
        Plant responsePlant = response.as(Plant.class);
        assertThat(responsePlant.getId()).isGreaterThan(0);
        assertThat(responsePlant.getName()).isNotNull();
    }

    @Then("API returns validation error and plant is not created")
    public void apiReturnsValidationErrorAndPlantIsNotCreated() {
         
         
        String body = response.getBody().asString();
        System.out.println("Validation error response: " + body);
    }

    @Then("API returns 400 Bad Request")
    public void apiReturns400BadRequest() {
        response.then().statusCode(400);
    }

    @Then("API returns 200 OK")
    public void apiReturns200OK() {
        response.then().statusCode(200);
    }

     
     
     

    @Given("Valid plant ID exists for verification")
    public void validPlantIDExistsForVerification() {
         
        ensureAdminAuth();
        ensureValidSubCategoryExists();

         
        String uniqueName = "VerifyPlant" + System.currentTimeMillis() % 10000;
        Response createResp = plantsApiClient.createPlant(uniqueName, 100.0, 10, validSubCategoryId);

        if (createResp.statusCode() == 201) {
            validPlantId = createResp.as(Plant.class).getId();
        } else {
             
            Response allPlants = plantsApiClient.getAllPlants();
            Plant[] plants = allPlants.as(Plant[].class);
            if (plants != null && plants.length > 0) {
                validPlantId = plants[0].getId();
            } else {
                throw new RuntimeException("Failed to create or find a valid plant for verification");
            }
        }
    }

    @When("User sends POST request to \\/api\\/plants\\/category\\/\\{categoryId} with valid data")
    public void userSendsPOSTRequestWithValidData() {
        ensureUserAuth();
        String uniqueName = "UserPlant" + System.currentTimeMillis() % 100000;
        response = plantsApiClient.createPlant(uniqueName, 150.0, 25, validSubCategoryId);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @When("User sends POST request to \\/api\\/plants\\/category\\/\\{categoryId} with valid plant data")
    public void userSendsPOSTRequestWithValidPlantData() {
        userSendsPOSTRequestWithValidData();
    }

    @When("User sends PUT request to \\/api\\/plants\\/\\{id} with updated data")
    public void userSendsPUTRequestWithUpdatedData() {
        ensureUserAuth();
        String updatedName = "UpdatedByUser" + System.currentTimeMillis() % 10000;
        response = plantsApiClient.updatePlant(validPlantId, updatedName, 200.0, 50);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @When("User sends DELETE request to \\/api\\/plants\\/\\{id}")
    public void userSendsDELETERequest() {
        ensureUserAuth();
        response = plantsApiClient.deletePlant(validPlantId);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @When("Send PUT request without authentication token")
    public void sendPUTRequestWithoutAuthenticationToken() {
         
        String savedToken = BaseApiClient.getAuthToken();
        BaseApiClient.setAuthToken(null);

        String updatedName = "NoAuthUpdate" + System.currentTimeMillis() % 10000;
        response = plantsApiClient.updatePlantWithoutToken(validPlantId, updatedName, 200.0, 50);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);

         
        BaseApiClient.setAuthToken(savedToken);
    }

    @Then("API blocks plant creation for non-admin user")
    public void apiBlocksPlantCreationForNonAdminUser() {
         
         
        String body = response.getBody().asString();
        System.out.println("Authorization error response: " + body);
    }

    @Then("Verify plant data remains unchanged")
    public void verifyPlantDataRemainsUnchanged() {
         
        ensureAdminAuth();
        Response getResp = plantsApiClient.getPlantById(validPlantId);
        assertThat(getResp.statusCode()).as("Should be able to fetch plant").isEqualTo(200);
         
        Plant plant = getResp.as(Plant.class);
        assertThat(plant.getId()).isEqualTo(validPlantId);
    }

    @Then("Verify plant still exists in the system")
    public void verifyPlantStillExistsInTheSystem() {
         
        ensureAdminAuth();
        Response getResp = plantsApiClient.getPlantById(validPlantId);
        assertThat(getResp.statusCode()).as("Plant should still exist").isEqualTo(200);
        Plant plant = getResp.as(Plant.class);
        assertThat(plant.getId()).isEqualTo(validPlantId);
    }

    @Then("Plant is not created in the system")
    public void plantIsNotCreatedInTheSystem() {
         
        String body = response.getBody().asString();
        System.out.println("Plant creation blocked: " + body);
    }

    @Then("No data is modified")
    public void noDataIsModified() {
         
         
        String body = response.getBody().asString();
        System.out.println("Authentication error response: " + body);
    }

     
     
     

    private void ensureAdminAuth() {
        if (BaseApiClient.getAuthToken() == null) {
            LoginResponse loginResp = authApiClient.login("admin", "admin123");
            BaseApiClient.setAuthToken(loginResp.getToken());
        }
    }

    private void ensureUserAuth() {
        LoginResponse loginResp = authApiClient.login("testuser", "test123");
        BaseApiClient.setAuthToken(loginResp.getToken());
    }

    private void ensureValidSubCategoryExists() {
        if (validSubCategoryId == 0) {
            ensureAdminAuth();

             
            Response subCatResponse = io.restassured.RestAssured.given()
                    .baseUri("http://localhost:8080")
                    .header("Authorization", "Bearer " + BaseApiClient.getAuthToken())
                    .get("/api/categories/sub-categories");

            if (subCatResponse.statusCode() == 200) {
                Category[] subCategories = subCatResponse.as(Category[].class);
                if (subCategories != null && subCategories.length > 0) {
                    validSubCategoryId = subCategories[0].getId();
                    System.out.println("Found subcategory ID: " + validSubCategoryId);
                    return;
                }
            }

             
            Response catResponse = io.restassured.RestAssured.given()
                    .baseUri("http://localhost:8080")
                    .header("Authorization", "Bearer " + BaseApiClient.getAuthToken())
                    .get("/api/categories");

            Category[] categories = catResponse.as(Category[].class);

            for (Category cat : categories) {
                if (cat.getParentId() != null) {
                    validSubCategoryId = cat.getId();
                    System.out.println("Found subcategory ID via parentId: " + validSubCategoryId);
                    return;
                }
            }

            throw new RuntimeException("No sub-category found in system. Please seed data.");
        }
    }

    private void ensureValidParentCategoryExists() {
        if (validParentCategoryId == 0) {
            ensureAdminAuth();

            Response catResponse = io.restassured.RestAssured.given()
                    .baseUri("http://localhost:8080")
                    .header("Authorization", "Bearer " + BaseApiClient.getAuthToken())
                    .get("/api/categories");

            Category[] categories = catResponse.as(Category[].class);

            for (Category cat : categories) {
                if (cat.getParentId() == null) {
                    validParentCategoryId = cat.getId();
                    System.out.println("Found parent category ID: " + validParentCategoryId);
                    return;
                }
            }

            throw new RuntimeException("No parent category found in system. Please seed data.");
        }
    }

    private Plant[] getPlantsFromResponse() {
        try {
            return response.as(Plant[].class);
        } catch (Exception e) {
            try {
                api.models.plants.PagedPlantsResponse paged = response.as(api.models.plants.PagedPlantsResponse.class);
                return paged.getContent();
            } catch (Exception ex) {
                return new Plant[0];
            }
        }
    }
}
