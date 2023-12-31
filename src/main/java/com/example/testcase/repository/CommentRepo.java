package com.example.testcase.repository;

import com.example.testcase.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
   
    List<Comment> findAllByTaskId(Long taskId, Pageable page);

    List<Comment> findAllByAuthorId(Long authorId, Pageable page);

    List<Comment> findAllByTextContainingIgnoreCase(String text, Pageable page);

    List<Comment> findAllByAuthorIdAndTextContainingIgnoreCase(Long userId, String text, Pageable pageable);

    List<Comment> findAllByTaskIdAndTextContainingIgnoreCase(Long taskId, String text, Pageable pageable);
}
