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
}

