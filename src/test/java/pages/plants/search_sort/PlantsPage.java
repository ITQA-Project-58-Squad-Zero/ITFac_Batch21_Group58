package pages.plants.search_sort;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

@DefaultUrl("/ui/plants")
public class PlantsPage extends PageObject {

    @FindBy(name = "name")
    WebElementFacade searchInput;

    @FindBy(name = "categoryId")
    WebElementFacade categorySelect;

    @FindBy(xpath = "//button[contains(text(),'Search')]")
    WebElementFacade searchButton;

    @FindBy(xpath = "//a[contains(text(),'Reset')]")
    WebElementFacade resetButton;

    @FindBy(css = "table.table")
    WebElementFacade plantsTable;

    // @FindBy(css = "table.table thead th")
    // List<WebElementFacade> tableHeaders;

    @FindBy(css = "ul.pagination")
    WebElementFacade pagination;

    public void searchForPlant(String name) {
        searchInput.type(name);
        searchButton.click();
        waitFor(plantsTable);
    }

    public void selectCategory(String category) {
        categorySelect.selectByVisibleText(category);
        searchButton.click();
        waitFor(plantsTable);
    }

    public void clickReset() {
        resetButton.click();
    }

    public boolean isTableDisplayed() {
        return plantsTable.isVisible();
    }

    public List<String> getTableHeaders() {
        return findAll(By.cssSelector("table.table thead th")).stream()
                .map(WebElementFacade::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean isPaginationDisplayed() {
        return pagination.isVisible();
    }

    public List<String> getColumnData(String columnName) {
        int columnIndex = getColumnIndex(columnName);
        if (columnIndex == -1)
            return List.of();

        return findAll(By.cssSelector("table.table tbody tr td:nth-child(" + (columnIndex + 1) + ")"))
                .stream()
                .map(WebElementFacade::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private int getColumnIndex(String columnName) {
        List<String> headers = getTableHeaders();
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).toLowerCase().contains(columnName.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }

    public void sortBy(String columnName) {
        // This assumes the header text matches the column name passed
        /*
         * The HTML shows headers like:
         * <a href="/ui/plants?page=0&sortField=name&sortDir=desc...">Name <span>
         * ↑</span></a>
         */

        // Find the header link that contains the column name
        findBy("//thead//th[contains(.,'" + columnName + "')]//a").click();
    }

    // ========== PAGINATION METHODS FOR TC_PL_UI_002 ==========

    /**
     * Checks if pagination controls are visible
     */
    public boolean arePaginationControlsVisible() {
        return pagination.isVisible();
    }

    /**
     * Clicks the "Next" pagination button
     */
    public void clickNextPage() {
        WebElementFacade nextButton = findBy("//ul[@class='pagination']//a[contains(text(),'Next')]");
        nextButton.waitUntilClickable().click();
        waitFor(plantsTable);
    }

    /**
     * Clicks the "Previous" pagination button
     */
    public void clickPreviousPage() {
        WebElementFacade previousButton = findBy("//ul[@class='pagination']//a[contains(text(),'Previous')]");
        previousButton.waitUntilClickable().click();
        waitFor(plantsTable);
    }

    /**
     * Gets all plant names currently displayed on the page
     */
    public List<String> getCurrentPagePlantNames() {
        return getColumnData("Name");
    }

    /**
     * Checks if the Previous button is disabled
     */
    public boolean isPreviousButtonDisabled() {
        WebElementFacade previousItem = findBy(
                "//ul[@class='pagination']//li[contains(@class,'page-item') and .//a[contains(text(),'Previous')]]");
        String classAttr = previousItem.getElement().getAttribute("class");
        return classAttr != null && classAttr.contains("disabled");
    }

    /**
     * Checks if the Next button is disabled
     */
    public boolean isNextButtonDisabled() {
        WebElementFacade nextItem = findBy(
                "//ul[@class='pagination']//li[contains(@class,'page-item') and .//a[contains(text(),'Next')]]");
        String classAttr = nextItem.getElement().getAttribute("class");
        return classAttr != null && classAttr.contains("disabled");
    }

    /**
     * Gets the active page number from pagination
     */
    public int getActivePageNumber() {
        WebElementFacade activePage = findBy("//ul[@class='pagination']//li[contains(@class,'active')]//a");
        return Integer.parseInt(activePage.getText().trim());
    }

    /**
     * Clicks a specific page number
     */
    public void clickPageNumber(int pageNumber) {
        WebElementFacade pageButton = findBy("//ul[@class='pagination']//a[text()='" + pageNumber + "']");
        pageButton.waitUntilClickable().click();
        waitFor(plantsTable);
    }
}
