package com.raqaf.ecom.repository;

import com.raqaf.ecom.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

    // Search by name or description
    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    // Filter by category
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // Filter by price range
    Page<Product> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
}
