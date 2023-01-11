package park.shop.web.product.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ProductRegisterDto {
    @NotBlank
    @Size(min = 1, max = 250)
    private String name;

    @NotNull
    private Integer price;

    private Integer salePrice;

    @NotNull
    private Integer quantity;

    private MultipartFile mainImage;

    private List<MultipartFile> descImageGroup;
}
