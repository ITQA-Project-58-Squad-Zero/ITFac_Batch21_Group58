package steps.ui.plant_add_edit;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.plant_add_edit.AddPlantPage;
import pages.plant_add_edit.PlantsPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
