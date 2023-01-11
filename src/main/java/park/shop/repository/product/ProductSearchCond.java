package park.shop.repository.product;

import lombok.Getter;
import lombok.Setter;
import park.shop.domain.member.Member;

@Getter
@Setter
public class ProductSearchCond {

    private Member member;

    private String name;

    private Integer price;

    private Integer quantity;
}
