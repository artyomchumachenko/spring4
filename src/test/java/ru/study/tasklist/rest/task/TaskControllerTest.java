package ru.study.tasklist.rest.task;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import ru.study.tasklist.config.MappersConfig;
import ru.study.tasklist.model.domain.task.SubTask;
import ru.study.tasklist.model.domain.task.Task;
import ru.study.tasklist.model.domain.task.TaskStatus;
import ru.study.tasklist.service.task.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TaskController.class)
@Import(MappersConfig.class)
class TaskControllerTest {
    @MockBean
    private TaskService taskService;

    @MockBean
    private TransactionTemplate transactionTemplate;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        //noinspection ConstantConditions
        when(transactionTemplate.execute(any())).thenAnswer(answer -> ((TransactionCallback<?>)answer.getArgument(0)).doInTransaction(null));
    }

    @Nested
    class Create {

        @Test
        void createWithResponse() throws Exception {
            when(taskService.create(ArgumentMatchers.argThat(task -> {
                Assertions.assertThat(task)
                        .hasFieldOrPropertyWithValue("name", "Задача");
                return true;
            }))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));


            mockMvc.perform(post("/task?withResult=true")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "name": "Задача",
                            "description": "Описание",
                            "subTasks": [
                                {
                                    "name": "Подзадача"
                                }
                            ]
                        }""")
                )
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Задача"))
                .andExpect(jsonPath("$.description").value("Описание"))
                .andExpect(jsonPath("$.subTasks[0].id").exists())
                .andExpect(jsonPath("$.subTasks[0].name").value("Подзадача"))
                .andReturn()
            ;
        }

        @Test
        void createWithoutResponse() throws Exception {
            when(taskService.create(ArgumentMatchers.argThat(task -> {
                Assertions.assertThat(task)
                        .hasFieldOrPropertyWithValue("name", "Задача");
                return true;
            }))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));


            mockMvc.perform(post("/task?withResult=false")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "name": "Задача",
                            "description": "Описание",
                            "subTasks": [
                                {
                                    "name": "Подзадача"
                                }
                            ]
                        }""")
                )
                .andExpect(status().is(201))
                .andExpect(header().string("Location", containsString("/task/")))
                .andExpect(content().bytes(new byte[0]))
                .andReturn()
            ;
        }
    }

    @Nested
    class ReadById {

        @Test
        void readExists() throws Exception {

            final Task task = new Task(
                    UUID.fromString("f71fd70e-618d-46a2-ab77-d1b6da30b992"),
                    1,
                    LocalDateTime.of(2022, 1, 1, 15, 16, 2),
                    LocalDateTime.of(2022, 1, 1, 15, 16, 2),
                    "Задача",
                    "Описание",
                    TaskStatus.NEW,
                    null,
                    false,
                    List.of(new SubTask(UUID.fromString("11111111-618d-46a2-ab77-d1b6da30b992"), 0, "Подзадача", false)));


            when(taskService.findById(UUID.fromString("f71fd70e-618d-46a2-ab77-d1b6da30b992")))
                .thenReturn(Optional.of(task));

            mockMvc.perform(get("/task/f71fd70e-618d-46a2-ab77-d1b6da30b992")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                {
                                    "name": "Задача",
                                    "description": "Описание",
                                    "subTasks": [
                                        {
                                            "name": "Подзадача"
                                        }
                                    ]
                                }""")
                    )
                    .andExpect(status().is(200))
                    .andExpect(content().json("""
                                {
                                    "name": "Задача",
                                    "description": "Описание",
                                    "subTasks": [
                                        {
                                            "name": "Подзадача"
                                        }
                                    ]
                                }"""))
                    .andReturn()
            ;
        }

        @Test
        void readAndNotFound() throws Exception {
            when(taskService.findById(UUID.fromString("f71fd70e-618d-46a2-ab77-d1b6da30b992")))
                    .thenReturn(Optional.empty());

            mockMvc.perform(get("/task/f71fd70e-618d-46a2-ab77-d1b6da30b992"))
                    .andExpect(status().is(404))
                    .andReturn()
            ;
        }
    }
}