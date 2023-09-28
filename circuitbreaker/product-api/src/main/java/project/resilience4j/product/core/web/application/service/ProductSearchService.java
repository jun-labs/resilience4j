package project.resilience4j.product.core.web.application.service;

import static project.resilience4j.common.codeandmessage.product.ProductErrorCodeAndMessage.PRODUCT_NOT_FOUND;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.resilience4j.product.core.domain.product.entity.Product;
import project.resilience4j.product.core.domain.product.repository.ProductRepository;
import project.resilience4j.product.core.web.application.ProductSearchUseCase;
import project.resilience4j.product.core.web.exception.ProductNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSearchService implements ProductSearchUseCase {

    private final ProductRepository productRepository;

    @Override
    public Product findProductById(Long productId) {
        log.info("[Product/Service]----xx> Search Request: {}", productId);
        Product findProduct = productRepository.findById(productId);
        if (findProduct == null) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        return productRepository.findById(productId);
    }
}
