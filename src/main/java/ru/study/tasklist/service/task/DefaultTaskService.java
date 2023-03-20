package ru.study.tasklist.service.task;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.study.tasklist.model.domain.task.Task;
import ru.study.tasklist.model.domain.task.events.GroupEvent;
import ru.study.tasklist.repository.task.TaskRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DefaultTaskService implements TaskService {
    private final TaskRepository repository;

    private final ApplicationEventPublisher eventPublisher;

    public DefaultTaskService(TaskRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @NonNull
    public Task create(Task task) {
        return repository.save(task);
    }

    @Override
    public Optional<Task> findById(UUID taskId) {
        return repository.findByIdAndArchiveIsFalse(taskId);
    }

    @Override
    @NonNull
    public Task update(Task task) {
        return repository.save(task);
    }

    @Override
    @NonNull
    public Task delete(Task task) {
        task.delete();
        repository.save(task);
        return task;
    }

    @Override
    public Task subStatus(Task task, String subId, String subStatus) {
        var event = task.ok(subId, subStatus);

        if (event instanceof GroupEvent group) {
            group.getEvents().forEach(eventPublisher::publishEvent);
        } else {
            eventPublisher.publishEvent(event);
        }

        return task;
    }
}
