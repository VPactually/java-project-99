package hexlet.code.app.services;

import hexlet.code.app.dto.users.UserCreateDTO;
import hexlet.code.app.dto.users.UserDTO;
import hexlet.code.app.dto.users.UserUpdateDTO;
import hexlet.code.app.exceptions.ResourceAlreadyExistsException;
import hexlet.code.app.exceptions.ResourceNotFoundException;
import hexlet.code.app.mappers.UserMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repositories.UserRepository;
import hexlet.code.app.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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


    public Page<User> getAll(Specification<User> spec, PageRequest pageRequest) {
        return repository.findAll(spec, pageRequest);

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
