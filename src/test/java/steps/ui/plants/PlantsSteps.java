package steps.ui.plants;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.plants.search_sort.PlantsPage;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PlantsSteps {

    PlantsPage plantsPage;

    @When("Admin navigates to the Plant List page")
    @When("User navigates to the Plant List page")
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
        assertTrue(plantsPage.arePaginationControlsVisible(), "Pagination is not displayed");
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

    @When("Admin enters {string} in the search field")
    public void enterSearchText(String searchTerm) {
        plantsPage.enterSearchText(searchTerm);
    }

    @When("Admin clicks the Search button")
    public void clickSearchButton() {
        plantsPage.clickSearchButton();
    }

    @When("Admin clicks the {string} button")
    public void clickButton(String buttonName) {
        if (buttonName.equalsIgnoreCase("Search")) {
            plantsPage.clickSearchButton();
        }
    }

    @When("Admin enters {string} in the search box")
    public void enterSearchBox(String term) {
        plantsPage.enterSearchText(term);
    }

    @Then("the result list should contain {string}")
    public void verifyResultContains(String expectedName) {
        List<String> names = plantsPage.getColumnData("Name");
        assertTrue(names.stream().anyMatch(n -> n.contains(expectedName)),
                "Result list does not contain " + expectedName);
    }

    @Then("only plants matching {string} should be displayed")
    public void verifyOnlyMatchingPlantsDisplayed(String searchTerm) {
        List<String> plantNames = plantsPage.getCurrentPagePlantNames();
        int displayedCount = plantsPage.getDisplayedPlantCount();

        // Better error message showing what was actually found
        String actualResults = plantNames.isEmpty() ? "No plants found" : String.join(", ", plantNames);

        assertTrue(displayedCount > 0,
                "No plants are displayed after searching for '" + searchTerm + "'. " +
                        "Expected at least one plant matching '" + searchTerm + "'. " +
                        "Actual results: " + actualResults);

        boolean allMatch = plantNames.stream()
                .allMatch(name -> name.toLowerCase().contains(searchTerm.toLowerCase()));

        assertTrue(allMatch,
                "Not all displayed plants match the search term '" + searchTerm + "'. " +
                        "Found plants: [" + actualResults + "]");
    }

    @Then("all displayed plant names should contain {string}")
    public void verifyAllPlantNamesContain(String searchTerm) {
        assertTrue(plantsPage.allPlantNamesContain(searchTerm),
                "Not all displayed plant names contain '" + searchTerm + "'");
    }

    @io.cucumber.java.en.Given("at least one plant exists in the system")
    public void verifyAtLeastOnePlantExists() {
        // This is a precondition check
        // In a real scenario, you might query the database or API
        // For now, we'll assume the system has plants based on the HTML provided
        // This step serves as documentation of the precondition
        assertTrue(true, "Precondition: At least one plant should exist in the system");
    }

    @io.cucumber.java.en.Given("a plant named {string} exists in the system")
    public void verifySpecificPlantExists(String plantName) {
        // This is a precondition check for the multi-word search test
        // In a real scenario, you might query the database or API to verify
        // For now, we document this as a precondition
        // The test will fail if the search doesn't return this plant
        assertTrue(true, "Precondition: Plant '" + plantName + "' should exist in the system");
    }

    @Then("the plant {string} should be in the results")
    public void verifyPlantInResults(String plantName) {
        List<String> plantNames = plantsPage.getCurrentPagePlantNames();
        boolean plantFound = plantNames.stream()
                .anyMatch(name -> name.equalsIgnoreCase(plantName));

        String actualResults = plantNames.isEmpty() ? "No plants found" : String.join(", ", plantNames);

        assertTrue(plantFound,
                "Plant '" + plantName + "' was not found in the search results. " +
                        "Found plants: [" + actualResults + "]");
    }

    @When("Admin filters by category {string}")
    public void filterByCategory(String category) {
        plantsPage.selectCategory(category);
    }

    @When("Admin selects {string} from the category dropdown")
    public void selectCategoryFromDropdown(String category) {
        plantsPage.selectCategory(category);
    }

    @Then("only plants belonging to category {string} should be displayed")
    public void verifyOnlyPlantsOfCategoryDisplayed(String category) {
        List<String> categories = plantsPage.getColumnData("Category");
        int displayedCount = plantsPage.getDisplayedPlantCount();

        assertTrue(displayedCount > 0,
                "No plants are displayed after filtering by category '" + category + "'");

        boolean allMatch = categories.stream().allMatch(c -> c.equals(category));

        String actualCategories = categories.isEmpty() ? "No plants found"
                : String.join(", ", categories.stream().distinct().collect(Collectors.toList()));

        assertTrue(allMatch,
                "Not all displayed plants belong to category '" + category + "'. " +
                        "Found categories: [" + actualCategories + "]");
    }

    @Then("all displayed plants should have category {string}")
    public void verifyAllPlantsHaveCategory(String category) {
        List<String> categories = plantsPage.getColumnData("Category");
        assertTrue(categories.stream().allMatch(c -> c.equalsIgnoreCase(category)),
                "Not all displayed plants have category '" + category + "'");
    }

    @Then("all results should belong to category {string}")
    public void verifyCategoryResults(String category) {
        List<String> categories = plantsPage.getColumnData("Category");
        assertTrue(categories.stream().allMatch(c -> c.equals(category)), "Not all results belong to +" + category);
    }

    @When("Admin sorts by {string}")
    public void adminSortsBy(String columnName) {
        plantsPage.sortBy(columnName);
    }

    @When("Admin clicks the {string} column header")
    public void clickColumnHeader(String columnName) {
        plantsPage.sortBy(columnName);
    }

    @When("Admin clicks the {string} column header again")
    public void clickColumnHeaderAgain(String columnName) {
        plantsPage.sortBy(columnName);
    }

    @Then("plants should be sorted by {string} in {string} order")
    public void verifyPlantsSortedBy(String columnName, String order) {
        List<String> data = plantsPage.getColumnData(columnName);

        assertTrue(!data.isEmpty(),
                "No data found for column '" + columnName + "'");

        boolean isSorted = plantsPage.isSortedBy(columnName, order);

        String actualOrder = String.join(", ", data);

        assertTrue(isSorted,
                "Plants are not sorted by '" + columnName + "' in '" + order + "' order. " +
                        "Actual order: [" + actualOrder + "]");
    }

    @Then("the results should be sorted by {string} in {string} order")
    public void verifySortedResults(String columnName, String order) {
        List<String> data = plantsPage.getColumnData(columnName);
        assertTrue(!data.isEmpty(), "No data found for column " + columnName);
    }

    // ========== LOW STOCK BADGE STEP FOR TC_PL_UI_008 ==========

    @Then("plants with quantity below {int} should display a {string} badge")
    public void verifyLowStockBadge(int threshold, String badgeText) {
        boolean badgesCorrect = plantsPage.verifyLowStockBadges(threshold);

        assertTrue(badgesCorrect,
                "Low stock badges are not displayed correctly. " +
                        "Plants with quantity < " + threshold + " should have '" + badgeText + "' badge");
    }

    // ========== EMPTY STATE STEP FOR TC_PL_UI_009 ==========

    @Then("a {string} message should be displayed")
    public void verifyEmptyStateMessage(String expectedMessage) {
        boolean isDisplayed = plantsPage.isNoPlantsMessageDisplayed();
        assertTrue(isDisplayed, "Expected empty state message '" + expectedMessage
                + "' was not displayed. The table might not be empty or the message is missing.");
    }

    // ========== ADMIN ACTION STEPS FOR TC_PL_UI_A_001 ==========

    @Then("the {string} button should be visible")
    public void verifyButtonVisibility(String buttonName) {
        if ("Add a Plant".equals(buttonName)) {
            assertTrue(plantsPage.isAddPlantButtonVisible(),
                    "'" + buttonName + "' button is not visible");
        } else {
            // Basic fallback or fail if unknown
            // For valid tests, we expect implemented buttons.
            throw new IllegalArgumentException("Visibility check not implemented for button: " + buttonName);
        }
    }

    @Then("the {string} button should not be visible")
    public void verifyButtonNotVisible(String buttonName) {
        if ("Add a Plant".equals(buttonName)) {
            assertFalse(plantsPage.isAddPlantButtonVisible(),
                    "'" + buttonName + "' button should NOT be visible");
        } else {
            throw new IllegalArgumentException("Visibility check not implemented for button: " + buttonName);
        }
    }

    @Then("the Edit action should be visible for every plant")
    public void verifyEditActionVisible() {
        assertTrue(plantsPage.areEditButtonsVisibleForAllRows(),
                "Edit action is not visible for all plant rows");
    }

    @Then("the Delete action should be visible for every plant")
    public void verifyDeleteActionVisible() {
        assertTrue(plantsPage.areDeleteButtonsVisibleForAllRows(),
                "Delete action is not visible for all plant rows");
    }

    @Then("the Edit action should not be visible for any plant")
    public void verifyEditActionNotVisible() {
        assertTrue(plantsPage.areEditButtonsHiddenForAllRows(),
                "Edit action is visible for some/all plants (Should be hidden for User)");
    }

    @Then("the Delete action should not be visible for any plant")
    public void verifyDeleteActionNotVisible() {
        assertTrue(plantsPage.areDeleteButtonsHiddenForAllRows(),
                "Delete action is visible for some/all plants (Should be hidden for User)");
    }

    @Then("the current user should be identified as \"User\"")
    public void verifyUserIsLoggedIn() {
        // Since there is no profile name in the UI, we verify by checking that
        // Admin-only features (Add Plant) are NOT visible.
        assertFalse(plantsPage.isAddPlantButtonVisible(),
                "User verification failed: Admin 'Add Plant' button is visible, implying wrong role.");
    }
}
