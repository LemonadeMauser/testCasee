package com.example.testcase.controller;

import com.example.testcase.dto.TaskDto;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto saveTask (@RequestHeader("X-SMedia-User-Id") Long requestSenderId,
                             @RequestBody TaskDto dto) {
        log.info("Received DTO: {}", dto);
        return taskService.save(dto, requestSenderId);
    }

    @GetMapping("/{taskId}")
    public TaskDto getById(@PathVariable Long taskId) {
        return taskService.getById(taskId);
    }

    @PatchMapping("/{taskId}")
    public TaskDto updateById(@RequestHeader("X-SMedia-User-Id") Long requestSenderId,
                              @PathVariable Long taskId,
                              @RequestBody TaskDto dto) {
        return taskService.updateTaskById(taskId, requestSenderId, dto);
    }

    @DeleteMapping("/{taskId}")
    public void deleteById(@RequestHeader("X-SMedia-User-Id") Long requestSenderId,
                           @PathVariable Long taskId) {
        taskService.deleteById(taskId, requestSenderId);
    }

    @PatchMapping("/status")
    public TaskDto updateStatus(@RequestHeader("X-SMedia-User-Id") Long requestSenderId,
                                @RequestParam Long taskId,
                                @RequestParam Status status) {
        return taskService.updateStatus(taskId, requestSenderId, status);
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
