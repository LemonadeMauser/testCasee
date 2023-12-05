package com.example.testcase.dto;

import com.example.testcase.model.User;
import com.example.testcase.model.enums.Priority;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDto {
    Long id;
    String message;
    String header;
    Priority priority;
    User author;
    User executor;
}
