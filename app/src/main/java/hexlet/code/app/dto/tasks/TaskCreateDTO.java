package hexlet.code.app.dto.tasks;

import hexlet.code.app.model.Label;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Label> labels = new ArrayList<>();
}
