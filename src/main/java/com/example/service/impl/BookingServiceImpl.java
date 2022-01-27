package com.example.service.impl;

import com.example.dao.BookingDAO;
import com.example.dao.UserAccountDAO;
import com.example.model.Ticket;
import com.example.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingDAO bookingDAO;
    private final UserAccountDAO userAccountDAO;

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        return bookingDAO.bookTicket(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTicketsForUser(Long userId, int pageSize, int pageNum) {
        return bookingDAO.getBookedTicketsForUser(userId, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTicketsForEvent(Long eventId, int pageSize, int pageNum) {
        return bookingDAO.getBookedTicketsForEvent(eventId, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return bookingDAO.cancelTicket(ticketId);
    }

    @Override
    public boolean payForTicket(Long ticketId, Long accountId, String password) {
        var ticket = bookingDAO.getTicketById(ticketId);
        if (ticket.isPaidUp()) {
            log.info("Ticket {} is already paid", ticketId);
            return false;
        }

        if (userAccountDAO.withdrawMoneyFromAccount(accountId, password, ticket.getEvent().getTicketPrice())) {
            ticket.setPaidUp(true);
            return true;
        }
        return false;
    }
}
