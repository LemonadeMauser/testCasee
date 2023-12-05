package com.example.testcase.repository;

import com.example.testcase.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Long, Comment> {
}
