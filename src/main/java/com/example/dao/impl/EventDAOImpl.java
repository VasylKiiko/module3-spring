package com.example.dao.impl;

import com.example.dao.EventDAO;
import com.example.exception.EventAlreadyExistsException;
import com.example.exception.EventNotFoundException;
import com.example.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
public class EventDAOImpl implements EventDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Event getEventById(long eventId) {
        var event = entityManager.find(Event.class, eventId);
        if (event == null) {
            throw new EventNotFoundException();
        }
        log.info("Event {} found", event);
        return event;
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        var eventsList = entityManager.createNamedQuery("Event.get_event_by_title", Event.class)
                .setParameter("title", title)
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
        log.info("Events with {} title found: {}", title, eventsList);
        return eventsList;
    }

    @Override
    public List<Event> getEventsForDay(LocalDate date, int pageSize, int pageNum) {
        var eventsList = entityManager
                .createNamedQuery("Event.get_events_for_day", Event.class)
                .setParameter("date", date)
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
        log.info("Events for date {} found: {}", date, eventsList);
        return eventsList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Event createEvent(Event event) {
        if (eventExists(event)) {
            throw new EventAlreadyExistsException(
                    String.format("Event with title %s for day %s already scheduled!", event.getTitle(), event.getDate())
            );
        }
        entityManager.persist(event);
        return event;
    }

    @Override
    public Event updateEvent(Event event) {
        var eventFromDB = entityManager.find(Event.class, event.getId());
        if (eventFromDB == null) {
            log.info("No such event in database {}", event);
            throw new EventNotFoundException();
        }
        eventFromDB.setTitle(event.getTitle());
        eventFromDB.setDate(event.getDate());
        eventFromDB.setTicketPrice(event.getTicketPrice());
        return eventFromDB;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteEvent(long eventId) {
        var eventFromDB = entityManager.find(Event.class, eventId);
        if (eventFromDB == null) {
            log.info("No such event in database with id {}", eventId);
            return false;
        }

        entityManager.remove(eventFromDB);
        return true;
    }

    @Override
    public boolean eventExists(Event event) {
        try {
            entityManager.createQuery("select e from Event e where e.title=:title and e.date=:date", Event.class)
                    .setParameter("title", event.getTitle())
                    .setParameter("date", event.getDate())
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Event> getAll() {
        return entityManager.createQuery("select ev from Event ev", Event.class).getResultList();
    }
}
