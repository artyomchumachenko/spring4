package ru.study.tasklist.model.dto.task;

import ru.study.tasklist.model.dto.common.EntityRequest;

import java.util.List;
import java.util.UUID;

public record TaskRequest(
        UUID id,
        int version,
        String name,
        String description,
        List<SubTaskRequest> subTasks) implements EntityRequest {
}
