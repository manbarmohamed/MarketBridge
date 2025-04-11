package com.api.marketbridge.category.mapper;

import com.api.marketbridge.category.dto.CategoryRequest;
import com.api.marketbridge.category.dto.CategoryResponse;
import com.api.marketbridge.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    

    // Entity to DTO mapping
    CategoryResponse toResponse(Category category);
    
    // Convert a list of entities to a list of DTOs
    List<CategoryResponse> toResponseList(List<Category> categories);
    
    // Request DTO to Entity mapping (for creating new categories)
    Category toEntity(CategoryRequest categoryRequest);
    
    // Update an existing entity from a request DTO
    void updateEntityFromRequest(CategoryRequest categoryRequest, @MappingTarget Category category);
}