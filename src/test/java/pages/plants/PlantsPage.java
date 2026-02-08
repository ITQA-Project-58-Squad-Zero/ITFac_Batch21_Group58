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

    public boolean isAddPlantButtonVisible() {
        return addPlantButton.isVisible();
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

    @FindBy(css = "table tbody tr:first-child td:first-child")
    WebElementFacade firstPlantIdCell;

    public int getFirstPlantId() {
        if (firstPlantIdCell.isVisible()) {
            String idText = firstPlantIdCell.getText().trim();
            try {
                return Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
