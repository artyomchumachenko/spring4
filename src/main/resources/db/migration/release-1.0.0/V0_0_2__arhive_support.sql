

alter table tsk_task add column archive boolean not null default false;
comment on column tsk_task.archive is 'Признак удаленности/архива';