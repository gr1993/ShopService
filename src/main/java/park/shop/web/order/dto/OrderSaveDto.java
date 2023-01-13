package park.shop.web.order.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderSaveDto {
    private Long productId; //주문 상품

    private Integer amount; //총 결제 금액 : 금액(또는 할인금액) x 수량

    private Integer quantity; //주문 수량

    private String merchantUid; //가맹점 주문번호 (고유값)
    
    private String pg; //PG사 종류
}
