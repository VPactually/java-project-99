package hexlet.code.app.dto.labels;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class LabelUpdateDTO {
    private JsonNullable<String> name;
}
