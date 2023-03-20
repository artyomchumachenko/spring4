package ru.study.tasklist.model.domain.common.exception;

import java.util.UUID;

/**
 * Сущность есть, но версия у нее другая
 */
public class EntityAlreadyModifiedException extends IllegalArgumentException {

    public EntityAlreadyModifiedException(UUID id, int storedVersion, int versionToModify) {
        super(String.format("Optimistic lock exception %s (exists %s, modify %s)",id, storedVersion, versionToModify));
    }
}
