package com.example.service.impl;

import com.example.dao.EventDAO;
import com.example.model.Event;
import com.example.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventDAO eventDAO;

    @Override
    public Event getEventById(long eventId) {
        return eventDAO.getEventById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventDAO.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
        return eventDAO.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        return eventDAO.createEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventDAO.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventDAO.deleteEvent(eventId);
    }
}
