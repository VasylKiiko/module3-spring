package com.example.dao;

import com.example.model.Ticket;

import java.util.List;

public interface BookingDAO {
    Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category);

    List<Ticket> getBookedTicketsForUser(Long userId, int pageSize, int pageNum);

    List<Ticket> getBookedTicketsForEvent(Long eventId, int pageSize, int pageNum);

    boolean cancelTicket(long ticketId);

    Ticket getTicketById(Long ticketId);
}
