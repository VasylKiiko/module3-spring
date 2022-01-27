package com.example.dao.impl;

import com.example.config.TestDAOBeans;
import com.example.config.TestDatabaseConfiguration;
import com.example.dao.EventDAO;
import com.example.exception.EventAlreadyExistsException;
import com.example.exception.EventNotFoundException;
import com.example.model.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDatabaseConfiguration.class, TestDAOBeans.class})
class EventDAOImplTest {
    @Autowired
    private EventDAO eventDAO;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldFindEvent_ifExists() {
        var eventToSave = Event.builder()
                .title("Test")
                .date(LocalDate.parse("2022-02-11"))
                .ticketPrice(100.0)
                .build();
        var savedEvent = eventDAO.createEvent(eventToSave);

        entityManager.flush();

        var eventFromDB = eventDAO.getEventById(savedEvent.getId());
        System.out.println(eventFromDB);

        assertEquals(savedEvent, eventFromDB);
    }

    @Test
    void shouldThrowException_ifEventWithIdNotExists() {
        assertThrows(EventNotFoundException.class, () -> eventDAO.getEventById(-1L));
    }

    @Test
    void shouldThrowException_ifEventWithNameAndDateExists() {
        var eventFromDB = eventDAO.getEventById(1L);

        var newEvent = Event.builder()
                .ticketPrice(eventFromDB.getTicketPrice())
                .date(eventFromDB.getDate())
                .title(eventFromDB.getTitle())
                .build();

        assertThrows(EventAlreadyExistsException.class, () -> eventDAO.createEvent(newEvent));
    }

    @Test
    void shouldDeleteEvent_ifEventExists() {
        var eventsBefore = eventDAO.getAll();
        var deleted = eventDAO.deleteEvent(1L);
        var eventsAfter = eventDAO.getAll();

        assertTrue(deleted);
        assertNotEquals(eventsBefore.size(), eventsAfter.size());
    }

    @Test
    void shouldNotDeleteEvent_ifEventDoesNotExists() {
        var eventsBefore = eventDAO.getAll();
        var deleted = eventDAO.deleteEvent(-1L);
        var eventsAfter = eventDAO.getAll();

        assertFalse(deleted);
        assertEquals(eventsBefore.size(), eventsAfter.size());
    }

    @Test
    void shouldUpdateEvent_ifExists() {
        var eventToUpdate = Event.builder()
                .id(1L)
                .title("Updated title")
                .ticketPrice(100.0)
                .date(LocalDate.parse("2020-11-11"))
                .build();

        var updatedEvent = eventDAO.updateEvent(eventToUpdate);
        entityManager.flush();

        var eventFromDB = eventDAO.getEventById(1L);

        assertEquals(eventFromDB, updatedEvent);
    }

    @Test
    void shouldNotUpdate_ifEventNotExists() {
        var eventToUpdate = Event.builder()
                .id(-1L)
                .title("Updated title")
                .ticketPrice(100.0)
                .date(LocalDate.parse("2020-11-11"))
                .build();

        assertThrows(EventNotFoundException.class, () -> eventDAO.updateEvent(eventToUpdate));
    }

    @Test
    void shouldReturnEvents_ifTitleExists() {
        var title = "star";
        var events = eventDAO.getEventsByTitle(title, 5, 1);

        assertFalse(events.isEmpty());

        events.forEach(event -> assertTrue(
                event.getTitle().toLowerCase(Locale.ROOT).contains(title.toLowerCase(Locale.ROOT))
        ));
    }

    @Test
    void shouldReturnEmptyList_ifNoEventsWithTitle() {
        var title = "title does not exist";
        var events = eventDAO.getEventsByTitle(title, 5, 1);

        assertTrue(events.isEmpty());
    }

    @Test
    void shouldReturnEvents_ifExistsForDay() {
        var date = LocalDate.parse("2022-01-26");
        var events = eventDAO.getEventsForDay(date, 5, 1);

        assertFalse(events.isEmpty());
        events.forEach(event -> assertEquals(event.getDate(), date));
    }

    @Test
    void shouldReturnEmptyList_ifNotEventWithDate() {
        var date = LocalDate.parse("2030-01-26");
        var events = eventDAO.getEventsForDay(date, 5, 1);

        assertTrue(events.isEmpty());
    }
}