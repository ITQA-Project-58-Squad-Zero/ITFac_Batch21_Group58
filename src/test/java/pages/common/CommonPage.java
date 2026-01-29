package pages.common;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonPage extends PageObject {

    @FindBy(css = "h3.mb-4")
    WebElementFacade pageHeader;

    public void shouldSeePageTitle(String expectedTitle) {
        pageHeader.waitUntilVisible();
        assertThat(pageHeader.getText().trim())
                .as("Page Header")
                .containsIgnoringCase(expectedTitle);
    }
}
