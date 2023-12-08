package com.example.testcase.controller;

import com.example.testcase.dto.CommentDto;
import com.example.testcase.service.CommentService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService service;


    @GetMapping("/{taskId}")
    public List<CommentDto> getAllCommentByTaskId(@PathVariable Long taskId,
                                                  @PositiveOrZero @RequestParam(name = "from",
                                                          defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size",
                                                          defaultValue = "20") Integer size,
                                                  @RequestParam String filter) {
        Pageable page = PageRequest.of(from / size, size);
        return service.getAllCommentByTaskId(taskId, page, filter);
    }

    @PostMapping("/{taskId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@RequestHeader("X-SMedia-User-Id") Long requestSenderId,
                                @PathVariable Long taskId,
                                 @RequestBody CommentDto dto) {
        return service.addComment(taskId, requestSenderId, dto);
    }

    @GetMapping("author/{authorId}")
    public List<CommentDto> getAllCommentByAuthorId(@PathVariable Long authorId,
                                                    @PositiveOrZero @RequestParam(name = "from",
                                                            defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(name = "size",
                                                            defaultValue = "20") Integer size,
                                                    @RequestParam String filter) {
        Pageable page = PageRequest.of(from / size, size);
        return service.getAllCommentByAuthorId(authorId, page, filter);
    }
}

