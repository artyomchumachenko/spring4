package ru.study.tasklist.model.domain.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AbstractEntity {
    @Id
    protected final UUID id;

    @Version
    protected int version;

    protected AbstractEntity(UUID id, int version) {
        this.id = id;
        this.version = version;
    }

    protected AbstractEntity() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }
}
