package com.raqaf.ecom.service.impl;

import com.raqaf.ecom.model.Category;
import com.raqaf.ecom.repository.CategoryRepository;
import com.raqaf.ecom.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category category) {
        Category exist = getById(id);
        exist.setName(category.getName());
        exist.setDescription(category.getDescription());
        return categoryRepository.save(exist);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
