package hexlet.code.services;

import hexlet.code.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatuses.TaskStatusDTO;
import hexlet.code.dto.taskStatuses.TaskStatusUpdateDTO;
import hexlet.code.exceptions.ResourceAlreadyExistsException;
import hexlet.code.exceptions.ResourceNotFoundException;

import hexlet.code.mappers.TaskStatusMapper;
import hexlet.code.repositories.TaskStatusRepository;
import hexlet.code.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

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

    public TaskStatusDTO update(TaskStatusUpdateDTO taskStatusUpdateDTO, Long id) {
        var taskStatus = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        mapper.update(taskStatusUpdateDTO, taskStatus);
        repository.save(taskStatus);
        return mapper.map(taskStatus);

    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
