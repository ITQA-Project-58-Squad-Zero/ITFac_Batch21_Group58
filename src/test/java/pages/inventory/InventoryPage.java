package pages.inventory;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

public class InventoryPage extends PageObject {

    @FindBy(xpath = "//h1[contains(text(),'Inventory')] | //h2[contains(text(),'Inventory')]")
    WebElementFacade inventoryHeader;

    public boolean isInventoryPageDisplayed() {
        return inventoryHeader.isVisible();
    }
}
