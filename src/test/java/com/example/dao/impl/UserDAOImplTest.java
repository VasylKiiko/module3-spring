package com.example.dao.impl;

import com.example.config.TestDAOBeans;
import com.example.config.TestDatabaseConfiguration;
import com.example.dao.UserDAO;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(classes = {TestDatabaseConfiguration.class, TestDAOBeans.class})
class UserDAOImplTest {

    @Autowired
    private UserDAO userDAO;

    @Test
    void testGetUserById_ifUserExists() {
        var addedUser = userDAO.createUser(User.builder().email("test").name("test test").build());
        var userFromDB = userDAO.getUserById(addedUser.getId());

        assertEquals(addedUser, userFromDB);
    }

    @Test
    void shouldThrowException_whenUserNotExists() {
        Exception exception = assertThrows(UserNotFoundException.class, () -> userDAO.getUserById(11L));
        var expectedString = "User with id 11 not found";

        assertEquals(expectedString, exception.getMessage());
    }

    @Test
    void shouldUpdateUser_ifUserExists() {
        var userFromDB = userDAO.getUserById(2L);
        var oldName = userFromDB.getName();

        userFromDB.setName("New name");
        var updatedUser = userDAO.updateUser(userFromDB);

        assertNotEquals(oldName, updatedUser.getName());
    }

    @Test
    void shouldThrowException_ifUserNotFound() {
        var userToUpdate = User.builder()
                .id(100L)
                .name("some name")
                .email("email")
                .build();

        assertThrows(UserNotFoundException.class, () -> userDAO.updateUser(userToUpdate));
    }

    @Test
    void shouldReturnTrue_ifUserWasDeleted() {
        assertTrue(userDAO.deleteUser(10L));
    }

    @Test
    void shouldReturnFalse_ifUserNotFound() {
        assertFalse(userDAO.deleteUser(100L));
    }

    @Test
    void shouldReturnEmptyList_ifNoUserWithName() {
        var users = userDAO.getUsersByName("no such name", 10, 1);

        assertTrue(users.isEmpty());
    }

    @Test
    void shouldReturnUsersList_ifUserWithNameExists() {
        var users = userDAO.getAll();
        System.out.println(users);
        var usersPage1 = userDAO.getUsersByName("asyl", 3, 1);
        var usersPage2 = userDAO.getUsersByName("asyl", 3, 2);

        assertTrue(usersPage1.size() > 0);
        assertTrue(usersPage2.size() > 0);
        usersPage1.addAll(usersPage2);
        usersPage1.forEach(user -> assertEquals("Vasyl", user.getName()));
    }

    @Test
    void shouldReturnUser_ifUserWithEmailExists() {
        final String testEmail = "sofia@gmail.com";
        var userFromDB = userDAO.getUserByEmail(testEmail);

        assertEquals(testEmail, userFromDB.getEmail());
    }

    @Test
    void shouldThrowException_ifUserWithEmailDoesNotExist() {
        assertThrows(UserNotFoundException.class, () -> userDAO.getUserByEmail("not existed email"));
    }

    @Test
    void shouldThrowException_ifUserWithEmailAlreadyExists() {
        var newUser = User.builder().name("test").email("e@mail.com").build();

        assertThrows(UserAlreadyExistsException.class, () -> userDAO.createUser(newUser));
    }

    @Test
    void shouldReturnAllUsers() {
        var users = userDAO.getAll();

        assertEquals(10, users.size());
    }

}