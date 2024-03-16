package hexlet.code.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    @NotBlank
    private JsonNullable<String> firstName;

    @NotBlank
    private JsonNullable<String> lastName;

    @Email
    private JsonNullable<String> email;

    @NotNull
    @Size(min = 3)
    private JsonNullable<String> passwordDigest;

    public UserUpdateDTO(String firstName, String lastName) {
        this.firstName = JsonNullable.of(firstName);
        this.lastName = JsonNullable.of(lastName);

    }
}
