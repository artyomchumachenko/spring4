package ru.study.tasklist.model.domain.task;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import ru.study.tasklist.model.domain.common.AbstractEntity;
import ru.study.tasklist.model.domain.task.events.GroupEvent;
import ru.study.tasklist.model.domain.task.events.OkEvent;
import ru.study.tasklist.model.domain.task.events.SilentEvent;
import ru.study.tasklist.model.domain.task.events.TaskEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table("tsk_task")
public class Task extends AbstractEntity {

    @CreatedDate
    protected LocalDateTime sysCreateDate;

    @LastModifiedDate
    protected LocalDateTime sysUpdateDate;


    /** Краткое описание задачи */
    protected String name;

    /** Подробное описание задачи */
    protected String description;

    /** Статус задачи */
    protected TaskStatus status;

    /** ID владельца файла */
    @Column("owner_id")
    protected UUID ownerId;

    /**  Запись удалена/отправлена в архив */
    protected boolean archive;

    /** Список подзадач */
    @MappedCollection(keyColumn = "order", idColumn = "task_id")
    protected final List<SubTask> subTasks = new ArrayList<>();

    public Task() {
        this.status = TaskStatus.NEW;
    }

    @PersistenceConstructor
    public Task(UUID id, int version, LocalDateTime sysCreateDate, LocalDateTime sysUpdateDate, String name, String description, TaskStatus status, UUID ownerId, boolean archive, Collection<SubTask> subTasks) {
        super(id, version);
        this.sysCreateDate = sysCreateDate;
        this.sysUpdateDate = sysUpdateDate;
        this.name = name;
        this.description = description;
        this.status = status;
        this.ownerId = ownerId;
        this.archive = archive;
        this.subTasks.addAll(subTasks);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", ownerId=" + ownerId +
                ", id=" + id +
                ", version=" + version +
                ", sysCreateDate=" + sysCreateDate +
                ", sysUpdateDate=" + sysUpdateDate +
                '}';
    }

    public void delete() {
        this.archive = true;
    }

    public TaskEvent ok(String subId, String subStatus) {

        if (subStatus.equals("ok")) {
            final var subTask = this.subTasks.get(Integer.valueOf(subId));
            subTask.ok = true;
            return new GroupEvent(new OkEvent(this, subTask)).register(new SilentEvent(this));
        }
        return new SilentEvent(this);
    }

    public String getIssue() {
        return "null";
    }
}
