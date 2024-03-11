package hexlet.code.app.controllers;

import hexlet.code.app.dto.labels.LabelCreateDTO;
import hexlet.code.app.dto.labels.LabelDTO;
import hexlet.code.app.dto.labels.LabelUpdateDTO;
import hexlet.code.app.services.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelsController {

    @Autowired
    private LabelService labelService;

    @GetMapping
    private ResponseEntity<List<LabelDTO>> index() {
        var result = labelService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping("/{id}")
    private LabelDTO show(@PathVariable Long id) {
        return labelService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private LabelDTO create(@RequestBody LabelCreateDTO labelCreateDTO) {
        return labelService.create(labelCreateDTO);
    }

    @PutMapping("/{id}")
    private LabelDTO update(@PathVariable Long id, @RequestBody LabelUpdateDTO labelUpdateDTO) {
        return labelService.update(labelUpdateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void destroy(@PathVariable Long id) {
        labelService.delete(id);
    }
}
