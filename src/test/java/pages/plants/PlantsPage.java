package pages.plant_add_edit;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

@DefaultUrl("/ui/plants")
public class PlantsPage extends PageObject {

    @FindBy(linkText = "Add a Plant")
    WebElementFacade addPlantButton;

    public void clickAddPlantButton() {
        addPlantButton.click();
    }
}
