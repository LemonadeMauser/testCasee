package com.example.testcase.controller;

import com.example.testcase.dto.UserDto;
import com.example.testcase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser (@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

}
