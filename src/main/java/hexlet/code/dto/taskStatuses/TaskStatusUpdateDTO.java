package hexlet.code.dto.taskStatuses;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskStatusUpdateDTO {

    @NotBlank
    private JsonNullable<String> name;
    @NotBlank
    private JsonNullable<String> slug;
}
