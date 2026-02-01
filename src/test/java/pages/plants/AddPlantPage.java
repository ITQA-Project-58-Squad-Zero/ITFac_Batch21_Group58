package pages.plant_add_edit;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

@DefaultUrl("/ui/plants/add")
public class AddPlantPage extends PageObject {

    @FindBy(xpath = "//h3[contains(text(),'Add Plant')]")
    WebElementFacade pageHeader;

    public boolean isPageOpen() {
        return getDriver().getCurrentUrl().contains("/ui/plants/add") && pageHeader.isVisible();
    }
}
