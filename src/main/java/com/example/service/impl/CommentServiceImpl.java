package com.example.service.impl;

import com.example.dao.CommentDAO;
import com.example.dao.EventDAO;
import com.example.dao.UserDAO;
import com.example.model.Comment;
import com.example.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentDAO commentDAO;
    private final UserDAO userDAO;
    private final EventDAO eventDAO;

    @Override
    public Comment addComment(Long userId, Long eventId, Comment comment) {
        var user = userDAO.getUserById(userId);
        var event = eventDAO.getEventById(eventId);

        comment.setEvent(event);
        comment.setUser(user);
        comment.setCreationTime(LocalDateTime.now());
        var addedComment = commentDAO.addComment(comment);

        event.setRating(commentDAO.getAverageRatingForEvent(eventId));
        log.info("Comment {} added, event rating updated {}", comment, event);

        return addedComment;
    }

    @Override
    public List<Comment> getCommentsForEvent(Long eventId, int pageSize, int pageNum) {
        return commentDAO.getCommentsForEvent(eventId, pageSize, pageNum);
    }

    @Override
    public List<Comment> getCommentsForUser(Long userId, int pageSize, int pageNum) {
        return commentDAO.getCommentsForUser(userId, pageSize, pageNum);
    }
}
