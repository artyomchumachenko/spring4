package ru.study.tasklist.model.domain.task;

import ru.study.tasklist.model.dto.common.MapperFactory;
import ru.study.tasklist.model.dto.task.SubTaskRequest;
import ru.study.tasklist.model.dto.task.TaskRequest;

import java.util.function.BiFunction;

public class TaskRequestMapper implements BiFunction<TaskRequest, Task, Task> {
    private final MapperFactory mapperFactory;
    private final BiFunction<SubTaskRequest, SubTask, SubTask> subTaskRequestMapper;


    public TaskRequestMapper(MapperFactory mapperFactory, BiFunction<SubTaskRequest, SubTask, SubTask> subTaskRequestMapper) {
        this.mapperFactory = mapperFactory;
        this.subTaskRequestMapper = subTaskRequestMapper;
    }

    public Task apply(TaskRequest taskRequest, Task task) {
        task.name = taskRequest.name();
        task.description = taskRequest.description();

        mapperFactory.createCollectionMapper(task.subTasks, SubTask::new, subTaskRequestMapper)
            .accept(taskRequest.subTasks());
        return task;
    }
}
