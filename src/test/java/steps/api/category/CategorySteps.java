package steps.api.category;

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

    private Response response;
    private Category createdCategory;

    @Given("a category exists with name {string}")
    public void aCategoryExistsWithName(String name) {
        // Check if exists, if not create
        Response listResp = categoryApiClient.getAllCategories();
        Category[] categories = listResp.as(Category[].class);
        
        boolean exists = Arrays.stream(categories)
                .anyMatch(c -> name.equals(c.getName()));
        
        if (!exists) {
            Category newCategory = new Category();
            newCategory.setName(name);
            categoryApiClient.createCategory(newCategory);
        }
    }

    @When("I create a category with name {string}")
    public void iCreateACategoryWithName(String name) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        response = categoryApiClient.createCategory(body);
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
        
        Category oldCategory = Arrays.stream(categories)
                .filter(c -> "OldCat".equals(c.getName()))
                .findFirst()
                .orElse(null);
        
        if (oldCategory != null) {
            Map<String, Object> updatePayload = new HashMap<>();
            updatePayload.put("name", newName);
            response = categoryApiClient.updateCategory(oldCategory.getId(), updatePayload);
            BaseApiClient.setLastResponse(response);
        } else {
            throw new RuntimeException("Category 'OldCat' not found for update test");
        }
    }

    @When("I delete the category")
    public void iDeleteTheCategory() {
        // Similar to update, we need to know WHICH category.
        // Feature: And a category exists with name "ToDelete"
        // When I delete the category
        
        Response listResp = categoryApiClient.getAllCategories();
        Category[] categories = listResp.as(Category[].class);
        
        Category toDelete = Arrays.stream(categories)
                .filter(c -> "DelCat".equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Category 'DelCat' not found"));
                
        response = categoryApiClient.deleteCategory(toDelete.getId());
        BaseApiClient.setLastResponse(response);
    }

    @Then("the response body should return correct category details with name {string}")
    public void theResponseBodyShouldReturnCorrectCategoryDetailsWithName(String name) {
        Category category = response.as(Category.class);
        assertThat(category.getName()).isEqualTo(name);
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

    @Then("the category should no longer exist")
    public void theCategoryShouldNoLongerExist() {
         // Verify deletion
         // We can't easily check 'ID' if we didn't store it, 
         // but we can check if "ToDelete" is gone.
         
         Response listResp = categoryApiClient.getAllCategories();
         Category[] categories = listResp.as(Category[].class);
         
         assertThat(categories).extracting(Category::getName).doesNotContain("ToDelete");
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
}
