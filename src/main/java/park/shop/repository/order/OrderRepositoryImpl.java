package park.shop.repository.order;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import park.shop.domain.order.Orders;
import park.shop.domain.order.QOrders;

import javax.persistence.EntityManager;
import java.util.Optional;

import static park.shop.domain.order.QOrders.*;

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Orders save(Orders order) {
        em.persist(order);
        return order;
    }

    @Override
    public String findMaxByMerchantUidLike(String merchantUid) {
        return query.select(orders.merchantUid.max())
                .from(orders)
                .where(likeMerchantUid(merchantUid))
                .fetchOne();
    }

    private BooleanExpression likeMerchantUid(String merchantUid) {
        if(StringUtils.hasText(merchantUid)) {
            return orders.merchantUid.like(merchantUid + "%");
        }
        return null;
    }
}
