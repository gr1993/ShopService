package park.shop.web.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import park.shop.domain.member.Member;
import park.shop.domain.order.Orders;
import park.shop.domain.product.Product;
import park.shop.repository.member.MemberRepository;
import park.shop.repository.order.OrderRepository;
import park.shop.repository.product.ProductRepository;
import park.shop.web.order.dto.OrderInfoDto;
import park.shop.web.util.MerchantUidUtil;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public OrderInfoDto getOrderInfo(Long memberId, Long productId, Integer orderQuantity) {
        Member member = memberRepository.findById(memberId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null) {
            return null;
        }


        OrderInfoDto result = new OrderInfoDto();
        result.setProductName(product.getName());

        if (product.getSalePrice() == null) {
            result.setAmount(product.getPrice() * orderQuantity);
        } else {
            result.setAmount(product.getSalePrice() * orderQuantity);
        }

        Orders findOrder = orderRepository.findByMerchantUid(MerchantUidUtil.getTodayMerchantUid()).orElse(null);
        if (findOrder == null) {
            result.setMerchantUid(MerchantUidUtil.getFirstMerchantUid());
        } else {
            result.setMerchantUid(MerchantUidUtil.getNextMerchantUid(findOrder.getMerchantUid()));
        }

        result.setMemberName(member.getName());
        result.setMemberAddress(member.getAddress());
        return result;
    }
}
