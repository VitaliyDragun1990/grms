package com.revenat.germes.gateway.presentation.security.controller;

import com.revenat.germes.gateway.domain.model.token.jwt.JwtProcessor;
import com.revenat.germes.user.presentation.rest.client.UserFacade;
import com.revenat.germes.user.presentation.rest.dto.LoginDTO;
import com.revenat.germes.user.presentation.rest.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Handles client authentication requests
 *
 * @author Vitaliy Dragun
 */
@RestController
@RequestMapping("user/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserFacade userFacade;

    private final JwtProcessor jwtProcessor;

    @PostMapping(
            path = "login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> login(@Valid @RequestBody final LoginDTO loginDTO) {
        final UserDTO user = userFacade.login(loginDTO);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtProcessor.generateToken(user.getUserName()));

        return ResponseEntity.ok().headers(headers).body(user);
    }
}
