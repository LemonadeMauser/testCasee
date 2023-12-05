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
    String message;
    String header;
    Priority priority;
    Long authorId;
    Long executorId;
}
