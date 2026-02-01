package pages.plants;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

@DefaultUrl("/ui/plants/add")
public class AddPlantPage extends PageObject {

    @FindBy(xpath = "//h3[contains(text(),'Add Plant')]")
    WebElementFacade pageHeader;

    @FindBy(id = "name")
    WebElementFacade plantNameInput;

    @FindBy(id = "categoryId")
    WebElementFacade categoryDropdown;

    @FindBy(id = "price")
    WebElementFacade priceInput;

    @FindBy(id = "quantity")
    WebElementFacade quantityInput;

    @FindBy(xpath = "//button[contains(text(),'Save')]")
    WebElementFacade saveButton;

    public boolean isPageOpen() {
        return getDriver().getCurrentUrl().contains("/ui/plants/add") && pageHeader.isVisible();
    }

    public void enterPlantName(String name) {
        plantNameInput.type(name);
    }

    public void selectCategory(String category) {
        categoryDropdown.selectByVisibleText(category);
    }

    public void enterPrice(String price) {
        priceInput.type(price);
    }

    public void enterQuantity(String quantity) {
        quantityInput.type(quantity);
    }

    public void clickSave() {
        saveButton.click();
    }

    public boolean isErrorMessageDisplayed(String message) {
        return findAll(net.serenitybdd.core.annotations.findby.By.xpath("//*[contains(text(),'" + message + "')]")).size() > 0;
    }
}
