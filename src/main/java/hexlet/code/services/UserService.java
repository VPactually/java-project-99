package hexlet.code.services;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.exceptions.ResourceAlreadyExistsException;
import hexlet.code.exceptions.ResourceNotFoundException;
import hexlet.code.mappers.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;


    public Page<User> getAll(Specification<User> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    public List<UserDTO> getAll() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

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

    public UserDTO update(UserUpdateDTO userUpdateDTO, Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        mapper.update(userUpdateDTO, user);
        repository.save(user);
        return mapper.map(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
