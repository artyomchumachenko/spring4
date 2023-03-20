package ru.study.tasklist.model.domain.task.events;

import ru.study.tasklist.model.domain.task.Task;

import java.util.ArrayList;
import java.util.List;

public class GroupEvent implements TaskEvent {
    private final List<TaskEvent> events = new ArrayList<>();

    public GroupEvent(TaskEvent event) {
        this.events.add(event);
    }

    public GroupEvent register(TaskEvent taskEvent) {
        this.events.add(taskEvent);
        return this;
    }

    @Override
    public Task task() {
        return events.get(0).task();
    }

    public List<TaskEvent> getEvents() {
        return events;
    }
}
