package park.shop.repository.product;

import park.shop.domain.product.Product;

import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);
}
