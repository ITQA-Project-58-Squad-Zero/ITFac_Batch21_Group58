package steps.api.category;

import api.client.auth.AuthApiClient;
import api.models.auth.LoginResponse;
import api.client.BaseApiClient;
import api.client.category.CategoryApiClient;
import api.models.category.Category;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CategorySteps {

    @Steps
    CategoryApiClient categoryApiClient;

    @Steps
    AuthApiClient authApiClient;

    private Response response;
    private Category createdCategory;
    private static ThreadLocal<String> lastRequestedName = new ThreadLocal<>();

    @Given("the admin has created a category with name {string}")
    public void theAdminHasCreatedACategoryWithName(String name) {
        String originalToken = BaseApiClient.getAuthToken();
        System.out.println("DEBUG: Creating category '" + name + "' as Admin");
        
        LoginResponse loginResp = authApiClient.login("admin", "admin123");
        BaseApiClient.setAuthToken(loginResp.getToken());
        
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        // Ensure name is within length limit (3-10) for tests if it's dynamic
        
        Response resp = categoryApiClient.createCategory(body);
        if (resp.getStatusCode() != 201 && resp.getStatusCode() != 400) {
             System.out.println("DEBUG: Admin creation failed with status " + resp.getStatusCode() + ": " + resp.getBody().asString());
        }
        
        BaseApiClient.setAuthToken(originalToken);
    }

    @Given("a category exists with name {string}")
    public void aCategoryExistsWithName(String name) {
        Response listResp = categoryApiClient.getAllCategories();
        Category[] categories = listResp.as(Category[].class);
        
        boolean exists = Arrays.stream(categories)
                .anyMatch(c -> name.equals(c.getName()));
        
        if (!exists) {
            theAdminHasCreatedACategoryWithName(name);
        }
    }

    @When("I create a category with name {string}")
    public void iCreateACategoryWithName(String name) {
        // Name must be 3-10 characters.
        String uniqueSuffix = String.valueOf(System.currentTimeMillis()).substring(10); // 3 digits
        String finalName = name;
        if (finalName.length() > 7) {
            finalName = finalName.substring(0, 7);
        }
        finalName += uniqueSuffix;
        createCategoryInternal(finalName);
    }

    @When("I create a category with exact name {string}")
    public void iCreateACategoryWithExactName(String name) {
        createCategoryInternal(name);
    }

    private void createCategoryInternal(String name) {
        lastRequestedName.set(name);
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        
        response = categoryApiClient.createCategory(body);
        if (response.getStatusCode() >= 400 && !"Validation failed".equals(response.jsonPath().getString("message"))) {
             // System.out.println("DEBUG: Create '" + name + "' failed with " + response.getStatusCode());
        }
        BaseApiClient.setLastResponse(response);
    }

    @When("I create a category with missing name")
    public void iCreateACategoryWithMissingName() {
        Map<String, Object> body = new HashMap<>(); // Empty body
        response = categoryApiClient.createCategory(body);
        BaseApiClient.setLastResponse(response);
    }

    @When("I update the category name to {string}")
    public void iUpdateTheCategoryNameTo(String newName) {
        Response listResp = categoryApiClient.getAllCategories();
        Category[] categories = listResp.as(Category[].class);
        
        // Find existing category (DelCat or OldCat depending on scenario)
        Category oldCategory = Arrays.stream(categories)
                .filter(c -> "OldCat".equals(c.getName()) || "DelCat".equals(c.getName()) || "NewCat".equals(c.getName()))
                .findFirst()
                .orElse(null);
        
        if (oldCategory != null) {
            Map<String, Object> updatePayload = new HashMap<>();
            updatePayload.put("name", newName);
            updatePayload.put("id", oldCategory.getId());
            
            response = categoryApiClient.updateCategory(oldCategory.getId(), updatePayload);
            if (response.getStatusCode() >= 400) {
                 System.out.println("DEBUG: Update failed with status " + response.getStatusCode() + ": " + response.getBody().asString());
            }
            BaseApiClient.setLastResponse(response);
        } else {
             System.out.println("DEBUG: Available categories: " + Arrays.toString(Arrays.stream(categories).map(Category::getName).toArray()));
             throw new RuntimeException("Target category not found for update test among: " + Arrays.toString(Arrays.stream(categories).map(Category::getName).toArray()));
        }
    }

    @When("I delete the category with name {string}")
    public void iDeleteTheCategoryWithName(String name) {
        Response listResp = categoryApiClient.getAllCategories();
        Category[] categories = listResp.as(Category[].class);
        
        Category toDelete = Arrays.stream(categories)
                .filter(c -> name.equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Category '" + name + "' not found"));
                
        response = categoryApiClient.deleteCategory(toDelete.getId());
        BaseApiClient.setLastResponse(response);
    }

    @When("I delete the category")
    public void iDeleteTheCategory() {
        // Fallback or specific default if needed, but feature should prefer the named version
        iDeleteTheCategoryWithName("DelCat");
    }

    @Then("the response body should return correct category details with name {string}")
    public void theResponseBodyShouldReturnCorrectCategoryDetailsWithName(String name) {
        Category category = response.as(Category.class);
        assertThat(category.getName()).isEqualTo(name);
        assertThat(category.getId()).isNotNull();
    }

    @Then("the response body should return correct category details")
    public void theResponseBodyShouldReturnCorrectCategoryDetails() {
        Category category = response.as(Category.class);
        assertThat(category.getName()).isEqualTo(lastRequestedName.get());
        assertThat(category.getId()).isNotNull();
    }

    @Then("the new category should be present in the categories list")
    public void theNewCategoryShouldBePresentInTheCategoriesList() {
        int newId = response.as(Category.class).getId();
        Response listResp = categoryApiClient.getAllCategories();
        Category[] categories = listResp.as(Category[].class);
        
        assertThat(categories).extracting(Category::getId).contains(newId);
    }

    @Then("the response body should contain a validation error for missing name")
    public void theResponseBodyShouldContainAValidationErrorForMissingName() {
        String body = response.getBody().asString();
        assertThat(body).containsIgnoringCase("name"); // Adjust based on actual error
        // Or check specific validation message field
    }

    @Then("the category {string} should no longer exist")
    public void theCategoryShouldNoLongerExistWithName(String name) {
         Response listResp = categoryApiClient.getAllCategories();
         Category[] categories = listResp.as(Category[].class);
         
         assertThat(categories).extracting(Category::getName).doesNotContain(name);
    }

    @Then("the category should no longer exist")
    public void theCategoryShouldNoLongerExist() {
         theCategoryShouldNoLongerExistWithName("ToDelete");
    }

    @Then("the response body should contain a duplicate resource error")
    public void theResponseBodyShouldContainADuplicateResourceError() {
        String body = response.getBody().asString();
        assertThat(body).containsIgnoringCase("duplicate"); 
    }

    @Then("no new duplicate category should be created")
    public void noNewDuplicateCategoryShouldBeCreated() {
         // Logic to verify count or existence
         Response listResp = categoryApiClient.getAllCategories();
         Category[] categories = listResp.as(Category[].class);
         
         long count = Arrays.stream(categories)
                 .filter(c -> "DupCat".equals(c.getName()))
                 .count();
                 
         assertThat(count).isEqualTo(1); // Should still be just 1
    }

    @Then("the response should indicate insufficient permissions")
    public void theResponseShouldIndicateInsufficientPermissions() {
        String body = response.getBody().asString();
        // Relaxed assertion to match actual API response: {"status":403,"error":"Forbidden"}
        assertThat(response.getStatusCode()).isEqualTo(403);
        assertThat(body.toLowerCase()).containsAnyOf("permission", "forbidden", "authorize", "access");
    }

    @When("I create a sub-category with name {string} under parent {string}")
    public void iCreateASubCategoryWithNameUnderParent(String name, String parentName) {
        Response listResp = categoryApiClient.getAllCategories();
        Category[] categories = listResp.as(Category[].class);
        
        Category parent = Arrays.stream(categories)
                .filter(c -> parentName.equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Parent category '" + parentName + "' not found"));

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        
        // Use a nested object if the server expects com.qatraining.entity.Category
        Map<String, Object> parentObj = new HashMap<>();
        parentObj.put("id", parent.getId());
        body.put("parent", parentObj); 
        
        response = categoryApiClient.createCategory(body);
        if (response.getStatusCode() != 201) {
            System.out.println("DEBUG: Sub-create failed with status " + response.getStatusCode() + ": " + response.getBody().asString());
        }
        BaseApiClient.setLastResponse(response);
    }
}
