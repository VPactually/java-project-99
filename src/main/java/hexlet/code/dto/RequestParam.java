package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestParam {
    private String order;
    private int page;
    private int perPage;
    private String sort;
}
