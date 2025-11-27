package com.raqaf.ecom.controller;


import com.raqaf.ecom.dto.DtoMapper;
import com.raqaf.ecom.dto.ProductRequest;
import com.raqaf.ecom.dto.ProductResponse;
import com.raqaf.ecom.model.Product;
import com.raqaf.ecom.repository.CategoryRepository;
import com.raqaf.ecom.repository.ProductRepository;
import com.raqaf.ecom.service.impl.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import  org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final ProductService productService;

    public ProductController(ProductRepository productRepo, CategoryRepository categoryRepo, ProductService productService) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> all() {
        return productRepo.findAll().stream().map(DtoMapper::toProductResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long id) {
        return productRepo.findById(id).map(p -> ResponseEntity.ok(DtoMapper.toProductResponse(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProductRequest req) {
        var cat = categoryRepo.findById(req.getCategoryId()).orElse(null);
        if (cat == null) return ResponseEntity.badRequest().body("Category not found");

        Product p = new Product();
        DtoMapper.updateProductFromRequest(p, req);
        p.setCategory(cat);
        Product saved = productRepo.save(p);
        return ResponseEntity.ok(DtoMapper.toProductResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductRequest req) {
        return productRepo.findById(id).map(existing -> {
            var cat = categoryRepo.findById(req.getCategoryId()).orElse(null);
            if (cat == null) return ResponseEntity.badRequest().body("Category not found");
            DtoMapper.updateProductFromRequest(existing, req);
            existing.setCategory(cat);
            return ResponseEntity.ok(DtoMapper.toProductResponse(productRepo.save(existing)));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/products")
    public Page<Map<String, Object>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        Page<Product> products;

        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProducts(keyword, page, size, sortBy, direction);
        } else if (categoryId != null) {
            products = productService.filterByCategory(categoryId, page, size, sortBy, direction);
        } else if (minPrice != null && maxPrice != null) {
            products = productService.filterByPrice(minPrice, maxPrice, page, size, sortBy, direction);
        } else {
            products = productService.getAllProducts(page, size, sortBy, direction);
        }

        return products.map(p -> Map.of(
                "id", p.getId(),
                "name", p.getName(),
                "description", p.getDescription(),
                "price", p.getPrice(),
                "stock", p.getStock(),
                "category", p.getCategory() != null ? p.getCategory().getName() : null,
                "imageUrl", p.getImageUrl(),
                "createdAt", p.getCreatedAt()
        ));
    }

}