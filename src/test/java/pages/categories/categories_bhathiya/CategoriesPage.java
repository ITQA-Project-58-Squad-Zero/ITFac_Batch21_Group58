package pages.categories.categories_bhathiya;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.annotations.findby.FindBy;

import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

@DefaultUrl("/ui/categories")
public class CategoriesPage extends PageObject {

    @FindBy(css = "table.table")
    WebElementFacade categoriesTable;

    @FindBy(css = "table.table thead th")
    List<WebElementFacade> tableHeaders;

    @FindBy(css = "table.table tbody tr")
    List<WebElementFacade> tableRows;

    @FindBy(css = "ul.pagination")
    WebElementFacade paginationContainer;

    @FindBy(xpath = "//ul[@class='pagination']//a[contains(text(),'Next')]")
    WebElementFacade nextPageLink;

    @FindBy(xpath = "//ul[@class='pagination']//a[contains(text(),'Previous')]")
    WebElementFacade previousPageLink;

    @FindBy(css = "ul.pagination li.page-item.active")
    WebElementFacade activePageItem;

    @FindBy(xpath = "//a[contains(text(),'Add Category') or contains(@href,'/ui/categories/add') or contains(@href,'/ui/categories/new')]")
    WebElementFacade addCategoryButton;

    @FindBy(css = "input[name='name']")
    WebElementFacade searchInput;

    @FindBy(css = "select[name='parentId']")
    WebElementFacade parentFilterDropdown;

    @FindBy(css = "button[type='submit']")
    WebElementFacade searchButton;

    @FindBy(xpath = "//tbody//td[contains(text(),'No category found') or contains(text(),'No categories found')]")
    WebElementFacade noCategoryFoundMessage;

    public boolean isCategoriesTableDisplayed() {
        return categoriesTable.isDisplayed();
    }

