package com.example.testcase.controller;

import com.example.testcase.dto.UserDto;
import com.example.testcase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser (@RequestBody UserDto userDto) {
        System.out.println(userDto.getUsername());
        return userService.save(userDto);
    }

}
