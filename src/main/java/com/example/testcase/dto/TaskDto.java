package com.example.testcase.dto;

import com.example.testcase.model.enums.Priority;
import com.example.testcase.model.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    String message;
    String header;
    Priority priority;
    Status status;
    Long authorId;
    Long executorId;
}
