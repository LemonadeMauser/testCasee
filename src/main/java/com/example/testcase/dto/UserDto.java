package com.example.testcase.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long id;
    @NotNull(message = "Имя пользователя не может быть пустым")
    String username;
    @Email(message = "Email введен некорректно")
    @NotNull(message = "Email не может быть пустым")
    String email;
}
