package ru.study.tasklist.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.study.tasklist.model.dto.common.RequestListMapper;
import ru.study.tasklist.model.dto.task.TaskRequest;
import ru.study.tasklist.model.dto.task.TaskResponse;
import ru.study.tasklist.model.domain.task.SubTaskRequestMapper;
import ru.study.tasklist.model.domain.task.SubTaskResponseMapper;
import ru.study.tasklist.model.domain.task.Task;
import ru.study.tasklist.model.domain.task.TaskRequestMapper;
import ru.study.tasklist.model.domain.task.TaskResponseMapper;

import java.util.function.BiFunction;
import java.util.function.Function;

@Configuration
public class MappersConfig {


    @Bean
    @Qualifier("taskResponseMapper")
    public Function<Task, TaskResponse> taskResponseMapper() {
        return new TaskResponseMapper(new SubTaskResponseMapper());
    }

    @Bean
    @Qualifier("taskRequestMapper")
    public BiFunction<TaskRequest, Task, Task> taskRequestMapper() {
        return new TaskRequestMapper(RequestListMapper::new, new SubTaskRequestMapper());
    }

}
