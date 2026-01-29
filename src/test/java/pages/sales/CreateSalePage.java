package pages.sales;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;

@DefaultUrl("/ui/sales/new")
public class CreateSalePage extends PageObject {

    public boolean isPageOpen() {
        return getDriver().getCurrentUrl().contains("/ui/sales/new");
    }
}
