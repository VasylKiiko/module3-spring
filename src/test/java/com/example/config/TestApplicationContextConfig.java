package com.example.config;

import com.example.dao.*;
import com.example.facade.BookingFacade;
import com.example.facade.BookingFacadeImpl;
import com.example.service.BookingService;
import com.example.service.CommentService;
import com.example.service.EventService;
import com.example.service.UserService;
import com.example.service.impl.BookingServiceImpl;
import com.example.service.impl.CommentServiceImpl;
import com.example.service.impl.EventServiceImpl;
import com.example.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TestDAOBeans.class)
public class TestApplicationContextConfig {
    @Bean
    public BookingService bookingService(BookingDAO bookingDAO, UserAccountDAO userAccountDAO) {
        return new BookingServiceImpl(bookingDAO, userAccountDAO);
    }

    @Bean
    public UserService userService(UserDAO userDAO, UserAccountDAO userAccountDAO) {
        return new UserServiceImpl(userDAO, userAccountDAO);
    }

    @Bean
    public EventService eventService(EventDAO eventDAO) {
        return new EventServiceImpl(eventDAO);
    }

    @Bean
    public CommentService commentService(CommentDAO commentDAO, UserDAO userDAO, EventDAO eventDAO) {
        return new CommentServiceImpl(commentDAO, userDAO, eventDAO);
    }

    @Bean
    public BookingFacade bookingFacade(UserService userService, BookingService bookingService,
                                       EventService eventService, CommentService commentService) {
        return new BookingFacadeImpl(userService, bookingService, eventService, commentService);
    }

}
