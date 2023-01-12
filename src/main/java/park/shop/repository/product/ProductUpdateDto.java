package park.shop.repository.product;


import lombok.Getter;
import lombok.Setter;
import park.shop.domain.file.File;
import park.shop.domain.file.FileGroup;

@Getter
@Setter
public class ProductUpdateDto {
    private String name;
    private Integer price;
    private Integer salePrice;
    private Integer quantity;
    private File mainImage;
    private FileGroup descImageGroup;
}
