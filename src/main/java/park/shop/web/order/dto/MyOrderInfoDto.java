package park.shop.web.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyOrderInfoDto {

    private Long id;

    private Long mainImageId;

    private String name;

    private Integer quantity;

    private String price;

    private String pg;

    private String createDt;
}
