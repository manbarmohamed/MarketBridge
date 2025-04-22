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
import java.util.List;
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

    @Test
    void testDeleteComment_Success() {
        Long commentId = 2L;

        Comment comment = new Comment();
        comment.setId(commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(comment);

        // Call
        commentService.deleteComment(commentId);

        // Verify
        verify(commentRepository).findById(commentId);
        verify(commentRepository).delete(comment);
    }

    @Test
    void testUpdateComment_Success() {
        Long commentId = 1L;

        Comment existingComment = new Comment();
        existingComment.setId(commentId);

        CommentRequest updateRequest = new CommentRequest();
        updateRequest.setContent("Updated content");

        Comment updatedComment = new Comment();
        updatedComment.setId(commentId);
        updatedComment.setContent("Updated content");

        CommentResponse expectedResponse = new CommentResponse();
        expectedResponse.setId(commentId);
        expectedResponse.setContent("Updated content");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        // Fix: Assume partialUpdate is void
        doAnswer(invocation -> {
            CommentRequest requestArg = invocation.getArgument(0);
            Comment commentArg = invocation.getArgument(1);
            commentArg.setContent(requestArg.getContent());
            return null;
        }).when(commentMapper).partialUpdate(any(CommentRequest.class), any(Comment.class));

        when(commentRepository.save(existingComment)).thenReturn(updatedComment);
        when(commentMapper.toResponse(updatedComment)).thenReturn(expectedResponse);

        // Call service
        CommentResponse result = commentService.updateComment(commentId, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Updated content", result.getContent());

        // Verify
        verify(commentRepository).findById(commentId);
        verify(commentMapper).partialUpdate(updateRequest, existingComment);
        verify(commentRepository).save(existingComment);
        verify(commentMapper).toResponse(updatedComment);
    }


    @Test
    void testGetCommentsByProduct_Success() {
        Long productId = 10L;

        Comment comment1 = new Comment();
        comment1.setId(1L);
        Comment comment2 = new Comment();
        comment2.setId(2L);

        CommentResponse response1 = new CommentResponse();
        response1.setId(1L);
        CommentResponse response2 = new CommentResponse();
        response2.setId(2L);

        List<Comment> commentList = List.of(comment1, comment2);
        List<CommentResponse> responseList = List.of(response1, response2);

        when(commentRepository.findByProductId(productId)).thenReturn(commentList);
        when(commentMapper.toResponse(comment1)).thenReturn(response1);
        when(commentMapper.toResponse(comment2)).thenReturn(response2);

        // Call
        List<CommentResponse> result = commentService.getCommentsByProduct(productId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        // Verify
        verify(commentRepository).findByProductId(productId);
        verify(commentMapper).toResponse(comment1);
        verify(commentMapper).toResponse(comment2);
    }

}
