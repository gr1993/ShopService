package park.shop.repository.order;

import park.shop.common.dto.Pageable;
import park.shop.domain.member.Member;
import park.shop.domain.order.Orders;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Orders save(Orders order);

    String findMaxByMerchantUidLike(String merchantUid);

    List<Orders> findMyOrderAll(Member member, Pageable pageable);

    Long findMyOrderAllCount(Member member);

    Optional<Orders> findById(Long id);

    void deleteById(Long id);
}
