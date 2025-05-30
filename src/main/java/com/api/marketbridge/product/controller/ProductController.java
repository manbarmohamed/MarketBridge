package com.api.marketbridge.product.controller;


import com.api.marketbridge.image.service.ImageService;
import com.api.marketbridge.product.dto.ProductRequest;
import com.api.marketbridge.product.dto.ProductResponse;
import com.api.marketbridge.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService;

    @PostMapping(produces = "application/json")
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "ASC") String sortDir) {

        Page<ProductResponse> productPage = productService.getAllProducts(page, size, sortBy, sortDir);
        return ResponseEntity.ok(productPage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("id") Long id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "ASC") String sortDir) {

        Page<ProductResponse> productPage = productService.getProductsByCategory(categoryId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Page<ProductResponse>> getProductsBySeller(
            @PathVariable("sellerId") Long sellerId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "ASC") String sortDir) {

        Page<ProductResponse> productPage = productService.getProductsBySeller(sellerId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(productPage);

    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam("keyword") String keyword
    ) {
        List<ProductResponse> results = productService.searchProducts(keyword);
        return ResponseEntity.ok(results);
    }

    @PutMapping(value = "/{id}/upload-image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponse uploadProductImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        validateFile(file);

        try {
            String imageUrl = imageService.uploadImage(file)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Image upload failed"
                    ));
            return productService.uploadImage(id, imageUrl);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to process image: " + e.getMessage(),
                    e
            );
        }
    }

    @PutMapping(value = "/{id}/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponse uploadProductImages(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files) {

        for (MultipartFile file : files) {
            validateFile(file);
        }

        try {
            List<String> imageUrls = files.stream()
                    .map(file -> imageService.uploadImage(file)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.INTERNAL_SERVER_ERROR, "Image upload failed")))
                    .toList();

            return productService.uploadMultipleImages(id, imageUrls);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload images: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/mark-sold")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductResponse> markProductAsSold(@PathVariable Long id) {
        return ResponseEntity.ok(productService.markProductAsSold(id));
    }
    @PutMapping("/{id}/mark-available")
    public ResponseEntity<ProductResponse> markProductAsAvailable(@PathVariable Long id) {
        return ResponseEntity.ok(productService.markProductAsAvailable(id));
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductResponse>> getProductsByStatus(@PathVariable String status) {
        List<ProductResponse> products = productService.getProductsByStatus(status);
        return ResponseEntity.ok(products);
    }

//    @GetMapping("/Elscsearch")
//    public ResponseEntity<List<ProductResponse>> ElasticSearchProducts(
//            @RequestParam("keyword") String keyword
//    ) {
//        List<ProductResponse> results = productService.searchProducts(keyword);
//        return ResponseEntity.ok(results);
//    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "File cannot be empty");
        }

        String contentType = file.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unsupported file type. Only JPEG/PNG allowed");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "File size exceeds maximum limit of 5MB");
        }
    }
}
