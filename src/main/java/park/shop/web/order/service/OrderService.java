package park.shop.web.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import park.shop.common.dto.Pageable;
import park.shop.domain.member.Member;
import park.shop.domain.order.Orders;
import park.shop.domain.product.Product;
import park.shop.repository.member.MemberRepository;
import park.shop.repository.order.OrderRepository;
import park.shop.repository.product.ProductRepository;
import park.shop.repository.product.ProductUpdateDto;
import park.shop.web.order.dto.MyOrderInfoDto;
import park.shop.web.order.dto.OrderInfoDto;
import park.shop.web.order.dto.OrderSaveDto;
import park.shop.web.util.MerchantUidUtil;
import park.shop.web.util.formatter.LocalDateTimeFormatter;
import park.shop.web.util.formatter.NumberFormatter;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final PaymentService paymentService;

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
        
        //주문한 수량 만큼, 상품 수량 차감
        productRepository.updateQuantity(orderedProduct.getId(), orderedProduct.getQuantity() - order.getQuantity());
    }

    public List<MyOrderInfoDto> findMyOrders(Member member, Pageable pageable) {
        List<Orders> findOrders = orderRepository.findMyOrderAll(member, pageable);
        LocalDateTimeFormatter dateTimeFormatter = new LocalDateTimeFormatter();
        NumberFormatter numberFormatter = new NumberFormatter();

        return findOrders.stream()
                .map(order -> {
                    MyOrderInfoDto myOrderInfoDto = new MyOrderInfoDto();
                    myOrderInfoDto.setId(order.getId());
                    myOrderInfoDto.setMainImageId(order.getProduct().getMainImage().getId());
                    myOrderInfoDto.setName(order.getProduct().getName());
                    myOrderInfoDto.setQuantity(order.getQuantity());
                    myOrderInfoDto.setPrice(numberFormatter.print(order.getPrice(), Locale.KOREA) + "원");
                    myOrderInfoDto.setPg(order.getPg());
                    myOrderInfoDto.setCreateDt(dateTimeFormatter.print(order.getCreateDt(), Locale.KOREA));
                    return myOrderInfoDto;
                })
                .collect(Collectors.toList());
    }

    public Long findMyOrdersCount(Member member) {
        return orderRepository.findMyOrderAllCount(member);
    }

    public void cancelOrder(Member member, Long id) {
        try {
            Orders order = orderRepository.findById(id).orElse(null);
            if (order.getMember().getId() != member.getId()) {
                throw new IllegalArgumentException("주문한 사용자와 요청한 사용자가 일치하지 않습니다.");
            }

            String token = paymentService.getToken();
            paymentService.paymentCancel(token, order.getMerchantUid(), order.getPrice(), "구매자 취소 요청");

            orderRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
