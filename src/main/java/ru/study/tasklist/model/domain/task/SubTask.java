package ru.study.tasklist.model.domain.task;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;
import ru.study.tasklist.model.domain.common.AbstractEntity;

import java.util.UUID;

@Table("tsk_sub")
public class SubTask extends AbstractEntity {

    /** Описание подзадачи */
    protected String name;
    /** Отметка о выполнении */
    protected boolean ok;


    public SubTask() {
    }

    @PersistenceConstructor
    public SubTask(UUID id, int version, String name, boolean ok) {
        super(id, version);
        this.name = name;
        this.ok = ok;
    }
}
