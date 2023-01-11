package park.shop.repository.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import park.shop.domain.product.Product;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class ProductRepositoryImpl implements ProductRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ProductRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Product save(Product product) {
        em.persist(product);
        return product;
    }
}
