package pages.plants.search_sort;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DefaultUrl("/ui/plants")
public class PlantsPage extends PageObject {

    // ==================================================================================
    // LOCATORS
    // ==================================================================================

    @FindBy(name = "name")
    WebElementFacade searchInput;

    @FindBy(name = "categoryId")
    WebElementFacade categorySelect;

    @FindBy(xpath = "//button[contains(text(),'Search')]")
    WebElementFacade searchButton;

    @FindBy(xpath = "//a[contains(text(),'Reset')]")
    WebElementFacade resetButton;

    @FindBy(xpath = "//a[normalize-space()='Add a Plant']")
    WebElementFacade addPlantButton;

    @FindBy(css = "table.table")
    WebElementFacade plantsTable;

    @FindBy(css = "ul.pagination")
    WebElementFacade pagination;

    // ==================================================================================
    // SEARCH & SORT METHODS
    // ==================================================================================

    public void searchForPlant(String name) {
        searchInput.type(name);
        searchButton.click();
        waitFor(plantsTable);
    }

    public void enterSearchText(String name) {
        searchInput.clear();
        searchInput.type(name);
    }

    public void clickSearchButton() {
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
        try {
            return findAll(By.cssSelector("table.table thead th")).stream()
                    .map(element -> {
                        try {
                            return element.getText();
                        } catch (org.openqa.selenium.StaleElementReferenceException e) {
                            return "";
                        }
                    })
                    .map(String::trim)
                    .filter(text -> !text.isEmpty())
                    .collect(Collectors.toList());
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            waitABit(500);
            return findAll(By.cssSelector("table.table thead th")).stream()
                    .map(WebElementFacade::getText)
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
    }

    public List<String> getColumnData(String columnName) {
        int columnIndex = getColumnIndex(columnName);
        if (columnIndex == -1)
            return List.of();

        try {
            return findAll(By.cssSelector("table.table tbody tr td:nth-child(" + (columnIndex + 1) + ")"))
                    .stream()
                    .map(element -> {
                        try {
                            return element.getText();
                        } catch (org.openqa.selenium.StaleElementReferenceException e) {
                            return "";
                        }
                    })
                    .map(String::trim)
                    .filter(text -> !text.isEmpty())
                    .collect(Collectors.toList());
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            waitABit(500);
            return findAll(By.cssSelector("table.table tbody tr td:nth-child(" + (columnIndex + 1) + ")"))
                    .stream()
                    .map(WebElementFacade::getText)
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
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
        findBy("//thead//th[contains(.,'" + columnName + "')]//a").click();
        waitABit(500);
    }

    public boolean isSortedBy(String columnName, String order) {
        List<String> data = getColumnData(columnName);

        if (data.isEmpty() || data.size() < 2) {
            return true;
        }

        if (columnName.equalsIgnoreCase("Price") || columnName.equalsIgnoreCase("Stock")) {
            List<Double> numericData = data.stream()
                    .map(s -> {
                        if (columnName.equalsIgnoreCase("Stock")) {
                            return Double.parseDouble(s.split("\\s+")[0]);
                        } else {
                            return Double.parseDouble(s);
                        }
                    })
                    .collect(Collectors.toList());

            List<Double> sortedNumericData = new ArrayList<>(numericData);

            if (order.equalsIgnoreCase("ascending") || order.equalsIgnoreCase("asc")) {
                sortedNumericData.sort(Double::compareTo);
            } else if (order.equalsIgnoreCase("descending") || order.equalsIgnoreCase("desc")) {
                sortedNumericData.sort((a, b) -> b.compareTo(a));
            }

            return numericData.equals(sortedNumericData);
        } else {
            List<String> sortedData = new ArrayList<>(data);

            if (order.equalsIgnoreCase("ascending") || order.equalsIgnoreCase("asc")) {
                sortedData.sort(String.CASE_INSENSITIVE_ORDER);
            } else if (order.equalsIgnoreCase("descending") || order.equalsIgnoreCase("desc")) {
                sortedData.sort(String.CASE_INSENSITIVE_ORDER.reversed());
            }

            return data.equals(sortedData);
        }
    }

    // ==================================================================================
    // PAGINATION METHODS
    // ==================================================================================

    public boolean arePaginationControlsVisible() {
        return pagination.isVisible();
    }

    public void clickNextPage() {
        WebElementFacade nextButton = findBy("//ul[@class='pagination']//a[contains(text(),'Next')]");
        nextButton.waitUntilClickable().click();
        waitFor(plantsTable);
    }

    public void clickPreviousPage() {
        WebElementFacade previousButton = findBy("//ul[@class='pagination']//a[contains(text(),'Previous')]");
        previousButton.waitUntilClickable().click();
        waitFor(plantsTable);
    }

    public List<String> getCurrentPagePlantNames() {
        return getColumnData("Name");
    }

    public boolean isPreviousButtonDisabled() {
        WebElementFacade previousItem = findBy(
                "//ul[@class='pagination']//li[contains(@class,'page-item') and .//a[contains(text(),'Previous')]]");
        String classAttr = previousItem.getElement().getAttribute("class");
        return classAttr != null && classAttr.contains("disabled");
    }

    public boolean isNextButtonDisabled() {
        WebElementFacade nextItem = findBy(
                "//ul[@class='pagination']//li[contains(@class,'page-item') and .//a[contains(text(),'Next')]]");
        String classAttr = nextItem.getElement().getAttribute("class");
        return classAttr != null && classAttr.contains("disabled");
    }

    public int getActivePageNumber() {
        WebElementFacade activePage = findBy("//ul[@class='pagination']//li[contains(@class,'active')]//a");
        return Integer.parseInt(activePage.getText().trim());
    }

    public void clickPageNumber(int pageNumber) {
        WebElementFacade pageButton = findBy("//ul[@class='pagination']//a[text()='" + pageNumber + "']");
        pageButton.waitUntilClickable().click();
        waitFor(plantsTable);
    }

    // ==================================================================================
    // VALIDATION METHODS
    // ==================================================================================

    public boolean allPlantNamesContain(String searchTerm) {
        List<String> plantNames = getCurrentPagePlantNames();
        if (plantNames.isEmpty()) {
            return false;
        }
        return plantNames.stream()
                .allMatch(name -> name.toLowerCase().contains(searchTerm.toLowerCase()));
    }

    public int getDisplayedPlantCount() {
        return findAll(By.cssSelector("table.table tbody tr")).size();
    }

    public boolean verifyLowStockBadges(int threshold) {
        List<WebElementFacade> rows = findAll(By.cssSelector("table.table tbody tr"));

        for (WebElementFacade row : rows) {
            WebElementFacade stockCell = row.findBy(By.cssSelector("td:nth-child(4)"));
            String stockText = stockCell.getText().trim();

            int quantity;
            try {
                quantity = Integer.parseInt(stockText.split("\\s+")[0]);
            } catch (NumberFormatException e) {
                continue;
            }

            List<WebElementFacade> badges = stockCell.thenFindAll(By.cssSelector("span.badge"));
            boolean hasBadge = !badges.isEmpty() &&
                    badges.stream().anyMatch(b -> b.getText().trim().equalsIgnoreCase("Low"));

            if (quantity < threshold && !hasBadge) {
                return false;
            }

            if (quantity >= threshold && hasBadge) {
                return false;
            }
        }
        return true;
    }

    public int getLowStockBadgeCount() {
        return findAll(By.cssSelector("table.table tbody tr td span.badge.bg-danger")).size();
    }

    public boolean isNoPlantsMessageDisplayed() {
        List<WebElementFacade> rows = findAll(By.cssSelector("table.table tbody tr"));
        if (rows.isEmpty()) {
            return containsText("No plants found");
        }
        if (rows.size() == 1) {
            String rowText = rows.get(0).getText();
            return rowText.contains("No plants found");
        }
        return false;
    }

    // ==================================================================================
    // ADMIN ACTIONS (VISIBLE)
    // ==================================================================================

    public boolean isAddPlantButtonVisible() {
        return addPlantButton.isVisible();
    }

    public boolean areEditButtonsVisibleForAllRows() {
        List<WebElementFacade> rows = findAll(By.cssSelector("table.table tbody tr"));
        if (rows.isEmpty())
            return false;

        for (WebElementFacade row : rows) {
            List<WebElementFacade> editButtons = row
                    .thenFindAll(By.xpath(".//a[contains(text(),'Edit')] | .//button[contains(text(),'Edit')]"));

            if (editButtons.isEmpty()) {
                editButtons = row.thenFindAll(By.cssSelector(".bi-pencil, .fa-pencil, .bi-pencil-square"));
            }

            if (editButtons.isEmpty() || !editButtons.get(0).isVisible()) {
                return false;
            }
        }
        return true;
    }

    public boolean areDeleteButtonsVisibleForAllRows() {
        List<WebElementFacade> rows = findAll(By.cssSelector("table.table tbody tr"));
        if (rows.isEmpty())
            return false;

        for (WebElementFacade row : rows) {
            List<WebElementFacade> deleteButtons = row
                    .thenFindAll(By.xpath(".//a[contains(text(),'Delete')] | .//button[contains(text(),'Delete')]"));

            if (deleteButtons.isEmpty()) {
                deleteButtons = row.thenFindAll(By.cssSelector(".bi-trash, .fa-trash, .bi-trash-fill, .text-danger"));
            }

            if (deleteButtons.isEmpty() || !deleteButtons.get(0).isVisible()) {
                return false;
            }
        }
        return true;
    }

    // ==================================================================================
    // USER CONSTRAINTS (HIDDEN)
    // ==================================================================================

    /**
     * Checks if Edit action is HIDDEN (not visible) OR DISABLED for every plant row
     */
    public boolean areEditButtonsHiddenForAllRows() {
        List<WebElementFacade> rows = findAll(By.cssSelector("table.table tbody tr"));
        if (rows.isEmpty())
            return true;

        for (WebElementFacade row : rows) {
            List<WebElementFacade> editButtons = row
                    .thenFindAll(By.xpath(".//a[contains(text(),'Edit')] | .//button[contains(text(),'Edit')]"));
            if (editButtons.isEmpty()) {
                editButtons = row.thenFindAll(By.cssSelector(".bi-pencil, .fa-pencil, .bi-pencil-square"));
            }
            if (!editButtons.isEmpty() && editButtons.get(0).isVisible() && editButtons.get(0).isEnabled()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if Delete action is HIDDEN (not visible) OR DISABLED for every plant
     * row
     */
    public boolean areDeleteButtonsHiddenForAllRows() {
        List<WebElementFacade> rows = findAll(By.cssSelector("table.table tbody tr"));
        if (rows.isEmpty())
            return true;

        for (WebElementFacade row : rows) {
            List<WebElementFacade> deleteButtons = row
                    .thenFindAll(By.xpath(".//a[contains(text(),'Delete')] | .//button[contains(text(),'Delete')]"));
            if (deleteButtons.isEmpty()) {
                deleteButtons = row.thenFindAll(By.cssSelector(".bi-trash, .fa-trash, .bi-trash-fill"));
            }
            if (!deleteButtons.isEmpty() && deleteButtons.get(0).isVisible() && deleteButtons.get(0).isEnabled()) {
                return false;
            }
        }
        return true;
    }
}
