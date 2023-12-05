package com.example.testcase.service;

import com.example.testcase.dto.UserDto;
import com.example.testcase.exception.NotFoundEx;
import com.example.testcase.model.Task;
import com.example.testcase.model.User;
import com.example.testcase.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo repo;
    private final ModelMapper mapper;

    public UserDto save(UserDto userDto) {
        return entityToDto(repo.save(dtoToEntity(userDto)));
    }


    protected User findByIdOrThrow(Long userId) {
        return repo.findById(userId).orElseThrow(()-> new NotFoundEx("User not found"));
    }

//    protected List<Task> getTasksByExecutorId(Long userId) {
//        return repo.findAllTasksById(userId);
//    }

    private User dtoToEntity(UserDto dto) {
        return mapper.map(dto, User.class);
    }

    private UserDto entityToDto(User user) {
        return mapper.map(user, UserDto.class);
    }

    public void reassignTask(Task updatedTask, User newExecutor) {

        User prevExecutor = updatedTask.getExecutor();
        List<Task> tasks = prevExecutor.getTasks();
        tasks.removeIf(task -> task.getExecutor().getId().equals(updatedTask.getExecutor().getId()));
        newExecutor.getTasks().add(updatedTask);
    }
}
