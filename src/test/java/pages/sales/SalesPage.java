package pages.sales;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@DefaultUrl("/ui/sales")
public class SalesPage extends PageObject {

    @FindBy(css = "table.table")
    WebElementFacade salesTable;

    @FindBy(css = "table.table thead th")
    List<WebElementFacade> tableHeaders;

    @FindBy(css = "a[href='/ui/sales']")
    WebElementFacade salesMenuLink;

    @FindBy(xpath = "//a[contains(text(), 'Sell Plant')]")
    WebElementFacade sellPlantButton;

    @FindBy(css = "table.table tbody tr")
    List<WebElementFacade> tableRows;

    @FindBy(css = "table.table tbody tr td form button.btn-outline-danger, table.table tbody tr td button.btn-outline-danger")
    List<WebElementFacade> deleteButtons;

    public void clickSalesMenu() {
        salesMenuLink.click();
    }

    public boolean isSellPlantButtonVisible() {
        return sellPlantButton.isVisible();
    }

    public void clickSellPlantButton() {
        sellPlantButton.click();
    }

    public boolean isSalesTableDisplayed() {
        return salesTable.isDisplayed();
    }

    public List<String> getColumnHeaders() {
        return tableHeaders.stream()
                .map(WebElementFacade::getText)
                .collect(Collectors.toList());
    }

    public boolean isSalePresent(String plantName) {
        for (WebElementFacade row : tableRows) {
            if (row.getText().contains(plantName)) {
                return true;
            }
        }
        return false;
    }

    public boolean areDeleteButtonsVisible() {
        // Check if any delete buttons are visible
        for (WebElementFacade deleteButton : deleteButtons) {
            if (deleteButton.isVisible()) {
                return true;
            }
        }
        return false;
    }

    public int getDeleteButtonCount() {
        return deleteButtons.size();
    }

    public void clickColumnHeader(String columnName) {
        // Assuming headers are <a> tags inside <th>
        // Map column name to text in header
        // Plant, Quantity, Total Price, Sold At
        findBy("//th/a[contains(text(), '" + columnName + "')]").click();
    }

    public List<String> getColumnData(String columnName) {
        // Map column name to index (1-based for xpath nth-child)
        int columnIndex = 0;
        switch (columnName) {
            case "Plant": columnIndex = 1; break; // Name is first
            case "Quantity": columnIndex = 2; break;
            case "Total Price": columnIndex = 3; break;
            case "Sold At": columnIndex = 4; break;
            default: throw new IllegalArgumentException("Unknown column: " + columnName);
        }

        return findAll("//table[contains(@class,'table')]//tbody//tr//td[" + columnIndex + "]")
                .stream()
                .map(WebElementFacade::getText)
                .collect(Collectors.toList());
    }

    public boolean isSorted(List<String> data, String order) {
        if (data.isEmpty() || data.size() == 1) return true;

        for (int i = 0; i < data.size() - 1; i++) {
            String current = data.get(i);
            String next = data.get(i + 1);
            
            // Try numeric comparison first for Price/Quantity
            try {
                 double v1 = Double.parseDouble(current.replaceAll("[^\\d.]", ""));
                 double v2 = Double.parseDouble(next.replaceAll("[^\\d.]", ""));
                 if (order.equalsIgnoreCase("ascending") && v1 > v2) return false;
                 if (order.equalsIgnoreCase("descending") && v1 < v2) return false;
            } catch (NumberFormatException e) {
                 // Fallback to string comparison
                 int comparison = current.compareToIgnoreCase(next);
                 if (order.equalsIgnoreCase("ascending") && comparison > 0) return false;
                 if (order.equalsIgnoreCase("descending") && comparison < 0) return false;
            }
        }
        return true;
    }
}

