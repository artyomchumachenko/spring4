package ru.study.tasklist.model.domain.task;

import ru.study.tasklist.model.dto.task.SubTaskRequest;


public class SubTaskRequestMapper implements java.util.function.BiFunction<SubTaskRequest, SubTask, SubTask> {

    @Override
    public SubTask apply(SubTaskRequest subTaskRequest, SubTask subTask) {

        subTask.name = subTaskRequest.name();
        return subTask;
    }
}
