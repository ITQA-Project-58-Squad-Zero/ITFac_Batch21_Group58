package pages.plants;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;
import java.time.Duration;

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
        return withTimeoutOf(Duration.ofSeconds(5))
                .find(net.serenitybdd.core.annotations.findby.By.xpath("//div[contains(@class,'text-danger') and contains(text(),'" + message + "')]"))
                .isVisible();
    }

    public void clearAllFields() {
        plantNameInput.clear();
        categoryDropdown.selectByVisibleText("-- Select Sub Category --"); // Assuming this resets it or just leaving it alone if it's default
        priceInput.clear();
        quantityInput.clear();
    }
}
