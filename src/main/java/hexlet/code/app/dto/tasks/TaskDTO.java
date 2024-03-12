package hexlet.code.app.dto.tasks;


import lombok.Getter;
import lombok.Setter;


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
