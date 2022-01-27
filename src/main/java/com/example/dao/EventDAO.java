package com.example.dao;

import com.example.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventDAO {
    Event getEventById(long eventId);

    List<Event> getEventsByTitle(String title, int pageSize, int pageNum);

    List<Event> getEventsForDay(LocalDate date, int pageSize, int pageNum);

    Event createEvent(Event event);

    Event updateEvent(Event event);

    boolean deleteEvent(long eventId);

    boolean eventExists(Event event);

    List<Event> getAll();
}
