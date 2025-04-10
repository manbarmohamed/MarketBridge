package com.api.marketbridge.category.controller;


import com.api.marketbridge.category.dto.CategoryRequest;
import com.api.marketbridge.category.dto.CategoryResponse;
import com.api.marketbridge.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> saveCategory(@RequestBody @Valid CategoryRequest request){
        CategoryResponse response=  categoryService.createCategory(request);
        return ResponseEntity
                .status(201)
                .body(response);
    }
}
