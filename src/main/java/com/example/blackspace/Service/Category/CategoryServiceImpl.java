package com.example.blackspace.Service.Category;




import com.example.blackspace.Model.Category;
import com.example.blackspace.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category saveCategory(Category category) {
        if (categoryRepository.findByName(category.getName()) != null) {
            throw new RuntimeException("Category with name '" + category.getName() + "' already exists.");
        }
        return categoryRepository.save(category);
    }

    // CategoryServiceImpl.java
    @Override
    public Category updateCategory(Category category) {
        Category existing = categoryRepository.findByName(category.getName());
        if (existing != null && !existing.getId().equals(category.getId())) {
            throw new RuntimeException("Category with name '" + category.getName() + "' already exists.");
        }
        return categoryRepository.save(category);
    }



    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
