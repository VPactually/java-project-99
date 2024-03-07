package hexlet.code.app.controllers;

import hexlet.code.app.dto.tasks.TaskCreateDTO;
import hexlet.code.app.dto.tasks.TaskDTO;
import hexlet.code.app.dto.tasks.TaskUpdateDTO;
import hexlet.code.app.services.TaskService;

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

    @GetMapping
    public ResponseEntity<List<TaskDTO>> index(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        var pageable = PageRequest.of(page - 1, pageSize);
        var result = taskService.getAll(pageable);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(taskService.getAll().size()))
                .body(result);
    }

    @GetMapping("/{id}")
    public TaskDTO show(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        return taskService.create(taskCreateDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public TaskDTO update(
            @Valid @RequestBody TaskUpdateDTO taskStatusUpdateDTO,
            @PathVariable Long id
    ) {
        return taskService.update(taskStatusUpdateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        taskService.delete(id);
    }
}
