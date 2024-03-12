package hexlet.code.app.dto.labels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelDTO {
    private Long id;
    private String name;
    private String createdAt;
}
