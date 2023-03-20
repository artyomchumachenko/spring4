package ru.study.tasklist.model.domain.task.events;

import ru.study.tasklist.model.domain.task.Task;

public interface TaskEvent {
    Task task();
}
