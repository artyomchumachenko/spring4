package ru.study.tasklist.model.task;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.study.tasklist.model.domain.task.Task;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @Nested
    class DeleteTask {
        @Test
        void delete() {
            var task = new Task();

            task.delete();

            assertThat(task)
                .as("Признак удаленной записи должен быть проставлен")
                .hasFieldOrPropertyWithValue("archive", true)
            ;

        }
    }


}