package com.example.testcase.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtRequest {
    @NotEmpty(message = "Имя пользователя не может быть пустым")
    String username;
    @NotEmpty(message = "Пароль не может быть пустым")
    String password;
}
