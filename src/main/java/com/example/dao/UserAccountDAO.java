package com.example.dao;

import com.example.model.UserAccount;

public interface UserAccountDAO {
    UserAccount registerNewUserAccount(UserAccount userAccount);

    boolean withdrawMoneyFromAccount(Long accountId, String password, Double totalToWithdraw);
}
