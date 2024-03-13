package hexlet.code.dto.tasks;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
public class TaskDTO {

    private Long id;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private Integer index;

    private String title;

    private String content;

    private String status;

    private String createdAt;

    private Set<Long> taskLabelIds = new HashSet<>();

}
