package pages.category;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

@DefaultUrl("/ui/categories")
public class CategoryPage extends PageObject {

    @FindBy(css = "a[href='/ui/categories/add']")
    WebElementFacade addCategoryButton;

    @FindBy(css = "h3.mb-4")
    WebElementFacade pageHeader;

    @FindBy(name = "name")
    WebElementFacade categoryNameField;

    @FindBy(css = "button[type='submit']")
    WebElementFacade saveButton;

    @FindBy(css = ".main-content .alert-danger, .main-content .invalid-feedback, form .text-danger, .main-content .error-message")
    WebElementFacade validationMessage;

    public void clickAddCategoryButton() {
        addCategoryButton.waitUntilClickable().click();
    }

    public boolean isAddCategoryPageDisplayed() {
        return getDriver().getCurrentUrl().contains("/ui/categories/add");
    }

    public String getPageTitle() {
        return pageHeader.waitUntilVisible().getText();
    }

    public void leaveCategoryNameEmpty() {
        categoryNameField.waitUntilVisible().clear();
    }

    public void enterCategoryName(String name) {
        categoryNameField.waitUntilVisible().clear();
        categoryNameField.type(name);
    }

    public void clickSaveButton() {
        saveButton.waitUntilClickable().click();
    }

    public String getValidationMessage() {
        return validationMessage.waitUntilVisible().getText();
    }

    public boolean isValidationMessageDisplayed() {
        return validationMessage.isVisible();
    }
}
