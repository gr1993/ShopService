package park.shop.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDto {
    private boolean success;
    private String message;
    private Object data;
    private Integer totalCount;
}
