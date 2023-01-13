package park.shop.repository.product;

import park.shop.domain.product.Product;
import park.shop.common.dto.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll(ProductSearchCond cond, Pageable pageable);

    Long findAllCount(ProductSearchCond cond);

    void update(Long productId, ProductUpdateDto updateDto);

    void updateQuantity(Long productId, Integer quantity);

    void deleteById(Long productId);
}
