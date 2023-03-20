package ru.study.tasklist.repository.task;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.study.tasklist.model.domain.task.Task;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends CrudRepository<Task, UUID> {

    Optional<Task> findByIdAndArchiveIsFalse(UUID taskId);

}
