package steps.ui.sales;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import io.cucumber.java.en.When;
import pages.sales.SalesPage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SalesSteps {

    SalesPage salesPage;

    // Navigation is now handled by NavigationSteps

    @Then("Admin should see the sales table")
    public void verifySalesTable() {
        assertTrue(salesPage.isSalesTableDisplayed(), "Sales table is not displayed");
    }

    @Then("the sales table should contain the following columns:")
    public void verifyTableColumns(DataTable dataTable) {
        List<String> expectedColumns = dataTable.asList();
        List<String> actualColumns = salesPage.getColumnHeaders();

        for (String expected : expectedColumns) {
            assertTrue(actualColumns.stream().anyMatch(c -> c.contains(expected)),
                    "Column '" + expected + "' not found in " + actualColumns);
        }
    }

    @Then("the \"Sell Plant\" button should be visible")
    public void verifySellPlantButtonVisible() {
        assertTrue(salesPage.isSellPlantButtonVisible(), "Sell Plant button is not visible");
    }

    @When("Admin clicks the \"Sell Plant\" button")
    public void clickSellPlantButton() {
        salesPage.clickSellPlantButton();
    }

    pages.sales.CreateSalePage createSalePage;

    @Then("the \"Sell Plant\" form should open successfully")
    public void verifySellPlantFormOpens() {
        assertTrue(createSalePage.isPageOpen(), "Create Sale page did not open");
    }

    private String selectedPlantName;

    @When("Admin selects a plant from the dropdown")
    public void selectPlantFromDropdown() {
        createSalePage.selectFirstAvailablePlant();
        selectedPlantName = createSalePage.getSelectedPlantName();
    }

    @When("Admin enters quantity {string}")
    public void enterQuantity(String quantity) {
        createSalePage.enterQuantity(quantity);
    }

    @When("Admin clicks the \"Sell\" button")
    public void clickSellButton() {
        createSalePage.clickSellButton();
    }

    @Then("Admin should be on the Sales list page")
    public void verifyOnSalesListPage() {
        assertTrue(salesPage.isSalesTableDisplayed(), "Not on Sales list page - table not displayed");
    }

    @Then("the newly created sale should appear in the sales table")
    public void verifyNewSaleInTable() {
        assertTrue(salesPage.isSalePresent(selectedPlantName), 
                "Newly created sale for plant '" + selectedPlantName + "' not found in sales table");
    }

    @Then("a validation error message should be displayed")
    public void verifyValidationErrorDisplayed() {
        assertTrue(createSalePage.isValidationErrorDisplayed(), 
                "Validation error message is not displayed");
    }

    @Then("the sale should not be created")
    public void verifySaleNotCreated() {
        // Either still on the form page or validation error is visible
        boolean stillOnForm = createSalePage.isStillOnCreateSalePage();
        boolean hasError = createSalePage.isValidationErrorDisplayed();
        assertTrue(stillOnForm || hasError, 
                "Sale was created - user was redirected away from Create Sale page and no validation error shown");
    }
}

