package ru.study.tasklist.rest.task;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.study.tasklist.model.dto.task.TaskRequest;
import ru.study.tasklist.model.dto.task.TaskResponse;

import ru.study.tasklist.model.domain.task.Task;
import ru.study.tasklist.service.task.TaskService;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TransactionTemplate transactionTemplate;

    private final BiFunction<TaskRequest, Task, Task> taskRequestMapper;
    private final Function<Task, TaskResponse> taskResponseMapper;
    private final TaskService taskService;

    public TaskController(TransactionTemplate transactionTemplate,
                          @Qualifier("taskRequestMapper") BiFunction<TaskRequest, Task, Task> taskRequestMapper,
                          @Qualifier("taskResponseMapper") Function<Task, TaskResponse> taskResponseMapper,
                          TaskService taskService) {
        this.transactionTemplate = transactionTemplate;
        this.taskRequestMapper = taskRequestMapper;
        this.taskResponseMapper = taskResponseMapper;
        this.taskService = taskService;
    }

    @SuppressWarnings("ConstantConditions")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody TaskRequest taskRequest,
                                    @RequestParam(defaultValue = "false") boolean withResult,
                                    UriComponentsBuilder uriComponentsBuilder) {

        var task = transactionTemplate.execute((status) -> taskService.create(taskRequestMapper.apply(taskRequest, new Task())));
        if (withResult) {
            return ResponseEntity.status(201).body(taskResponseMapper.apply(task));
        } else {
            return ResponseEntity.created(uriComponentsBuilder.path("/task/{taskId}").build(task.getId()))
                    .build();
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> read(@PathVariable UUID taskId) {
        return ResponseEntity.of(taskService.findById(taskId).map(taskResponseMapper));
    }

    @SuppressWarnings("ConstantConditions")
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> update(@PathVariable UUID taskId,
                                               @RequestBody TaskRequest taskRequest,
                                               @RequestParam(defaultValue = "false") boolean withResult
                                               ) {
        var task = transactionTemplate.execute((status) -> {
                    var t = taskService.findById(taskId);
                    if (t.isEmpty()) {
                        return t;
                    }
                    return Optional.of(taskRequestMapper.apply(taskRequest, t.get())).map(taskService::update);
                }
        );
        if (withResult) {
            return ResponseEntity.of(task.map(taskResponseMapper));
        } else {
            return task.isPresent()? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        }
    }

    @SuppressWarnings("ConstantConditions")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> delete(@PathVariable UUID taskId) {
        var task = transactionTemplate.execute((status) -> taskService.findById(taskId).map(taskService::delete));
        return task.isPresent()? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

//    @PutMapping("/{taskId}/sub-task/{subId}/{subStatus}")
//    ResponseEntity<TaskResponse> checkSubTask(@PathVariable UUID taskId, @PathVariable String subId, @PathVariable String subStatus) {
//        var task = transactionTemplate.execute((status) -> taskService.findById(taskId)
//                .map(task -> taskService.subStatus(task, subId, subStatus)));
//        return task.isPresent()? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
//    }

}
