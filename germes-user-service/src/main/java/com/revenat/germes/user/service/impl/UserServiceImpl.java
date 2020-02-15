package com.revenat.germes.user.service.impl;

import com.revenat.germes.persistence.infrastructure.cdi.DBSource;
import com.revenat.germes.user.model.entity.User;
import com.revenat.germes.user.repository.UserRepository;
import com.revenat.germes.user.service.UserService;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Inject
    public UserServiceImpl(@DBSource final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(final User user) {
        requireNonNull(user, "user to save can not be null");

        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(final int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByUserName(final String userName) {
        requireNonNull(userName, "userName to find user with can not be null");

        return userRepository.findByUserName(userName);
    }

    @Override
    public void delete(final int userId) {
        userRepository.delete(userId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
