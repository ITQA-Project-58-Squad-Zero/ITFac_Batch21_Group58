package steps.ui.categories.categories_anjana;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.categories.categories_anjana.CategoriesPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoriesSteps {

    CategoriesPage categoriesPage;

    @When("Admin clicks the \"Add A Category\" button")
    public void clickAddCategoryButton() {
        categoriesPage.clickAddCategoryButton();
    }

    @Then("the \"Add Category\" page should open successfully")
    public void verifyAddCategoryPageOpens() {
        assertTrue(categoriesPage.isAddCategoryPageDisplayed(), 
                "Add Category page did not open");
    }

    @When("Admin leaves the Category Name empty")
    public void leaveCategoryNameEmpty() {
        categoriesPage.leaveCategoryNameEmpty();
    }

    private String currentCategoryName;

    @When("Admin enters Category Name {string}")
    public void enterCategoryName(String name) {
        if ("TestCat".equals(name)) {
            // Generate a unique name shorter than 10 characters (e.g., Cat12345)
            name = "Cat" + (System.currentTimeMillis() % 100000);
        }
        currentCategoryName = name;
        categoriesPage.enterCategoryName(name);
    }

    @When("Admin clicks the Save button")
    public void clickSaveButton() {
        categoriesPage.clickSaveButton();
    }

    @Then("Admin should see validation message {string}")
    public void verifyValidationMessage(String expectedMessage) {
        assertTrue(categoriesPage.isValidationMessageDisplayed(), 
                "Validation message is not displayed");
        assertTrue(categoriesPage.getValidationMessage().contains(expectedMessage),
                "Expected message: " + expectedMessage + " but got: " + categoriesPage.getValidationMessage());
    }

    @Then("Category {string} should be created successfully and appear in the category list")
    public void verifyCategoryCreated(String name) {
        String nameToCheck = name;
        if ("TestCat".equals(name) && currentCategoryName != null) {
            nameToCheck = currentCategoryName;
        }

        if (categoriesPage.isValidationMessageDisplayed()) {
            throw new AssertionError("Category creation failed with message: " + categoriesPage.getValidationMessage());
        }

        assertTrue(categoriesPage.isCategoryDisplayed(nameToCheck),
                "Category " + nameToCheck + " was not found in the list");
    }

    @When("Admin clicks the Cancel button")
    public void clickCancelButton() {
        categoriesPage.clickCancelButton();
    }

    @Then("Admin should be redirected to the Categories list page")
    public void verifyRedirectToCategoryList() {
        assertTrue(categoriesPage.isCategoryListDisplayed(), "Category list page is not displayed");
    }

    @Given("User is logged in")
    public void userIsLoggedIn() {
        // Handled by @login_as_user hook
    }

    @Then("the \"Add A Category\" button should not be visible")
    public void verifyAddCategoryButtonNotVisible() {
        assertTrue(categoriesPage.isAddCategoryButtonNotVisible(), "Add Category button is visible but should not be");
    }

    @When("User attempts to access the Add Category page directly")
    public void attemptDirectAccess() {
        String baseUrl = categoriesPage.getDriver().getCurrentUrl().split("/ui")[0]; 
        if (baseUrl.isEmpty()) baseUrl = "http://localhost:8080"; // Fallback
        categoriesPage.navigateToUrl(baseUrl + "/ui/categories/add");
    }

    @Then("User should be redirected to Access Denied page or Login page")
    public void verifyAccessDenied() {
        assertTrue(categoriesPage.isAccessDenied(), "User was not blocked from accessing the page");
    }

    @Then("the Edit option should not be visible for any category")
    public void verifyEditOptionHidden() {
        assertTrue(categoriesPage.areEditButtonsHidden(), "Edit option is visible but should not be");
    }

    @When("User attempts to update an existing category")
    public void attemptCategoryUpdate() {
        String baseUrl = categoriesPage.getDriver().getCurrentUrl().split("/ui")[0];
        if (baseUrl.isEmpty()) baseUrl = "http://localhost:8080";
        categoriesPage.navigateToUrl(baseUrl + "/ui/categories/edit/1");
    }

    @Then("the update action should be blocked")
    public void verifyUpdateBlocked() {
        assertTrue(categoriesPage.isUpdateBlocked(), "Update was not blocked");
    }

    @Then("the Delete option should not be visible for any category")
    public void verifyDeleteOptionHidden() {
        assertTrue(categoriesPage.areDeleteButtonsHidden(), "Delete option is visible but should not be");
    }

    // Unique category name generation
    private String uniqueCategoryName;

    @When("Admin enters a unique Category Name")
    public void enterUniqueCategoryName() {
        // Generate a unique name within 3-10 character limit
        uniqueCategoryName = "Cat" + (System.currentTimeMillis() % 100000);
        currentCategoryName = uniqueCategoryName;
        categoriesPage.enterCategoryName(uniqueCategoryName);
    }

    @Then("the unique category should be created successfully and appear in the category list")
    public void verifyUniqueCategoryCreated() {
        assertTrue(uniqueCategoryName != null, "Unique category name not set");
        
        if (categoriesPage.isValidationMessageDisplayed()) {
            throw new AssertionError("Category creation failed with message: " + categoriesPage.getValidationMessage());
        }

        assertTrue(categoriesPage.isCategoryDisplayed(uniqueCategoryName),
                "Category '" + uniqueCategoryName + "' was not found in the list");
    }
}
