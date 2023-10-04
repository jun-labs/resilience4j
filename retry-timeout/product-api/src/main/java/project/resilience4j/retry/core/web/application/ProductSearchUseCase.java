package project.resilience4j.retry.core.web.application;

import project.resilience4j.retry.core.domain.product.entity.Product;

public interface ProductSearchUseCase {

    Product findProductById(Long productId);
}
