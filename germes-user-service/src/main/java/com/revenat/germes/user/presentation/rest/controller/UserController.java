package com.revenat.germes.user.presentation.rest.controller;

import com.revenat.germes.infrastructure.transform.Transformer;
import com.revenat.germes.user.presentation.rest.dto.UserDTO;
import com.revenat.germes.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<UserDTO> findAll() {
        return userService.findAll().stream()
                .map(user -> transformer.transform(user, UserDTO.class))
                .collect(Collectors.toUnmodifiableList());
    }
}
