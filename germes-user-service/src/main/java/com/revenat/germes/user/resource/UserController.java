package com.revenat.germes.user.resource;

import com.revenat.germes.infrastructure.exception.AuthenticationException;
import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.user.application.security.Authenticator;
import com.revenat.germes.user.application.service.UserService;
import com.revenat.germes.user.presentation.rest.dto.LoginDTO;
import com.revenat.germes.user.presentation.rest.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vitaliy Dragun
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    private final Transformer transformer;

    private final Authenticator authenticator;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> findAll() {
        return userService.findAll().stream()
                .map(user -> transformer.transform(user, UserDTO.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @PostMapping(path = "login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO login(@Valid @RequestBody final LoginDTO loginDTO) {
        return authenticator.authenticate(loginDTO.getUserName(), loginDTO.getHashedPassword())
                .map(user -> transformer.transform(user, UserDTO.class))
                .orElseThrow(() -> new AuthenticationException("Invalid login/password for user " + loginDTO.getUserName()));
    }
}
