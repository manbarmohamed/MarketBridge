package com.api.marketbridge;

import com.api.marketbridge.comment.dto.CommentRequest;
import com.api.marketbridge.comment.dto.CommentResponse;
import com.api.marketbridge.comment.entity.Comment;
import com.api.marketbridge.comment.mapper.CommentMapper;
import com.api.marketbridge.comment.repository.CommentRepository;
import com.api.marketbridge.comment.service.impl.CommentServiceImpl;
import com.api.marketbridge.product.entity.Product;
import com.api.marketbridge.product.repository.ProductRepository;
import com.api.marketbridge.user.entity.User;
import com.api.marketbridge.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CommentServiceImpl commentService;

    private CommentRequest request;
    private Comment comment;
    private Comment savedComment;
    private Product product;
    private User user;
    private CommentResponse response;

    @BeforeEach
    void setUp() {
        request = new CommentRequest();
        request.setProductId(1L);
        request.setContent("Nice product!");

        product = new Product(); // Minimal setup; fill as needed
        user = mock(User.class); // Abstract class, mock it

        comment = new Comment();
        savedComment = new Comment();
        savedComment.setId(100L);

        response = new CommentResponse();
        response.setId(100L);
    }

    @Test
    void testAddComment_Success() {
        // Set up SecurityContextHolder mock
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("john.doe");
        SecurityContextHolder.setContext(securityContext);

        // Mock interactions
        when(commentMapper.toEntity(request)).thenReturn(comment);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(user));
        when(commentRepository.save(comment)).thenReturn(savedComment);
        when(commentMapper.toResponse(savedComment)).thenReturn(response);

        // Call service
        CommentResponse result = commentService.addComment(request);

        // Verify
        assertNotNull(result);
        assertEquals(100L, result.getId());

        verify(commentMapper).toEntity(request);
        verify(productRepository).findById(1L);
        verify(userRepository).findByUsername("john.doe");
        verify(commentRepository).save(comment);
        verify(commentMapper).toResponse(savedComment);
    }
}
