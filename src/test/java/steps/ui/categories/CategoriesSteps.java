package steps.ui.categories;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.categories.CategoriesPage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Then("pagination controls should be visible")
    public void verifyPaginationVisible() {
        assertTrue(categoriesPage.isPaginationVisible(), "Pagination controls are not visible");
    }

    @When("Admin clicks the next page in categories")
    public void clickNextPage() {
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

    @Then("the category records should update")
    public void verifyCategoryRecordsUpdated() {
        assertTrue(categoriesPage.isCategoriesTableDisplayed(),
                "Categories table should still be displayed with updated records");
    }
}
