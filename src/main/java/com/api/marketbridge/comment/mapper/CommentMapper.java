package com.api.marketbridge.comment.mapper;

import com.api.marketbridge.comment.dto.CommentRequest;
import com.api.marketbridge.comment.dto.CommentResponse;
import com.api.marketbridge.comment.entity.Comment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    Comment toEntity(CommentRequest commentRequest);

    CommentRequest toDto(Comment comment);

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "authorName", source = "author.fullName")
    @Mapping(target = "timestamp", source = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    CommentResponse toResponse(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment partialUpdate(CommentRequest commentRequest, @MappingTarget Comment comment);

    }