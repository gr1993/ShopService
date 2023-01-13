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
import park.shop.web.order.dto.OrderSaveDto;
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

        String merChantUid = orderRepository.findMaxByMerchantUidLike(MerchantUidUtil.getTodayMerchantUid());
        if (merChantUid == null) {
            result.setMerchantUid(MerchantUidUtil.getFirstMerchantUid());
        } else {
            result.setMerchantUid(MerchantUidUtil.getNextMerchantUid(merChantUid));
        }

        result.setMemberName(member.getName());
        result.setMemberAddress(member.getAddress());
        return result;
    }

    public void saveOrder(Member member, OrderSaveDto orderSaveDto) {
        Product orderedProduct = productRepository.findById(orderSaveDto.getProductId()).orElse(null);
        if (orderedProduct == null) {
            throw new IllegalArgumentException();
        }

        Orders order = new Orders();
        order.setMember(member);
        order.setProduct(orderedProduct);
        order.setPrice(orderSaveDto.getAmount());
        order.setQuantity(orderSaveDto.getQuantity());
        order.setIsPayment("Y");
        order.setPg(orderSaveDto.getPg());
        order.setMerchantUid(orderSaveDto.getMerchantUid());

        orderRepository.save(order);
    }
}
