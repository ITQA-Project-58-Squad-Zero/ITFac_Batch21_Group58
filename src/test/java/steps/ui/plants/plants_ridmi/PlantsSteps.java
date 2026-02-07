package steps.ui.plants.plants_ridmi;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.plants.AddPlantPage;
import pages.plants.EditPlantPage;
import pages.plants.PlantsPage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import api.client.BaseApiClient;

public class PlantsSteps {

    PlantsPage plantsPage;
    AddPlantPage addPlantPage;
    EditPlantPage editPlantPage;

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
        addPlantPage.selectCategory("Succulent");
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
        addPlantPage.selectCategory("Succulent");
    }

    @When("Admin enters a Price of {string}")
    public void enterPrice(String price) {
        addPlantPage.enterPrice(price);
    }

    @When("Admin enters a valid Quantity")
    public void enterValidQuantity() {
        addPlantPage.enterQuantity("10");
    }

    @Then("the \"Add a Plant\" button should be visible")
    public void verifyAddPlantButtonVisible() {
        assertTrue(plantsPage.isAddPlantButtonVisible(), "Add a Plant button should be visible for Admin");
    }

    @Then("the \"Add a Plant\" button should not be visible")
    public void verifyAddPlantButtonNotVisible() {
        plantsPage.shouldAddPlantButtonNotBeVisible();
    }

    @Then("the \"Edit\" button should not be visible")
    public void verifyEditButtonNotVisible() {
        plantsPage.shouldEditButtonNotBeVisible();
    }

    @Then("the \"Delete\" button should not be visible")
    public void verifyDeleteButtonNotVisible() {
        plantsPage.shouldDeleteButtonNotBeVisible();
    }

    @When("User navigates to the \"Add Plant\" page directly via URL")
    public void navigateToAddPlantDirectly() {
        addPlantPage.navigateToPage();
    }

    @Then("the \"Add Plant\" page should not be displayed")
    public void verifyAddPlantPageNotDisplayed() {
        assertTrue(!addPlantPage.isPageHeaderVisible(), "Add Plant page is displayed for non-admin user");
    }

    @When("User navigates to the \"Edit Plant\" page with ID {string} directly via URL")
    public void navigateToEditPlantDirectly(String id) {
        addPlantPage.getDriver().get(addPlantPage.getDriver().getCurrentUrl().split("/ui/")[0] + "/ui/plants/edit/" + id);
    }

    @Then("the \"Edit Plant\" page should not be displayed")
    public void verifyEditPlantPageNotDisplayed() {
        assertTrue(!editPlantPage.isPageHeaderVisible(), "Edit Plant page is displayed for non-admin user");
    }

    // Dynamic plant ID retrieval
    private int firstAvailablePlantId;
    
    @net.serenitybdd.annotations.Steps
    api.client.plants.PlantsApiClient plantsApiClient;
    
    @net.serenitybdd.annotations.Steps
    api.client.auth.AuthApiClient authApiClient;
    
    private net.thucydides.model.util.EnvironmentVariables environmentVariables;

    @io.cucumber.java.en.Given("a first available plant exists")
    public void aFirstAvailablePlantExists() {
        // Navigate to plants page and get first plant ID from the table or use API
        plantsPage.open();
        firstAvailablePlantId = plantsPage.getFirstPlantId();
        
        if (firstAvailablePlantId == 0) {
            // Seed data using API
            try {
                // Use shorter name to avoid validation errors (max 20-25 chars)
                String shortPlantName = "Plant" + (1000 + new java.util.Random().nextInt(9000));
                plantsApiClient.createPlant(shortPlantName, 50.0, 10, 1);
            } catch (AssertionError | Exception e) {
                 // Likely 403 Forbidden if running as User
                 // Switch to Admin
                 String userToken = BaseApiClient.getAuthToken();
                 
                 String adminUser = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                         .getProperty("credentials.admin.username");
                 String adminPass = net.serenitybdd.model.environment.EnvironmentSpecificConfiguration.from(environmentVariables)
                         .getProperty("credentials.admin.password");
                 
                 if (adminUser != null && adminPass != null) {
                     try {
                         api.models.auth.LoginResponse login = authApiClient.login(adminUser, adminPass);
                         BaseApiClient.setAuthToken(login.getToken());
                         
                         // Create Plant
                         String shortPlantNameAdmin = "Plant" + (1000 + new java.util.Random().nextInt(9000));
                         plantsApiClient.createPlant(shortPlantNameAdmin, 50.0, 10, 1);
                     } catch (Exception ex) {
                         System.err.println("Failed to seed plant data as admin: " + ex.getMessage());
                     } finally {
                         // Restore User Token
                         BaseApiClient.setAuthToken(userToken);
                     }
                 } else {
                     System.err.println("Admin credentials not found for seeding.");
                 }
            }
            
            plantsPage.open(); // Refresh
            firstAvailablePlantId = plantsPage.getFirstPlantId();
        }
        
        assertTrue(firstAvailablePlantId > 0, "No plants available in the system");
    }

    @When("User navigates to the \"Edit Plant\" page with the first available plant ID directly via URL")
    public void navigateToEditPlantWithFirstAvailablePlantId() {
        assertTrue(firstAvailablePlantId > 0,
                "First available plant ID not retrieved. Call 'a first available plant exists' first.");
        addPlantPage.getDriver().get(addPlantPage.getDriver().getCurrentUrl().split("/ui/")[0] + "/ui/plants/edit/" + firstAvailablePlantId);
    }
}
