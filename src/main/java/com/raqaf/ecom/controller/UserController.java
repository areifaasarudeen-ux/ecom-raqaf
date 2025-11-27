package com.raqaf.ecom.controller;

import com.raqaf.ecom.dto.DtoMapper;
import com.raqaf.ecom.dto.UserResponse;
import com.raqaf.ecom.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repo;
    public UserController(UserRepository repo) { this.repo = repo; }

    @GetMapping
    public List<UserResponse> all() {
        return repo.findAll().stream().map(DtoMapper::toUserResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        return repo.findById(id).map(u -> ResponseEntity.ok(DtoMapper.toUserResponse(u))).orElseGet(() -> ResponseEntity.notFound().build());
    }

}