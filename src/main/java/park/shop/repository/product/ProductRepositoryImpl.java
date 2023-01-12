package park.shop.repository.product;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import park.shop.common.dto.Pageable;
import park.shop.domain.file.File;
import park.shop.domain.file.FileGroup;
import park.shop.domain.member.Member;
import park.shop.domain.product.Product;

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

    @Override
    public void update(Long productId, ProductUpdateDto updateDto) {
        Product findProduct = em.find(Product.class, productId);
        findProduct.setName(updateDto.getName());
        findProduct.setPrice(updateDto.getPrice());
        findProduct.setSalePrice(updateDto.getSalePrice());
        findProduct.setQuantity(updateDto.getQuantity());
        if (updateDto.getMainImage() != null) {
            findProduct.setMainImage(updateDto.getMainImage());
        }
        if (updateDto.getDescImageGroup() != null) {
            findProduct.setDescImageGroup(updateDto.getDescImageGroup());
        }
    }

    @Override
    public void deleteById(Long productId) {
        Product findProduct = findById(productId).orElse(null);

        if(findProduct != null) {
            File mainImage = findProduct.getMainImage();
            FileGroup fileGroup = findProduct.getDescImageGroup();

            //자식 엔티티 우선 삭제
            em.remove(findProduct);
            for (File descImage : fileGroup.getFiles()) {
                em.remove(descImage);
            }

            //부모 엔티티들 제거
            em.remove(mainImage);
            em.remove(fileGroup);
        }
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
