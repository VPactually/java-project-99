package hexlet.code.app.services;

import hexlet.code.app.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.app.dto.taskStatuses.TaskStatusDTO;
import hexlet.code.app.dto.taskStatuses.TaskStatusUpdateDTO;
import hexlet.code.app.exceptions.ResourceAlreadyExistsException;
import hexlet.code.app.exceptions.ResourceNotFoundException;

import hexlet.code.app.mappers.TaskStatusMapper;
import hexlet.code.app.repositories.TaskStatusRepository;
import hexlet.code.app.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository repository;

    @Autowired
    private TaskStatusMapper mapper;

    @Autowired
    private UserUtils userUtils;


    public List<TaskStatusDTO> getAll(PageRequest pageRequest) {
        return repository.findAll(pageRequest).map(mapper::map).toList();

    }
    public List<TaskStatusDTO> getAll() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

    public TaskStatusDTO create(TaskStatusCreateDTO taskStatusCreateDTO) {

        var taskStatus = mapper.map(taskStatusCreateDTO);
        if (!repository.findAll().contains(taskStatus)) {
            repository.save(taskStatus);
        } else {
            throw new ResourceAlreadyExistsException("Resource already exists");
        }
        return mapper.map(taskStatus);
    }

    public TaskStatusDTO findById(Long id) {
        return mapper.map(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found")));
    }

    public ResponseEntity<TaskStatusDTO> update(TaskStatusUpdateDTO taskStatusUpdateDTO, Long id) {
        var taskStatus = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        if (userUtils.checkUserPermission()) {
            mapper.update(taskStatusUpdateDTO, taskStatus);
            repository.save(taskStatus);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.map(taskStatus));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        }
    }

    public ResponseEntity<String> delete(Long id) {
        if (userUtils.checkUserPermission()) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User has been deleted");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission for it!");
        }
    }

}
