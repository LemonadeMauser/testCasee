package com.example.testcase.service;

import com.example.testcase.dto.RegistrationDto;
import com.example.testcase.exception.NotFoundEx;
import com.example.testcase.model.Task;
import com.example.testcase.model.User;
import com.example.testcase.model.enums.Role;
import com.example.testcase.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo repo;
    private final ModelMapper mapper;


    public User findByIdOrThrow(Long userId) {
        return repo.findById(userId).orElseThrow(() -> new NotFoundEx("Пользователь " +
                "с userId=" + userId + " не найден."));
    }

    protected Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public User addUser(RegistrationDto dto) {
        User user = registrationDtoToEntity(dto);
        user.setRoles(Collections.singleton(Role.User));
        user.setActive(true);
        return repo.save(user);
    }

    private User registrationDtoToEntity(RegistrationDto dto) {
        return mapper.map(dto, User.class);
    }

    public void reassignTask(Task updatedTask, User newExecutor) {
        User prevExecutor = updatedTask.getExecutor();
        List<Task> tasks = prevExecutor.getTasks();
        tasks.removeIf(task -> task.getExecutor().getId().equals(updatedTask.getExecutor().getId()));
        newExecutor.getTasks().add(updatedTask);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        return repo.findByUsername(username).orElseThrow(
                () -> new NotFoundEx("Пользователь с никнеймом "
                + username +" не найден."));
    }
}
