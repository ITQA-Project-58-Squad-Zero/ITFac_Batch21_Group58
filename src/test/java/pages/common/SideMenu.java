package pages.common;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

public class SideMenu extends PageObject {

    @FindBy(linkText = "Dashboard")
    WebElementFacade dashboardLink;

    @FindBy(linkText = "Categories")
    WebElementFacade categoriesLink;

    @FindBy(linkText = "Plants")
    WebElementFacade plantsLink;

    @FindBy(linkText = "Sales")
    WebElementFacade salesLink;

    @FindBy(linkText = "Inventory")
    WebElementFacade inventoryLink;

    @FindBy(linkText = "Logout")
    WebElementFacade logoutLink;

    public void navigateTo(String pageName) {
        switch (pageName.trim().toLowerCase()) {
            case "dashboard":
                dashboardLink.click();
                break;
            case "categories":
                categoriesLink.click();
                break;
            case "plants":
                plantsLink.click();
                break;
            case "sales":
                salesLink.click();
                break;
            case "inventory":
                inventoryLink.click();
                break;
            case "logout":
                logoutLink.click();
                break;
            default:
                throw new IllegalArgumentException("Unknown page: " + pageName);
        }
    }

    public boolean isDashboardActive() {
        return dashboardLink.getAttribute("class").contains("active");
    }

    public boolean isInventoryDisabled() {
        return inventoryLink.getAttribute("class").contains("disabled");
    }

    public String getInventoryTooltip() {
        return inventoryLink.getAttribute("title");
    }
}
