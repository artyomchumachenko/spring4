
CREATE TYPE tsk_status AS ENUM (
 'NEW',
 'IN_WORK',
 'PAUSE',
 'FORGET_IT',
 'READY'
);

create table tsk_task
(
    id          uuid
        constraint task_pk
            primary key,
    -- технические поля
    version     int     default 0 not null,
    sys_create_date timestamp not null,
    sys_update_date timestamp not null,
    -- бизнесовые поля
    name        varchar(200)      not null,
    description text,
    status      tsk_status not null,
    owner_id    uuid
);

comment on table tsk_task is 'Список задач';
comment on column tsk_task.id is 'Уникальный идентификатор';
comment on column tsk_task.version is 'Версия изменений, для подержки блокировок';
comment on column tsk_task.name is 'Краткое описание задачи';
comment on column tsk_task.description is 'Подробное описание задачи';
comment on column tsk_task.status is 'Статус задачи';


