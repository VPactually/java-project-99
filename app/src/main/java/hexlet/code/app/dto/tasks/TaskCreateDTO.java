package hexlet.code.app.dto.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {

    private Integer index;

    @NotNull
    private Long assignee_id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private String status;
}
