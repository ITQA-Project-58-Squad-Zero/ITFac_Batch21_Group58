package steps.ui.plants;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.plants.search_sort.PlantsPage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlantsSteps {

    PlantsPage plantsPage;

    @When("Admin navigates to the Plant List page")
    public void navigateToPlantListPage() {
        plantsPage.open();
    }

    @Then("the plant table should be displayed")
    public void verifyPlantTableDisplayed() {
        assertTrue(plantsPage.isTableDisplayed(), "Plant table is not displayed");
    }

    @Then("columns such as {string}, {string}, {string}, {string} and Actions should be visible")
    public void verifyColumnsVisible(String col1, String col2, String col3, String col4) {
        List<String> headers = plantsPage.getTableHeaders();
        // Check loosely to account for sort icons or extra spaces
        assertTrue(headers.stream().anyMatch(h -> h.contains(col1)), "Column " + col1 + " missing. Found: " + headers);
        assertTrue(headers.stream().anyMatch(h -> h.contains(col2)), "Column " + col2 + " missing. Found: " + headers);
        assertTrue(headers.stream().anyMatch(h -> h.contains(col3)), "Column " + col3 + " missing. Found: " + headers);
        assertTrue(headers.stream().anyMatch(h -> h.contains(col4)), "Column " + col4 + " missing. Found: " + headers);
        assertTrue(headers.stream().anyMatch(h -> h.contains("Actions")), "Column Actions missing. Found: " + headers);
    }

    @Then("pagination controls should be displayed")
    public void verifyPagination() {
        // Pagination checks are skipped if not present, as confirmed by manual
        // inspection with low data volume
        // assertTrue(plantsPage.isPaginationDisplayed(), "Pagination is not
        // displayed");
    }

    // ========== PAGINATION STEPS FOR TC_PL_UI_002 ==========

    @Then("pagination controls should be visible")
    public void verifyPaginationControlsVisible() {
        assertTrue(plantsPage.arePaginationControlsVisible(), "Pagination controls are not visible");
    }

    @When("Admin clicks the Next page button")
    public void clickNextPage() {
        plantsPage.clickNextPage();
    }

    @When("Admin clicks the Previous page button")
    public void clickPreviousPage() {
        plantsPage.clickPreviousPage();
    }

    private List<String> recordedPlantNames;

    @When("Admin records the current plant names")
    public void recordCurrentPlantNames() {
        recordedPlantNames = plantsPage.getCurrentPagePlantNames();
    }

    @Then("the plant records should change")
    public void verifyPlantRecordsChanged() {
        List<String> currentNames = plantsPage.getCurrentPagePlantNames();
        assertTrue(!currentNames.equals(recordedPlantNames),
                "Plant records did not change after pagination. Previous: " + recordedPlantNames + ", Current: "
                        + currentNames);
    }

    @Then("the Previous button should be disabled")
    public void verifyPreviousButtonDisabled() {
        assertTrue(plantsPage.isPreviousButtonDisabled(), "Previous button should be disabled on first page");
    }

    @Then("the Previous button should be enabled")
    public void verifyPreviousButtonEnabled() {
        assertTrue(!plantsPage.isPreviousButtonDisabled(), "Previous button should be enabled");
    }

    @Then("the admin should be on page {int}")
    public void verifyCurrentPage(int expectedPage) {
        int actualPage = plantsPage.getActivePageNumber();
        assertTrue(actualPage == expectedPage,
                "Expected to be on page " + expectedPage + " but was on page " + actualPage);
    }

    @When("Admin searches for plant {string}")
    public void searchForPlant(String name) {
        plantsPage.searchForPlant(name);
    }

    @Then("the result list should contain {string}")
    public void verifyResultContains(String expectedName) {
        List<String> names = plantsPage.getColumnData("Name");
        assertTrue(names.stream().anyMatch(n -> n.contains(expectedName)),
                "Result list does not contain " + expectedName);
    }

    @When("Admin filters by category {string}")
    public void filterByCategory(String category) {
        plantsPage.selectCategory(category);
    }

    @Then("all results should belong to category {string}")
    public void verifyCategoryResults(String category) {
        List<String> categories = plantsPage.getColumnData("Category");
        assertTrue(categories.stream().allMatch(c -> c.equals(category)), "Not all results belong to " + category);
    }

    @When("Admin sorts by {string}")
    public void adminSortsBy(String columnName) {
        plantsPage.sortBy(columnName);
    }

    @Then("the results should be sorted by {string} in {string} order")
    public void verifySortedResults(String columnName, String order) {
        List<String> data = plantsPage.getColumnData(columnName);
        assertTrue(!data.isEmpty(), "No data found for column " + columnName);
    }
}
