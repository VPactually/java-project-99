package hexlet.code.controllers;

import hexlet.code.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatuses.TaskStatusDTO;
import hexlet.code.dto.taskStatuses.TaskStatusUpdateDTO;
import hexlet.code.services.TaskStatusService;
import hexlet.code.utils.UserUtils;
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
@RequestMapping("/api/task_statuses")
public class TaskStatusesController {

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private UserUtils userUtils;

    @GetMapping
    public ResponseEntity<List<TaskStatusDTO>> index(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "1000") Integer pageSize) {
        var pageable = PageRequest.of(page - 1, pageSize);
        var result = taskStatusService.getAll(pageable);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(taskStatusService.getAll().size()))
                .body(result);
    }

    @GetMapping("/{id}")
    public TaskStatusDTO show(@PathVariable Long id) {
        return taskStatusService.findById(id);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@Valid @RequestBody TaskStatusCreateDTO taskStatusCreateDTO) {
        return taskStatusService.create(taskStatusCreateDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public TaskStatusDTO update(
            @Valid @RequestBody TaskStatusUpdateDTO taskStatusUpdateDTO,
            @PathVariable Long id
    ) {
        return taskStatusService.update(taskStatusUpdateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        taskStatusService.delete(id);
    }
}
