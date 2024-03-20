package hexlet.code.services;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.mappers.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;


    public Page<User> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<UserDTO> getAll() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

    public UserDTO create(UserCreateDTO userCreateDTO) {
        var user = mapper.map(userCreateDTO);
        repository.save(user);
        return mapper.map(user);
    }

    public UserDTO findById(Long id) {
        return mapper.map(repository.findById(id)
                .orElseThrow());
    }

    public UserDTO update(UserUpdateDTO userUpdateDTO, Long id) {
        var user = repository.findById(id)
                .orElseThrow();

        mapper.update(userUpdateDTO, user);
        repository.save(user);
        return mapper.map(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
