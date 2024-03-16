package hexlet.code.dto.taskStatuses;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@NoArgsConstructor
public class TaskStatusUpdateDTO {

    @NotBlank
    private JsonNullable<String> name;
    @NotBlank
    private JsonNullable<String> slug;

    public TaskStatusUpdateDTO(String name, String slug) {
        this.name = JsonNullable.of(name);
        this.slug = JsonNullable.of(slug);
    }
}
