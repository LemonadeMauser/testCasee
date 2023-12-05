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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepo repo;
    private final ModelMapper mapper;
    private final UserService userService;
    private Task task;

    public TaskDto save(TaskDto dto) {
        task = dtoToEntity(dto);
        task.setStatus(Status.WAITING);
        return entityToDto(repo.save(task));
    }

    public TaskDto getById(Long id) {
        task = findByIdOrThrow(id);
        return entityToDto(task);
    }

    public TaskDto updateTaskById(Long taskId, Long authorId, TaskDto dto) {
        task = findByIdOrThrow(taskId);
        accessCheck(task, authorId);
        if (dto.getMessage() != null) {
            task.setMessage(dto.getMessage());
        }
        if (dto.getExecutorId() != null) {
            User newExecutor  = userExistChecker(dto.getExecutorId());
            userService.reassignTask(task, newExecutor);
            task.setExecutor(newExecutor);
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
        task = findByIdOrThrow(taskId);
        accessCheck(task, userId);
        User executor = task.getExecutor();
        if (!executor.getId().equals(userId)) {
            throw new AccessEx("User with ID " + userId + " is not an executor for this task");
        }
        task.setStatus(status);

        return entityToDto(repo.save(task));
    }

    public List<TaskDto> getTasksByAuthorId(Long authorId, Pageable page) {
        User author = userExistChecker(authorId);
        List<Task> tasks = repo.findAllByAuthor(author, page);
        return entityListToDtoList(tasks);
    }

    public List<TaskDto> getTasksByExecutorId(Long executorId, Pageable page) {
        userExistChecker(executorId);
        List<Task> tasks = repo.findAllTasksByExecutorId(executorId, page);
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

    protected Task findByIdOrThrow(Long taskId) {
        return repo.findById(taskId).orElseThrow(() -> new NotFoundEx("NOT Found"));
    }

    private Task dtoToEntity(TaskDto dto) {
        task = mapper.map(dto, Task.class);
        User user = userExistChecker(dto.getAuthorId());
        task.setAuthor(user);
        user = userExistChecker(dto.getExecutorId());
        task.setExecutor(user);
        return task;
    }

    private TaskDto entityToDto(Task task) {
        TaskDto dto = mapper.map(task, TaskDto.class);
        dto.setAuthorId(task.getAuthor().getId());
        dto.setExecutorId(task.getExecutor().getId());
        return dto;
    }

    private User userExistChecker(Long userId) {
        return userService.findByIdOrThrow(userId);
    }

    private List<TaskDto> entityListToDtoList(List<Task> tasks) {
       return tasks.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
