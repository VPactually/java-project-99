package hexlet.code.app.dto.users;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserParamsDTO {
    private String firstNameCont;
    private String lastNameCont;
    private LocalDate createdAtGt;
    private LocalDate createdAtLt;
}
