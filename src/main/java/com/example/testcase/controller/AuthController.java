package com.example.testcase.controller;

import com.example.testcase.dto.JwtRequest;
import com.example.testcase.dto.RegistrationDto;
import com.example.testcase.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@Valid @RequestBody JwtRequest authRequest) {
        log.info("Поступил запрос на получения токена от {}", authRequest.getUsername());
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody RegistrationDto registrationUserDto) {
        log.info("Поступил запрос на регистрацию пользователя {}", registrationUserDto.getUsername());
        return authService.createNewUser(registrationUserDto);
    }
}
