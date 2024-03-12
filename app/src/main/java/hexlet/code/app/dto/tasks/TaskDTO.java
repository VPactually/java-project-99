package hexlet.code.app.dto.tasks;


import hexlet.code.app.model.Label;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskDTO {

    private Long id;

    private Long assignee_id;

    private Integer index;

    private String title;

    private String content;

    private String status;

    private String createdAt;

}
