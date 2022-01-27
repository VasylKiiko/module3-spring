package com.example.dao;

import com.example.model.User;

import java.util.List;

public interface UserDAO {

    User getUserById(Long userId);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User createUser(User user);

    User updateUser(User user);

    boolean deleteUser(long userId);

    List<User> getAll();

    boolean userExistsByEmail(String email);
}
