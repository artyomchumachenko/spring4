package ru.study.tasklist.model.domain.task.events;

import ru.study.tasklist.model.domain.task.Task;

public class SilentEvent implements TaskEvent {
    private final Task task;

    public SilentEvent(Task task) {
        this.task = task;
    }

    @Override
    public Task task() {
        return task;
    }
}
