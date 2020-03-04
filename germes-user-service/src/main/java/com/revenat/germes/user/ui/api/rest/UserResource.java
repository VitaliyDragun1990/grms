package com.revenat.germes.user.ui.api.rest;

import com.revenat.germes.common.core.shared.exception.AuthenticationException;
import com.revenat.germes.common.core.shared.transform.Transformer;
import com.revenat.germes.user.core.application.Authenticator;
import com.revenat.germes.user.core.application.UserService;
import com.revenat.germes.user.core.application.LoginInfo;
import com.revenat.germes.user.core.application.UserInfo;
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
public class UserResource {

    private final UserService userService;

    private final Transformer transformer;

    private final Authenticator authenticator;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserInfo> findAll() {
        return userService.findAll().stream()
                .map(user -> transformer.transform(user, UserInfo.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @PostMapping(path = "login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserInfo login(@Valid @RequestBody final LoginInfo loginInfo) {
        return authenticator.authenticate(loginInfo.getUserName(), loginInfo.getHashedPassword())
                .map(user -> transformer.transform(user, UserInfo.class))
                .orElseThrow(() -> new AuthenticationException("Invalid login/password for user " + loginInfo.getUserName()));
    }
}
