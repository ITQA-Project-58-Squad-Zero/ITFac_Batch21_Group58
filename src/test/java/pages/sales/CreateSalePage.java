package pages.sales;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

@DefaultUrl("/ui/sales/new")
public class CreateSalePage extends PageObject {

    @FindBy(id = "plantId")
    WebElementFacade plantDropdown;

    @FindBy(id = "quantity")
    WebElementFacade quantityInput;

    @FindBy(css = "button.btn-primary")
    WebElementFacade sellButton;

    @FindBy(css = ".text-danger, .invalid-feedback, .alert-danger, .error-message")
    List<WebElementFacade> validationErrors;

    public boolean isPageOpen() {
        return getDriver().getCurrentUrl().contains("/ui/sales/new");
    }

    public void selectFirstAvailablePlant() {
        Select select = new Select(plantDropdown);
         
        if (select.getOptions().size() > 1) {
            select.selectByIndex(1);
        }
    }

    public void enterQuantity(String quantity) {
        quantityInput.clear();
        if (quantity != null && !quantity.isEmpty()) {
            quantityInput.type(quantity);
        }
    }

    public void clickSellButton() {
        sellButton.click();
    }

    public String getSelectedPlantName() {
        Select select = new Select(plantDropdown);
        String selectedText = select.getFirstSelectedOption().getText();
         
        if (selectedText.contains("(")) {
            return selectedText.substring(0, selectedText.indexOf("(")).trim();
        }
        return selectedText;
    }

    public boolean isValidationErrorDisplayed() {
        for (WebElementFacade error : validationErrors) {
            if (error.isVisible() && !error.getText().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean isStillOnCreateSalePage() {
        return getDriver().getCurrentUrl().contains("/ui/sales/new");
    }
}

