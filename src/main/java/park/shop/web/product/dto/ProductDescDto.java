package park.shop.web.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDescDto {
    private Long id;

    private String name;

    private Integer price;

    private Integer salePrice;

    private Integer quantity;

    private Long mainImageId;
    private String mainImageName;

    private Long[] descImageIds;
    private String descImageNames;
}
