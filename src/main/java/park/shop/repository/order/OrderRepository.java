package park.shop.repository.order;

import park.shop.domain.order.Orders;

import java.util.Optional;

public interface OrderRepository {

    Orders save(Orders order);

    Optional<Orders> findByMerchantUid(String merchantUid);
}
