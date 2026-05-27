package com.example.blackspace.Service.Category;



import com.example.blackspace.Model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category saveCategory(Category category);

    void deleteCategory(Long id);
    // Update an existing category
    Category updateCategory(Category category);
}

