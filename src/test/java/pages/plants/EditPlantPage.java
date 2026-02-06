package pages.plants;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

@DefaultUrl("/ui/plants/edit/{1}")
public class EditPlantPage extends PageObject {

    @FindBy(xpath = "//h3[contains(text(),'Edit Plant')]")
    WebElementFacade pageHeader;

    public void navigateTo(String id) {
        open("edit.plant", withParameters(id));
    }

    public boolean isPageHeaderVisible() {
        return pageHeader.isVisible();
    }
}
