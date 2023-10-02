package project.resilience4j.product.core.web.application;

import project.resilience4j.product.core.domain.product.entity.Product;

public interface ProductSearchUseCase {

    Product findProductById(Long productId);
}
