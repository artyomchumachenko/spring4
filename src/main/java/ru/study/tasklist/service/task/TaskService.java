package ru.study.tasklist.service.task;

import org.springframework.lang.NonNull;
import ru.study.tasklist.model.domain.task.Task;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;


public interface TaskService {

    @NotNull Task create(Task task);

    Optional<Task> findById(UUID taskId);

    @NonNull
    Task update(Task task);

    @NonNull
    Task delete(Task task);

    Task subStatus(Task task, String subId, String subStatus);
}
