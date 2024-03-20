package hexlet.code.dto.labels;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabelUpdateDTO {
    @Size(min = 3, max = 1000)
    private JsonNullable<String> name;

    public LabelUpdateDTO(String name) {
        this.name = JsonNullable.of(name);
    }
}
