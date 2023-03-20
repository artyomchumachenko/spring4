create table tsk_sub
(
    id              uuid              not null
        constraint tsk_sub_pk
            primary key,
    version         int default 1 not null,
    task_id         uuid
        constraint tsk_sub_tsk_task__fk
            references tsk_task,
    "order"         int               not null default 0,
    name            varchar(2000)     not null,
    ok              bool              not null

);

comment on table tsk_sub is 'Список действий, чтобы выполнить задачу(чек-лист)';
comment on column tsk_sub.id is 'Уникальный идентификатор';
comment on column tsk_sub.version is 'Версия изменений, для поддержки блокировок';
comment on column tsk_sub.task_id is 'FK на задачу';
comment on column tsk_sub."order" is 'Порядок подзадач в задаче';
comment on column tsk_sub.name is 'Краткое описание задачи';
comment on column tsk_sub.ok is 'Статус выполнения';

