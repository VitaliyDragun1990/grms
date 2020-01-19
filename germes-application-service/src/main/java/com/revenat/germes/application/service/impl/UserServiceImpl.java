package com.revenat.germes.application.service.impl;

import com.revenat.germes.application.infrastructure.exception.flow.ValidationException;
import com.revenat.germes.application.infrastructure.helper.Checker;
import com.revenat.germes.application.model.entity.person.User;
import com.revenat.germes.application.service.UserService;
import com.revenat.germes.persistence.repository.UserRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Vitaliy Dragun
 */
@Named
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Validator validator;

    private final Checker checker = new Checker();

    @Inject
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;

        final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void save(final User user) {
        checker.checkParameter(user != null, "user to save can not be null");

        final Set constraintViolations = validator.validate(user);
        if (!constraintViolations.isEmpty()) {
            throw new ValidationException("User validation failure", constraintViolations);
        }

        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(final int userId) {
        return userRepository.findById(userId);
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
