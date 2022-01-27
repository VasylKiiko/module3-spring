package com.example.dao.impl;

import com.example.config.TestDAOBeans;
import com.example.config.TestDatabaseConfiguration;
import com.example.dao.UserAccountDAO;
import com.example.dao.UserDAO;
import com.example.exception.AccountAlreadyRegisteredException;
import com.example.exception.AccountNotFoundException;
import com.example.exception.InvalidPasswordException;
import com.example.model.UserAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDatabaseConfiguration.class, TestDAOBeans.class})
class UserAccountDAOImplTest {
    @Autowired
    private UserAccountDAO userAccountDAO;

    @Autowired
    private UserDAO userDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldCreateUserAccount() {
        var userFromDB = userDAO.getUserById(3L);
        var userAccount = UserAccount.builder()
                .id(654321L)
                .password("test")
                .amount(400.0)
                .user(userFromDB)
                .build();

        userAccountDAO.registerNewUserAccount(userAccount);
        entityManager.flush();
        var accountList = userFromDB.getAccounts();

        assertFalse(accountList.isEmpty());
    }

    @Test
    void shouldThrowException_ifAccountAlreadyRegistered() {
        var userFromDB = userDAO.getUserById(3L);
        var userAccount = UserAccount.builder()
                .id(1234L)
                .password("test")
                .amount(400.0)
                .user(userFromDB)
                .build();

        assertThrows(AccountAlreadyRegisteredException.class,
                () -> userAccountDAO.registerNewUserAccount(userAccount));
    }

    @Test
    void shouldWithdrawMoneyFromAccount_ifPasswordValidAndEnoughMoney() {
        var accountFromDBBefore = entityManager.find(UserAccount.class, 1234L);
        var startAmount = accountFromDBBefore.getAmount();
        var isChanged = userAccountDAO.withdrawMoneyFromAccount(accountFromDBBefore.getId(),
                accountFromDBBefore.getPassword(), 100.0);

        assertTrue(isChanged);

        entityManager.flush();
        var accountFromDBAfter = entityManager.find(UserAccount.class, 1234L);

        assertNotEquals(startAmount, accountFromDBAfter.getAmount());
    }

    @Test
    void shouldReturnFalse_ifNotEnoughMoney() {
        var accountFromDBBefore = entityManager.find(UserAccount.class, 1234L);
        var startAmount = accountFromDBBefore.getAmount();

        assertFalse(startAmount > 10_000);

        var isChanged = userAccountDAO.withdrawMoneyFromAccount(accountFromDBBefore.getId(),
                accountFromDBBefore.getPassword(), 10_000.0);

        assertFalse(isChanged);

        entityManager.flush();
        var accountFromDBAfter = entityManager.find(UserAccount.class, 1234L);

        assertEquals(startAmount, accountFromDBAfter.getAmount());
    }

    @Test
    void shouldThrowException_ifInvalidPassword() {
        var accountFromDB = entityManager.find(UserAccount.class, 1234L);
        assertThrows(InvalidPasswordException.class,
                () -> userAccountDAO.withdrawMoneyFromAccount(accountFromDB.getId(),
                        accountFromDB.getPassword().substring(1), 1.0)
        );
    }

    @Test
    void shouldThrowException_ifInvalidAccountId() {
        assertThrows(AccountNotFoundException.class, () ->
                userAccountDAO.withdrawMoneyFromAccount(-1L, "wrong", 1.0));
    }
}