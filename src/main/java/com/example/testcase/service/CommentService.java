package com.example.testcase.service;

import com.example.testcase.dto.CommentDto;
import com.example.testcase.exception.ValidationEx;
import com.example.testcase.model.Comment;
import com.example.testcase.model.Task;
import com.example.testcase.model.User;
import com.example.testcase.repository.CommentRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ModelMapper mapper;
    private final CommentRepo repo;
    private final TaskService taskService;
    private final UserService userService;

    public List<CommentDto> getAllCommentByTaskId(Long taskId, Pageable page, String filter) {
        if(filter.trim().isEmpty()){
            List<Comment> commentList = repo.
                    findAllByTaskIdAndTextContainingIgnoreCase(taskId, filter, page);
            return entityListToDtoList(commentList);
        }
        taskService.findByIdOrThrow(taskId);
        List<Comment> commentsList = repo.findAllByTaskId(taskId, page);
        return entityListToDtoList(commentsList);
    }

    public CommentDto addComment(Long taskId, Long userId, CommentDto dto) {
        Task task = taskService.findByIdOrThrow(taskId);
        User author =  userService.findByIdOrThrow(userId);
        Comment comment = dtoToEntity(dto);
        comment.setAuthor(author);
        comment.setTask(task);
        return  entityToDto(repo.save(comment));
    }

    public List<CommentDto> getAllCommentByAuthorId(Long authorId, Pageable page,
                                                    String filter) {
        if(filter.trim().isEmpty()){
            List<Comment> commentList = repo.
                    findAllByAuthorIdAndTextContainingIgnoreCase(authorId, filter, page);
            return entityListToDtoList(commentList);
        }
            userService.findByIdOrThrow(authorId);
            List<Comment> commentList = repo.findAllByAuthorId(authorId,page);
            return entityListToDtoList(commentList);
    }

    public List<CommentDto> search(String text, Pageable page) {
        if(text.trim().isEmpty())
            throw new ValidationEx("Текст по которому происходит поиск не может быть пустым.");
        List<Comment> comments = repo.findAllByTextContainingIgnoreCase(text.trim(),page);
        return entityListToDtoList(comments);
    }

    private CommentDto entityToDto(Comment comment) {
        CommentDto dto = mapper.map(comment, CommentDto.class);
        dto.setTaskId(comment.getTask().getId());
        dto.setAuthorId(comment.getAuthor().getId());
        return dto;
    }

    private List<CommentDto> entityListToDtoList(List<Comment> comments) {
        return comments.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    private Comment dtoToEntity(CommentDto dto) {
        return mapper.map(dto, Comment.class);
    }
}
