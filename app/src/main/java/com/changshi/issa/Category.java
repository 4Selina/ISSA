package com.changshi.issa;

import java.util.List;

public class Category {
    private String parentCategory;
    private List<String> subCategories;
    public Category(String parentCategory, List<String> subCategories) {
        this.parentCategory = parentCategory;
        this.subCategories = subCategories;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public List<String> getSubCategories() {
        return subCategories;
    }
}