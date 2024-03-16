package hexlet.code.services;

import hexlet.code.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatuses.TaskStatusDTO;
import hexlet.code.dto.taskStatuses.TaskStatusUpdateDTO;

import hexlet.code.mappers.TaskStatusMapper;
import hexlet.code.repositories.TaskStatusRepository;
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

    public List<TaskStatusDTO> getAll(PageRequest pageRequest) {
        return repository.findAll(pageRequest).map(mapper::map).toList();

    }

    public List<TaskStatusDTO> getAll() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

    public TaskStatusDTO create(TaskStatusCreateDTO taskStatusCreateDTO) {

        var taskStatus = mapper.map(taskStatusCreateDTO);
        repository.save(taskStatus);
        return mapper.map(taskStatus);
    }

    public TaskStatusDTO findById(Long id) {
        return mapper.map(repository.findById(id)
                .orElseThrow());
    }

    public TaskStatusDTO update(TaskStatusUpdateDTO taskStatusUpdateDTO, Long id) {
        var taskStatus = repository.findById(id)
                .orElseThrow();
        mapper.update(taskStatusUpdateDTO, taskStatus);
        repository.save(taskStatus);
        return mapper.map(taskStatus);

    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
