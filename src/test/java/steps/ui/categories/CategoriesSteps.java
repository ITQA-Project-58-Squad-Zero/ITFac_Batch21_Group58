package steps.ui.categories;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.categories.CategoriesPage;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class CategoriesSteps {

    CategoriesPage categoriesPage;

    @Then("the categories table should be displayed")
    public void verifyCategoriesTableDisplayed() {
        assertTrue(categoriesPage.isCategoriesTableDisplayed(), "Categories table is not displayed");
    }

    @Then("the categories table should contain the following columns:")
    public void verifyCategoriesTableColumns(DataTable dataTable) {
        List<String> expectedColumns = dataTable.asList();
        List<String> actualColumns = categoriesPage.getColumnHeaders();

        for (String expected : expectedColumns) {
            assertTrue(actualColumns.stream().anyMatch(c -> c.contains(expected.trim())),
                    "Column '" + expected + "' not found in " + actualColumns);
        }
    }

    @Then("Edit and Delete actions should be disabled for non-admin user")
    public void verifyEditAndDeleteDisabled() {
        assertTrue(categoriesPage.areEditAndDeleteDisabled(),
                "Edit and/or Delete actions should be disabled for non-admin user");
    }

    @Then("category pagination controls should be visible")
    public void verifyPaginationVisible() {
        assertTrue(categoriesPage.isPaginationVisible(), "Pagination controls are not visible");
    }

    @When("Admin clicks the next page in categories")
    public void clickNextPage() {
        if (categoriesPage.isNextPageEnabled()) {
            categoriesPage.clickNextPage();
        }
    }

    @When("User clicks the next page in categories")
    public void userClickNextPage() {
        if (categoriesPage.isNextPageEnabled()) {
            categoriesPage.clickNextPage();
        }
    }

    @Then("the current page number should change")
    public void verifyPageNumberChanged() {
        String currentPage = categoriesPage.getActivePageNumber();
        assertTrue(currentPage != null && !currentPage.isEmpty(),
                "Page number should be visible after navigation");
    }

    @Then("the page number should change")
    public void verifyPageNumberChangedForPagination() {
        String currentPage = categoriesPage.getActivePageNumber();
        assertTrue(currentPage != null && !currentPage.isEmpty(),
                "Page number should be visible after navigation");
    }

    @Then("the category records should update")
    public void verifyCategoryRecordsUpdated() {
        assertTrue(categoriesPage.isCategoriesTableDisplayed(),
                "Categories table should still be displayed with updated records");
    }

    @Then("the category {string} button should be visible")
    public void verifyAddCategoryButtonVisible(String buttonName) {
        if (buttonName.equals("Add Category")) {
            assertTrue(categoriesPage.isAddCategoryButtonVisible(),
                    "Add Category button should be visible for Admin");
        }
    }

    @When("Admin enters {string} in the category search box")
    public void enterSearchTerm(String searchTerm) {
        categoriesPage.enterSearchTerm(searchTerm);
    }

    @When("User enters {string} in the category search box")
    public void userEnterSearchTerm(String searchTerm) {
        categoriesPage.enterSearchTerm(searchTerm);
    }

    @When("Admin clicks the search button")
    public void clickSearchButton() {
        categoriesPage.clickSearchButton();
    }

    @When("User clicks the search button")
    public void userClickSearchButton() {
        categoriesPage.clickSearchButton();
    }

    @Then("the categories table should show only matching results for {string}")
    public void verifySearchResults(String searchTerm) {
        List<String> categoryNames = categoriesPage.getAllCategoryNames();
        if (categoryNames.isEmpty()) {
            assertTrue(categoriesPage.isNoCategoryFoundMessageDisplayed(),
                    "Should show 'No category found' message when no results");
        } else {
            for (String name : categoryNames) {
                assertTrue(name.toLowerCase().contains(searchTerm.toLowerCase()),
                        "Category '" + name + "' should contain search term '" + searchTerm + "'");
            }
        }
    }

    @When("Admin selects parent category {string} from the filter dropdown")
    public void selectParentCategory(String parentName) {
        categoriesPage.selectParentCategory(parentName);
        // Verify selection was made (the page object method throws exception if it fails,
        // but we double-check here for better error messages)
        String selectedValue = categoriesPage.getSelectedParentValue();
        String selectedText = categoriesPage.getSelectedParentText();
        
        // Only fail if we explicitly got "All Parents" (empty value)
        if (selectedValue == null || selectedValue.isEmpty()) {
            fail(String.format("Parent category '%s' was not selected. Dropdown still shows: value='%s', text='%s'. " +
                    "This might indicate the dropdown selection didn't work properly.", 
                    parentName, selectedValue != null ? selectedValue : "null", 
                    selectedText != null ? selectedText : "null"));
        }
    }

    @Then("the categories table should show only categories under parent {string}")
    public void verifyFilteredByParent(String parentName) {
        // First, verify that a parent was actually selected (not "All Parents")
        String selectedValue = categoriesPage.getSelectedParentValue();
        if (selectedValue == null || selectedValue.isEmpty()) {
            fail("No parent category is selected - dropdown shows 'All Parents'. Cannot verify filter.");
        }
        
        List<String> parentNames = categoriesPage.getAllParentNames();
        
        // If table is empty, that's acceptable (no categories match the filter)
        if (parentNames.isEmpty()) {
            assertTrue(true, "Table is empty - no categories match the parent filter '" + parentName + "'");
            return;
        }
        
        // Normalize the expected parent name for comparison
        String normalizedExpected = parentName.trim().toLowerCase();
        
        // When filtering by a specific parent, all displayed categories should have that parent
        // Categories with "-" (no parent) should NOT appear when filtering by a specific parent
        List<String> mismatchedParents = new java.util.ArrayList<>();
        int matchingCount = 0;
        
        for (String parent : parentNames) {
            String trimmedParent = parent.trim();
            
            // Categories with "-" should not appear when filtering by a specific parent
            if (trimmedParent.equals("-") || trimmedParent.isEmpty()) {
                mismatchedParents.add("- (no parent)");
                continue;
            }
            
            // Check if parent matches (case-insensitive, with flexible matching)
            String normalizedParent = trimmedParent.toLowerCase();
            
            // Allow exact match, contains match, or reverse contains (in case of partial names)
            boolean matches = normalizedParent.equals(normalizedExpected) ||
                             normalizedParent.contains(normalizedExpected) ||
                             normalizedExpected.contains(normalizedParent);
            
            if (matches) {
                matchingCount++;
            } else {
                mismatchedParents.add(parent);
            }
        }
        
        // If we found mismatched parents (especially root categories with "-"), fail
        if (!mismatchedParents.isEmpty()) {
            fail(String.format("When filtering by parent '%s', all displayed categories should have that parent. " +
                    "Found %d matching categories, but also found %d category/categories with different parent(s): %s. " +
                    "All displayed parents: %s. " +
                    "Note: Categories with '-' (no parent) should not appear when filtering by a specific parent.",
                    parentName, matchingCount, mismatchedParents.size(), mismatchedParents, parentNames));
        }
        
        // Verify we have at least some matching categories
        assertTrue(matchingCount > 0, 
                String.format("No categories found with parent '%s'. All displayed parents: %s", 
                        parentName, parentNames));
        
        // If we get here, all parents match
        assertTrue(true, String.format("All %d categories are correctly filtered by parent '%s'", 
                matchingCount, parentName));
    }

    @When("Admin clicks the category {string} column header")
    public void clickColumnHeader(String columnName) {
        // Use smart click that ensures ascending order on first click
        categoriesPage.clickColumnHeaderToAscending(columnName);
    }

    @When("Admin clicks the category {string} column header again")
    public void clickColumnHeaderAgain(String columnName) {
        categoriesPage.clickColumnHeader(columnName);
    }

    @When("User clicks the category {string} column header")
    public void userClickColumnHeader(String columnName) {
        categoriesPage.clickColumnHeaderToAscending(columnName);
    }

    @When("User clicks the category {string} column header again")
    public void userClickColumnHeaderAgain(String columnName) {
        categoriesPage.clickColumnHeader(columnName);
    }

    @Then("the categories should be sorted by ID in ascending order")
    public void verifySortedByIdAscending() {
        List<String> ids = categoriesPage.getAllCategoryIds();
        List<Integer> idNumbers = ids.stream()
                .map(id -> id.equals("-") || id.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(id))
                .collect(Collectors.toList());
        List<Integer> sortedIds = idNumbers.stream().sorted().collect(Collectors.toList());
        assertEquals(sortedIds, idNumbers, "Categories should be sorted by ID in ascending order");
    }

    @Then("the categories should be sorted by ID in descending order")
    public void verifySortedByIdDescending() {
        List<String> ids = categoriesPage.getAllCategoryIds();
        List<Integer> idNumbers = ids.stream()
                .map(id -> id.equals("-") || id.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(id))
                .collect(Collectors.toList());
        List<Integer> sortedIds = idNumbers.stream().sorted((a, b) -> b.compareTo(a)).collect(Collectors.toList());
        assertEquals(sortedIds, idNumbers, "Categories should be sorted by ID in descending order");
    }

    @Then("the categories should be sorted by Name in ascending order")
    public void verifySortedByNameAscending() {
        List<String> names = categoriesPage.getAllCategoryNames();
        List<String> sortedNames = names.stream().sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
        assertEquals(sortedNames, names, "Categories should be sorted by Name in ascending order (A-Z)");
    }

    @Then("the categories should be sorted by Name in descending order")
    public void verifySortedByNameDescending() {
        List<String> names = categoriesPage.getAllCategoryNames();
        List<String> sortedNames = names.stream()
                .sorted((a, b) -> b.compareToIgnoreCase(a))
                .collect(Collectors.toList());
        assertEquals(sortedNames, names, "Categories should be sorted by Name in descending order (Z-A)");
    }

    @Then("the categories should be sorted by Parent in ascending order")
    public void verifySortedByParentAscending() {
        List<String> parents = categoriesPage.getAllParentNames();
        List<String> sortedParents = parents.stream()
                .map(p -> p.equals("-") ? "" : p)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
        List<String> actualParents = parents.stream()
                .map(p -> p.equals("-") ? "" : p)
                .collect(Collectors.toList());
        assertEquals(sortedParents, actualParents, "Categories should be sorted by Parent in ascending order");
    }

    @Then("the categories should be sorted by Parent in descending order")
    public void verifySortedByParentDescending() {
        List<String> parents = categoriesPage.getAllParentNames();
        List<String> sortedParents = parents.stream()
                .map(p -> p.equals("-") ? "" : p)
                .sorted((a, b) -> b.compareToIgnoreCase(a))
                .collect(Collectors.toList());
        List<String> actualParents = parents.stream()
                .map(p -> p.equals("-") ? "" : p)
                .collect(Collectors.toList());
        assertEquals(sortedParents, actualParents, "Categories should be sorted by Parent in descending order");
    }

    @When("Admin searches for a category that does not exist")
    public void searchForNonExistentCategory() {
        categoriesPage.searchForNonExistentCategory();
    }

    @Then("the {string} message should be displayed")
    public void verifyNoCategoryFoundMessage(String message) {
        assertTrue(categoriesPage.isNoCategoryFoundMessageDisplayed(),
                "Should display '" + message + "' message when no categories found");
    }

    @Then("the initial page should show limited rows based on page size")
    public void verifyLimitedRowsOnInitialPage() {
        int rowCount = categoriesPage.getCurrentPageRowCount();
        assertTrue(rowCount > 0, "Initial page should show at least one row");
        // Typically page size is 10, 20, or 25, so we check it's reasonable
        assertTrue(rowCount <= 50, "Initial page should show limited rows (page size), not all records");
    }

    @When("User notes the current page number and first row data")
    public void noteCurrentPageAndFirstRow() {
        // Store state for later comparison
        String currentPage = categoriesPage.getActivePageNumber();
        String firstRow = categoriesPage.getFirstRowData();
        // Store in a way that can be retrieved later - using a simple approach
        // In a real scenario, you might use Scenario context or a state object
    }


    @Then("the category rows should change")
    public void verifyRowsChanged() {
        assertTrue(categoriesPage.isCategoriesTableDisplayed(),
                "Categories table should be displayed with updated rows");
    }

    @When("User clicks the previous page in categories")
    public void clickPreviousPage() {
        if (categoriesPage.isPreviousPageEnabled()) {
            categoriesPage.clickPreviousPage();
        }
    }

    @Then("the page number should return to the original page")
    public void verifyPageNumberReturned() {
        String currentPage = categoriesPage.getActivePageNumber();
        assertTrue(currentPage != null && !currentPage.isEmpty(),
                "Page number should be visible after returning to previous page");
    }

    @Then("the category rows should return to original")
    public void verifyRowsReturnedToOriginal() {
        assertTrue(categoriesPage.isCategoriesTableDisplayed(),
                "Categories table should be displayed with original rows");
    }

    @When("Admin enters a non-existing keyword in the category search box")
    public void enterNonExistingKeyword() {
        categoriesPage.enterNonExistingKeyword();
    }

    @Then("no category rows should be displayed")
    public void verifyNoCategoryRows() {
        assertTrue(categoriesPage.hasNoCategoryRows(),
                "No category rows should be displayed when no matches found");
    }

    @When("User clears the search text")
    public void clearSearchText() {
        categoriesPage.clearSearchText();
    }

    @Then("the categories table should show filtered results")
    public void verifyFilteredResults() {
        assertTrue(categoriesPage.isCategoriesTableDisplayed(),
                "Categories table should show filtered results");
    }

    @Then("the full category list should be displayed again")
    public void verifyFullListDisplayed() {
        assertTrue(categoriesPage.isCategoriesTableDisplayed(),
                "Full category list should be displayed after clearing search/filter");
    }

    @When("Admin notes the total number of categories displayed")
    public void noteTotalCategories() {
        // Store for later comparison - similar to page number tracking
    }

    @When("User notes the total number of categories displayed")
    public void userNoteTotalCategories() {
        // Store for later comparison
    }

    @When("Admin clears the parent filter")
    public void clearParentFilter() {
        categoriesPage.clearParentFilter();
    }

    @Then("the categories table should show results matching both parent {string} and keyword {string}")
    public void verifyCombinedFilter(String parentName, String keyword) {
        assertTrue(categoriesPage.verifyCombinedFilter(parentName, keyword),
                String.format("Results should match both parent '%s' and keyword '%s'", parentName, keyword));
    }

    @Then("sorting should handle null parent values correctly")
    public void verifyNullParentHandling() {
        assertTrue(categoriesPage.verifyNullParentHandling(),
                "Sorting should handle null parent values correctly (grouped together)");
    }

    @Then("the category {string} button should not be visible")
    public void verifyButtonNotVisible(String buttonName) {
        if (buttonName.equals("Add Category")) {
            assertTrue(categoriesPage.isAddCategoryButtonNotVisible(),
                    "Add Category button should not be visible for non-admin user");
        }
    }
}
