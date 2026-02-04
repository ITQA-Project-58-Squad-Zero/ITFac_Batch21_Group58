package api.models.common;

import java.util.List;

public class Category {
    private int id;
    private String name;
    private List<Object> subCategories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Object> subCategories) {
        this.subCategories = subCategories;
    }
}
