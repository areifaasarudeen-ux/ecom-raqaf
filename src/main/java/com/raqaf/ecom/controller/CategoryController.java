package com.raqaf.ecom.controller;

import com.raqaf.ecom.dto.CategoryResponse;
import com.raqaf.ecom.dto.DtoMapper;
import com.raqaf.ecom.model.Category;
import com.raqaf.ecom.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository repo;
    public CategoryController(CategoryRepository repo) { this.repo = repo; }

    @GetMapping
    public List<CategoryResponse> all() {
        return repo.findAll().stream().map(DtoMapper::toCategoryResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> get(@PathVariable Long id) {
        return repo.findById(id).map(c -> ResponseEntity.ok(DtoMapper.toCategoryResponse(c))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public CategoryResponse create(@RequestBody CategoryResponse req) {
        Category c = new Category();
        c.setName(req.getName());
        c.setDescription(req.getDescription());
        return DtoMapper.toCategoryResponse(repo.save(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @RequestBody CategoryResponse req) {
        return repo.findById(id).map(existing -> {
            existing.setName(req.getName());
            existing.setDescription(req.getDescription());
            return ResponseEntity.ok(DtoMapper.toCategoryResponse(repo.save(existing)));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}