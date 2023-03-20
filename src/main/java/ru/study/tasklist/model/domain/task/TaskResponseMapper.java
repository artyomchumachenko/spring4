package ru.study.tasklist.model.domain.task;

import ru.study.tasklist.model.dto.task.SubTaskResponse;
import ru.study.tasklist.model.dto.task.TaskResponse;

import java.util.function.Function;

public class TaskResponseMapper implements Function<Task, TaskResponse> {

    private final Function<SubTask, SubTaskResponse> subTaskResponseMapper;

    public TaskResponseMapper(Function<SubTask, SubTaskResponse> subTaskResponseMapper) {
        this.subTaskResponseMapper = subTaskResponseMapper;
    }

    @Override
    public TaskResponse apply(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getVersion(),
            task.name,
            task.description,
            task.status,
            task.subTasks.stream().map(subTaskResponseMapper).toList()
        );
    }
}
