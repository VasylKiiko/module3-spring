package com.example.dao;

import com.example.model.Comment;

import java.util.List;

public interface CommentDAO {
    Comment addComment(Comment comment);

    double getAverageRatingForEvent(Long eventId);

    List<Comment> getCommentsForEvent(Long eventId, int pageSize, int pageNum);

    List<Comment> getCommentsForUser(Long userId, int pageSize, int pageNum);
}
