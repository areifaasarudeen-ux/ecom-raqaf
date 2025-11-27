package com.raqaf.ecom.service.impl;

import com.raqaf.ecom.model.Product;
import com.raqaf.ecom.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // General pagination and sorting
    public Page<Product> getAllProducts(int page, int size, String sortBy, String direction) {
        Pageable pageable = getPageable(page, size, sortBy, direction);
        return productRepository.findAll(pageable);
    }

    // Search by keyword
    public Page<Product> searchProducts(String keyword, int page, int size, String sortBy, String direction) {
        Pageable pageable = getPageable(page, size, sortBy, direction);
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
    }

    // Filter by category
    public Page<Product> filterByCategory(Long categoryId, int page, int size, String sortBy, String direction) {
        Pageable pageable = getPageable(page, size, sortBy, direction);
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    // Filter by price range
    public org.springframework.data.domain.Page<Product> filterByPrice(Double minPrice, Double maxPrice, int page, int size, String sortBy, String direction) {
        Pageable pageable = getPageable(page, size, sortBy, direction);
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    // Helper method to create Pageable object
    private Pageable getPageable(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(page, size, sort);
    }
}