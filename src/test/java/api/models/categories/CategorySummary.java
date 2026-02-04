package api.models.categories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategorySummary {
    private int mainCategories;
    private int subCategories;

    public int getMainCategories() {
        return mainCategories;
    }

    @JsonProperty(value = "mainCategories", access = JsonProperty.Access.WRITE_ONLY)
    public void setMainCategories(int mainCategories) {
        this.mainCategories = mainCategories;
    }

    @JsonProperty(value = "main_categories", access = JsonProperty.Access.WRITE_ONLY)
    public void setMainCategoriesSnake(int mainCategories) {
        this.mainCategories = mainCategories;
    }

    public int getSubCategories() {
        return subCategories;
    }

    @JsonProperty(value = "subCategories", access = JsonProperty.Access.WRITE_ONLY)
    public void setSubCategories(int subCategories) {
        this.subCategories = subCategories;
    }

    @JsonProperty(value = "sub_categories", access = JsonProperty.Access.WRITE_ONLY)
    public void setSubCategoriesSnake(int subCategories) {
        this.subCategories = subCategories;
    }
}
