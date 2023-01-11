package park.shop.repository.product;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import park.shop.domain.member.Member;
import park.shop.domain.product.Product;
import park.shop.common.dto.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static park.shop.domain.product.QProduct.product;

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

    @Override
    public Optional<Product> findById(Long id) {
        Product product = em.find(Product.class, id);
        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> findAll(ProductSearchCond cond, Pageable pageable) {
        Member member = cond.getMember();
        String name = cond.getName();
        Integer price = cond.getPrice();
        Integer quantity = cond.getQuantity();

        return query
                .select(product)
                .from(product)
                .where(equalMemberId(member), likeName(name))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    private BooleanExpression equalMemberId(Member member) {
        if (member != null) {
            return product.member.eq(member);
        }
        return null;
    }

    private BooleanExpression likeName(String name) {
        if(StringUtils.hasText(name)) {
            return product.name.like("%" + name + "%");
        }
        return null;
    }
}
