package com.api.marketbridge;

import com.api.marketbridge.comment.mapper.CommentMapper;
import com.api.marketbridge.comment.repository.CommentRepository;
import com.api.marketbridge.comment.service.impl.CommentServiceImpl;
import com.api.marketbridge.product.repository.ProductRepository;
import com.api.marketbridge.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
