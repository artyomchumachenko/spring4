package ru.study.tasklist.model.dto.common;

import java.util.UUID;

public interface EntityRequest {
    UUID id();
    int version();
}
