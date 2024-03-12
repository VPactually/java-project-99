package hexlet.code.dto.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    private Set<Long> taskLabelIds;
}
