package com.example.testcase;

import com.example.testcase.Utility.JwtTokenUtils;
import com.example.testcase.controller.CommentController;
import com.example.testcase.controller.TaskController;
import com.example.testcase.dto.CommentDto;
import com.example.testcase.dto.TaskDto;
import com.example.testcase.model.Task;
import com.example.testcase.model.User;
import com.example.testcase.model.enums.Priority;
import com.example.testcase.model.enums.Role;
import com.example.testcase.model.enums.Status;
import com.example.testcase.service.CommentService;
import com.example.testcase.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTest {

        private MockMvc mvc;

        @Autowired
        private ObjectMapper mapper;

        @Autowired
        private WebApplicationContext webApplicationContext;

        @MockBean
        private CommentService commentService;

        @MockBean
        private JwtTokenUtils jwtTokenUtils;

        private CommentDto dto;
        private Task task;
        private User author;
        private User executor;
        private Long authorId;
        private Long executorId;

        private Long taskId;
        private Pageable page;

        @BeforeEach
        void setUp() {
            mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

            page = PageRequest.of(0,20);
            author = User.builder()
                    .id(2L)
                    .username("testUsername")
                    .email("test@test.ru")
                    .password("password")
                    .roles(Collections.singleton(Role.User))
                    .build();

            executor = User.builder()
                    .id(1L)
                    .username("testUsername")
                    .email("test@test.ru")
                    .password("password")
                    .roles(Collections.singleton(Role.User))
                    .build();

            authorId = author.getId();
            executorId = executor.getId();

            task = Task.builder()
                    .id(1L)
                    .message("Some_message")
                    .header("Some_header")
                    .priority(Priority.HIGH)
                    .status(Status.WAITING)
                    .author(author)
                    .executor(executor)
                    .build();

            taskId = task.getId();

            dto = CommentDto.builder()
                    .text("Some_comment")
                    .taskId(taskId)
                    .authorId(authorId)
                    .build();
        }

        @Test
        void contextLoad() {
            assertThat(commentService).isNotNull();
        }

        @Test
        @WithMockUser
        void testAddTaskOkWhenValid() throws Exception {
            when(commentService.addComment(anyLong(), anyLong(), any(CommentDto.class)))
                    .thenReturn(dto);


            mvc.perform(post("/comments/" +taskId)
                            .header("X-SMedia-User-Id", 1)
                            .content(mapper.writeValueAsString(dto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.text", is("Some_comment")))
                    .andExpect(jsonPath("$.taskId", is(taskId.intValue())))
                    .andExpect(jsonPath("$.authorId", is(authorId.intValue())))
                    .andReturn();
        }

    @Test
    @WithMockUser
    void testGetAllCommentByTaskIdOkWhenValid() throws Exception {
        when(commentService.getAllCommentByTaskId(anyLong(), eq(page), anyString()))
                .thenReturn(List.of(dto));


        mvc.perform(get("/comments/" +taskId)
                        .param("from", "0")
                        .param("size", "20")
                        .param("filter", "  ")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].text", is("Some_comment")))
                .andExpect(jsonPath("$[0].taskId", is(taskId.intValue())))
                .andExpect(jsonPath("$[0].authorId", is(authorId.intValue())))
                .andReturn();
    }

    @Test
    @WithMockUser
    void testGetAllCommentsByAuthorIdOkWhenValid() throws Exception {
        when(commentService.getAllCommentByAuthorId(anyLong(), eq(page), anyString()))
                .thenReturn(List.of(dto));


        mvc.perform(get("/comments/" + executorId)
                        .param("from", "0")
                        .param("size", "20")
                        .param("filter", "2333")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
    }


}
