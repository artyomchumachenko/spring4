package ru.study.tasklist.repository.task;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.study.tasklist.integration.AbstractRepositoryTest;
import ru.study.tasklist.model.domain.task.SubTask;
import ru.study.tasklist.model.domain.task.Task;
import ru.study.tasklist.model.domain.task.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TaskRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Test
    void create() {

        final var task = new Task(
            UUID.fromString("1aacea25-29aa-4349-96fa-ba66c1a5f21c"),
            0,
            LocalDateTime.of(2020, 1, 2, 3, 4, 5, 6),
            LocalDateTime.of(2020, 1, 2, 3, 4, 5, 6),
            "test create",
            "test create description",
            TaskStatus.NEW,
            null,
            true,
            List.of(new SubTask(
                    UUID.fromString("0aacea25-29aa-4349-96fa-ba66c1a5f21c"),
                    0,
                    "Подзадача",
                    true
                )
            )
        );
        taskRepository.save(task);

        var taskMap = jdbcTemplate.queryForMap("select * from tsk_task where id = :taskId::uuid", Map.of("taskId", "1aacea25-29aa-4349-96fa-ba66c1a5f21c"));

        assertThat(taskMap)
            .hasFieldOrPropertyWithValue("id", UUID.fromString("1aacea25-29aa-4349-96fa-ba66c1a5f21c"))
            .hasFieldOrPropertyWithValue("name", "test create")
            .hasFieldOrPropertyWithValue("description", "test create description")
            .hasFieldOrPropertyWithValue("status", "NEW")
            .hasFieldOrPropertyWithValue("archive", true)
            ;

        var subTaskMap = jdbcTemplate.queryForMap("select * from tsk_sub where id = :subTaskId::uuid", Map.of("subTaskId", "0aacea25-29aa-4349-96fa-ba66c1a5f21c"));
        assertThat(subTaskMap)
            .hasFieldOrPropertyWithValue("id", UUID.fromString("0aacea25-29aa-4349-96fa-ba66c1a5f21c"))
            .hasFieldOrPropertyWithValue("name", "Подзадача")
            .hasFieldOrPropertyWithValue("ok", true)
        ;

    }


    @Test
    @Sql(statements = {"""
        insert into tsk_task (id, sys_create_date, sys_update_date, name, description, status, archive)
        values ('1aacea25-29aa-4349-96fa-ba66c1a5f21e', '2022-05-06 10:23:54', '2022-01-06 10:23:54', 'test read', 'test read description', 'NEW', true)
        ;""","""
        insert into tsk_sub (id, task_id, "order", name, ok)
        values ('0aacea25-29aa-4349-96fa-ba66c1a5f21e', '1aacea25-29aa-4349-96fa-ba66c1a5f21e', 0, 'subtask read', true)
        ;"""})
    void read() {
        var readResult = taskRepository.findById(UUID.fromString("1aacea25-29aa-4349-96fa-ba66c1a5f21e"));

        assertThat(readResult.orElseThrow())
            .hasFieldOrPropertyWithValue("id", UUID.fromString("1aacea25-29aa-4349-96fa-ba66c1a5f21e"))
            .hasFieldOrPropertyWithValue("name", "test read")
            .hasFieldOrPropertyWithValue("description", "test read description")
            .hasFieldOrPropertyWithValue("status", TaskStatus.NEW)
            .hasFieldOrPropertyWithValue("archive", true)
        ;
    }

    @Test
    @Sql(statements = {"""
        insert into tsk_task (id, version, sys_create_date, sys_update_date, name, description, status)
        values ('1aacea25-29aa-4349-96fa-ba66c1a5f21a', 1, '2022-05-06 10:23:54', '2022-01-06 10:23:54', 'test read', 'test read description', 'NEW')
        ;""","""
        insert into tsk_sub (id, task_id, "order", name, ok)
        values ('0aacea25-29aa-4349-96fa-ba66c1a5f21a', '1aacea25-29aa-4349-96fa-ba66c1a5f21a', 0, 'subtask read', true)
        ;"""})
    void update() {
        final var task = new Task(
            UUID.fromString("1aacea25-29aa-4349-96fa-ba66c1a5f21a"),
            1,
            LocalDateTime.of(2020, 1, 2, 3, 4, 5, 6),
            LocalDateTime.of(2020, 1, 2, 3, 4, 5, 6),
            "test update",
            "test update description",
            TaskStatus.IN_WORK,
            null,
            true,
            List.of(new SubTask(
                    UUID.fromString("0aacea25-29aa-4349-96fa-ba66c1a5f21a"),
                    0,
                    "subtask read modify",
                    false
                )
            )
        );

        taskRepository.save(task);

        var taskMap = jdbcTemplate.queryForMap("select * from tsk_task where id = :taskId::uuid", Map.of("taskId", "1aacea25-29aa-4349-96fa-ba66c1a5f21a"));

        assertThat(taskMap)
                .hasFieldOrPropertyWithValue("id", UUID.fromString("1aacea25-29aa-4349-96fa-ba66c1a5f21a"))
                .hasFieldOrPropertyWithValue("name", "test update")
                .hasFieldOrPropertyWithValue("description", "test update description")
                .hasFieldOrPropertyWithValue("status", "IN_WORK")
                .hasFieldOrPropertyWithValue("archive", true)
        ;

        var subTaskMap = jdbcTemplate.queryForMap("select * from tsk_sub where id = :subTaskId::uuid", Map.of("subTaskId", "0aacea25-29aa-4349-96fa-ba66c1a5f21a"));

        assertThat(subTaskMap)
                .hasFieldOrPropertyWithValue("id", UUID.fromString("0aacea25-29aa-4349-96fa-ba66c1a5f21a"))
                .hasFieldOrPropertyWithValue("name", "subtask read modify")
                .hasFieldOrPropertyWithValue("ok", false)
        ;
    }

    @Test
    @Sql(statements = {"""
        insert into tsk_task (id, sys_create_date, sys_update_date, name, description, status)
        values ('1aacea25-29aa-4349-96fa-ba66c1a5f21d', '2022-05-06 10:23:54', '2022-01-06 10:23:54', 'test read', 'test read description', 'NEW')
        ;""","""
        insert into tsk_sub (id, task_id, "order", name, ok)
        values ('0aacea25-29aa-4349-96fa-ba66c1a5f21d', '1aacea25-29aa-4349-96fa-ba66c1a5f21d', 0, 'subtask read', true)
        ;"""})
    void delete() {

        taskRepository.deleteById(UUID.fromString("1aacea25-29aa-4349-96fa-ba66c1a5f21d"));



        assertThat(jdbcTemplate.queryForMap("select count(*) as c from tsk_task where id = :taskId::uuid",
                Map.of("taskId", "1aacea25-29aa-4349-96fa-ba66c1a5f21d")))
            .as("Ожидается, что запись удалится")
            .hasFieldOrPropertyWithValue("c", 0L)
        ;

        assertThat(jdbcTemplate.queryForMap("select count(*) as c from tsk_sub where id = :subTaskId::uuid",
                Map.of("subTaskId", "1aacea25-29aa-4349-96fa-ba66c1a5f21d")))
                .as("Ожидается, что вложенная запись удалится")
                .hasFieldOrPropertyWithValue("c", 0L)
        ;

    }

    @Nested
    class findByIdAndNotArchive {
        @Test
        @Sql(statements = {"""
            insert into tsk_task (id, sys_create_date, sys_update_date, name, description, status)
            values ('1aacea25-29aa-4349-96fa-ba66c1a5f2aa', '2022-05-06 10:23:54', '2022-01-06 10:23:54', 'test read', 'test read description', 'NEW')
            ;"""})
        void findOk() {
            var findResult = taskRepository.findByIdAndArchiveIsFalse(UUID.fromString("1aacea25-29aa-4349-96fa-ba66c1a5f2aa"));

            assertThat(findResult.orElseThrow())
                    .hasFieldOrPropertyWithValue("id", UUID.fromString("1aacea25-29aa-4349-96fa-ba66c1a5f2aa"))
                    .hasFieldOrPropertyWithValue("archive", false)
            ;
        }

        @Test
        @Sql(statements = {"""
            insert into tsk_task (id, sys_create_date, sys_update_date, name, description, status, archive)
            values ('1aacea25-29aa-4349-96fa-ba66c1a5f2a1', '2022-05-06 10:23:54', '2022-01-06 10:23:54', 'test read', 'test read description', 'NEW', false)
            ;"""})
        void findNotFound() {
            var findResult = taskRepository.findByIdAndArchiveIsFalse(UUID.fromString("1aacea25-29aa-4349-96fa-ba66c1a5f2a0"));

            assertThat(findResult).isEmpty();
        }

        @Test
        @Sql(statements = {"""
            insert into tsk_task (id, sys_create_date, sys_update_date, name, description, status, archive)
            values ('1aacea25-29aa-4349-96fa-ba66c1a5f2a2', '2022-05-06 10:23:54', '2022-01-06 10:23:54', 'test read', 'test read description', 'NEW', true)
            ;"""})
        void findButArchive() {
            var findResult = taskRepository.findByIdAndArchiveIsFalse(UUID.fromString("1aacea25-29aa-4349-96fa-ba66c1a5f2a2"));

            assertThat(findResult).isEmpty();
        }
    }


}