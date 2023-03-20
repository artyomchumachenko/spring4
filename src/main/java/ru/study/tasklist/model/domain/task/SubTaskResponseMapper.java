package ru.study.tasklist.model.domain.task;

import ru.study.tasklist.model.dto.task.SubTaskResponse;

import java.util.function.Function;

public class SubTaskResponseMapper implements Function<SubTask, SubTaskResponse> {
    @Override
    public SubTaskResponse apply(SubTask subTask) {
        return new SubTaskResponse(subTask.getId(), subTask.getVersion(), subTask.name, subTask.ok);
    }
}
