package com.example.service.impl;

import com.example.dao.UserAccountDAO;
import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.model.UserAccount;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final UserAccountDAO userAccountDAO;

    @Override
    public User getUserById(long userId) {
        return userDAO.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userDAO.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        return userDAO.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userDAO.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userDAO.deleteUser(userId);
    }

    @Override
    public UserAccount registerNewAccountForUser(Long userId, UserAccount userAccount) {
        var userFromDB = userDAO.getUserById(userId);
        userAccount.setUser(userFromDB);
        return userAccountDAO.registerNewUserAccount(userAccount);
    }
}
