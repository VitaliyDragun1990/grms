package com.revenat.germes.user.core.application;

import com.revenat.germes.user.core.domain.model.User;
import com.revenat.germes.user.core.domain.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * @author Vitaliy Dragun
 */
@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
