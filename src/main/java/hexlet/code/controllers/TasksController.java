package hexlet.code.controllers;

import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.tasks.TaskParamsDTO;
import hexlet.code.dto.tasks.TaskUpdateDTO;
import hexlet.code.services.TaskService;

import hexlet.code.specifications.TaskSpecification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskSpecification specification;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAll(
            TaskParamsDTO taskParamsDTO,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "1000") Integer pageSize) {
        var spec = specification.build(taskParamsDTO);
        var pageable = PageRequest.of(page - 1, pageSize);
        var result = taskService.getAll(spec, pageable);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(taskService.getAll().size()))
                .body(result);
    }

    @GetMapping("/{id}")
    public TaskDTO getById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        return taskService.create(taskCreateDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO updateById(
            @Valid @RequestBody TaskUpdateDTO taskStatusUpdateDTO,
            @PathVariable Long id
    ) {
        return taskService.update(taskStatusUpdateDTO, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroyById(@PathVariable Long id) {
        taskService.delete(id);
    }
}
