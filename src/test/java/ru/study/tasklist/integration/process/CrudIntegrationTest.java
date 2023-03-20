package ru.study.tasklist.integration.process;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;
import ru.study.tasklist.model.dto.task.SubTaskRequest;
import ru.study.tasklist.model.dto.task.TaskRequest;
import ru.study.tasklist.model.dto.task.TaskResponse;
import ru.study.tasklist.integration.AbstractIntegrationTest;
import ru.study.tasklist.model.domain.task.TaskStatus;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class CrudIntegrationTest extends AbstractIntegrationTest {

    @Test
    void crud() {
        String id;
        // CREATE
        {
            final var createRequest = new TaskRequest(
                    null,
                    0,
                    "Тестовая таска",
                    "Тестовое описание",
                    List.of(new SubTaskRequest(null, 0, "Подзадача"))
            );
            var createResponse = restTemplate.postForEntity("/task", createRequest, Void.class);

            assertThat(createResponse.getStatusCodeValue())
                .as("Статус ответа при создании сущности")
                .isEqualTo(201)
            ;


            final var location = createResponse.getHeaders().getLocation();
            var segments = UriComponentsBuilder.fromUri(location).build().getPathSegments();
            id = segments.get(segments.size() - 1);

            log.info("creating task with {} and id {}", location, id);
        }

        // READ
        {
            var readResponse = restTemplate.getForEntity("/task/{taskId}", TaskResponse.class, id);

            assertThat(readResponse.getStatusCodeValue())
                .as("Статус ответа при получении сущности")
                .isEqualTo(200)
            ;
            assertThat(readResponse.getBody())
                .hasFieldOrPropertyWithValue("id", UUID.fromString(id))
                .hasFieldOrPropertyWithValue("name", "Тестовая таска")
                .hasFieldOrPropertyWithValue("description", "Тестовое описание")
                .hasFieldOrPropertyWithValue("status", TaskStatus.NEW)
            ;
        }
        // UPDATE
        {
            final var updateRequest = new TaskRequest(
                    UUID.fromString(id),
                    1,
                    "Тестовая таска обновление",
                    "Тестовое описание обновление",
                    List.of(new SubTaskRequest(null, 0, "Подзадача"))
            );
            restTemplate.put("/task/{taskId}", updateRequest, id);

            var readResponse = restTemplate.getForEntity("/task/{taskId}", TaskResponse.class, id);

            assertThat(readResponse.getStatusCodeValue())
                    .as("Статус ответа при получении сущности")
                    .isEqualTo(200)
            ;
            assertThat(readResponse.getBody())
                    .hasFieldOrPropertyWithValue("id", UUID.fromString(id))
                    .hasFieldOrPropertyWithValue("name", "Тестовая таска обновление")
                    .hasFieldOrPropertyWithValue("description", "Тестовое описание обновление")
                    .hasFieldOrPropertyWithValue("status", TaskStatus.NEW)
            ;
        }

        // DELETE
        {
            restTemplate.delete("/task/{taskId}", id);

            var readResponse = restTemplate.getForEntity("/task/{taskId}", TaskResponse.class, id);

            assertThat(readResponse.getStatusCodeValue())
                    .as("Сущеность уже удалена")
                    .isEqualTo(404)
            ;
        }

    }

}
