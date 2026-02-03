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
}
