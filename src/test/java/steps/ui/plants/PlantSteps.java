package steps.ui.plants;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.plants.AddPlantPage;
import pages.plants.PlantsPage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;

public class PlantSteps {

    PlantsPage plantsPage;
    AddPlantPage addPlantPage;

    @When("Admin clicks the \"Add a Plant\" button")
    public void clickAddPlantButton() {
        plantsPage.clickAddPlantButton();
    }

    @Then("the \"Add Plant\" page should be displayed successfully")
    public void verifyAddPlantPageDisplayed() {
        assertTrue(addPlantPage.isPageOpen(), "Add Plant page is not displayed");
    }

    @When("Admin enters a \"Plant Name\" with more than 25 characters")
    public void enterLongPlantName() {
        addPlantPage.enterPlantName("ThisPlantNameIsWayTooLongToBeAcceptedByTheSystem");
    }

    @When("Admin enters a \"Plant Name\" with less than 3 characters")
    public void enterShortPlantName() {
        addPlantPage.enterPlantName("Ab");
    }

    @When("Admin fills remaining fields with valid data")
    public void fillRemainingFields() {
        addPlantPage.selectCategory("category2");
        addPlantPage.enterPrice("50.00");
        addPlantPage.enterQuantity("10");
    }

    @When("Admin clicks the \"Save\" button")
    public void clickSave() {
        addPlantPage.clickSave();
    }

    @Then("the error message {string} should be displayed")
    public void verifyErrorMessage(String message) {
        assertTrue(addPlantPage.isErrorMessageDisplayed(message), "Error message '" + message + "' is not displayed");
    }

    @When("Admin leaves all mandatory fields empty")
    public void leaveAllMandatoryFieldsEmpty() {
        addPlantPage.clearAllFields();
    }

    @Then("the following error messages should be displayed")
    public void verifyErrorMessages(List<String> messages) {
        for (String message : messages) {
            assertTrue(addPlantPage.isErrorMessageDisplayed(message), "Error message '" + message + "' is not displayed");
        }
    }

    @When("Admin enters a valid Plant Name")
    public void enterValidPlantName() {
        addPlantPage.enterPlantName("Valid Plant Name");
    }

    @When("Admin selects a valid Category")
    public void selectValidCategory() {
        addPlantPage.selectCategory("category2");
    }

    @When("Admin enters a Price of {string}")
    public void enterPrice(String price) {
        addPlantPage.enterPrice(price);
    }

    @When("Admin enters a valid Quantity")
    public void enterValidQuantity() {
        addPlantPage.enterQuantity("10");
    }

    @Then("the \"Add a Plant\" button should not be visible")
    public void verifyAddPlantButtonNotVisible() {
        plantsPage.shouldAddPlantButtonNotBeVisible();
    }

    @Then("the \"Edit\" button should not be visible")
    public void verifyEditButtonNotVisible() {
        plantsPage.shouldEditButtonNotBeVisible();
    }
}
