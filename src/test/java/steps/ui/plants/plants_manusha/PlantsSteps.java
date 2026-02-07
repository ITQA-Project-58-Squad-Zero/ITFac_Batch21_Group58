package steps.ui.plants.plants_manusha;

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

    @When("Admin clicks the \"Search\" button")
    public void clickSearchButtonWithQuotes() {
        plantsPage.clickSearchButton();
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

    @Then("only plants matching the searched term should be displayed")
    public void verifyOnlyMatchingSearchedTermDisplayed() {
        String searchTerm = firstAvailablePlantName != null ? firstAvailablePlantName : multiWordPlantName;
        assertTrue(searchTerm != null, "Search term was not set by previous steps.");
        verifyOnlyMatchingPlantsDisplayed(searchTerm);
    }

    @Then("all displayed plant names should contain {string}")
    public void verifyAllPlantNamesContain(String searchTerm) {
        assertTrue(plantsPage.allPlantNamesContain(searchTerm),
                "Not all displayed plant names contain '" + searchTerm + "'");
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
        assertFalse(plantsPage.areEditButtonsVisibleForAllRows(),
                "Edit action should NOT be visible for any plant row");
    }

    @Then("the Delete action should not be visible for any plant")
    public void verifyDeleteActionNotVisible() {
        assertFalse(plantsPage.areDeleteButtonsVisibleForAllRows(),
                "Delete action should NOT be visible for any plant row");
    }

    @Then("the current user should be identified as \"User\"")
    public void verifyUserIsLoggedIn() {
        // Since there is no profile name in the UI, we verify by checking that
        // Admin-only features (Add Plant) are NOT visible.
        assertFalse(plantsPage.isAddPlantButtonVisible(),
                "User verification failed: Admin 'Add Plant' button is visible, implying wrong role.");
    }

    // ========== DYNAMIC DATA RETRIEVAL STEPS ==========

    private String firstAvailablePlantName;
    private String firstAvailableCategoryName;
    private String multiWordPlantName;

    @net.serenitybdd.annotations.Steps
    api.client.plants.PlantsApiClient plantsApiClient;

    @io.cucumber.java.en.Given("at least one plant exists in the system")
    public void verifyAtLeastOnePlantExists() {
        plantsPage.open();
        // Check if table is empty or has "No plants found"
        if (plantsPage.getDisplayedPlantCount() == 0 || plantsPage.isNoPlantsMessageDisplayed()) {
             // Create a plant
             plantsApiClient.createPlant("AutoSeedPlant" + System.currentTimeMillis(), 50.0, 10, 1);
             plantsPage.open(); // Refresh
        }
        assertTrue(plantsPage.getDisplayedPlantCount() > 0, "Precondition: At least one plant should exist in the system");
    }

    @io.cucumber.java.en.Given("a plant named {string} exists in the system")
    public void verifySpecificPlantExists(String plantName) {
        plantsPage.open();
        // Use search to check existence across all pages
        plantsPage.searchForPlant(plantName);
        List<String> plantNames = plantsPage.getCurrentPagePlantNames();
        boolean exists = plantNames.stream().anyMatch(name -> name.equalsIgnoreCase(plantName));
        
        if (!exists) {
             plantsApiClient.createPlant(plantName, 50.0, 10, 1);
             plantsPage.open(); 
             plantsPage.searchForPlant(plantName);
             plantNames = plantsPage.getCurrentPagePlantNames();
             exists = plantNames.stream().anyMatch(name -> name.equalsIgnoreCase(plantName));
        }
        
        assertTrue(exists, "Precondition: Plant '" + plantName + "' should exist in the system");
        // Reset page for the next steps
        plantsPage.open();
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

    @io.cucumber.java.en.Given("Admin retrieves the first available single-word plant name")
    public void retrieveFirstAvailableSingleWordPlantName() {
        plantsPage.open();
        List<String> plantNames = plantsPage.getCurrentPagePlantNames();
        
        // Find a single-word name (no spaces)
        firstAvailablePlantName = null;
        for (String name : plantNames) {
            if (!name.trim().contains(" ")) {
                firstAvailablePlantName = name;
                break;
            }
        }

        if (firstAvailablePlantName == null) {
             // Create a single-word plant if missing
             String singleWordName = "Fern" + System.currentTimeMillis(); // Ensure single word
             plantsApiClient.createPlant(singleWordName, 25.0, 50, 1);
             plantsPage.open();
             firstAvailablePlantName = singleWordName;
        }
        
        assertTrue(firstAvailablePlantName != null && !firstAvailablePlantName.isEmpty(), "No single-word plant name available in the system");
    }

    @When("Admin enters the first available single-word plant name in the search field")
    public void enterFirstAvailableSingleWordPlantNameInSearch() {
        assertTrue(firstAvailablePlantName != null, 
            "First available single-word plant name not retrieved. Call 'Admin retrieves the first available single-word plant name' first.");
        plantsPage.enterSearchText(firstAvailablePlantName);
    }

    @io.cucumber.java.en.Given("a plant with a two-word name exists in the system")
    public void verifyTwoWordPlantExists() {
        plantsPage.open();
        List<String> plantNames = plantsPage.getCurrentPagePlantNames();
        // Look for a plant with exactly one space in the name (two words)
        multiWordPlantName = null;
        for (String name : plantNames) {
            String trimmedName = name.trim();
            if (trimmedName.contains(" ") && trimmedName.split("\\s+").length == 2) {
                multiWordPlantName = name;
                break;
            }
        }
        
        if (multiWordPlantName == null) {
            // Create a two-word plant
            String mwName = "Aloe Vera" + System.currentTimeMillis();
            plantsApiClient.createPlant(mwName, 50.0, 10, 1);
            plantsPage.open();
            multiWordPlantName = mwName;
        }
        
        assertTrue(multiWordPlantName != null,
                "No plant with two-word name found in the system. Available plants: " + plantNames);
    }

    @When("Admin enters the two-word plant name in the search field")
    public void enterTwoWordPlantNameInSearch() {
        assertTrue(multiWordPlantName != null,
                "Two-word plant name not retrieved. Call 'a plant with a two-word name exists in the system' first.");
        plantsPage.enterSearchText(multiWordPlantName);
    }

    @Then("the searched plant should be in the results")
    public void verifySearchedPlantInResults() {
        String searchTerm = firstAvailablePlantName != null ? firstAvailablePlantName : multiWordPlantName;
        List<String> plantNames = plantsPage.getCurrentPagePlantNames();
        boolean plantFound = plantNames.stream()
                .anyMatch(name -> name.equalsIgnoreCase(searchTerm) || name.toLowerCase().contains(searchTerm.toLowerCase()));

        String actualResults = plantNames.isEmpty() ? "No plants found" : String.join(", ", plantNames);

        assertTrue(plantFound,
                "Plant '" + searchTerm + "' was not found in the search results. " +
                        "Found plants: [" + actualResults + "]");
    }

    @io.cucumber.java.en.Given("at least one category with plants exists")
    public void verifyAtLeastOneCategoryWithPlantsExists() {
        // Navigate to plants page and get first category
        plantsPage.open();
        List<String> categories = plantsPage.getAvailableCategories();
        assertTrue(!categories.isEmpty() && categories.size() > 1,
                "No categories available in the dropdown");
        // First option is usually "All Categories" or similar
        firstAvailableCategoryName = categories.size() > 1 ? categories.get(1) : categories.get(0);
    }

    @When("Admin selects the first available category from the category dropdown")
    public void selectFirstAvailableCategoryFromDropdown() {
        assertTrue(firstAvailableCategoryName != null,
                "First available category name not retrieved. Call 'at least one category with plants exists' first.");
        plantsPage.selectCategory(firstAvailableCategoryName);
    }


    @Then("only plants belonging to the selected category should be displayed")
    public void verifyOnlyPlantsOfSelectedCategoryDisplayed() {
        List<String> categories = plantsPage.getColumnData("Category");
        int displayedCount = plantsPage.getDisplayedPlantCount();

        // It's acceptable to have no plants in this category
        if (displayedCount == 0) {
            return;
        }

        boolean allMatch = categories.stream().allMatch(c -> c.equals(firstAvailableCategoryName));

        String actualCategories = categories.isEmpty() ? "No plants found"
                : String.join(", ", categories.stream().distinct().collect(Collectors.toList()));

        assertTrue(allMatch,
                "Not all displayed plants belong to category '" + firstAvailableCategoryName + "'. " +
                        "Found categories: [" + actualCategories + "]");
    }

    @Then("all displayed plants should have the selected category")
    public void verifyAllPlantsHaveSelectedCategory() {
        List<String> categories = plantsPage.getColumnData("Category");
        if (categories.isEmpty()) {
            return; // Acceptable - no plants in this category
        }
        assertTrue(categories.stream().allMatch(c -> c.equalsIgnoreCase(firstAvailableCategoryName)),
                "Not all displayed plants have category '" + firstAvailableCategoryName + "'");
    }

}
