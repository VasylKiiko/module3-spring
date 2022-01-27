package com.example.service;

import com.example.model.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(Long userId, Long eventId, Comment comment);

    List<Comment> getCommentsForEvent(Long eventId, int pageSize, int pageNum);

    List<Comment> getCommentsForUser(Long userId, int pageSize, int pageNum);
}
