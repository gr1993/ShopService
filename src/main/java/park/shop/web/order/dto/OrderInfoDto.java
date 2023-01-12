package park.shop.web.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInfoDto {
    private String productName;

    private Integer amount; //총 결제 금액 : 금액(또는 할인금액) x 수량

    private String merchantUid; //가맹점 주문번호 (고유값)

    private String memberName; //구매자 이름
    
    private String memberAddress; //구매자 주소
}
