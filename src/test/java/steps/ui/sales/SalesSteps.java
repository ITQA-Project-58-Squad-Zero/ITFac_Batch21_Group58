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
}
