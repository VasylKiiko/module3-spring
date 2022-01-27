package com.example.dao.impl;

import com.example.dao.UserDAO;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUserById(Long userId) {
        var user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        log.info("User {} found", user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            var user = entityManager
                    .createNamedQuery("User.get_user_by_email", User.class)
                    .setParameter("email", email)
                    .setHint("org.hibernate.cacheable", true)
                    .getSingleResult();
            log.info("User {} found", user);
            return user;
        } catch (NoResultException ex) {
            throw new UserNotFoundException(String.format("User with email %s not found", email));
        }
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        var selectUsersQuery = entityManager
                .createNamedQuery("User.get_users_with_name", User.class)
                .setParameter("name", name)
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize);

        var usersList = selectUsersQuery.getResultList();

        log.info("Users found: {}", usersList);
        return usersList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User createUser(User user) {
        if (userExistsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(String.format("User with current email %s already exists!", user.getEmail()));
        }
        entityManager.persist(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        var userFromDB = entityManager.find(User.class, user.getId());
        if (userFromDB == null) {
            throw new UserNotFoundException();
        }
        userFromDB.setName(user.getName());
        userFromDB.setEmail(user.getEmail());
        entityManager.merge(userFromDB);
        return userFromDB;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteUser(long userId) {
        var userToDelete = entityManager.find(User.class, userId);
        if (userToDelete == null) {
            return false;
        }
        entityManager.remove(userToDelete);
        return true;
    }

    @Override
    public List<User> getAll() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    public boolean userExistsByEmail(String email) {
        try {
            entityManager.createQuery("select u from User u where u.email=:email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return false;
        }
        return true;
    }
}
