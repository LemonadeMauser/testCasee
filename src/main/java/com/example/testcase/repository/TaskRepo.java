package com.example.testcase.repository;

import com.example.testcase.model.Task;
import com.example.testcase.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    List<Task> findAllByAuthor(User author, Pageable page);

    List<Task> findAllTasksByExecutorId(@Param("executorId") Long executorId, Pageable page);
}
