package steps.ui.common;

import io.cucumber.java.en.When;
import pages.common.SideMenu;
import pages.common.CommonPage;

public class NavigationSteps {

    SideMenu sideMenu;
    CommonPage commonPage;

    @When("Admin navigates to the {string} page")
    public void navigateToPage(String pageName) {
        sideMenu.navigateTo(pageName);
        commonPage.shouldSeePageTitle(pageName);
    }
}
