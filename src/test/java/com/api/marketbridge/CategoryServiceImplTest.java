package com.api.marketbridge;


import com.api.marketbridge.category.dto.CategoryRequest;
import com.api.marketbridge.category.dto.CategoryResponse;
import com.api.marketbridge.category.entity.Category;
import com.api.marketbridge.category.mapper.CategoryMapper;
import com.api.marketbridge.category.repository.CategoryRepository;
import com.api.marketbridge.category.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
