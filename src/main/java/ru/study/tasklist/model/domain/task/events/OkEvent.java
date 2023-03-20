package ru.study.tasklist.model.domain.task.events;

import ru.study.tasklist.model.domain.task.SubTask;
import ru.study.tasklist.model.domain.task.Task;

public class OkEvent implements TaskEvent {
    private final Task task;
    private final SubTask okSubTask;

    public OkEvent(Task task, SubTask subTask) {
        this.task = task;
        this.okSubTask = subTask;
    }

    @Override
    public Task task() {
        return task;
    }

    public SubTask getOkSubTask() {
        return okSubTask;
    }
}
