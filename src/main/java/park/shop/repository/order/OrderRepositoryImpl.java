package park.shop.repository.order;


import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import park.shop.domain.order.Orders;

import javax.persistence.EntityManager;
import java.util.Optional;

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
    public Optional<Orders> findByMerchantUid(String merchantUid) {
        return Optional.empty();
    }
}
