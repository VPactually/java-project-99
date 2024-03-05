package hexlet.code.app.services;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.exceptions.ResourceAlreadyExistsException;
import hexlet.code.app.exceptions.ResourceNotFoundException;
import hexlet.code.app.mappers.UserMapper;
import hexlet.code.app.repositories.UserRepository;
import hexlet.code.app.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private UserUtils userUtils;

    public List<UserDTO> getAll() {
        var users = repository.findAll();
        return users.stream()
                .map(mapper::map)
                .toList();
    }
//    public List<UserDTO> getAll(PageParamsDTO params, @RequestParam(defaultValue = "1") int page) {
//        var spec = specBuilder.build(params);
//        return repository.findAll(spec, PageRequest.of(page - 1, 10))
//                .stream().map(mapper::map).toList();
//
//    }

    public UserDTO create(UserCreateDTO userCreateDTO) {
        var user = mapper.map(userCreateDTO);
        if (!repository.findAll().contains(user)) {
            repository.save(user);
        } else {
            throw new ResourceAlreadyExistsException("Resource already exists");
        }
        return mapper.map(user);
    }

    public UserDTO findById(Long id) {
        return mapper.map(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found")));
    }

    public ResponseEntity<UserDTO> update(UserUpdateDTO userUpdateDTO, Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        if (userUtils.getCurrentUser().getId() == id) {
            mapper.update(userUpdateDTO, user);
            repository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.map(user));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        }
    }

    public ResponseEntity<String> delete(Long id) {
        if (userUtils.getCurrentUser().getId() == id) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User has been deleted");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission for it!");
        }
    }
}
