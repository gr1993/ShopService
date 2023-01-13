package park.shop.repository.order;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import park.shop.common.dto.Pageable;
import park.shop.domain.member.Member;
import park.shop.domain.order.Orders;
import park.shop.domain.order.QOrders;

import javax.persistence.EntityManager;
import java.util.List;
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

    @Override
    public List<Orders> findMyOrderAll(Member member, Pageable pageable) {
        return query.select(orders)
                .from(orders)
                .where(equalMemberId(member))
                .orderBy(
                        orders.createDt.desc()
                )
                .fetch();
    }

    @Override
    public Long findMyOrderAllCount(Member member) {
        return query.select(orders.count())
                .from(orders)
                .where(equalMemberId(member))
                .fetchFirst();
    }


    @Override
    public Optional<Orders> findById(Long id) {
        Orders findOrder = em.find(Orders.class, id);
        return Optional.ofNullable(findOrder);
    }

    @Override
    public void deleteById(Long id) {
        Orders findOrder = findById(id).orElse(null);
        if(findOrder != null) {
            em.remove(findOrder);
        }
    }

    private BooleanExpression equalMemberId(Member member) {
        if (member != null) {
            return orders.member.eq(member);
        }
        return null;
    }

    private BooleanExpression likeMerchantUid(String merchantUid) {
        if(StringUtils.hasText(merchantUid)) {
            return orders.merchantUid.like(merchantUid + "%");
        }
        return null;
    }
}
