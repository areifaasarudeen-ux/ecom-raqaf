package com.raqaf.ecom.service;

import com.raqaf.ecom.model.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> getAll();
    public Category getById(Long id);
    public Category create(Category category);
    public Category update(Long id, Category category);
    public void delete(Long id);
}
