package hexlet.code.app.dto.tasks;

import hexlet.code.app.model.Label;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Getter
@Setter
public class TaskUpdateDTO {

    private JsonNullable<Integer> index;

    @NotNull
    private JsonNullable<Long> assignee_id;

    @NotBlank
    private JsonNullable<String> title;

    @NotBlank
    private JsonNullable<String> content;

    @NotNull
    private JsonNullable<String> status;

    private JsonNullable<List<Label>> labels;

}
