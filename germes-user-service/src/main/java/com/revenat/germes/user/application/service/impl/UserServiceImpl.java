package com.revenat.germes.user.application.service.impl;

import com.revenat.germes.user.model.entity.User;
import com.revenat.germes.user.persistence.repository.UserRepository;
import com.revenat.germes.user.application.service.UserService;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * @author Vitaliy Dragun
 */
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository userRepository) {
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
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}