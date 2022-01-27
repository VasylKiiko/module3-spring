package com.example.dao.impl;

import com.example.dao.BookingDAO;
import com.example.dao.EventDAO;
import com.example.dao.UserDAO;
import com.example.exception.TicketIsAlreadyBookedException;
import com.example.exception.TicketNotFoundException;
import com.example.model.Ticket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookingDAOImpl implements BookingDAO {
    @PersistenceContext
    private EntityManager entityManager;

    private final UserDAO userDAO;
    private final EventDAO eventDAO;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        if (ticketAlreadyBooked(eventId, place)) {
            throw new TicketIsAlreadyBookedException(String.format("Place %s for event %s is booked already", place, eventId));
        }
        var userFromDB = userDAO.getUserById(userId);
        var eventFromDB = eventDAO.getEventById(eventId);

        var ticket = Ticket.builder()
                .category(category)
                .place(place)
                .event(eventFromDB)
                .user(userFromDB)
                .build();

        entityManager.persist(ticket);
        log.info("Ticket {} is added", ticket);
        return ticket;
    }

    @Override
    public List<Ticket> getBookedTicketsForUser(Long userId, int pageSize, int pageNum) {
        var tickets = entityManager.createNamedQuery("Ticket.get_by_user_id", Ticket.class)
                .setParameter("user_id", userId)
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        log.info("For user {} found tickets {}", userId, tickets);
        return tickets;
    }

    @Override
    public List<Ticket> getBookedTicketsForEvent(Long eventId, int pageSize, int pageNum) {
        var tickets = entityManager.createNamedQuery("Ticket.get_by_event_id", Ticket.class)
                .setParameter("event_id", eventId)
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
        log.info("For event {} found tickets {}", eventId, tickets);
        return tickets;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean cancelTicket(long ticketId) {
        var ticket = entityManager.find(Ticket.class, ticketId);
        if (ticket == null) {
            return false;
        }
        entityManager.remove(ticket);
        return true;
    }

    @Override
    public Ticket getTicketById(Long ticketId) {
        var ticket = entityManager.find(Ticket.class, ticketId);
        if (ticket == null) {
            throw new TicketNotFoundException();
        }
        return ticket;
    }

    public boolean ticketAlreadyBooked(Long eventId, int place) {
        try {
            entityManager.createNamedQuery("Ticket.is_already_booked", Ticket.class)
                    .setParameter("event_id", eventId)
                    .setParameter("place", place)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return false;
        }
        return true;
    }
}
