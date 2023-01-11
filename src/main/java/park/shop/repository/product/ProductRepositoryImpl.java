package park.shop.repository.product;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
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

import static org.springframework.data.jpa.domain.Specification.where;
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
        JPAQuery<Product> queryUtilFrom = query
                .select(product)
                .from(product);

        JPAQuery<Product> queryUtilWhere = setWhereInQuery(queryUtilFrom, cond);

        return queryUtilWhere
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(
                        product.createDt.desc()
                )
                .fetch();
    }

    @Override
    public Long findAllCount(ProductSearchCond cond) {
        JPAQuery<Long> queryUtilFrom = query
                .select(product.count())
                .from(product);

        JPAQuery<Long> queryUtilWhere = setWhereInQuery(queryUtilFrom, cond);
        return queryUtilWhere.fetchFirst();
    }

    private <T> JPAQuery<T> setWhereInQuery(JPAQuery<T> queryUtilFrom, ProductSearchCond cond) {
        Member member = cond.getMember();
        String name = cond.getName();
        Integer price = cond.getPrice();
        Integer quantity = cond.getQuantity();

        return queryUtilFrom.where(
                equalMemberId(member),
                likeName(name),
                maxPrice(price),
                maxQuantity(quantity)
        );
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

    private BooleanExpression maxPrice(Integer maxPrice) {
        if(maxPrice != null) {
            return product.price.loe(maxPrice);
        }
        return null;
    }

    private BooleanExpression maxQuantity(Integer maxQuantity) {
        if(maxQuantity != null) {
            return product.quantity.loe(maxQuantity);
        }
        return null;
    }
}
