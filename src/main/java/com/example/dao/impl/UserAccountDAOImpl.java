package com.example.dao.impl;

import com.example.dao.UserAccountDAO;
import com.example.exception.AccountAlreadyRegisteredException;
import com.example.exception.AccountNotFoundException;
import com.example.exception.InvalidPasswordException;
import com.example.model.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@Repository
public class UserAccountDAOImpl implements UserAccountDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAccount registerNewUserAccount(UserAccount userAccount) {
        var accountFromDB = entityManager.find(UserAccount.class, userAccount.getId());
        if (accountFromDB != null) {
            throw new AccountAlreadyRegisteredException();
        }

        entityManager.persist(userAccount);
        log.info("Account added {}", userAccount);
        return userAccount;
    }

    @Override
    public boolean withdrawMoneyFromAccount(Long accountId, String password, Double totalToWithdraw) {
        var account = entityManager.find(UserAccount.class, accountId);
        if (account == null) {
            throw new AccountNotFoundException();
        }
        if (!account.getPassword().equals(password)) {
            throw new InvalidPasswordException();
        }

        if (totalToWithdraw > account.getAmount()) {
            return false;
        }

        account.setAmount(account.getAmount() - totalToWithdraw);
        return true;
    }

}
