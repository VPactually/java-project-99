package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateDTO {

    private JsonNullable<Integer> index;

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;

    @NotBlank
    private JsonNullable<String> title;

    @NotBlank
    private JsonNullable<String> content;

    @NotNull
    private JsonNullable<String> status;

    private JsonNullable<Set<Long>> taskLabelIds;

    public TaskUpdateDTO(String title, String content, String status) {
        this.title = JsonNullable.of(title);
        this.content = JsonNullable.of(content);
        this.status = JsonNullable.of(status);
    }
}
