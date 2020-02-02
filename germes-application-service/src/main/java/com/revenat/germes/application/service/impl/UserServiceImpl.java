package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.model.entity.person.User;
import com.revenat.germes.application.service.UserService;
import com.revenat.germes.persistence.infrastructure.cdi.DBSource;
import com.revenat.germes.persistence.repository.UserRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

/**
 * @author Vitaliy Dragun
 */
@Named
@Dependent
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Checker checker = new Checker();

    @Inject
    public UserServiceImpl(@DBSource final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void save(final User user) {
        checker.checkParameter(user != null, "user to save can not be null");

        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(final int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByUserName(final String userName) {
        checker.checkParameter(userName != null, "userName to find user with can not be null");

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
