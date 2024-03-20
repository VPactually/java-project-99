package hexlet.code.services;

import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.dto.labels.LabelDTO;
import hexlet.code.dto.labels.LabelUpdateDTO;
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
        return mapper.map(repository.findAll());
    }

    public LabelDTO create(LabelCreateDTO labelCreateDTO) {
        var label = mapper.map(labelCreateDTO);
        repository.save(label);
        return mapper.map(label);
    }

    public LabelDTO findById(Long id) {
        return mapper.map(repository.findById(id)
                .orElseThrow());
    }

    public LabelDTO update(LabelUpdateDTO labelUpdateDTO, Long id) {
        var label = repository.findById(id)
                .orElseThrow();
        mapper.update(labelUpdateDTO, label);
        repository.save(label);
        return mapper.map(label);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
