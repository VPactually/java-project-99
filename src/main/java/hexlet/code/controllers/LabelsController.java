package hexlet.code.controllers;

import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.dto.labels.LabelDTO;
import hexlet.code.dto.labels.LabelUpdateDTO;
import hexlet.code.services.LabelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelsController {

    @Autowired
    private LabelService labelService;

    @GetMapping
    private ResponseEntity<List<LabelDTO>> getAll() {
        var result = labelService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping("/{id}")
    private LabelDTO getById(@PathVariable Long id) {
        return labelService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private LabelDTO create(@RequestBody LabelCreateDTO labelCreateDTO) {
        return labelService.create(labelCreateDTO);
    }

    @PutMapping("/{id}")
    private LabelDTO updateById(@PathVariable Long id, @Valid @RequestBody LabelUpdateDTO labelUpdateDTO) {
        return labelService.update(labelUpdateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void destroyById(@PathVariable Long id) {
        labelService.delete(id);
    }
}
