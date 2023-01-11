package park.shop.web.product.dto;

import lombok.Getter;
import lombok.Setter;
import park.shop.domain.file.File;
import park.shop.domain.file.FileGroup;
import park.shop.domain.member.Member;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
public class ProductInfoDto {
    private Long mainImageId;

    private String name;

    private Integer price;

    private Integer salePrice;

    private Integer quantity;

    private String createDt;

    private String updateDt;
}
