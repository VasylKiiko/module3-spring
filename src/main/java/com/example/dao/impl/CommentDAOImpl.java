package com.example.dao.impl;

import com.example.dao.CommentDAO;
import com.example.model.Comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Repository
public class CommentDAOImpl implements CommentDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Comment addComment(Comment comment) {
        entityManager.persist(comment);
        log.info("Comment added {}", comment);
        return comment;
    }

    @Override
    public double getAverageRatingForEvent(Long eventId) {
        return entityManager.createNamedQuery("get_average_rating_for_event", Double.class)
                .setParameter("event_id", eventId)
                .getSingleResult();
    }

    @Override
    public List<Comment> getCommentsForEvent(Long eventId, int pageSize, int pageNum) {
        var comments = entityManager.createNamedQuery("get_comments_for_event", Comment.class)
                .setParameter("event_id", eventId)
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        log.info("Comments found {}", comments);
        return comments;
    }

    @Override
    public List<Comment> getCommentsForUser(Long userId, int pageSize, int pageNum) {
        var comments = entityManager.createNamedQuery("get_comments_for_user", Comment.class)
                .setParameter("user_id", userId)
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        log.info("Comments found {}", comments);
        return comments;
    }
}
