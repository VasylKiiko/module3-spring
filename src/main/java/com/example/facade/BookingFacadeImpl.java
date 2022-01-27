package com.example.facade;

import com.example.model.*;
import com.example.service.BookingService;
import com.example.service.CommentService;
import com.example.service.EventService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingFacadeImpl implements BookingFacade{
    final private UserService userService;
    final private BookingService bookingService;
    final private EventService eventService;
    final private CommentService commentService;

    @Override
    public Event getEventById(long eventId) {
        return eventService.getEventById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        return eventService.createEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventService.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventService.deleteEvent(eventId);
    }

    @Override
    public User getUserById(long userId) {
        return userService.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        return userService.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userService.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userService.deleteUser(userId);
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        return bookingService.bookTicket(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTicketsForUser(Long userId, int pageSize, int pageNum) {
        return bookingService.getBookedTicketsForUser(userId, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTicketsForEvent(Long eventId, int pageSize, int pageNum) {
        return bookingService.getBookedTicketsForEvent(eventId, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return bookingService.cancelTicket(ticketId);
    }

    @Override
    public UserAccount registerNewAccountForUser(Long userId, UserAccount userAccount){
        return userService.registerNewAccountForUser(userId, userAccount);
    }

    @Override
    public boolean payForTicket(Long ticketId, Long accountId, String password) {
        return bookingService.payForTicket(ticketId, accountId, password);
    }

    @Override
    public Comment addComment(Long userId, Long eventId, Comment comment) {
        return commentService.addComment(userId, eventId, comment);
    }

    @Override
    public List<Comment> getCommentsForEvent(Long eventId, int pageSize, int pageNum) {
        return commentService.getCommentsForEvent(eventId, pageSize, pageNum);
    }

    @Override
    public List<Comment> getCommentsForUser(Long userId, int pageSize, int pageNum) {
        return commentService.getCommentsForUser(userId, pageSize, pageNum);
    }
}
