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

    @PostMapping(produces = "application/json")
    public ResponseEntity<CategoryResponse> saveCategory(@RequestBody CategoryRequest request){
        CategoryResponse response=  categoryService.createCategory(request);
        return ResponseEntity
                .status(201)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("id") Long id){
        CategoryResponse response=  categoryService.getCategoryById(id);
        return ResponseEntity
                .status(200)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable("id") Long id,
            @RequestBody @Valid CategoryRequest request){
        CategoryResponse response=  categoryService.updateCategory(id, request);
        return ResponseEntity
                .status(200)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
