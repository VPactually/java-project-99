package hexlet.code.app.services;


import hexlet.code.app.dto.tasks.TaskCreateDTO;
import hexlet.code.app.dto.tasks.TaskDTO;
import hexlet.code.app.dto.tasks.TaskUpdateDTO;
import hexlet.code.app.exceptions.ResourceAlreadyExistsException;
import hexlet.code.app.exceptions.ResourceNotFoundException;
import hexlet.code.app.mappers.TaskMapper;

import hexlet.code.app.repositories.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper mapper;

    public List<TaskDTO> getAll(PageRequest pageRequest) {
        return repository.findAll(pageRequest).map(mapper::map).toList();
    }

    public List<TaskDTO> getAll() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

    public TaskDTO create(TaskCreateDTO taskCreateDTO) {

        var task = mapper.map(taskCreateDTO);
        if (!repository.findAll().contains(task)) {
            repository.save(task);
        } else {
            throw new ResourceAlreadyExistsException("Resource already exists");
        }
        return mapper.map(task);
    }

    public TaskDTO findById(Long id) {
        return mapper.map(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found")));
    }

    public TaskDTO update(TaskUpdateDTO taskUpdateDTO, Long id) {
        var task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        mapper.update(taskUpdateDTO, task);
        repository.save(task);
        return mapper.map(task);

    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
