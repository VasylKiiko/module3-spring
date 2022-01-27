package com.example.config;

import com.example.dao.*;
import com.example.dao.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDAOBeans {
    @Bean
    public UserDAO userDAO() {
        return new UserDAOImpl();
    }

    @Bean
    public EventDAO eventDAO() {
        return new EventDAOImpl();
    }

    @Bean
    public UserAccountDAO userAccountDAO() {
        return new UserAccountDAOImpl();
    }

    @Bean
    public CommentDAO commentDAO() {
        return new CommentDAOImpl();
    }

    @Bean
    public BookingDAO bookingDAO(UserDAO userDAO, EventDAO eventDAO) {
        return new BookingDAOImpl(userDAO, eventDAO);
    }
}
