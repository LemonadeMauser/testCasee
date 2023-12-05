package com.example.testcase.controller;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.example.testcase.dto.TaskDto;
import com.example.testcase.model.Task;
import com.example.testcase.model.User;
import com.example.testcase.model.enums.Status;
import com.example.testcase.service.TaskService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto saveTask (@RequestBody TaskDto dto) {
        return taskService.save(dto);
    }

    @GetMapping("/{taskId}")
    public TaskDto getById(@PathVariable Long taskId) {
        return taskService.getById(taskId);
    }

    @PatchMapping("/{taskId}")
    public TaskDto updateById(@PathVariable Long taskId,
                                  @RequestParam Long authorId,
                                  @RequestBody TaskDto dto) {
        return taskService.updateTaskById(taskId, authorId, dto);
    }

    @DeleteMapping("/{taskId}")
    public void deleteById(@PathVariable Long taskId,
                           @RequestParam Long userId) {
        taskService.deleteById(taskId, userId);
    }

    @PatchMapping("/status")
    public TaskDto updateStatus(@RequestParam Long taskId,
                                @RequestParam Long userID,
                                @RequestParam Status status) {
        return taskService.updateStatus(taskId, userID, status);
    }

    @GetMapping("/author/{authorId}")
    public List<TaskDto> getTasksByAuthorId(@PathVariable Long authorId,
                                            @PositiveOrZero @RequestParam(name = "from",
                                                    defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size",
                                                    defaultValue = "20")Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return taskService.getTasksByAuthorId(authorId, page);
    }

    @GetMapping("/executor/{executorId}")
    public List<TaskDto> getTasksByExecutorId(@PathVariable Long executorId,
                                            @PositiveOrZero @RequestParam(name = "from",
                                                    defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size",
                                                    defaultValue = "20")Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return taskService.getTasksByExecutorId(executorId, page);
    }
}
