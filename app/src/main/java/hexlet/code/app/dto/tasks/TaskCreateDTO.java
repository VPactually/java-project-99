package hexlet.code.app.dto.tasks;

import hexlet.code.app.model.Label;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
