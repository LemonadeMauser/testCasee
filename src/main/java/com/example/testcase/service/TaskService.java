package com.example.testcase.service;

import com.example.testcase.dto.TaskDto;
import com.example.testcase.exception.AccessEx;
import com.example.testcase.exception.NotFoundEx;
import com.example.testcase.model.Task;
import com.example.testcase.model.User;
import com.example.testcase.model.enums.Status;
import com.example.testcase.repository.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepo repo;
    private final ModelMapper mapper;
    private final UserService userService;

    public TaskDto save(TaskDto dto) {
        return entityToDto(repo.save(dtoToEntity(dto)));
    }

    public TaskDto getById(Long id) {
        Task task = findByIdOrThrow(id);
        return entityToDto(task);
    }

    public TaskDto updateTaskById(Long taskId, Long authorId, TaskDto dto) {
        Task task = findByIdOrThrow(taskId);
        accessCheck(task, authorId);
        if (dto.getMessage() != null) {
            task.setMessage(dto.getMessage());
        }
        if (dto.getExecutor() != null) {
            User user  = userService.findByIdOrThrow(dto.getExecutor().getId());
            userService.reassignTask(task, user);
            task.setExecutor(dto.getExecutor());
        }
        if (dto.getHeader() != null) {
            task.setHeader(dto.getHeader());
        }
        if (dto.getPriority() != null) {
            task.setPriority(dto.getPriority());
        }

        return entityToDto(repo.save(task));
    }

    public TaskDto updateStatus(Long taskId, Long userId, Status status) {
        Task task = findByIdOrThrow(taskId);
        accessCheck(task, userId);
        User executor = task.getExecutor();
        if (!executor.getId().equals(userId)) {
            throw new AccessEx("User with ID " + userId + " is not an executor for this task");
        }
        task.setStatus(status);

        return entityToDto(repo.save(task));
    }

    public List<TaskDto> getTasksByAuthorId(Long authorId, Pageable page) {
        User author = userService.findByIdOrThrow(authorId);
        List<Task> tasks = repo.findAllByAuthor(author, page);
        return entityListToDtoList(tasks);
    }

    public List<TaskDto> getTasksByExecutorId(Long executorId, Pageable page) {
        userService.findByIdOrThrow(executorId);
        List<Task> tasks = repo.findTasksByExecutorId(executorId, page);
        return entityListToDtoList(tasks);
    }

    public void deleteById(Long taskId, Long userId) {
        accessCheck(findByIdOrThrow(taskId), userId);
        repo.deleteById(taskId);
    }

    private void accessCheck(Task task, Long authorId) {
        if (!task.getAuthor().getId().equals(authorId)) {
            throw new AccessEx("u dont have a nihu9");
        }
    }

    public Task findByIdOrThrow(Long taskId) {
        return repo.findById(taskId).orElseThrow(() -> new NotFoundEx("NOT Found"));
    }

    private Task dtoToEntity(TaskDto dto) {
        return mapper.map(dto, Task.class);
    }

    private TaskDto entityToDto(Task task) {
        return mapper.map(task, TaskDto.class);
    }

    private List<TaskDto> entityListToDtoList(List<Task> tasks) {
       return tasks.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
