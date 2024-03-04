package hexlet.code.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreateDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    @NotNull
    @Size(min = 3)
    private String passwordDigest;
}
