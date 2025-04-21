package com.api.marketbridge;


import com.api.marketbridge.category.dto.CategoryRequest;
import com.api.marketbridge.category.dto.CategoryResponse;
import com.api.marketbridge.category.entity.Category;
import com.api.marketbridge.category.mapper.CategoryMapper;
import com.api.marketbridge.category.repository.CategoryRepository;
import com.api.marketbridge.category.service.impl.CategoryServiceImpl;
import com.api.marketbridge.commun.ResourceAlreadyExistException;
import com.api.marketbridge.commun.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Electronics");

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setName("Electronics");
    }
    @Test
    void createCategory_Success(){
        when(categoryMapper.toEntity(categoryRequest)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.createCategory(categoryRequest);

        assertNotNull(result);
        assertEquals(categoryResponse.getId(), result.getId());
        assertEquals(categoryResponse.getName(), result.getName());

        verify(categoryMapper).toEntity(categoryRequest);
        verify(categoryRepository).existsByName("Electronics");
        verify(categoryRepository).save(category);
        verify(categoryMapper).toResponse(category);

    }

    @Test
    void createCategory_ThrowsExceptionWhenNameAlreadyExists(){
        when(categoryMapper.toEntity(categoryRequest)).thenReturn(category);
        when(categoryRepository.existsByName("Electronics")).thenReturn(true);

        assertThrows(ResourceAlreadyExistException.class, () -> {
            categoryService.createCategory(categoryRequest);
        });

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getCategoryById_success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getCategoryById_notFound_throwsException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryById(1L);
        });
    }

    @Test
    void updateCategory_success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateEntityFromRequest(categoryRequest, category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.updateCategory(1L, categoryRequest);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository).save(category);
    }


}
