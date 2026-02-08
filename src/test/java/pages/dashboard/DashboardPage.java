package pages.dashboard;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

import java.util.List;

@DefaultUrl("/ui/dashboard")
public class DashboardPage extends PageObject {

    @FindBy(css = "h3.mb-4")
    WebElementFacade pageHeader;

    @FindBy(css = "div.dashboard-card")
    List<WebElementFacade> dashboardCards;

     
    @FindBy(xpath = "//h6[contains(text(), 'Categories')]")
    WebElementFacade categoriesCardTitle;

    @FindBy(xpath = "//a[contains(text(), 'Manage Categories')]")
    WebElementFacade manageCategoriesButton;

    @FindBy(xpath = "//div[contains(@class, 'card-body') and .//h6[contains(text(), 'Categories')]]//div[contains(text(), 'Main')]/preceding-sibling::div")
    WebElementFacade mainCategoriesCount;

    @FindBy(xpath = "//div[contains(@class, 'card-body') and .//h6[contains(text(), 'Categories')]]//div[contains(text(), 'Sub')]/preceding-sibling::div")
    WebElementFacade subCategoriesCount;

     
    @FindBy(xpath = "//h6[contains(text(), 'Plants')]")
    WebElementFacade plantsCardTitle;

    @FindBy(xpath = "//div[contains(@class, 'card-body') and .//h6[contains(text(), 'Plants')]]//div[contains(text(), 'Total')]/preceding-sibling::div")
    WebElementFacade totalPlantsCount;

    @FindBy(xpath = "//div[contains(@class, 'card-body') and .//h6[contains(text(), 'Plants')]]//div[contains(text(), 'Low Stock')]/preceding-sibling::div")
    WebElementFacade lowStockPlantsCount;

    @FindBy(xpath = "//a[contains(text(), 'Manage Plants')]")
    WebElementFacade managePlantsButton;

     
    @FindBy(xpath = "//h6[contains(text(), 'Sales')]")
    WebElementFacade salesCardTitle;

    @FindBy(xpath = "//div[contains(@class, 'card-body') and .//h6[contains(text(), 'Sales')]]//div[contains(text(), 'Revenue')]/preceding-sibling::div")
    WebElementFacade revenueCount;

    @FindBy(xpath = "//div[contains(@class, 'card-body') and .//h6[contains(text(), 'Sales')]]//div[contains(@class, 'text-end')]//div[contains(@class, 'fw-bold')]")
    WebElementFacade salesCount;

    @FindBy(xpath = "//a[contains(text(), 'View Sales')]")
    WebElementFacade viewSalesButton;

     
    @FindBy(xpath = "//h6[contains(text(), 'Inventory')]")
    WebElementFacade inventoryCardTitle;

    public boolean isDashboardDisplayed() {
        pageHeader.waitUntilVisible();
        return pageHeader.isVisible() &&
                pageHeader.getText().trim().equalsIgnoreCase("Dashboard");
    }

    public boolean isCategoriesCardDisplayed() {
        return categoriesCardTitle.isVisible() &&
                manageCategoriesButton.isVisible();
    }

    public boolean isPlantsCardDisplayed() {
        return plantsCardTitle.isVisible() &&
                managePlantsButton.isVisible();
    }

    public boolean isSalesCardDisplayed() {
        return salesCardTitle.isVisible() &&
                viewSalesButton.isVisible();
    }

    public boolean isInventoryCardDisplayed() {
        return inventoryCardTitle.isVisible();
    }

    public int getDashboardCardCount() {
        return dashboardCards.size();
    }

    public String getMainCategoryCount() {
        return mainCategoriesCount.getText();
    }

    public String getSubCategoryCount() {
        return subCategoriesCount.getText();
    }

    public String getTotalPlantsCount() {
        return totalPlantsCount.getText();
    }

    public String getLowStockPlantsCount() {
        return lowStockPlantsCount.getText();
    }

    public String getRevenue() {
        return revenueCount.getText().replace("\n", " ").trim();
    }

    public String getSalesCount() {
        return salesCount.getText().trim();
    }
}
