package hexlet.code.app.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private long id;

    private String firstName;

    private String lastName;

    private String email;
}
