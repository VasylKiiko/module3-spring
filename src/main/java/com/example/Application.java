package com.example;

import com.example.config.AppConfig;
import com.example.facade.BookingFacade;
import com.example.model.Event;
import com.example.model.Ticket;
import com.example.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        BookingFacade bookingFacade = context.getBean(BookingFacade.class);

        testEventPart(bookingFacade);
        testUserPart(bookingFacade);
        testTicket(bookingFacade);
        scenario4(bookingFacade);
    }

    public static void testEventPart(BookingFacade bookingFacade) {
        var eventToUpdate = bookingFacade.getEventById(5L);
        System.out.println(eventToUpdate);
        System.out.println("----------------------");
        System.out.println(bookingFacade.getEventsByTitle("Star", 2, 1));
        System.out.println("----------------------");
        System.out.println(bookingFacade.getEventsForDay(LocalDate.of(2022, 1, 20), 3, 1));
        System.out.println("----------------------");
        var newEvent = bookingFacade.createEvent(
                Event.builder().date(LocalDate.parse("2022-02-21")).title("Exhibition \"Summer\"").build()
        );
        System.out.println(newEvent);
        System.out.println("----------------------");
        eventToUpdate.setTitle("Changed");
        bookingFacade.updateEvent(eventToUpdate);
        System.out.println(bookingFacade.getEventById(eventToUpdate.getId()));
        System.out.println("----------------------");
        System.out.println(bookingFacade.deleteEvent(eventToUpdate.getId()));
        System.out.println("----------------------");
    }

    public static void testUserPart(BookingFacade bookingFacade) {
        var user = bookingFacade.getUserById(1L);
        System.out.println(user);
        System.out.println("----------------------");
        System.out.println(bookingFacade.getUserByEmail("sofia@gmail.com"));
        System.out.println("----------------------");
        System.out.println(bookingFacade.getUsersByName("Vasyl", 10, 1));
        System.out.println("----------------------");
        System.out.println(bookingFacade.deleteUser(3L));
        System.out.println(bookingFacade.getUsersByName("Vasyl", 10, 1));
        System.out.println("----------------------");
        System.out.println(bookingFacade.createUser(User.builder().email("inna_mail@gmail.com").name("Inna").build()));
        System.out.println("----------------------");
        System.out.println(bookingFacade.getUserByEmail("inna_mail@gmail.com"));
        System.out.println("----------------------");
        user.setEmail("vasyl_kiiko@epam.com");
        System.out.println(bookingFacade.updateUser(user));
        System.out.println(bookingFacade.getUserById(1L));
    }

    public static void testTicket(BookingFacade bookingFacade) {
        var newTicket = bookingFacade.bookTicket(3, 1, 17, Ticket.Category.STANDARD);
        System.out.println(newTicket);
        System.out.println(bookingFacade.getBookedTicketsForUser(1L, 5, 1));
        System.out.println(bookingFacade.getBookedTicketsForEvent(1L, 10, 1));
//
        System.out.println("----------------------");
        var user3 = bookingFacade.getUserById(3L);
        var event1 = bookingFacade.getEventById(1L);
        //System.out.println(bookingFacade.getBookedTickets(user3, 10, 1));
        System.out.println(bookingFacade.getBookedTicketsForEvent(event1.getId(), 10, 1));

        System.out.println("----------------------");
        System.out.println(bookingFacade.cancelTicket(10));
        //System.out.println(bookingFacade.getBookedTickets(user3, 10, 1));
        System.out.println(bookingFacade.getBookedTicketsForEvent(event1.getId(), 10, 1));

        System.out.println("----------------------");
        var user7 = bookingFacade.getUserById(7L);
        var event5 = bookingFacade.getEventById(5L);
        System.out.println(bookingFacade.getBookedTicketsForUser(user7.getId(), 10, 1));
        System.out.println(bookingFacade.getBookedTicketsForEvent(event5.getId(), 10, 1));
    }

    public static void scenario4(BookingFacade bookingFacade) {
        var createdUser = bookingFacade.createUser(User.builder().email("inna_mail@gmail.com").name("Inna").build());
        var accountAfterPayment = createdUser.getAccounts().get(0);
        User user1 = User.builder().email("test1").name("test1").build();
        User user2 = User.builder().email("test2").name("test2").build();

        Ticket ticket1 = Ticket.builder()
                .place(1)
                .category(Ticket.Category.STANDARD)
                .user(user1)
                .build();

        Ticket ticket2 = Ticket.builder()
                .place(2)
                .category(Ticket.Category.STANDARD)
                .user(user2)
                .build();

        var tickets = List.of(ticket1);

        user2.setUserTickets(tickets);

        bookingFacade.payForTicket(2L, 1234L, "qwe");
    }
}
