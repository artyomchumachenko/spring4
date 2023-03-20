package ru.study.tasklist.model.dto.task;

import ru.study.tasklist.model.dto.common.EntityRequest;

import java.util.UUID;

public record SubTaskRequest(
        UUID id,
        int version,
        String name) implements EntityRequest {
}
