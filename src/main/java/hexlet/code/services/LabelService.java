package hexlet.code.services;

import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.dto.labels.LabelDTO;
import hexlet.code.dto.labels.LabelUpdateDTO;
import hexlet.code.exceptions.ResourceAlreadyExistsException;
import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.mappers.LabelMapper;
import hexlet.code.repositories.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelRepository repository;

    @Autowired
    private LabelMapper mapper;

    public List<LabelDTO> getAll() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

    public LabelDTO create(LabelCreateDTO labelCreateDTO) {
        var label = mapper.map(labelCreateDTO);
        if (!repository.findAll().contains(label)) {
            repository.save(label);
        } else {
            throw new ResourceAlreadyExistsException("Resource already exists");
        }
        return mapper.map(label);
    }

    public LabelDTO findById(Long id) {
        return mapper.map(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found")));
    }

    public LabelDTO update(LabelUpdateDTO labelUpdateDTO, Long id) {
        var label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        mapper.update(labelUpdateDTO, label);
        repository.save(label);
        return mapper.map(label);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
