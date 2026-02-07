package steps.api.categories;

import api.client.BaseApiClient;
import api.client.categories.CategoriesApiClient;
import api.context.ApiResponseContext;
import api.models.common.Category;
import api.models.categories.CategorySummary;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoriesSteps {

    @Steps
    CategoriesApiClient categoriesApiClient;

    private Response response;

    @Given("a category exists with ID {int}")
    public void aCategoryExistsWithId(int id) {
        // Precondition: category is assumed to exist (seeded or created)
    }

    @When("I request all categories with no name or parentId filter")
    public void iRequestAllCategoriesWithNoFilter() {
        response = categoriesApiClient.getAllCategories();
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the response body should be a list of categories")
    public void theResponseBodyShouldBeAListOfCategories() {
        Category[] categories = response.as(Category[].class);
        assertThat(categories).isNotNull();
        // Response is array/list (can be empty)
        assertThat(categories).as("Response should be an array/list").isNotNull();
    }

    @When("I request the category by ID {int}")
    public void iRequestTheCategoryById(int id) {
        response = categoriesApiClient.getCategoryById(id);
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the response body should return the category with id {int}")
    public void theResponseBodyShouldReturnTheCategoryWithId(int id) {
        Category category = response.as(Category.class);
        assertThat(category).isNotNull();
        assertThat(category.getId()).isEqualTo(id);
    }

    @When("I request main categories")
    public void iRequestMainCategories() {
        response = categoriesApiClient.getMainCategories();
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("each returned category should have no parent")
    public void eachReturnedCategoryShouldHaveNoParent() {
        Category[] categories = response.as(Category[].class);
        assertThat(categories).isNotNull();
        for (Category cat : categories) {
            // Main categories have no parent: parentId null and/or parent null
            boolean noParent = (cat.getParentId() == null) && (cat.getParent() == null);
            assertThat(noParent)
                    .as("Main category id=%d name=%s should have no parent (parentId=%s, parent=%s)",
                            cat.getId(), cat.getName(), cat.getParentId(), cat.getParent())
                    .isTrue();
        }
    }

    @When("I request the categories summary")
    public void iRequestTheCategoriesSummary() {
        response = categoriesApiClient.getCategoriesSummary();
        BaseApiClient.setLastResponse(response);
        ApiResponseContext.setResponse(response);
    }

    @Then("the response body should contain mainCategories and subCategories")
    public void theResponseBodyShouldContainMainAndSubCategoryFields() {
        CategorySummary summary = response.as(CategorySummary.class);
        assertThat(summary).isNotNull();
        assertThat(summary.getMainCategories()).as("mainCategories field should be present").isGreaterThanOrEqualTo(0);
        assertThat(summary.getSubCategories()).as("subCategories field should be present").isGreaterThanOrEqualTo(0);
    }

    @Then("mainCategories and subCategories should be numeric and greater than or equal to zero")
    public void mainAndSubCategoriesShouldBeNumericAndNonNegative() {
        CategorySummary summary = response.as(CategorySummary.class);
        assertThat(summary.getMainCategories()).isGreaterThanOrEqualTo(0);
        assertThat(summary.getSubCategories()).isGreaterThanOrEqualTo(0);
    }
}
