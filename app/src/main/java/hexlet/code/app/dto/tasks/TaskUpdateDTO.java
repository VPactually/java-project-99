package hexlet.code.app.dto.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskUpdateDTO {

    private JsonNullable<Integer> index;

    @NotNull
    private JsonNullable<Long> assigneeId;

    @NotBlank
    private JsonNullable<String> title;

    @NotBlank
    private JsonNullable<String> content;

    @NotNull
    private JsonNullable<String> status;

}
