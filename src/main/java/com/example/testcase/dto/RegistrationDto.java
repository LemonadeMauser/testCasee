package com.example.testcase.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationDto {

    @NotEmpty(message = "Имя пользователя не может быть пустым")
    String username;

    @NotEmpty(message = "Имейл не может быть пустым")
    @Email
    String email;

    @NotEmpty(message = "Пароль не может быть пустым")
    String password;

    @NotEmpty(message = "Подтвердите пароль")
    String confirmPassword;
}
