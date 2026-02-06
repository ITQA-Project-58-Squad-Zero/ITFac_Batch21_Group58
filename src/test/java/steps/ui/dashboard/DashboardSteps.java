package steps.ui.dashboard;

import io.cucumber.java.en.Then;
import pages.dashboard.DashboardPage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DashboardSteps {

    DashboardPage dashboardPage;

    @Then("Admin should see the Dashboard page")
    public void adminShouldSeeDashboardPage() {
        assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard page is not displayed after login");
    }

    @Then("User should see the Dashboard page")
    public void userShouldSeeDashboardPage() {
        assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard page is not displayed for User after login");
    }

    @Then("the Dashboard should display the Categories card")
    public void dashboardShouldDisplayCategoriesCard() {
        assertTrue(dashboardPage.isCategoriesCardDisplayed(),
                "Categories card is not displayed on Dashboard");
    }

    @Then("the Dashboard should display the Plants card")
    public void dashboardShouldDisplayPlantsCard() {
        assertTrue(dashboardPage.isPlantsCardDisplayed(),
                "Plants card is not displayed on Dashboard");
    }

    @Then("the Dashboard should display the Sales card")
    public void dashboardShouldDisplaySalesCard() {
        assertTrue(dashboardPage.isSalesCardDisplayed(),
                "Sales card is not displayed on Dashboard");
    }

    @Then("the Dashboard should display the Inventory card")
    public void dashboardShouldDisplayInventoryCard() {
        assertTrue(dashboardPage.isInventoryCardDisplayed(),
                "Inventory card is not displayed on Dashboard");
    }

    pages.common.SideMenu sideMenu;

    @Then("the Dashboard menu item should be highlighted as active")
    public void verifyDashboardMenuHighlight() {
        assertTrue(sideMenu.isDashboardActive(),
                "Dashboard menu item is not highlighted as active");
    }

    @Then("the Categories card should display {string} Main categories")
    public void verifyMainCategoryCount(String expectedCount) {
        String actualCount = dashboardPage.getMainCategoryCount();
        assertTrue(actualCount.equals(expectedCount),
                "Expected " + expectedCount + " main categories but found " + actualCount);
    }

    @Then("the Categories card should display {string} Sub categories")
    public void verifySubCategoryCount(String expectedCount) {
        String actualCount = dashboardPage.getSubCategoryCount();
        assertTrue(actualCount.equals(expectedCount),
                "Expected " + expectedCount + " sub categories but found " + actualCount);
    }

    @Then("the Plants card should display {string} Total plants")
    public void verifyTotalPlantsCount(String expectedCount) {
        String actualCount = dashboardPage.getTotalPlantsCount();
        assertTrue(actualCount.equals(expectedCount),
                "Expected " + expectedCount + " total plants but found " + actualCount);
    }

    @Then("the Plants card should display {string} Low Stock plants")
    public void verifyLowStockPlantsCount(String expectedCount) {
        String actualCount = dashboardPage.getLowStockPlantsCount();
        assertTrue(actualCount.equals(expectedCount),
                "Expected " + expectedCount + " low stock plants but found " + actualCount);
    }

    @Then("the Sales card should display valid Revenue")
    public void verifyRevenue() {
        String actualRevenue = dashboardPage.getRevenue();
        assertTrue(actualRevenue.startsWith("Rs "),
                "Expected revenue to start with 'Rs ' but found " + actualRevenue);
        // Extract number and verify it's a valid number
        String amount = actualRevenue.replace("Rs ", "").trim();
        try {
            Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            assertTrue(false, "Revenue amount '" + amount + "' is not a valid number");
        }
    }

    @Then("the Sales card should display valid Sales count")
    public void verifySalesCount() {
        String actualCount = dashboardPage.getSalesCount();
        try {
            Integer.parseInt(actualCount);
        } catch (NumberFormatException e) {
            assertTrue(false, "Sales count '" + actualCount + "' is not a valid integer");
        }
        assertTrue(Integer.parseInt(actualCount) >= 0, "Sales count should be non-negative");
    }

    @io.cucumber.java.en.When("Admin clicks Categories from navigation menu")
    public void clickCategoriesFromNavigationMenu() {
        sideMenu.navigateTo("categories");
    }

    pages.categories.categories_bhathiya.CategoriesPage categoriesPage;

    @Then("the Categories page should be displayed")
    public void verifyCategoriesPageDisplayed() {
        assertTrue(categoriesPage.isCategoriesTableDisplayed(),
                "Categories page (table) is not displayed");
    }

    @io.cucumber.java.en.When("Admin clicks Plants from navigation menu")
    public void clickPlantsFromNavigationMenu() {
        sideMenu.navigateTo("plants");
    }

    pages.plants.search_sort.PlantsPage plantsPage;

    @Then("the Plants page should be displayed")
    public void verifyPlantsPageDisplayed() {
        assertTrue(plantsPage.isTableDisplayed(),
                "Plants page (table) is not displayed");
    }

    @io.cucumber.java.en.When("Admin clicks Inventory from navigation menu")
    public void clickInventoryFromNavigationMenu() {
        sideMenu.navigateTo("inventory");
    }

    pages.inventory.InventoryPage inventoryPage;

    @Then("the Inventory page should be displayed")
    public void verifyInventoryPageDisplayed() {
        assertTrue(inventoryPage.isInventoryPageDisplayed(),
                "Inventory page is not displayed");
    }

    @Then("the Inventory link should be disabled with tooltip {string}")
    public void verifyInventoryLinkDisabled(String expectedTooltip) {
        assertTrue(sideMenu.isInventoryDisabled(), "Inventory link should be disabled");
        String actualTooltip = sideMenu.getInventoryTooltip();
        assertEquals(expectedTooltip, actualTooltip, "Tooltip text mismatch");
    }
}
