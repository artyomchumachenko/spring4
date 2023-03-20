package ru.study.tasklist.model.dto.task;

import ru.study.tasklist.model.domain.task.TaskStatus;

import java.util.List;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        int version,
        String name,
        String description,
        TaskStatus status,
        List<SubTaskResponse> subTasks) {

}
