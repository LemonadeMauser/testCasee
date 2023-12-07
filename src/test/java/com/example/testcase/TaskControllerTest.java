package com.example.testcase;

import com.example.testcase.Utility.JwtTokenUtils;
import com.example.testcase.controller.TaskController;
import com.example.testcase.dto.TaskDto;
import com.example.testcase.model.User;
import com.example.testcase.model.enums.Priority;
import com.example.testcase.model.enums.Role;
import com.example.testcase.model.enums.Status;
import com.example.testcase.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TaskController.class)
class TaskControllerTest {

    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private TaskService taskService;

    @MockBean
    private JwtTokenUtils jwtTokenUtils;

    private TaskDto dto;
    private User author;
    private User executor;
    private Long authorId;
    private Long executorId;
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

        dto = TaskDto.builder()
                .message("Some_message")
                .header("Some_header")
                .priority(Priority.HIGH)
                .status(Status.WAITING)
                .authorId(authorId)
                .executorId(executorId)
                .build();

    }

    @Test
    void contextLoad() {
        assertThat(taskService).isNotNull();
    }

    @Test
    @WithMockUser
    void testAddTaskOkWhenValid() throws Exception {
        when(taskService.save(any(TaskDto.class), anyLong()))
                .thenReturn(dto);


        mvc.perform(post("/tasks")
                        .header("X-SMedia-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Some_message")))
                .andExpect(jsonPath("$.header", is("Some_header")))
                .andExpect(jsonPath("$.priority", is("HIGH")))
                .andExpect(jsonPath("$.authorId", is(authorId.intValue())))
                .andExpect(jsonPath("$.executorId", is(executorId.intValue())))
                .andReturn();

    }

    @Test
    @WithMockUser
    void testGetByIdOkWhenValid() throws Exception {
        when(taskService.getById(anyLong()))
                .thenReturn(dto);


        mvc.perform(get("/tasks/1")
                        .header("X-SMedia-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.message", is("Some_message")))
                .andExpect(jsonPath("$.header", is("Some_header")))
                .andExpect(jsonPath("$.priority", is("HIGH")))
                .andExpect(jsonPath("$.authorId", is(authorId.intValue())))
                .andExpect(jsonPath("$.executorId", is(executorId.intValue())))
                .andReturn();

    }

    @Test
    @WithMockUser
    void testUpdateByIdOkWhenValid() throws Exception {
        TaskDto newDto = TaskDto.builder()
                .message("Updated")
                .header("Updated_header")
                .priority(Priority.HIGH)
                .authorId(authorId)
                .executorId(executorId)
                .build();

        when(taskService.updateTaskById(anyLong(), anyLong(), any(TaskDto.class)))
                .thenReturn(newDto);


        mvc.perform(patch("/tasks/1")
                        .header("X-SMedia-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.message", is("Updated")))
                .andExpect(jsonPath("$.header", is("Updated_header")))
                .andExpect(jsonPath("$.priority", is("HIGH")))
                .andExpect(jsonPath("$.authorId", is(authorId.intValue())))
                .andExpect(jsonPath("$.executorId", is(executorId.intValue())))
                .andReturn();

    }

    @Test
    @WithMockUser
    void testDeleteByIdOkWhenValid() throws Exception {
        mvc.perform(delete("/tasks/1")
                        .header("X-SMedia-User-Id", authorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testUpdateStatusOkWhenValid() throws Exception {
        TaskDto newDto = TaskDto.builder()
                .message("Some_message")
                .header("Some_header")
                .priority(Priority.HIGH)
                .status(Status.IN_PROGRESS)
                .authorId(authorId)
                .executorId(executorId)
                .build();
        when(taskService.updateStatus(anyLong(), anyLong(), any(Status.class)))
                .thenReturn(newDto);


        mvc.perform(patch("/tasks/status")
                        .header("X-SMedia-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .param("taskId", "1")
                        .param("status", "IN_PROGRESS")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.message", is("Some_message")))
                .andExpect(jsonPath("$.header", is("Some_header")))
                .andExpect(jsonPath("$.priority", is("HIGH")))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")))
                .andExpect(jsonPath("$.authorId", is(authorId.intValue())))
                .andExpect(jsonPath("$.executorId", is(executorId.intValue())))
                .andReturn();
    }

    @Test
    @WithMockUser
    void testGetTasksByAuthorIdOkWhenValid() throws Exception {
        when(taskService.getTasksByAuthorId(anyLong(), eq(page)))
                .thenReturn(List.of(dto));


        mvc.perform(get("/tasks/author/" + authorId)
                        .param("from", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is("Some_message")))
                .andExpect(jsonPath("$[0].header", is("Some_header")))
                .andExpect(jsonPath("$[0].priority", is("HIGH")))
                .andExpect(jsonPath("$[0].authorId", is(authorId.intValue())))
                .andExpect(jsonPath("$[0].executorId", is(executorId.intValue())))
                .andReturn();

    }

    @Test
    @WithMockUser
    void testGetTasksByExecutorIdOkWhenValid() throws Exception {
        when(taskService.getTasksByAuthorId(anyLong(), eq(page)))
                .thenReturn(List.of(dto));


        mvc.perform(get("/tasks/executor/" +executorId )
                        .param("from", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();

    }
}