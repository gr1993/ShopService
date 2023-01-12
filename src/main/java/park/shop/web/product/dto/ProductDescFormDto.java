package park.shop.web.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDescFormDto {
    private Long id;

    private String name;

    private String price;

    private String salePrice;

    private Integer quantity;

    private String createDt;

    private Long mainImageId;

    private Long[] descImageIds;
}
