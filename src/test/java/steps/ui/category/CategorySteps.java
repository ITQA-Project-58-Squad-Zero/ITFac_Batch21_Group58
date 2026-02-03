package steps.ui.category;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.category.CategoryPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategorySteps {

    CategoryPage categoryPage;

    @When("Admin clicks the \"Add A Category\" button")
    public void clickAddCategoryButton() {
        categoryPage.clickAddCategoryButton();
    }

    @Then("the \"Add Category\" page should open successfully")
    public void verifyAddCategoryPageOpens() {
        assertTrue(categoryPage.isAddCategoryPageDisplayed(), 
                "Add Category page did not open");
    }

    @When("Admin leaves the Category Name empty")
    public void leaveCategoryNameEmpty() {
        categoryPage.leaveCategoryNameEmpty();
    }

    private String currentCategoryName;

    @When("Admin enters Category Name {string}")
    public void enterCategoryName(String name) {
        if ("TestCat".equals(name)) {
            // Generate a unique name shorter than 10 characters (e.g., Cat12345)
            name = "Cat" + (System.currentTimeMillis() % 100000);
        }
        currentCategoryName = name;
        categoryPage.enterCategoryName(name);
    }

    @When("Admin clicks the Save button")
    public void clickSaveButton() {
        categoryPage.clickSaveButton();
    }

    @Then("Admin should see validation message {string}")
    public void verifyValidationMessage(String expectedMessage) {
        assertTrue(categoryPage.isValidationMessageDisplayed(), 
                "Validation message is not displayed");
        assertTrue(categoryPage.getValidationMessage().contains(expectedMessage),
                "Expected message: " + expectedMessage + " but got: " + categoryPage.getValidationMessage());
    }

    @Then("Category {string} should be created successfully and appear in the category list")
    public void verifyCategoryCreated(String name) {
        String nameToCheck = name;
        if ("TestCat".equals(name) && currentCategoryName != null) {
            nameToCheck = currentCategoryName;
        }

        if (categoryPage.isValidationMessageDisplayed()) {
            throw new AssertionError("Category creation failed with message: " + categoryPage.getValidationMessage());
        }

        assertTrue(categoryPage.isCategoryDisplayed(nameToCheck),
                "Category " + nameToCheck + " was not found in the list");
    }

    @When("Admin clicks the Cancel button")
    public void clickCancelButton() {
        categoryPage.clickCancelButton();
    }

    @Then("Admin should be redirected to the Categories list page")
    public void verifyRedirectToCategoryList() {
        assertTrue(categoryPage.isCategoryListDisplayed(), "Category list page is not displayed");
    }

    @Given("User is logged in")
    public void userIsLoggedIn() {
        // Handled by @login_as_user hook
    }

    @Then("the \"Add A Category\" button should not be visible")
    public void verifyAddCategoryButtonNotVisible() {
        assertTrue(categoryPage.isAddCategoryButtonNotVisible(), "Add Category button is visible but should not be");
    }

    @When("User attempts to access the Add Category page directly")
    public void attemptDirectAccess() {
        // Assuming base URL is handled or we use a relative path if supported, 
        // but getDriver().get() usually wants absolute. 
        // However, Serenity might handle relative if we use openAt? 
        // Let's rely on retrieving the base URL from environment or constructing it.
        // For simplicity and robustness given existing code style, we might try to construct it 
        // or just use the page default URL logic if possible, but the requirement is specific.
        // Let's assume the Serenity properties have the base URL.
        // Ideally: categoryPage.openUrl(categoryPage.getDriver().getCurrentUrl().replaceAll("/ui/categories.*", "") + "/ui/categories/add");
        // A safer bet locally if we don't know the exact host:
        String baseUrl = categoryPage.getDriver().getCurrentUrl().split("/ui")[0]; 
        if (baseUrl.isEmpty()) baseUrl = "http://localhost:8080"; // Fallback
        categoryPage.navigateToUrl(baseUrl + "/ui/categories/add");
    }

    @Then("User should be redirected to Access Denied page or Login page")
    public void verifyAccessDenied() {
        assertTrue(categoryPage.isAccessDenied(), "User was not blocked from accessing the page");
    }

    @Then("the Edit option should not be visible for any category")
    public void verifyEditOptionHidden() {
        assertTrue(categoryPage.areEditButtonsHidden(), "Edit option is visible but should not be");
    }

    @When("User attempts to update an existing category")
    public void attemptCategoryUpdate() {
        String baseUrl = categoryPage.getDriver().getCurrentUrl().split("/ui")[0];
        if (baseUrl.isEmpty()) baseUrl = "http://localhost:8080";
        // Attempt to access an edit page (assuming ID 1 exists or is just a test URL)
        categoryPage.navigateToUrl(baseUrl + "/ui/categories/edit/1");
    }

    @Then("the update action should be blocked")
    public void verifyUpdateBlocked() {
        assertTrue(categoryPage.isUpdateBlocked(), "Update was not blocked");
    }

    @Then("the Delete option should not be visible for any category")
    public void verifyDeleteOptionHidden() {
        assertTrue(categoryPage.areDeleteButtonsHidden(), "Delete option is visible but should not be");
    }
}
