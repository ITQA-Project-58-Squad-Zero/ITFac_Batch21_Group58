package pages.categories;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@DefaultUrl("/ui/categories")
public class CategoriesPage extends PageObject {

    @FindBy(css = "table.table")
    WebElementFacade categoriesTable;

    @FindBy(css = "table.table thead th")
    List<WebElementFacade> tableHeaders;

    @FindBy(css = "table.table tbody tr")
    List<WebElementFacade> tableRows;

    @FindBy(css = "ul.pagination")
    WebElementFacade paginationContainer;

    @FindBy(xpath = "//ul[@class='pagination']//a[contains(text(),'Next')]")
    WebElementFacade nextPageLink;

    @FindBy(xpath = "//ul[@class='pagination']//a[contains(text(),'Previous')]")
    WebElementFacade previousPageLink;

    @FindBy(css = "ul.pagination li.page-item.active")
    WebElementFacade activePageItem;

    public boolean isCategoriesTableDisplayed() {
        return categoriesTable.isDisplayed();
    }

    public List<String> getColumnHeaders() {
        return tableHeaders.stream()
                .map(WebElementFacade::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean isPaginationVisible() {
        return paginationContainer.isPresent() && paginationContainer.isDisplayed();
    }

    public void clickNextPage() {
        nextPageLink.waitUntilClickable().click();
    }

    public boolean isNextPageEnabled() {
        return nextPageLink.isPresent()
                && nextPageLink.getAttribute("href") != null
                && !nextPageLink.findBy("..").getAttribute("class").contains("disabled");
    }

    public String getActivePageNumber() {
        return activePageItem.getText().trim();
    }

    public List<String> getFirstRowCellValues() {
        if (tableRows.isEmpty()) {
            return List.of();
        }
        return tableRows.get(0).thenFindAll("td")
                .stream()
                .map(WebElementFacade::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean areEditAndDeleteDisabled() {
        List<WebElementFacade> editLinks = findAll("table.table tbody tr td a[href*='/ui/categories/edit']");
        List<WebElementFacade> deleteButtons = findAll("table.table tbody tr button.btn-outline-danger");
        if (editLinks.isEmpty() && deleteButtons.isEmpty()) {
            return true;
        }
        boolean allEditDisabled = editLinks.stream()
                .allMatch(el -> "true".equalsIgnoreCase(el.getAttribute("disabled")));
        boolean allDeleteDisabled = deleteButtons.stream()
                .allMatch(el -> "true".equalsIgnoreCase(el.getAttribute("disabled")));
        return allEditDisabled && allDeleteDisabled;
    }
}