    public List<String> getColumnHeaders() {
        return tableHeaders.stream()
                .map(WebElementFacade::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean isPaginationVisible() {
        return paginationContainer.isPresent() && paginationContainer.isDisplayed();
    }

    public void clickNextPage() {
        nextPageLink.waitUntilClickable().click();
    }

    public boolean isNextPageEnabled() {
        return nextPageLink.isPresent()
                && nextPageLink.getAttribute("href") != null
                && !nextPageLink.findBy("..").getAttribute("class").contains("disabled");
    }

    public String getActivePageNumber() {
        return activePageItem.getText().trim();
    }

    public List<String> getFirstRowCellValues() {
        if (tableRows.isEmpty()) {
            return List.of();
        }
        return tableRows.get(0).thenFindAll("td")
                .stream()
                .map(WebElementFacade::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean areEditAndDeleteDisabled() {
        List<WebElementFacade> editLinks = findAll("table.table tbody tr td a[href*='/ui/categories/edit']");
        List<WebElementFacade> deleteButtons = findAll("table.table tbody tr button.btn-outline-danger");
        if (editLinks.isEmpty() && deleteButtons.isEmpty()) {
            return true;
        }
        boolean allEditDisabled = editLinks.stream()
                .allMatch(el -> "true".equalsIgnoreCase(el.getAttribute("disabled")));
        boolean allDeleteDisabled = deleteButtons.stream()
                .allMatch(el -> "true".equalsIgnoreCase(el.getAttribute("disabled")));
        return allEditDisabled && allDeleteDisabled;
    }

    public boolean isAddCategoryButtonVisible() {
        return addCategoryButton.isPresent() && addCategoryButton.isDisplayed();
    }

    public void enterSearchTerm(String searchTerm) {
        $(By.cssSelector("input[name='name']")).waitUntilVisible().clear();
        $(By.cssSelector("input[name='name']")).type(searchTerm);
    }

    public void clickSearchButton() {
        $(By.cssSelector("button[type='submit']")).waitUntilClickable().click();
         
        waitFor(1).second();
        categoriesTable.waitUntilVisible();
    }

    public String getSelectedParentValue() {
        WebElementFacade dropdown = $(By.cssSelector("select[name='parentId']"));
        return dropdown.getSelectedValue();
    }

    public String getSelectedParentText() {
        WebElementFacade dropdown = $(By.cssSelector("select[name='parentId']"));
        return dropdown.getSelectedVisibleTextValue();
    }

    public void selectParentCategory(String parentName) {
        WebElementFacade dropdown = $(By.cssSelector("select[name='parentId']"));
        dropdown.waitUntilVisible();
        
         
        dropdown.click();
        waitFor(500).milliseconds();
        
         
        List<WebElementFacade> options = dropdown.thenFindAll("option");
        String normalizedParentName = parentName.trim().toLowerCase();
        String targetValue = null;
        String targetText = null;
        
         
        for (WebElementFacade option : options) {
            String optionText = option.getText().trim();
            String optionValue = option.getAttribute("value");
            
             
            if (optionValue == null || optionValue.isEmpty() || 
                optionText.toLowerCase().contains("all parent")) {
                continue;
            }
            
            String normalizedOptionText = optionText.toLowerCase();
            
             
            if (normalizedOptionText.equals(normalizedParentName) || 
                normalizedOptionText.contains(normalizedParentName) ||
                normalizedParentName.contains(normalizedOptionText)) {
                targetValue = optionValue;
                targetText = optionText;
                break;
            }
        }
        
        if (targetValue == null) {
            throw new RuntimeException("Could not find parent category option matching: " + parentName);
        }
        
         
        try {
             
            dropdown.selectByValue(targetValue);
            waitFor(1).second();
            
             
            String currentValue = dropdown.getSelectedValue();
            if (currentValue != null && currentValue.equals(targetValue)) {
                return;  
            }
        } catch (Exception e) {
             
        }
        
        try {
             
            dropdown.selectByVisibleText(targetText);
            waitFor(1).second();
            
             
            String currentValue = dropdown.getSelectedValue();
            if (currentValue != null && currentValue.equals(targetValue)) {
                return;  
            }
        } catch (Exception e) {
             
        }
        
         
        try {
            evaluateJavascript("arguments[0].value = arguments[1];", dropdown, targetValue);
            waitFor(1).second();
            
             
            evaluateJavascript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", dropdown);
            waitFor(500).milliseconds();
            
             
            String currentValue = dropdown.getSelectedValue();
            if (currentValue != null && currentValue.equals(targetValue)) {
                return;  
            }
        } catch (Exception e) {
             
            for (WebElementFacade option : options) {
                String optionValue = option.getAttribute("value");
                if (targetValue.equals(optionValue)) {
                    option.click();
                    waitFor(1).second();
                    return;
                }
            }
        }
        
         
        throw new RuntimeException("Failed to select parent category: " + parentName + 
                ". Tried value: " + targetValue + ", text: " + targetText);
    }

    public List<String> getAllCategoryNames() {
        return findAll("table.table tbody tr td:nth-child(2)")
                .stream()
                .map(WebElementFacade::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public List<String> getAllParentNames() {
        return findAll("table.table tbody tr td:nth-child(3)")
                .stream()
                .map(WebElementFacade::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public List<String> getAllCategoryIds() {
        return findAll("table.table tbody tr td:first-child")
                .stream()
                .map(WebElementFacade::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public void clickColumnHeader(String columnName) {
        String xpath = String.format("//th//a[contains(text(),'%s')]", columnName);
        $(By.xpath(xpath)).waitUntilClickable().click();
         
        waitFor(2).seconds();
        categoriesTable.waitUntilVisible();
    }

    public void clickColumnHeaderToAscending(String columnName) {
        clickColumnHeader(columnName);
         
        if (columnName.equals("Name")) {
            List<String> names = getAllCategoryNames();
            if (!names.isEmpty()) {
                List<String> sortedAsc = names.stream().sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
                 
                if (!names.equals(sortedAsc)) {
                    clickColumnHeader(columnName);
                }
            }
        } else if (columnName.equals("ID")) {
            List<String> ids = getAllCategoryIds();
            if (!ids.isEmpty()) {
                List<Integer> idNumbers = ids.stream()
                        .map(id -> id.equals("-") || id.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(id))
                        .collect(Collectors.toList());
                List<Integer> sortedAsc = idNumbers.stream().sorted().collect(Collectors.toList());
                if (!idNumbers.equals(sortedAsc)) {
                    clickColumnHeader(columnName);
                }
            }
        } else if (columnName.equals("Parent")) {
            List<String> parents = getAllParentNames();
            if (!parents.isEmpty()) {
                List<String> sortedAsc = parents.stream()
                        .map(p -> p.equals("-") ? "" : p)
                        .sorted(String.CASE_INSENSITIVE_ORDER)
                        .collect(Collectors.toList());
                List<String> actualParents = parents.stream()
                        .map(p -> p.equals("-") ? "" : p)
                        .collect(Collectors.toList());
                if (!actualParents.equals(sortedAsc)) {
                    clickColumnHeader(columnName);
                }
            }
        }
    }

    public boolean isNoCategoryFoundMessageDisplayed() {
        List<WebElementFacade> emptyRows = findAll("table.table tbody tr");
        if (emptyRows.isEmpty()) {
            return true;
        }
        String tbodyText = $(By.cssSelector("table.table tbody")).getText().toLowerCase();
        return tbodyText.contains("no category found") || 
               tbodyText.contains("no categories found") ||
               tbodyText.contains("no records");
    }

    public void searchForNonExistentCategory() {
        enterSearchTerm("NonExistentCategoryXYZ123");
        clickSearchButton();
    }

    public int getCurrentPageRowCount() {
        return tableRows.size();
    }

    public String getFirstRowData() {
        if (tableRows.isEmpty()) {
            return "";
        }
        return tableRows.get(0).getText();
    }

    public void clearSearchText() {
        $(By.cssSelector("input[name='name']")).waitUntilVisible().clear();
    }

    public void clearParentFilter() {
        WebElementFacade dropdown = $(By.cssSelector("select[name='parentId']"));
        dropdown.waitUntilVisible();
         
        dropdown.selectByValue("");
        waitFor(500).milliseconds();
    }

    public void clickPreviousPage() {
        $(By.xpath("//ul[@class='pagination']//a[contains(text(),'Previous')]"))
                .waitUntilClickable().click();
        waitFor(1).second();
        categoriesTable.waitUntilVisible();
    }

    public boolean isPreviousPageEnabled() {
        WebElementFacade previousLink = $(By.xpath("//ul[@class='pagination']//a[contains(text(),'Previous')]"));
        if (!previousLink.isPresent()) {
            return false;
        }
        return previousLink.getAttribute("href") != null &&
               !previousLink.findBy("..").getAttribute("class").contains("disabled");
    }

    public boolean isAddCategoryButtonNotVisible() {
        return !addCategoryButton.isPresent() || !addCategoryButton.isDisplayed();
    }

    public int getTotalCategoryCount() {
        return tableRows.size();
    }

    public List<String> getAvailableParentOptions() {
        WebElementFacade dropdown = $(By.cssSelector("select[name='parentId']"));
        dropdown.waitUntilVisible();
        List<WebElementFacade> options = dropdown.thenFindAll(By.tagName("option"));
        return options.stream()
                .map(WebElementFacade::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public boolean verifyCombinedFilter(String parentName, String keyword) {
        List<String> categoryNames = getAllCategoryNames();
        List<String> parentNames = getAllParentNames();
        
        if (categoryNames.isEmpty()) {
            return false;  
        }
        
        String normalizedKeyword = keyword.trim().toLowerCase();
        String normalizedParent = parentName.trim().toLowerCase();
        
        for (int i = 0; i < categoryNames.size(); i++) {
            String name = categoryNames.get(i).toLowerCase();
            String parent = parentNames.get(i).trim().toLowerCase();
            
             
            boolean nameMatches = name.contains(normalizedKeyword);
            
             
            boolean parentMatches = parent.equals("-") ? false :
                    (parent.equals(normalizedParent) || parent.contains(normalizedParent));
            
            if (!nameMatches || !parentMatches) {
                return false;
            }
        }
        return true;
    }

    public boolean verifyNullParentHandling() {
        List<String> parents = getAllParentNames();
         
        boolean hasNullParents = parents.stream().anyMatch(p -> p.equals("-"));
        if (!hasNullParents) {
            return true;  
        }
        
         
        boolean inNullGroup = false;
        boolean nullGroupEnded = false;
        for (String parent : parents) {
            if (parent.equals("-")) {
                if (nullGroupEnded) {
                    return false;  
                }
                inNullGroup = true;
            } else {
                if (inNullGroup) {
                    nullGroupEnded = true;
                }
            }
        }
        return true;  
    }

    public void enterNonExistingKeyword() {
        enterSearchTerm("NonExistentCategoryXYZ999");
    }

    public boolean hasNoCategoryRows() {
        return tableRows.isEmpty() || 
               (tableRows.size() == 1 && 
                tableRows.get(0).getText().toLowerCase().contains("no category"));
    }
}
