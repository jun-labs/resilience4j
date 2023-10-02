package project.resilience4j.product.core.domain.product.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import project.resilience4j.product.core.domain.product.entity.Product;

@Repository
public class ProductRepository {

    private final Map<Long, Product> factory;

    public ProductRepository() {
        factory = new HashMap<>();
        factory.put(1L, new Product(1L, "스터디 카페 정기 이용권", new BigDecimal(10000L)));
        factory.put(2L, new Product(2L, "스터디 카페 정기 이용권", new BigDecimal(15000L)));
        factory.put(3L, new Product(3L, "스터디 카페 정기 이용권", new BigDecimal(18000L)));
    }

    public Product findById(Long productId) {
        return factory.get(productId);
    }
}
