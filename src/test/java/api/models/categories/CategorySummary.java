package api.models.categories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategorySummary {
    private int mainCategories;
    private int subCategories;

    public int getMainCategories() {
        return mainCategories;
    }

    public void setMainCategories(int mainCategories) {
        this.mainCategories = mainCategories;
    }

    public int getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(int subCategories) {
        this.subCategories = subCategories;
    }
}
