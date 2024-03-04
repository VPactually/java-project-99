package hexlet.code.app.services;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.exceptions.ResourceAlreadyExistsException;
import hexlet.code.app.mappers.UserMapper;
import hexlet.code.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;

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

}
