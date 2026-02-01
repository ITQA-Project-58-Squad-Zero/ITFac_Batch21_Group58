package steps.ui.category;

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

    @When("Admin enters Category Name {string}")
    public void enterCategoryName(String name) {
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
}
