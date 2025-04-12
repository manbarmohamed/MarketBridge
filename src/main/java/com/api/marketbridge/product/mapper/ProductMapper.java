package com.api.marketbridge.product.mapper;

import com.api.marketbridge.category.entity.Category;
import com.api.marketbridge.category.mapper.CategoryMapper;
import com.api.marketbridge.product.dto.ProductRequest;
import com.api.marketbridge.product.dto.ProductResponse;
import com.api.marketbridge.product.entity.Product;
import com.api.marketbridge.user.entity.Seller;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryIdToCategory")
    @Mapping(target = "owner", source = "sellerId", qualifiedByName = "sellerIdToSeller")
    Product toEntity(ProductRequest request);

    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "formatLocalDateTime")
    @Mapping(target = "category", source = "category")
    ProductResponse toResponse(Product product);

    // List mappings
    List<Product> toEntityList(List<ProductRequest> requestList);

    List<ProductResponse> toResponseList(List<Product> productList);

    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);

    @Named("categoryIdToCategory")
    default Category categoryIdToCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    @Named("sellerIdToSeller")
    default Seller sellerIdToSeller(Long sellerId) {
        if (sellerId == null) {
            return null;
        }
        Seller seller = new Seller();
        seller.setId(sellerId);
        return seller;
    }

    @Named("formatLocalDateTime")
    default String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @AfterMapping
    default void setAdditionalFields(@MappingTarget Product product, ProductRequest request) {
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(LocalDateTime.now());
        }
    }
}