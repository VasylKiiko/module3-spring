package com.example;

import com.example.config.TestApplicationContextConfig;
import com.example.config.TestDatabaseConfiguration;
import com.example.facade.BookingFacade;
import com.example.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestApplicationContextConfig.class, TestDatabaseConfiguration.class})
public class IntegrationTests {

    @Autowired
    private BookingFacade bookingFacade;

    @Test
    void scenario1_createUserAndEventAndAccount_bookTicketAndPayForIt() {
        var userToStore = User.builder().name("FirstName").email("scenario1@email").build();
        var eventToStore = Event.builder()
                .title("Test title")
                .date(LocalDate.parse("2022-12-12"))
                .ticketPrice(100.0)
                .build();

        var createdUser = bookingFacade.createUser(userToStore);
        var createdEvent = bookingFacade.createEvent(eventToStore);

        assertNotNull(createdUser.getId());
        assertNotNull(createdEvent.getId());

        var bookedTicket = bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 10, Ticket.Category.BAR);

        assertEquals(Ticket.Category.BAR, bookedTicket.getCategory());
        assertFalse(bookedTicket.isPaidUp());

        var userAccountToRegister = UserAccount.builder()
                .id(123456L)
                .amount(200.0)
                .password("qwerty")
                .build();

        var userAccount = bookingFacade.registerNewAccountForUser(userToStore.getId(), userAccountToRegister);

        assertEquals(userToStore, userAccount.getUser());
        assertTrue(userAccount.getAmount() > eventToStore.getTicketPrice());

        assertTrue(bookingFacade.payForTicket(bookedTicket.getId(), userAccount.getId(), userAccount.getPassword()));

        var ticketAfterPayment = bookingFacade.getBookedTicketsForUser(createdUser.getId(), 1, 1).get(0);
        assertTrue(ticketAfterPayment.isPaidUp());

        var accountAfterPayment = bookingFacade.getUserById(createdUser.getId()).getAccounts().get(0);
        assertNotEquals(200.0, accountAfterPayment.getAmount());

        assertTrue(bookingFacade.cancelTicket(bookedTicket.getId()));
        assertTrue(bookingFacade.deleteEvent(createdEvent.getId()));
        assertTrue(bookingFacade.deleteUser(createdUser.getId()));
    }

    @Test
    void scenario2_createAndDeleteFewEvents_selectByDateAndTitle() {
        var testDate = LocalDate.parse("2019-09-09");
        var testTitle = "test_scenario";
        var eventsByDateBefore = bookingFacade.getEventsForDay(testDate, 1, 1);
        var eventsByTitleBefore = bookingFacade.getEventsByTitle("test_sce", 1, 1);

        assertTrue(eventsByDateBefore.isEmpty());
        assertTrue(eventsByTitleBefore.isEmpty());

        var event1 = Event.builder()
                .ticketPrice(100.0)
                .date(testDate)
                .title(testTitle + 1)
                .build();
        var event2 = Event.builder()
                .ticketPrice(100.0)
                .date(testDate)
                .title(testTitle + 2)
                .build();

        var createdEvent1 = bookingFacade.createEvent(event1);
        var createdEvent2 = bookingFacade.createEvent(event2);

        var eventsByDateAfter = bookingFacade.getEventsForDay(testDate, 2, 1);
        var eventsByTitleAfter = bookingFacade.getEventsByTitle("test_sce", 2, 1);

        assertEquals(2, eventsByDateAfter.size());
        assertEquals(2, eventsByTitleAfter.size());

        assertTrue(bookingFacade.deleteEvent(createdEvent1.getId()));
        assertTrue(bookingFacade.deleteEvent(createdEvent2.getId()));

        var eventsByDateAfterDelete = bookingFacade.getEventsForDay(testDate, 1, 1);
        var eventsByTitleAfterDelete = bookingFacade.getEventsByTitle("test_sce", 1, 1);

        assertTrue(eventsByDateAfterDelete.isEmpty());
        assertTrue(eventsByTitleAfterDelete.isEmpty());
    }

    @Test
    void scenario3_createUser_assignTickets_updateUser() {
        var userToStore = User.builder().name("FirstName").email("scenario3@email").build();
        var eventToStore = Event.builder()
                .title("Test title")
                .date(LocalDate.parse("2022-12-12"))
                .ticketPrice(100.0)
                .build();

        var createdUser = bookingFacade.createUser(userToStore);
        var createdEvent = bookingFacade.createEvent(eventToStore);

        var userByEmail = bookingFacade.getUserByEmail("scenario3@email");
        assertEquals("scenario3@email", userByEmail.getEmail());

        bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 1, Ticket.Category.BAR);
        bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 2, Ticket.Category.STANDARD);
        bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 3, Ticket.Category.PREMIUM);

        var usersTickets = bookingFacade.getBookedTicketsForUser(createdUser.getId(), 10, 1);
        assertEquals(3, usersTickets.size());

        createdUser.setName("FirstName updated");
        var updatedUser = bookingFacade.updateUser(createdUser);

        assertEquals("FirstName updated", updatedUser.getName());

        var updatedUsersTickets = bookingFacade.getBookedTicketsForUser(updatedUser.getId(), 10, 1);
        assertEquals(3, updatedUsersTickets.size());

        bookingFacade.deleteEvent(createdEvent.getId());
        bookingFacade.deleteUser(createdUser.getId());
    }

    @Test
    void scenario4_testCommentsFlow() {
        var userToStore = User.builder().name("scenario4").email("scenario4@email").build();
        var eventToStore = Event.builder()
                .title("Test title scenario4")
                .date(LocalDate.parse("2022-12-12"))
                .ticketPrice(100.0)
                .build();

        var createdUser = bookingFacade.createUser(userToStore);
        var createdEvent = bookingFacade.createEvent(eventToStore);

        assertEquals(0.0, createdEvent.getRating());

        var comment1 = Comment.builder()
                .comment("Good")
                .rating(2)
                .build();

        var storedComment1 = bookingFacade.addComment(createdUser.getId(), createdEvent.getId(), comment1);

        assertNotNull(storedComment1.getId());
        assertNotNull(storedComment1.getCreationTime());

        var eventFromDb = bookingFacade.getEventById(createdEvent.getId());
        assertNotEquals(createdEvent.getRating(), eventFromDb.getRating());

        var comment2 = Comment.builder()
                .comment("Excellent")
                .rating(4)
                .build();

        bookingFacade.addComment(createdUser.getId(), createdEvent.getId(), comment2);

        var commentsForEvent = bookingFacade.getCommentsForEvent(createdEvent.getId(), 10, 1);
        var commentsForUser = bookingFacade.getCommentsForUser(createdUser.getId(), 10, 1);

        assertFalse(commentsForEvent.isEmpty());
        assertEquals(2, commentsForUser.size());

        bookingFacade.deleteEvent(createdEvent.getId());
        bookingFacade.deleteUser(createdUser.getId());
    }

}
