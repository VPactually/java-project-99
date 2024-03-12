package hexlet.code.dto.taskStatuses;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusCreateDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String slug;
}
