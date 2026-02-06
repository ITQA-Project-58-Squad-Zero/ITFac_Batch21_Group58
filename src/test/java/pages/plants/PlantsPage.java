package pages.plants;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

@DefaultUrl("/ui/plants")
public class PlantsPage extends PageObject {

    @FindBy(linkText = "Add a Plant")
    WebElementFacade addPlantButton;

    @FindBy(linkText = "Edit")
    WebElementFacade editButton;

    @FindBy(css = "button[data-bs-target='#deleteModal']")
    WebElementFacade deleteButton;

    public void clickAddPlantButton() {
        addPlantButton.click();
    }

    public void shouldAddPlantButtonNotBeVisible() {
        addPlantButton.shouldNotBeVisible();
    }

    public void shouldEditButtonNotBeVisible() {
        editButton.shouldNotBeVisible();
    }

    public void shouldDeleteButtonNotBeVisible() {
        deleteButton.shouldNotBeVisible();
    }
}
