package com.revenat.germes.gateway.ui.api.rest;

import com.revenat.germes.gateway.core.token.jwt.JwtProcessor;
import com.revenat.germes.user.core.application.UserFacade;
import com.revenat.germes.user.core.application.LoginInfo;
import com.revenat.germes.user.core.application.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Handles client authentication requests
 *
 * @author Vitaliy Dragun
 */
@RestController
@RequestMapping("user/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.POST}, exposedHeaders = {"Authorization"})
public class AuthenticationResource {

    private final UserFacade userFacade;

    private final JwtProcessor jwtProcessor;

    @PostMapping(
            path = "login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfo> login(@Valid @RequestBody final LoginInfo loginInfo) {
        final UserInfo user = userFacade.login(loginInfo);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtProcessor.generateToken(user.getUserName()));

        return ResponseEntity.ok().headers(headers).body(user);
    }
}
