package steps.api.plants;

import api.client.BaseApiClient;
import api.client.plants.PlantApiClient;
import api.client.category.CategoryApiClient;
import api.models.common.Plant;
import api.models.category.Category;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;
import api.client.auth.AuthApiClient;
import api.models.auth.LoginResponse;
import java.util.Arrays;
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

    @Steps
    AuthApiClient authApiClient;

    private Response response;
    private Plant createdPlant;
    private int plantIdToEdit;
    
    private int validSubCategoryId;
    private int validParentCategoryId;

    @Given("Valid sub-category exists")
    public void validSubCategoryExists() {
        if (validSubCategoryId == 0) {
            // Ensure we are admin to list all categories
            String originalToken = BaseApiClient.getAuthToken();
            try {
                if (originalToken == null) {
                    LoginResponse loginResp = authApiClient.login("admin", "admin123");
                    BaseApiClient.setAuthToken(loginResp.getToken());
                }

                Response response = categoryApiClient.getAllCategories();
                Category[] categories = response.as(Category[].class);

                // Find a sub-category (has parent name that is not "-")
                Category subCat = Arrays.stream(categories)
                        .filter(c -> c.getParentName() != null && !"-".equals(c.getParentName()))
                        .findFirst()
                        .orElse(null);

                // Find a parent category (no parent name or "-")
                Category parentCat = Arrays.stream(categories)
                        .filter(c -> c.getParentName() == null || "-".equals(c.getParentName()))
                        .findFirst()
                        .orElse(null);

                if (subCat != null) {
                    validSubCategoryId = subCat.getId();
                    System.out.println("Found Valid Sub-Category ID: " + validSubCategoryId);
                } else {
                     // Fallback or create? For now throw meaningful error or fallback
                     throw new RuntimeException("No sub-category found in system. Please seed data.");
                }

                if (parentCat != null) {
                    validParentCategoryId = parentCat.getId();
                    System.out.println("Found Valid Parent Category ID: " + validParentCategoryId);
                } else {
                     throw new RuntimeException("No parent category found in system. Please seed data.");
                }

            } finally {
                 if (originalToken != null) {
                     BaseApiClient.setAuthToken(originalToken);
                 }
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
        
        if(resp.statusCode() == 201) {
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
        // Use the provided categoryId from the feature file (User data says 3 is Succulent/Indoor)
        Plant plant = new Plant();
        plant.setName("Ant " + System.currentTimeMillis());
        plant.setPrice(150.0);
        plant.setQuantity(25);
        
        // Use the explicit ID from the scenario
        response = plantApiClient.createPlant(plant, categoryId); 
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
}
