package ru.study.tasklist.model.dto.task;

import java.util.UUID;

public record SubTaskResponse(
        UUID id,
        int version,
        String name,
        boolean ok) {
}
