package com.api.marketbridge.comment.mapper;

import com.api.marketbridge.comment.dto.CommentRequest;
import com.api.marketbridge.comment.dto.CommentResponse;
import com.api.marketbridge.comment.entity.Comment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    Comment toEntity(CommentRequest commentRequest);

    CommentRequest toDto(Comment comment);

    CommentResponse toResponse(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment partialUpdate(CommentRequest commentRequest, @MappingTarget Comment comment);

    }